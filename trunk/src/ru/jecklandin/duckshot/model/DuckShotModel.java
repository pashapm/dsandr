package ru.jecklandin.duckshot.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.Stone;
import ru.jecklandin.duckshot.levels.Level;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Duck;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;
import ru.jecklandin.duckshot.units.Obstacle.Type;
import android.util.Log;

public class DuckShotModel {

	private static String TAG = "DuckShotModel";
	  
	// Game objects 
	public ArrayList<GroundObject> mGrounds = new ArrayList<GroundObject>();
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

	public static int GROUNDS_NUM = 10;
	public static int GROUND_HEIGHT = 0;
	public static int GROUND_OFFSET;
	public static int GROUNDS_GAP = ScrProps.scale(28);
	
	private ManipulatingThread mWorkingThread;
	
	public DuckShotModel() {
		mWorkingThread = new ManipulatingThread();
		mWorkingThread.start();
	}
	
	/**
	 * 
	 * @param density - how many obstacles (1,2,3)
	 */
	public void addObstacles(int density) {
		DuckApplication.getInstance().getCurrentLevel().setObstacles();
	} 
	
	public void reinitialize(Level level) {
		mStones.clear();
		mGrounds.clear();
		
		GROUNDS_NUM = DuckApplication.getInstance().getCurrentLevel().getSettings().getInt("groundsNumber", 10);
		GROUNDS_GAP = ScrProps.scale( GROUNDS_NUM == 10 ? 28 : 40); //TODO rem hardcode
		
		GROUND_OFFSET = ScrProps.screenHeight - GROUNDS_NUM * GROUNDS_GAP - Desk.mDesk.getHeight() - ScrProps.scale(80); 
//		if (GROUNDS_NUM == 6) {
//			GROUND_OFFSET -= ScrProps.scale(50);
//		}
		GROUND_HEIGHT = GROUNDS_NUM * GROUNDS_GAP;
		
		
		
		// loading Y-coord
		mYes.clear();
		for (int i=0; i<GROUNDS_NUM; ++i) {
			mYes.add(GROUND_OFFSET + i * GROUNDS_GAP);
		} 
		
		for (int i=0; i<mYes.size(); ++i) {
			// -50 .. +50
			int mx = (int) (Math.random()*50 - 50);
			// 1 .. 5
			int ms = i / 2;
			mGrounds.add(level.createGroundObject(mx, mYes.get(i), ms, i));
		}
		
		populate(0);
		mWorkingThread.mQueue.clear();
	}
	  
	public synchronized void populate(int num) {
		
		for (GroundObject w : mGrounds) {
			w.mCreatures.clear();
		}
		
		for (int i=0; i<num; ++i) {
			addRandomCreature();
		}
	}
	
	public int getCreaturesNumber() {
		int sum = 0;
		for (GroundObject w : mGrounds) {
			for (CreatureObject d : w.mCreatures) {
				if (!d.toRecycle) {
					sum ++;
				}
			}
		}
		return sum;
	}
	
	public void launchStone(int wave_number, int x) {
		wave_number = mGrounds.size() > wave_number ? wave_number : mGrounds.size() - 1; //wtf
		if (wave_number < 0) {
			wave_number = 0;
		}
		Stone stone = new Stone(x, mGrounds.get(wave_number).y);
		mStones.add(stone);
		notifyCreatures(stone, wave_number);
		notifyObstacles(stone, wave_number);
	}
	
	public int getTopY() {
		return mYes.get(0);
	}
	
	public int getBottomY() {
		return mYes.get(mYes.size()-1) + GROUNDS_GAP;
	}
	
	private void notifyCreatures(Stone stone, int ny) {
		for (CreatureObject creat : mGrounds.get(ny).mCreatures) {  
			creat.notifyStoneWasThrown(stone); 
		} 
	}
	
	private void notifyObstacles(Stone stone, int ny) {
		for (Obstacle obs : mGrounds.get(ny).mObstacles) {  
			obs.notifyStoneWasThrown(stone); 
		} 
	}
	
	/**
	 * Adds new duck to the given wave
	 * @param wave_num
	 * @return x is it able to place one more duck, -1 otherwise
	 */
	public int addCreature(int wave_num) {
		Level lev = DuckApplication.getInstance().getCurrentLevel();
		CreatureObject creature = lev.createCreatureObject(0);
		return addCreature(creature, wave_num);
	}
	
	/**
	 * Adds existing duck to the given wave
	 * @param d
	 * @param wave_num
	 * @return -1 if can't, x otherwise
	 */
	public int addCreature(CreatureObject d, int wave_num) {
		
		boolean outOfScreen = false;
		if (d instanceof Duck && ((Duck)d).isArmored()) {
			outOfScreen = true;
		}

		int randx;
		int tries = 3; 
		do {
			randx = (int) (ScrProps.screenWidth * (outOfScreen ? 1 : Math.random()));
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
		GroundObject ownedGround = mGrounds.get(wave_num);
		if (!ownedGround.isPlaceFree(x)) {
			return false;
		}
		
		d.setOwnedWave(ownedGround, x);
		return true;
	}
	
	/**
	 * 
	 * @param creature
	 * @return distance
	 */
	public int moveCreatureToRandomGround(CreatureObject creature) {
		CreatureObject d = creature;
		GroundObject wave = creature.ownedGround;
		int wasy = d.ownedGround.y;
		int wasx = (int) d.ownedGround.offset;
		creature.ownedGround.removeCreature(creature);
		int randy = 0;
		int randx = 0; 
		// look for free wave
		do {
			randy = (int) (Math.random() * mGrounds.size());
			if (randy == mGrounds.indexOf(wave)) {  //we want another wave
				randy -= (randy==0 ? -1 : 1);
			}
			randx = (int) (Math.random() * ScrProps.screenWidth);;
		} while ( ! addCreature(d, randy, randx));
		
		
		int ydistance = Math.abs(wasy - mGrounds.get(randy).y);
		int xdistance = Math.abs(wasx - randx);
		Log.d(TAG, "Moved duck "+ydistance+" | "+xdistance);
		Log.d(TAG, "Dist "+Math.hypot(xdistance, ydistance));
		return (int) Math.hypot(xdistance, ydistance);
	}

	public void addRandomCreature() {
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
		int maxy = mGrounds.get(mGrounds.size()-1).y - mGrounds.get(0).y;
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
							randy = (int) (Math.random() * mGrounds.size());
						} while (addCreature(randy) < 0);
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
