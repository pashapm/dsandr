package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.utils.FpsCounter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameField extends View {


 
	private ObjectDrawer mDrawer = ObjectDrawer.getInstance(getContext());
	
	public GameField(Context context) {
		super(context);
		setBackgroundColor(Color.parseColor("#6b98df"));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		 
		//antialiasing for bitmaps 
		PaintFlagsDrawFilter setfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(setfil);  
		
    	long c = System.currentTimeMillis();
    	
    	ObjectDrawer.lock = true;
    	mDrawer.drawObjects(canvas);
    	ObjectDrawer.lock = false;
//    	Log.d("FRAME:", "" + (System.currentTimeMillis() - c));
		
//		Log.d("LAAAATE", "late:"+late);
		FpsCounter.notifyDrawing();
		//invalidate();
  	}    
 
	private long time_pressed = 0;

	public void touch(MotionEvent event) {
 	
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			time_pressed = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			int time_elapsed = (int) (System.currentTimeMillis() - time_pressed);
			time_elapsed = time_elapsed < DuckShotModel.MAX_MSEC ? time_elapsed : DuckShotModel.MAX_MSEC;
			Desk.getInstance().setPowerIndicator(time_elapsed, (int) event.getX());
			//Log.d("qq", time_elapsed+"");
			break;
		case MotionEvent.ACTION_UP:
			Desk.getInstance().setPowerIndicator(0, (int) event.getX());
			time_elapsed = (int) (System.currentTimeMillis() - time_pressed);
			//normalizing
			Log.d("RES", time_elapsed+"");
			time_elapsed = time_elapsed < DuckShotModel.MAX_MSEC ? time_elapsed : DuckShotModel.MAX_MSEC;
			DuckShotModel.getInstance().launchStone((int) event.getX(), time_elapsed);
			break;
		default:
			break;
		}
	}
	
}
