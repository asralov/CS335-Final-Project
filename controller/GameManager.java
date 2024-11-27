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

    public GameManager()
    {
        this.board = new GameBoard();
        this.move = new Move();
        this.move_count = 0;
        this.gameState = GameStateEnum.Unselected;
    }
    
    public void OnPieceClick(int x, int y) {
    	// perform reset
    	Piece clickedPiece = board.getPiece(x, y);
    	// click on piece
    	if (clickedPiece != null) {
    		this.gameState = GameStateEnum.Selected;
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
    
}
