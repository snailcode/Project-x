package com.trufflemuffle;

public class Mudhole {
	private Vector position;
	private Sprite spMudhole;
	private char chIndex;
	
	// Constructor
	// 
	public Mudhole(Sprite sprite, Vector vector, char index) {
		this.spMudhole = sprite;
		this.position = vector;
		this.chIndex = index;
	}
	
	// Draw-Method
	public void draw() {
		
	}
	
	// Getter and Setter
	
	public Vector getPosition() {
		return this.position;
	}
	
	public char getIndex() {
		return this.chIndex;
	}
}
