package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.DuckGame;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.Match.Bonus;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Cupcake extends CreatureObject {

	private int dead_stage;
	
	private static Bitmap[] mCupcake;
	private static Bitmap mCupcakeDead;
	
	public Cupcake(int x) {
		super(); 
		this.offset = x < ScrProps.screenWidth/2 ? 
				-mCupcakeDead.getWidth() : 
				ScrProps.screenWidth+mCupcakeDead.getWidth(); 
		speed = 4f;
		addit_m = new Matrix();
		
		mHealth = 1;
	}

	@Override
	protected void drawNormal(Canvas c, Paint p) {
		matrix.postTranslate(0, -commonBm.getHeight()/5 + DuckShotModel.GROUNDS_GAP/2);
		c.drawBitmap(mCupcake[(overallTicks/2)%8], matrix, p);
	}

	@Override
	public float getNextOffset(float curOffset) {
		if (isDead) {
			return offset;
		}
		
		overallTicks++;
		if (Desk.getInstance().getSightVisibility() 
				&& overallTicks % (DuckApplication.FPS * 2/3) == 0) {
			if (isDanger()) {  //the sight is nearby!  
				rotate();
				speed = 5;
			}
		}
		
		if (mMovingRight) {
			offset += speed;
		} else {
			offset -= speed;
		}
		
		if (isOutOfScreen()) {
			hide();
		} else if (--ticksBeforeNextRotate < 0) {
			rotate();
		}
		
		if (offset < MIN_OFFSET) {
			mMovingRight = true;
		} else if (offset > MAX_OFFSET) {
			mMovingRight = false;
		}
		return offset;
	}

	@Override
	protected float generateNextSpeed() {
		return (float) (Math.random()*2.5+2);
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		if (end_animation) {
			toRecycle = true;
			return;
		}
		
		if (delay > 0) {
			delay--;
			return;
		}
		
		if (timeout > 0) {
			timeout--;
			return;
		}
		
		matrix.reset();
		
		float next_offset = getNextOffset(offset);
		if (mMovingRight) {
			matrix.setScale(-1, 1);
			matrix.postTranslate(commonBm.getWidth(), 0);
		}
		matrix.postTranslate(next_offset, y - commonBm.getHeight() / 2);
		
		if (!isDead && mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				handleHit(mStone.HPS);
				mStone.mHasHitSomething = true;
			}
			mStone = null;
		}
		
		
		
		if (isDead) {
			drawDeadAnimation(c, p);
		} else {
			if (!isHidden) {
				drawNormal(c, p);
			}
		}
	}
	
	int sp = 0;
	int acceleration = 4;
	
	private void drawDeadAnimation(Canvas c, Paint p) {
		matrix.postTranslate(0, sp);
		c.drawBitmap(mCupcakeDead, matrix, p);
		drawScore(c, p);
		sp-=acceleration;
		acceleration-=2;
		if (sp > ScrProps.scale(480)) { 
			end_animation = true;
		}
	}
	
	@Override
	public void hide() {
		mMoveFlag  = true;
		super.hide();
	}
	
	@Override
	public void appear() {
		super.appear();
		isHidden = false;
	}
	
	private void drawScore(Canvas c, Paint p) {
		dead_stage-=ScrProps.scale(4);
		
		addit_m.reset();
		addit_m.setScale(0.6f, 0.6f);
		addit_m.postTranslate(this.offset, ownedGround.y + ScrProps.scale(dead_stage));
		Desk.drawScoreDigits(c, p, addit_m, mSumValues);
	}
	
	public static void initBitmaps() {
		mCupcake = ImgManager.getAnimation("cupcake");
		mCupcakeDead = ImgManager.getBitmap("cupcake_dead");
		commonBm = mCupcake[0];
	}
	
	@Override
	public void handleHit(int hps) {
		SoundManager.getInstance().vibrate(30);
		mStone.makeFountain = false;
		mHealth -= hps;
		 
		addValue(mScoreValue);
		if (mHealth <=0) {
			isDead = true;
			SoundManager.getInstance().playScream();
			SoundManager.getInstance().playHit();
//			DuckGame.getCurrentMatch().requestNextCreatureIfNeed();
			Bonus bonus = DuckGame.getCurrentMatch().addKilledCreature(this);
			if (bonus != Bonus.NO) {
				Desk.getInstance().playBonus(bonus);
			}
			DuckGame.getCurrentMatch().addScore((int) (mSumValues *= bonus.getMultiplier()));
			
			if (DuckShotModel.getInstance().getCreaturesNumber() == 0) {
				DuckApplication.getInstance().getCurrentMatch().stopMatch();
			}
		} 
	}
	
	@Override
	public void setOwnedWave(GroundObject ground, int xa) {
		mScoreValue = 90 + 20*(DuckShotModel.getInstance().mGrounds.size() - 1 - ground.wave_num);
		appear();
		super.setOwnedWave(ground, xa);
		this.offset = x < ScrProps.screenWidth/2 ? 
				-mCupcakeDead.getWidth() : 
				ScrProps.screenWidth+mCupcakeDead.getWidth(); 
		setRandomDelay();
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o == null || o.getClass() != getClass()) {
			return false;
		}
		return this.id == ((Cupcake)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
