package ru.jecklandin.duckshot.model;

import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import ru.jecklandin.duckshot.*;
import ru.jecklandin.duckshot.GameObject.OBJ_TYPE;

public class DuckShotModel {

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

	private final int WAVES_NUM = 10;
	private final int WAVES_GAP = 28;
	
	public static final int MAX_MSEC = 1999;
	public static int WAVES_OFFSET;
	
	public DuckShotModel() {
		
		WAVES_OFFSET = ScreenProps.screenHeight - WAVES_NUM * WAVES_GAP - Desk.getInstance().mDesk.getHeight() - 80; 
		
		int waves_height = WAVES_NUM * WAVES_GAP;
		
		
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
	
	public void launchStone(int x, long msec) {
		Stone stone = new Stone(x, getYFromMsec(msec));
		synchronized (mStones) { 
			mStones.add(stone); 
		}
		
		checkForCollide(stone, mYes.size() - 1 - getYNumFromMsec(msec));
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
	
	public void addDuck(int wave_num) {
		int randx = (int) (Math.random() * ScreenProps.screenWidth);
		Duck d = new Duck( randx );
		d.mValue = 50 + 10*(mWaves.size() - 1 - wave_num);
		d.ownedWave = mWaves.get(wave_num);
		mWaves.get(wave_num).addDuck(d);
	}
	
	public void addRandomDuck() {
		int randy = (int) (Math.random() * mWaves.size());
		addDuck(randy);
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
