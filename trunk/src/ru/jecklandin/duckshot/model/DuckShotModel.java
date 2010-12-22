package ru.jecklandin.duckshot.model;

import java.util.ArrayList;
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
	public ArrayList<Wave> mWaves = new ArrayList<Wave>();
	public ArrayList<Stone> mStones = new ArrayList<Stone>();
	public ArrayList<Integer> mYes = new ArrayList<Integer>();
	
	private int mTargetWave;
	
	private static DuckShotModel s_instance;
	public static synchronized DuckShotModel getInstance() {
		if (DuckShotModel.s_instance == null) {
			DuckShotModel.s_instance = new DuckShotModel();
		}
		return s_instance;
	}

	public static final int WAVES_NUM = 10;
	public static int WAVES_HEIGHT = 0;
	public static final int MAX_MSEC = 1999;
	public static int WAVES_OFFSET;
	private static final int WAVES_GAP = ScrProps.scale(28);
	
	public DuckShotModel() {
		WAVES_OFFSET = ScrProps.screenHeight - WAVES_NUM * WAVES_GAP - Desk.getInstance().mDesk.getHeight() - ScrProps.scale(80); 
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
			mWaves.add(new Wave(mx, mYes.get(i), ms, i));
		}
	}
	
	public void reinitialize() {
		populate(0);
	}
	
	public synchronized void populate(int num) {
		
		for (Wave w : mWaves) {
			w.ducks.clear();
		}
		
		for (int i=0; i<num; ++i) {
			addRandomDuck();
		}
	}
	
	public int getDucksNumber() {
		int sum = 0;
		for (Wave w : mWaves) {
			for (Duck d : w.ducks) {
				if (!d.toRecycle) {
					sum += w.ducks.size();
				}
			}
		}
		return sum;
	}
	
	public void launchStone(int wave_number, int x) {
		Stone stone = new Stone(x, mWaves.get(wave_number).y);
		mStones.add(stone);
		notifyDucks(stone, wave_number);
		Map<String, String> map = new HashMap<String, String>() ;
	}
	
	public int getTopY() {
		return mYes.get(0);
	}
	
	public int getBottomY() {
		return mYes.get(mYes.size()-1) + WAVES_GAP;
	}
	
	private void notifyDucks(Stone stone, int ny) {
		for (Duck duck : mWaves.get(ny).ducks) {  
			duck.notifyStoneWasThrown(stone); 
		} 
	}
	
	/**
	 * Adds new duck to the given wave
	 * @param wave_num
	 * @return x is it able to place one more duck, -1 otherwise
	 */
	public int addDuck(int wave_num) {
		Duck d = new Duck( 0 );
		return addDuck(d, wave_num);
	}
	
	/**
	 * Adds existing duck to the given wave
	 * @param d
	 * @param wave_num
	 * @return -1 if can't, x otherwise
	 */
	public int addDuck(Duck d, int wave_num) {
		int randx;
		int tries = 3; 
		do {
			randx = (int) (Math.random() * ScrProps.screenWidth);
			if (--tries < 0) {
				return -1;
			}
		} while (!addDuck(d, wave_num, randx));
		
		return randx;
	}
	
	/**
	 * Try to add the duck to the specified place
	 * @param d
	 * @param wave_num
	 * @param x
	 * @return
	 */
	public boolean addDuck(Duck d, int wave_num, int x) {
		Wave ownedWave = mWaves.get(wave_num);
		if (!ownedWave.isPlaceFree(x)) {
			return false;
		}
		
		d.setOwnedWave(ownedWave, x);
		d.setRandomDelay();
		return true;
	}
	
	/**
	 * 
	 * @param duck
	 * @return distance
	 */
	public int moveDuckToRandomWave(Duck duck) {
		Duck d = duck;
		Wave wave = duck.ownedWave;
		int wasy = d.ownedWave.y;
		int wasx = (int) d.ownedWave.offset;
		duck.ownedWave.removeDuck(duck);
		int randy = 0;
		int randx = 0; 
		// look for free wave
		do {
			randy = (int) (Math.random() * mWaves.size());
			if (randy == mWaves.indexOf(wave)) {  //we want another wave
				randy -= (randy==0 ? -1 : 1);
			}
			randx = (int) (Math.random() * ScrProps.screenWidth);;
		} while ( ! addDuck(d, randy, randx));
		
		
		int ydistance = Math.abs(wasy - mWaves.get(randy).y);
		int xdistance = Math.abs(wasx - randx);
		Log.d(TAG, "Moved duck "+ydistance+" | "+xdistance);
		Log.d(TAG, "Dist "+Math.hypot(xdistance, ydistance));
		return (int) Math.hypot(xdistance, ydistance);
	}

	public void addRandomDuck() {
		Log.d(TAG, "ADDINg duck");

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (DuckShotModel.getInstance()) {
					int randy;
					// look for free wave
					do {
						randy = (int) (Math.random() * mWaves.size());
					} while (addDuck(randy) < 0);
					DuckShotModel.getInstance().notifyAll();
				}
			}
		}).start();
		
	}

	public void cleanupStones() {
		synchronized (mStones) {
			Iterator<Stone> it = mStones.iterator();
			while (it.hasNext()) {
				if  (it.next().sank) {
					it.remove();
				}
			}
		}
	}

	public int getTimeoutByDistance(int distance) {
		int maxtm = 80; //80 drawings is max timeout
		int maxy = mWaves.get(mWaves.size()-1).y - mWaves.get(0).y;
		int maxx = ScrProps.screenWidth;
		int maxdist = (int) Math.hypot(maxx, maxy);
		int timeout = distance * maxtm / maxdist;
		return timeout;
	}

	public void setTargetWave(int waveNum) {
		mTargetWave = waveNum;
	}
	
	public int getTargetWave() {
		return mTargetWave;
	}
}
