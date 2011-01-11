package ru.jecklandin.duckshot.levels;

import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class WaterLevel extends Level {

	public WaterLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		mCreatureVoices = new int[] {R.raw.quack1, R.raw.quack2, R.raw.quack3};
		mPunch = R.raw.punch;
		mStoneHit = R.raw.rock1;
		mPointsToComplete = 1000;
		
		mEnvironment = new WaterEnvironment();
	}

	@Override
	public void loadResources() {
		
		if (mBitmapsLoaded) {
			return;
		}
		
		Bitmap bm = getBitmap(R.drawable.wave);
		mLevelImgMap.put("wave", bm);
		bm = getBitmap(R.drawable.duck);
		mLevelImgMap.put("duck", bm);
		bm = getBitmap(R.drawable.deadduck);
		mLevelImgMap.put("deadduck", bm);
		bm = getBitmap(R.drawable.stonea);
		mLevelImgMap.put("stone", bm);
		bm = getBitmap(R.drawable.clouda);
		mLevelImgMap.put("cloud1", bm);
		bm = getBitmap(R.drawable.cloudb);
		mLevelImgMap.put("cloud2", bm);
//		bm = getBitmap(R.drawable.cloudc);
//		mLevelImgMap.put("cloud3", bm);
		bm = getBitmap(R.drawable.rock_1);
		mLevelImgMap.put("rock1", bm);
		bm = getBitmap(R.drawable.rock_2);
		mLevelImgMap.put("rock2", bm);
		bm = getBitmap(R.drawable.rock_3);
		mLevelImgMap.put("rock3", bm);
		
		bm = getBitmap(R.drawable.anifountain);
		Bitmap[] anim = new Bitmap[8];
		int diff_x = ScrProps.scale(84);
		int diff_y = ScrProps.scale(84);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		bm.getWidth();
		bm.getHeight(); 
		mLevelAniMap.put("fountain", anim);
		bm.recycle();
		   
		bm = getBitmap(R.drawable.duckdive);
		anim = new Bitmap[16]; 
		diff_x = ScrProps.scale(84);
		diff_y = ScrProps.scale(84);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mLevelAniMap.put("duckdive", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.duckemerge);
		anim = new Bitmap[8];
		diff_x = ScrProps.scale(84);
		diff_y = ScrProps.scale(84);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		} 
		mLevelAniMap.put("duckemerge", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.shrapnel);
		anim = new Bitmap[6]; 
		diff_x = ScrProps.scale(100);
		diff_y = ScrProps.scale(100);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mLevelAniMap.put("shrapnel", anim);
		bm.recycle();
		
		super.loadResources();
	}
	
	private static Bitmap getBitmap(int id) {
		return BitmapFactory.decodeResource(DuckApplication.getInstance().getResources(), id);
	}
	
	private static class WaterEnvironment implements Environment {
		
		// Bitmaps
		private static Bitmap mCloud1;
		private static Bitmap mCloud2;
		
		Matrix m = new Matrix();
		float rot_degree = 0;
		float x_offset1 = 0;
		float x_offset2 = 0;
		
		public void draw(Canvas c, Paint p) {
			m.setTranslate(x_offset2, 0 );
			c.drawBitmap(mCloud2, m, p);
			if (x_offset2 > ScrProps.screenWidth * 1.2) {
				x_offset2 = -mCloud2.getWidth();
			} else {
				x_offset2+=0.3;
			}
			
			m.setTranslate(x_offset1, 30);
			c.drawBitmap(mCloud1, m, p);
			if (x_offset1 > ScrProps.screenWidth * 1.1) {
				x_offset1 = -mCloud1.getWidth();
			} else {
				x_offset1+=0.1;
			}
			
			p.setColor(Color.parseColor("#5984c8")); 
			c.drawRect(0, ScrProps.screenHeight-200, ScrProps.screenWidth, ScrProps.screenHeight, p);
		}

		@Override
		public void init() {
			mCloud1 = ImgManager.getBitmap("cloud1");
			mCloud2 = ImgManager.getBitmap("cloud2");
		}
	}
	
}
