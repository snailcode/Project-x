package com.trufflemuffle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
	
	private Bitmap bmp;
	private Vector vPosition;
	private int width;
	private int height;

	public Sprite(Bitmap bmp) {
		this.bmp = bmp;
		this.width = bmp.getWidth();
		this.height = bmp.getHeight();
		this.vPosition = new Vector(0,0);
	}
	
	// Draw-Method   
	public void draw(Canvas canvas, int x, int y) {
		canvas.drawBitmap(bmp, x, y, null);
	}
	
	public boolean isTouched(float x, float y) {
		return x > vPosition.x && x < vPosition.x + width && y > vPosition.y && y < vPosition.y + height; 
	}
	
	// Getter and Setter Methods
	public int getHeight() {
		return this.bmp.getHeight();
	}
	
	public int getWidth() {
		return this.bmp.getWidth();
	}
	
	// Get actual bitmap of Sprite
	public Bitmap getBitmap() {
		return this.bmp;
	}
	
	public Vector getPosition() {
		return this.vPosition;
	}
	
	public void setPosition(int x, int y) {
		this.vPosition.x = x;
		this.vPosition.y = y;
	}
}
