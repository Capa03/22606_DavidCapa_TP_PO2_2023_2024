package pt.ipbeja.app.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Start a game with a hardcoded board
 * @author David Capa
 * @version 2024/04/14
 */
public class StartWordSearch extends Application {

    @Override
    public void start(Stage primaryStage) {

        WSMenu WSMenu = new WSMenu(primaryStage);
        primaryStage.setScene(new Scene(WSMenu));

        primaryStage.show();
    }

    /**
     * @param args  not used
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
