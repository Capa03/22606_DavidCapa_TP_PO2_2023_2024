package pt.ipbeja.app.ui;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import pt.ipbeja.app.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Game interface. Just a GridPane of buttons. No images. No menu.
 * @author anonymized
 * @version 2024/04/14
 */
public class WSBoard extends GridPane implements WSView {
    private final WSModel wsModel;
    private static final int SQUARE_SIZE = 80;
    private final AtomicReference<Button> previousButton = new AtomicReference<>();
    private final Set<Position> foundWordPositions = new HashSet<>();
    /**
     * Create a board with letters
     */
    public WSBoard(WSModel wsModel) {
        this.wsModel = wsModel;
        this.buildGUI();
    }

    /**
     * Build the interface
     */
    private void buildGUI() {
        assert (this.wsModel != null);

        EventHandler<ActionEvent> actionEventHandler = event -> {
            Button button = (Button) event.getSource();
            Position buttonPosition = new Position(getRowIndex(button), getColumnIndex(button));

            if ("-fx-background-color: #FFFF00".equals(button.getStyle())) {
                if (!foundWordPositions.contains(buttonPosition)) {
                    button.setStyle("");
                }
                previousButton.set(null);
            } else {
                if (previousButton.get() != null) {
                    Position previousButtonPosition = new Position(getRowIndex(previousButton.get()), getColumnIndex(previousButton.get()));
                    if (!foundWordPositions.contains(previousButtonPosition)) {
                        previousButton.get().setStyle("");
                    }
                }
                button.setStyle("-fx-background-color: #FFFF00");
                previousButton.set(button);
            }

            wsModel.positionSelected(buttonPosition);
        };

        for (int line = 0; line < this.wsModel.nLines(); line++) {
            for (int col = 0; col < this.wsModel.nCols(); col++) {

                String textForButton = this.wsModel.textInPosition(new Position(line, col));
                Button button = new Button(textForButton);
                button.setOnAction(actionEventHandler);
                button.setMinWidth(SQUARE_SIZE);
                button.setMinHeight(SQUARE_SIZE);
                this.add(button, col, line); // add button to GridPane
            }
        }
        this.requestFocus();
    }


    /**
     * Can be optimized using an additional matrix with all the buttons
     * @param line line of label in board
     * @param col column of label in board
     * @return the button at line, col
     */
    public Button getButton(int line, int col) {
        ObservableList<Node> children = this.getChildren();
        for (Node node : children) {
            if(GridPane.getRowIndex(node) == line && GridPane.getColumnIndex(node) == col) {
                assert(node.getClass() == Button.class);
                return (Button)node;
            }
        }
        assert(false); // must not happen
        return null;
    }

    /**
     * Simply updates the text for the buttons in the received positions
     *
     * @param messageToUI the WS model
     */
    @Override
    public void update(MessageToUI messageToUI) {
        for (Position p : messageToUI.positions()) {
            //String s = this.wsModel.textInPosition(p);
            Button button = this.getButton(p.line(), p.col());
            button.setStyle("-fx-background-color: #00D100");
            button.setDisable(true);
            foundWordPositions.add(p);
        }
        if (this.wsModel.allWordsWereFound()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Level completed!");
            alert.showAndWait();
            System.exit(0);
        }
    }
}
