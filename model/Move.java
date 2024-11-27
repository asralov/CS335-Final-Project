package model;

import java.util.ArrayList;

public class Move {

	public static ArrayList<ArrayList<int[]>> getPossibleMoves(Piece piece, Piece[][] board) {
		ArrayList<ArrayList<int[]>> list1 = new ArrayList<>();
		ArrayList<ArrayList<int[]>> list_capture = new ArrayList<>();

		int x = piece.getRow();
		int y = piece.getColumn();
		boolean isKing = piece.isKing();
		Color color = piece.getColor();

		int[][] directions = getMoveDirections(isKing, color);

		for (int[] direction : directions) {
			getPositionsList(x, y, direction, board, color, list1, list_capture);
		}
		if (list_capture.isEmpty() == true)
			return list1;
		else
			return getLongestList(list_capture);
		
	}

	private static ArrayList<ArrayList<int[]>> getLongestList(ArrayList<ArrayList<int[]>> list_capture) {
		int len = 0;
		int index = 0;
		int i = 0;
		ArrayList<ArrayList<int[]>> list1 = new ArrayList<>();
		for (ArrayList<int[]> list : list_capture)
		{
			if (list.size() > len)
			{
				len = list.size();
				index = i;
			}
			i++;
		}
		
		list1.add(list_capture.get(index));
		return list1;
	}

	private static int[][] getMoveDirections(boolean isKing, Color color) {
		if (isKing) {
			return new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
		}
		else
		{
			if (color == Color.WHITE)
				return  (new int[][]{{1, 1}, {1, -1}});
			else
				return (new int[][]{{-1, 1}, {-1, -1}});
				
		}
	}

	private static void getPositionsList(int x, int y, int[] direction, Piece[][] board, Color color,
			ArrayList<ArrayList<int[]>> list1, ArrayList<ArrayList<int[]>> list_capture) {
		int x1 = x + direction[0];
		int y1 = y + direction[1];

		if (validIndex(x1, y1)) {
			if (board[x1][y1] == null) {
				addNormalMove(x, y, x1, y1, list1);
			} else if (board[x1][y1].getColor() != color) {
				ArrayList<int[]> path = new ArrayList<>();
				path.add(new int[]{x, y}); 
				catchPiece(x, y, board, color, direction, path, list_capture);
			}
		}
	}


	private static void addNormalMove(int x, int y, int x1, int y1, ArrayList<ArrayList<int[]>> list1) {
		ArrayList<int[]> move = new ArrayList<>();
		move.add(new int[]{x, y}); 
		move.add(new int[]{x1, y1}); 
		list1.add(move);
	}


	private static void catchPiece(int x, int y, Piece[][] board, Color color, int[] direction,
			ArrayList<int[]> currentPath, ArrayList<ArrayList<int[]>> list_capture) {
		int x1 = x + direction[0];
		int y1 = y + direction[1];
		int x2 = x1 + direction[0];
		int y2 = y1 + direction[1];

		
		if (validIndex(x1, y1) && board[x1][y1] != null &&
				board[x1][y1].getColor() != color &&
				validIndex(x2, y2) && board[x2][y2] == null) {

			ArrayList<int[]> newPath = new ArrayList<>(currentPath);
			newPath.add(new int[]{x1, y1}); 
			newPath.add(new int[]{x2, y2}); 

			Piece temp = board[x1][y1];
			board[x1][y1] = null;

			for (int[] nextDirection : getMoveDirections(true, color)) {
				catchPiece(x2, y2, board, color, nextDirection, newPath, list_capture);
			}

		
			board[x1][y1] = temp;

			if (newPath.size() > currentPath.size()) {
				list_capture.add(newPath);
			}
		}
	}





	private static boolean validIndex(int x, int y) {

		if (x>= 0 && x<8)
		{
			if (y>= 0 && y<8)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	private static void printBoard(Piece[][] board) {
	    System.out.println("   0 1 2 3 4 5 6 7"); // Column indices
	    System.out.println("   ----------------");

	    for (int i = 0; i < board.length; i++) {
	        System.out.print(i + "| "); 
	        for (int j = 0; j < board[i].length; j++) {
	            if (board[i][j] == null) {
	                System.out.print(". "); 
	            } else if (board[i][j].getColor() == Color.WHITE) {
	                System.out.print(board[i][j].isKing() ? "W " : "w "); 
	            } else if (board[i][j].getColor() == Color.BLACK) {
	                System.out.print(board[i][j].isKing() ? "B " : "b "); 
	            }
	        }
	        System.out.println(); 
	    }
	    System.out.println();
	}




	public static void main(String[] args) {
		Piece[][] board = new Piece[8][8];
		board[0][0] = new Piece(Color.WHITE, 0, 0);
		board[3][4] = new Piece(Color.BLACK, 3, 4);
		board[5][6] = new Piece(Color.BLACK, 5, 6);
		board[4][5] = null;  
		board[6][7] = null;  

		
		Piece piece = board[0][0];
		
		printBoard(board);
		ArrayList<ArrayList<int[]>> moves = Move.getPossibleMoves(piece, board);

		System.out.println("Possible moves for the piece:");
		for (ArrayList<int[]> move : moves) {
			System.out.print("Move path: ");
			for (int[] step : move) {
				System.out.print("(" + step[0] + ", " + step[1] + ") ");
			}
			System.out.println();
		}
	}


}