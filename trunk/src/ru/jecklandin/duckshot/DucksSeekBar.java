package ru.jecklandin.duckshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class DucksSeekBar extends SeekBar {

	private static Bitmap mQuant;
	private static Bitmap mQuantE;
	
	static {
		DucksSeekBar.mQuant = ImgManager.getBitmap("quant");
		DucksSeekBar.mQuantE = ImgManager.getBitmap("quant_e");
	} 
	
	public DucksSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	} 
	 
	private Matrix mMatr = new Matrix();
	private Paint mPaint = new Paint();
	
	@Override   
	protected synchronized void onDraw(Canvas canvas) {
		mMatr.reset();
		mMatr.setTranslate(0, (getHeight()-mQuant.getHeight())/2);
		int quantplace = getWidth() / getMax(); 
		int quantPadding = (quantplace-mQuant.getWidth()) / 2;
		for (int i=0; i<getMax(); ++i) { 
			mMatr.postTranslate(quantPadding, 0);
			canvas.drawBitmap( i<getProgress() ? mQuant : mQuantE, mMatr, mPaint);
			mMatr.postTranslate(mQuant.getWidth() + quantPadding, 0);
		}  
	}

}
