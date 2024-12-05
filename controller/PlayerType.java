package controller;

import java.util.ArrayList;

import model.Color;
import model.Piece;

public interface PlayerType {

    public ArrayList<int[]> make_a_move(ArrayList<Piece> movablePieces, Piece[][] board);
    public void incrScore(int taken);
    public int getScore();
    public Color getColor();
    public String getName();

} 