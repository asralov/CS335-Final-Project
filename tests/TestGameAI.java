package tests;

import static org.junit.jupiter.api.Assertions.*;

import model.Piece;
import model.Color;
import controller.Computer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TestGameAI {

    @Test
    void testAI1() {
       
        Piece[][] board = new Piece[8][8];
        
        
        board[3][4] = new Piece(Color.WHITE, 3, 4); 
        board[3][2] = new Piece(Color.WHITE, 3, 2);
        board[4][1] = new Piece(Color.BLACK, 4, 1); // AI's piece
        
        
        board[4][1].ToKing();
        
        Computer aiPlayer = new Computer("AI Player", Color.BLACK);
        ArrayList<Piece> movablePieces = new ArrayList<>();
        movablePieces.add(board[4][1]);

        ArrayList<int[]> selectedMove = aiPlayer.make_a_move(movablePieces, board);

        // The longest path would be a capture sequence: (4, 1) -> (3, 2) -> (3, 4) -> (4, 5)
        assertEquals(5, selectedMove.size()); 
        assertArrayEquals(new int[]{3, 2}, selectedMove.get(1)); 
        assertArrayEquals(new int[]{4, 5}, selectedMove.get(selectedMove.size()-1)); 
    }
    
    @Test
    void testAI2() {
       
        Piece[][] board = new Piece[8][8];
        
        
        board[3][4] = new Piece(Color.WHITE, 3, 4); 
        board[4][1] = new Piece(Color.BLACK, 4, 1); // AI's piece
        
        Computer aiPlayer = new Computer("AI Player", Color.BLACK);
        ArrayList<Piece> movablePieces = new ArrayList<>();
        movablePieces.add(board[4][1]);

        ArrayList<int[]> selectedMove = aiPlayer.make_a_move(movablePieces, board);
        // Since, there are no captures available, it rakes the first available longest path
        // The longest path would be a capture sequence: (4, 1) -> (3, 2) 
        assertEquals(2, selectedMove.size()); 
        assertArrayEquals(new int[]{3, 2}, selectedMove.get(1)); 
        assertArrayEquals(new int[]{3, 2}, selectedMove.get(selectedMove.size()-1)); 
    }
}
