package ru.jecklandin.duckshot.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.flurry.android.FlurryAgent;

import android.content.Context;
import android.util.Log;
import ru.jecklandin.duckshot.*;
import ru.jecklandin.duckshot.GameObject.OBJ_TYPE;

public class DuckShotModel {

	private static String TAG = "DuckShotModel";
	
	// Game objects 
	public Vector<Wave> mWaves = new Vector<Wave>();
	public Vector<Stone> mStones = new Vector<Stone>();
	public Vector<Integer> mYes = new Vector<Integer>();
	
	private int mScore = 0;
	
	private static DuckShotModel s_instance;
	public static synchronized DuckShotModel getInstance() {
		if (DuckShotModel.s_instance == null) {
			DuckShotModel.s_instance = new DuckShotModel();
		}
		return s_instance;
	}

	public static final int WAVES_NUM = 10;
	public static int WAVES_HEIGHT = 0;
	private static final int WAVES_GAP = 28;
	
	
	public static final int MAX_MSEC = 1999;
	public static int WAVES_OFFSET;
	
	public DuckShotModel() {
		
		WAVES_OFFSET = ScreenProps.screenHeight - WAVES_NUM * WAVES_GAP - Desk.getInstance().mDesk.getHeight() - 80; 
		
		WAVES_HEIGHT = WAVES_NUM * WAVES_GAP;
		
		
		// loading Y-coord
		for (int i=0; i<WAVES_NUM; ++i) {
			mYes.add(WAVES_OFFSET + i * WAVES_GAP);
		}
		
		for (int i=0; i<mYes.size(); ++i) {
			// -50 .. +50
			int mx = (int) (Math.random()*50 - 50);
			// 1 .. 5
			int ms = i / 2;
			mWaves.add(new Wave(mx, mYes.get(i), ms));
		}
		
		for (int i=0; i<3; ++i) {
			addRandomDuck();
//			int wave_num = i*2;
//			Duck d = new Duck(50*i);
//			d.ownedWave = mWaves.get(wave_num);
//			mWaves.get(wave_num).addDuck(d);
		}
	}
	
	@Deprecated
	public void launchStone(int x, long msec) {
		Stone stone = new Stone(x, getYFromMsec(msec));
		synchronized (mStones) { 
			mStones.add(stone); 
		}
		
		checkForCollide(stone, mYes.size() - 1 - getYNumFromMsec(msec));
	}
	
	public void launchStone(int wave_number, int x) {
		Log.d(TAG, "!!!"+x);
		Stone stone = new Stone(x, mWaves.get(wave_number).y);
		mStones.add(stone);
		checkForCollide(stone, wave_number);
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("wave_number", ""+wave_number);
		map.put("x", ""+x);
		FlurryAgent.onEvent("shot", map);
	}
	/**
	 * Final point of stone's flight
	 * @param msec
	 * @return
	 */
	public int getYFromMsec( long msec ) {
		return mYes.get(mYes.size() - 1 - getYNumFromMsec(msec));
	}
	
	public int getYNumFromMsec( long msec ) {
		//msec is 1999 max
		//1999 / 10yes = 200  
		final int MULT = 200;
		
		//matching Y-es with mseconds
		int y = (int) (msec / 200);
		return y;
	}
	
	public int getTopY() {
		return mYes.get(0);
	}
	
	public int getBottomY() {
		return mYes.get(mYes.size()-1) + WAVES_GAP;
	}
	
	private void checkForCollide(Stone stone, int ny) {
		for (Duck duck : mWaves.get(ny).ducks) {  
			duck.throwStone(stone); 
		} 
	}
	
	/**
	 * 
	 * @param wave_num
	 * @return is it able to place one more duck
	 */
	public boolean addDuck(int wave_num) {
		Wave ownedWave = mWaves.get(wave_num);

		int randx;
		int tries = 3; 
		do {
			randx = (int) (Math.random() * ScreenProps.screenWidth);
			if (--tries < 0) {
				return false;
			}
		} while (!ownedWave.isPlaceFree(randx));
		
		Duck d = new Duck( 0 );
		d.mScoreValue = 50 + 10*(mWaves.size() - 1 - wave_num);
		d.ownedWave =  ownedWave;
		d.offset = randx;
		d.ownedWave.addDuck(d);
		return true;
	}

	public void addRandomDuck() {
		int randy;
		// look for free wave
		do {
			randy = (int) (Math.random() * mWaves.size());
		} while (!addDuck(randy));
	}

	public void cleanup() {
		boolean need_gc = false;
		synchronized (mStones) {
			Iterator<Stone> it = mStones.iterator();
			while (it.hasNext()) {
				if  (it.next().sank) {
					need_gc = true;
					it.remove();
				}
			}
		}
		
		
		if (need_gc) {
			//System.gc();
		}
		
	}
	
	public void addScore(int sc) {
		mScore += sc;
	}
	
	public int getScore() {
		return mScore;
	}

}
