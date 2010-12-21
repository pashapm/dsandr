package ru.jecklandin.duckshot;

import java.util.ArrayList;

import ru.jecklandin.duckshot.Match.Bonus;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AwardsView extends View {

	protected ArrayList<Bonus> mAwards;
	
	private Matrix mMatrix = new Matrix();
	private Paint mPaint = new Paint();
	
	public AwardsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (mAwards != null) {
			mMatrix.setTranslate(ScrProps.scale(10), ScrProps.scale(10));
			for (Bonus b : mAwards) {
				canvas.drawBitmap(Desk.mAwards[b.ordinal()], mMatrix, mPaint);
				mMatrix.postTranslate(ScrProps.scale(20), 0);
			}
		}
	}
	
}
