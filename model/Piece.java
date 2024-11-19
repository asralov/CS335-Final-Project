package model;
// Suhani's task
public class Piece {
    private Color color; 
    private boolean isKing;
    private int row;
    private int column; 
    private GameBoard board = new GameBoard();

    public Piece(Color color, int row, int column) {
        this.color = color;
        this.row = row;
        this.column = column;
        this.isKing = false; 
        
    }

    public Color getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void move(int newRow, int newCol) {
    	
    	if (this.isValidMove(newRow, newCol))
    	{
        this.row = newRow;
        this.column = newCol;
    	}
    }

    public void ToKing() {
        this.isKing = true;
    }

    public boolean isValidMove(int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - this.row);
        int colDiff = Math.abs(newCol - this.column);

        if (rowDiff != colDiff) {
            return false;
        }

        if (isKing) 
        {
            int rowStep = (newRow > this.row) ? 1 : -1;
            int colStep = (newCol > this.column) ? 1 : -1;
            int currentRow = this.row + rowStep;
            int currentCol = this.column + colStep;

            while (currentRow != newRow && currentCol != newCol) {
                if (board.getCell(currentRow, currentCol).isOccupied()) {
                    return false; 
                }
                currentRow += rowStep;
                currentCol += colStep;
            }
        } 
        else 
        {
            
            if (rowDiff != 1 || colDiff != 1) {
                return false;
            }
            if (this.color == Color.WHITE && newRow <= this.row) {
                return false; 
            }
            if (this.color == Color.BLACK && newRow >= this.row) {
                return false; 
            }
            
            if (board.getCell(newRow, newCol).isOccupied())
            	return false;
            else
            	return true;
        }
        
        return true;
    }


    @Override
    public String toString() {
        return (isKing ? "King" : "Piece") + " [" + color + "] at (" + row + ", " + column + ")";
    }
}

