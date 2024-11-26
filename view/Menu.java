package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu implements State {
    @Override
    public void setup(JFrame window) {
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        // green rgb we are gonna use: 77, 135, 50
        panel.setBackground(new Color(77,135,50));
        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel();
        System.out.println(new File("../assets/logo.png").exists());

        ImageIcon ic = new ImageIcon("../assets/logo.png");
        Image img = ic.getImage();
        Image scaled_image = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaled_ic = new ImageIcon(scaled_image);
        title.setIcon(scaled_ic);
        
        // title.setFont(new Font("Arial", Font.BOLD, 48));
        // title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        // new game button
        JButton play_button = new JButton("NEW GAME");
        play_button.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        panel.add(play_button, gbc);

        // continue button
        JButton cont_button = new JButton("CONTINUE");
        cont_button.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 2;
        panel.add(cont_button, gbc);

        // exit button
        JButton exit_button = new JButton("EXIT");
        exit_button.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 3;
        panel.add(exit_button, gbc);

        // event listeners
        play_button.addActionListener(e -> System.out.println("new game button clicked!"));
        cont_button.addActionListener(e -> System.out.println("continue button clicked!"));
        exit_button.addActionListener(e -> System.exit(0));

        // clearning the existing components
        window.getContentPane().removeAll();
        window.add(panel);
        window.revalidate();
        window.repaint();
    }
}
