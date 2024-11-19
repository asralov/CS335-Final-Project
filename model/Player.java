package model;

public class Player {
	private String name; 
	private int score; 
	private Color color; 
	
	public Player(String name, Color color) {
		this.name = name;
		this.color = color; 
		this.score = 0; 
	}
	
	public void incrScore(int taken) {
		this.score += taken; 
	}
	
	public int getScore() {
		return this.score; 
	}
	
	public Color getColor() {
		return this.color; 
	}
}
