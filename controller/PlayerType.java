package controller;

import java.util.ArrayList;
import model.Color;
import model.Piece;

/**
 * Defines the behavior of a player in the Checkers game.
 * Implementing classes must provide methods for making moves, managing scores,
 * and accessing player details such as name and piece color.
 */
public interface PlayerType {

    /**
     * Determines the move to make based on the player's available pieces and the board state.
     *
     * @param movablePieces A list of pieces the player can move.
     * @param board         The current state of the game board.
     * @return A list of coordinates representing the chosen move path.
     */
    public ArrayList<int[]> make_a_move(ArrayList<Piece> movablePieces, Piece[][] board);

    /**
     * Increases the player's score by a specified value.
     *
     * @param taken The number of points to add to the player's score.
     */
    public void incrScore(int taken);

    /**
     * Retrieves the player's current score.
     *
     * @return The current score of the player.
     */
    public int getScore();

    /**
     * Retrieves the color of the player's pieces.
     *
     * @return The color of the player's pieces.
     */
    public Color getColor();

    /**
     * Retrieves the player's name.
     *
     * @return The name of the player.
     */
    public String getName();
}
