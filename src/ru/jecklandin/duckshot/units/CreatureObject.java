package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.Stone;
import ru.jecklandin.duckshot.Desk.DigitType;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class CreatureObject extends GameObject {

	protected abstract void drawNormal(Canvas c, Paint p);
	
	protected Stone mStone;
	
	protected static Bitmap commonBm;
	/**
	 * The duck is dead and need to be removed 
	 */
	public boolean toRecycle = false;
	
	/**
	 * set to true to move duck to another wave
	 */
	public boolean mMoveFlag = false;
	
	public GroundObject ownedWave;
	
	public int mScoreValue = 0;
	public int mSumValues = 0;
	public int mHealth = 100;
	
	// state 
    protected boolean mMovingRight = true;
    protected int ticksBeforeNextDive = generateNextDive();
    protected int ticksBeforeNextRotate = generateNextRotate();
    protected int timeout = 0;
	
	@Override
	public void draw(Canvas c, Paint p) {
	}

	@Override
	public float getNextOffset(float curOffset) {
		return 0;
	}
	
	public void notifyStoneWasThrown(Stone stone) {
		mStone = stone;
	}
	
	public void setOwnedWave(GroundObject wave, int xa) {
		this.ownedWave = wave;
		this.offset = xa;
		this.ownedWave.addCreature(this);
	}
	
	protected boolean isDanger() {
		int wave_num = DuckShotModel.getInstance().getTargetWave();
		if (wave_num != ownedWave.wave_num) {
			return false;
		}
		
		int sight_x = Desk.getInstance().getSightX();
		if (Math.abs(this.offset - sight_x) < ScrProps.scale(60)) {
			return true;
		}
		return false;
	}
	
	protected boolean isIntersects(int ix) {
		for (Obstacle obs : ownedWave.mObstacles) {
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
		if (mHealth < 100 && mHealth > 0) {
			color = Color.parseColor("#ff5a00");
			length = w / 2;
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
		
//		Log.d(TAG, "timeout: "+timeout+", ticksBeforeNextDive: "+ticksBeforeNextDive+" ,movingRight: "+mMovingRight);
	}
}
