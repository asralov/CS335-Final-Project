package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Color;
import model.Piece;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MoveTests {

    private Piece[][] gameBoard;

    @BeforeEach
    public void setUp() {
        gameBoard = new Piece[8][8]; 
    }

    @Test
    public void testGetPossibleMoves1() {
    	// Testin for a regular piece
        gameBoard[3][4] = new Piece(Color.WHITE, 3, 4);
        Piece piece = gameBoard[3][4];
        ArrayList<ArrayList<int[]>> moves = model.Move.getPossibleMoves(piece, gameBoard);

        assertEquals(2, moves.size());
        assertArrayEquals(new int[]{3, 4}, moves.get(0).get(0));
        assertArrayEquals(new int[]{4, 5}, moves.get(0).get(1));
    }

    @Test
    public void testGetPossibleMoves2() {
    	// Testing the possible moves for a king
        gameBoard[4][4] = new Piece(Color.BLACK, 4, 4);
        Piece piece = gameBoard[4][4];
        piece.ToKing();
        ArrayList<ArrayList<int[]>> moves = model.Move.getPossibleMoves(piece, gameBoard);

        assertEquals(4, moves.size());
        assertArrayEquals(new int[]{4, 4}, moves.get(0).get(0));
    }

    @Test
    public void testCaptureMove() {
    	// Testing the moves where there can be captures
        gameBoard[3][4] = new Piece(Color.WHITE, 3, 4);
        gameBoard[4][5] = new Piece(Color.BLACK, 4, 5);
        Piece piece = gameBoard[3][4];
        ArrayList<ArrayList<int[]>> moves = model.Move.getPossibleMoves(piece, gameBoard);

        assertEquals(1, moves.size());
        ArrayList<int[]> capturePath = moves.get(0);
        assertEquals(3, capturePath.size());
        assertArrayEquals(new int[]{3, 4}, capturePath.get(0));
        assertArrayEquals(new int[]{4, 5}, capturePath.get(1));
        assertArrayEquals(new int[]{5, 6}, capturePath.get(2));
    }


}
