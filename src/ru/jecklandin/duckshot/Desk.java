package ru.jecklandin.duckshot;

import java.util.ArrayList;
import java.util.Vector;

import ru.jecklandin.duckshot.Match.Bonus;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Desk { 
	  
	private static Desk s_instance;
	public synchronized static Desk getInstance() {
		if (s_instance == null) {
			s_instance = new Desk();
		}  
		return s_instance;
	} 
	   
	static {
		Desk.mDesk = ImgManager.getBitmap("desk");
		Desk.mSight = ImgManager.getBitmap("sight");
		Desk.mDigits = ImgManager.getAnimation("digits");
		Desk.mAwards = ImgManager.getAnimation("awards");
	}
	
	public static Bitmap mDesk;
	public static Bitmap mStub;
	public static Bitmap mSight; 
	public static Bitmap mDoubleKill; 
	public static Bitmap mTripleKill; 
	public static Bitmap mQuadKill; 
	public static Bitmap mMaxiKill; 
	public static Bitmap[] mDigits;
	public static Bitmap[] mAwards;
	
	private Matrix matrix = new Matrix();;
	private Matrix addit_m = new Matrix();
	
	private boolean mShowSight = false;
	private int mSightX;
	private int mSightY;
	
	private static final int COMMON_FONT = 25;
	
	private Paint mTextPaint = new Paint();
	private Paint mBonusPaint = new Paint();
	
	private Bonus mBonus;
	private int mBonusFont = COMMON_FONT;
	
	public enum DigitType {YELLOW, RED, WHITE};
	
	public Desk() {
		super();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTypeface(DuckApplication.getCommonTypeface());
		mTextPaint.setTextSize(ScrProps.mMetrics.scaledDensity * COMMON_FONT);
		mTextPaint.setColor(Color.WHITE);
		
		mBonusPaint.setAntiAlias(true);
		mBonusPaint.setTypeface(DuckApplication.getCommonTypeface());
		mBonusPaint.setTextSize(ScrProps.mMetrics.scaledDensity * COMMON_FONT);
	}
	
	public void draw(Canvas c, Paint p) {
		int y = ScrProps.screenHeight - mDesk.getHeight();
		int x = 5;
		matrix.reset();
		
		matrix.setTranslate(x, y);
		c.drawBitmap(mDesk, matrix, p);
		
		drawAwards(c, p);
		
		// draw score
		addit_m.reset();
		addit_m.postTranslate(ScrProps.screenWidth/2, ScrProps.scale(5));
		Bitmap[] scoreBms = getDigits(DuckGame.getCurrentMatch().getScore(), DigitType.YELLOW);
		for (int i=0; i<scoreBms.length; ++i) {
			addit_m.postTranslate(ScrProps.scale(20), 0);
			c.drawBitmap(scoreBms[i], addit_m, p);	
		}
		
		//draw time
		addit_m.reset();
		addit_m.setTranslate(ScrProps.screenWidth*2/3, ScrProps.screenHeight - ScrProps.scale(100));
		int total = DuckGame.s_instance.mMatch.secondsRemain();
		mTextPaint.setColor(total <= 5 ? Color.RED : Color.WHITE);   
		int mins = total / 60;
		int secs = total % 60;
		String time_str = mins + " : " + secs;
		
		c.drawText(time_str, ScrProps.scale(20), 
				ScrProps.screenHeight - ScrProps.scale(80), mTextPaint);
		
		//draw sight
		if (mShowSight) {
			addit_m.reset();
			addit_m.setTranslate(mSightX-mSight.getWidth()/2, mSightY-mSight.getHeight()/2);
			c.drawBitmap(mSight, addit_m, p);
		}
		
		//draw bonus
		if (mBonus != null) {
			mBonusPaint.setTextSize(ScrProps.mMetrics.scaledDensity * mBonusFont);
			Rect rect = new Rect();
			String bs = mBonus.toString();
			mBonusPaint.getTextBounds(bs, 0, bs.length(), rect);
			int bx = (ScrProps.screenWidth - rect.right)/2;
			int by = ScrProps.screenHeight/2 - 20;
			c.drawText(mBonus.toString(), bx, by, mBonusPaint);
			mBonusFont+=1;
			if (mBonusFont > 40) {
				mBonusFont = COMMON_FONT;
				mBonus = null;
			}
		} 
	}  
	
	public static Bitmap[] getDigits(int score, DigitType type) {
		
		Bitmap[] digits = null;
		switch (type) {
		case YELLOW:  
			digits = Desk.mDigits;
			break;
		case RED:
			digits = Desk.mDigits;
			break;
		}
		
		String sc = String.valueOf(score);
		Bitmap[] bd = new Bitmap[sc.length()];
		for (int i=0; i<sc.length(); ++i)	{
			Bitmap dig = digits[ Integer.valueOf( sc.substring(i, i+1) ) ];
			bd[i] = dig;
		}
		return bd;
	}
	 
	private void drawAwards(Canvas c, Paint p) {
		ArrayList<Bonus> bons = DuckGame.getCurrentMatch().getAwards();
		matrix.postTranslate(ScrProps.scale(10), ScrProps.scale(10));
		
		int length = Bonus.values().length;
		int[] xses = new int[length-1]; //except for the template
		for (int i=0; i<xses.length; ++i) {
			if (i<xses.length/2) {
				xses[i]=ScrProps.scale(40)*i;
			} else {
				xses[i]=ScrProps.screenWidth - ScrProps.scale(40)*(xses.length-i+1);
			}
		}
		
		addit_m.set(matrix);
		for (int i=0; i<xses.length; ++i) {
			addit_m.set(matrix);
			addit_m.postTranslate(xses[i], 0);
			Bonus b = Bonus.values()[i+1];
			if (bons.contains(b)) {
				c.drawBitmap(mAwards[b.ordinal()], addit_m, p);
			} else {
				c.drawBitmap(mAwards[0], addit_m, p);
			}
		}
	} 
	
	public void setSight(int x, int y) {
		mSightX = x;
		mSightY = y;
	}
	
	public int getSightX() {
		return mSightX;
	}
	
	protected int getSightY() {
		return mSightY;
	}
	
	public void setSightVisibility(boolean vis) {
		mShowSight = vis;
	}
	
	public boolean getSightVisibility() {
		return mShowSight;
	}

	public void playBonus(Bonus bonus) {
		mBonus = bonus;
		mBonusPaint.setColor(bonus.getColor());
	}
	
	public static void drawScoreDigits(Canvas c, Paint p, Matrix mat, int score) {
		Bitmap[] bms = Desk.getDigits(score, DigitType.YELLOW);
		for (int i=0; i<bms.length; ++i) {
			mat.postTranslate(ScrProps.scale(15), 0);
			c.drawBitmap(bms[i], mat, p);	
		}
	}
}
