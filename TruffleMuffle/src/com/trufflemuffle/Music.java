package com.trufflemuffle;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Music {
	
	private MediaPlayer mediaPlayer;
	private AudioManager audioManager;
	
	public Music(GameActivity ga, Context context, int r) {
		mediaPlayer = MediaPlayer.create(context, r);
		mediaPlayer.setLooping(true);

	}
	
	public Music(Context context, int r) {
		mediaPlayer = MediaPlayer.create(context, r);
		mediaPlayer.setLooping(true);
	}
	
	public void playMusic() {

		mediaPlayer.start();
	}
	
	public void stopMusic() {
		mediaPlayer.stop();
	}

	// Aufr�umarbeiten f�r Sound, Grafik und Musik hier hin

}
