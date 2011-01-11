package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.DuckApplication;
import ru.jecklandin.duckshot.DuckGame;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.Match;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SoundManager;
import ru.jecklandin.duckshot.Stone;
import ru.jecklandin.duckshot.Desk.DigitType;
import ru.jecklandin.duckshot.Match.Bonus;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
   
public class Duck extends CreatureObject {

	private static final String TAG = "ru.jecklandin.duckshot.Duck";
	private int MAX_OFFSET = ScrProps.scale(300);
	private int MIN_OFFSET = 0;
  
	static {
		CreatureObject.commonBm = ImgManager.getBitmap("duck");
		Duck.deadDuckBm = ImgManager.getBitmap("deadduck");
		Duck.mAniDiving = ImgManager.getAnimation("duckdive");
		Duck.mAniEmerging = ImgManager.getAnimation("duckemerge");
	}

	private static Bitmap deadDuckBm;
	private static Bitmap[] mAniDiving;
	private static Bitmap[] mAniEmerging;
	
	private Matrix addit_m;
	
	// ===============   state

	private boolean isDiving = false;
	private int diving_frame = 0;

	private boolean isEmerging = false;
	private int emerging_frame = 0;

	private boolean isDead = false;
	private int dead_degree = 0;
	private int dead_sink = 0;
	private boolean has_sink = false;
	private boolean end_animation = false;
	
	private int delay = 0;
	
	private int overallTicks = 0;
	
	public Duck(int x) {
		super(); 
		this.offset = x; 
		speed = 2;
		addit_m = new Matrix();
	}

	@Override
	public float getNextOffset(float curOffset) {
		if (isDead) {
			return offset;
		}
		
		overallTicks++;
		if (Desk.getInstance().getSightVisibility() 
				&& overallTicks % (DuckApplication.FPS) == 0 && !isDiving) {
			if (isDanger()) {  //the sight is nearby!
				dive();
			}
		}
		
		if (mMovingRight) {
			offset += speed;
		} else {
			offset -= speed;
		}
		
		if (--ticksBeforeNextDive < 0) {
			dive();
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
		matrix.postTranslate(next_offset, y - commonBm.getHeight() / 4);

		
		if (!isDead && mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				
				DuckGame.getVibrator().vibrate(30);
				mStone.makeFountain = false;
				mHealth -= 50;
				
				addValue(mScoreValue);
				if (mHealth <=0) {
					isDead = true;
					SoundManager.getInstance().playHit();
					DuckGame.getCurrentMatch().requestNextDuckIfNeed();
					Bonus bonus = DuckGame.getCurrentMatch().addKilledDuck(this);
					if (bonus != Bonus.NO) {
						Desk.getInstance().playBonus(bonus);
					}
					DuckGame.getCurrentMatch().addScore((int) (mSumValues *= bonus.getMultiplier()));
				} else {
					SoundManager.getInstance().playQuack();
					dive();
				}
				
			}
			mStone = null;
		}
		
		if (isDead) {
			drawDeadAnimation(c, p);
		} else {
			if (isDiving) {
				if (isEmerging) {
					drawEmerging(c, p);
				} else {
					drawDiving(c, p); 	
				}
			} else {
				drawNormal(c, p);
			}
		}
	}

	private void addValue(int val) {
		mSumValues += val;
	}
	
	private void drawEmerging(Canvas c, Paint p) {
		if (emerging_frame < 8) {
			c.drawBitmap(mAniEmerging[emerging_frame], matrix, p); 
			emerging_frame++;
		} else {
			isEmerging = false;
			isDiving = false;
			drawNormal(c, p);
		}
	}

	private void drawDiving(Canvas c, Paint p) {
		matrix.postTranslate(0, ScrProps.scale(-10));
		if (diving_frame < 16) {
			c.drawBitmap(mAniDiving[diving_frame], matrix, p); 
			diving_frame++;
		}  else {
			emerge();
			mMoveFlag  = true;
		}
	}
 
	@Override
	protected void drawNormal(Canvas c, Paint p) {
		c.drawBitmap(commonBm, matrix, p);
		float[] point = new float[] {0, 0};   
		matrix.mapPoints(point);
		drawHealth(point, c, p);
	}
	
	private void drawDeadAnimation(Canvas c, Paint p) {
		
		if (has_sink) {
			drawScore(c, p);
			return;
		}
		
		if (dead_sink == 0) {
			dead_degree += 15;
			matrix.postRotate(dead_degree, offset + deadDuckBm.getWidth() / 2,
					y + deadDuckBm.getHeight() / 3 - ScrProps.scale(10));
			if (dead_degree > 160) {
				dead_sink++;
			}
		} else {
			matrix.postRotate(180, offset + deadDuckBm.getWidth() / 2, y
					+ deadDuckBm.getHeight() / 3 - ScrProps.scale(10));
			dead_sink += 1;
			matrix.postTranslate(0, dead_sink);
		}
 
		if (dead_sink > ScrProps.scale(15)) { //have sink
			has_sink = true;
		} 
		
		matrix.postTranslate(0, ScrProps.scale(10));
		c.drawBitmap(deadDuckBm, matrix, p);
	}

	private void drawScore(Canvas c, Paint p) {
		int alpha = ScrProps.scale(100)+dead_sink;
		if (alpha < ScrProps.scale(40)) {
			end_animation = true;
		}
		dead_sink-=ScrProps.scale(4);
		
		addit_m.reset();
		addit_m.setScale(0.6f, 0.6f);
		addit_m.postTranslate(this.offset, ownedWave.y + ScrProps.scale(dead_sink));
		Desk.drawScoreDigits(c, p, addit_m, mSumValues);
	}

	@Override
	protected boolean isIntersects(int ix) {
		return !isDiving && super.isIntersects(ix);
	}
	

	public void dive() {
		isDiving = true;
		isEmerging = false;
		emerging_frame = 0;
	}
	
	public void emerge() {
		isEmerging = true;
		diving_frame = 0;
	}

	public void setRandomDelay() {
		delay = (int) (Math.random() * 4 * DuckApplication.FPS);
	}
	
	@Override
	public boolean equals(Object o) {
		return this.id == ((Duck)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
	public void setOwnedWave(GroundObject ground, int xa) {
		mScoreValue = 50 + 10*(DuckShotModel.getInstance().mWaves.size() - 1 - ground.wave_num);
		isDiving = true;
		emerge();
		super.setOwnedWave(ground, xa);
		setRandomDelay();
	}
	 
}
