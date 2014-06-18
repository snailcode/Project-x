package com.trufflemuffle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity implements OnClickListener{
	
	private Button bReplay;
	private Button bExit;
	private int score;
	private Music music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		music = new Music(this, R.raw.gameover);
		music.playMusic();
		this.initialize();
		// Buttons
		bReplay = (Button) findViewById(R.id.bReplay);
		bExit = (Button) findViewById(R.id.bExit);
		bReplay.setOnClickListener(this);
		bExit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bReplay:
			Intent GameScreen = new Intent(this, GameActivity.class);
			startActivity(GameScreen);
			music.stopMusic();
			finish();
			break;
		case R.id.bExit:
			music.stopMusic();
			finish();
			break;
		}	
	}
	
	public void initialize() {
		score = this.getIntent().getExtras().getInt("score");
		TextView tvScore = (TextView) findViewById(R.id.score);
		tvScore.setText("Score: " + Integer.toString(score));
	}

}
