package pt.ipbeja.app.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BoardContent {

    private String boardContent;
    private List<String> easy;
    private Map<String, List<String>> solutions;
    private final int SIZE;
    private FileReadWrite fileReadWrite;
    private final char PLACEHOLDER = '-'; // Placeholder for empty cells

    public BoardContent() {
        this(5);
    }

    public BoardContent(int SIZE) {
        this.solutions = new HashMap<>();
        this.fileReadWrite = new FileReadWrite();
        this.SIZE = SIZE;
        setBoardContent();
        setSolutions();
    }

    public Map<String, List<String>> getSolutions() {
        return this.solutions;
    }

    public String getBoardContent() {
        return generateBoard(boardContent, this.SIZE);
    }

    private final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String generateBoard(String wordString, int size) {
        // Split the input string into words
        String[] words = wordString.split("\\s+"); // Assuming words are separated by whitespace

        // Filter out words longer than the board size
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() <= size) {
                filteredWords.add(word);
            }
        }

        // Create a 2D array to represent the board
        char[][] board = new char[size][size];

        // Fill the board with placeholders initially
        for (int row = 0; row < size; row++) {
            Arrays.fill(board[row], PLACEHOLDER);
        }

        // List to store words that are successfully placed on the board
        List<String> placedWords = new ArrayList<>();

        // Place each word on the board
        Random random = new Random();
        for (String word : filteredWords) {
            if (placeWord(board, word, size, random)) {
                placedWords.add(word);
            }
        }

        // Fill the remaining cells with random letters
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == PLACEHOLDER) {
                    board[row][col] = getRandomLetter(random);
                }
            }
        }

        // Build the board string from the 2D array
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boardString.append(board[row][col]);
            }
            if (row < size - 1) {
                boardString.append('\n');
            }
        }

        // Update the solutions map
        this.solutions.put("easy", placedWords);

        System.out.println(boardString.toString());
        return boardString.toString();
    }

    private boolean placeWord(char[][] board, String word, int size, Random random) {
        boolean placed = false;
        while (!placed) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            boolean horizontal = random.nextBoolean();

            if (horizontal) {
                if (col + word.length() <= size && canPlaceWord(board, word, row, col, horizontal)) {
                    for (int i = 0; i < word.length(); i++) {
                        board[row][col + i] = word.charAt(i);
                    }
                    placed = true;
                }
            } else {
                if (row + word.length() <= size && canPlaceWord(board, word, row, col, horizontal)) {
                    for (int i = 0; i < word.length(); i++) {
                        board[row + i][col] = word.charAt(i);
                    }
                    placed = true;
                }
            }
        }
        return placed;
    }

    private boolean canPlaceWord(char[][] board, String word, int row, int col, boolean horizontal) {
        for (int i = 0; i < word.length(); i++) {
            if (horizontal) {
                if (board[row][col + i] != PLACEHOLDER) {
                    return false;
                }
            } else {
                if (board[row + i][col] != PLACEHOLDER) {
                    return false;
                }
            }
        }
        return true;
    }

    private char getRandomLetter(Random random) {
        int index = random.nextInt(LETTERS.length);
        return LETTERS[index];
    }

    // Set readFile to BoardContent
    public void setBoardContent() {
        this.easy = new ArrayList<>();
        this.boardContent = this.fileReadWrite.readFile();
        this.easy.add(this.boardContent);
    }

    private void setSolutions() {
        this.solutions.put("easy", easy);
    }
}
