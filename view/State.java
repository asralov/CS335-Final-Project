package view;

import javax.swing.JFrame;
import controller.GameModeEnum;

/**
 * Represents a state in the Checkers game. 
 * Each state is responsible for setting up the game window, handling interactions,
 * and processing user actions such as cell clicks.
 */
public interface State {

    /**
     * Sets up the game state within the specified window.
     * This method is used for states where the game mode is not required.
     *
     * @param window The JFrame window where the state will be displayed.
     */
    public void setup(JFrame window);

    /**
     * Sets up the game state within the specified window with the provided game mode.
     *
     * @param window    The JFrame window where the state will be displayed.
     * @param gameMode  The game mode (e.g., Player vs Player, Player vs Computer) for this state.
     */
    public void setup(JFrame window, GameModeEnum gameMode);

    /**
     * Handles a click event on a cell within the game board.
     *
     * @param row The row index of the clicked cell.
     * @param col The column index of the clicked cell.
     */
    public void handleCellClick(int row, int col);
}
