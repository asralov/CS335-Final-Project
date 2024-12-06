package model;

import java.util.ArrayList;  

public class GameBoard {
    private Piece[][] board;
    private ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    private ArrayList<Piece> whitePieces = new ArrayList<Piece>();

    public GameBoard()
    {
        init_board(); // getting a board
    }
    
    
    private void init_board()
    {
        this.board = new Piece[8][8];

        init_white();
        init_black();
    }

    
    public void init_white() {
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (i % 2 == 0 && j % 2 == 1) {
    				board[i][j] = new Piece(Color.WHITE, i, j);
    				whitePieces.add(board[i][j]);
    			} else if (i % 2 == 1 && j % 2 == 0){
    				board[i][j] = new Piece(Color.WHITE, i, j);
    				whitePieces.add(board[i][j]);
    			}
    		}
    	}
    }
    
    public void init_black() {
    	for (int i = 5; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (i % 2 == 0 && j % 2 == 1) {
    				board[i][j] = new Piece(Color.BLACK, i, j);
    				blackPieces.add(board[i][j]);
    			} else if (i % 2 == 1 && j % 2 == 0){
    				board[i][j] = new Piece(Color.BLACK, i, j);
    				blackPieces.add(board[i][j]);
    			}
    		}
    	}
    }
    
    public int getWhitePieces() {
    	int whitePiece = 0 ; 
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) continue;
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
				if (board[i][j] == null) continue;
    			if (board[i][j].getColor().equals(Color.BLACK)) {
    				blackPiece ++;
    			}
    		}
    	}
    	
    	return blackPiece; 
    }
    
    public ArrayList<Piece> getWhitePiecesList() {
    	return whitePieces;
    }

    public ArrayList<Piece> getBlackPiecesList() {
    	return blackPieces;
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
    

    public ArrayList<Piece> move(ArrayList<int[]> path, Color pieceColor, boolean isKing) {
        // Remove captured pieces along the path
    	
		ArrayList<Piece> out = new ArrayList<Piece>();
        for (int i = 0; i < path.size() - 1; i++) {
            int x = path.get(i)[0];
            int y = path.get(i)[1];
            System.out.println(x + " " + y);
            
            if (board[x][y] != null) {
                Piece pieceOnPath = board[x][y];
				out.add(pieceOnPath);
                System.out.println("PIECE TO REMOVE: " + pieceOnPath);
                if (pieceOnPath.getColor().equals(Color.BLACK)) {
                    blackPieces.remove(pieceOnPath);
                } else {
                    whitePieces.remove(pieceOnPath);
                }
                board[x][y] = null;
            }
        }

       
        int newX = path.get(path.size() - 1)[0];
        int newY = path.get(path.size() - 1)[1];
        Piece newPiece = new Piece(pieceColor, newX, newY);
		out.add(newPiece);
        
        
        if (isKing)
        	newPiece.ToKing();
        
       
        if ((pieceColor.equals(Color.BLACK) && newX == 0) || (pieceColor.equals(Color.WHITE) && newX == 7)) {
            newPiece.ToKing();  
            System.out.println("Piece promoted to king!");
        }
        board[newX][newY] = newPiece;
        

        // Update the pieces list
        if (pieceColor.equals(Color.BLACK)) {
            blackPieces.add(newPiece);
        } else {
            whitePieces.add(newPiece);
        }
		
		return out;
    }
    
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
    		}
    	}
    	
    	return copy; 
    }
    
    public void setBoard(Piece[][] newBoard) {
        this.board = newBoard;
        // Rebuild the white and black piece lists
        whitePieces.clear();
        blackPieces.clear();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    Piece piece = board[i][j];
                    if (piece.getColor() == Color.WHITE) {
                        whitePieces.add(piece);
                    } else {
                        blackPieces.add(piece);
                    }
                }
            }
        }
    }

    
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