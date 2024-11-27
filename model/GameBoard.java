package model;
// Amyra and Abror's task
// Suggestion: Make it create once and make it reusable all the time
// not a FLYWEIGHT class

public class GameBoard {
    private Piece[][] board;
    // cell and pieces array (Amyra)

    public GameBoard()
    {
        init_board(); // getting a board
        // init_white();  // initialize the white player pieces
        // init_black();  // initialize the black player pieces
    }
    
    
    private void init_board()
    {
        this.board = new Piece[8][8];
        // int row = 8, col = 8;
        // for (int i = 0; i < row; i++)
        // {
        //     for (int j = 0; j < col; j++)
        //     {
        //         Color cell_color = ((i + j) % 2 == 0) ? Color.WHITE : Color.BLACK;
        //         this.board[i][j] = new Cell(cell_color, i, j);
        //     }
        // }
        init_white();
        init_black();
    }

    
    public void init_white() {
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (i % 2 == 0 && j % 2 == 1) {
    				board[i][j] = new Piece(Color.WHITE, i, j);
    			} else if (i % 2 == 1 && j % 2 == 0){
    				board[i][j] = new Piece(Color.WHITE, i, j);
    			}
    		}
    	}
    }
    
    public void init_black() {
    	for (int i = 5; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (i % 2 == 0 && j % 2 == 1) {
    				board[i][j] = new Piece(Color.BLACK, i, j);
    			} else if (i % 2 == 1 && j % 2 == 0){
    				board[i][j] = new Piece(Color.BLACK, i, j);
    			}
    		}
    	}
    }
    
    public int getWhitePieces() {
    	int whitePiece = 0 ; 
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (board[i][j].getColor().equals(Color.WHITE)) {
    				whitePiece ++;
    			}
    		}
    	}
    	
    	return whitePiece; 
    }
    
    public int getBlackPieces() {
    	int blackPiece = 0 ; 
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (board[i][j].getColor().equals(Color.BLACK)) {
    				blackPiece ++;
    			}
    		}
    	}
    	
    	return blackPiece; 
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
        this.board[x][y] = piece;
    }

<<<<<<< HEAD
    public Cell get_cell(int row, int col)
    {
        if (row < 0 || row > 7 || col < 0 || col > 7)
        {
            System.out.println("Out of index");
            return null;
        }
=======
    // public Cell get_cell(int row, int col)
    // {
    //     if (row < 0 || row > 7 || col < 0 || row > 7)
    //     {
    //         System.out.println("Out of index");
    //         return null;
    //     }
>>>>>>> origin/main

    //     return this.board[row][col];
    // }
    
    
    public Piece[][] getBoardCopy() {
    	Piece[][] copy = new Piece[8][8]; 
    	for (int i = 0; i < 8; i ++) {
    		for (int j = 0; j < 8; j++) {
    			// copy[i][j] = new Cell(board[i][j]);
                copy[i][j] = new Piece(board[i][j]);
    		}
    	}
    	
    	return copy; 
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