package controller;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JPanel;

import model.*;
import view.Cell;

public class GameManager 
{
	private JPanel gamePanel;
	private GameBoard board;
	public GameManager(JPanel panel, GameBoard gameBoard) {
		this.gamePanel = panel;
		this.board = gameBoard;
	}
    private int move_count;
    private Player p1;
    private PlayerType p2;
    
    private GameStateEnum gameState;
    private Move move;
    private Piece selectedPiece = null;
	private java.util.List<Cell> highlightedCells = new ArrayList<>(); // Highlighted cells

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
			if (!IsPieceMoveable(clickedPiece, GetMovablePieces())) return;
    		this.gameState = GameStateEnum.Selected;
    		this.selectedPiece = clickedPiece;
    		ArrayList<ArrayList<int[]>> possiblePaths = move.getPossibleMoves(clickedPiece, board.getBoardCopy());
    		System.out.println("Highlighted Cells: " );
    		for (ArrayList<int[]> path : possiblePaths) {
    			// i is initialized at 1 so that it doesn't highlight the clicked
    			// piece
    			

				for (int i = 1; i < path.size(); i++) {
					int[] move = path.get(i);
					Cell cell = (Cell) gamePanel.getComponent(move[0] * 8 + move[1]);
					cell.highlightCell(true);
					highlightedCells.add(cell);
				}
				System.out.println(highlightedCells);
				
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

	private boolean IsPieceMoveable(Piece piece, ArrayList<Piece> moveablePieces) {
		for (int i = 0; i < moveablePieces.size(); i++) {
			if (moveablePieces.get(i).equals(piece)) return true;
		}
		return false;
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
    	for (Cell cell : highlightedCells) {
            cell.highlightCell(false);
        }
        highlightedCells.clear();
    }
    
    public ArrayList<Piece> GetMovablePieces() {
    	
    	ArrayList<Piece> pieces = (move_count % 2 == 0) ? board.getWhitePiecesList() : board.getBlackPiecesList();
    	
    	ArrayList<Piece> out = new ArrayList<Piece>();

		ArrayList<Piece> takeables = new ArrayList<Piece>();
    	
    	for (int i = 0; i < pieces.size(); i ++) {
			ArrayList<ArrayList<int[]>> paths = move.getPossibleMoves(pieces.get(i), board.getBoardCopy());
    		if (paths.size() != 0 ) {
				for (ArrayList<int[]> path: paths) {
					if (path.size() > 2) {
						takeables.add(pieces.get(i));
					}
				}
    			out.add(pieces.get(i));
    		}
    	}

		if (takeables.size() != 0) {
			System.out.println("TAKEABLE EXISTS");
			for (int i = 0; i < takeables.size(); i++) {
				System.out.println(takeables.get(i));
			}
			return takeables;
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
