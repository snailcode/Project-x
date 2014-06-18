package com.trufflemuffle;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Highscore {
	public static final String KEY = "Score";
	
	private int currentScore;
	private int greatestScore;
	
	public Highscore(SharedPreferences prefs) {
		this.greatestScore = this.loadHighscore(prefs);
	}
	
	
	public void setNewHighscore(SharedPreferences prefs) {
		if(this.currentScore > this.greatestScore) {
			Editor editor = prefs.edit();
			editor.putInt(KEY, this.currentScore);
			editor.commit();
		}
	}
	
	
	private int loadHighscore(SharedPreferences prefs) {
		return prefs.getInt(KEY, 0);
	}
	
	public int getHighScore() {
		return this.greatestScore;
	}
	
	public int getCurrentScore() {
		return this.currentScore;
	}
	
	public void setScore(int score) {
		this.currentScore = score;
	}
}
