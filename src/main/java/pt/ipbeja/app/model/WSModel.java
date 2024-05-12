package pt.ipbeja.app.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Game model
 * @author anonymized
 * @version 2024/04/14
 */
public class WSModel {

    // The following matrix could also be List<List<Character>>
    // for a more complex game, it should be a List<List<Cell>>
    // where Letter is a class with the letter and other attributes
    private final List<List<String>> lettersGrid;
    private WSView wsView;
    private Position previousButtonPosition;
    private final BoardContent boardContent;
    public WSModel(String boardContent) {
        this.boardContent = new BoardContent(boardContent);
        this.lettersGrid = new ArrayList<>();
        lettersGrid.add(new ArrayList<>());
        for(char c : boardContent.toCharArray()) {
            if (c == '\n') lettersGrid.add(new ArrayList<>());
            else lettersGrid.get(lettersGrid.size() - 1).add(c + "");
        }
    }

    public int nLines() { return this.lettersGrid.size(); }
    public int nCols() { return this.lettersGrid.get(0).size(); }

    public void registerView(WSView wsView) {
        this.wsView = wsView;
    }

    /**
     * Communicates selected position
     *
     * @param currentPosition The user's selected position
     */
    public void positionSelected(Position currentPosition) {

        if(this.previousButtonPosition != null){
           String word = this.checkWord(this.previousButtonPosition, currentPosition);
            System.out.println(this.wordFound(word));
        }

        this.previousButtonPosition = currentPosition;
    }

    /**
     * Checks the Word in the given interval position
     *
     * @param previousPosition The user's first selected position
     *
     * @param currentPosition The user's second selected position
     */
    private String checkWord(Position previousPosition, Position currentPosition){
        StringBuilder word = new StringBuilder();
        //Loop through the button interval
        for(int line = previousPosition.line(); line <= currentPosition.line(); line++){
            for(int col = previousPosition.col(); col <= currentPosition.col(); col++){
                word.append(this.textInPosition(new Position(line,col)));
                System.out.println("Line " + line + " col " + col + " word " + this.textInPosition(new Position(line,col)));
            }
        }
        // Reset previous position
        this.previousButtonPosition = null;
        return word.toString();
    }

    /**
     * Check if the word is in the board
     * @param word
     * @return true if the word is in the board
     */
    public String wordFound(String word) {
        String boardContent = this.boardContent.getBoardContent();

        if(boardContent.contains(word)){
            return "Match: " + word;
        }
        return "Not Match";
    }


    /**
     * Get the text in a position
     * @param position  position
     * @return  the text in the position
     */
    public String textInPosition(Position position) {
        return this.lettersGrid.get(position.line()).get(position.col());
    }

    /**
     * Check if all words were found
     * @return  true if all words were found
     */
    public boolean allWordsWereFound() {
        // TODO: implement this method
        return true;
    }



    /**
     * Check if the word with wildcard is in the board
     * @param word
     * @return  true if the word with wildcard is in the board
     */
    public String wordWithWildcardFound(String word) {
        // TODO implement this method
        return word;
    }
}
