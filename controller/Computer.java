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
        //ArrayList<Piece> movablePieces = GetMovablePieces();

        if (movablePieces.isEmpty()) {
            return new ArrayList<>();
        }

        Random random = new Random();
        Piece piece = movablePieces.get(random.nextInt(movablePieces.size()));

        ArrayList<ArrayList<int[]>> list1 = Move.getPossibleMoves(piece, board);

        ArrayList<int[]> longestPath = new ArrayList<>();
        for (ArrayList<int[]> path : list1) {
            if (path.size() > longestPath.size()) {
                longestPath = path;
            }
        }
        return longestPath;
    }
    
}
