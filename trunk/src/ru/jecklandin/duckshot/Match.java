package ru.jecklandin.duckshot;

import java.util.ArrayList;

import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.units.CreatureObject;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

public class Match extends Thread {

	public static int DEFAULT_TIME = 20;
	
	public enum Bonus {
		NO, DOUBLE {
			@Override
			public String toString() {
				return "DOUBLE KILL";
			}

			@Override
			public int getColor() {
				return Color.parseColor("#ffeb88");
			}
			
			@Override
			public float getMultiplier() {
				return 1.6f;
			}
		},
		TRIPLE {
			@Override
			public String toString() {
				return "TRIPLE KILL";
			}

			@Override
			public int getColor() {
				return Color.parseColor("#15ffa3");
			}
			
			@Override
			public float getMultiplier() {
				return 2.2f;
			}
		},
		QUAD {
			@Override
			public String toString() {
				return "QUAD KILL";
			}

			@Override
			public int getColor() {
				return Color.parseColor("#ffa330");
			}
			
			@Override
			public float getMultiplier() {
				return 2.8f;
			}
		},
		MAXIKILL {
			@Override
			public String toString() {
				return "MAXIKILL";
			}

			@Override
			public int getColor() {
				return Color.parseColor("#ff4c00");
			}
			
			@Override
			public float getMultiplier() {
				return 3.2f;
			}
		};

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

		public int getColor() {
			return 0;
		}
		
		public float getMultiplier() {
			return 1;
		}
	}
	
	public int mInitialTime;
	
	private Handler mHandler;
	private long mMatchMs; 
	private boolean mPaused = false;
	private int mScore = 0;
	private ArrayList<KillEvent> mKilledCreatures = new ArrayList<KillEvent>();
	private ArrayList<Bonus> mAwards = new ArrayList<Bonus>();
	
	public Match(int seconds, Handler han) {
		mMatchMs = seconds * 1000;
		mInitialTime = seconds;
		mHandler = han;
		
		DuckShotModel.getInstance().addObstacles(3);
		DuckShotModel.getInstance().populate(5);
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
				} else {
					Message mess = new Message();
					mess.arg1 = 42;
					mHandler.sendMessage(mess);
					
					//test
//					DuckApplication.getInstance().getCurrentLevel().unloadResources();
					
					
					return;
				}
			}
		}
	}
	
	public Bonus addKilledCreature(CreatureObject creature) {
		long timestamp = System.currentTimeMillis();
		KillEvent event = new KillEvent(creature, timestamp);
		int length = mKilledCreatures.size();
		Bonus bonus = Bonus.NO;
		if (!mKilledCreatures.isEmpty() && (timestamp - mKilledCreatures.get(length-1).mTimestamp) < 5000) {
			bonus = mKilledCreatures.get(length-1).mBonusAwarded.next();
		}
		event.mBonusAwarded = bonus;
		mKilledCreatures.add(event);
		if (bonus != Bonus.NO) {
			mAwards.add(bonus);
		}
		return bonus;
	}
	
	// score 
	public void addScore(int sc) {
		mScore += sc;
	}
	
	public void requestNextCreatureIfNeed() {
		if (DuckShotModel.getInstance().getCreaturesNumber() < 5) {
			DuckShotModel.getInstance().addRandomCreature();
		}
	}
	
	public int getScore() {
		return mScore;
	}
	
	public ArrayList<Bonus> getAwards() {
		return mAwards;
	}
	
	
	class KillEvent {
		CreatureObject mCreature;
		Long mTimestamp;
		Bonus mBonusAwarded;
		KillEvent(CreatureObject d, long timestamp) {
			mCreature = d;
			mTimestamp = timestamp;
		}
	}
}
