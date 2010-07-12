package ru.jecklandin.duckshot;

import java.util.Vector;

import android.os.Handler;
import android.os.Message;

public class Match extends Thread {

	public enum Bonus {NO, DOUBLE, TRIPLE, QUAD, MAXIKILL;
		public Bonus next() {
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
	
	private Handler mHandler;
	
	private long mMatchMs; 
	private boolean mPaused = false;
	private Vector<KillEvent> mKilledDucks = new Vector<KillEvent>();
	
	public Match(int seconds, Handler han) {
		mMatchMs = seconds * 1000;
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
