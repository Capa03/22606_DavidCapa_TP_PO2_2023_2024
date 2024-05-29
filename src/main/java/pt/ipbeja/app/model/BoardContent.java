package pt.ipbeja.app.model;

import java.util.*;

/**
 * The BoardContent class represents a board for placing words.
 * It generates a board of a specified size, places words from a file, and fills the remaining cells with random letters.
 */
public class BoardContent {

    private String boardContent;
    private final List<String> words;
    private final List<String> solutions;
    private final int size;
    private final FileReadWrite fileReadWrite;
    private static final char PLACEHOLDER = '-';
    private static final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * Constructs a BoardContent instance with a specified board size.
     *
     * @param size the size of the board.
     */
    public BoardContent(int size) {
        this.size = size;
        this.words = new ArrayList<>();
        this.solutions = new ArrayList<>();
        this.fileReadWrite = new FileReadWrite();
    }

    /**
     * Returns the list of solutions (placed words) on the board.
     *
     * @return a list of placed words.
     */
    public List<String> getSolutions() {
        return new ArrayList<>(this.solutions);
    }

    /**
     * Returns the string representation of the board content.
     *
     * @return the board content as a string.
     */
    public String getBoardContent() {
        return this.boardContent;
    }

    /**
     * Sets the board content by reading words from a file and generating the board.
     */
    public void setBoardContent(String boardContent) {
        this.boardContent = generateBoard(boardContent);
    }

    /**
     * Gets the number of columns in the board (same as rows for a square board).
     *
     * @return the number of columns.
     */
    private int getCols() {
        return this.size;
    }

    /**
     * Gets the number of rows in the board (same as columns for a square board).
     *
     * @return the number of rows.
     */
    private int getRows() {
        return this.size;
    }

    /**
     * Generates a board with the given words and size, placing words and filling empty cells with random letters.
     *
     * @param wordString the string containing words to place on the board.
     * @return the string representation of the generated board.
     */
    private String generateBoard(String wordString) {
        List<String> filteredWords = filterWordsBySize(wordString.split("\\s+"), getCols());
        List<String> selectedWords = selectRandomWords(filteredWords);
        List<List<Character>> board = initializeBoard(getRows(), getCols());
        List<String> placedWords = placeWordsOnBoard(board, selectedWords);

        fillEmptyCells(board);
        this.words.addAll(placedWords);
        this.solutions.addAll(this.words);

        return convertBoardToString(board);
    }

    /**
     * Filters words by the specified size, returning only those that fit within the board.
     *
     * @param words an array of words to filter.
     * @param maxSize the maximum size of a word.
     * @return a list of words that fit within the specified size.
     */
    private List<String> filterWordsBySize(String[] words, int maxSize) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() <= maxSize) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    /**
     * Selects a random subset of words from the filtered list.
     *
     * @param words the list of filtered words.
     * @return a randomly selected list of words.
     */
    private List<String> selectRandomWords(List<String> words) {
        List<String> selectedWords = new ArrayList<>();
        Random random = new Random();
        Set<String> selectedSet = new HashSet<>();

        while (selectedSet.size() < words.size()) {
            String word = words.get(random.nextInt(words.size()));
            if (!selectedSet.contains(word)) {
                selectedSet.add(word);
                selectedWords.add(word);
            }
        }

        return selectedWords;
    }

    /**
     * Initializes a 2D list (board) with placeholders.
     *
     * @param rows the number of rows in the board.
     * @param cols the number of columns in the board.
     * @return the initialized board.
     */
    private List<List<Character>> initializeBoard(int rows, int cols) {
        List<List<Character>> board = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            List<Character> boardRow = new ArrayList<>(Collections.nCopies(cols, PLACEHOLDER));
            board.add(boardRow);
        }
        return board;
    }

    /**
     * Places words on the board if they fit, and returns a list of successfully placed words.
     *
     * @param board the board to place words on.
     * @param words the list of words to place.
     * @return a list of successfully placed words.
     */
    private List<String> placeWordsOnBoard(List<List<Character>> board, List<String> words) {
        List<String> placedWords = new ArrayList<>();
        Random random = new Random();

        for (String word : words) {
            if (tryPlaceWord(board, word, random)) {
                placedWords.add(word);
            }
        }
        return placedWords;
    }

    /**
     * Tries to place a word on the board, attempting up to a maximum number of times.
     *
     * @param board the board to place the word on.
     * @param word the word to place.
     * @param random the Random instance used for generating random positions.
     * @return true if the word was successfully placed, false otherwise.
     */
    private boolean tryPlaceWord(List<List<Character>> board, String word, Random random) {
        for (int attempts = 0; attempts < 100; attempts++) {
            int row = random.nextInt(getRows());
            int col = random.nextInt(getCols());
            boolean horizontal = random.nextBoolean();

            if (canPlaceWord(board, word, row, col, horizontal)) {
                placeWord(board, word, row, col, horizontal);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a word can be placed on the board at the specified position.
     *
     * @param board the board to check.
     * @param word the word to place.
     * @param row the starting row position.
     * @param col the starting column position.
     * @param horizontal whether the word is placed horizontally or vertically.
     * @return true if the word can be placed, false otherwise.
     */
    private boolean canPlaceWord(List<List<Character>> board, String word, int row, int col, boolean horizontal) {
        if (horizontal) {
            if (col + word.length() > getCols()) return false;
            for (int i = 0; i < word.length(); i++) {
                if (board.get(row).get(col + i) != PLACEHOLDER) return false;
            }
        } else {
            if (row + word.length() > getRows()) return false;
            for (int i = 0; i < word.length(); i++) {
                if (board.get(row + i).get(col) != PLACEHOLDER) return false;
            }
        }
        return true;
    }

    /**
     * Places a word on the board at the specified position.
     *
     * @param board the board to place the word on.
     * @param word the word to place.
     * @param row the starting row position.
     * @param col the starting column position.
     * @param horizontal whether the word is placed horizontally or vertically.
     */
    private void placeWord(List<List<Character>> board, String word, int row, int col, boolean horizontal) {
        for (int i = 0; i < word.length(); i++) {
            if (horizontal) {
                board.get(row).set(col + i, word.charAt(i));
            } else {
                board.get(row + i).set(col, word.charAt(i));
            }
        }
    }

    /**
     * Fills empty cells on the board with random letters.
     *
     * @param board the board to fill.
     */
    private void fillEmptyCells(List<List<Character>> board) {
        Random random = new Random();
        for (List<Character> row : board) {
            for (int col = 0; col < getCols(); col++) {
                if (row.get(col) == PLACEHOLDER) {
                    row.set(col, getRandomLetter(random));
                }
            }
        }
    }

    /**
     * Gets a random letter from the LETTERS array.
     *
     * @param random the Random instance used for generating random letters.
     * @return a random letter.
     */
    private char getRandomLetter(Random random) {
        return LETTERS[random.nextInt(LETTERS.length)];
    }

    /**
     * Converts the board to string.
     *
     * @param board the board to convert.
     * @return the string of the board.
     */
    private String convertBoardToString(List<List<Character>> board) {
        StringBuilder boardString = new StringBuilder(getRows() * (getCols() + 1));
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                boardString.append(board.get(row).get(col));
            }
            if (row < getRows() - 1) {
                boardString.append('\n');
            }
        }
        return boardString.toString();
    }
}
