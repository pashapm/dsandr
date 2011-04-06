package ru.jecklandin.duckshot.levels;

import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FriedLevel extends Level {

	public FriedLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		mCreatureVoices = new int[] {R.raw.hh_1, R.raw.hh_2, R.raw.hh_3};
		mPunch = R.raw.punch;
		mStoneHit = R.raw.apple;
		
		mPointsToComplete = 3500;
		
		setDominatingColor(Color.parseColor("#234a25"));
		setBackgroundColor(Color.parseColor("#226c26"));
		mEnvironment = new FireEnvironment(mBgColor);
		 
		mLevelSettings.putBoolean("fountain", true);
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
		bm = loadBitmap(R.drawable.apple_socket);
		mLevelImgMap.put("socket", bm);
		bm = loadBitmap(R.drawable.apple);
		mLevelImgMap.put("missile", bm);
		
		makeAnimation(R.drawable.cupcake, 8, ScrProps.scale(50), ScrProps.scale(50), "cupcake");
	};
	
	public static class FireEnvironment extends Environment {
		
		FireEnvironment(int bgColor) {
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
