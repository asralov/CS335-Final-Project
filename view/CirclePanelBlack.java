package view;

import javax.swing.*;
import java.awt.*;


/**
 * Represents a black circular panel used as an icon for captured pieces
 * in the checkers game.
 * 
 * This panel customizes its rendering to display a filled black circle 
 * with a white border.
 */
public class CirclePanelBlack extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        // Fill the panel with a black circle
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        // Draw a white border around the circle
        g.setColor(Color.WHITE);
        g.drawOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}