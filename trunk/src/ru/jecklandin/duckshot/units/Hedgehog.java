package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.DuckGame;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.Match.Bonus;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Hedgehog extends CreatureObject {

	private int dead_stage;
	
	public Hedgehog(int x) {
		super(); 
		this.offset = x; 
		speed = 2;
		addit_m = new Matrix();
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
			drawNormal(c, p);
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
		c.drawBitmap(ImgManager.getBitmap("spirit"), matrix, p);
		drawScore(c, p);
		sp-=4;
		if (sp < -52) {
			end_animation = true;
		}
	}
	
	public void setOwnedWave(GroundObject ground, int xa) {
		mScoreValue = 50 + 10*(DuckShotModel.getInstance().mGrounds.size() - 1 - ground.wave_num);
		super.setOwnedWave(ground, xa);
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
