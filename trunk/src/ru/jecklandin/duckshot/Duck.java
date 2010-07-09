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

	private int MAX_OFFSET = 300;
	private int MIN_OFFSET = 0;

	private int DUCK_ID = 0;
	static {
		
		Duck.duckBm = ImgManager.getBitmap("duck");
		Duck.deadDuckBm = ImgManager.getBitmap("deadduck");
		Duck.mAniDiving = ImgManager.getAnimation("duckdive");
		Duck.mAniEmerging = ImgManager.getAnimation("duckemerge");
		
	}

	// state
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
	
	private Stone mStone;

	private int id = DUCK_ID++;

	private static Bitmap duckBm;
	private static Bitmap deadDuckBm;
	private static Bitmap[] mAniDiving;
	private static Bitmap[] mAniEmerging;
	

	public Wave ownedWave;
	public int mScoreValue = 100;

	private Matrix emptyMatrix;
	private Matrix addit_m;
	

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
		
		if (offset == 150)
			dive();
		
		if (offset == 100)
			emerge();
 
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
		
		matrix.reset();
		if (mMovingRight) {
			matrix.setScale(-1, 1);
			matrix.postTranslate(duckBm.getWidth(), 0);
		}

		matrix.postTranslate(getNextOffset(offset), y - duckBm.getHeight() / 3);

		
		if (!isDead && mStone != null && mStone.mVector.y <= this.y) {
			if (isIntersects((int) mStone.mVector.x)) {
				isDead = true;
				mStone.makeFountain = false;
				DuckShotModel.getInstance().addScore(mScoreValue);
				DuckGame.s_instance.mVibro.vibrate(30);
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
		
//		c.drawCircle(this.offset - duckBm.getWidth()/3, y, 5, p);
//		c.drawCircle(this.offset + 4*duckBm.getWidth()/3 , y, 5, p);
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
		matrix.postTranslate(0, -10);
		if (diving_frame < 16) {
			c.drawBitmap(mAniDiving[diving_frame], matrix, p); 
			diving_frame++;
		} 
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
					y + deadDuckBm.getHeight() / 2 - 10);
			if (dead_degree > 160) {
				dead_sink++;
			}
		} else {
			matrix.postRotate(180, offset + deadDuckBm.getWidth() / 2, y
					+ deadDuckBm.getHeight() / 2 - 10);
			dead_sink += 1;
			matrix.postTranslate(0, dead_sink);
		}
 
		if (dead_sink > 10) { //have sink
			has_sink = true;
		} 
		
		c.drawBitmap(deadDuckBm, matrix, p);
	}

	private void drawScore(Canvas c, Paint p) {
		int alpha = 100+dead_sink;
		if (alpha < 40) {
			end_animation = true;
		}
		//p.setAlpha(alpha);
		dead_sink-=4;
		
		addit_m.reset();
		addit_m.setScale(0.6f, 0.6f);
		addit_m.postTranslate(this.offset, ownedWave.y + dead_sink);
		drawScoreDigits(c, p, addit_m, mScoreValue);
		//p.setAlpha(255);
	}

	
	
	private void drawScoreDigits(Canvas c, Paint p, Matrix mat, int score) {
		Bitmap[] bms = Desk.getDigits(score, DigitType.YELLOW);
		for (int i=0; i<bms.length; ++i) {
			mat.postTranslate(15, 0);
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
