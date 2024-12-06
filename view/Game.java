package view;

import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import controller.GameManager;
import controller.GameModeEnum;
import controller.GameManager.GameOverEvent;
import model.*;


public class Game implements State, GameManager.GameOverListener {
    private GameBoard gameBoard; // Backend game board
    private GameManager gameManager;
    private JPanel gamePanel; // UI panel for the board
    private Timer timer; // Swing Timer
    private JLabel timerLabel; // Label to display the timer
    
    private JLabel capturedPiecesLabelWhite;
    private JLabel capturedPiecesLabelBlack;
    private JPanel timerPanel;

    private model.Color turn; 
    
    private int elapsedTime = 0; // Time in seconds
    private boolean gameLoaded = false; 
   
    @Override
    public void setup(JFrame window) {
        initializeUI(window);
        initializeNewGame(); // Specific logic for a new game
    }

    public void setupLoaded(JFrame window) {
        initializeUI(window);
        initializeLoadedGame(); // Specific logic for a loaded game
    }

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
        mainGamePanel.setLayout(new BorderLayout());
        mainGamePanel.setOpaque(false);

        // Timer panel setup
        if (timerPanel == null) {
            timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            timerPanel.setOpaque(false); // Ensure transparency over background

            JButton menu_btn = new JButton("MENU");
            Menu.styleButton(menu_btn);
            menu_btn.addActionListener(e -> Does_User_Want());
            Dimension buttonSize = new Dimension(60, 30);
            menu_btn.setPreferredSize(buttonSize);
            menu_btn.setMinimumSize(buttonSize);
            menu_btn.setMaximumSize(buttonSize);
            timerPanel.add(menu_btn);

            timerLabel = new JLabel("0:00");
            timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            timerLabel.setForeground(Color.WHITE);
            timerPanel.add(timerLabel);

            // Create panel for captured white pieces
            capturedPiecesLabelWhite = new JLabel("x0");
            capturedPiecesLabelWhite.setFont(new Font("Arial", Font.BOLD, 20));
            capturedPiecesLabelWhite.setForeground(Color.WHITE);

            JPanel piecePanelWhite = new JPanel(new FlowLayout(FlowLayout.LEFT));
            piecePanelWhite.setOpaque(false); // Transparent background
            CirclePanelWhite circleWhite = new CirclePanelWhite();
            circleWhite.setPreferredSize(new Dimension(30, 30));
            circleWhite.setOpaque(true);
            piecePanelWhite.add(circleWhite);
            piecePanelWhite.add(capturedPiecesLabelWhite);

            // Add white piece panel to timer panel
            timerPanel.add(piecePanelWhite);

            // Create panel for captured black pieces
            capturedPiecesLabelBlack = new JLabel("x0");
            capturedPiecesLabelBlack.setFont(new Font("Arial", Font.BOLD, 20));
            capturedPiecesLabelBlack.setForeground(Color.WHITE);

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
        wrapperPanel.setOpaque(false); // Transparent for background visibility
        wrapperPanel.setPreferredSize(new Dimension(800, 800));

        // Game panel setup
        if (gamePanel == null) {
            gamePanel = new JPanel(new GridLayout(8, 8));
            gamePanel.setPreferredSize(new Dimension(700, 700));
        }
        wrapperPanel.add(gamePanel);

        // Add panels to the main game panel
        mainGamePanel.add(timerPanel, BorderLayout.NORTH); // Timer at the top
        mainGamePanel.add(wrapperPanel, BorderLayout.CENTER); // Board in the center

        // Setup the window
        window.getContentPane().removeAll();
        window.add(mainGamePanel);
        window.revalidate();
        window.repaint();
    }


    private void initializeNewGame() {
        gameBoard = new GameBoard();
        gameManager = new GameManager(gamePanel, gameBoard, this, GameModeEnum.PvC);
        updateBoard();
    }

    private void initializeLoadedGame() {
        if (gameBoard == null) {
            System.err.println("GameBoard is not initialized. Initializing a new board.");
            gameBoard = new GameBoard();
        }

        gameManager = new GameManager(gamePanel, gameBoard, this, GameModeEnum.PvC);

        // Set the turn in GameManager
        gameManager.setTurn(this.turn);

        // Refresh the panel to reflect the loaded board
        gamePanel.removeAll();
        updateBoard();
        gamePanel.revalidate();
        gamePanel.repaint();

        // Handle the first turn
        gameManager.handleTurn();

        System.out.println("Game board set up for loaded game.");
    }


    public void loadGameState(String filename) {
        if (gameBoard == null) {
            System.err.println("GameBoard is not initialized. Initializing a new board.");
            gameBoard = new GameBoard();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isBoardSection = false;
            Piece[][] board = new Piece[8][8]; // Prepare an empty board
            String turn = "WHITE"; // Default turn

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"board\": [")) {
                    isBoardSection = true;
                    continue;
                }

                if (isBoardSection) {
                    if (line.equals("]") || line.equals("],")) {
                        isBoardSection = false;
                        continue;
                    }

                    String[] cells = line.replace("[", "").replace("]", "").split("},");
                    int colIndex = 0; // Reset column index for each row

                    for (String cell : cells) {
                        cell = cell.trim();
                        if (cell.equals("null")) {
                            board[board.length - 1][colIndex] = null; // Ensure placement respects current row
                        } else {
                            cell = cell.replace("{", "").replace("}", "").trim();
                            String[] attributes = cell.split(",");
                            String color = "";
                            boolean isKing = false;
                            int row = board.length - 1, col = colIndex; // Initialize row and column

                            for (String attribute : attributes) {
                                String[] keyValue = attribute.split(":");
                                if (keyValue.length < 2) continue;
                                String key = keyValue[0].trim().replace("\"", "");
                                String value = keyValue[1].trim().replace("\"", "");

                                switch (key) {
                                    case "color":
                                        color = value;
                                        break;
                                    case "king":
                                        isKing = Boolean.parseBoolean(value);
                                        break;
                                    case "row":
                                        row = Integer.parseInt(value); // Parse the actual row
                                        break;
                                    case "col":
                                        col = Integer.parseInt(value); // Parse the actual column
                                        break;
                                }
                            }

                            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                                System.err.println("Invalid color value in save file: " + color);
                                continue;
                            }

                            Piece piece = new Piece(color.equals("WHITE") ? model.Color.WHITE : model.Color.BLACK, row, col);
                            if (isKing) {
                                piece.ToKing();
                            }
                            board[row][col] = piece; // Place the piece in its correct position
                        }
                        colIndex++; // Move to the next column
                    }
                }

                if (line.startsWith("\"turn\":")) {
                    turn = line.split(":")[1].trim().replace("\"", "").replace(",", "");
                }
            }

            // Use setBoard to apply the loaded board
            gameBoard.setBoard(board);

            // Update the turn
            this.setTurn(turn.equals("BLACK") ? model.Color.BLACK : model.Color.WHITE);

            gameLoaded = true;
            System.out.println("Game loaded successfully from " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load game.");
        }
    }


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

    // Add this method for saving game state
    private void saveGameState(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("{\n");
            writer.write("  \"gameState\": {\n");

            // Save the board state
            writer.write("    \"board\": [\n");
            Piece[][] board = gameBoard.getBoard();
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
                    if (j < board[i].length - 1) writer.write(", ");
                }
                writer.write("]");
                if (i < board.length - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("    ],\n");

            // Save the turn
            writer.write("    \"turn\": \"" + (turn == model.Color.WHITE ? "WHITE" : "BLACK") + "\"\n");

            writer.write("  }\n");
            writer.write("}\n");
            writer.close();

            // Debugging Output
            System.out.println("Game saved successfully to " + filename);
            System.out.println("Saved Turn: " + turn);
            System.out.println("Saved Board:");
            for (Piece[] row : board) {
                for (Piece piece : row) {
                    System.out.print((piece == null ? "null" : piece.toString()) + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save game.");
        }
    }
    
    public void setTurn(model.Color turn) {
        this.turn = turn; // Assuming `turn` is a field in the Game class
    }


    /**
     * Sets up the timer to update the time label every second.
     */
    private void setupTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                int minutes = elapsedTime / 60;
                int seconds = elapsedTime % 60;
                timerLabel.setText(String.format("%d:%02d", minutes, seconds));
            }
        });
        timer.start(); // Start the timer
    }

    @Override
    public void handleCellClick(int row, int col) {

        gameManager.OnPieceClick(row, col);
        
        int[] currentScore = gameManager.calculateScore();  

        String blackScore = "x" + currentScore[0];
        String whiteScore = "x" + currentScore[1];
        capturedPiecesLabelWhite.setText(whiteScore);
        capturedPiecesLabelBlack.setText(blackScore);
        timerPanel.revalidate();
        timerPanel.repaint();
        }

    private void updateBoard() {
        gameManager.NextMove();
    }

     @Override
    public void GameOverOccurred(GameOverEvent event) {
        String winner = event.getWinner();
        showGameOverDialog(winner);
    }

    private void showGameOverDialog(String winner) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Game Over!");
        dialog.setLayout(new BorderLayout());


        JLabel messageLabel = new JLabel(winner, JLabel.CENTER);
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(e -> GoToMenu(dialog) );

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(mainMenuButton);

        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);


        dialog.pack();
        dialog.setLocationRelativeTo(Checkers.window);
        dialog.setVisible(true);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GoToMenu((JDialog) e.getSource()); 
            }
        });
    }

    public void GoToMenu(JDialog dialog) {
        Checkers.game_state = new Menu();
        Checkers.game_state.setup(Checkers.window);
        Checkers.window.setVisible(true);
        dialog.dispose();
    }

    // used for menu button
    public void GoToMenu() {
        Checkers.game_state = new Menu();
        Checkers.game_state.setup(Checkers.window);
        Checkers.window.setVisible(true);
    }

    /**
     * Stops the timer.
     */
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}


class CirclePanelWhite extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}

class CirclePanelBlack extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g.setColor(Color.WHITE);
        g.drawOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}