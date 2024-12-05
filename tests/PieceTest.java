package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.*;
// Pulat's task
public class PieceTest {
	
	private Piece newPiece = new Piece(Color.BLACK, 0, 1);
	
	@Test
	public void TestPieceDefaultColorAndCoordinates() {
		assertEquals(Color.BLACK, newPiece.getColor());
		assertEquals(1, newPiece.getColumn());
		assertEquals(0, newPiece.getRow());
	}
	
	@Test
	public void TestIsKing() {
		assertFalse(newPiece.isKing());
		newPiece.ToKing();
		assertTrue(newPiece.isKing());
	}

	@Test
	public void ToString() {
		System.out.println(newPiece);
	}
    
}
