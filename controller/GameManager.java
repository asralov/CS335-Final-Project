package controller;

import java.util.ArrayList;

import model.*;

public class GameManager 
{
    private GameBoard board;
    private int move_count;
    private Player p1;
    private PlayerType p2;
    
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
    		System.out.println(this.selectedPiece);
    		ArrayList<ArrayList<int[]>> possiblePaths = move.getPossibleMoves(clickedPiece, board.getBoardCopy());
    		for (ArrayList<int[]> path : possiblePaths) {
    			// i is initialized at 1 so that it doesn't highlight the clicked
    			// piece
    			for (int i = 1; i < path.size(); i++) {
    				HighLightCell(path.get(i)[0], path.get(i)[1]);
    			}
    		}
    		// get the possible moves, iterate through them
    		// and highlight each one
    	}
    	// click on cell
    	else {
    		// if clicked on cell while unselected the do nothing
    		if (gameState.equals(GameStateEnum.Unselected)) return;
    		
    		// check if the coordinate is in the arraylist of possible moves
    		//if yes then perform move, otherwise go back to unselected and reset
    	}
    }
    
    public void HighLightCell(int x, int y) {
    	
    }
    
    public void NextMove() {
    	move_count++;
    }
    
    public void ResetHighlights() {
    	
    }
    
    public void GetMovablePieces() {
    	
    }
    
    public GameStateEnum getState() {
    	return gameState;
    }
    
    public String toString() {
    	System.out.println("String: " + this.selectedPiece);
    	Piece[][] newBoard = board.getBoard();
    	String output = "   0 1 2 3 4 5 6 7\n   ----------------\n";

	    for (int i = 0; i < newBoard.length; i++) {
	        output += i + "| "; 
	        for (int j = 0; j < newBoard[i].length; j++) {
	            if (newBoard[i][j] == null) {
	            	output += ". "; 
	            } else if (selectedPiece != null) {
	            	System.out.println("yes");
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
