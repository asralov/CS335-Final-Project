package view;

import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
    private JLabel turnLabel; // Label to display the turn
    private JLabel capturedPiecesLabelWhite;
    private JLabel capturedPiecesLabelBlack;
    private JPanel timerPanel;

    private int elapsedTime = 0; // Time in seconds

   
    @Override
    public void setup(JFrame window) {
        // Initialize the game board
        gameBoard = new GameBoard();

        // Main panel setup
        // JPanel mainGamePanel = new JPanel(new BorderLayout());
        // creating the base panel with BorderLayout
        ImageIcon bg_icon = new ImageIcon("./assets/game_bg.jpg");
        Image bg_img = bg_icon.getImage();
        // Create a panel with overridden paintComponent
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
        timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton menu_btn = new JButton("MENU");
        Menu.styleButton(menu_btn);
        menu_btn.addActionListener(e -> Does_User_Want());
        Dimension s = new Dimension(60, 30);
        menu_btn.setPreferredSize(s);
        menu_btn.setMinimumSize(s);
        menu_btn.setMaximumSize(s);

        timerPanel.add(menu_btn);
        timerLabel = new JLabel("0:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        timerPanel.add(timerLabel);

        
        // timerPanel.setBackground(new Color(77, 135, 50));
        timerPanel.setPreferredSize(new Dimension(1000, 50));

        // // Score panel setup
        // JPanel scorePanel = new JPanel(); 
        // scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS)); // Arrange labels vertically
        // // scorePanel.setBackground(new Color(77, 135, 50));
        // scorePanel.setPreferredSize(new Dimension(100, 500)); // Adjust width as needed


        // scoreLabel = new JLabel("White Score: 0");
        // scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        // scoreLabel.setForeground(Color.WHITE);

        // Create a JLabel for the captured piece count
        capturedPiecesLabelWhite = new JLabel("x0"); // Initial count
        capturedPiecesLabelWhite.setFont(new Font("Arial", Font.BOLD, 20));
        capturedPiecesLabelWhite.setForeground(Color.WHITE);
        // capturedPiecesLabelWhite.setBackground(new Color(0, 0, 0, 0));

        // Create a panel to hold the image and text
        JPanel piecePanelWhite = new JPanel();
        piecePanelWhite.setLayout(new FlowLayout(FlowLayout.LEFT));
        piecePanelWhite.setBackground(new Color(0, 0, 0, 0)); // Transparent background if needed

        // Add the image and text to the panel
        CirclePanelWhite circleWhite = new CirclePanelWhite();
        circleWhite.setPreferredSize(new Dimension(30, 30));
        circleWhite.setOpaque(true);
        piecePanelWhite.add(circleWhite);
        piecePanelWhite.add(capturedPiecesLabelWhite);

        // Add the panel to your GUI layout
        timerPanel.add(piecePanelWhite); // Replace 'yourPanel' with your container

        // Create a JLabel for the captured piece count
        capturedPiecesLabelBlack = new JLabel("x0"); // Initial count
        capturedPiecesLabelBlack.setFont(new Font("Arial", Font.BOLD, 20));
        capturedPiecesLabelBlack.setForeground(Color.WHITE);
        // capturedPiecesLabelBlack.setBackground(new Color(0, 0, 0, 0));


        // Create a panel to hold the image and text
        JPanel piecePanelBlack = new JPanel();
        piecePanelBlack.setLayout(new FlowLayout(FlowLayout.LEFT));
        piecePanelBlack.setBackground(new Color(0, 0, 0, 0)); // Transparent background if needed

        // Add the image and text to the panel
        CirclePanelBlack circleBlack = new CirclePanelBlack();
        circleBlack.setPreferredSize(new Dimension(30, 30));
        circleBlack.setOpaque(true);
        piecePanelBlack.add(circleBlack);
        piecePanelBlack.add(capturedPiecesLabelBlack);

        timerPanel.add(piecePanelBlack);

        // scoreLabel1 = new JLabel("Black Score: 0");
        // scoreLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        // scoreLabel1.setForeground(Color.WHITE);

        

        // Add score labels to the score panel
        // scorePanel.add(Box.createVerticalStrut(20)); // Spacer for aesthetics
        // scorePanel.add(scoreLabel);
        // scorePanel.add(Box.createVerticalStrut(20)); // Spacer between labels
        // scorePanel.add(scoreLabel1);

        // Wrapper panel for the game board
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // wrapperPanel.setBackground(new Color(77, 135, 50));
        wrapperPanel.setPreferredSize(new Dimension(800, 800));

        // Game panel setup
        gamePanel = new JPanel(new GridLayout(8, 8));
        gamePanel.setPreferredSize(new Dimension(700, 700));

        gameManager = new GameManager(gamePanel, gameBoard, this, GameModeEnum.PvC);

        // Add cells to the game panel
        updateBoard();

        // Add the game panel to the wrapper
        wrapperPanel.add(gamePanel);
        // wrapperPanel.setBorder(BorderFactory.createEmptyBorder(45, 0, 0, 0));

        // Add panels to the main game panel
        mainGamePanel.add(timerPanel, BorderLayout.NORTH); // Timer at the top
        mainGamePanel.add(wrapperPanel, BorderLayout.CENTER); // Board in the center
        // mainGamePanel.add(scorePanel, BorderLayout.EAST); // Score panel on the right side

        // Setup the timer
        setupTimer();
        timerPanel.setOpaque(false);
        wrapperPanel.setOpaque(false);
        // scorePanel.setOpaque(false);

        // Setup the window
        window.getContentPane().removeAll();
        window.add(mainGamePanel);
        window.revalidate();
        window.repaint();
    }


    private void Does_User_Want() {
        // Create the dialog
        JDialog dialog = new JDialog();
        // dialog.setTitle("");
        dialog.setSize(500, 350);
        dialog.setLayout(new GridLayout(2, 1));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);

        // Create a label with the message
        JLabel label = new JLabel("DO YOU WANT TO GO THE MAIN MENU?", SwingConstants.CENTER);
        dialog.add(label);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create the "Yes" button
        JButton yesButton = new JButton("YES");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User chose to go to the menu.");
                // Add logic for "Yes" button here
                GoToMenu();
                dialog.dispose(); // Close the dialog
            }
        });

        // Create the "No" button
        JButton noButton = new JButton("NO");
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("User chose not to go to the menu.");
                // Add logic for "No" button here
                dialog.dispose(); // Close the dialog
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Add the button panel to the dialog
        dialog.add(buttonPanel);

        // Center the dialog on the screen
        dialog.setLocationRelativeTo(null);

        // Show the dialog
        dialog.setVisible(true);
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
        // Set background to match parent and ensure no black background appears
        // capturedPiecesLabelBlack.setBackground(new Color(0,0,0,255));
        // capturedPiecesLabelWhite.setBackground(new Color(0,0,0,255));
        // capturedPiecesLabelBlack.setOpaque(true); // or true, depending on your needs
        // capturedPiecesLabelWhite.setOpaque(true); // or true, depending on your needs

        int[] currentScore = gameManager.calculateScore();  

        String blackScore = "x" + currentScore[0];
        String whiteScore = "x" + currentScore[1];
        capturedPiecesLabelWhite.setText(whiteScore);
        capturedPiecesLabelBlack.setText(blackScore);
        timerPanel.revalidate();
        timerPanel.repaint();


        // Ensure repaint to reflect changes
        // capturedPiecesLabelBlack.repaint();
        // capturedPiecesLabelWhite.repaint();
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

    private void updateBoard() {
        gameManager.NextMove();
        // System.out.println("UPDATING BOARD...");
        
        // gamePanel.removeAll();
        // for (int row = 0; row < 8; row++) {
        //     for (int col = 0; col < 8; col++) {
        //         Color color = (row + col) % 2 == 0 ? new Color(246, 187, 146) : new Color(152, 86, 40);
        //         Piece piece = gameBoard.getPiece(row, col);
        //         Cell cell = new Cell(color, piece, row, col);
        //         gamePanel.add(cell);
        //     }
        // }
        // ArrayList<Piece> pieces = gameManager.GetMovablePieces();
        // for (int i = 0; i < pieces.size(); i++) {
    	// 	//HighLightCell(pieces.get(i).getColumn(), pieces.get(i).getRow());
		// 	Cell cellToHighlight = (Cell) gamePanel.getComponent(pieces.get(i).getRow() * 8 + pieces.get(i).getColumn());
		// 	cellToHighlight.highlightCell(true, new Color(122, 64, 121));
    	// }
        // gamePanel.revalidate();
        // gamePanel.repaint();
        
    }

     @Override
    public void GameOverOccurred(GameOverEvent event) {
        String winner = event.getWinner();
        showGameOverDialog(winner);
    }

    private void showGameOverDialog(String winner) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Game Over!");
        // dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        // dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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
                GoToMenu((JDialog) e.getSource()); //Get the dialog from the event
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



// class CirclePanel extends JPanel {
//     private int diameter;

//     public CirclePanel(int diameter, Color color) {
//         this.diameter = diameter;
//         this.setPreferredSize(new Dimension(diameter, diameter)); // Set the preferred size of the circle
//         this.setBackground(color); // Transparent background
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         Graphics2D g2d = (Graphics2D) g;
//         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//         g2d.setColor(Color.WHITE); // Set the circle color to white
//         g2d.fillOval(0, 0, diameter, diameter); // Draw the circle
//     }
// }

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