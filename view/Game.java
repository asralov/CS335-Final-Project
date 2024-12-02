package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game implements State{
    @Override
    public void setup(JFrame window)
    {
        JPanel main_game_panel = new JPanel(new BorderLayout());
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.setBackground(new Color(77, 135, 50));
        // for TESTING PURPOSES TO CHECK IF WE ARE ABLE TO COME BACK
        JButton menu_button = new JButton("MENU");
        menu_button.setFont(new Font("Arial", Font.BOLD, 20));
        wrapperPanel.add(menu_button);

        menu_button.addActionListener(e -> switch_to_menu());
        wrapperPanel.setPreferredSize(new Dimension(800, 800)); 

        JPanel game_panel = new JPanel(new GridLayout(8, 8));
        game_panel.setPreferredSize(new Dimension(700, 700));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color color = (row + col) % 2 == 0 ? new Color(246, 187, 146) : new Color(152, 86, 40);
                game_panel.add(new Cell(color));
            }
        }
        wrapperPanel.add(game_panel);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(45, 0, 0, 0));
        main_game_panel.add(wrapperPanel, BorderLayout.CENTER);
        // setting up the window
        window.getContentPane().removeAll();
        window.add(main_game_panel);
        window.revalidate();
        window.repaint();
    }


    private void switch_to_menu()
    {
        Checkers.game_state = new Menu();
        Checkers.game_state.setup(Checkers.window);
    }
}
