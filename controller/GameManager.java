package controller;

import model.GameBoard;

public class GameManager 
{
    private GameBoard board;
    private int move_count;
    private Player p1;
    private PlayerType p2;

    public GameManager()
    {
        this.board = new GameBoard();
        this.move_count = 0;
    }

    


    // private void reset_move_count() {this.move_count = 0;}


}
