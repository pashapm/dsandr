package ru.jecklandin.duckshot.levels;

import java.util.ArrayList;
import java.util.ListIterator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.R;

public class LevelManager {

	private static LevelManager sInstance = new LevelManager();
	
	public static LevelManager getInstance() {
		return sInstance;
	}
	
	private ArrayList<Level> mLevels = new ArrayList<Level>();
	
	public LevelManager() {
		WaterLevel lev1 = new WaterLevel(1, R.drawable.level_thumb1, "Rocky Lakes");
		mLevels.add(lev1);
		
		FarwaterLevel lev2 = new FarwaterLevel(2, R.drawable.level_thumb3, "Fairway");
		mLevels.add(lev2);
		
		ForestLevel lev3 = new ForestLevel(3, R.drawable.level_thumb2, "Apple Madness");
		mLevels.add(lev3);
		
		FriedLevel lev4 = new FriedLevel(4, R.drawable.level_thumb4, "Overdone Cupcakes");
		mLevels.add(lev4);
		
//		mLevels.add(new Level(0, R.drawable.level_thumb0, "<N/A>"));
		
		SharedPreferences prefs = DuckApplication.getInstance()
			.getSharedPreferences("duckshot", Activity.MODE_PRIVATE);
		if (! prefs.contains("lastLevel")) {
			Editor ed = prefs.edit();
			ed.putInt("lastLevel", 1);
			ed.commit();
		}
	}
	
	public ArrayList<Level> getLevels() {
		return mLevels; 
	}
	
	public ArrayList<Level> getAvailableLevels() {
		ArrayList<Level> avl = new ArrayList<Level>();
		ListIterator<Level> it = mLevels.listIterator();
		while (it.hasNext()) {
			Level lvl = it.next();
			if (isUnlocked(lvl.mLevelId)) {
				avl.add(lvl);
			}
		}
 		return avl;
	}
	
	public boolean loadNextLevel() {
		
		if (! isNextLevelAvailable()) {
			return false;
		}
		
		Level next = getNextLevel();
		DuckApplication.getInstance().setLevel(next);
		return true; 
	}
	
	public boolean isNextLevelAvailable() {
		Level next = getNextLevel();
		return next != null && next.mLevelId != 0;
	}
	
	public boolean isAvailable(int lvl_id) {
		return lvl_id != 0;
	}
	
	public boolean isNextLevelUnlocked() {
		
		if (! isNextLevelAvailable()) {
			return false;
		}
		
		Level next = getNextLevel();
		return isUnlocked(next.mLevelId);
	}
	
	private Level getNextLevel() {
		Level cur = DuckApplication.getInstance().getCurrentLevel();
		int indx = mLevels.indexOf(cur);
		if (indx != mLevels.size()-1) {
			return mLevels.get(indx+1);
		} else {
			return null;
		}
	}
	
	public void unlockNextLevel() {
		
		if (! isNextLevelAvailable()) {
			return;
		}
		
		Level next = getNextLevel();
		SharedPreferences prefs = DuckApplication.getInstance()
			.getSharedPreferences("duckshot", Activity.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putInt("lastLevel", next.mLevelId);
		ed.commit();
		
		Log.d("GO SLING", "Level "+next.mLevelId+" is unlocked.");
	}
	
	public boolean isUnlocked(int lvl_id) {
		
		if (lvl_id == 0) {   
			return false;
		}  
		
		SharedPreferences prefs = DuckApplication.getInstance()
			.getSharedPreferences("duckshot", Activity.MODE_PRIVATE);
		int lastLevel = prefs.getInt("lastLevel", 1);
	   
		return lastLevel >= lvl_id;
	}
	
}
