package tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Color;
import model.Piece;
import controller.Player;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

    private Player player;

    @BeforeEach
    public void setUp() {
    	// Setting the name of the player and the color of the piece
        player = new Player("abc", Color.WHITE);
    }

    @Test
    public void testGetColor() {
    	// Test get color
        assertEquals(Color.WHITE, player.getColor());
    }

    @Test
    public void testGetName() 
    {
    	// Test get name
        assertEquals("abc", player.getName());
    }

    @Test
    public void score() {
    	// Test get score
        player.incrScore(5);
        assertEquals(5, player.getScore());
        
    }

    @Test
    public void test1() {
    	// Mandatory method for classes that implement Player.java
        ArrayList<Piece> movablePieces = new ArrayList<>();
        Piece[][] board = new Piece[8][8];
        ArrayList<int[]> result = player.make_a_move(movablePieces, board);
        assertNull(result);
    }
}
