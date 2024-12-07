package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import model.Piece;

/**
 * Represents a cell on the game board in the Checkers game.
 * Each cell has a position (x, y), an optional piece, and visual properties such as color and size.
 */
public class Cell extends JButton {
    private int x; // The x-coordinate of the cell
    private int y; // The y-coordinate of the cell
    private Piece piece; // The piece currently on this cell (null if empty)

    /**
     * Constructs a new Cell with the specified color, piece, and coordinates.
     *
     * @param color The background color of the cell.
     * @param piece The piece to place on this cell (null if empty).
     * @param x     The x-coordinate of the cell.
     * @param y     The y-coordinate of the cell.
     */
    public Cell(Color color, Piece piece, int x, int y) {
        this.x = x;
        this.y = y;

        // Set the size of the cell
        Dimension size = new Dimension(75, 75);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        // Configure the button appearance
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setBackground(color);

        // Set the icon if a piece exists
        if (piece != null) {
            String imagePath;
            if (piece.getColor().name().equals("WHITE")) {
                if (!piece.isKing()) {
                    imagePath = "./assets/white_man.png";
                } else {
                    imagePath = "./assets/white_king.png";
                }
            } else {
                if (!piece.isKing()) {
                    imagePath = "./assets/black_man.png";
                } else {
                    imagePath = "./assets/black_king.png";
                }
            }
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(img);
            this.setIcon(newIcon);
        }

        // Add an action listener to handle clicks on the cell
        this.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleCellClick();
            }
        });
    }

    /**
     * Returns the x-coordinate of this cell.
     *
     * @return The x-coordinate.
     */
    public int getXCoord() {
        return x;
    }

    /**
     * Returns the y-coordinate of this cell.
     *
     * @return The y-coordinate.
     */
    public int getYCoord() {
        return y;
    }

    /**
     * Handles a click on the cell by invoking the game state's handler.
     */
    private void handleCellClick() {
        Checkers.game_state.handleCellClick(x, y);
    }

    /**
     * Updates the cell with a new piece and adjusts its icon.
     *
     * @param newPiece The new piece to place on the cell (null if the cell is to be cleared).
     */
    public void updateCell(Piece newPiece) {
        this.piece = newPiece;

        // Update the icon based on the new piece
        if (piece != null) {
            String imagePath;
            if (piece.getColor().name().equals("WHITE")) {
                if (!piece.isKing()) {
                    imagePath = "./assets/white_man.png";
                } else {
                    imagePath = "./assets/white_king.png";
                }
            } else {
                if (!piece.isKing()) {
                    imagePath = "./assets/black_man.png";
                } else {
                    imagePath = "./assets/black_king.png";
                }
            }

            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            this.setIcon(new ImageIcon(img));
        } else {
            this.setIcon(null); // Clear the icon if no piece exists
        }

        this.repaint();
    }

    /**
     * Highlights or un-highlights the cell using a default highlight color.
     *
     * @param highlight If true, highlights the cell; otherwise, resets the background color.
     */
    public void highlightCell(boolean highlight) {
        System.out.println("HIGHLIGHTING CELL: " + this.x + " " + this.y);

        if (highlight) {
            this.setBackground(new Color(168, 66, 167)); // Highlight color
        } else {
            if ((x + y) % 2 == 0) {
                this.setBackground(new Color(246, 187, 146)); // Light color
            } else {
                this.setBackground(new Color(152, 86, 40)); // Dark color
            }
        }
    }

    /**
     * Highlights or un-highlights the cell using a specified highlight color.
     *
     * @param highlight If true, highlights the cell; otherwise, resets the background color.
     * @param color     The color to use for highlighting.
     */
    public void highlightCell(boolean highlight, Color color) {
        System.out.println("HIGHLIGHTING CELL: " + this.x + " " + this.y);

        if (highlight) {
            this.setBackground(color); // Custom highlight color
        } else {
            if ((x + y) % 2 == 0) {
                this.setBackground(new Color(246, 187, 146)); // Light color
            } else {
                this.setBackground(new Color(152, 86, 40)); // Dark color
            }
        }
    }
}
