package ru.jecklandin.duckshot;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class GameObject {
	
	public GameObject() {
		matrix = new Matrix();
	}
	
	public enum OBJ_TYPE {WAVE, DUCK, STONE};
	
	public int x;
	public int y;
	public float speed;
	
	protected Matrix matrix;
	public float offset = 0;
	public abstract float getNextOffset(float curOffset);
	public abstract OBJ_TYPE getRtti();
	public abstract void draw(Canvas c, Paint p);
}
