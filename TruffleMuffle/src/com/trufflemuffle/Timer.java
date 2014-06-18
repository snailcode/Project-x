package com.trufflemuffle;

public class Timer {
	private long currentTime;
	private long elapsedTime;
	
	public Timer() { 
		currentTime = System.currentTimeMillis();
	}
	
	public void update() {
		this.elapsedTime = System.currentTimeMillis() - this.currentTime;
		this.currentTime = System.currentTimeMillis();
	}
	
	public long getElapsedTime() {
		return this.elapsedTime;
	}
}
