package model;
// Suhani's task
public class Piece {
    private Color color; 
    private boolean isKing;
    private int x;
    private int y; 

    public Piece(Color color, int x, int y) {
        assert ((x+y)%2==0): "Invalid Piece";
        this.color = color;
        this.x = x;
        this.y = y;
        this.isKing = false; 
        
    }
    
    public Piece(Piece p)
    {
    	
    	p.color = this.color;
    	p.x = this.x;
    	p.y = this.y;
    	p.isKing = this.isKing;
    	
    }

    public Color getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public int getRow() {
        return x;
    }

    public int getColumn() {
        return y;
    }

    public void ToKing() {
        this.isKing = true;
    }
    
    @Override
    public String toString() {
        return (isKing ? "King" : "Piece") + " [" + color + "] at (" + x + ", " + y + ")";
    }
}

