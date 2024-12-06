package view;

import javax.swing.*;

import controller.GameModeEnum;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Menu implements State {
    private GameModeEnum gameMode;
    @Override
    public void setup(JFrame window) {
        // creating the base panel with BorderLayout
        ImageIcon backgroundImageIcon = new ImageIcon("./assets/bg2.jpg");
        Image backgroundImage = backgroundImageIcon.getImage();
        // Create a panel with overridden paintComponent
        JPanel basePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        basePanel.setLayout(new BorderLayout());      
        // basePanel.setBackground(new Color(77, 135, 50));
        
        // creating a panel for the title and buttons
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 80, 10, 10);

        
        // buttons
        JButton playButton = new JButton("NEW GAME");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        gridPanel.add(playButton, gbc);
        
        JButton contButton = new JButton("CONTINUE");
        contButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gridPanel.add(contButton, gbc);
        
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 2;
        gridPanel.add(exitButton, gbc);
        
        // styling buttons
        styleButton(playButton);
        styleButton(contButton);
        styleButton(exitButton);
        
        // adding the grid panel to the center of the base panel
        basePanel.add(gridPanel, BorderLayout.WEST);
        playButton.addActionListener(e -> {
            JDialog modePromt = new JDialog(Checkers.window, "Select Game Mode", true); // Make the dialog modal
            modePromt.setLocationRelativeTo(Checkers.window);
            modePromt.setSize(new Dimension(300, 200));
            modePromt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            modePromt.setLayout(new GridLayout(3, 1)); // Layout for proper alignment

            JLabel modeString = new JLabel("Please Choose The Mode", SwingConstants.CENTER);
            JButton playerVsPlayer = new JButton("Player VS Player");
            JButton playerVsComputer = new JButton("Player Vs Computer");

            styleButton(playerVsPlayer);
            styleButton(playerVsComputer);

            // Add action listeners to set the game mode and close the dialog
            playerVsPlayer.addActionListener(event -> {
                pvp(); // Set game mode to PvP
                modePromt.dispose(); // Close the dialog
            });

            playerVsComputer.addActionListener(event -> {
                pvc(); // Set game mode to PvC
                modePromt.dispose(); // Close the dialog
            });

            // Add components to the dialog
            modePromt.add(modeString);
            modePromt.add(playerVsPlayer);
            modePromt.add(playerVsComputer);

            modePromt.setVisible(true); // Show the modal dialog, execution will pause here until dialog is closed

            // Start the game only after mode selection
            Game newGame = new Game();
            Checkers.game_state = newGame;
            newGame.setup(Checkers.window, this.gameMode);
        });
        
        contButton.addActionListener(e -> {
            try {
                // Read the game mode from a file
                String gameMode;
                try (BufferedReader reader = new BufferedReader(new FileReader("game_mode.txt"))) {
                    gameMode = reader.readLine().trim();
                    if (gameMode.equals("PvP")) {
                    	pvp(); 
                    } else {
                    	pvc(); 
                    }
                    
                }
 
                // Create a new Game instance based on the loaded game mode
                Game loadedGame = new Game();
                Checkers.game_state = loadedGame;

                // Load the saved game state
                loadedGame.loadGameState("saved_game.txt");
                loadedGame.setupLoaded(Checkers.window);

                System.out.println("Loaded Game Mode: " + gameMode);

                // Additional logic based on game mode can be added later
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    Checkers.window,
                    "Failed to load game mode. Please ensure the game_mode.txt file exists.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });




        gridPanel.add(contButton, gbc);
        exitButton.addActionListener(e -> System.exit(0));
        
        // setting up the window
        window.getContentPane().removeAll();
        window.add(basePanel);
        window.revalidate();
        window.repaint();
    }

    private void pvc() {
        Checkers.mode = GameModeEnum.PvC;
    }

    private void pvp()
    {
    	Checkers.mode = GameModeEnum.PvP;
    }

    public static void styleButton(JButton button) {
        Dimension size = new Dimension(400, 60);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        // Color defaultColor = new Color(0, 0, 0);
        Color defaultColor = Color.WHITE;
        button.setOpaque(true);
        button.setBackground(defaultColor);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(55, 55, 55));
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(80, 80, 80));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(55, 55, 55));
            }
        });
    }

	@Override
	public void handleCellClick(int row, int col) {
		
	}


    @Override
    public void setup(JFrame window, GameModeEnum gameMode) {
        throw new UnsupportedOperationException("Unimplemented method 'setup'");
    }
}