package pt.ipbeja.app.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import pt.ipbeja.app.model.BoardContent;
import pt.ipbeja.app.model.FileReadWrite;
import pt.ipbeja.app.model.WSModel;

public class WSMenu extends GridPane {
    private Stage stage;
    private TextField inputField;
    private FileReadWrite fileReadWrite;
    private WSBoard wsBoard;
    private BoardContent board;

    /**
     * Constructs a WSMenu with the given stage.
     *
     * @param stage the stage for the application
     */
    public WSMenu(Stage stage) {
        this.fileReadWrite = new FileReadWrite();
        this.stage = stage;
        this.buildUI();
    }

    /**
     * Setup Menu UI
     */
    private void buildUI() {
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setVgap(15);

        this.inputField = new TextField();
        inputField.setPromptText("Enter board size...");
        inputField.setMaxWidth(200);

        this.buttonSetup();
    }

    /**
     * Start Game
     *
     * @param SIZE size of the board
     */
    private void startGame(int SIZE) {
        board = new BoardContent(SIZE);
        board.setBoardContent();

        WSModel wsModel = new WSModel(board);

        WSBoard wsBoard = new WSBoard(wsModel);
        this.wsBoard = wsBoard;

        this.setupMenuBar();

        wsModel.registerView(wsBoard);

        wsBoard.requestFocus();

        this.stage.show();
    }

    /**
     * Setup Buttons UI
     */
    private void buttonSetup() {
        Button buttonStart = new Button("Start");
        Button buttonLeaderBoard = new Button("Leaderboard");
        Button buttonQuit = new Button("Quit");

        buttonStart.setPrefSize(200, 50);
        buttonLeaderBoard.setPrefSize(200, 50);
        buttonQuit.setPrefSize(200, 50);

        buttonStart.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        buttonLeaderBoard.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        buttonQuit.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

        buttonStart.setOnAction(event -> showSizeInputDialog());
        buttonQuit.setOnAction(event -> showQuitConfirmation(false));

        this.add(buttonStart, 0, 0);
        this.add(buttonLeaderBoard, 0, 1);
        this.add(buttonQuit, 0, 2);
    }

    /**
     * Show Input Box to get Size of the board
     */
    private void showSizeInputDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enter Size");
        alert.setHeaderText("Enter the size of the board");

        alert.getDialogPane().setContent(inputField);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String input = inputField.getText().trim(); // Trim leading and trailing whitespace
                if (!input.isEmpty() && input.matches("\\d+")) { // Check if input is not empty and contains only digits
                    startGame(Integer.parseInt(input));
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Invalid Input");
                    errorAlert.setContentText("Please enter a valid number for the size.");
                    errorAlert.showAndWait();
                    showSizeInputDialog();
                }
            }
        });
    }

    /**
     * Setup Menu Bar Menu in Top of the game
     */
    private void setupMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem saveMovements = new MenuItem("Save Movements");
        MenuItem restartMenuItem = new MenuItem("Restart");
        MenuItem quitMenuItem = new MenuItem("Quit");

        saveMovements.setOnAction(event -> saveMovements());
        restartMenuItem.setOnAction(event -> showSizeInputDialog());
        quitMenuItem.setOnAction(event -> showQuitConfirmation(true));

        gameMenu.getItems().addAll(saveMovements, restartMenuItem, quitMenuItem);
        menuBar.getMenus().add(gameMenu);

        // Create a BorderPane to hold the menu bar and the game content
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(wsBoard.getMainLayout());

        this.stage.setScene(new Scene(borderPane));
    }

    private void saveMovements() {
        this.fileReadWrite.writeFile(wsBoard.saveMovements(this.board.getBoardContent()), "movements", true);
    }

    /**
     * Show Quit Confirmation dialog
     *
     * @param save if true saves the score, if false just close the game
     */
    private void showQuitConfirmation(boolean save) {
        if (save) {
            this.fileReadWrite.writeFile(wsBoard.getWordsFound(), "score", true);
        }
        // Create a confirmation dialog for quitting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quit the game?");
        // Show the dialog box and wait for user input
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.exit(0); // Quit the application
            }
        });
    }
}
