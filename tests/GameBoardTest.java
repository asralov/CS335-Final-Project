package tests;

import static org.junit.jupiter.api.Assertions.*;
<<<<<<< HEAD
import org.junit.jupiter.api.Test;

import model.*;
=======

import org.junit.jupiter.api.Test;

import model.GameBoard;

>>>>>>> origin/main
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
