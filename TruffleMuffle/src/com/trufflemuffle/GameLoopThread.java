package com.trufflemuffle;

import android.graphics.Canvas;

public class GameLoopThread extends Thread{
	
	static final long FPS = 20;
	private GameView view;
	private boolean isRunning = false;
	
	public GameLoopThread(GameView view) {
		this.view = view;
	}
	
	public void setRunning(boolean run) {
		isRunning = run;
	}
	
	@Override
	public void run() {
		long TPS = 1000 / FPS;
		long startTime, sleepTime;
		
		while (isRunning) {
			Canvas canvas = null;
			startTime = System.currentTimeMillis();
			try {
				canvas = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.draw(canvas);
				}
			} finally {
				if (canvas != null) {
					view.getHolder().unlockCanvasAndPost(canvas);
				}
			}
			sleepTime = TPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0) {
					sleep(sleepTime);
				}
				else {
					sleep(10);
				} 
			} catch (Exception e) {
				
			}
		}
	}

}
