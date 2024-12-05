package view;

import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import controller.GameManager;
import model.*;

public class Game implements State {
    private GameBoard gameBoard; // Backend game board
    private GameManager gameManager;
    private JPanel gamePanel; // UI panel for the board
    private Piece selectedPiece; // Currently selected piece
    private Timer timer; // Swing Timer
    private JLabel timerLabel; // Label to display the timer
    private JLabel turnLabel; // Label to display the turn
    private JLabel scoreLabel;
    private JLabel scoreLabel1;
    private int elapsedTime = 0; // Time in seconds
    private java.util.List<Cell> highlightedCells = new ArrayList<>(); // Highlighted cells

   
    @Override
    public void setup(JFrame window) {
        // Initialize the game board
        gameBoard = new GameBoard();

        // Main panel setup
        JPanel mainGamePanel = new JPanel(new BorderLayout());

        // Timer panel setup
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerLabel = new JLabel("Time: 0:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerPanel.add(timerLabel);
        timerPanel.setBackground(new Color(77, 135, 50));
        timerPanel.setPreferredSize(new Dimension(800, 50));

        // Score panel setup
        JPanel scorePanel = new JPanel(); 
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS)); // Arrange labels vertically
        scorePanel.setBackground(new Color(77, 135, 50));
        scorePanel.setPreferredSize(new Dimension(100, 500)); // Adjust width as needed

        scoreLabel = new JLabel("White Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 10));
        scoreLabel.setForeground(Color.WHITE);

        scoreLabel1 = new JLabel("Black Score: 0");
        scoreLabel1.setFont(new Font("Arial", Font.BOLD, 10));
        scoreLabel1.setForeground(Color.WHITE);

        // Add score labels to the score panel
        scorePanel.add(Box.createVerticalStrut(20)); // Spacer for aesthetics
        scorePanel.add(scoreLabel);
        scorePanel.add(Box.createVerticalStrut(20)); // Spacer between labels
        scorePanel.add(scoreLabel1);

        // Wrapper panel for the game board
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.setBackground(new Color(77, 135, 50));
        wrapperPanel.setPreferredSize(new Dimension(800, 800));

        // Game panel setup
        gamePanel = new JPanel(new GridLayout(8, 8));
        gamePanel.setPreferredSize(new Dimension(700, 700));

        gameManager = new GameManager(gamePanel, gameBoard);

        // Add cells to the game panel
        updateBoard();

        // Add the game panel to the wrapper
        wrapperPanel.add(gamePanel);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(45, 0, 0, 0));

        // Add panels to the main game panel
        mainGamePanel.add(timerPanel, BorderLayout.NORTH); // Timer at the top
        mainGamePanel.add(wrapperPanel, BorderLayout.CENTER); // Board in the center
        mainGamePanel.add(scorePanel, BorderLayout.EAST); // Score panel on the right side

        // Setup the timer
        setupTimer();

        // Setup the window
        window.getContentPane().removeAll();
        window.add(mainGamePanel);
        window.revalidate();
        window.repaint();
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
                timerLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
            }
        });
        timer.start(); // Start the timer
    }

    @Override
    public void handleCellClick(int row, int col) {

        gameManager.OnPieceClick(row, col);
        int[] currentScore = gameManager.calculateScore();  
        scoreLabel.setText("White Score: " + currentScore[1]);
        scoreLabel1.setText("Black Score: " + currentScore[0]);
        // Piece piece = gameBoard.getPiece(row, col);

        // if (selectedPiece == null && piece != null) {
        //     // Select a piece and highlight moves
        //     selectedPiece = piece;
        //     highlightPossibleMoves();
        // } else if (selectedPiece != null) {
        //     // Check if clicked on a valid move
        //     for (Cell cell : highlightedCells) {
        //         if (cell.getXCoord() == row && cell.getYCoord() == col) {
        //             // Check if the move is a jump (capture)
        //             int middleRow = (selectedPiece.getRow() + row) / 2;
        //             int middleCol = (selectedPiece.getColumn() + col) / 2;

        //             if (Math.abs(selectedPiece.getRow() - row) == 2 &&
        //                 Math.abs(selectedPiece.getColumn() - col) == 2) {
        //                 // Remove the captured piece
        //                 gameBoard.removePiece(middleRow, middleCol);
        //             }

        //             // Move the selected piece
        //             gameBoard.move(selectedPiece, row, col);
        //             selectedPiece = null; // Deselect piece
        //             clearHighlights(); // Clear highlighted moves
        //             updateBoard(); // Refresh UI
        //             return;
        //         }
        //     }
        //     // Deselect if invalid move
        //     selectedPiece = null;
            // clearHighlights();
            //updateBoard();
        }

    private void highlightPossibleMoves() {
        ArrayList<ArrayList<int[]>> moves = Move.getPossibleMoves(selectedPiece, gameBoard.getBoardCopy());
        for (ArrayList<int[]> path : moves) {
            for (int i = 1; i < path.size(); i++) {
                int[] move = path.get(i);
                Cell cell = (Cell) gamePanel.getComponent(move[0] * 8 + move[1]);
                cell.highlightCell(true);
                highlightedCells.add(cell);
            }
        }
    }

    private void clearHighlights() {
        for (Cell cell : highlightedCells) {
            cell.highlightCell(false);
        }
        highlightedCells.clear();
    }

    private void updateBoard() {
        System.out.println("UPDATING BOARD...");
        gamePanel.removeAll();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color color = (row + col) % 2 == 0 ? new Color(246, 187, 146) : new Color(152, 86, 40);
                Piece piece = gameBoard.getPiece(row, col);
                Cell cell = new Cell(color, piece, row, col);
                gamePanel.add(cell);
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
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
