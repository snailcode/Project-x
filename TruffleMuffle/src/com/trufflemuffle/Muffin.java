package com.trufflemuffle;

public abstract class Muffin {
	
	/**
	 * The Spawn interval for all muffins
	 */
	private static long spawnInterval = 1400;
	
	/**
	 * The Falling speed for all muffins
	 */
	private static int muffinYSpeed = 8;
	
	/**
	 * Indicator to check if the muffin collides
	 */
	private boolean alive;	

	/**
	 * Muffin Sprite
	 */
	private Sprite spMuffin;	
	
	/**
	 * Muffin position
	 */
	private Vector position;
	
	/**
	 * The current Pipe of the Muffin (A, B, C)
	 */
	private char pipe;
	
	/**
	 * Token for each muffin. The first letter indicates the Muffin.
	 * n = normal-Muffin, e = evil-Muffin, g = golden-Muffin
	 */
	protected char token;
	
	
	
	/**
	 *  Constructor
	 *  
	 *  @param sprite the sprite, that'll hold the bmp and some other stuff for the muffin
	 *  @param position the vector position of the muffin
	 *  @param isAlive the indicator if the muffin is alive
	 *  
	 */
	public Muffin(Sprite sprite, Vector position, char pipe) {
		this.spMuffin = sprite;
		this.position = position;
		this.alive = true;
		
		this.pipe = pipe;
		this.setToken();
	}
	
	 /**
	  * Checks if the muffin collided either with the
	  *	ground or the player. If he collided, the indicator "alive"
	  *	will turn to false. It also returns if the muffin collided (true) or
	  *	not (false)
	  */
	public boolean checkCollision(int ground) {
		if(this.position.y >= ground) {
			this.alive = false;
			return true;
		} 
		
		return false;
	}
	
	
	
	/**
	 * Increases the y position of the muffin.
	 * It also checks if the muffin collides with the ground
	 * and returns true, if there was a collision, and false
	 * if there wasn't
	 * 
	 * @param ground Y-Position of the ground
	 * @return true if collided, false if not
	 */
	public boolean animation(int ground) {
		this.position.y = this.position.y + muffinYSpeed;
		return this.checkCollision(ground);
	}
	
	/**
	 * Each Muffin has it's own Effect after it was collected.
	 * 
	 * <p>
	 * NormalMuffin -> Point and Combo increase by 1
	 * <p>
	 * <p>
	 * EvilMuffin   -> Point decrease by 20% and combo will be set to 0
	 * </p>
	 * <p>
	 * GoldenMuffin	-> The Player will gain a shield that holds either for 10 seconds, or
	 * 				   the player collects an EvilMuffin. If the player collects an 
	 * 				   EvilMuffin, the effect of EvilMuffin wont trigger.
	 * </p>
	 * @param player The Player
	 */
	public abstract void doEffect(Player player);
	
	/**
	 * Each Muffin has to set his own Token
	 * @see com.trufflemuffle.Muffin#token
	 */
	public abstract void setToken();
	

	/*
	 * Setter Methods
	 */
	public void setPosition(Vector vector) {
		this.position = vector;
	}
	 
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public static void setMuffinYSpeed(int speed) {
		muffinYSpeed = speed;
	}
	
	public static void setMuffinSpawnInterval(long interval) {
		spawnInterval = interval;
	}
	
	public static void resetMuffinStats() {
		spawnInterval = 1400;
		muffinYSpeed = 8;
	}
	
	/*
	 * Getter Methods
	 */
	public boolean isAlive() {
		return this.alive;
	}
	
	
	public Sprite getSprite() {
		return this.spMuffin;
	}
	
	public Vector getPosition() {
		return this.position;
	}
	
	public char getToken() {
		return this.token;
	}
	
	public char getPipe() {
		return this.pipe;
	}
	
	public static long getSpawnInterval() {
		return spawnInterval;
	}
	
	public static int getMuffinYSpeed() {
		return muffinYSpeed;
	}
	
}
