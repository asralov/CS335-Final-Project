package controller;

import java.util.EventListener;

/**
 * Listener interface for handling game-over and piece movement events in the Checkers game.
 * Classes implementing this interface can respond to game-over events and requests for moved pieces.
 */
public interface GameOverListener extends EventListener {

    /**
     * Called when a game-over event occurs.
     *
     * @param event The {@link GameOverEvent} containing information about the game-over state, including the winner.
     */
    void GameOverOccurred(GameOverEvent event);

    /**
     * Called to handle an event requesting information about moved pieces.
     *
     * @param event The {@link GetMovedPieces} event containing details about the moved pieces.
     */
    void GetMovedPieces(GetMovedPieces event);
}
