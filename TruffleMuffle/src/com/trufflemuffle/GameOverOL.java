package com.trufflemuffle;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class GameOverOL extends Activity implements OnClickListener{

	private Button bReplay;
	private Button bExit;
	private int score;
	private Music music;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over_ol);
		music = new Music(this, R.raw.highscore);
		music.playMusic();
		this.initialize();
		// Buttons
		bReplay = (Button) findViewById(R.id.bReplay);
		bExit = (Button) findViewById(R.id.bExit);
		bReplay.setOnClickListener(this);
		bExit.setOnClickListener(this);
		TextView tv = (TextView) findViewById(R.id.tvCongrat);
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(50);
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		tv.startAnimation(anim);
		
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
		TextView tvScore = (TextView) findViewById(R.id.tvNewScore);
		tvScore.setText("Your new Highscore: " + Integer.toString(score));
	}
	

}
