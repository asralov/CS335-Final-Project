package controller;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Random;

import java.util.HashSet;

import javax.swing.JPanel;

import model.Piece;
import model.GameBoard;
import model.Move;
import view.Cell;
//import model.Color;
import java.awt.Color;

public class GameManager 
{
	private JPanel gamePanel;
	private GameBoard board;
	private final GameOverListener gameOverListener;
	public GameManager(JPanel panel, GameBoard gameBoard, GameOverListener listener) {
		this.gamePanel = panel;
		this.board = gameBoard;
		this.gameOverListener = listener;

		this.board = new GameBoard();
        // this.move = new Move();
        this.move_count = 0;
        this.gameState = GameStateEnum.Unselected;
	}
    private int move_count;
    private Player p1;
    private PlayerType p2;
    
    private GameStateEnum gameState;
    // private Move move;
    private Piece selectedPiece = null;
	private java.util.List<Cell> highlightedCells = new ArrayList<>(); // Highlighted cells

	private boolean hasToTake = false;
	private int moveCountUntilDraw = 40;

	

    // public GameManager()
    // {
        
    // }
    
    public void OnPieceClick(int x, int y) {
		ResetHighlights();
    	
    	// perform reset
    	Piece clickedPiece = board.getPiece(x, y);
    	
    	// click on piece
    	if (clickedPiece != null) {
			System.out.println("HERE");
			if (!IsPieceMoveable(clickedPiece, GetMovablePieces())) return;
    		this.gameState = GameStateEnum.Selected;
    		this.selectedPiece = clickedPiece;
    		ArrayList<ArrayList<int[]>> possiblePaths = Move.getPossibleMoves(clickedPiece, board.getBoardCopy());
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
				//System.out.println(highlightedCells);
				
    		}
    	}
    	// click on cell
    	else {
    		// if clicked on cell while unselected the do nothing
			// System.out.println(this.gameState);
    		if (gameState.equals(GameStateEnum.Unselected)) return;
			
    		ArrayList<ArrayList<int[]>> possiblePaths = Move.getPossibleMoves(selectedPiece, board.getBoardCopy());
			System.out.println("POSSIBLE PATHS: " + possiblePaths);
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
				moveCountUntilDraw--;
            	//System.out.println("Valid");
            	for (ArrayList<int[]> path : possiblePaths) {
            		if (path.get(path.size()-1)[0] == x && path.get(path.size()-1)[1] == y) {
            			System.out.println("FOUND PATH, MOVING...");
            			board.move(path, selectedPiece.getColor());
            			// break;
            		}
            	}
            	
				// CHECKS IF A PIECE CAN JUMP AGAIN AFTER MOVING
				boolean canJumpAgain = false;
                this.selectedPiece = board.getPiece(x, y);
				if (this.selectedPiece != null) {
					for (ArrayList<int[]> path: Move.getPossibleMoves(selectedPiece, board.getBoardCopy())) {
						if (path.size() > 2) {
							canJumpAgain = true;
						}
					}
				}
            	
				if (canJumpAgain && hasToTake) {
					ResetHighlights();
					HighLightCell();

					Cell cellToHighlight = (Cell) gamePanel.getComponent(this.selectedPiece.getRow() * 8 + this.selectedPiece.getColumn());
					cellToHighlight.highlightCell(true);
					
				} else {
					this.selectedPiece = null;
                	this.gameState = GameStateEnum.Unselected;
					NextMove();
				}
                
            } else {
            	//System.out.println("Invalid");
                // Revert state
                this.selectedPiece = null;
                this.gameState = GameStateEnum.Unselected;
            }
    		// check if the coordinate is in the arraylist of possible moves
    		//if yes then perform move, otherwise go back to unselected and reset
    	}
    }
    
    public void HighLightCell() {
    	//System.out.println("UPDATING BOARD...");
        gamePanel.removeAll();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color color = (row + col) % 2 == 0 ? new Color(246, 187, 146) : new Color(152, 86, 40);
                Piece piece = board.getPiece(row, col);
                Cell cell = new Cell(color, piece, row, col);
                gamePanel.add(cell);
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }

	private boolean IsPieceMoveable(Piece piece, ArrayList<Piece> moveablePieces) {
		for (int i = 0; i < moveablePieces.size(); i++) {
			if (moveablePieces.get(i).equals(piece)) return true;
		}
		return false;
	}
    
    public void NextMove() {
		CheckGameOver();
		ResetHighlights();
    	move_count++;
    	ArrayList<Piece> pieces = GetMovablePieces();
    	//System.out.print("Possible pieces: " + pieces);
		HighLightCell();
    	for (int i = 0; i < pieces.size(); i++) {
    		//HighLightCell(pieces.get(i).getColumn(), pieces.get(i).getRow());
			Cell cellToHighlight = (Cell) gamePanel.getComponent(pieces.get(i).getRow() * 8 + pieces.get(i).getColumn());
			cellToHighlight.highlightCell(true, new Color(122, 64, 121));
    	}
		hasToTake = false;
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
			ArrayList<ArrayList<int[]>> paths = Move.getPossibleMoves(pieces.get(i), board.getBoardCopy());
    		if (paths.size() != 0 ) {
				for (ArrayList<int[]> path: paths) {
					if (path.size() > 2) {
						moveCountUntilDraw = 40;
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
			hasToTake = true;
			return takeables;
		}
    	return out;
    	
    }
    
    
    public GameStateEnum getState() {
    	return gameState;
    }
    
    public ArrayList<int[]> simpleAI() {
        ArrayList<Piece> movablePieces = GetMovablePieces();

        if (movablePieces.isEmpty()) {
            return new ArrayList<>();
        }

        Random random = new Random();
        Piece piece = movablePieces.get(random.nextInt(movablePieces.size()));

        ArrayList<ArrayList<int[]>> list1 = Move.getPossibleMoves(piece, board.getBoardCopy());

        ArrayList<int[]> longestPath = new ArrayList<>();
        for (ArrayList<int[]> path : list1) {
            if (path.size() > longestPath.size()) {
                longestPath = path;
            }
        }
        return longestPath;
    }

	public void CheckGameOver() {
        if (moveCountUntilDraw <= 0) {
            FireGameOverEvent("DRAW!");
        } else if (board.getBlackPieces() == 0) {
            FireGameOverEvent("WHITE WON!");
        } else if (board.getWhitePieces() == 0) {
            FireGameOverEvent("BLACK WON!");
        }
    }

	private void FireGameOverEvent(String winner) {
        if (gameOverListener != null) {
            gameOverListener.GameOverOccurred(new GameOverEvent(this, winner));
        }
    }

    //Define the listener interface
    public interface GameOverListener extends java.util.EventListener {
        void GameOverOccurred(GameOverEvent event);
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
	            	else if (newBoard[i][j].getColor() == model.Color.WHITE) {
	            	output += newBoard[i][j].isKing() ? "W " : "w "; 
	            } else if (newBoard[i][j].getColor() == model.Color.BLACK) {
	            	output += newBoard[i][j].isKing() ? "B " : "b ";
	            }
	        }
	        output += "\n";
	    }
	    output += "\n";
	    
	    return output;
    	
    }

	public class GameOverEvent extends EventObject {
    private final String winner;

    public GameOverEvent(Object source, String winner) {
        super(source);
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
    
}
