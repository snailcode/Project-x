package com.trufflemuffle;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;


public class Player {
	private Sprite spPlayer;		// Sprite for Player
	private Vector position;		// position of the Player
	private char curPosition;		// Current mudhole position of the player
	private int point;				// Spielerpunkte (zum Start 0);
	private int life;				// Lifes of the player
	private int combo;				// Points in a row
	private String strCombo;
	private SoundManager sm;		/** Playin Sounds */
	private boolean goldenMuffin;	/** Indicates if the player collected a golden muffin */
	private char lastCollected;		/** Stores the last collected Muffin => n = normal, e = evil, g = golden */
	
	// Constructor
	public Player(Sprite sprite, int ground, Combo combo, Context context) {
		this.spPlayer = sprite;
		// At the beginning, the Position of the Player is always
		// the first Mudhole (Rightside)
		this.position = new Vector(0,ground);  
		this.curPosition = 'A';
		this.point = 0;
		this.life = 3;
		this.combo = 0;
		this.strCombo = "";
		this.sm = new SoundManager(context);
		this.lastCollected = ' ';
	}
	
	public void checkCollision(LinkedList<Muffin> muffins) {
		for (Muffin m : muffins) {			
			if ((this.position.y-this.spPlayer.getBitmap().getHeight()+m.getSprite().getHeight()/3 <= m.getPosition().y) &&
				(this.getCurPosition() == m.getPipe())){
				
				// Sets the last collected Muffin type
				this.lastCollected = m.getToken();
				
				// Triggers the effect of the muffin
				m.doEffect(this);
				
				// Set the status of the muffin to "dead"
				m.setAlive(false); 
			}
			else if (m.checkCollision(this.position.y - m.getSprite().getHeight()/2) &&
					(this.getCurPosition() != m.getPipe()) && (m.getToken() == 'n')) {
					this.decreaseLife();
					this.combo = 0;
					
			}
		}
	}
	
	// Draw-Method for Player
	public void draw(Canvas canvas, int xPosition) {
		this.position.x = xPosition;
		
		if (this.position.y == 0) {
			this.position.y = (int) (canvas.getHeight()/1.6);
		}
		
		canvas.drawBitmap(spPlayer.getBitmap(), this.position.x, this.position.y, null);
		animation(canvas);
	}
	
	// Method for Animation
	public void animation(Canvas canvas) {
		
	}
	
	// Move the player
	public void move(String direction) {
		if (direction.equals("left")) {
			if (curPosition == 'B') {
				this.curPosition='A';
			}
			else if (curPosition == 'C') {
				this.curPosition='B';
			}
		}
		else {
			if (curPosition=='A') {
				this.curPosition='B';
			}
			else if (curPosition=='B') {
				this.curPosition = 'C';
			}
		}
	}
	
	public void resetCombo() {
		this.combo = 0;
	}
	
	private void decreaseLife() {
		this.life--;
	}
	
	public int getLifeCount() {
		return this.life;
	}
	
	public void increaseLife() {
		this.life++;
	}
	
	public void increasePoint() {
		this.point++;
		this.combo++;
	}
	
	public void playEvilMuffin() {
		this.sm.playEvilMuffin();
	}
	
	
	
	/*
	 * Setter - Methods	
	 */
	public void setPoints(int points) {
		this.point = points;
	}
	
	public void setGoldenMuffin(boolean goldenMuffin) {
		this.goldenMuffin = goldenMuffin;
	}
	
	/*
	 * Getter - Methods
	 */

	
	public Vector getPosition() {
		return this.position;
	}
	
	public char getCurPosition() {
		return this.curPosition;
	}
	
	public char getLastCollected() {
		return this.lastCollected;
	}

	public Sprite getSprite() {
		return this.spPlayer;
	}
	
	public int getPoint() {
		return this.point;
	}
	
	public int getCombo() {
		return this.combo;
	}
	
	public boolean hasGoldenMuffin() {
		return this.goldenMuffin;
	}
	
}
