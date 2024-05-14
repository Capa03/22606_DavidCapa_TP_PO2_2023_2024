package pt.ipbeja.app.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ipbeja.app.model.BoardContent;
import pt.ipbeja.app.model.Position;
import pt.ipbeja.app.model.WSModel;

/**
 * Start a game with a hardcoded board
 * @author anonymized
 * @version 2024/04/14
 */
public class StartWordSearch extends Application {

    @Override
    public void start(Stage primaryStage) {
        BoardContent board = new BoardContent();


        final String boardContent =
                """
                CASAIAED
                GALINHAI
                WIQFELAA
                OFLFGATO
                EFFAFFPP""";


        Menu menu = new Menu(primaryStage, board.getBoardContent());
        primaryStage.setScene(new Scene(menu));

        primaryStage.show();
    }

    /**
     * @param args  not used
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
