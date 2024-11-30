package tests;

import static org.junit.jupiter.api.Assertions.*;
import controller.*;

import org.junit.jupiter.api.Test;

class GameManagerTest {
	
	private GameManager gameManager = new GameManager();

	@Test
	void testInitializing() {
		System.out.println(gameManager);
	}
	
	@Test
	public void testOnPieceClick_ValidPiece() {
	    GameManager manager = new GameManager();
	    manager.OnPieceClick(0, 1); // Assuming this is a valid piece
	    assertEquals(GameStateEnum.Selected, manager.getState());
	    System.out.println(gameManager);
	}


}
