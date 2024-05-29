package pt.ipbeja.app.model;

import java.util.*;

/**
 * Game model
 * Handles the logic and state of the word search game.
 *
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

    /**
     * Constructs a WSModel instance with a specified BoardContent.
     *
     * @param boardContent the board content to use for the game.
     */
    public WSModel(BoardContent boardContent) {
        this.boardContent = boardContent;
        this.positions = new ArrayList<>();
        this.wordsFound = new ArrayList<>();
        this.lettersGrid = new ArrayList<>();
        lettersGrid.add(new ArrayList<>());

        for (char c : boardContent.getBoardContent().toCharArray()) {
            if (c == '\n') lettersGrid.add(new ArrayList<>());
            else lettersGrid.get(lettersGrid.size() - 1).add(c + "");
        }
    }

    /**
     * Returns the number of lines in the board.
     *
     * @return the number of lines.
     */
    public int nLines() {
        return this.lettersGrid.size();
    }

    /**
     * Returns the number of columns in the board.
     *
     * @return the number of columns.
     */
    public int nCols() {
        return this.lettersGrid.get(0).size();
    }

    /**
     * Registers a view to the model.
     *
     * @param wsView the view to register.
     */
    public void registerView(WSView wsView) {
        this.wsView = wsView;
    }

    /**
     * Communicates the selected position to the model.
     *
     * @param currentPosition the user's selected position.
     */
    public void positionSelected(Position currentPosition) {
        if (currentPosition == null) {

            this.previousButtonPosition = null;
            this.positions.clear();
            return;
        }



        if (this.previousButtonPosition != null) {
            String word = this.checkWord(this.previousButtonPosition, currentPosition);
            word = this.wordFound(word);
            this.previousButtonPosition = null;

            if (!word.equals("Not Match")) {
                wsView.update(new MessageToUI(this.positions, "match"));
            }

            this.positions.clear();
        } else {
            this.previousButtonPosition = currentPosition;
        }
    }

    /**
     * Checks the word in the given interval position.
     *
     * @param previousPosition the user's first selected position.
     * @param currentPosition  the user's second selected position.
     * @return the word formed by the interval.
     */
    private String checkWord(Position previousPosition, Position currentPosition) {
        StringBuilder word = new StringBuilder();
        int startLine = Math.min(previousPosition.line(), currentPosition.line());
        int endLine = Math.max(previousPosition.line(), currentPosition.line());
        int startCol = Math.min(previousPosition.col(), currentPosition.col());
        int endCol = Math.max(previousPosition.col(), currentPosition.col());

        // Loop through the button interval
        for (int line = startLine; line <= endLine; line++) {
            for (int col = startCol; col <= endCol; col++) {
                this.positions.add(new Position(line, col));
                word.append(this.textInPosition(new Position(line, col)));
            }
        }
        // Reset previous position
        this.previousButtonPosition = null;
        return word.toString();
    }

    /**
     * Returns the list of words found by the player.
     *
     * @return a list of words found.
     */
    public List<String> getWordsFound() {
        return this.wordsFound;
    }

    /**
     * Returns the list of solution words.
     *
     * @return a list of solution words.
     */
    public List<String> getSolutions() {
        return this.boardContent.getSolutions();
    }

    /**
     * Check if the word is in the board.
     *
     * @param word the word to check.
     * @return true if the word is in the board.
     */
    public String wordFound(String word) {
        List<String> solutions = getSolutions();

        // Check each solution
        for (String solution : solutions) {
            // Split the solution into individual words
            String[] words = solution.split("\\s+");
            // Check each word in the solution
            for (String w : words) {
                if (w.equals(word)) {
                    this.wordsFound.add(word);
                    StringBuilder message = new StringBuilder(word + " ");
                    for (Position p : this.positions) {
                        message.append(String.format("(%d, %c) ", p.line(), (char) ('A' + p.col())));
                    }
                    this.wsView.updateInfoLabel(message.toString());
                    return "Match: " + word;
                }
            }
        }
        return "Not Match";
    }

    /**
     * Returns the text at a given position.
     *
     * @param position the position to get the text from.
     * @return the text at the position.
     */
    public String textInPosition(Position position) {
        return this.lettersGrid.get(position.line()).get(position.col());
    }

    /**
     * Checks if all words have been found.
     *
     * @return true if all words have been found.
     */
    public boolean allWordsWereFound() {
        List<String> solutions = getSolutions();

        Set<String> allSolutionsWords = new HashSet<>();
        for (String solution : solutions) {
            allSolutionsWords.addAll(Arrays.asList(solution.split("\\s+")));
        }
        return this.wordsFound.size() == allSolutionsWords.size() &&
                allSolutionsWords.containsAll(this.wordsFound);
    }

    /**
     * Check if the word with wildcard is in the board.
     *
     * @param word the word to check with wildcard.
     * @return true if the word with wildcard is in the board.
     */
    public String wordWithWildcardFound(String word) {
        // TODO implement this method
        return word;
    }
}
