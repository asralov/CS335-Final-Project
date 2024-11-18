package model;
// Amyra and Abror's task
// Suggestion: Make it create once and make it reusable all the time
// not a FLYWEIGHT class

public class GameBoard {
    private Cell[][] board;

    public GameBoard()
    {
        init_board(); // getting a board
    }

    private void init_board()
    {
        this.board = new Cell[8][8];
        int row = 8, col = 8;
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                Color cell_color = ((i + j) % 2 == 0) ? Color.WHITE : Color.BLACK;
                this.board[i][j] = new Cell(cell_color, i, j);
            }
        }
    }

    public String toString()
    {
        String str_board = "";
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {   
                str_board += this.board[i][j].toString() + " ";
            }
            str_board += "\n";
        }
        return str_board;
    }
    
}