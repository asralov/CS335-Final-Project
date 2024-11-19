package model;
// Amyra and Abror's task
// Suggestion: Make it create once and make it reusable all the time
// not a FLYWEIGHT class

public class GameBoard {
    private Cell[][] board;

    public GameBoard()
    {
        init_board(); // getting a board
        init_white();  // initialize the white player pieces
        init_black();  // initialize the black player pieces
    }

    private void init_board()
    {
        this.board = new Cell[8][8];
        int row = 8, col = 8;
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                Color cell_color = ((i + j) % 2 == 0) ? Color.WHITE : Color.BLACK;
                this.board[i][j] = new Cell(cell_color, i, j);
            }
        }
    }


    public void init_white() {
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (i % 2 == 0 && j % 2 == 1) {
    				board[i][j].move_piece(new Piece(Color.WHITE, i, j));
    			} else if (i % 2 == 1 && j % 2 == 0){
    				board[i][j].move_piece(new Piece(Color.WHITE, i, j));
    			}
    		}
    	}
    }
    
    public void init_black() {
    	for (int i = 5; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (i % 2 == 0 && j % 2 == 1) {
    				board[i][j].move_piece(new Piece(Color.BLACK, i, j));
    			} else if (i % 2 == 1 && j % 2 == 0){
    				board[i][j].move_piece(new Piece(Color.BLACK, i, j));
    			}
    		}
    	}
    }
    
    public int getWhitePieces() {
    	return 0; 
    }
    
    public int getBlackPieces() {
    	return 0; 
    }


    // if the move is valid
    // BY assuming that the move is valid
    // we need to work on the case when it can eat
    public void move(Cell cell, int x, int y)
    {  
        Piece piece = cell.get_piece();
        // putting cordinates

        //  cell  cell  cell
        //  cell  cell  cell
        //  cell  cell  cell
        //  cell  cell  cell
        cell.empty_cell();
        this.board[x][y].move_piece(piece);
    }

    public Cell get_cell(int row, int col)
    {
        if (row < 0 || row > 7 || col < 0 || row > 7)
        {
            System.out.println("Out of index");
            return null;
        }

        return this.board[row][col];
    }


    // for debugging and visualizing purposes
    public String toString()
    {
        String str_board = "";
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {   
                str_board += this.board[i][j].toString() + " ";
            }
            str_board += "\n";
        }
        return str_board;
    }
    
}