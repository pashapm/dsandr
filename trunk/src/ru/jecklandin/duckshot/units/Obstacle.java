package ru.jecklandin.duckshot.units;

import java.util.List;

import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.Stone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class Obstacle extends GameObject {

	private Wave mHostingWave;
	private int mWidth;
	
	private Stone mStone; 
	
	static {
		Obstacle.rock1 = ImgManager.getBitmap("rock1");
		Obstacle.rock2 = ImgManager.getBitmap("rock2");
		Obstacle.rock3 = ImgManager.getBitmap("rock3");
	}
	
	public enum Type {ROCK1, ROCK2, ROCK3, STUB};
	
	private static Bitmap rock1;
	private static Bitmap rock2;
	private static Bitmap rock3;
	
	private Matrix mMatrix = new Matrix();
	private Bitmap mCurrentBitmap;
	
	private int h_offset;
	
	public Obstacle(Wave hostingWave, int x, Type type) {
		this.x = x;
		this.y = hostingWave.y;
		this.mHostingWave = hostingWave;
		
		switch (type) {
		case ROCK1:
			mCurrentBitmap = rock1;
			break;
		case ROCK2:
			mCurrentBitmap = rock2;
			break;
		case ROCK3:
			mCurrentBitmap = rock3;
			break;
		default:
			break;
		}
		
		this.mWidth = mCurrentBitmap.getWidth();  
		h_offset = mCurrentBitmap.getHeight() - Wave.mGroundBitmap.getHeight();
	}
	
	/**
	 * Stub (transparent) obstacle
	 */
	public Obstacle(Obstacle mainObs, Wave hostingWave) {
		this.x = mainObs.x;
		this.mHostingWave = hostingWave;
		this.y = hostingWave.y;
		this.mWidth = mainObs.mWidth;
		mCurrentBitmap = Bitmap.createBitmap(mWidth, 1, Config.ALPHA_8);
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		p.setColor(Color.BLACK);
		
		mMatrix.reset();
		mMatrix.setTranslate(x, mHostingWave.y - h_offset); 
		c.drawBitmap(mCurrentBitmap, mMatrix, p);
		
		if (mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				mStone.makeFountain = false;
				mStone.fallen = true; 
				mStone.bounce();
				mStone = null;
				
				SoundManager.getInstance().playRockHit(); 
			}
		}
	}

	@Override
	public float getNextOffset(float curOffset) {
		return 0;
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
