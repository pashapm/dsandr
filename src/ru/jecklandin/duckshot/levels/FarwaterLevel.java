package ru.jecklandin.duckshot.levels;

import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.model.ObstacleManager;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Duck;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle.Type;
import android.graphics.Bitmap;

public class FarwaterLevel extends WaterLevel {

	public FarwaterLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		mStoneHit = R.raw.metal;
	}

	@Override
	public void loadResources() {
		super.loadResources();
		
		mLevelImgMap.get("obstacle1").recycle();
		Bitmap bm = loadBitmap(R.drawable.submarine);
		mLevelImgMap.put("obstacle1", bm);
		bm = loadBitmap(R.drawable.duck_armored);
		mLevelImgMap.put("duck_armored", bm);
	}
	
	@Override
	public Bitmap[] getObstacleBitmaps() {
		return new Bitmap[] {
				ImgManager.getBitmap("obstacle1"),
				ImgManager.getBitmap("obstacle1"),
				ImgManager.getBitmap("obstacle1")
		};
	}
	
	public CreatureObject createCreatureObject(int x) {
		Duck duck = (Duck) super.createCreatureObject(x);
		if (Math.random() > .5d) {
			duck.setArmored(true);
		}
		return duck;
	}
	
	@Override
	public void setObstacles() {
		for (GroundObject go : DuckShotModel.getInstance().mGrounds) {
			go.mObstacles.clear();
		}
		ObstacleManager.getInstance().addObstacle(Type.TYPE1, ScrProps.scale(20), 4);
	}
	
}
