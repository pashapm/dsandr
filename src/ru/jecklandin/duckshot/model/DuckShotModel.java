package ru.jecklandin.duckshot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.flurry.android.FlurryAgent;

import android.content.Context;
import android.util.Log;
import ru.jecklandin.duckshot.*;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Duck;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;
import ru.jecklandin.duckshot.units.Wave;
import ru.jecklandin.duckshot.units.Obstacle.Type;

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
	public static final int WAVES_GAP = ScrProps.scale(28);
	
	private ManipulatingThread mWorkingThread;
	
	public DuckShotModel() {
		WAVES_OFFSET = ScrProps.screenHeight - WAVES_NUM * WAVES_GAP - Desk.mDesk.getHeight() - ScrProps.scale(80); 
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
		
		mWorkingThread = new ManipulatingThread();
		mWorkingThread.start();
	}
	
	/**
	 * 
	 * @param density - how many obstacles (1,2,3)
	 */
	public void addObstacles(int density) {
		if (density > 0) {
			ObstacleManager.getInstance().addRock(Type.ROCK1, 10, 6);
		}
		
		if (density > 1) {
			ObstacleManager.getInstance().addRock(Type.ROCK2, 30, 0);
			ObstacleManager.getInstance().addRock(Type.ROCK2, 80, 2);
		}
		
		if (density > 2) {
			ObstacleManager.getInstance().addRock(Type.ROCK3, 200, 3);
		}
	} 
	
	public void reinitialize() {
		populate(0);
		mWorkingThread.mQueue.clear();
	}
	  
	public synchronized void populate(int num) {
		
		for (Wave w : mWaves) {
			w.mCreatures.clear();
		}
		
		for (int i=0; i<num; ++i) {
			addRandomDuck();
		}
	}
	
	public int getDucksNumber() {
		int sum = 0;
		for (Wave w : mWaves) {
			for (CreatureObject d : w.mCreatures) {
				if (!d.toRecycle) {
					sum ++;
				}
			}
		}
		return sum;
	}
	
	public void launchStone(int wave_number, int x) {
		wave_number = mWaves.size() > wave_number ? wave_number : mWaves.size() - 1; //wtf
		if (wave_number < 0) {
			wave_number = 0;
		}
		Stone stone = new Stone(x, mWaves.get(wave_number).y);
		mStones.add(stone);
		notifyDucks(stone, wave_number);
		notifyObstacles(stone, wave_number);
	}
	
	public int getTopY() {
		return mYes.get(0);
	}
	
	public int getBottomY() {
		return mYes.get(mYes.size()-1) + WAVES_GAP;
	}
	
	private void notifyDucks(Stone stone, int ny) {
		for (CreatureObject creat : mWaves.get(ny).mCreatures) {  
			creat.notifyStoneWasThrown(stone); 
		} 
	}
	
	private void notifyObstacles(Stone stone, int ny) {
		for (Obstacle obs : mWaves.get(ny).mObstacles) {  
			obs.notifyStoneWasThrown(stone); 
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
		} while (!addCreature(d, wave_num, randx));
		
		return randx;
	}
	
	/**
	 * Try to add the duck to the specified place
	 * @param d
	 * @param wave_num
	 * @param x
	 * @return
	 */
	public boolean addCreature(CreatureObject d, int wave_num, int x) {
		Wave ownedWave = mWaves.get(wave_num);
		if (!ownedWave.isPlaceFree(x)) {
			return false;
		}
		
		d.setOwnedWave(ownedWave, x);
		return true;
	}
	
	/**
	 * 
	 * @param duck
	 * @return distance
	 */
	public int moveCreatureToRandomGround(CreatureObject duck) {
		CreatureObject d = duck;
		GroundObject wave = duck.ownedWave;
		int wasy = d.ownedWave.y;
		int wasx = (int) d.ownedWave.offset;
		duck.ownedWave.removeCreature(duck);
		int randy = 0;
		int randx = 0; 
		// look for free wave
		do {
			randy = (int) (Math.random() * mWaves.size());
			if (randy == mWaves.indexOf(wave)) {  //we want another wave
				randy -= (randy==0 ? -1 : 1);
			}
			randx = (int) (Math.random() * ScrProps.screenWidth);;
		} while ( ! addCreature(d, randy, randx));
		
		
		int ydistance = Math.abs(wasy - mWaves.get(randy).y);
		int xdistance = Math.abs(wasx - randx);
		Log.d(TAG, "Moved duck "+ydistance+" | "+xdistance);
		Log.d(TAG, "Dist "+Math.hypot(xdistance, ydistance));
		return (int) Math.hypot(xdistance, ydistance);
	}

	public void addRandomDuck() {
		mWorkingThread.mQueue.add(42);
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
	
	class ManipulatingThread extends Thread {
		
		BlockingQueue<Integer> mQueue = new LinkedBlockingQueue<Integer>();
		
		@Override
		public void run() {
			while (true) {
				try {
					int type = mQueue.take();
					synchronized (DuckShotModel.getInstance()) {
						int randy;
						// look for free wave
						do {
							randy = (int) (Math.random() * mWaves.size());
						} while (addDuck(randy) < 0);
						DuckShotModel.getInstance().notifyAll();
					} 
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				
			}
		}
	}
}
