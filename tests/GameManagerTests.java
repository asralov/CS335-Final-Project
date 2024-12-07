package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.GameManager;
import model.Color;
import model.GameBoard;
import model.Piece;
import controller.GameModeEnum;
import view.Cell;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTests {

    private GameManager gameManager;
    private GameBoard board;
    private JPanel panel;

    @BeforeEach
    public void setUp() {
    	// This is the setup which we will use to test some elements of the GameManager Tests
        panel = new JPanel();
        board = new GameBoard();
        gameManager = new GameManager(panel, board, null, GameModeEnum.PvP);
    }

    @Test
    public void testInitialization() {
    	// The initialzed game manager should not be null
        assertNotNull(gameManager);
        assertNotNull(gameManager.toString());
    }
    
    @Test
	private void testFirstMove() {
    	// The first move should be given to the white colored pieces
		assertEquals(Color.WHITE, gameManager.getCurrentTurn());
	}

    @Test
    public void testTurn() {
    	// Test to see if the current switch turn changes the turn to the black piece
        gameManager.SwitchCurrentTurn();
        assertEquals(Color.BLACK, gameManager.getCurrentTurn());
    }

   

    @Test
    public void testMove() {
    	// Testing a regular piece and checking if the piece has moved and the change of state is reflected ibn game manager
        Piece piece = new Piece(Color.WHITE, 2, 2);
        board.move(piece, 2, 2);

        gameManager.OnPieceClick(2, 2);
        gameManager.OnPieceClick(3, 3);

        // The piece should have moved from there to (3,3)
        assertNull(board.getPiece(2, 2));
        assertNotNull(board.getPiece(3, 3));
    }

    @Test
    public void testGetMovablePieces() {
    	/// Test to see if we get a non empty array list to get movable pieces.
        ArrayList<Piece> pieces = gameManager.GetMovablePieces();
        assertEquals(4, pieces.size());
        assertNotNull(pieces);
    }

    
}
