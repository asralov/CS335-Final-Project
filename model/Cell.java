package model;
// Abror
public class Cell {
    private int x;
    private int y;
    private Color color;

    public Cell(Color color, int x, int y)
    {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public int x_cord() {return this.x;}
    
    public int y_cord() {return this.y;}

    public Color cell_color() {return this.color;}

    public String toString()
    {
        return (this.color == Color.WHITE) ? "white" : "black";
    }
}
