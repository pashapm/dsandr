package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.Stone;
import ru.jecklandin.duckshot.Desk.DigitType;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public abstract class CreatureObject extends GameObject {

	protected int MAX_OFFSET = ScrProps.scale(300);
	protected int MIN_OFFSET = -ScrProps.scale(70);
	
	protected abstract void drawNormal(Canvas c, Paint p);
	
	protected Stone mStone;
	
	protected static Bitmap commonBm;
	protected static Bitmap secondCommonBm;
	
	public GroundObject ownedGround;
	
	public int mScoreValue = 0;
	public int mSumValues = 0;
	public int mHealth = 100;
	
	protected Matrix addit_m;
	
	// state 
    protected boolean mMovingRight = true;
    protected int ticksBeforeNextDive = generateNextDive();
    protected int ticksBeforeNextRotate = generateNextRotate();
    protected int timeout = 0;
    protected boolean isDead = false;
    protected int delay = 0;
    protected int overallTicks = 0;
    protected boolean end_animation = false;
    
    protected boolean isHidden = false;
    protected int diving_frame = 0;

    protected boolean isAppearing = false;
    protected int emerging_frame = 0;
    
    /**
	 * The duck is dead and need to be removed 
	 */
	public boolean toRecycle = false;
	
	/**
	 * set to true to move duck to another wave
	 */
	public boolean mMoveFlag = false;
	
	protected abstract void handleHit(int hps); 

	@Override
	public float getNextOffset(float curOffset) {
		return 0;
	}
	
	public void notifyStoneWasThrown(Stone stone) {
		mStone = stone;
	}
	
	public void setOwnedWave(GroundObject wave, int xa) {
		this.ownedGround = wave;
		this.offset = xa;
		this.ownedGround.addCreature(this);
	}
	
	protected boolean isDanger() {
		int wave_num = DuckShotModel.getInstance().getTargetWave();
		if (wave_num != ownedGround.wave_num) {
			return false;
		}
		
		int sight_x = Desk.getInstance().getSightX();
		if (Math.abs(this.offset - sight_x) < ScrProps.scale(60)) {
			return true;
		}
		return false;
	}
	
	protected boolean isIntersects(int ix) {
		for (Obstacle obs : ownedGround.mObstacles) {
			if (obs.isIntersects(ix)) {
				return false;
			}
		}
		return ( ix > (this.offset - commonBm.getWidth()/4) 
				&& ix < (this.offset + commonBm.getWidth()));
	}
	
	protected void drawHealth(float[] point, Canvas c, Paint p) {
		point[1]-=ScrProps.scale(10);
		int w = (int) (commonBm.getWidth());
		
		int color = Color.parseColor("#6bff00");
		int length = w;
		
		switch (mHealth) {
		case 70:
			color = Color.parseColor("#ffe900");
			length = (w * 2) / 3;
			break;
		case 50:
			color = Color.parseColor("#ff8500");
			length = w / 2;
			break;
		case 20:
			color = Color.parseColor("#ff5a00");
			length = w / 5;
			break;
		default:
			break;
		}
		
		p.setColor(color);  
		
		if (mMovingRight) {
			point[0]-=w;
		}
		c.drawRect(point[0], point[1], point[0]+length, point[1]+ScrProps.scale(4), p);
	}
	
	/**
	 * from 20 to 200 ticks 
	 * @return
	 */
	protected int generateNextRotate() {
		return (int) (Math.random()*180+20);
	}
	
	protected float generateNextSpeed() {
		return (float) (Math.random()*1.5+1);
	}
	
	/**
	 * from 20 to 300 ticks 
	 * @return
	 */
	protected int generateNextDive() {
		return (int) (Math.random()*280+20);
	}
	
	protected int moveToAnotherWave() {
		return DuckShotModel.getInstance().moveCreatureToRandomGround(this);
	} 
	
	protected void rotate() {
		mMovingRight = !mMovingRight;
		ticksBeforeNextRotate = generateNextRotate();
		speed = generateNextSpeed();
	}
	
	public void move() {
		int distance = moveToAnotherWave();
		timeout = DuckShotModel.getInstance().getTimeoutByDistance(distance);
		mMoveFlag = false;
		 
		ticksBeforeNextDive = generateNextDive();
		speed = generateNextSpeed();
		double rnd = Math.random();
		mMovingRight = rnd < 0.5;   
	}
	
	protected void addValue(int val) {
		mSumValues += val;
	}
	
	public void hide() {
		isHidden = true;
		isAppearing = false;
		emerging_frame = 0;
	}
	
	public void appear() {
		isAppearing = true;
		diving_frame = 0;
	}
	
	public void setRandomDelay() {
		delay = (int) (Math.random() * 4 * DuckApplication.FPS);
	}
}
