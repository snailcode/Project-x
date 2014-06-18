package com.trufflemuffle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class GameActivity extends Activity {

	private GameView gameView;
	private Music music;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.gameView = new GameView(this);
		setContentView(gameView);
		music = new Music(this, this, R.raw.music);
		music.playMusic();
	}
	
	public void onGameOver() {
		Intent gameOverscreen = new Intent(getApplicationContext(), GameOverActivity.class);
		gameOverscreen.putExtra("score", gameView.getScore());
		music.stopMusic();
		startActivity(gameOverscreen);
		this.finish();
	}
	
	public void onGameOverHighscore() {
		Intent gameOverscreenOL = new Intent(getApplicationContext(), GameOverOL.class);
		gameOverscreenOL.putExtra("score", gameView.getScore());
		music.stopMusic();
		startActivity(gameOverscreenOL);
		this.finish();
	}
	
}
