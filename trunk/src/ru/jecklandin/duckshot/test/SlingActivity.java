package ru.jecklandin.duckshot.test;



import ru.jecklandin.duckshot.ScreenProps;
import ru.jecklandin.duckshot.SlingView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SlingActivity extends Activity {

	SlingView v;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v  = new SlingView(this);
        setContentView(v);
        ScreenProps.initialize(this);
    }
	
	

    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if (true || event.getY() > 350) {
    		
    		v.sly = (int) event.getY();
    	}
    	v.slx = (int) event.getX();

		return super.onTouchEvent(event);
	}

}


