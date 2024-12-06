package controller;

import java.util.ArrayList;
import java.util.Random;

import model.Color;
import model.Move;
import model.Piece;

public class Computer extends Player {
	
	public Computer(String name, Color color) {
		super(name, color);
	}
   
    @Override
    public ArrayList<int[]> make_a_move(ArrayList<Piece> movablePieces, Piece[][] board) {
        return simpleAI(movablePieces, board);
    }

    private ArrayList<int[]> simpleAI(ArrayList<Piece> movablePieces, Piece[][] board) {
        if (movablePieces.isEmpty()) {
            return new ArrayList<>();
        }

        Piece bestPiece = null;
        int maxPathLength = 0;

        for (Piece piece : movablePieces) {
            ArrayList<ArrayList<int[]>> possibleMoves = Move.getPossibleMoves(piece, board);

            for (ArrayList<int[]> path : possibleMoves) {
                if (path.size() > maxPathLength) {
                    maxPathLength = path.size();
                    bestPiece = piece;
                }
            }
        }

        if (bestPiece != null) {
            ArrayList<ArrayList<int[]>> bestPieceMoves = Move.getPossibleMoves(bestPiece, board);
            ArrayList<int[]> longestPath = new ArrayList<>();
            for (ArrayList<int[]> path : bestPieceMoves) {
                if (path.size() > longestPath.size()) {
                    longestPath = path;
                }
            }
            return longestPath;
        }

        return new ArrayList<>();
    }

    
}
