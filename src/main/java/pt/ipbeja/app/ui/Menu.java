package pt.ipbeja.app.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import pt.ipbeja.app.model.WSModel;


public class Menu extends GridPane{
    private Stage stage;
    private final String boardContent;
    public Menu(Stage stage, String boardContent) {
        this.stage = stage;
        this.boardContent = boardContent;
        this.buildUI();
    }

    private void buildUI() {

        Button buttonStart = new Button();
        buttonStart.setText("Start");
        Button buttonLeaderBoard = new Button();
        buttonLeaderBoard.setText("Leaderboard");
        Button buttonQuit = new Button();
        buttonQuit.setText("Quit");

        buttonStart.setPrefSize(200, 50);
        buttonLeaderBoard.setPrefSize(200, 50);
        buttonQuit.setPrefSize(200, 50);

        EventHandler<ActionEvent> eventStart = event -> {
            Button button = (Button) event.getSource();
            switch (button.getText()) {
                case "Start":
                    //GOTO -> Board
                    WSModel WSModel = new WSModel(this.boardContent);
                    WSBoard WSBoard = new WSBoard(WSModel);
                    this.stage.setScene(new Scene(WSBoard));
                    WSModel.registerView(WSBoard);
                    WSBoard.requestFocus();
                    this.stage.show();
                    break;
                case "LeaderBoard":
                    //GOTO -> LeaderBoard View
                    break;
                case "Quit":
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            System.exit(0);
                        }
                    });
                    break;
            }
        };

        buttonQuit.setOnAction(eventStart);
        buttonStart.setOnAction(eventStart);
        buttonLeaderBoard.setOnAction(eventStart);

        this.add(buttonStart,0,0);
        this.add(buttonLeaderBoard,0,1);
        this.add(buttonQuit,0,2);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
    }

}
