package controller;

import java.util.ArrayList;
import java.util.HashSet;

import model.*;

public class GameManager 
{
    private GameBoard board;
    private int move_count;
    private Player p1;
    private Player p2;
    
    private GameStateEnum gameState;
    private Move move;
    private Piece selectedPiece = null;

    public GameManager()
    {
        this.board = new GameBoard();
        this.move = new Move();
        this.move_count = 0;
        this.gameState = GameStateEnum.Unselected;
    }
    
    public void OnPieceClick(int x, int y) {
    	ResetHighlights();
    	// perform reset
    	Piece clickedPiece = board.getPiece(x, y);
    	
    	// click on piece
    	if (clickedPiece != null) {
    		this.gameState = GameStateEnum.Selected;
    		this.selectedPiece = clickedPiece;
    		ArrayList<ArrayList<int[]>> possiblePaths = move.getPossibleMoves(clickedPiece, board.getBoardCopy());
    		System.out.println("Highlighted Cells: " );
    		for (ArrayList<int[]> path : possiblePaths) {
    			// i is initialized at 1 so that it doesn't highlight the clicked
    			// piece
    			
    			for (int i = 1; i < path.size(); i++) {
    				if (board.getPiece(path.get(i)[0], path.get(i)[1]) == null) {
    					System.out.print(path.get(i)[0] + " " + path.get(i)[1]);
    					HighLightCell(path.get(i)[0], path.get(i)[1]);
    				}
    			}
    			System.out.println();
    		}
    	}
    	// click on cell
    	else {
    		// if clicked on cell while unselected the do nothing
    		if (gameState.equals(GameStateEnum.Unselected)) return;
    		ArrayList<ArrayList<int[]>> possiblePaths = move.getPossibleMoves(selectedPiece, board.getBoardCopy());
            HashSet<int[]> possibleMoves = new HashSet<>();
            for (ArrayList<int[]> path : possiblePaths) {
                for (int[] cell : path) {
                    possibleMoves.add(cell);
                }
            }

            // Check if clicked cell is in the set of possible moves
            boolean isValidMove = false;
            for (int[] coords : possibleMoves) {
            	if (coords[0] == x && coords[1] == y) {
            		
            		isValidMove = true;
            	}
            }

            if (isValidMove) {
            	System.out.println("Valid");
            	for (ArrayList<int[]> path : possiblePaths) {
            		if (path.get(path.size()-1)[0] == x && path.get(path.size()-1)[1] == y) {
            			System.out.println("FOUND PATH, MOVING...");
            			board.move(path, selectedPiece.getColor());
            			break;
            		}
            	}
            	
                // Perform move
                // board.movePiece(selectedPiece, x, y); // Implement movePiece method
            	
                this.selectedPiece = null;
                this.gameState = GameStateEnum.Unselected;
                NextMove();
            } else {
            	System.out.println("Invalid");
                // Revert state
                this.selectedPiece = null;
                this.gameState = GameStateEnum.Unselected;
            }
    		// check if the coordinate is in the arraylist of possible moves
    		//if yes then perform move, otherwise go back to unselected and reset
    	}
    }
    
    public void HighLightCell(int x, int y) {
    	
    }
    
    public void NextMove() {
    	move_count++;
    	// NEED TO UPDATE: if a piece can take, then only highlight that piece
    	ArrayList<Piece> pieces = GetMovablePieces();
    	System.out.print("Possible pieces: " + pieces);
    	for (int i = 0; i < pieces.size(); i++) {
    		HighLightCell(pieces.get(i).getColumn(), pieces.get(i).getRow());
    	}
    	
    }
    
    public void ResetHighlights() {
    	
    }
    
    public ArrayList<Piece> GetMovablePieces() {
    	
    	ArrayList<Piece> pieces = (move_count % 2 == 0) ? board.getWhitePiecesList() : board.getBlackPiecesList();
    	
    	ArrayList<Piece> out = new ArrayList<Piece>();
    	
    	for (int i = 0; i < pieces.size(); i ++) {
    		if (move.getPossibleMoves(pieces.get(i), board.getBoardCopy()).size() != 0 ) {
    			out.add(pieces.get(i));
    		}
    	}
    	
    	return out;
    	
    }
    
    
    public GameStateEnum getState() {
    	return gameState;
    }
    
    public String toString() {
    	System.out.println("String: " + selectedPiece);
    	Piece[][] newBoard = board.getBoard();
    	String output = "   0 1 2 3 4 5 6 7\n   ----------------\n";
    	// System.out.println(newBoard[2][1]);

	    for (int i = 0; i < newBoard.length; i++) {
	        output += i + "| "; 
	        for (int j = 0; j < newBoard[i].length; j++) {
	            if (newBoard[i][j] == null) {
	            	output += ". "; 
	            } else if (selectedPiece != null && selectedPiece.equals(newBoard[i][j]) ) {
	            	if (newBoard[i][j].getColumn() == selectedPiece.getColumn() && 
	            			newBoard[i][j].getRow() == selectedPiece.getRow()) {
	            		output += "S ";
	            	}
	            }
	            	else if (newBoard[i][j].getColor() == Color.WHITE) {
	            	output += newBoard[i][j].isKing() ? "W " : "w "; 
	            } else if (newBoard[i][j].getColor() == Color.BLACK) {
	            	output += newBoard[i][j].isKing() ? "B " : "b ";
	            }
	        }
	        output += "\n";
	    }
	    output += "\n";
	    
	    return output;
    	
    }
    
}
