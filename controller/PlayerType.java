package controller;

import model.Color;

public interface PlayerType {

    public void make_a_move();
    public void incrScore(int taken);
    public int getScore();
    public Color getColor();
    public String getName();

} 