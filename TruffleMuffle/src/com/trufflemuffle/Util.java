package com.trufflemuffle;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class Util {

	private Context context;
	
	public Util(Context context) {
		this.context = context;
	}
	
	public TextView createTextview(String text) {
		TextView tv = new TextView(context);
		tv.setText(text);
		return tv;
	}
	
}
