package tests;

import static org.junit.jupiter.api.Assertions.*;
import controller.*;

import org.junit.jupiter.api.Test;
import view.*;

class GameManagerTest {
	
	private GameManager gameManager = new GameManager();

	@Test
	void testInitializing() {
		System.out.println(gameManager);
	}
	
	@Test
	public void testOnPieceClick_ValidPiece() {
	    gameManager.OnPieceClick(2, 1); // Assuming this is a valid piece
	    System.out.println(gameManager);
	    assertEquals(GameStateEnum.Selected, gameManager.getState());
	}
	
	@Test
	public void testOnPieceClick_MovePiece() {
	    gameManager.OnPieceClick(2, 1);
	    System.out.println(gameManager);
	    assertEquals(GameStateEnum.Selected, gameManager.getState());
	    gameManager.OnPieceClick(3, 2);
	    System.out.println(gameManager);
	    assertEquals(GameStateEnum.Unselected, gameManager.getState());
	}
	
	@Test
	public void testMovingTwoPlayers() {
		gameManager.OnPieceClick(2, 1);
		System.out.println(gameManager);
		gameManager.OnPieceClick(3, 2);
		System.out.println(gameManager);
		
		gameManager.OnPieceClick(5, 4);
		System.out.println(gameManager);
		gameManager.OnPieceClick(4, 3);
		System.out.println(gameManager);
		
		gameManager.OnPieceClick(3, 2);
		System.out.println(gameManager);
		gameManager.OnPieceClick(5, 4);
		System.out.println(gameManager);
	}
}
