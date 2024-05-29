package pt.ipbeja.po2.app.model;

import org.junit.jupiter.api.Test;
import pt.ipbeja.app.model.*;

import static org.junit.jupiter.api.Assertions.*;

class WSModelTest {

    @Test
    void testWordFound() {
        BoardContent mock = new BoardContent(5);
        mock.setBoardContent("ACCD\nEAGH\nCASA\nMAOP");
        WSModel model = new WSModel(mock);

        this.registerEmptyView(model);

        assertEquals("CASA", model.wordFound("CASA"));
    }

    @Test
    void testWordWithWildcardFound() {
        BoardContent mock = new BoardContent(5);
        mock.setBoardContent("MA*A\nEAGH\nISKL\nMSOP");
        WSModel model = new WSModel(mock);
        this.registerEmptyView(model);
        assertEquals("MALA", model.wordWithWildcardFound("MALA"));
    }

    @Test
    void testAllWordsWereFound() {
        BoardContent mock = new BoardContent(5);
        mock.setBoardContent("MALA");
        WSModel model = new WSModel(mock);
        this.registerEmptyView(model);
        assertEquals("MALA", model.wordFound("MALA"));
        //assertEquals("CA", model.wordFound("CA"));
        assertTrue(model.allWordsWereFound());
    }

    @Test
    void testReadFile() {
        FileReadWrite fileReadWrite = new FileReadWrite();
        String expectedContent = "CASA\nCAO\nGATO";
        String actualContent = fileReadWrite.readFile("mockFileContent");
        assertEquals(expectedContent, actualContent );
    }

    @Test
    void testWriteFile(){
        FileReadWrite fileReadWrite = new FileReadWrite();
        String writeToFile = "WRITED INTO FILE";
        fileReadWrite.writeFile(writeToFile,"mockWritedFile",false);
        String actualContent = fileReadWrite.readFile("mockWritedFile");
        assertEquals(writeToFile, actualContent );
    }

    @Test
    void testCheckWord() {
        MockBoardContent mock = new MockBoardContent(5);
        mock.setMockBoardContent("CASA\nCAO");
        WSModel wsModel = new WSModel(mock);
        String board = mock.getBoardContent();
        String casa = wsModel.checkWord(new Position(0,0),new Position(0,3));
        assertEquals("CASA", casa, "Expected casa: " + "Result: " + casa);
        String cao = wsModel.checkWord(new Position(1,0),new Position(1,2));
        assertEquals("CAO", cao, "Expected cao: " + "Result: " + cao);
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

class MockBoardContent extends BoardContent {
    private String boardContent;
    /**
     * Constructs a BoardContent instance with a specified board size.
     *
     * @param size the size of the board.
     */
    public MockBoardContent(int size) {
        super(size);
    }

    /**
     * Sets the board content by reading words from a file and generating the board.
     */
    public void setMockBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getBoardContent() {
        return boardContent;
    }
}


