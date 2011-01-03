package ru.jecklandin.duckshot;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Obstacle extends GameObject {

	private Wave mHostingWave;
	private int mWidth;
	
	private Stone mStone; 
	
	public Obstacle(Wave hostingWave, int x, int width) {
		this.x = x;
		this.y = hostingWave.y;
		this.mHostingWave = hostingWave;
		this.mWidth = width;
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		p.setColor(Color.BLACK);
		c.drawRect(x, mHostingWave.y, x+mWidth, mHostingWave.y+40, p);
		
		if (mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				mStone.makeFountain = false;
				mStone.fallen = true;
				mStone.bounce();
			}
		}
	}

	@Override
	public float getNextOffset(float curOffset) {
		return 0;
	}

	@Override
	public OBJ_TYPE getRtti() {
		return null;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getX() {
		return x;
	}

	boolean isIntersects(int ix) {
		return (ix >= this.x && ix <= this.x+mWidth);
	}
	
	public void notifyStoneWasThrown(Stone stone) {
		mStone = stone;
	}
	
}
