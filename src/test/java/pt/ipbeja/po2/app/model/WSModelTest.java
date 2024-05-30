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
        mock.setBoardContent("MALA\nCA");
        WSModel model = new WSModel(mock);
        this.registerEmptyView(model);
        assertEquals("MALA", model.wordFound("MALA"));
        assertEquals("CA", model.wordFound("CA"));
        assertTrue(model.allWordsWereFound());
    }


    @Test
    void testReadFile() {

        String mockFileContent = "CASA\nCAO\nGATO\nMECANICO";

        MockFileReadWrite fileReadWrite = new MockFileReadWrite();

        fileReadWrite.setFileContent(mockFileContent);

        // Read the mocked content
        String actualContent = fileReadWrite.readFile("mockFileContent");

        // Criação do mock do conteúdo do tabuleiro
        MockBoardContent mockBoardContent = new MockBoardContent(7);
        mockBoardContent.setMockBoardContent(actualContent);


        WSModel model = new WSModel(mockBoardContent);
        this.registerEmptyView(model);

        // Check if actualContent contains the words
        assertTrue(actualContent.contains("CASA"));
        assertTrue(actualContent.contains("CAO"));
        assertTrue(actualContent.contains("GATO"));
        assertTrue(actualContent.contains("MECANICO"));

        // Set and check if word as found
        model.wordFound("CASA");
        model.wordFound("CAO");
        model.wordFound("GATO");
        model.wordFound("MECANICO");

        assertTrue(model.allWordsWereFound());
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
        this.registerEmptyView(wsModel);
        String casa = wsModel.checkWord(new Position(0,0),new Position(0,3));
        assertEquals("CASA", casa, "Expected casa: " + "Result: " + casa);
        String cao = wsModel.checkWord(new Position(1,0),new Position(1,2));
        assertEquals("CAO", cao, "Expected cao: " + "Result: " + cao);
    }

    @Test
    void testFinishGame() {
        MockBoardContent mock = new MockBoardContent(5);
        mock.setMockBoardContent("CASA\nCAO");
        WSModel wsModel = new WSModel(mock);
        this.registerEmptyView(wsModel);
        String casa = wsModel.checkWord(new Position(0,0),new Position(0,3));
        assertEquals("CASA", casa, "Expected casa: " + "Result: " + casa);
        String cao = wsModel.checkWord(new Position(1,0),new Position(1,2));
        assertEquals("CAO", cao, "Expected cao: " + "Result: " + cao);
        assertTrue(wsModel.allWordsWereFound());
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
     * Sets the mock board content.
     *
     * @param boardContent the content to set.
     */
    public void setMockBoardContent(String boardContent) {
        this.boardContent = boardContent;
        setBoardContent(boardContent);
    }

    @Override
    public String getBoardContent() {
        return boardContent;
    }
}

class MockFileReadWrite extends FileReadWrite {
    private String mockFileContent;

    public void setFileContent(String content) {
        this.mockFileContent = content;
    }

    @Override
    public String readFile(String fileName) {
        if (mockFileContent != null) {
            return mockFileContent;
        }
        return super.readFile(fileName);
    }
}


