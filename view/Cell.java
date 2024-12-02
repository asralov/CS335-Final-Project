package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class Cell extends JButton{
    public Cell(Color color)
    {
        // Set the preferred size of the button
        Dimension size = new Dimension(75, 75);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        // Configure the button appearance
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setBackground(color);
    }
}
