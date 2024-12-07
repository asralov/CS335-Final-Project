package model;

/**
 * Represents a piece on the Checkers game board.
 * A piece has a color, position (row and column), and may optionally be a king.
 */
public class Piece {

    private Color color;    // The color of the piece (e.g., Black or White)
    private boolean isKing; // Indicates whether the piece has been promoted to a king
    private int x;          // The row position of the piece
    private int y;          // The column position of the piece

    /**
     * Constructs a new piece with the specified color and position.
     * The piece is not a king by default.
     *
     * @param color The color of the piece.
     * @param x     The row position of the piece on the board.
     * @param y     The column position of the piece on the board.
     */
    public Piece(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.isKing = false;
    }

    /**
     * Copy constructor to create a new piece based on another piece.
     *
     * @param p The piece to copy.
     */
    public Piece(Piece p) {
        this.color = p.color;
        this.x = p.x;
        this.y = p.y;
        this.isKing = p.isKing;
    }

    /**
     * Returns the color of the piece.
     *
     * @return The color of the piece.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks if the piece is a king.
     *
     * @return {@code true} if the piece is a king, {@code false} otherwise.
     */
    public boolean isKing() {
        return isKing;
    }

    /**
     * Returns the row position of the piece.
     *
     * @return The row position of the piece.
     */
    public int getRow() {
        return x;
    }

    /**
     * Returns the column position of the piece.
     *
     * @return The column position of the piece.
     */
    public int getColumn() {
        return y;
    }

    /**
     * Promotes the piece to a king.
     */
    public void ToKing() {
        this.isKing = true;
    }

    /**
     * Sets the position of the piece on the board.
     *
     * @param x The new row position of the piece.
     * @param y The new column position of the piece.
     */
    public void setPosition(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the piece.
     * The string includes whether the piece is a king, its color, and its position.
     *
     * @return A string representation of the piece.
     */
    @Override
    public String toString() {
        String pieceType;
        if (isKing) {
            pieceType = "King";
        } else {
            pieceType = "Piece";
        }
        return pieceType + " [" + color + "] at (" + x + ", " + y + ")";
    }
}
