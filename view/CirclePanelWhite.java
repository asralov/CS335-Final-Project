package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Represents a white circular panel used as an icon for captured pieces
 * in the checkers game.
 * 
 * This panel customizes its rendering to display a filled white circle 
 * with a black border.
 */
public class CirclePanelWhite extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        // Fill the panel with a white circle
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        // Draw a black border around the circle
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}
