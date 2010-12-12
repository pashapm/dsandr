package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.Desk.DigitType;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
   
public class Duck extends GameObject {

	private static final String TAG = "ru.jecklandin.duckshot.Duck";
	private int MAX_OFFSET = ScrProps.scale(300);
	private int MIN_OFFSET = 0;
  
	static {
		Duck.duckBm = ImgManager.getBitmap("duck");
		Duck.deadDuckBm = ImgManager.getBitmap("deadduck");
		Duck.mAniDiving = ImgManager.getAnimation("duckdive");
		Duck.mAniEmerging = ImgManager.getAnimation("duckemerge");
	}


	
	private Stone mStone;

	private static Bitmap duckBm;
	private static Bitmap deadDuckBm;
	private static Bitmap[] mAniDiving;
	private static Bitmap[] mAniEmerging;
	

	public Wave ownedWave;
	public int mScoreValue = 100;

	private Matrix emptyMatrix;
	private Matrix addit_m;
	
	// ===============   state
	private boolean mMovingRight = true;

	private boolean isDiving = false;
	private int diving_frame = 0;

	private boolean isEmerging = false;
	private int emerging_frame = 0;

	private boolean isDead = false;
	private int dead_degree = 0;
	private int dead_sink = 0;
	private boolean has_sink = false;
	private boolean end_animation = false;
	
	private int timeout = 0;
	
	private int ticksBeforeNextDive = generateNextDive();
	private int ticksBeforeNextRotate = generateNextRotate();
	
	// ===============   \state
	
	/**
	 * set to true to move duck to another wave
	 */
	public boolean mMoveFlag = false;
	

	public Duck(int x) {
		super();
		this.offset = x; 
		speed = 1;
		matrix = new Matrix();
		emptyMatrix = new Matrix();
		addit_m = new Matrix();
	}

	@Override
	public float getNextOffset(float curOffset) {
		if (isDead) {
			return offset;
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
		
		
		
//		if (offset == ScrProps.scale(150))
//			dive();
//		
//		if (offset == ScrProps.scale(100))
//			emerge();
 
		if (offset < MIN_OFFSET) {
			mMovingRight = true;
		} else if (offset > MAX_OFFSET) {
			mMovingRight = false;
		}
		return offset;
	}
 
	@Override
	public OBJ_TYPE getRtti() {
		return OBJ_TYPE.DUCK;
	}

	@Override 
	public void draw(Canvas c, Paint p) {
		if (end_animation) {
			return;
		}
		
		if (timeout > 0) {
			timeout--;
			return;
		}
		
		matrix.reset();
		if (mMovingRight) {
			matrix.setScale(-1, 1);
			matrix.postTranslate(duckBm.getWidth(), 0);
		}

		matrix.postTranslate(getNextOffset(offset), y - duckBm.getHeight() / 2);

		
		if (!isDead && mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				isDead = true;
				SoundManager.getInstance().play();
				mStone.makeFountain = false;
				DuckGame.getCurrentMatch().addScore(mScoreValue);
				DuckGame.getVibrator().vibrate(30);
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
 
	public void move() {
		int distance = moveToANotherWave();
		timeout = DuckShotModel.getInstance().getTimeoutByDistance(distance);
		mMoveFlag = false;
		 
		ticksBeforeNextDive = generateNextDive();
		double rnd = Math.random();
		mMovingRight = rnd < 0.5;   
		
		Log.d(TAG, "timeout: "+timeout+", ticksBeforeNextDive: "+ticksBeforeNextDive+" ,movingRight: "+mMovingRight);
	}
	
	private void rotate() {
		mMovingRight = !mMovingRight;
		ticksBeforeNextRotate = generateNextRotate();
	}
	
	/**
	 * from 20 to 300 ticks 
	 * @return
	 */
	private int generateNextDive() {
		return (int) (Math.random()*280+20);
	}
	
	/**
	 * from 20 to 200 ticks 
	 * @return
	 */
	private int generateNextRotate() {
		return (int) (Math.random()*180+20);
	}
	
	private int moveToANotherWave() {
		return DuckShotModel.getInstance().moveDuckToRandomWave(this);
	}
	
	private void drawNormal(Canvas c, Paint p) {
		c.drawBitmap(duckBm, matrix, p);
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
 
		if (dead_sink > ScrProps.scale(10)) { //have sink
			has_sink = true;
		} 
		
		c.drawBitmap(deadDuckBm, matrix, p);
	}

	private void drawScore(Canvas c, Paint p) {
		int alpha = ScrProps.scale(100)+dead_sink;
		if (alpha < ScrProps.scale(40)) {
			end_animation = true;
		}
		//p.setAlpha(alpha);
		dead_sink-=ScrProps.scale(4);
		
		addit_m.reset();
		addit_m.setScale(0.6f, 0.6f);
		addit_m.postTranslate(this.offset, ownedWave.y + ScrProps.scale(dead_sink));
		drawScoreDigits(c, p, addit_m, mScoreValue);
		//p.setAlpha(255);
	}

	
	
	private void drawScoreDigits(Canvas c, Paint p, Matrix mat, int score) {
		Bitmap[] bms = Desk.getDigits(score, DigitType.YELLOW);
		for (int i=0; i<bms.length; ++i) {
			mat.postTranslate(ScrProps.scale(15), 0);
			c.drawBitmap(bms[i], mat, p);	
		}
	}
	
	private boolean isIntersects(int ix) {
		return (!isDiving 
				&& ix > (this.offset - duckBm.getWidth()/3) 
				&& ix < (this.offset + 4*duckBm.getWidth()/3));
	}

	public void throwStone(Stone stone) {
		mStone = stone;
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

	@Override
	public boolean equals(Object o) {
		return this.id == ((Duck)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
	 
}
