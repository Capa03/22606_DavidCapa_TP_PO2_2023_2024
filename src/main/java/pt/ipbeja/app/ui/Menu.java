package pt.ipbeja.app.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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


public class Menu extends GridPane {
    private Stage stage;
    private TextField inputField; // Declare inputField at the class level

    public Menu(Stage stage) {
        this.stage = stage;
        this.buildUI();
    }

    private void buildUI() {
        // Create buttons
        Button buttonStart = new Button("Start");
        Button buttonLeaderBoard = new Button("Leaderboard");
        Button buttonQuit = new Button("Quit");

        // Set button sizes
        buttonStart.setPrefSize(200, 50);
        buttonLeaderBoard.setPrefSize(200, 50);
        buttonQuit.setPrefSize(200, 50);

        // Set event handlers
        buttonStart.setOnAction(event -> showSizeInputDialog());
        buttonQuit.setOnAction(event -> showQuitConfirmation());

        // Add buttons to grid
        this.add(buttonStart, 0, 0);
        this.add(buttonLeaderBoard, 0, 1);
        this.add(buttonQuit, 0, 2);

        // Create text field for size input
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
        //GOTO -> Board
        System.out.println("SIZE" + SIZE);

        BoardContent board = new BoardContent(SIZE);
        board.setBoardContent(); // Set the size of the board
        WSModel WSModel = new WSModel(board.getBoardContent());
        WSBoard WSBoard = new WSBoard(WSModel);
        this.stage.setScene(new Scene(WSBoard));
        WSModel.registerView(WSBoard);
        WSBoard.requestFocus();
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
