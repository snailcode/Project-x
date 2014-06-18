package com.trufflemuffle;

public class Level {

	/**
	 * Current Level
	 */
	private int curLevel;
	
	/**
	 * Points for the next level
	 */
	private int[] scorePerLvlIncrease;
	
	/**
	 * Index for the next score
	 */
	private int nextScoreIndex;
	
	/**
	 * Next score
	 */
	private int nextScore;
	
	/**
	 * Constructor
	 * 
	 * Initialize the array and the attributes
	 */
	public Level() {
		this.scorePerLvlIncrease = new int[]{2, 5, 10, 12, 14, 16, 18};
		this.nextScoreIndex = 0;
		this.curLevel = 1;
		this.nextScore = this.scorePerLvlIncrease[nextScoreIndex];
	}
	
	/**
	 * Increases the Speed and returns the new calculated value
	 * 
	 * @param y Current falling speed
	 * @return new falling speed
	 */
	public int increaseSpeed(int y) {
		return (int)(this.curLevel * 1.1 + y * 0.9);
	}
	
	/**
	 * Decreases the spawn time for the new muffin
	 * 
	 * @param time Current spawn - time
	 * @return new spawn - time
	 */
	public long decreaseSpawnTime(long time) {
		return  (long)(time * 0.7 - this.curLevel * 2.5);
	}
	
	/**
	 * Checks the given points with nextScore and increases the level.
	 * It also decides if the falling speed will increase (level is odd) or the
	 * spawn - time decrease (level is even)
	 * 
	 * @param points Points that the player currently have
	 * @return true if there was a level increase, false if not
	 */
	public boolean checkPointsAndIncraseLevel(int points) {
		// Checks if the playerscore is equal to next score you need for
		// a level up
		if(this.nextScore == points) {
			this.nextScoreIndex++;
			
			// Checks if the index i still within the range of the scorePerLvlIncrease array
			// if so, the next score will increase, if not, it'll get the value -1, which means
			// there aren't higher level
			this.nextScore = this.nextScoreIndex >= this.scorePerLvlIncrease.length ? -1 : this.scorePerLvlIncrease[this.nextScoreIndex];
			this.increaseLevel();
			
			// Checks if the level is even.
			// If it's even, the spawn interval will decrease
			// If it's odd, the speed will increase
			if(this.curLevel % 2 == 0) {
				Muffin.setMuffinSpawnInterval(this.decreaseSpawnTime(Muffin.getSpawnInterval()));
			} else {
				Muffin.setMuffinYSpeed(this.increaseSpeed(Muffin.getMuffinYSpeed()));
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Increases the level if it's within the range of the length of
	 * the scorePerLvlIncrease array
	 */
	private void increaseLevel() {
		if(this.curLevel < this.scorePerLvlIncrease.length) {
			this.curLevel++;
		}
	}
	
	/**
	 * Returns the current level
	 * 
	 * @return Current level
	 */
	public int getLevel() {
		return this.curLevel;
	}
}
