package com.trufflemuffle;

import android.graphics.Canvas;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationText {

	private String text;
	private Canvas canvas;
	
	public AnimationText(String text, Canvas canvas) {
		this.text = text;
		this.canvas = canvas;
	}

}
