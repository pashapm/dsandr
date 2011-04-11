package ru.jecklandin.duckshot;

import java.util.HashMap;
import java.util.Map;

import ru.jecklandin.duckshot.levels.Level;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImgManager {

	private static Context mCtx;
	
	public static Map<String, Bitmap> mCommonImgMap = new HashMap<String, Bitmap>();
	private static Map<String, Bitmap[]> mCommonAniMap = new HashMap<String, Bitmap[]>();
	  
	private static boolean mCommonBitmapsLoaded = false;
	
	private static void loadCommonBitmaps() {
		Bitmap bm = getBitmap(R.drawable.desk);
		mCommonImgMap.put("desk", bm);
		bm = getBitmap(R.drawable.sight);
		mCommonImgMap.put("sight", bm);
		bm = getBitmap(R.drawable.settings_quant);
		mCommonImgMap.put("quant", bm);
		bm = getBitmap(R.drawable.settings_quant_e);
		mCommonImgMap.put("quant_e", bm);
		
		
		bm = getBitmap(R.drawable.digits);
		Bitmap[] anim = new Bitmap[11];
		int diff_x = ScrProps.scale(30);
		int diff_y = ScrProps.scale(30);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mCommonAniMap.put("digits", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.awards);
		anim = new Bitmap[5]; 
		diff_x = ScrProps.scale(35);
		diff_y = ScrProps.scale(35);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mCommonAniMap.put("awards", anim);
		bm.recycle();
	}
	
	public static void loadImages(Context ctx) {
		if (mCommonBitmapsLoaded) {
			return;
		}
		mCtx = ctx;  
		loadCommonBitmaps();
		mCommonBitmapsLoaded = true;
	}

	private static Bitmap getBitmap(int id) {
		return BitmapFactory.decodeResource(mCtx.getResources(), id);
	}
	
	public static Bitmap getBitmap(String name) {
		return mCommonImgMap.containsKey(name) 
		? mCommonImgMap.get(name) : 
		  DuckApplication.getInstance().getCurrentLevel().getBitmap(name);
	}
	
	public static Bitmap[] getAnimation(String name) {
			if (mCommonAniMap.containsKey(name)) {
				return mCommonAniMap.get(name);
			} else {
				Level l = DuckApplication.getInstance().getCurrentLevel();
 				return l.getAnimation(name);
			}
	}

	public static void loadLevelResources(Level level) {
		level.loadResources();
	}
	
}
