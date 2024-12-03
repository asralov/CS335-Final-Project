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
    public void move(Piece piece, int newX, int newY) {
        if (piece == null || !isValidPosition(newX, newY)) {
            throw new IllegalArgumentException("Invalid move.");
        }

 
        board[piece.getRow()][piece.getColumn()] = null;

        piece.setPosition(newX, newY);

        board[newX][newY] = piece;
    }

    public void removePiece(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = null;
        }
    }


    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // public Cell get_cell(int row, int col)
    // {
    //     if (row < 0 || row > 7 || col < 0 || row > 7)
    //     {
    //         System.out.println("Out of index");
    //         return null;
    //     }

    //     return this.board[row][col];
    // }
    
    public Piece getPiece(int x, int y) {
    	return board[x][y];
    }
    
    public Piece[][] getBoard() {
    	return board;
    }
    
    public Piece[][] getBoardCopy() {
    	Piece[][] copy = new Piece[8][8]; 
    	for (int i = 0; i < 8; i ++) {
    		for (int j = 0; j < 8; j++) {
    			if (board[i][j] != null) {
    				copy[i][j] = new Piece(board[i][j]);
    			}
    			// copy[i][j] = new Cell(board[i][j]);
                
    		}
    	}
    	
    	return copy; 
    }
    
//    // for debugging and visualizing purposes
//    public String toString()
//    {
//        String str_board = "";
//        for (int i = 0; i < 8; i++)
//        {
//            for (int j = 0; j < 8; j++)
//            {   
//                str_board += this.board[i][j].toString() + " ";
//            }
//            str_board += "\n";
//        }
//        return str_board;
//    }
    
    
    public String toString() {
    	String output = "   0 1 2 3 4 5 6 7\n   ----------------\n";

	    for (int i = 0; i < board.length; i++) {
	        output += i + "| "; 
	        for (int j = 0; j < board[i].length; j++) {
	            if (board[i][j] == null) {
	            	output += ". "; 
	            } else if (board[i][j].getColor() == Color.WHITE) {
	            	output += board[i][j].isKing() ? "W " : "w "; 
	            } else if (board[i][j].getColor() == Color.BLACK) {
	            	output += board[i][j].isKing() ? "B " : "b ";
	            }
	        }
	        output += "\n";
	    }
	    output += "\n";
	    
	    return output;
    	
    }
    
}