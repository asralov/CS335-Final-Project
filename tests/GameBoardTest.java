package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.GameBoard;

// Pulat's task
public class GameBoardTest {
    
	private GameBoard gameBoard = new GameBoard();
	
	@Test
	public void TestGetCell() {
		assertNull(gameBoard.get_cell(-1, 0));
		assertNull(gameBoard.get_cell(8, 0));
		assertNull(gameBoard.get_cell(-1, -1));
		assertNull(gameBoard.get_cell(0, 8));
	}

}
