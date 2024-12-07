package view;

import java.util.ArrayList;
import model.Piece;

/**
 * Represents the data for a move in the Checkers game.
 * Includes the starting piece, ending piece, and any captured pieces during the move.
 */
public class MoveData {
    private Piece startPiece; // The piece at the starting location of the move
    private Piece endPiece;   // The piece at the ending location of the move
    private ArrayList<Piece> capturedPieces; // List of pieces captured during the move

    /**
     * Constructs a MoveData object with the specified starting piece, ending piece, and captured pieces.
     *
     * @param start    The piece at the starting location of the move.
     * @param end      The piece at the ending location of the move.
     * @param captured The list of pieces captured during the move.
     */
    public MoveData(Piece start, Piece end, ArrayList<Piece> captured) {
        this.startPiece = start;
        this.endPiece = end;
        this.capturedPieces = captured;
    }

    /**
     * Returns a string representation of the move.
     * Includes details about the starting and ending positions, and the captured pieces.
     *
     * @return A string representing the move data.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Add starting and ending piece details
        sb.append("OLD LOCATION: ").append(startPiece).append("\n");
        sb.append("NEW LOCATION: ").append(endPiece).append("\n");
        
        // Add captured pieces details
        for (Piece p : capturedPieces) {
            sb.append("\tEATEN PIECE: ").append(p).append("\n");
        }
        
        return sb.toString();
    }
}
