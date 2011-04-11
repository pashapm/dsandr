package ru.jecklandin.duckshot.levels;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.levels.WaterLevel.WaterEnvironment;
import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.model.ObstacleManager;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Duck;
import ru.jecklandin.duckshot.units.Grass;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Hedgehog;
import ru.jecklandin.duckshot.units.Grass.GRASS_TYPE;
import ru.jecklandin.duckshot.units.Obstacle.Type;

public class ForestLevel extends Level {

	public ForestLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		mCreatureVoices = new int[] {R.raw.hh_1, R.raw.hh_2, R.raw.hh_3};
		mPunch = R.raw.punch;
		mStoneHit = R.raw.apple;
		
		mPointsToComplete = 2500;
		
		setDominatingColor(Color.parseColor("#234a25"));
		setBackgroundColor(Color.parseColor("#226c26"));
		mEnvironment = new ForestEnvironment(mBgColor);
		 
		mLevelSettings.putBoolean("fountain", true);
	}
 
	@Override
	public void loadSounds() {
		SoundManager.getInstance().loadObstacleSounds(new int[] 
		  {R.raw.apple, R.raw.apple, R.raw.apple});
	}
	
	@Override
	public void loadResources() {
		
		Bitmap bm = loadBitmap(R.drawable.grass);
		mLevelImgMap.put("ground", bm);
		bm = loadBitmap(R.drawable.hh);
		mLevelImgMap.put("creature", bm);
		bm = loadBitmap(R.drawable.grass_dark);
		mLevelImgMap.put("grass_dark", bm);
		bm = loadBitmap(R.drawable.grass_light);
		mLevelImgMap.put("grass_light", bm);
		bm = loadBitmap(R.drawable.tree_1);
		mLevelImgMap.put("obstacle1", bm);
		bm = loadBitmap(R.drawable.tree_2);
		mLevelImgMap.put("obstacle2", bm);
		bm = loadBitmap(R.drawable.apple_socket);
		mLevelImgMap.put("socket", bm);
		bm = loadBitmap(R.drawable.apple);
		mLevelImgMap.put("missile", bm);
		bm = loadBitmap(R.drawable.hh_spirit);
		mLevelImgMap.put("spirit", bm);
		
		makeAnimation(R.drawable.apple_shrapnel, 5, ScrProps.scale(100), ScrProps.scale(100), "shrapnel");
		makeAnimation(R.drawable.apple_fountain, 5, ScrProps.scale(100), ScrProps.scale(50), "fountain");
		makeAnimation(R.drawable.hh_hide, 8, ScrProps.scale(75), ScrProps.scale(75), "hide");
		makeAnimation(R.drawable.hh_appear, 8, ScrProps.scale(75), ScrProps.scale(75), "appear");
		
		super.loadResources();
	}
	
	@Override
	public void initItemsBitmaps() {
		super.initItemsBitmaps();
		Hedgehog.initBitmaps();
		assignColorsToGrass(); 
	}
	
	@Override
	public GroundObject createGroundObject(int x, int y, float speed, int wave_num) {
		return new Grass(x, y, speed, wave_num);
	}
	
	public CreatureObject createCreatureObject(int x) {
		return new Hedgehog(x);
	}
	
	private void assignColorsToGrass() {
		ArrayList<GroundObject> grs = DuckShotModel.getInstance().mGrounds;
		for (int i=0; i<grs.size(); ++i) {
			Grass grass = (Grass) grs.get(i); 
			grass.setBitmap(i%2 != 0 ? GRASS_TYPE.AVERAGE : GRASS_TYPE.DARK);
			if (i==0 || i==6){
				grass.setBitmap(GRASS_TYPE.LIGHT);
			}
		}
	}
	
	@Override
	public Bitmap[] getObstacleBitmaps() {
		return new Bitmap[] {
				ImgManager.getBitmap("obstacle2"),
				ImgManager.getBitmap("obstacle1"),
				ImgManager.getBitmap("obstacle2")
		};
	}
	 
	public void setObstacles() {
		for (GroundObject go : DuckShotModel.getInstance().mGrounds) {
			go.mObstacles.clear();
		}
		ObstacleManager.getInstance().addObstacle(Type.TYPE1, ScrProps.scale(10), 4);
		ObstacleManager.getInstance().addObstacle(Type.TYPE2, ScrProps.scale(160), 0);
		ObstacleManager.getInstance().addObstacle(Type.TYPE2, ScrProps.scale(220), 1);
		ObstacleManager.getInstance().addObstacle(Type.TYPE3, ScrProps.scale(200), 7);
	}
	
	public static class ForestEnvironment extends Environment {
		
	    ForestEnvironment(int bgColor) {
	    	super(bgColor);
	    }
	    
		public void draw(Canvas c, Paint p) {
			c.drawColor(Color.parseColor("#234a25"));
			
			p.setColor(mBgColor); 
			c.drawRect(0, ScrProps.screenHeight-200, ScrProps.screenWidth, ScrProps.screenHeight, p);
		}

		@Override
		public void init() {
		}
	}

}
