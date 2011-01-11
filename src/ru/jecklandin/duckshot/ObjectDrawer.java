package ru.jecklandin.duckshot;

import java.util.ArrayList;

import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class ObjectDrawer {
	private static final String TAG = "ObjectDrawer";
	private static ObjectDrawer s_instance;

	public static boolean lock = false;
	
	private Paint mPaint;
	private DuckShotModel model = DuckShotModel.getInstance();
	private ArrayList<CreatureObject> mMovingCreatures  = new ArrayList<CreatureObject>();

	private ObjectDrawer(Context ctx) {
		mPaint = new Paint();
		mPaint.setTypeface(DuckApplication.getCommonTypeface());
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

	public void drawGroundObject(Canvas c, GroundObject w) {

		for (CreatureObject d : w.mCreatures) {
			d.draw(c, mPaint);
			if (d.mMoveFlag) {
				mMovingCreatures.add(d);
			}
		}
		
		for (Obstacle obs : w.mObstacles) {
			obs.draw(c, mPaint);
		}

		w.draw(c, mPaint);
	}

	public boolean drawObjects(Canvas c) {
		drawEnvironment(c);
		
		mMovingCreatures.clear();
		
		synchronized (DuckShotModel.getInstance()) {
			for (int i = 0; i < model.mWaves.size(); ++i) {
				GroundObject w = model.mWaves.get(i);
				drawGroundObject(c, w);
			}

			// we need this to avoid concurrentexception (modifying waves while
			// iterating)
			for (CreatureObject d : mMovingCreatures) {
				d.move();
			}
			DuckShotModel.getInstance().notifyAll();
		}
		drawDeck(c);
		drawStones(c);
		
		return false;
	}
	
	private void drawEnvironment(Canvas c) {
		DuckApplication.getInstance().getCurrentLevel().mEnvironment.draw(c, mPaint);
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

