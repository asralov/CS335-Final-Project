package controller;

import java.util.ArrayList;
import model.Color;
import model.Move;
import model.Piece;

/**
 * Represents a computer-controlled player in the Checkers game.
 * The computer uses a simple AI to determine its moves based on the longest path available.
 */
public class Computer extends Player {

    /**
     * Constructs a Computer player with the specified name and color.
     *
     * @param name  The name of the computer player.
     * @param color The color of the computer player's pieces.
     */
    public Computer(String name, Color color) {
        super(name, color);
    }

    /**
     * Determines the move to make using the computer's AI logic.
     *
     * @param movablePieces The list of pieces the computer can move.
     * @param board         The current state of the game board.
     * @return A list of coordinates representing the path of the move.
     */
    @Override
    public ArrayList<int[]> make_a_move(ArrayList<Piece> movablePieces, Piece[][] board) {
        return simpleAI(movablePieces, board);
    }

    /**
     * A simple AI algorithm that selects the best move based on the longest path available.
     *
     * @param movablePieces The list of pieces the computer can move.
     * @param board         The current state of the game board.
     * @return A list of coordinates representing the path of the chosen move.
     */
    private ArrayList<int[]> simpleAI(ArrayList<Piece> movablePieces, Piece[][] board) {
        if (movablePieces.isEmpty()) {
            // No pieces can be moved, return an empty path
            return new ArrayList<>();
        }

        Piece bestPiece = null; // The piece that can make the best move
        int maxPathLength = 0;  // Length of the longest path found

        // Evaluate all movable pieces
        for (Piece piece : movablePieces) {
            // Get all possible moves for the current piece
            ArrayList<ArrayList<int[]>> possibleMoves = Move.getPossibleMoves(piece, board);

            // Check each path to find the longest one
            for (ArrayList<int[]> path : possibleMoves) {
                if (path.size() > maxPathLength) {
                    maxPathLength = path.size();
                    bestPiece = piece; // Update the best piece to the current one
                }
            }
        }

        // If a piece with a valid move is found, choose its longest path
        if (bestPiece != null) {
            ArrayList<ArrayList<int[]>> bestPieceMoves = Move.getPossibleMoves(bestPiece, board);
            ArrayList<int[]> longestPath = new ArrayList<>();
            
            // Find the longest path among the moves for the best piece
            for (ArrayList<int[]> path : bestPieceMoves) {
                if (path.size() > longestPath.size()) {
                    longestPath = path;
                }
            }
            return longestPath; // Return the longest path for the best piece
        }

        // No valid moves found, return an empty path
        return new ArrayList<>();
    }
}
