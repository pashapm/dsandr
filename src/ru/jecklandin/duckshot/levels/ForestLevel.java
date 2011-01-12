package ru.jecklandin.duckshot.levels;

import android.graphics.Bitmap;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.levels.WaterLevel.WaterEnvironment;

public class ForestLevel extends WaterLevel {

	public ForestLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
	}

	@Override
	public void loadResources() {
		super.loadResources();
		
		Bitmap bm = loadBitmap(R.drawable.grass);
		mLevelImgMap.put("ground", bm);
	}

}
