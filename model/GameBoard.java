package model;

import java.util.ArrayList;

/**
 * Represents the game board for a Checkers game. 
 * Manages the pieces, their positions, and game logic such as moves, captures, and promotions.
 */
public class GameBoard {
    private Piece[][] board; // The 8x8 game board
    private ArrayList<Piece> blackPieces = new ArrayList<>(); // List of all black pieces
    private ArrayList<Piece> whitePieces = new ArrayList<>(); // List of all white pieces

    /**
     * Constructs a new game board and initializes the pieces.
     */
    public GameBoard() {
        init_board(); // Initialize the board and pieces
    }

    /**
     * Initializes the board and sets up the pieces for both players.
     */
    private void init_board() {
        this.board = new Piece[8][8]; // Create an empty board
        init_white(); // Place white pieces
        init_black(); // Place black pieces
    }

    /**
     * Initializes white pieces and places them on the board.
     */
    public void init_white() {
        for (int i = 0; i < 3; i++) { // Loop through the first three rows
            for (int j = 0; j < 8; j++) { // Loop through all columns
                if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                    // Place a white piece based on the row and column indices
                    board[i][j] = new Piece(Color.WHITE, i, j);
                    whitePieces.add(board[i][j]); // Add the piece to the whitePieces list
                }
            }
        }
    }

    /**
     * Initializes black pieces and places them on the board.
     */
    public void init_black() {
        for (int i = 5; i < 8; i++) { // Loop through the last three rows
            for (int j = 0; j < 8; j++) { // Loop through all columns
                if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                    // Place a black piece based on the row and column indices
                    board[i][j] = new Piece(Color.BLACK, i, j);
                    blackPieces.add(board[i][j]); // Add the piece to the blackPieces list
                }
            }
        }
    }

    /**
     * Returns the number of white pieces currently on the board.
     *
     * @return The count of white pieces.
     */
    public int getWhitePieces() {
        int whitePiece = 0; // Initialize counter
        for (int i = 0; i < 8; i++) { // Loop through rows
            for (int j = 0; j < 8; j++) { // Loop through columns
                if (board[i][j] == null) { // Skip empty cells
                    continue;
                }
                if (board[i][j].getColor().equals(Color.WHITE)) {
                    // Increment counter if the piece is white
                    whitePiece++;
                }
            }
        }
        return whitePiece; // Return total count
    }

    /**
     * Returns the number of black pieces currently on the board.
     *
     * @return The count of black pieces.
     */
    public int getBlackPieces() {
        int blackPiece = 0; // Initialize counter
        for (int i = 0; i < 8; i++) { // Loop through rows
            for (int j = 0; j < 8; j++) { // Loop through columns
                if (board[i][j] == null) { // Skip empty cells
                    continue;
                }
                if (board[i][j].getColor().equals(Color.BLACK)) {
                    // Increment counter if the piece is black
                    blackPiece++;
                }
            }
        }
        return blackPiece; // Return total count
    }

    /**
     * Returns the list of all white pieces.
     *
     * @return The list of white pieces.
     */
    public ArrayList<Piece> getWhitePiecesList() {
        return whitePieces;
    }

    /**
     * Returns the list of all black pieces.
     *
     * @return The list of black pieces.
     */
    public ArrayList<Piece> getBlackPiecesList() {
        return blackPieces;
    }

    /**
     * Moves a piece to a new position on the board.
     *
     * @param piece The piece to move.
     * @param newX  The new row position.
     * @param newY  The new column position.
     * @throws IllegalArgumentException if the move is invalid.
     */
    public void move(Piece piece, int newX, int newY) {
        if (piece == null || !isValidPosition(newX, newY)) {
            throw new IllegalArgumentException("Invalid move."); // Validate the move
        }

        // Clear the old position
        board[piece.getRow()][piece.getColumn()] = null;

        // Update the piece's position
        piece.setPosition(newX, newY);

        // Place the piece in the new position
        board[newX][newY] = piece;
    }

    /**
     * Removes a piece from the board at the specified position.
     *
     * @param row The row of the piece to remove.
     * @param col The column of the piece to remove.
     */
    public void removePiece(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = null; // Clear the cell if the position is valid
        }
    }

    /**
     * Validates if the given position is within the board boundaries.
     *
     * @param x The row index.
     * @param y The column index.
     * @return {@code true} if the position is valid, otherwise {@code false}.
     */
    private boolean isValidPosition(int x, int y) {
        if (x >= 0 && x < 8) { // Check row boundaries
            if (y >= 0 && y < 8) { // Check column boundaries
                return true;
            } else {
                return false; // Column out of bounds
            }
        } else {
            return false; // Row out of bounds
        }
    }

    /**
     * Handles a multi-step move along a given path, removing captured pieces and promoting kings.
     *
     * @param path      The path of the move.
     * @param pieceColor The color of the piece making the move.
     * @param isKing     Whether the moving piece is a king.
     * @return A list of pieces removed during the move.
     */
    public ArrayList<Piece> move(ArrayList<int[]> path, Color pieceColor, boolean isKing) {
        ArrayList<Piece> removedPieces = new ArrayList<>(); // Initialize removed pieces list

        // Remove captured pieces along the path
        for (int i = 0; i < path.size() - 1; i++) {
            int x = path.get(i)[0];
            int y = path.get(i)[1];

            if (board[x][y] != null) { // If a piece exists at the position
                Piece pieceToRemove = board[x][y];
                removedPieces.add(pieceToRemove); // Add to removed pieces list

                // Remove from the appropriate piece list
                if (pieceToRemove.getColor().equals(Color.BLACK)) {
                    blackPieces.remove(pieceToRemove);
                } else {
                    whitePieces.remove(pieceToRemove);
                }
                board[x][y] = null; // Clear the position
            }
        }

        // Add the new piece at the end of the path
        int newX = path.get(path.size() - 1)[0];
        int newY = path.get(path.size() - 1)[1];
        Piece newPiece = new Piece(pieceColor, newX, newY);
        removedPieces.add(newPiece); // Add the new piece to the list

        // Promote to king if applicable
        if (isKing || (pieceColor.equals(Color.BLACK) && newX == 0) || (pieceColor.equals(Color.WHITE) && newX == 7)) {
            newPiece.ToKing();
        }

        board[newX][newY] = newPiece; // Place the new piece on the board

        // Update the appropriate piece list
        if (pieceColor.equals(Color.BLACK)) {
            blackPieces.add(newPiece);
        } else {
            whitePieces.add(newPiece);
        }

        return removedPieces; // Return the list of removed pieces
    }
    /**
     * Retrieves the piece at the specified position on the board.
     *
     * @param x The row index.
     * @param y The column index.
     * @return The piece at the given position, or {@code null} if the position is empty.
     */
    public Piece getPiece(int x, int y) {
        return board[x][y]; // Directly return the piece from the board
    }

    /**
     * Creates and returns a deep copy of the current board.
     * This ensures that modifications to the copy do not affect the original board.
     *
     * @return A deep copy of the board.
     */
    public Piece[][] getBoardCopy() {
        Piece[][] copy = new Piece[8][8]; // Initialize a new 8x8 board
        for (int i = 0; i < 8; i++) { // Loop through each row
            for (int j = 0; j < 8; j++) { // Loop through each column
                if (board[i][j] != null) {
                    // Create a new Piece object to copy the current piece
                    copy[i][j] = new Piece(board[i][j]);
                }
            }
        }
        return copy; // Return the deep copy of the board
    }

    /**
     * Sets the board to a new configuration and rebuilds the piece lists.
     * Clears the current white and black piece lists and updates them based on the new board.
     *
     * @param newBoard The new board configuration to set.
     */
    public void setBoard(Piece[][] newBoard) {
        this.board = newBoard; // Update the board reference

        // Clear the current piece lists
        whitePieces.clear();
        blackPieces.clear();

        // Rebuild the piece lists based on the new board configuration
        for (int i = 0; i < board.length; i++) { // Loop through each row
            for (int j = 0; j < board[i].length; j++) { // Loop through each column
                if (board[i][j] != null) { // If there is a piece at the position
                    Piece piece = board[i][j];
                    if (piece.getColor() == Color.WHITE) {
                        // Add to white pieces list if the piece is white
                        whitePieces.add(piece);
                    } else {
                        // Add to black pieces list if the piece is black
                        blackPieces.add(piece);
                    }
                }
            }
        }
    }

    /**
     * Returns a string representation of the board for display purposes.
     * Each piece is represented by a character: 
     * 'W' for white kings, 'w' for white normal pieces, 
     * 'B' for black kings, 'b' for black normal pieces, 
     * and '.' for empty cells.
     *
     * @return A string representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("   0 1 2 3 4 5 6 7\n   ----------------\n");

        for (int i = 0; i < board.length; i++) { // Loop through each row
            output.append(i).append("| "); // Add row index with separator
            for (int j = 0; j < board[i].length; j++) { // Loop through each column
                if (board[i][j] == null) {
                    // Represent empty cells with '.'
                    output.append(". ");
                } else {
                    // Check the color and type of the piece
                    if (board[i][j].getColor() == Color.WHITE) {
                        // Use 'W' for white kings and 'w' for normal white pieces
                        if (board[i][j].isKing()) {
                            output.append("W ");
                        } else {
                            output.append("w ");
                        }
                    } else if (board[i][j].getColor() == Color.BLACK) {
                        // Use 'B' for black kings and 'b' for normal black pieces
                        if (board[i][j].isKing()) {
                            output.append("B ");
                        } else {
                            output.append("b ");
                        }
                    }
                }
            }
            output.append("\n"); // Move to the next line for the next row
        }
        output.append("\n"); // Add extra spacing after the board for readability

        return output.toString(); // Return the board as a formatted string
    }

    
}