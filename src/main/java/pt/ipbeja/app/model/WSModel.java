package pt.ipbeja.app.model;


import java.util.*;

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
    private List<String> wordsFound;
    private WSView wsView;
    private Position previousButtonPosition;
    private final BoardContent boardContent;
    private List<Position> positions;

    public WSModel(String boardContent) {
        this.boardContent = new BoardContent();
        this.positions = new ArrayList<>();
        this.wordsFound = new ArrayList<>();
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
        if (currentPosition == null) {
            System.out.println("Position reset");
            this.previousButtonPosition = null;
            this.positions.clear();
            return;
        }
        System.out.println("LETTER " + textInPosition(currentPosition));
        if(this.previousButtonPosition != null ){

           String word = this.checkWord(this.previousButtonPosition, currentPosition);
           word = this.wordFound(word);
            System.out.println(word);
            if(!word.equals("Not Match")){
                wsView.update(new MessageToUI( this.positions,"match"));
            }
            this.positions.clear();
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
                this.positions.add(new Position(line, col));
                word.append(this.textInPosition(new Position(line,col)));
                //System.out.println("Line " + line + " col " + col + " word " + this.textInPosition(new Position(line,col)));
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
        List<String> solutions = boardContent.getSolutions().get("easy");

        // Check each solution
        for (String solution : solutions) {
            // Split the solution into individual words
            String[] words = solution.split("\\s+");

            // Check each word in the solution
            for (String w : words) {
                if (w.equals(word)) {
                    this.wordsFound.add(word);
                    return "Match: " + word;
                }
            }
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
        List<String> solutions = this.boardContent.getSolutions().get("easy");

        Set<String> allSolutionsWords = new HashSet<>();
        for (String solution : solutions) {
            allSolutionsWords.addAll(Arrays.asList(solution.split("\\s+")));
        }

        // Check if all words found are contained in the set of all solutions words
        return this.wordsFound.size() == allSolutionsWords.size() &&
                allSolutionsWords.containsAll(this.wordsFound);
    }

    /**
     * Check if the word with wildcard is in the board
     * @param word
     * @return true if the word with wildcard is in the board
     */
    public String wordWithWildcardFound(String word) {
        // TODO implement this method
        return word;
    }
}
