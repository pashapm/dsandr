package ru.jecklandin.duckshot;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Environment {
	
	public void init();
	
	public void draw(Canvas c, Paint p);
	
}
