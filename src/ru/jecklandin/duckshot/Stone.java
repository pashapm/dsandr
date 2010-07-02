package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class Stone extends GameObject {

	private static final String TAG = "Stone";
	
	public static Bitmap mStone;
	public static Bitmap mFountain;
	public static Bitmap[] mAniFountain;
	
	static {
		Stone.mStone = ImgManager.getBitmap("stone");
		Stone.mFountain = ImgManager.getBitmap("fountain");
		Stone.mAniFountain = ImgManager.getAnimation("fountain");
	}
	
	public boolean makeFountain = true;
	private int rot_degree = 0;
	private boolean fallen = false;
	private boolean vibrated = false;
	private int y_dest;
	private int anim_frame = 0;
	public boolean sank = false;
	
	
	public SpeedVector mVector;
	
	public Stone(int x, int y_dest) {
		super();
		this.x = x;
		this.y = ScreenProps.screenHeight;
		this.y_dest = y_dest;
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
				matrix.setTranslate(x, y);
				drawFountain(c, p);
			}
	} else {
			getNextOffset(offset);
			matrix.setTranslate(x, y);
			matrix.postRotate(rot_degree, x, y);
			c.drawBitmap(mStone, matrix, p);
		}
		
	}  

	private void drawFountain(Canvas c, Paint p) {
		matrix.postTranslate(- mFountain.getWidth()/2, - mFountain.getHeight()*6/10);
		if (anim_frame < 8) {
			c.drawBitmap(mAniFountain[anim_frame], matrix, p); 
			anim_frame++;
		} else if (anim_frame > 8) { 
			Log.d(TAG, "sank");
			sank = true;
		} else {
			anim_frame++;
		}
				
		
	}

	@Override
	public float getNextOffset(float curOffset) {
		rot_degree+=3;
    	speed += 1;
		 
		this.y-=(9+speed);
		if (y <= y_dest) {   
			fallen = true;
		}
		return y;
	}

	@Override
	public OBJ_TYPE getRtti() {
		return OBJ_TYPE.STONE;
	}


}
