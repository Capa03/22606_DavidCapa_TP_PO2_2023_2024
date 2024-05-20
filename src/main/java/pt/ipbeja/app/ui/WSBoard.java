package pt.ipbeja.app.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.ipbeja.app.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Game interface. Just a GridPane of buttons. No images. No menu.
 * Displays the game board and an info panel beside it.
 *
 * @version 2024/04/14
 */

public class WSBoard extends GridPane implements WSView {

    private final WSModel wsModel;
    private static final int SQUARE_SIZE = 80;
    private final AtomicReference<Button> previousButton = new AtomicReference<>();
    private final Set<Position> foundWordPositions = new HashSet<>();
    private final VBox infoPanel = new VBox();
    private final TextArea movesTextArea = new TextArea();

    /**
     * Constructs a WSBoard with the given model.
     *
     * @param wsModel the model for the word search game
     */

    public WSBoard(WSModel wsModel) {
        this.wsModel = wsModel;
        this.buildGUI();
        this.setupInfoSidePane();
    }

    /**
     * Builds the user interface for the word search game.
     */

    private void buildGUI() {
        assert (this.wsModel != null);

        EventHandler<ActionEvent> actionEventHandler = event -> {
            Button button = (Button) event.getSource();
            Position buttonPosition = new Position(getRowIndex(button), getColumnIndex(button));
            Background background = button.getBackground();
            boolean isYellow = background != null && background.getFills().stream()
                    .anyMatch(fill -> fill.getFill().equals(Color.YELLOW));

            if (isYellow) {
                button.setStyle("");
                wsModel.positionSelected(null);
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
                wsModel.positionSelected(buttonPosition);
            }
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
     * Sets up the info side pane and adds it to the main layout.
     */
    private void setupInfoSidePane() {
        infoPanel.setPadding(new Insets(10));
        infoPanel.setSpacing(10);
        infoPanel.getChildren().add(movesTextArea); // Add the label to the info panel
        movesTextArea.setDisable(true);
    }

    /**
     * Retrieves the button at the specified position in the grid.
     *
     * @param line the row index of the button
     * @param col  the column index of the button
     * @return the button at the specified position
     */
    public Button getButton(int line, int col) {
        ObservableList<Node> children = this.getChildren();
        for (Node node : children) {
            if (GridPane.getRowIndex(node) == null || GridPane.getColumnIndex(node) == null) {
                continue;
            }
            if (GridPane.getRowIndex(node) == line && GridPane.getColumnIndex(node) == col) {
                assert (node instanceof Button);
                return (Button) node;
            }
        }
        return null;
    }

    /**
     * Updates the game board and info panel based on the message received from the model.
     *
     * @param messageToUI the message containing the updated positions and game state
     */
    @Override
    public void update(MessageToUI messageToUI) {
        for (Position p : messageToUI.positions()) {
            Button button = this.getButton(p.line(), p.col());
            if (button == null) {
                System.err.println("Button not found at position: " + p);
                continue;
            }
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

    @Override
    public void updateInfoLabel(String text) {
        movesTextArea.appendText(text + "\n");
    }

    /**
     * Returns the main layout containing the board and the info panel.
     *
     * @return the main layout
     */
    public HBox getMainLayout() {
        HBox mainLayout = new HBox();
        mainLayout.getChildren().addAll(this, infoPanel); // Add GridPane and infoPanel to HBox
        mainLayout.setSpacing(10);
        return mainLayout;
    }
}
