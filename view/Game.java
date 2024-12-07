package view;

import java.awt.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import controller.GameManager;
import controller.GameModeEnum;
import controller.GameOverEvent;
import controller.GameOverListener;
import controller.GetMovedPieces;
import model.*;

public class Game implements State, GameOverListener {
	private GameBoard gameBoard; // Backend game board
	private GameManager gameManager;
	private JPanel gamePanel; // UI panel for the board
	private Timer timer; // Swing Timer
	private JLabel timerLabel; // Label to display the timer
	private JLabel capturedPiecesLabelWhite;
	private JLabel capturedPiecesLabelBlack;
	private JPanel timerPanel;
	private ArrayList<MoveData> moveHistory = new ArrayList<>();
	private JTextArea moveHistoryArea;
	private JScrollPane moveHistoryScrollPane;
	private JPanel moveHistoryPanel;
	private File moveHistoryFile; // Added this line
	private model.Color turn;
	private int elapsedTime = 0; // Time in seconds
	private static JLabel curr;

	// Public getter method for `curr`
	public static JLabel getCurr() {
	    return curr;
	}

	// Private setter method for internal use
	private static void setCurr(JLabel currLabel) {
	    curr = currLabel;
	}
	
	/**
	 * Method sets up the GUI for a new game of checkers by calling
	 * initailzeUI(window, and initializeNewGame()
	 * 
	 * @param window   the window for the GUI
	 * @param gameMode the game mode for the game [PvP or PvC]
	 */
	@Override
	public void setup(JFrame window, GameModeEnum gameMode) {
		initializeUI(window);

		// specific logic for a new game of checkers
		initializeNewGame();
	}

	/**
	 * Method sets up an instance of checkers from a previous game, calls methods
	 * initializeUI, loadGameState, and initializeLoadedGame to do so.
	 * 
	 * @param window the window for the GUI
	 */
	public void setupLoaded(JFrame window) {
		// Initializes all UI components
		initializeUI(window);

		// Load game data after UI initialization
		loadGameState("saved_game.txt");

		// Additional setup logic for game
		initializeLoadedGame();
	}

	/**
	 * Method initializes the UI components
	 * 
	 * @param window the window for the GUI
	 */
	private void initializeUI(JFrame window) {
		// Load background image
		ImageIcon bg_icon = new ImageIcon("./assets/game_bg.jpg");
		Image bg_img = bg_icon.getImage();

		// Create a main panel with overridden paintComponent for background image
		JPanel mainGamePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (bg_img != null) {
					g.drawImage(bg_img, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};

		// Set customization for mainGamePanel
		mainGamePanel.setLayout(new BorderLayout());
		mainGamePanel.setOpaque(false);

		// Timer panel setup
		if (timerPanel == null) {
			timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

			// Ensure transparency over background
			timerPanel.setOpaque(false);

			// Create menu button with checkers GUI
			JButton menu_btn = new JButton("MENU");
			Menu.styleButton(menu_btn);

			// Set the event to be the Does_User_Want function
			menu_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Does_User_Want();
				}
			});

			// Set menu's button size
			Dimension buttonSize = new Dimension(60, 30);
			menu_btn.setPreferredSize(buttonSize);
			menu_btn.setMinimumSize(buttonSize);
			menu_btn.setMaximumSize(buttonSize);

			// Add menu button to the timer panel
			timerPanel.add(menu_btn);

			// Set timer label
			timerLabel = new JLabel("0:00");
			timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
			timerLabel.setForeground(Color.WHITE);
			timerPanel.add(timerLabel);

			// Create panel for captured white pieces
			capturedPiecesLabelWhite = new JLabel("x0"); // pieces captured start at 0
			capturedPiecesLabelWhite.setFont(new Font("Arial", Font.BOLD, 20));
			capturedPiecesLabelWhite.setForeground(Color.WHITE);

			// set icon and add it to the piece panel
			JPanel piecePanelWhite = new JPanel(new FlowLayout(FlowLayout.LEFT));
			// Transparent background
			piecePanelWhite.setOpaque(false);
			CirclePanelWhite circleWhite = new CirclePanelWhite();
			circleWhite.setPreferredSize(new Dimension(30, 30));
			circleWhite.setOpaque(true);
			piecePanelWhite.add(circleWhite);
			piecePanelWhite.add(capturedPiecesLabelWhite);

			// Add white piece panel to timer panel
			timerPanel.add(piecePanelWhite);

			// Create panel for captured black pieces
			capturedPiecesLabelBlack = new JLabel("x0"); // captured pieces start at 0
			capturedPiecesLabelBlack.setFont(new Font("Arial", Font.BOLD, 20));
			capturedPiecesLabelBlack.setForeground(Color.WHITE);

			// Set icon and add it to the piece panel
			JPanel piecePanelBlack = new JPanel(new FlowLayout(FlowLayout.LEFT));
			piecePanelBlack.setOpaque(false); // Transparent background
			CirclePanelBlack circleBlack = new CirclePanelBlack();
			circleBlack.setPreferredSize(new Dimension(30, 30));
			circleBlack.setOpaque(true);
			piecePanelBlack.add(circleBlack);
			piecePanelBlack.add(capturedPiecesLabelBlack);

			// Add black piece panel to timer panel
			timerPanel.add(piecePanelBlack);
		}

		// Wrapper panel for the game board
		JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		// Transparent for background visibility
		wrapperPanel.setOpaque(false);
		wrapperPanel.setPreferredSize(new Dimension(800, 800));

		// Game panel setup
		gamePanel = new JPanel(new GridLayout(8, 8));
		gamePanel.setPreferredSize(new Dimension(700, 700));

		// Add the game panel to the wrapper
		if (gamePanel == null) {
			gamePanel = new JPanel(new GridLayout(8, 8));
			gamePanel.setPreferredSize(new Dimension(700, 700));
		}
		wrapperPanel.add(gamePanel);

		// Add panels to the main game panel
		mainGamePanel.add(timerPanel, BorderLayout.NORTH); // Timer at the top
		mainGamePanel.add(wrapperPanel, BorderLayout.CENTER); // Board in the center

		// HISTOGRAM PANEL
		moveHistoryArea = new JTextArea();
		moveHistoryArea.setEditable(false);
		moveHistoryScrollPane = new JScrollPane(moveHistoryArea);
		moveHistoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Set the size of the moveHistoryPanel to match the gamePanel
		moveHistoryPanel = new JPanel(new BorderLayout());
		moveHistoryPanel.add(moveHistoryScrollPane, BorderLayout.CENTER);
		moveHistoryPanel.setPreferredSize(new Dimension(325, 700)); // Match the size of the checkerboard
		moveHistoryPanel.setOpaque(false); // Ensure transparency if needed

		// Add padding to the panel to force the panel to size
		moveHistoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 150, 10)); // Top, Left, Bottom, Right
																						// padding

		// Add the panel to the right of the checkerboard
		mainGamePanel.add(moveHistoryPanel, BorderLayout.EAST); // Add to the right of the checkerboard

		// Setup the timer from elapsed seconds
		setupTimer();
		timerPanel.setOpaque(false);
		wrapperPanel.setOpaque(false);

		// Setup the window
		window.getContentPane().removeAll();
		window.add(mainGamePanel);
		window.revalidate();
		window.repaint();
	}

	/**
	 * Method initalizes the gameBoard and gameManager for a new game of checkers.
	 */
	private void initializeNewGame() {
		// create gameBoard and gameManager for the game of checkers
		gameBoard = new GameBoard();
		gameManager = new GameManager(gamePanel, gameBoard, this, Checkers.mode);

		// set current turn using gameMangers getCurrent turn
		curr = new JLabel();
		String curr_turn = "Current Turn: ";
		if (gameManager.getCurrentTurn().name().equals("WHITE")) {
			curr_turn += gameManager.p1name();
		} else {
			curr_turn += gameManager.p2name();
		}
		curr.setText(curr_turn); // Set the text or curr
		curr.setFont(new Font("Arial", Font.BOLD, 20));
		curr.setForeground(Color.WHITE);
		timerPanel.add(curr); // add curr to the timer panel

		// clear move history from potential previous game
		moveHistory.clear();

		timer.restart(); // restart the timer
		// reset move history for UI
		updateMoveHistory();

		// make moveHistory file if not present
		try {
			moveHistoryFile = new File("move_history.txt");
			if (!moveHistoryFile.exists()) {
				moveHistoryFile.createNewFile();
			} else {
				// Clear the file contents for a new game
				new PrintWriter(moveHistoryFile).close();
			}
			// file not created
		} catch (IOException e) {
			e.printStackTrace();
		}

		// update the board to reflect the new game
		updateBoard();
	}

	/**
	 * Method initializes a previous game of checkers
	 */
	private void initializeLoadedGame() {

		// if a game board was not created, create a new game board
		if (gameBoard == null) {
			gameBoard = new GameBoard();
		}

		// initialize game Manager with panel, board, this instance. and game mode
		gameManager = new GameManager(gamePanel, gameBoard, this, Checkers.mode);

		// get the current score for both players
		int[] currentScore = gameManager.calculateScore();

		// set scores on the capturedPieces panels
		String blackScore = "x" + currentScore[0];
		String whiteScore = "x" + currentScore[1];
		capturedPiecesLabelWhite.setText(whiteScore);
		capturedPiecesLabelBlack.setText(blackScore);

		// set curr to the value of the game managers turn getter
		curr = new JLabel();
		String curr_turn = "Current Turn: ";
		if (gameManager.getCurrentTurn().name().equals("WHITE")) {
			curr_turn += gameManager.p1name();
		} else {
			curr_turn += gameManager.p2name();
		}
		curr.setText(curr_turn);
		curr.setFont(new Font("Arial", Font.BOLD, 20));
		curr.setForeground(Color.WHITE);
		timerPanel.add(curr);

		// Set the turn in GameManager
		gameManager.setTurn(this.turn);

		// Refresh the panel to reflect the loaded board
		gamePanel.removeAll();
		updateBoard();
		gamePanel.revalidate();
		gamePanel.repaint();

	}

	/**
	 * Method loads in a gameState from the last checkers game that was saved
	 * 
	 * @param filename name of the file containing the state of the board and |->
	 *                 current turn, and the history moves
	 */
	public void loadGameState(String filename) {
		// if the game board has not been created, create the game board
		if (gameBoard == null) {
			gameBoard = new GameBoard();
		}

		// try to open the time remaining file and updated the elapsed time instance
		// variable
		try (BufferedReader reader = new BufferedReader(new FileReader("time_remaining.txt"))) {
			elapsedTime = Integer.parseInt(reader.readLine().trim());

		} catch (IOException ex) {
			// file was not found for time elapsed
			ex.printStackTrace();
			JOptionPane.showMessageDialog(Checkers.window,
					"Failed to load game mode. Please ensure the game_mode.txt file exists.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// try to open the save file and load the pieces, move history and turn
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			// create string for line
			String line;

			// create flags
			boolean isBoardSection = false;
			boolean isHistorySection = false;

			// make an array for an empty board
			Piece[][] board = new Piece[8][8];
			StringBuilder historyText = new StringBuilder();

			String turn = "WHITE"; // Default turn

			// Read through the file
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// board state was found, update Board Section
				if (line.startsWith("\"board\": [")) {
					isBoardSection = true;
					continue;
				}

				// history section was found , update history section
				if (line.startsWith("\"history\": [")) {
					isHistorySection = true;
					continue;
				}

				// currently in the board section
				if (isBoardSection) {

					// end of board section reached
					if (line.equals("]") || line.equals("],")) {
						isBoardSection = false;
						continue;
					}

					// get beginning cells and split by },
					String[] cells = line.replace("[", "").replace("]", "").split("},");
					for (int colIndex = 0; colIndex < cells.length && colIndex < 8; colIndex++) {
						String cell = cells[colIndex].trim();

						// if the cell it equivalent to null continue
						if (cell.equals("null")) {
							continue; // Skip null cells
						}

						// remove brackets
						cell = cell.replace("{", "").replace("}", "").trim();

						// get attributes
						String[] attributes = cell.split(",");
						String color = "";

						// set flags for piece changes
						boolean isKing = false;
						int row = -1, col = -1;

						// go over each attribute
						for (String attribute : attributes) {
							// split string on :
							String[] keyValue = attribute.split(":");

							// if attributes is less than 2 continue/ignore
							if (keyValue.length < 2)
								continue;

							// get the key and value
							String key = keyValue[0].trim().replace("\"", "");
							String value = keyValue[1].trim().replace("\"", "");

							// figure out what the key currently is, assign the values
							if (key.equals("color")) {
								color = value;
							} else if (key.equals("king")) {
								isKing = Boolean.parseBoolean(value);
							} else if (key.equals("row")) {
								row = Integer.parseInt(value);
							} else if (key.equals("col")) {
								col = Integer.parseInt(value);
							}
						}

						// Validate row, column, and square color
						if (row >= 0 && row < 8 && col >= 0 && col < 8 && (row + col) % 2 != 0) {
							Piece piece = new Piece(color.equals("WHITE") ? model.Color.WHITE : model.Color.BLACK, row,
									col);
							if (isKing)
								piece.ToKing();

							// place piece on board
							board[row][col] = piece;
						}
					}
				}

				// history section reached
				if (isHistorySection) {

					// history section ended
					if (line.equals("]") || line.equals("],")) {
						isHistorySection = false;
						continue;
					}

					// append the history text to the history panel
					historyText.append(line.replace("\"", "").replace(",", "")).append("\n");
				}

				// update the turn
				if (line.startsWith("\"turn\":")) {
					turn = line.split(":")[1].trim().replace("\"", "").replace(",", "");
				}
			}

			// set board from board array
			gameBoard.setBoard(board);

			// Update the turn
			if (turn.equals("BLACK")) {
				this.setTurn(model.Color.BLACK);
			} else {
				this.setTurn(model.Color.WHITE);
			}

			// Load move history
			if (moveHistoryArea != null) {
				moveHistoryArea.setText(historyText.toString());
				moveHistoryArea.setText(moveHistoryArea.getText() + "\n");
			}

		} catch (IOException e) {
			// file not found
			e.printStackTrace();
		}
	}

	/**
	 * Method prompts user to save the game before going to main menu, exit without
	 * saving, or the cancel the move
	 *
	 */
	private void Does_User_Want() {
		// Create the dialog
		JDialog dialog = new JDialog();
		dialog.setSize(500, 350);
		dialog.setLayout(new GridLayout(3, 1));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModal(true);

		// Create a label with the message
		JLabel label = new JLabel("DO YOU WANT TO SAVE THE GAME BEFORE GOING TO THE MAIN MENU?", SwingConstants.CENTER);
		dialog.add(label);

		// Create a panel for the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// Create the "Save & Exit" button
		JButton saveAndExitButton = new JButton("SAVE & EXIT");
		saveAndExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Prompt user to save the game
				saveGameState("saved_game.txt");
				GoToMenu(); // Proceed to main menu after saving
				dialog.dispose(); // Close the dialog
			}
		});

		// Create the "Exit Without Saving" button
		JButton exitWithoutSavingButton = new JButton("EXIT WITHOUT SAVING");
		exitWithoutSavingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("User chose to exit without saving.");
				GoToMenu(); // Proceed to main menu without saving
				dialog.dispose(); // Close the dialog
			}
		});

		// Create the "Cancel" button
		JButton cancelButton = new JButton("CANCEL");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("User chose to cancel.");
				dialog.dispose(); // Close the dialog
			}
		});

		// Add buttons to the button panel
		buttonPanel.add(saveAndExitButton);
		buttonPanel.add(exitWithoutSavingButton);
		buttonPanel.add(cancelButton);

		// Add the button panel to the dialog
		dialog.add(buttonPanel);

		// Center the dialog on the screen
		dialog.setLocationRelativeTo(null);

		// Show the dialog
		dialog.setVisible(true);
	}

	/**
	 * Method saves the state of the game to the specified file
	 * 
	 * @param filename the name of file holding the state of the board, turn, and
	 *                 history
	 */
	private void saveGameState(String filename) {
		try {
			// Save game state to "saved_game.txt"
			FileWriter writer = new FileWriter(filename);
			writer.write("{\n");
			writer.write("  \"gameState\": {\n");

			// Save the board state
			writer.write("    \"board\": [\n");
			Piece[][] board = gameBoard.getBoard();
			// go over each cell and build them up
			for (int i = 0; i < board.length; i++) {
				writer.write("      [");
				for (int j = 0; j < board[i].length; j++) {
					if (board[i][j] != null) {
						Piece piece = board[i][j];
						writer.write("{");
						writer.write("\"color\": \"" + piece.getColor().name() + "\", ");
						writer.write("\"king\": " + piece.isKing() + ", ");
						writer.write("\"row\": " + piece.getRow() + ", ");
						writer.write("\"col\": " + piece.getColumn());
						writer.write("}");
					} else {
						writer.write("null");
					}

					// separate by comma
					if (j < board[i].length - 1)
						writer.write(", ");
				}
				// Finish cell line
				writer.write("]");

				// separate cell lines by commas
				if (i < board.length - 1)
					writer.write(",");
				writer.write("\n");
			}
			writer.write("    ],\n");

			// Save the turn
			writer.write("    \"turn\": \"" + (gameManager.getCurrentTurn() == model.Color.WHITE ? "WHITE" : "BLACK")
					+ "\",\n");

			// Save the move history
			writer.write("    \"history\": [\n");
			String[] historyLines = moveHistoryArea.getText().split("\n");
			for (int i = 0; i < historyLines.length; i++) {
				writer.write("      \"" + historyLines[i].replace("\"", "\\\"") + "\"");
				if (i < historyLines.length - 1)
					writer.write(",");
				writer.write("\n");
			}
			writer.write("    ]\n");

			writer.write("  }\n");
			writer.write("}\n");
			writer.close();

			// Save the game mode to "game_mode.txt"
			FileWriter modeWriter = new FileWriter("game_mode.txt");
			modeWriter.write(Checkers.mode.name());
			modeWriter.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	/**
	 * Method sets the turn for the game 
	 * 
	 * @param turn the color of the person who's turn it is
	 */
	public void setTurn(model.Color turn) {
		this.turn = turn; 
	}


	/**
	 * Sets up the timer to update the time label every second and write the elapsed
	 * time to "time_remaining.txt".
	 */
	private void setupTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				elapsedTime++;
				int minutes = elapsedTime / 60;
				int seconds = elapsedTime % 60;
				timerLabel.setText(String.format("%d:%02d", minutes, seconds));

				// Write elapsed time to the "time_remaining.txt" file
				try (FileWriter writer = new FileWriter("time_remaining.txt", false)) { 
					
					writer.write(String.valueOf(elapsedTime)); // Write elapsed time in seconds
				} catch (IOException ex) {
					ex.printStackTrace();				}
			}
		});
		timer.start(); // Start the timer
	}

	/**
	 * Method calls game managers on piece click to handle a move or selection 
	 * in the game
	 * 
	 * @param row the row of the cell
	 * @param col the col of the cell chosen 
	 */
	@Override
	public void handleCellClick(int row, int col) {

		// call game manager to handle move
		gameManager.OnPieceClick(row, col);

		// get current score after move 
		int[] currentScore = gameManager.calculateScore();

		// adjust score each time 
		String blackScore = "x" + currentScore[0];
		String whiteScore = "x" + currentScore[1];
		capturedPiecesLabelWhite.setText(whiteScore);
		capturedPiecesLabelBlack.setText(blackScore);
		
		// repaint and validate the timerPanel
		timerPanel.revalidate();
		timerPanel.repaint();
	}

	/*
	 * Method updates the board to show the current turn and to prompt the 
	 * next move 
	 */
	private void updateBoard() {
		// get the current turn adn set the curr text to the persons text 
		String curr_turn = "Current Turn: ";
		if (gameManager.getCurrentTurn().name().equals("WHITE")) {
			curr_turn += gameManager.p1name();
		} else {
			curr_turn += gameManager.p2name();
		}
		curr.setText(curr_turn);
		
		// go to next move 
		gameManager.NextMove();

	}

	/**
	 * Method ends game is the game is over and show the dialog for a won game
	 * 
	 * @param event and event representing if the same was over or not 
	 */
	@Override
	public void GameOverOccurred(GameOverEvent event) {
		// get winner
		String winner = event.getWinner();
		
		// show dialog 
		showGameOverDialog(winner);
	}

	/**
	 * Method gets the moved pieces from the from the event and updates
	 * the move history panel 
	 * 
	 * @param event the event of a moved piece(s)
	 */
	@Override
	public void GetMovedPieces(GetMovedPieces event) {
		// get the pieces that moved 
		ArrayList<Piece> pieces = event.getPieces();
		
		// make a new MoveData object 
		MoveData move = new MoveData(pieces.get(0), pieces.get(pieces.size() - 1),
				new ArrayList<>(pieces.subList(1, pieces.size() - 1)));
		
		// add move to move history 
		moveHistory.add(move);

		// Append the new move to the history area instead of replacing it
		if (moveHistoryArea != null) {
			String currentText = moveHistoryArea.getText(); // Get existing text
			moveHistoryArea.setText(currentText + move.toString() + "\n"); // Append new move
			moveHistoryScrollPane.getVerticalScrollBar()
					.setValue(moveHistoryScrollPane.getVerticalScrollBar().getMaximum());
		}

		// Make move file if it does not already exist and then add to it 
		if (moveHistoryFile != null) {
			try (FileWriter fw = new FileWriter(moveHistoryFile, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				out.println(move);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	/**
	 * Method updates the MoveHistory panel 
	 */
	private void updateMoveHistory() {
		// gets all of the history from the move panel 
		StringBuilder historyText = new StringBuilder();
		for (MoveData move : moveHistory) {
			historyText.append(move.toString()).append("\n");
		}
		
		// sets the text to the history found and tranforms it to the 
		// scrollPane 
		moveHistoryArea.setText(historyText.toString());
		moveHistoryScrollPane.getVerticalScrollBar()
				.setValue(moveHistoryScrollPane.getVerticalScrollBar().getMaximum());
	}

	/**
	 * Method shows the game over dialog 
	 * 
	 * @param winner the winner of the game 
	 */
	/**
	 * Displays a "Game Over" dialog with the winner's name
	 * and a button to return to the main menu.
	 *
	 * @param winner The name of the winner to display.
	 */
	private void showGameOverDialog(String winner) {
	    // Create a modal dialog for the "Game Over" message
	    JDialog dialog = new JDialog();
	    dialog.setTitle("Game Over!"); // Set the dialog title
	    dialog.setLayout(new BorderLayout()); // Use BorderLayout for positioning components

	    // Create a label to display the winner's name
	    JLabel messageLabel = new JLabel(winner, JLabel.CENTER); // Center-align the text

	    // Create a button to navigate back to the main menu
	    JButton mainMenuButton = new JButton("Main Menu");

	    // Add an ActionListener using an anonymous class to handle the button click
	    mainMenuButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Navigate to the main menu and close the dialog
	            GoToMenu(dialog);
	        }
	    });

	    // Create a panel to hold the button
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.add(mainMenuButton); // Add the button to the panel

	    // Add components to the dialog
	    dialog.add(messageLabel, BorderLayout.CENTER); // Add the message label to the center
	    dialog.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom

	    // Configure dialog settings
	    dialog.pack(); // Adjust the dialog size based on its content
	    dialog.setLocationRelativeTo(Checkers.window); // Center the dialog relative to the main window
	    dialog.setVisible(true); // Display the dialog

	    // Add a WindowListener to handle the window-closing event
	    dialog.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            // Navigate to the main menu when the dialog is closed
	            GoToMenu((JDialog) e.getSource());
	        }
	    });
	}

	/**
	 * Method returns the user to the menu
	 * 
	 * @param dialog dialog pertaining to a menu switch 
	 */
	public void GoToMenu(JDialog dialog) {
		// change game state to the menu 
		Checkers.game_state = new Menu();
		
		// set up Checkers Window 
		Checkers.game_state.setup(Checkers.window);
		Checkers.window.setVisible(true);
		
		// close dialog
		dialog.dispose();
	}

	// used for menu button
	public void GoToMenu() {
		Checkers.game_state = new Menu();
		Checkers.game_state.setup(Checkers.window);
		Checkers.window.setVisible(true);
	}

	/**
	 * Method stops the time 
	 */
	public void stopTimer() {
		// if the timer exists stop it 
		if (timer != null) {
			timer.stop();
		}
	}

	/**
	 * Overloaded method not of use to the class, but necessary for
	 * compilation 
	 */
	@Override
	public void setup(JFrame window) {
		// throw error to show missing support 
		throw new UnsupportedOperationException("Unimplemented method 'setup'");
	}

	
}

