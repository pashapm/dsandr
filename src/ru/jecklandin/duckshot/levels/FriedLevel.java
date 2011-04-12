package ru.jecklandin.duckshot.levels;

import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.model.ObstacleManager;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Cupcake;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.MetalBeam;
import ru.jecklandin.duckshot.units.Obstacle.Type;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class FriedLevel extends Level {

	public FriedLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		mCreatureVoices = new int[] {R.raw.cupcake_scream1, R.raw.cupcake_scream2, R.raw.cupcake_scream3};
		mPunch = R.raw.punch;
		mStoneHit = R.raw.metal;
		
		mPointsToComplete = 1000;
		
		setDominatingColor(Color.parseColor("#234a25"));
		setBackgroundColor(Color.parseColor("#226c26"));
		mEnvironment = new FireEnvironment(mBgColor);
		 
		mLevelSettings.putBoolean("fountain", false);
		mLevelSettings.putBoolean("creaturesOnTop", true);
		mLevelSettings.putInt("groundsNumber", 6);
		mLevelSettings.putBoolean("destroyedByGround", true);
		
		mLevelTime = 30;
	}
	
	@Override
	public void loadSounds() {
		SoundManager.getInstance().loadObstacleSounds(new int[] 
		  {R.raw.bell1, R.raw.bell2, R.raw.hiss});
	}
	
	@Override
	public void loadResources() {
		
		Bitmap bm = loadBitmap(R.drawable.metal_beam);
		mLevelImgMap.put("ground", bm);
		bm = loadBitmap(R.drawable.big_fr_pan);
		mLevelImgMap.put("obstacle1", bm);
		bm = loadBitmap(R.drawable.small_fr_pan);
		mLevelImgMap.put("obstacle2", bm);
		bm = loadBitmap(R.drawable.leg);
		mLevelImgMap.put("obstacle3", bm);
		bm = loadBitmap(R.drawable.cupcake_dead);
		mLevelImgMap.put("cupcake_dead", bm);
		bm = loadBitmap(R.drawable.sling_socket); 
		mLevelImgMap.put("socket", bm);
		bm = loadBitmap(R.drawable.stonea);
		mLevelImgMap.put("missile", bm);
		bm = loadBitmap(R.drawable.fire);
		mLevelImgMap.put("bg_pic", bm);
		bm = loadBitmap(R.drawable.hypno_socket);
		mLevelImgMap.put("socket", bm);
		
		makeAnimation(R.drawable.hypno, 4, ScrProps.scale(30), ScrProps.scale(30), "missile");
		makeAnimation(R.drawable.cupcake, 8, ScrProps.scale(50), ScrProps.scale(50), "cupcake");
		makeAnimation(R.drawable.anifountain, 8, ScrProps.scale(84), ScrProps.scale(84), "fountain");
		makeAnimation(R.drawable.shrapnel, 6, ScrProps.scale(100), ScrProps.scale(100), "shrapnel");
	
		super.loadResources();
	};
	
	@Override
	public Bitmap[] getObstacleBitmaps() {
		return new Bitmap[] {
				ImgManager.getBitmap("obstacle2"),
				ImgManager.getBitmap("obstacle1"),
				ImgManager.getBitmap("obstacle3")
		};
	}
	
	public void setObstacles() {
		for (GroundObject go : DuckShotModel.getInstance().mGrounds) {
			go.mObstacles.clear();
		}
		ObstacleManager.getInstance().addObstacle(Type.TYPE2, ScrProps.scale(10), 1, DuckShotModel.GROUNDS_GAP+ScrProps.scale(5));
		ObstacleManager.getInstance().addObstacle(Type.TYPE2, ScrProps.scale(100), 2, DuckShotModel.GROUNDS_GAP+ScrProps.scale(5));
		ObstacleManager.getInstance().addObstacle(Type.TYPE1, ScrProps.scale(220), 3, ScrProps.scale(10));
		ObstacleManager.getInstance().addObstacle(Type.TYPE3, ScrProps.scale(210), 5, ScrProps.scale(15))
			.setBounce(false);
	}
	
	@Override
	public void initItemsBitmaps() {
		super.initItemsBitmaps();
		Cupcake.initBitmaps();
	}
	
	@Override
	public GroundObject createGroundObject(int x, int y, float speed, int wave_num) {
		return new MetalBeam(y, wave_num);
	}
	
	public CreatureObject createCreatureObject(int x) {
		return new Cupcake(x);
	}
	
	public static class FireEnvironment extends Environment {
		
		protected Matrix mMatrix = new Matrix();
		protected Bitmap mBgPic;
		
		FireEnvironment(int bgColor) {
	    	super(bgColor);
	    }
	    
		public void draw(Canvas c, Paint p) {
			c.drawColor(Color.parseColor("#fbd9a8"));
			mMatrix.reset();
			mMatrix.setTranslate(0, ScrProps.screenHeight 
					- mBgPic.getHeight());
			c.drawBitmap(mBgPic, mMatrix, p);
		}

		@Override
		public void init() {
			mBgPic = ImgManager.getBitmap("bg_pic");
		}
	}

}
