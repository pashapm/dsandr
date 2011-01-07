package ru.jecklandin.duckshot.levels;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.graphics.Bitmap;

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
	
	public Level(int level_id, int thumb, String name) {
		mThumb = thumb;
		mName = name;
		mLevelId = level_id;
	}
	
	public boolean isLoaded() {
		return mBitmapsLoaded;
	}
	
	public void loadResources() {
		mBitmapsLoaded = true;
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
}
