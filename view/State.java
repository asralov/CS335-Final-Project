package view;

import javax.swing.JFrame;

import controller.GameModeEnum;

public interface State {
    public void setup(JFrame window);
    public void setup(JFrame window, GameModeEnum gameMode);
    public void handleCellClick(int row, int col); 
}
