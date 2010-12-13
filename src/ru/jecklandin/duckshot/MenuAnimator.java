package ru.jecklandin.duckshot;

import java.util.Formatter.BigDecimalLayoutForm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.widget.ViewAnimator;

public class MenuAnimator extends ViewAnimator {

	private Bitmap mBg1;
	private Matrix mMatrix = new Matrix();
	private Paint mPaint = new Paint();
	
	public MenuAnimator(Context context) {
		super(context);
		BitmapFactory.Options opts = new Options();
		opts.inDither = true;
		opts.inPreferredConfig = Config.RGB_565;
		mBg1 = BitmapFactory.decodeResource(getResources(), R.drawable.menubackt, opts);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mBg1, mMatrix, mPaint);
	}

}
