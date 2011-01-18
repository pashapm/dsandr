package ru.jecklandin.duckshot.levels;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SlingView;
import ru.jecklandin.duckshot.Stone;
import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.model.ObstacleManager;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;
import ru.jecklandin.duckshot.units.Obstacle.Type;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;

public class Level {
	
	public int mThumb;
	public String mName;
	public int mLevelId;
	public int mPointsToComplete;
	
	protected boolean mBitmapsLoaded = false;
	
	protected static Map<String, Bitmap> mLevelImgMap = new HashMap<String, Bitmap>();
	protected static Map<String, Bitmap[]> mLevelAniMap = new HashMap<String, Bitmap[]>();
	
	//sounds
	public int[] mCreatureVoices;
	public int mPunch;
	public int mStoneHit;
	
	public int mDominatingColor;
	public int mBgColor;
	
	protected Bundle mLevelSettings = new Bundle();
	
	public Environment mEnvironment;
	
	public Level(int level_id, int thumb, String name) {
		mThumb = thumb;
		mName = name;
		mLevelId = level_id;
		setDominatingColor(Color.parseColor("#845a21"));
	}
	
	public void setDominatingColor(int dominatingColor) {
		mDominatingColor = dominatingColor;
	}
	
	public void setBackgroundColor(int bgColor) {
		mBgColor = bgColor;
	}
	
	public boolean isLoaded() {
		return mBitmapsLoaded;
	}
	
	public void loadResources() {
		mEnvironment.init();
		mBitmapsLoaded = true;
		
		GroundObject.initBitmaps();
	}
	
	public void unloadResources() {
		for (String res : mLevelAniMap.keySet()) {
			for (Bitmap b : mLevelAniMap.get(res)) {
				b.recycle();
			}
		}
		
		for (String res : mLevelImgMap.keySet()) {
			mLevelImgMap.get(res).recycle();
		}
		
		mLevelImgMap.clear();
		mLevelAniMap.clear();
		
		mBitmapsLoaded = false;
	}
	
	public Bitmap getBitmap(String name) {
		return mLevelImgMap.get(name);
	}
	
	public Bitmap[] getAnimation(String name) {
		return mLevelAniMap.get(name);
	}
	
	@Override
	public String toString() {
		return mLevelId == 0 ? "<N/A>" : ("Level "+mLevelId+": "+mName);
	}
	
	public Bitmap[] getObstacleBitmaps() {
		return null;
	}
	
	protected static Bitmap loadBitmap(int id) {
		return BitmapFactory.decodeResource(DuckApplication.getInstance().getResources(), id);
	}
	
	public Bundle getSettings() {
		return mLevelSettings;
	}
	
	//      FACTORY METHODS
	public GroundObject createGroundObject(int x, int y, float speed, int wave_num) {
		return null;
	}
	
	public CreatureObject createCreatureObject(int x) {
		return null;
	}

	public void initItemsBitmaps() {
		Obstacle.initBitmaps();
		GroundObject.initBitmaps();
		SlingView.initBitmaps(); 
		Stone.initBitmaps();
	}

	public void setObstacles() {
	}
	
	protected Bitmap[] makeAnimation(int source, int frames_num, int dx, int dy, String storeAs) {
		Bitmap bm = loadBitmap(source);
		Bitmap[] anim = new Bitmap[frames_num]; 
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*dx, 0, dx, dy);
		}  
		mLevelAniMap.put(storeAs, anim);
		bm.recycle();
		return anim;
	}
}
