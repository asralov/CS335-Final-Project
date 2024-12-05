package tests;

import controller.GameManager;
import model.*;

import java.util.ArrayList;

public class TestGameAI {

    public static void main(String[] args) {
        // Create a GameManager instance for testing AI
        GameManager gameManager = new GameManager();

        // Set up some test data (game state)
        initializeTestGame(gameManager);

        // Test Simple AI move
        System.out.println("Testing Simple AI Move:");
        ArrayList<int[]> simpleAIMove = gameManager.simpleAI();
        printMoveDetails(simpleAIMove);

    }

    // Initialize the game with a simple board setup
    private static void initializeTestGame(GameManager gameManager) {
        // Reset game board and any other necessary state
        gameManager = new GameManager();
        // Add any setup here if needed, for example setting player types, move counts, etc.
    }

    // Helper method to print the details of the move
    private static void printMoveDetails(ArrayList<int[]> move) {
        if (move != null && !move.isEmpty()) {
            for (int[] moveCoords : move) {
                System.out.println("Move to: (" + moveCoords[0] + ", " + moveCoords[1] + ")");
            }
        } else {
            System.out.println("No valid move found.");
        }
    }
}
