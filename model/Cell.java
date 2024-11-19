package model;
// Abror
public class Cell {
    private int x;
    private int y;
    private Color color;
    private Piece piece = null; // by default

    public Cell(Color color, int x, int y)
    {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public int x_cord() {return this.x;}
    
    public int y_cord() {return this.y;}

    public boolean is_taken() {return this.piece != null;}

    public void take(Piece piece) {this.piece = piece;}

    public Color cell_color() {return this.color;}

    public String toString()
    {
        return (this.color == Color.WHITE) ? "white" : "black";
    }
}
