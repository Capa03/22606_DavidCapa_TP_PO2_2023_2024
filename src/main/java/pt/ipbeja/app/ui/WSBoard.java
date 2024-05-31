package pt.ipbeja.app.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.ipbeja.app.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Game interface. Just a GridPane of buttons. No images. No menu.
 * @author David Capa
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
     * Create a board with letters
     */
    public WSBoard(WSModel wsModel) {
        this.wsModel = wsModel;
        this.buildGUI();
        this.setupInfoSidePane();
    }

    /**
     * Build the interface
     */
    private void buildGUI() {
        assert (this.wsModel != null);

        EventHandler<ActionEvent> actionEventHandler = event -> {
            Button button = (Button) event.getSource();
            handleButtonAction(button);
        };

        for (int line = 0; line < this.wsModel.nLines(); line++) {
            for (int col = 0; col < this.wsModel.nCols(); col++) {
                String textForButton = this.wsModel.textInPosition(new Position(line, col));
                Button button = new Button(textForButton);
                button.setFont(Font.font(18));
                button.setOnAction(actionEventHandler);
                button.setMinWidth(SQUARE_SIZE);
                button.setMinHeight(SQUARE_SIZE);
                button.setStyle("-fx-background-color: #FFFFFF;");
                this.add(button, col, line); // add button to GridPane
            }
        }
        this.setPadding(new Insets(10));
        this.setHgap(5);
        this.setVgap(5);
        this.requestFocus();
    }

    private void handleButtonAction(Button button) {
        Position buttonPosition = new Position(getRowIndex(button), getColumnIndex(button));
        Background background = button.getBackground();
        boolean isYellow = background != null && background.getFills().stream()
                .anyMatch(fill -> fill.getFill().equals(Color.YELLOW));

        if (isYellow) {
            button.setStyle("");
            wsModel.positionSelected(null);
            previousButton.set(null);
        } else {
            button.setStyle("-fx-background-color: #FFFF00");

            wsModel.positionSelected(buttonPosition);

            if (previousButton.get() != null) {
                Position previousButtonPosition = new Position(getRowIndex(previousButton.get()), getColumnIndex(previousButton.get()));
                String stylePrevious = foundWordPositions.contains(previousButtonPosition) ? "-fx-background-color: #00D100" : "";
                previousButton.get().setStyle(stylePrevious);
                String styleCurrent = foundWordPositions.contains(buttonPosition) ? "-fx-background-color: #00D100" : "";
                button.setStyle(styleCurrent);

                previousButton.set(null);
            } else {
                previousButton.set(button);
            }
        }
    }

    /**
     * Sets up the info side pane and adds it to the main layout.
     */
    private void setupInfoSidePane() {
        infoPanel.setPadding(new Insets(10));
        infoPanel.setSpacing(10);
        infoPanel.getChildren().add(movesTextArea); // Add the label to the info panel
        movesTextArea.setDisable(true);
        movesTextArea.setStyle("-fx-opacity: 1; -fx-font-size: 16px;");
        movesTextArea.setPrefHeight(300);
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
     * Simply updates the text for the buttons in the received positions
     *
     * @param messageToUI the WS model
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
            foundWordPositions.add(p);
        }
        if (this.wsModel.allWordsWereFound()) {
            FileReadWrite readWrite = new FileReadWrite();
            readWrite.writeFile(getWordsFound(), "score", true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Finish");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("Level completed!");
            alert.showAndWait();
            System.exit(0);
        }
    }

    public String saveMovements(String board) {
        StringBuilder movements = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        String boardContent = board;

        movements.append("Time: " + timestamp + "\n");
        movements.append("Board content: \n" + boardContent + "\n");
        movements.append("\nMovements: \n" + movesTextArea.getText() + "\n");

        return movements.toString();
    }

    public String getWordsFound() {
        StringBuilder sb = new StringBuilder();
        List<String> solutions = wsModel.getSolutions();
        Set<String> totalWords = new HashSet<>();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        for (String solution : solutions) {
            totalWords.addAll(Arrays.asList(solution.split("\\s+")));
        }
        List<String> wordsFound = this.wsModel.getWordsFound();
        int totalWordsCount = totalWords.size();
        int foundWordsCount = wordsFound.size();

        sb.append("Words found: " + "Time: " + timestamp + "\n");
        for (String word : wordsFound) {
            sb.append(word).append("\n");
        }

        double percentage = ((double) foundWordsCount / totalWordsCount) * 100;
        sb.append(String.format("Total words found: %d/%d (%.2f%%) \n", foundWordsCount, totalWordsCount, percentage));

        return sb.toString();
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
        mainLayout.setPadding(new Insets(10));
        return mainLayout;
    }

    @Override
    public void updateInfoLabel(String text) {
        movesTextArea.appendText(text + "\n");
    }
}
