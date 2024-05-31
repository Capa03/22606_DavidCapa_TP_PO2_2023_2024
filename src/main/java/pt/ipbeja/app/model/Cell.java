package pt.ipbeja.app.model;

/**
 * Cell in the board
 * Contains a letter and a boolean that indicates if the cell is part of a word
 * @author David Capa
 * @version 2024/04/14
 */
public class Cell {
    private final char letter;

    public Cell(char letter) {
        this.letter = letter;
    }
}
