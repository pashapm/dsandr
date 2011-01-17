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

public class Hedgehog extends CreatureObject {

	private int dead_stage;
	
	private static Bitmap mAngel;
	private static Bitmap[] mHidingAnim;
	private static Bitmap[] mAppearingAnim;
	
	public Hedgehog(int x) {
		super(); 
		this.offset = x; 
		speed = 2.5f;
		addit_m = new Matrix();
		
		mHealth = 120;
	}

	@Override
	protected void drawNormal(Canvas c, Paint p) {
		c.drawBitmap(commonBm, matrix, p);
		float[] point = new float[] {0, 0};   
		matrix.mapPoints(point);
		drawHealth(point, c, p);
	}

	public static void initBitmaps() {
		CreatureObject.commonBm = ImgManager.getBitmap("creature");
		Hedgehog.mAngel = ImgManager.getBitmap("spirit");
		Hedgehog.mHidingAnim = ImgManager.getAnimation("hide");
		Hedgehog.mAppearingAnim = ImgManager.getAnimation("appear");
	}
	
	@Override
	protected float generateNextSpeed() {
		return (float) (Math.random()*2.5+1);
	}
	
	@Override
	public float getNextOffset(float curOffset) {
		if (isDead) {
			return offset;
		}
		
		overallTicks++;
		if (Desk.getInstance().getSightVisibility() 
				&& overallTicks % (DuckApplication.FPS) == 0) {
			if (isDanger()) {  //the sight is nearby!
				rotate();
			}
		}
		
		if (mMovingRight) {
			offset += speed;
		} else {
			offset -= speed;
		}
		
		if (--ticksBeforeNextDive < 0) {
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
			}
			mStone = null;
		}
		
		
		
		if (isDead) {
			drawDeadAnimation(c, p);
		} else {
			if (isHidden) {
				if (isAppearing) {
					drawEmerging(c, p);
				} else {
					drawHiding(c, p); 	
				}
			} else {
				drawNormal(c, p);
			}
		}
	}
	
	@Override
	public void handleHit(int hps) {
		SoundManager.getInstance().vibrate(30);
		mStone.makeFountain = false;
		mHealth -= hps;
		 
		addValue(mScoreValue);
		if (mHealth <=0) {
			isDead = true;
			SoundManager.getInstance().playHit();
			DuckGame.getCurrentMatch().requestNextCreatureIfNeed();
			Bonus bonus = DuckGame.getCurrentMatch().addKilledCreature(this);
			if (bonus != Bonus.NO) {
				Desk.getInstance().playBonus(bonus);
			}
			DuckGame.getCurrentMatch().addScore((int) (mSumValues *= bonus.getMultiplier()));
		} else {
			SoundManager.getInstance().playScream();
		}  
	}
	
	private void drawScore(Canvas c, Paint p) {
		dead_stage-=ScrProps.scale(4);
		
		addit_m.reset();
		addit_m.setScale(0.6f, 0.6f);
		addit_m.postTranslate(this.offset, ownedGround.y + ScrProps.scale(dead_stage));
		Desk.drawScoreDigits(c, p, addit_m, mSumValues);
	}
	
	int sp = 0;
	
	private void drawDeadAnimation(Canvas c, Paint p) {
		matrix.postTranslate(0, sp);
		c.drawBitmap(mAngel, matrix, p);
		drawScore(c, p);
		sp-=4;
		if (sp < -80) { 
			end_animation = true;
		}
	}
	
	private void drawEmerging(Canvas c, Paint p) {
		if (emerging_frame < 8) {
			c.drawBitmap(mAppearingAnim[emerging_frame], matrix, p); 
			emerging_frame++;
		} else {
			isAppearing = false;
			isHidden = false;
			drawNormal(c, p);
		}
	}

	private void drawHiding(Canvas c, Paint p) {
		matrix.postTranslate(0, ScrProps.scale(-10));
		if (diving_frame < 8) {
			c.drawBitmap(mHidingAnim[diving_frame], matrix, p); 
			diving_frame++;
		}  else {
			appear();
			mMoveFlag  = true;
		}
	}
	
	public void setOwnedWave(GroundObject ground, int xa) {
		mScoreValue = 50 + 10*(DuckShotModel.getInstance().mGrounds.size() - 1 - ground.wave_num);
		super.setOwnedWave(ground, xa);
	}
	
	public void setRandomDelay() {
		delay = (int) (Math.random() * 4 * DuckApplication.FPS);
	}
	
	
	@Override
	public boolean equals(Object o) {
		if ( o == null || o.getClass() != getClass()) {
			return false;
		}
		return this.id == ((Hedgehog)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
