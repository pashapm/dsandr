package ru.jecklandin.duckshot;

import java.util.Vector;

import ru.jecklandin.duckshot.Match.Bonus;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
		Desk.mIndent = ImgManager.getBitmap("indent");
		Desk.mPower = ImgManager.getBitmap("power");
		Desk.mStub = ImgManager.getBitmap("stub");
		Desk.mPointer = ImgManager.getBitmap("pointer");
		Desk.mPointerh = ImgManager.getBitmap("pointerh");
		Desk.mSight = ImgManager.getBitmap("sight");
		Desk.mLeftPanel = ImgManager.getBitmap("pl_yell");
		Desk.mRightPanel = ImgManager.getBitmap("pl_green");
		Desk.mDigits = ImgManager.getAnimation("digits");
		Desk.mDigitsTime = ImgManager.getAnimation("digits_time");
		Desk.mAwards = ImgManager.getAnimation("awards");
	}
	
	public static Bitmap mDesk;
	public static Bitmap mIndent;
	public static Bitmap mPower;
	public static Bitmap mStub;
	public static Bitmap mPointer;
	public static Bitmap mPointerh;
	public static Bitmap mSight; 
	public static Bitmap mLeftPanel;
	public static Bitmap mRightPanel; 
	public static Bitmap[] mDigitsTime;
	public static Bitmap[] mDigits;
	public static Bitmap[] mAwards;
	
	private int power = 0;
	private int x = 0;
	
	private Matrix matrix = new Matrix();;
	private Matrix addit_m = new Matrix();
	
	private boolean mShowSight = false;
	private int mSightX;
	private int mSightY;
	
	public enum DigitType {YELLOW, RED, WHITE};
	
	public void draw(Canvas c, Paint p) {
		int y = ScrProps.screenHeight - mDesk.getHeight();
		int x = 5;
		matrix.reset();
		
//		matrix.postTranslate(5, 5);
//		c.drawBitmap(mLeftPanel, matrix, p);
//		
//		matrix.setTranslate(ScrProps.screenWidth-mRightPanel.getWidth()-5, 5);
//		c.drawBitmap(mRightPanel, matrix, p);
		
		matrix.setTranslate(x, y);
		c.drawBitmap(mDesk, matrix, p);
		
		drawAwards(c, p);
		
//		//draw indent
//		matrix.postTranslate(mDesk.getWidth() - mIndent.getWidth() - 20, (mDesk.getHeight() - mIndent.getHeight())/2);
//		c.drawBitmap(mIndent, matrix, p);
//		
//		//draw power
//		matrix.postTranslate(5,5); 
//		if (this.power != 0) {
//			addit_m.reset();
//			c.drawBitmap(mPower, matrix, p);	
//			float part = 1 - ((float)this.power / DuckShotModel.MAX_MSEC);
//			part = part < 0 ? 0 : part;
//			addit_m.setScale(part, 1); 
//			addit_m.postConcat(matrix); 
//			c.drawBitmap(mStub, addit_m, p);	
//			
//		} 
		
		
//		if (this.power != 0) {
//			c.drawBitmap(mPointer, 0, DuckShotModel.getInstance().getYFromMsec(this.power), p);
//			c.drawBitmap(Stone.mStone, this.x, ScreenProps.screenHeight - Stone.mStone.getHeight()/2, p);
//			//this.is.sparta
//		}
		
//		p.setColor(color)
//		c.drawText(String.valueOf(DuckShotModel.getInstance().getScore()), ScreenProps.screenWidth/2, 30, p);
		
		
		// draw score
		addit_m.reset();
//		addit_m.postTranslate(0, ScrProps.screenHeight - ScrProps.scale(100));
		addit_m.postTranslate(ScrProps.screenWidth/2, ScrProps.scale(5));
		Bitmap[] scoreBms = getDigits(DuckGame.getCurrentMatch().getScore(), DigitType.YELLOW);
		for (int i=0; i<scoreBms.length; ++i) {
			addit_m.postTranslate(ScrProps.scale(20), 0);
			c.drawBitmap(scoreBms[i], addit_m, p);	
		}
		
		//draw time
		addit_m.reset();
		addit_m.setTranslate(ScrProps.screenWidth*2/3, ScrProps.screenHeight - ScrProps.scale(100));
		int sec = DuckGame.s_instance.mMatch.secondsRemain();
		Bitmap[] minBms = getDigits(sec/60, DigitType.WHITE);
		for (int i=0; i<minBms.length; ++i) {
			addit_m.postTranslate(ScrProps.scale(20), 0);
			c.drawBitmap(minBms[i], addit_m, p);	
		}
		addit_m.postTranslate(ScrProps.scale(20), 0);
		c.drawBitmap(Desk.mDigitsTime[Desk.mDigitsTime.length-1], addit_m, p);	
		Bitmap[] secBms = getDigits(sec%60, DigitType.WHITE);
		for (int i=0; i<secBms.length; ++i) {
			addit_m.postTranslate(ScrProps.scale(15), 0);
			c.drawBitmap(secBms[i], addit_m, p);	
		}
		
		//draw sight
		if (mShowSight) {
			addit_m.reset();
			addit_m.setTranslate(mSightX-mSight.getWidth()/2, mSightY-mSight.getHeight()/2);
			c.drawBitmap(mSight, addit_m, p);
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
		case WHITE:
			digits = Desk.mDigitsTime;
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
		Vector<Bonus> bons = DuckGame.getCurrentMatch().getAwards();
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
		
//		Matrix awMat = new Matrix(matrix);
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
	
	@Deprecated
	public void setPowerIndicator(long msec, int x) {
		this.power = (int) msec;
		this.x = x;
	}
	
	public void setSight(int x, int y) {
		mSightX = x;
		mSightY = y;
	}
	
	public void setSightVisibility(boolean vis) {
		mShowSight = vis;
	}
}
