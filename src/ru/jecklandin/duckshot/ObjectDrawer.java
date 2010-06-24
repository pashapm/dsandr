package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.GameObject.OBJ_TYPE;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class ObjectDrawer {
	private static final String TAG = "ObjectDrawer";
	private static ObjectDrawer s_instance;

	
	private Context mCtx;
	private Paint mPaint;
	private DuckShotModel model = DuckShotModel.getInstance();
	private Environment mEnvir = new Environment();

	private ObjectDrawer(Context ctx) {
		mCtx = ctx;
		mPaint = new Paint();
		mPaint.setTypeface(DuckGame.s_instance.mTypeface);
		mPaint.setTextSize(15);
		mPaint.setColor(Color.YELLOW);
	}
	
	public static long sLastTick;
	public static boolean isLate() {
		return (System.currentTimeMillis() - ObjectDrawer.sLastTick) > 50;
	}
	
	public static synchronized ObjectDrawer getInstance(Context ctx) {
		if (ObjectDrawer.s_instance == null) {
			ObjectDrawer.s_instance = new ObjectDrawer(ctx);
		}
		return s_instance;
	}

	public void drawWave(Canvas c, Wave w) {
		for (Duck d : w.ducks) {
			d.draw(c, mPaint);
		}
		w.draw(c, mPaint);
	}

	public boolean drawObjects(Canvas c) {
		drawEnvironment(c);
		//draw waves and ducks
		for (int i = 0; i < model.mWaves.size(); ++i) {
			Wave w = model.mWaves.get(i);
			drawWave(c, w);
		} 
		drawDeck(c);
		drawStones(c);
		
		//draw ad
		//c.drawRect(0, 0, 320, 50, mPaint);
		//drawLines(c);
		
		DuckShotModel.getInstance().cleanup();
		return false;
	}
	
	private void drawEnvironment(Canvas c) {
		mEnvir.draw(c, mPaint);
	}

	private void drawStones(Canvas c) {
		synchronized (DuckShotModel.getInstance().mStones) {
			for (Stone stone: DuckShotModel.getInstance().mStones) {
				stone.draw(c, mPaint);
			}
		}
		
		
	}

	public void drawDeck(Canvas c) {
		Desk.getInstance().draw(c, mPaint);
	}
	
	public void drawLines(Canvas c) {
		for (int i:DuckShotModel.getInstance().mYes) {
			c.drawLine(0, i, 300, i, mPaint);
		}
	}
}

class Environment {
	
	// Bitmaps
	private static Bitmap mSun;
	private static Bitmap mCloud1;
	private static Bitmap mCloud2;
	private static Bitmap mCloud3;
	
	static {
		mSun = ImgManager.getBitmap("sun");
		mCloud1 = ImgManager.getBitmap("cloud1");
		mCloud2 = ImgManager.getBitmap("cloud2");
		mCloud3 = ImgManager.getBitmap("cloud3");
	}
	
	Matrix m = new Matrix();
	float rot_degree = 0;
	float x_offset1 = 0;
	float x_offset2 = 0;
	
	public void draw(Canvas c, Paint p) {
//		m.setRotate(rot_degree, mSun.getWidth()/2, mSun.getHeight()/2);
//		m.postTranslate(-mSun.getWidth()/2, -mSun.getHeight()/2);
//		c.drawBitmap(mSun, m, p);
//		rot_degree+=0.1;
		
		m.setTranslate(x_offset2, 60);
		c.drawBitmap(mCloud2, m, p);
		if (x_offset2 > ScreenProps.screenWidth * 1.2) {
			x_offset2 = -mCloud2.getWidth();
		} else {
			x_offset2+=0.3;
		}
		
		m.setTranslate(x_offset1, 55);
		c.drawBitmap(mCloud1, m, p);
		if (x_offset1 > ScreenProps.screenWidth * 1.1) {
			x_offset1 = -mCloud1.getWidth();
		} else {
			x_offset1+=0.1;
		}
		
		p.setColor(Color.parseColor("#457bd5")); 
		c.drawRect(0, ScreenProps.screenHeight-50, ScreenProps.screenWidth, ScreenProps.screenHeight, p);
		
	}
}
