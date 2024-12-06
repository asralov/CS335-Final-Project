package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import model.Piece;



public class Cell extends JButton{
	private int x; 
	private int y; 
	private Piece piece; 
	
    public Cell(Color color, Piece piece, int x, int y)
    {
    	this.x = x; 
    	this.y = y; 
    	
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
        
        this.addActionListener(e -> handleCellClick());
        
    }
    
    public int getXCoord() { 
    	return x; 
    }
    
    public int getYCoord() { 
    	return y;
    }

    private void handleCellClick() {
        Checkers.game_state.handleCellClick(x, y);
    }

    public void updateCell(Piece newPiece) {
        this.piece = newPiece;

        // Update icon
        if (piece != null) {
            String imagePath = piece.getColor().name().equals("WHITE")
                ? (piece.isKing() ? "./assets/white_king.png" : "./assets/white_man.png")
                : (piece.isKing() ? "./assets/black_king.png" : "./assets/black_man.png");

            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            this.setIcon(new ImageIcon(img));
        } else {
            this.setIcon(null);
        }

        this.repaint();
    }


    public void highlightCell(boolean highlight) {
        System.out.println("HIGHLIGHTING CELL: " + this.x + " " + this.y);
        if (highlight) {
            this.setBackground(new Color(168, 66, 167)); 
        } else {
            this.setBackground((x + y) % 2 == 0 ? new Color(246, 187, 146) : new Color(152, 86, 40));
        }
    }

    public void highlightCell(boolean highlight, Color color) {
        System.out.println("HIGHLIGHTING CELL: " + this.x + " " + this.y);
        if (highlight) {
            this.setBackground(color); 
        } else {
            this.setBackground((x + y) % 2 == 0 ? new Color(246, 187, 146) : new Color(152, 86, 40));
        }
    }

}
