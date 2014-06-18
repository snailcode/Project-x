package com.trufflemuffle;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	private SoundPool soundPool;
	private int extraLife;
	private int evilmuffin;
	private int comboSound[];

	public SoundManager(Context context) {
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		extraLife = soundPool.load(context, R.raw.extralife, 1);
		evilmuffin = soundPool.load(context, R.raw.evilmuffin, 1);

	}
	
	public void playExtraLife() {
		soundPool.play(extraLife, 100, 100, 1, 0, 1);
	}
	
	public void playEvilMuffin() {
		soundPool.play(evilmuffin, 150, 150, 1, 0, 1);
	}
	
	public void playComboSound(int index) {
		soundPool.play(comboSound[index], 100, 100, 1, 0, 1);
	}
}
