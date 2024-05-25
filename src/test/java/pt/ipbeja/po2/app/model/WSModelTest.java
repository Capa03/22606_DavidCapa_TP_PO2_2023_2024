package pt.ipbeja.po2.app.model;

import org.junit.jupiter.api.Test;
import pt.ipbeja.app.model.BoardContent;
import pt.ipbeja.app.model.MessageToUI;
import pt.ipbeja.app.model.WSModel;
import pt.ipbeja.app.model.WSView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WSModelTest {

    @Test
    void testWordFound() {
        BoardContent boardContent = new BoardContent();

        WSModel model = new WSModel(boardContent);

        this.registerEmptyView(model);

        assertEquals("CASA", model.wordFound("CASA"));
    }

    @Test
    void testWordWithWildcardFound() {
        BoardContent boardContent = new BoardContent();
        WSModel model = new WSModel(boardContent);
        this.registerEmptyView(model);
        assertEquals("MALA", model.wordWithWildcardFound("MALA"));
    }

    @Test
    void testallWordsWereFound() {
        BoardContent boardContent = new BoardContent();

        WSModel model = new WSModel(boardContent);
        this.registerEmptyView(model);
        assertEquals("MALA", model.wordFound("MALA"));
        assertEquals("CA", model.wordFound("CA"));
        assertTrue(model.allWordsWereFound());
    }




    private void registerEmptyView(WSModel model) {
        model.registerView(new WSView() {
            @Override
            public void update(MessageToUI message) {
                // do nothing
            }

            @Override
            public void updateInfoLabel(String text) {

            }

        });
    }
}

