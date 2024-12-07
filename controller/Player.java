package controller;

import java.util.ArrayList;
import model.Color;
import model.Piece;

/**
 * Represents a player in the Checkers game.
 * Each player has a name, score, color, and the ability to make moves.
 */
public class Player implements PlayerType {

    private String name;  // The player's name
    private int score;    // The player's current score
    private Color color;  // The color of the player's pieces

    /**
     * Constructs a Player object with the specified name and piece color.
     * The initial score is set to zero.
     *
     * @param name  The name of the player.
     * @param color The color of the player's pieces.
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.score = 0;
    }

    /**
     * Increases the player's score by a specified value.
     *
     * @param taken The number of points to add to the player's score.
     */
    public void incrScore(int taken) {
        this.score += taken;
    }

    /**
     * Returns the player's current score.
     *
     * @return The current score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the color of the player's pieces.
     *
     * @return The player's piece color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns the player's name.
     *
     * @return The player's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Determines the move to make based on the player's available pieces and the board state.
     * This method is intended to be overridden by subclasses (e.g., `Computer`).
     *
     * @param movablePieces The list of pieces the player can move.
     * @param board         The current state of the game board.
     * @return A list of coordinates representing the chosen move path, or {@code null} if no move is made.
     */
    public ArrayList<int[]> make_a_move(ArrayList<Piece> movablePieces, Piece[][] board) {
        return null; // Default implementation returns null
    }
}
