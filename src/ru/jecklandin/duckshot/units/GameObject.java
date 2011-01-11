package ru.jecklandin.duckshot.units;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class GameObject {
	
	protected static int OBJECT_ID = 0;
	protected int id = ++OBJECT_ID;
	
	public GameObject() {
		matrix = new Matrix();
	}
	
	public int x;
	public int y;
	public float speed;
	
	protected Matrix matrix = new Matrix();
	public float offset = 0;
	
	public float getNextOffset(float curOffset) {
		return this.offset;
	}
	
	public abstract void draw(Canvas c, Paint p);
	
	@Override
	public boolean equals(Object o) {
		return this.id == ((Wave)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
