package controller;

import java.util.EventObject;

/**
 * Represents an event that signals the end of a game in the Checkers application.
 * It includes information about the winner of the game.
 */
public class GameOverEvent extends EventObject {

    private final String winner; // The name of the player who won the game

    /**
     * Constructs a new GameOverEvent.
     *
     * @param source The object on which the event initially occurred.
     * @param winner The name of the player who won the game.
     */
    public GameOverEvent(Object source, String winner) {
        super(source);
        this.winner = winner;
    }

    /**
     * Returns the name of the winner.
     *
     * @return The winner of the game.
     */
    public String getWinner() {
        return winner;
    }
}
