package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.units.GameObject;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Stone extends GameObject {

	private static final String TAG = "Stone";
	
	public static Bitmap mStone;
	public static Bitmap mFountain;
	public static Bitmap[] mAniFountain;
	public static Bitmap[] mShrapnel;
	
	static {
		Stone.mStone = ImgManager.getBitmap("stone");
		Stone.mFountain = ImgManager.getBitmap("fountain");
		Stone.mAniFountain = ImgManager.getAnimation("fountain");
		Stone.mShrapnel = ImgManager.getAnimation("shrapnel");
	}
	
	public boolean makeFountain = true;
	private int rot_degree = 0;
	public boolean fallen = false;
	private boolean vibrated = false;
	private float y_dest;
	private float x_dest;
	private int anim_frame = 0;
	public boolean sank = false;
	
	private static int SPEED_Y = 20;
	
	public SpeedVector mVector;
	SpeedVector mDeltaVector;

	private boolean mBounce = false;
	
	public Stone(int x_dest, int y_dest) {
		super();
		this.x = SlingView.SOCKET_DEFAULT_X;
		this.y = SlingView.SOCKET_DEFAULT_Y;
		this.y_dest = y_dest;
		this.x_dest = x_dest;
		
		mVector = new SpeedVector(this.x, this.y);
		
		float ratio = (this.y - this.y_dest) / (x_dest - SlingView.SOCKET_DEFAULT_X);
		mDeltaVector = new SpeedVector(SPEED_Y/ratio, -SPEED_Y);
	}
	
	@Override
	public void draw(Canvas c, Paint p) { 
		matrix.reset();
		if (fallen) {
			if (!vibrated) {
				DuckGame.s_instance.mVibro.vibrate(20);
				vibrated = true;
			}

			if (makeFountain) { 
				matrix.setTranslate(mVector.x, mVector.y);
				drawFountain(c, p);
			} else if (mBounce) {
				matrix.setTranslate(mVector.x, mVector.y);
				drawShrapnel(c, p);
			}
	} else {
			getNextOffset(offset);
			matrix.setTranslate(mVector.x, mVector.y);
			matrix.postRotate(rot_degree, mVector.x, mVector.y);
			c.drawBitmap(mStone, matrix, p);
		}
		
	}  

	private void drawFountain(Canvas c, Paint p) {
		matrix.postTranslate(- mAniFountain[0].getWidth()/2, - mAniFountain[0].getHeight()*6/10);
		if (anim_frame < mAniFountain.length) {
			c.drawBitmap(mAniFountain[anim_frame], matrix, p); 
			anim_frame++;
		} else if (anim_frame > mAniFountain.length) { 
			sank = true;
		} else {
			anim_frame++; 
		} 
	}
	
	private void drawShrapnel(Canvas c, Paint p) {
		matrix.postTranslate(- mAniFountain[0].getWidth()/2, - mAniFountain[0].getHeight()*6/10);
		if (anim_frame < mShrapnel.length) {
			c.drawBitmap(mShrapnel[anim_frame], matrix, p); 
			anim_frame++;
		} else if (anim_frame > mAniFountain.length) { 
			sank = true;
		} else {
			anim_frame++;
		}
	}
	

	@Override
	public float getNextOffset(float curOffset) {
		mVector.add(mDeltaVector);
		rot_degree+=3;
		
		if (this.y_dest >= mVector.y) { 
			fallen = true;
		}
		
		return y;
	}

	public boolean isFallen() {
		return fallen;
	}
	
	public void bounce() {
		mBounce = true;
		fallen = true;
	}
	
}
