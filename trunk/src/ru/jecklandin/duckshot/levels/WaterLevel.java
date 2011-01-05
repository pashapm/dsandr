package ru.jecklandin.duckshot.levels;

import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WaterLevel extends Level {

	public WaterLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		mCreatureVoices = new int[] {R.raw.quack1, R.raw.quack2, R.raw.quack3};
		mPunch = R.raw.punch;
		mStoneHit = R.raw.rock1;
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
		bm = getBitmap(R.drawable.cloudc);
		mLevelImgMap.put("cloud3", bm);
		
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
	
}
