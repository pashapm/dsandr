package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.utils.FpsCounter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Debug;
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
		
//		Debug.startMethodTracing("ducks");
		 
		//antialiasing for bitmaps 
		PaintFlagsDrawFilter setfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(setfil);  
   	
     	mDrawer.drawObjects(canvas);
 		FpsCounter.notifyDrawing();
 		
// 		Debug.stopMethodTracing();
  	}    
}
