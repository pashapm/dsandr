package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.Stone;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;

public class Obstacle extends GameObject {

	protected GroundObject mHostingGround;
	protected int mWidth;
	protected Stone mStone; 
	
	public enum Type {TYPE1, TYPE2, TYPE3, STUB, TYPE4};
	
	protected Type mType;
	
	protected static Bitmap mPic1;
	protected static Bitmap mPic2;
	protected static Bitmap mPic3;
	
	private Matrix mMatrix = new Matrix();
	private Bitmap mCurrentBitmap;
	
	private int h_offset;
	
	private boolean mBounce = true;
	
	public Obstacle(GroundObject hostingGround, int x, Type type, int h_offset) {
		this.x = x;
		this.y = hostingGround.y;
		this.mHostingGround = hostingGround;
		this.mType = type;
		
		switch (mType) {
		case TYPE1:
			mCurrentBitmap = mPic1;
			break;
		case TYPE2:
			mCurrentBitmap = mPic2;
			break;
		case TYPE3:
			mCurrentBitmap = mPic3;
			break;
		default:
			break;
		}
		
		this.mWidth = mCurrentBitmap.getWidth();  
		this.h_offset = h_offset == -1 ? 
				mCurrentBitmap.getHeight() - Wave.mGroundBitmap.getHeight()
				: h_offset;
	}
	
	public Obstacle(GroundObject hostingGround, int x, Type type) {
		this(hostingGround, x, type, -1);
	}
	
	/**
	 * Stub (transparent) obstacle
	 */
	public Obstacle(Obstacle mainObs, GroundObject hostingGround) {
		this.x = mainObs.x;
		this.mHostingGround = hostingGround;
		this.y = hostingGround.y;
		this.mWidth = mainObs.mWidth;
		this.mType = mainObs.mType;
		mCurrentBitmap = Bitmap.createBitmap(mWidth, 1, Config.ALPHA_8);
	}
	
	/**
	 * Stub obstacle
	 * @param hostingGround
	 * @param obstacleOffset
	 * @param width
	 */
	public Obstacle(GroundObject hostingGround, int x, int width, Obstacle.Type parentType) {
		this.mHostingGround = hostingGround;
		this.x = x;
		this.y = hostingGround.y;
		this.mWidth = width;
		this.mType = parentType;
		mCurrentBitmap = Bitmap.createBitmap(mWidth, 1, Config.ALPHA_8);
	}
	
	public static void initBitmaps() {
		Bitmap[] bms = DuckApplication.getInstance().getCurrentLevel().getObstacleBitmaps();
		mPic1 = bms[0];
		mPic2 = bms[1];
		mPic3 = bms[2];
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		p.setColor(Color.BLACK);
		
		mMatrix.reset();
		mMatrix.setTranslate(x, mHostingGround.y - h_offset); 
		c.drawBitmap(mCurrentBitmap, mMatrix, p);
		
		if (mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				mStone.makeFountain = false;
				mStone.fallen = true; 
				if (mBounce) {
					mStone.bounce();
				}
				mStone = null;
				SoundManager.getInstance().playObstacleSound(mType);
			}
		}
	}

	public void playSound() {
		
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
	
	public void setBounce(boolean bounce) {
		mBounce = bounce;
	}
}
