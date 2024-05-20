package pt.ipbeja.app.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import pt.ipbeja.app.model.BoardContent;
import pt.ipbeja.app.model.WSModel;


public class WSMenu extends GridPane {
    private Stage stage;
    private TextField inputField; // Declare inputField at the class level

    public WSMenu(Stage stage) {
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
        buttonQuit.setOnAction(event -> showQuitConfirmation());


        this.add(buttonStart, 0, 0);
        this.add(buttonLeaderBoard, 0, 1);
        this.add(buttonQuit, 0, 2);


        inputField = new TextField();

        // Set padding and alignment
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
    }

    private void showSizeInputDialog() {
        // Create a dialog box for inputting size
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
        // Print the size for debugging purposes
        System.out.println("SIZE" + SIZE);

        // Initialize the board content and set the size of the board
        BoardContent board = new BoardContent(SIZE);
        board.setBoardContent();

        // Create the model with the board content
        WSModel wsModel = new WSModel(board.getBoardContent());

        // Create the WSBoard with the model
        WSBoard wsBoard = new WSBoard(wsModel);

        // Set the scene with the main layout from WSBoard which includes the board and the side panel
        this.stage.setScene(new Scene(wsBoard.getMainLayout()));

        // Register the WSBoard view with the model
        wsModel.registerView(wsBoard);

        // Request focus for WSBoard
        wsBoard.requestFocus();

        // Show the stage
        this.stage.show();
    }

    private void showQuitConfirmation() {
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
