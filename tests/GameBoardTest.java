package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.*;

import org.junit.jupiter.api.Test;


public class GameBoardTest {
    
	private GameBoard gameBoard = new GameBoard();
	
	@Test
	public void testInitialization()
	{
		assertEquals(12, gameBoard.getBlackPieces());
		assertEquals(12, gameBoard.getWhitePieces());
		
	}
	
	@Test
	public void testGetListPieces()
	{
		ArrayList<Piece> listPieces = gameBoard.getWhitePiecesList();
		ArrayList<Piece> listPieces1 = gameBoard.getBlackPiecesList();
		
		assertEquals(12, listPieces.size());
		assertEquals(12, listPieces1.size());
		
	}
	
	@Test
	public void testInitialPositions()
	{
		// Checking if there is no piece in the middle or in between the pieces
		assertNull(gameBoard.getPiece(3, 3));
		assertNull(gameBoard.getPiece(0, 0));
		
		// Checking if there is a piece at the start of the game
		assertNotNull(gameBoard.getPiece(0, 1));
		assertNotNull(gameBoard.getPiece(7, 0));
		
		
	}
	
	@Test
	public void testSimpleMovePiece() {
		// Test if it moves the piece correctly and changes the board structure
	    Piece piece = gameBoard.getPiece(2, 1);
	    
	    gameBoard.move(piece, 3, 2);
	    
	    assertNull(gameBoard.getPiece(2, 1)); 
	    assertEquals(piece.getColumn(), gameBoard.getPiece(3, 2).getColumn()); 
	    assertEquals(piece.getRow(), gameBoard.getPiece(3, 2).getRow()); 
	}
	
	  @Test
	    public void testKingPromotionDuringMove() {
	        ArrayList<int[]> pathToEnd = new ArrayList<>();
	        pathToEnd.add(new int[]{5, 0});
	        pathToEnd.add(new int[]{0, 0});  

	        gameBoard.move(pathToEnd, Color.BLACK, false);

	        Piece piece = gameBoard.getPiece(0, 0);
	        assertTrue(piece.isKing());
	        assertEquals(Color.BLACK, piece.getColor());
	    }
	  
	  @Test
	    public void testGetBoardCopyObjects() {
		  	GameBoard gameBoard = new GameBoard();
	        ArrayList<int[]> pathToEnd = new ArrayList<>();
	        pathToEnd.add(new int[]{5, 0});
	        pathToEnd.add(new int[]{1, 1});  

	        gameBoard.move(pathToEnd, Color.BLACK, false);
	        
	        Piece[][] p = gameBoard.getBoardCopy();
	        Piece[][] p1 = gameBoard.getBoardCopy();
	        
	        // Check if it is not the same object
	        
	        assertNotEquals(p, p1);
	        
	        // Check if we make a change in the copied board, the change is not reflected in the 
	        // original code
	        p1[7][0] = null;
	        assertNotNull(p[7][0]);
		  	
	     
	    }
	  
	  @Test
	    public void testGetBoardCopyContents() {
		  	GameBoard gameBoard = new GameBoard();
	        ArrayList<int[]> pathToEnd = new ArrayList<>();
	        pathToEnd.add(new int[]{5, 0});
	        pathToEnd.add(new int[]{1, 1});  

	        gameBoard.move(pathToEnd, Color.BLACK, false);
	        
	        Piece[][] p = gameBoard.getBoardCopy();
	        Piece[][] p1 = gameBoard.getBoardCopy();
	        
	        // Check if its contents are the same
	        
	        for (int i = 0; i < 8; i++) {
	            for (int j = 0; j < 8; j++) {
	                Piece piece = p[i][j];
	                Piece piece1 = p1[i][j];
	                // if the original peice is null at a particular
	                // position, the copied board should also have null
	                if (piece == null) {
	                    assertNull(piece1);
	                } else {
	                	// Checking if all the characteristics of the piece is same
	                    assertNotNull(piece1);
	                    assertEquals(piece.getColor(), piece1.getColor());
	                    assertEquals(piece.isKing(), piece1.isKing());
	                    assertNotEquals(piece, piece1);
	                }
	            }
	        }

	    }
	  
	  @Test
	    public void testToString() {	
		  // Checks if the gameBoard in the initial state is printed out as a string correctly
	        String s = gameBoard.toString();
	        String s1 = "   0 1 2 3 4 5 6 7\n"
                    + "   ----------------\n"
                    + "0| . w . w . w . w \n"
                    + "1| w . w . w . w . \n"
                    + "2| . w . w . w . w \n"
                    + "3| . . . . . . . . \n"
                    + "4| . . . . . . . . \n"
                    + "5| b . b . b . b . \n"
                    + "6| . b . b . b . b \n"
                    + "7| b . b . b . b . \n\n";

	        int x = s.compareTo(s1);
	        
	        assertEquals(0, x);
	        
	    }
	
	  @Test
	    public void testRemove() {	
		  // Checks if the gameBoard in the initial state is printed out as a string correctly
		  GameBoard gameBoard = new GameBoard();
	      gameBoard.removePiece(7, 0);
	      assertNull(gameBoard.getPiece(7, 0));
	        
	    }
	  
	    @Test
	    void testSetBoard() {
	        // Create a new board and initialize the new configuration
	        GameBoard board = new GameBoard(); 
	        Piece[][] newBoard = new Piece[8][8];
	        newBoard[0][0] = new Piece(model.Color.WHITE, 0, 0); // White piece at (0, 0)
	        newBoard[0][1] = new Piece(model.Color.BLACK, 0, 1); // Black piece at (0, 1)

	        board.setBoard(newBoard);
	        assertEquals(model.Color.WHITE, board.getBoardCopy()[0][0].getColor());
	        assertEquals(model.Color.BLACK, board.getBoardCopy()[0][1].getColor());
	        assertNull(board.getBoardCopy()[7][7]); 
	        assertEquals(1, board.getBlackPieces());
	       
	    }
	}



