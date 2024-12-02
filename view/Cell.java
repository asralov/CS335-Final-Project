package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import model.Piece;



public class Cell extends JButton{
    public Cell(Color color, Piece piece)
    {
        Dimension size = new Dimension(75, 75);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        // Configure the button appearance
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setBackground(color);

        String image_path = "";
        if (piece != null)
        {
            if (piece.getColor().name().equals("WHITE"))
            {
                image_path = (!piece.isKing()) ? "./assets/white_man.png" : "./assets/white_king.png";
            }
            else
            {
                image_path = (!piece.isKing()) ? "./assets/black_man.png" : "./assets/black_king.png";
            }
            ImageIcon icon = new ImageIcon(image_path);
            Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            ImageIcon new_icon = new ImageIcon(img);
            this.setIcon(new_icon);
        }
    }
}
