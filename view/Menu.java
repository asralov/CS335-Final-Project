package view;

import javax.swing.*;
import controller.GameModeEnum;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The Menu class represents the main menu of the Checkers game.
 * It provides options for starting a new game, continuing a saved game,
 * and exiting the application. It also supports setting the game mode
 * (Player vs Player or Player vs Computer).
 */
public class Menu implements State {
    private GameModeEnum gameMode;

    /**
     * Sets up the main menu interface.
     *
     * @param window The main application window to display the menu.
     */
    @Override
    public void setup(JFrame window) {
        // Create the base panel with a custom background
        ImageIcon backgroundImageIcon = new ImageIcon("./assets/bg2.jpg");
        Image backgroundImage = backgroundImageIcon.getImage();
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

        // Create a grid panel for the title and buttons
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false); // Ensure the panel is transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 80, 10, 10); // Add padding around buttons

        // Create buttons for the menu
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

        // Style buttons
        styleButton(playButton);
        styleButton(contButton);
        styleButton(exitButton);

        // Add the grid panel to the base panel
        basePanel.add(gridPanel, BorderLayout.WEST);

        // Add action listener for "NEW GAME" button
        playButton.addActionListener(e -> {
            JDialog modePrompt = new JDialog(Checkers.window, "Select Game Mode", true);
            modePrompt.setLocationRelativeTo(Checkers.window);
            modePrompt.setSize(new Dimension(300, 200));
            modePrompt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            modePrompt.setLayout(new GridLayout(3, 1));

            JLabel modeString = new JLabel("Please Choose The Mode", SwingConstants.CENTER);
            JButton playerVsPlayer = new JButton("Player VS Player");
            JButton playerVsComputer = new JButton("Player VS Computer");

            styleButton(playerVsPlayer);
            styleButton(playerVsComputer);

            // Action listeners for mode selection
            playerVsPlayer.addActionListener(event -> {
                pvp();
                modePrompt.dispose();
            });

            playerVsComputer.addActionListener(event -> {
                pvc();
                modePrompt.dispose();
            });

            modePrompt.add(modeString);
            modePrompt.add(playerVsPlayer);
            modePrompt.add(playerVsComputer);

            modePrompt.setVisible(true);

            // Start a new game
            Game newGame = new Game();
            Checkers.game_state = newGame;
            newGame.setup(Checkers.window, this.gameMode);
        });

        // Add action listener for "CONTINUE" button
        contButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Read the saved game mode from a file
                    String gameMode;
                    try (BufferedReader reader = new BufferedReader(new FileReader("game_mode.txt"))) {
                        gameMode = reader.readLine().trim();
                        if (gameMode.equals("PvP")) {
                            pvp();
                        } else {
                            pvc();
                        }
                    }

                    // Load the saved game
                    Game loadedGame = new Game();
                    Checkers.game_state = loadedGame;
                    loadedGame.setupLoaded(Checkers.window);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        Checkers.window,
                        "Failed to load game mode. Please ensure the game_mode.txt file exists.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // Add action listener for "EXIT" button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Set up the main application window
        window.getContentPane().removeAll();
        window.add(basePanel);
        window.revalidate();
        window.repaint();
    }

    /**
     * Sets the game mode to Player vs Computer.
     */
    private void pvc() {
        Checkers.mode = GameModeEnum.PvC;
    }

    /**
     * Sets the game mode to Player vs Player.
     */
    private void pvp() {
        Checkers.mode = GameModeEnum.PvP;
    }

    /**
     * Styles a button with custom properties like size, background, and hover effects.
     *
     * @param button The button to style.
     */
    public static void styleButton(JButton button) {
        Dimension size = new Dimension(400, 60);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
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
        // Method not implemented for Menu state
    }

    @Override
    public void setup(JFrame window, GameModeEnum gameMode) {
        throw new UnsupportedOperationException("Unimplemented method 'setup'");
    }
}
