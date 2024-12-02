// hello
package view;

import javax.swing.*;

public class Checkers {
    public static State game_state;
    public static JFrame window;
    public static void main(String[] args) {
        
        window = new JFrame("Checkers");

        // default position to be a center of the window
        window.setLocationRelativeTo(null);
        // setting to the max size
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // default operation to exit on the closing
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // creating a state instance to keep track of what state we are in
        // and by default we start with menu
        game_state = new Menu();
        game_state.setup(window);
        
        // displaying the window
        window.setVisible(true);
    }
}
