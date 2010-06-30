package ru.jecklandin.duckshot;

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
	}
	
	public static Bitmap mDesk;
	public static Bitmap mIndent;
	public static Bitmap mPower;
	public static Bitmap mStub;
	public static Bitmap mPointer;
	public static Bitmap mPointerh;
	public static Bitmap mSight;
	
	private int power = 0;
	private int x = 0;
	
	private Matrix matrix = new Matrix();;
	Matrix addit_m = new Matrix();
	
	public void draw(Canvas c, Paint p) {
		int y = ScreenProps.screenHeight - mDesk.getHeight();
		int x = 5;
		matrix.reset();
		matrix.setTranslate(x, y);
		c.drawBitmap(mDesk, matrix, p);
		
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
		
		
		if (this.power != 0) {
			c.drawBitmap(mPointer, 0, DuckShotModel.getInstance().getYFromMsec(this.power), p);
			c.drawBitmap(Stone.mStone, this.x, ScreenProps.screenHeight - Stone.mStone.getHeight()/2, p);
			
			//this.is.sparta
		}
		
//		p.setColor(color)
//		c.drawText(String.valueOf(DuckShotModel.getInstance().getScore()), ScreenProps.screenWidth/2, 30, p);
		
		addit_m.reset();
		addit_m.setScale(1f, 1f); 
		addit_m.postTranslate(ScreenProps.screenWidth/2, 5);
		Bitmap[] bd = getScoreDigits(DuckShotModel.getInstance().getScore());
		for (int i=0; i<bd.length; ++i) {
			addit_m.postTranslate(30, 0);
			c.drawBitmap(bd[i], addit_m, p);	
		}
		
	}  
	
	private Bitmap[] getScoreDigits(int score) {
		String sc = String.valueOf(score);
		
		Bitmap[] bd = new Bitmap[sc.length()];
		for (int i=0; i<sc.length(); ++i)	{
			Bitmap dig = Duck.mDigits[ Integer.valueOf( sc.substring(i, i+1) ) ];
			bd[i] = dig;
		}
		return bd;
	}
	 
	public void setPowerIndicator(long msec, int x) {
		this.power = (int) msec;
		this.x = x;
	}
}
