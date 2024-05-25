package pt.ipbeja.app.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    public WSMenu(Stage stage) {
        this.fileReadWrite = new FileReadWrite();
        this.stage = stage;
        this.buildUI();
    }

    private void buildUI() {

        Button buttonStart = new Button("Start");
        Button buttonLeaderBoard = new Button("Leaderboard");
        Button buttonQuit = new Button("Quit");

        buttonStart.setPrefSize(200, 50);
        buttonLeaderBoard.setPrefSize(200, 50);
        buttonQuit.setPrefSize(200, 50);

        buttonStart.setOnAction(event -> showSizeInputDialog());
        buttonQuit.setOnAction(event -> showQuitConfirmation(false));

        this.add(buttonStart, 0, 0);
        this.add(buttonLeaderBoard, 0, 1);
        this.add(buttonQuit, 0, 2);

        inputField = new TextField();

        // Set padding and alignment
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
    }

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

    private void startGame(int SIZE) {

        board = new BoardContent(SIZE);
        board.setBoardContent();

        WSModel wsModel = new WSModel(board);

        WSBoard wsBoard = new WSBoard(wsModel);
        this.wsBoard = wsBoard;

        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem saveMovements = new MenuItem("Save Movements");
        MenuItem restartMenuItem = new MenuItem("Restart");
        MenuItem quitMenuItem = new MenuItem("Quit");

        saveMovements.setOnAction(event -> saveMovements());
        restartMenuItem.setOnAction(event -> showSizeInputDialog());
        quitMenuItem.setOnAction(event -> showQuitConfirmation(true));

        gameMenu.getItems().addAll(saveMovements,restartMenuItem, quitMenuItem);
        menuBar.getMenus().add(gameMenu);

        // Create a BorderPane to hold the menu bar and the game content
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(wsBoard.getMainLayout());

        this.stage.setScene(new Scene(borderPane));

        wsModel.registerView(wsBoard);

        wsBoard.requestFocus();

        this.stage.show();
    }

    private void saveMovements() {
        this.fileReadWrite.writeFile(wsBoard.saveMovements(this.board.getBoardContent()),"movements",true);
    }

    private void showQuitConfirmation(boolean save) {
        if(save){
            this.fileReadWrite.writeFile(wsBoard.getWordsFound(),"score",true);
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
