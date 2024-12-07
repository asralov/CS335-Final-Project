package model;

import java.util.ArrayList;

/**
 * Represents the logic for generating possible moves for a Checkers piece on the board.
 * Includes methods for normal moves, captures, and handling king-specific rules.
 */
public class Move {

    static boolean isKing1 = true; // Temporary king status for recursive logic

    /**
     * Calculates all possible moves for a given piece on the board.
     * This includes both normal moves and capture moves.
     *
     * @param piece The piece for which to calculate moves.
     * @param board The current state of the game board.
     * @return A list of move paths (normal or capture moves).
     */
    public static ArrayList<ArrayList<int[]>> getPossibleMoves(Piece piece, Piece[][] board) {
        ArrayList<ArrayList<int[]>> list1 = new ArrayList<>(); // Normal moves
        ArrayList<ArrayList<int[]>> list_capture = new ArrayList<>(); // Capture moves

        int x = piece.getRow(); // Row position of the piece
        int y = piece.getColumn(); // Column position of the piece
        boolean isKing = piece.isKing(); // Check if the piece is a king
        isKing1 = isKing; // Store king status for recursive functions
        Color color = piece.getColor(); // Get the color of the piece

        // Get valid move directions for the piece
        int[][] directions = getMoveDirections(isKing, color);

        // Check all possible directions for moves
        for (int[] direction : directions) {
            getPositionsList(x, y, direction, board, color, list1, list_capture);
        }

        // If capture moves are available, prioritize them
        if (list_capture.isEmpty()) {
            return list1; // Return normal moves if no captures
        } else {
            return list_capture; // Return capture moves if available
        }
    }

    /**
     * Returns valid move directions based on the piece's type (king or normal) and color.
     *
     * @param isKing Whether the piece is a king.
     * @param color  The color of the piece.
     * @return A 2D array of valid move directions.
     */
    private static int[][] getMoveDirections(boolean isKing, Color color) {
        if (isKing) {
            // Kings can move in all four diagonal directions
            return new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        } else {
            // Non-king pieces move forward based on their color
            if (color == Color.WHITE) {
                return new int[][]{{1, 1}, {1, -1}}; // White pieces move down
            } else {
                return new int[][]{{-1, 1}, {-1, -1}}; // Black pieces move up
            }
        }
    }

    /**
     * Checks possible moves or captures in a specific direction.
     *
     * @param x             The starting row of the piece.
     * @param y             The starting column of the piece.
     * @param direction     The direction to check for moves.
     * @param board         The current state of the game board.
     * @param color         The color of the piece.
     * @param list1         The list to store normal moves.
     * @param list_capture  The list to store capture moves.
     */
    private static void getPositionsList(int x, int y, int[] direction, Piece[][] board, Color color,
                                         ArrayList<ArrayList<int[]>> list1, ArrayList<ArrayList<int[]>> list_capture) {
        int x1 = x + direction[0]; // Next row in the direction
        int y1 = y + direction[1]; // Next column in the direction

        if (validIndex(x1, y1)) { // Check if the position is within bounds
            if (board[x1][y1] == null) {
                // Add a normal move if the position is empty
                addNormalMove(x, y, x1, y1, list1);
            } else if (board[x1][y1].getColor() != color) {
                // Check for capture opportunities if the piece is of the opposite color
                ArrayList<int[]> path = new ArrayList<>();
                path.add(new int[]{x, y});
                catchPiece(x, y, board, color, direction, path, list_capture);
            }
        }
    }

    /**
     * Adds a normal move to the list of moves.
     *
     * @param x      The starting row.
     * @param y      The starting column.
     * @param x1     The ending row.
     * @param y1     The ending column.
     * @param list1  The list to store the normal move.
     */
    private static void addNormalMove(int x, int y, int x1, int y1, ArrayList<ArrayList<int[]>> list1) {
        ArrayList<int[]> move = new ArrayList<>();
        move.add(new int[]{x, y}); // Starting position
        move.add(new int[]{x1, y1}); // Ending position
        list1.add(move);
    }

    /**
     * Handles capturing an opponent's piece and extends the capture path recursively.
     *
     * @param x             The starting row.
     * @param y             The starting column.
     * @param board         The current state of the game board.
     * @param color         The color of the piece.
     * @param direction     The direction to capture.
     * @param currentPath   The current path of the capture.
     * @param list_capture  The list to store the capture moves.
     */
    private static void catchPiece(int x, int y, Piece[][] board, Color color, int[] direction,
                                   ArrayList<int[]> currentPath, ArrayList<ArrayList<int[]>> list_capture) {
        int x1 = x + direction[0]; // Row of the piece to be captured
        int y1 = y + direction[1]; // Column of the piece to be captured
        int x2 = x1 + direction[0]; // Row after the capture
        int y2 = y1 + direction[1]; // Column after the capture

        if (validIndex(x1, y1) && board[x1][y1] != null &&
                board[x1][y1].getColor() != color &&
                validIndex(x2, y2) && board[x2][y2] == null) {

            ArrayList<int[]> newPath = new ArrayList<>(currentPath);
            newPath.add(new int[]{x1, y1}); // Add captured piece position
            newPath.add(new int[]{x2, y2}); // Add landing position

            // Temporarily remove the captured piece for recursive capture checks
            Piece temp = board[x1][y1];
            board[x1][y1] = null;

            // Check for additional captures in all directions
            for (int[] nextDirection : getMoveDirections(isKing1, color)) {
                catchPiece(x2, y2, board, color, nextDirection, newPath, list_capture);
            }

            // Restore the captured piece
            board[x1][y1] = temp;

            // Add the completed capture path to the list
            if (newPath.size() > currentPath.size()) {
                list_capture.add(newPath);
            }
        }
    }

    /**
     * Validates if a given position is within the board boundaries.
     *
     * @param x The row position.
     * @param y The column position.
     * @return {@code true} if the position is valid, {@code false} otherwise.
     */
    private static boolean validIndex(int x, int y) {
        if (x >= 0 && x < 8) { // Check row boundaries
            if (y >= 0 && y < 8) { // Check column boundaries
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Prints the current state of the board to the console for debugging.
     *
     * @param board The game board to print.
     */
    private static void printBoard(Piece[][] board) {
        // Print column indices
        System.out.println("   0 1 2 3 4 5 6 7"); 
        System.out.println("   ----------------"); // Separator line

        // Iterate through each row of the board
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + "| "); // Print row index with a separator
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    System.out.print(". "); // Print a dot for an empty cell
                } else if (board[i][j].getColor() == Color.WHITE) {
                    // Print 'W' for a white king, 'w' for a white normal piece
                    System.out.print(board[i][j].isKing() ? "W " : "w ");
                } else if (board[i][j].getColor() == Color.BLACK) {
                    // Print 'B' for a black king, 'b' for a black normal piece
                    System.out.print(board[i][j].isKing() ? "B " : "b ");
                }
            }
            System.out.println(); // Move to the next row
        }
        System.out.println(); // Add an extra line after the board for readability
    }

    /**
     * Main method for testing the Move logic and printing possible moves.
     * Initializes a sample board, sets up a piece, and displays its possible moves.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Initialize an empty board
        Piece[][] board = new Piece[8][8];

        // Place a white piece at position (3, 4)
        board[3][4] = new Piece(Color.WHITE, 3, 4);

        // Place a black piece at position (4, 1) and make it a king
        board[4][1] = new Piece(Color.BLACK, 4, 1);
        board[4][1].ToKing();

        // Print the initial state of the board
        printBoard(board);

        // Get possible moves for the black king at (4, 1)
        ArrayList<ArrayList<int[]>> moves = Move.getPossibleMoves(board[4][1], board);

        // Print all possible moves for the black king
        System.out.println("Possible moves for the piece:");
        for (ArrayList<int[]> move : moves) {
            System.out.print("Move path: ");
            for (int[] step : move) {
                // Print each step in the move path
                System.out.print("(" + step[0] + ", " + step[1] + ") ");
            }
            System.out.println(); // Move to the next line for the next path
        }
    }
}