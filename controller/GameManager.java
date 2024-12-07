package controller;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Piece;
import model.GameBoard;
import model.Move;
import view.Cell;
import view.Game;

import java.awt.Color;

/**
 * The GameManager class handles the main game logic for a checkers game,
 * including turn management, move validation, piece selection, and win
 * conditions. It interacts with the game board, players, and the user
 * interface.
 */
public class GameManager {
	private JPanel gamePanel; // game panel for checkers game
	private GameBoard board; // board for checkers game
	private GameOverListener gameOverListener; // listener for game over
	private GameModeEnum gameMode; // game mode for current game
	private model.Color currentTurn; // current turn of the players
	private PlayerType p1; // player 1
	private PlayerType p2; // player 2 or cpu

	private int move_count; // current move count
	private GameStateEnum gameState; // current game state 
	private Piece selectedPiece = null; // set selected Piece 
	private java.util.List<Cell> highlightedCells = new ArrayList<>(); // Highlighted cells
	private boolean hasToTake = false; // set flag for needs to continue 
	private int moveCountUntilDraw = 40; // set count until draw
	private Timer computerMoveTimer; // set timer for computer

	/**
	 * Constructor initializes the Game manager with the necessary attributes
	 * 
	 * @param panel     the panel of the game board
	 * @param gameBoard the game board itself
	 * @param listener  the listener for game over
	 * @param mode      the mode of the game
	 */
	public GameManager(JPanel panel, GameBoard gameBoard, GameOverListener listener, GameModeEnum mode) {
		this.gamePanel = panel;
		this.board = gameBoard;
		this.gameOverListener = listener;
		this.gameMode = mode;

		// set up pvp
		if (gameMode.equals(GameModeEnum.PvP)) {
			p1 = new Player("PLAYER1", model.Color.WHITE);
			p2 = new Player("PLAYER2", model.Color.BLACK);

			// set up pvc
		} else if (gameMode.equals(GameModeEnum.PvC)) {
			p1 = new Player("PLAYER1", model.Color.WHITE);
			p2 = new Computer("PLAYER2", model.Color.BLACK);
		}

		// set current turn to whoever is white
		currentTurn = p1.getColor();
	}

	/**
	 * Constructor constructs game manager wuth the game board, 
	 * move_count, gameState and current turn 
	 */
	public GameManager() {
		this.board = new GameBoard();
		this.move_count = 0;
		this.gameState = GameStateEnum.Unselected;
		currentTurn = model.Color.WHITE;
	}

	/**
	 * Method sets the turn for the next go
	 * 
	 * @param turn the turn of the next person
	 */
	public void setTurn(model.Color turn) {
		this.currentTurn = turn;
	}


	/**
	 * Method shows the movable pieces and the path of the
	 * piece that is clicked.
	 * 
	 * @param x the x of the piece
	 * @param y the y of the piece 
	 */
	public void OnPieceClick(int x, int y) {
		// reset all highlights 
		ResetHighlights();

		// perform reset
		Piece clickedPiece = board.getPiece(x, y);

		// click on piece
		if (clickedPiece != null) {
			System.out.println("HERE");
			if (!IsPieceMoveable(clickedPiece, GetMovablePieces()))
				return;
			this.gameState = GameStateEnum.Selected;
			this.selectedPiece = clickedPiece;
			// get all possible moves s
			ArrayList<ArrayList<int[]>> possiblePaths = Move.getPossibleMoves(clickedPiece, board.getBoardCopy());
			for (ArrayList<int[]> path : possiblePaths) {
				// i is initialized at 1 so that it doesn't highlight the clicked
				// piece
				for (int i = 1; i < path.size(); i++) {
					int[] move = path.get(i);
					Cell cell = (Cell) gamePanel.getComponent(move[0] * 8 + move[1]);
					cell.highlightCell(true);
					highlightedCells.add(cell);
				}

			}
		}
		// click on cell
		else {
			// if clicked on cell while unselected the do nothing
			if (gameState.equals(GameStateEnum.Unselected))
				return;

			ArrayList<ArrayList<int[]>> possiblePaths = Move.getPossibleMoves(selectedPiece, board.getBoardCopy());
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
				for (ArrayList<int[]> path : possiblePaths) {
					if (path.get(path.size() - 1)[0] == x && path.get(path.size() - 1)[1] == y) {
						ArrayList<Piece> histogram = board.move(path, selectedPiece.getColor(), selectedPiece.isKing());
						SendPiecesToHistogram(histogram);
					}
				}

				// CHECKS IF A PIECE CAN JUMP AGAIN AFTER MOVING
				boolean canJumpAgain = false;
				this.selectedPiece = board.getPiece(x, y);
				if (this.selectedPiece != null) {
					for (ArrayList<int[]> path : Move.getPossibleMoves(selectedPiece, board.getBoardCopy())) {
						if (path.size() > 2) {
							canJumpAgain = true;
						}
					}
				}

				// force player to complete jump cycle 
				if (canJumpAgain && hasToTake) {
					ResetHighlights();
					HighLightCell();

					Cell cellToHighlight = (Cell) gamePanel
							.getComponent(this.selectedPiece.getRow() * 8 + this.selectedPiece.getColumn());
					cellToHighlight.highlightCell(true);

				} else {
					// go to next player
					SwitchCurrentTurn();
					this.selectedPiece = null;
					this.gameState = GameStateEnum.Unselected;
					NextMove();
				}

			} else {
				// Revert state
				this.selectedPiece = null;
				this.gameState = GameStateEnum.Unselected;
			}
		}
	}

	/**
	 * Method highlights cells on the gamePanel
	 */
	public void HighLightCell() {
		// remove all current highlights 
		gamePanel.removeAll();
		
		// add the cells
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Color color;
				if ((row + col) % 2 == 0) {
				    color = new Color(246, 187, 146); // Assign a light color for even sum of row and col
				} else {
				    color = new Color(152, 86, 40); // Assign a dark color for odd sum of row and col
				}

				// get the piece from the board 
				Piece piece = board.getPiece(row, col);
				Cell cell = new Cell(color, piece, row, col);
				
				// add the cell with the piece 
				gamePanel.add(cell);
			}
		}
		
		// revalidate and repaint
		gamePanel.revalidate();
		gamePanel.repaint();
	}

	/**
	 * Method returns if a piece is movable on the board
	 * 
	 * @param piece the piece to be moved or stay put 
	 * @param moveablePieces the array of pieces that are movable 
	 * 
	 * @return boolean if piece can move or not 
	 */
	private boolean IsPieceMoveable(Piece piece, ArrayList<Piece> moveablePieces) {
		for (int i = 0; i < moveablePieces.size(); i++) {
			// if desired piece was found, return true
			if (moveablePieces.get(i).equals(piece))
				return true;
		}
		return false; // otherwise return false
	}

	/**
	 * Method gets the board ready for the next move 
	 */
	public void NextMove() {
		// Determine whose turn it is based on move_count
		PlayerType currentPlayer;

		// gets the curr player 
		JLabel curr = Game.getCurr();
		// set the current turn respectfully 
		if (currentTurn.equals(p1.getColor())) {
			currentPlayer = p1;
			curr.setText("Current Turn: " + p1.getName());
		} else {
			currentPlayer = p2;
			curr.setText("Current Turn: " + p2.getName());
		}
		
		// revalidate and repaints 
		curr.revalidate();
		curr.repaint();

		// if the player is a computer, make the move and pass it on
		if (currentPlayer instanceof Computer) {
			// reset game baord for AI
			ResetHighlights();
			HighLightCell();
			computerMoveTimer = new Timer(1000, e -> {

				// make a move 
				ArrayList<int[]> computerMove = currentPlayer.make_a_move(GetMovablePieces(), board.getBoardCopy());
				SwitchCurrentTurn();
				move_count++; // increase move count
				if (!computerMove.isEmpty()) {
					// get the piece to move
					Piece pieceToMove = board.getPiece(computerMove.get(0)[0], computerMove.get(0)[1]);
					ArrayList<Piece> histogram = board.move(computerMove, pieceToMove.getColor(), pieceToMove.isKing());
					SendPiecesToHistogram(histogram);
					CheckGameOver();

					// pass next move
					NextMove();
				} else {
					CheckGameOver(); // Computer has no moves
				}

			});
			computerMoveTimer.setRepeats(false); // Only execute once
			computerMoveTimer.start();
		} else {
			// reset highlights, check for game over 
			ResetHighlights();
			CheckGameOver();
			
			// get the moveable pieces and highlight cells 
			ArrayList<Piece> pieces = GetMovablePieces();
			HighLightCell();
			hasToTake = false;
			
			// makes cpu jump to not take
			for (int i = 0; i < pieces.size(); i++) {
				Cell cellToHighlight = (Cell) gamePanel
						.getComponent(pieces.get(i).getRow() * 8 + pieces.get(i).getColumn());
				cellToHighlight.highlightCell(true, new Color(122, 64, 121));
			}

			move_count++;
		}

	}

	/**
	 * Method switches the current move 
	 */
	public void SwitchCurrentTurn() {
		// switches move to white if black went 
		if (currentTurn.equals(model.Color.BLACK)) {
			currentTurn = model.Color.WHITE;
		} else {
			currentTurn = model.Color.BLACK;
		}
	}

	/**
	 * Method gets the current color of the person who has 
	 * the turn
	 * 
	 * @return the color of the player
	 */
	public model.Color getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * Method resets the highlights on the board
	 */
	public void ResetHighlights() {
		// unhighlight cells
		for (Cell cell : highlightedCells) {
			cell.highlightCell(false);
		}
		
		// clear the array of highlighted cells
		highlightedCells.clear();
	}

	/**
	 * Method gets the moveable pieces on the board
	 * 
	 * @return the array of moveable pieces
	 */
	public ArrayList<Piece> GetMovablePieces() {

		// array of pieces 
		ArrayList<Piece> pieces;

		// get the black pieces 
		if (currentTurn.equals(model.Color.BLACK)) {
			pieces = board.getBlackPiecesList();
		} else {
			// get the white pieces 
			pieces = board.getWhitePiecesList();
		}

		// get the out pieces 
		ArrayList<Piece> out = new ArrayList<Piece>();

		// get the takeables 
		ArrayList<Piece> takeables = new ArrayList<Piece>();

		// get the possible moves for each 
		for (int i = 0; i < pieces.size(); i++) {
			ArrayList<ArrayList<int[]>> paths = Move.getPossibleMoves(pieces.get(i), board.getBoardCopy());
			if (paths.size() != 0) {
				for (ArrayList<int[]> path : paths) {
					if (path.size() > 2) {
						moveCountUntilDraw = 40; // reset draw
						takeables.add(pieces.get(i));
					}
				}
				// add paths to pieces
				out.add(pieces.get(i));
			}
		}

		// make player take
		if (takeables.size() != 0) {
			hasToTake = true;
			return takeables;
		}
		return out;

	}

	/**
	 * Method gets the state of the game and returns it 
	 * 
	 * @return the state of the game 
	 */
	public GameStateEnum getState() {
		return gameState;
	}

	/**
	 * Method checks if the game is over using end game
	 * cases 
	 */
	public void CheckGameOver() {
		// if moveCOunt till draw is met set event to draw
		if (moveCountUntilDraw <= 0) {
			FireGameOverEvent("DRAW!");
		} else if (board.getBlackPieces() == 0) {
			// white won
			FireGameOverEvent("WHITE WON!");
		} else if (board.getWhitePieces() == 0) {
			//black won
			FireGameOverEvent("BLACK WON!");
		} else if (GetMovablePieces().size() == 0) {
			// black or white wins by no moves available 
			if (board.getBlackPieces() > board.getWhitePieces()) {
				FireGameOverEvent("BLACK WON!");
			} else {
				FireGameOverEvent("WHITE WON!");
			}
		}
	}

	/**
	 * Method triggers GameOverEvent with winner string 
	 * 
	 * @param winner the winner of the game 
	 */
	private void FireGameOverEvent(String winner) {
		// trigger gameListener and GameOverOccured 
		if (gameOverListener != null) {
			gameOverListener.GameOverOccurred(new GameOverEvent(this, winner));
		}
	}

	/**
	 * Method sends pieces that were moved or taken to histogram 
	 * 
	 * @param pieces array of pieces
	 */
	private void SendPiecesToHistogram(ArrayList<Piece> pieces) {
		// trigger a GetMovedPieces event
		if (gameOverListener != null) {
			gameOverListener.GetMovedPieces(new GetMovedPieces(this, pieces));
		}
	}

	/**
	 *  Method calculates the score using the players live peices 
	 *  
	 * @return an int array of white score and black score
	 */
	public int[] calculateScore() {
		// get live pieces
		int whiteCount = board.getWhitePieces();
		int blackCount = board.getBlackPieces();

		// calcualte score 
		int whiteScore = 12 - whiteCount;
		int blackScore = 12 - blackCount;

		return new int[] { whiteScore, blackScore };
	}

	/**
	 * Method reutrn the P1's name 
	 * 
	 * @return the player 1's name 
	 */
	public String p1name() {
		return this.p1.getName();
	}

	/**
	 * Method returns the P2's name
	 * 
	 * @return the playuer 2's name
	 */
	public String p2name() {
		return this.p2.getName();
	}

	/**
	 * Method returns a string representation of the GameManager
	 * 
	 * @returns String of the GameMangager
	 */
	public String toString() {
		Piece[][] newBoard = board.getBoardCopy();
		String output = "   0 1 2 3 4 5 6 7\n   ----------------\n";
		// System.out.println(newBoard[2][1]);

		for (int i = 0; i < newBoard.length; i++) {
			output += i + "| ";
			for (int j = 0; j < newBoard[i].length; j++) {
				if (newBoard[i][j] == null) {
					output += ". ";
				} else if (selectedPiece != null && selectedPiece.equals(newBoard[i][j])) {
					if (newBoard[i][j].getColumn() == selectedPiece.getColumn()
							&& newBoard[i][j].getRow() == selectedPiece.getRow()) {
						output += "S ";
					}
				} else if (newBoard[i][j].getColor() == model.Color.WHITE) {
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

}
