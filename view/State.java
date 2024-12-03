package view;

import javax.swing.JFrame;

public interface State {
    public void setup(JFrame window);
    
    public void handleCellClick(int row, int col); 
}
