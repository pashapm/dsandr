package ru.jecklandin.duckshot;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Debug;
import android.util.Log;

public class ImgManager {

	private static Context mCtx;
	private static Map<String, Bitmap> mImgMap = new HashMap<String, Bitmap>();
	private static Map<String, Bitmap[]> mAniMap = new HashMap<String, Bitmap[]>();
	  
	private static boolean mLoaded = false;
	
	public static void loadImages(Context ctx) {
		if (mLoaded) {
			return;
		}
		mCtx = ctx;  
		
		Bitmap bm = getBitmap(R.drawable.wave);
		
		int qq[] = new int[bm.getWidth() * bm.getHeight()];
		bm.getPixels(qq, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight() );
		
		bm = Bitmap.createBitmap(qq, 0, bm.getWidth(), bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
		
		mImgMap.put("wave", bm);
		bm = getBitmap(R.drawable.duck);
		mImgMap.put("duck", bm);
		bm = getBitmap(R.drawable.deadduck);
		mImgMap.put("deadduck", bm);
		bm = getBitmap(R.drawable.desk);
		mImgMap.put("desk", bm);
		bm = getBitmap(R.drawable.stonea);
		mImgMap.put("stone", bm);
		bm = getBitmap(R.drawable.stoneb);
		mImgMap.put("stone2", bm);
		bm = getBitmap(R.drawable.stonec);
		mImgMap.put("stone3", bm);      
		bm = getBitmap(R.drawable.clouda);
		mImgMap.put("cloud1", bm);
		bm = getBitmap(R.drawable.cloudb);
		mImgMap.put("cloud2", bm);
		bm = getBitmap(R.drawable.cloudc);
		mImgMap.put("cloud3", bm);
		bm = getBitmap(R.drawable.sight);
		mImgMap.put("sight", bm);
		bm = getBitmap(R.drawable.settings_quant);
		mImgMap.put("quant", bm);
		bm = getBitmap(R.drawable.settings_quant_e);
		mImgMap.put("quant_e", bm);
		
		bm = getBitmap(R.drawable.rock_1);
		mImgMap.put("rock1", bm);
		
		bm = getBitmap(R.drawable.anifountain);
		Bitmap[] anim = new Bitmap[8];
		int diff_x = ScrProps.scale(84);
		int diff_y = ScrProps.scale(84);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		bm.getWidth();
		bm.getHeight(); 
		mAniMap.put("fountain", anim);
		bm.recycle();
		   
		bm = getBitmap(R.drawable.duckdive);
		anim = new Bitmap[16]; 
		diff_x = ScrProps.scale(84);
		diff_y = ScrProps.scale(84);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mAniMap.put("duckdive", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.duckemerge);
		anim = new Bitmap[8];
		diff_x = ScrProps.scale(84);
		diff_y = ScrProps.scale(84);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		} 
		mAniMap.put("duckemerge", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.digits);
		anim = new Bitmap[11];
		diff_x = ScrProps.scale(30);
		diff_y = ScrProps.scale(30);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mAniMap.put("digits", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.digits_red);
		anim = new Bitmap[11]; 
		diff_x = ScrProps.scale(30);
		diff_y = ScrProps.scale(30);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mAniMap.put("digits_red", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.digits_time);
		anim = new Bitmap[11]; 
		diff_x = ScrProps.scale(25);
		diff_y = ScrProps.scale(20);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mAniMap.put("digits_time", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.awards);
		anim = new Bitmap[5]; 
		diff_x = ScrProps.scale(35);
		diff_y = ScrProps.scale(35);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mAniMap.put("awards", anim);
		bm.recycle();
		
		bm = getBitmap(R.drawable.shrapnel);
		anim = new Bitmap[6]; 
		diff_x = ScrProps.scale(100);
		diff_y = ScrProps.scale(100);
		for (int i=0; i<anim.length; ++i) {
			anim[i] = Bitmap.createBitmap(bm, i*diff_x, 0, diff_x, diff_y);
		}
		mAniMap.put("shrapnel", anim);
		bm.recycle();
		
		mLoaded = true;
	}

	private static Bitmap getBitmap(int id) {
		return BitmapFactory.decodeResource(mCtx.getResources(), id);
	}
	
	public static Bitmap getBitmap(String name) {
		return mImgMap.get(name);
	}
	
	public static Bitmap[] getAnimation(String name) {
		return mAniMap.get(name);
	}
}
