package ru.jecklandin.duckshot.levels;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

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
	
	public Environment mEnvironment;
	
	public Level(int level_id, int thumb, String name) {
		mThumb = thumb;
		mName = name;
		mLevelId = level_id;
	}
	
	public boolean isLoaded() {
		return mBitmapsLoaded;
	}
	
	public void loadResources() {
		mEnvironment.init();
		mBitmapsLoaded = true;
		
		GroundObject.initBitmap();
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
		return "Level "+mLevelId+": "+mName;
	}
	
	public Bitmap[] getObstacleBitmaps() {
		return null;
	}
	
	protected static Bitmap loadBitmap(int id) {
		return BitmapFactory.decodeResource(DuckApplication.getInstance().getResources(), id);
	}
	
	//      FACTORY METHODS
	
	public GroundObject createGroundObject(int x, int y, float speed, int wave_num) {
		return null;
	}
	
	public CreatureObject createCreatureObject(int x) {
		return null;
	}
}
