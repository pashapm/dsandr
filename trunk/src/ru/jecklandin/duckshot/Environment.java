package ru.jecklandin.duckshot;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Environment {
	
	protected int mBgColor;
	
	public Environment(int bgColor) {
		mBgColor = bgColor;
	}
	
	public abstract void init();
	
	public abstract void draw(Canvas c, Paint p);
	
}
