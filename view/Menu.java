package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu implements State {
    private String mode;
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

        // title
        // JLabel title = new JLabel();
        // ImageIcon ic = new ImageIcon("./assets/logo.png");
        // Image img = ic.getImage();
        // int w = 300;
        // int h = 180; 
        // Image scaledImage = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        // ImageIcon scaledIc = new ImageIcon(scaledImage);
        // title.setIcon(scaledIc);
        // gbc.gridx = 0;
        // gbc.gridy = 0;
        // gridPanel.add(title, gbc);
        
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
        
        // creating a top panel for the mode button
        // JPanel topPanel = new JPanel(new BorderLayout());
        // topPanel.setOpaque(false);
        // JButton modeButton = new JButton();
        // styleButton(modeButton);
        
        // Dimension mode_button_size = new Dimension(100, 75);
        // modeButton.setPreferredSize(mode_button_size);
        // modeButton.setMaximumSize(mode_button_size);
        // modeButton.setMinimumSize(mode_button_size);
        // topPanel.add(modeButton, BorderLayout.EAST); 
        
        // basePanel.add(topPanel, BorderLayout.NORTH);

        // scaling the pictures
        // int w_icon = 70;
        // int h_icon = 50;
        // ImageIcon pp_ic = new ImageIcon("./assets/p_vs_p.png");
        // Image pp_img = pp_ic.getImage().getScaledInstance(w_icon, h_icon, Image.SCALE_SMOOTH);
        // ImageIcon p_vs_p = new ImageIcon(pp_img);

        // ImageIcon pc_ic = new ImageIcon("./assets/p_vs_c.png");
        // Image pc_img = pc_ic.getImage().getScaledInstance(w_icon, h_icon, Image.SCALE_SMOOTH);
        // ImageIcon p_vs_c = new ImageIcon(pc_img);
        
        // // setting the initial icon for the mode button
        // modeButton.setIcon(p_vs_p);     
        // mode = "PvP";
        
          
        // // mode button action listener to toggle images
        // modeButton.addActionListener(e -> {
        //     // checking current icon and toggle
        //     if (modeButton.getIcon() == p_vs_p) {
        //         modeButton.setIcon(p_vs_c);
        //         mode = "PvC";
        //     } else {
        //         modeButton.setIcon(p_vs_p);
        //         mode = "PvP";
        //     }
        // });
        // // setting the initial icon for the mode button
        // modeButton.setIcon(p_vs_p);     
       
 
          
        // // mode button action listener to toggle images
        // modeButton.addActionListener(e -> {
        //     // checking current icon and toggle
        //     if (modeButton.getIcon() == p_vs_p) {
        //         modeButton.setIcon(p_vs_c);
        //     } else {
        //         modeButton.setIcon(p_vs_p);
        //     }
        // });
        
        playButton.addActionListener(e -> switch_to_game());
        contButton.addActionListener(e -> System.out.println("Continue button clicked!"));
        exitButton.addActionListener(e -> System.exit(0));
        
        // setting up the window
        window.getContentPane().removeAll();
        window.add(basePanel);
        window.revalidate();
        window.repaint();
    }

    private void switch_to_game()
    {
        Checkers.game_state = new Game();
        Checkers.game_state.setup(Checkers.window);
        Checkers.mode = mode;
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
		// TODO Auto-generated method stub
		
	}
}
