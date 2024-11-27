package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu implements State {
    @Override
    public void setup(JFrame window) {
        // Create the base panel with BorderLayout
        JPanel basePanel = new JPanel(new BorderLayout());
        basePanel.setBackground(new Color(77, 135, 50));
        
        // Create a panel for the title and buttons
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false); // Transparent to show basePanel background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel title = new JLabel();
        ImageIcon ic = new ImageIcon("./assets/logo.png");
        Image img = ic.getImage();
        int w = 400;
        int h = w * (16/9); 
        Image scaledImage = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon scaledIc = new ImageIcon(scaledImage);
        title.setIcon(scaledIc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gridPanel.add(title, gbc);
        
        // Buttons
        JButton playButton = new JButton("NEW GAME");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gridPanel.add(playButton, gbc);
        
        JButton contButton = new JButton("CONTINUE");
        contButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 2;
        gridPanel.add(contButton, gbc);
        
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 3;
        gridPanel.add(exitButton, gbc);
        
        // Styling buttons
        styleButton(playButton);
        styleButton(contButton);
        styleButton(exitButton);
        
        // Add the grid panel to the center of the base panel
        basePanel.add(gridPanel, BorderLayout.CENTER);
        
        // Create a top panel for the mode button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Transparent to show basePanel background
        JButton modeButton = new JButton("PvsP");
        styleButton(modeButton);
        modeButton.setPreferredSize(new Dimension(100, 40)); // Adjust size of the button
        topPanel.add(modeButton, BorderLayout.EAST); // Align the button to the right
        
        // Add the top panel to the base panel
        basePanel.add(topPanel, BorderLayout.NORTH);
        
        // Mode button action listener
        modeButton.addActionListener(e -> {
            String currentText = modeButton.getText();
            modeButton.setText(currentText.equals("PvsP") ? "PvsC" : "PvsP");
        });
        
        // Event listeners
        playButton.addActionListener(e -> System.out.println("New game button clicked!"));
        contButton.addActionListener(e -> System.out.println("Continue button clicked!"));
        exitButton.addActionListener(e -> System.exit(0));
        
        // Set up the window
        window.getContentPane().removeAll();
        window.add(basePanel);
        window.revalidate();
        window.repaint();
    }

    public static void styleButton(JButton button) {
        Dimension size = new Dimension(400, 50);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        Color defaultColor = new Color(0, 0, 0);
        button.setOpaque(true);
        button.setBackground(defaultColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(55, 55, 55));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
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
}
