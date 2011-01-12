package ru.jecklandin.duckshot.levels;

import java.util.ArrayList;
import ru.jecklandin.duckshot.R;

public class LevelManager {

	private static LevelManager sInstance = new LevelManager();
	
	public static LevelManager getInstance() {
		return sInstance;
	}
	
	private ArrayList<Level> mAvailableLevels = new ArrayList<Level>();
	
	public LevelManager() {
		WaterLevel lev1 = new WaterLevel(1, R.drawable.level_thumb1, "Rocky Lakes");
		mAvailableLevels.add(lev1);
		
		ForestLevel lev2 = new ForestLevel(2, R.drawable.level_thumb2, "Homish Forest");
		mAvailableLevels.add(lev2);
		
		mAvailableLevels.add(new Level(0, R.drawable.level_thumb0, "Unknown"));
		mAvailableLevels.add(new Level(0, R.drawable.level_thumb0, "Unknown"));
		mAvailableLevels.add(new Level(0, R.drawable.level_thumb0, "Unknown"));
	}
	
	public ArrayList<Level> getLevels() {
		return mAvailableLevels;
	}
	
}