package pt.ipbeja.app.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BoardContent {

    private String boardContent;
    private List<String> easy ;
    private Map<String,List<String>> solutions;
    private final int SIZE;
    private FileReadWrite fileReadWrite;
    public BoardContent() {
        this.solutions = new HashMap<>();
        this.fileReadWrite = new FileReadWrite();
        this.SIZE = 5;
        setBoardContent();
        setSolutions();
    }

    public BoardContent(int SIZE) {
        this.solutions = new HashMap<>();
        this.fileReadWrite = new FileReadWrite();
        this.SIZE = SIZE;
        setBoardContent();
        setSolutions();
    }

    public Map<String,List<String>> getSolutions(){
        return this.solutions;
    }

    public String getBoardContent() {
     return generateBoard(boardContent,this.SIZE);
    }

    private final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String generateBoard(String wordString, int size) {
        // Split the input string into words
        String[] words = wordString.split("\\s+"); // Assuming words are separated by whitespace

        // Create a StringBuilder to build the board
        StringBuilder board = new StringBuilder();

        // Fill the board with random letters
        Random random = new Random();
        for (int row = 0; row < size; row++) {
            String word = (row < words.length) ? words[row] : ""; // Get the word for the current row or an empty string if no more words
            for (int col = 0; col < size; col++) {
                char cell;
                if (col < word.length()) {
                    // If the position corresponds to a word, fill it with the word's character
                    cell = word.charAt(col);
                } else {
                    // Otherwise, fill it with a random letter
                    cell = getRandomLetter(random);
                }
                // Append the cell to the board
                board.append(cell);
            }
            // Add newline character after each row except the last one
            if (row < size - 1) {
                board.append('\n');
            }
        }

        return board.toString();
    }

    private char getRandomLetter(Random random) {
        int index = random.nextInt(LETTERS.length);
        return LETTERS[index];
    }

    //Set readFile to BoardContent
    public void setBoardContent(){
        this.easy = new ArrayList<>();
        this.boardContent = this.fileReadWrite.readFile();
        this.easy.add(this.boardContent);
    }

    private void setSolutions() {
        this.solutions.put("easy",easy);
    }

}
