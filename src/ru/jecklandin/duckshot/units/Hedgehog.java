package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.ImgManager;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Hedgehog extends CreatureObject {

	public Hedgehog(int x) {
		super(); 
		this.offset = x; 
		speed = 2;
		addit_m = new Matrix();
	}

	@Override
	protected void drawNormal(Canvas c, Paint p) {
		c.drawBitmap(commonBm, matrix, p);
		float[] point = new float[] {0, 0};   
		matrix.mapPoints(point);
		drawHealth(point, c, p);
	}

	public static void initBitmaps() {
		CreatureObject.commonBm = ImgManager.getBitmap("creature");
	}
	
	@Override
	public float getNextOffset(float curOffset) {
		if (isDead) {
			return offset;
		}
		
		overallTicks++;
//		if (Desk.getInstance().getSightVisibility() 
//				&& overallTicks % (DuckApplication.FPS) == 0 && !isDiving) {
//			if (isDanger()) {  //the sight is nearby!
//				dive();
//			}
//		}
		
		if (mMovingRight) {
			offset += speed;
		} else {
			offset -= speed;
		}
		
		if (offset < MIN_OFFSET) {
			mMovingRight = true;
		} else if (offset > MAX_OFFSET) {
			mMovingRight = false;
		}
		return offset;
	}

	@Override
	public void draw(Canvas c, Paint p) {
		if (end_animation) {
			toRecycle = true;
			return;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o == null || o.getClass() != getClass()) {
			return false;
		}
		return this.id == ((Duck)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
}
