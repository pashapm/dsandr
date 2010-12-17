package ru.jecklandin.duckshot;

import java.util.Vector;

import ru.jecklandin.duckshot.model.DuckShotModel;

import android.os.Handler;
import android.os.Message;

public class Match extends Thread {

	public static int DEFAULT_TIME = 60;
	
	public enum Bonus {NO, DOUBLE, TRIPLE, QUAD, MAXIKILL;

		Bonus next() {
			switch (this) {
			case NO:
				return Bonus.DOUBLE;
			case DOUBLE:
				return Bonus.TRIPLE;
			case TRIPLE:
				return Bonus.QUAD;
			case QUAD:
			case MAXIKILL:
				return Bonus.MAXIKILL;
			default:
				return NO;
			}
		}
	}
	
	public int mInitialTime;
	
	private Handler mHandler;
	private long mMatchMs; 
	private boolean mPaused = false;
	private int mScore = 0;
	private Vector<KillEvent> mKilledDucks = new Vector<KillEvent>();
	private Vector<Bonus> mAwards = new Vector<Bonus>();
	
	private long mNextDuckAppearance = Long.MAX_VALUE; 
	
	public Match(int seconds, Handler han) {
		mMatchMs = seconds * 1000;
		mInitialTime = seconds;
		mHandler = han;
		
//		mAwards.add(Bonus.DOUBLE);
		mAwards.add(Bonus.TRIPLE);
//		mAwards.add(Bonus.QUAD);
		mAwards.add(Bonus.MAXIKILL);
		
		DuckShotModel.getInstance().populate(3);
	}
	
	public void setHandler(Handler han) {
		mHandler = han;
	}
	
	public void startMatch() {
		this.start();
	}
	
	public void pauseMatch() {
		mPaused = true;
	}
	
	public void resumeMatch() {
		mPaused = false;
	}
	
	public int secondsRemain() {
		return (int) (mMatchMs/1000);
	}
	
	public boolean isPaused() {
		return mPaused;
	}

	@Override
	public void run() {
		while (! isInterrupted()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				return;
			}
			if (!mPaused) {
				if (mMatchMs > 50) {
					mMatchMs-=50;
					
					if (mNextDuckAppearance < System.currentTimeMillis()) {
						DuckShotModel.getInstance().addRandomDuck();
						mNextDuckAppearance = Long.MAX_VALUE;
					}
					
				} else {
					Message mess = new Message();
					mess.arg1 = 42;
					mHandler.sendMessage(mess);
					return;
				}
			}
		}
	}
	
	public Bonus addKilledDuck(Duck duck, long timestamp) {
		KillEvent event = new KillEvent(duck, timestamp);
		int length = mKilledDucks.size();
		Bonus bonus = Bonus.NO;
		if (!mKilledDucks.isEmpty() && (timestamp - mKilledDucks.get(length-1).mTimestamp) < 4000) {
			bonus = mKilledDucks.get(length-1).mBonusAwarded.next();
		}
		event.mBonusAwarded = bonus;
		mKilledDucks.add(event);
		return bonus;
	}
	
	// score 
	public void addScore(int sc) {
		mScore += sc;
	}
	
	public void requestNextDuckIfNeed() {
		if (DuckShotModel.getInstance().getDucksNumber() < 3) {
			mNextDuckAppearance = (long) (System.currentTimeMillis() + 1000 + Math.random()*2000);
		}
	}
	
	public int getScore() {
		return mScore;
	}
	
	public Vector<Bonus> getAwards() {
		return mAwards;
	}
	
	
	class KillEvent {
		Duck mDuck;
		Long mTimestamp;
		Bonus mBonusAwarded;
		KillEvent(Duck d, long timestamp) {
			mDuck = d;
			mTimestamp = timestamp;
		}
	}
}
