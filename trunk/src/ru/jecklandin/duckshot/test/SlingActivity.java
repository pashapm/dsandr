package ru.jecklandin.duckshot.test;

import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.SlingView;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class SlingActivity extends Activity {

	SlingView v;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v  = new SlingView(this);
        setContentView(v);
        ScrProps.initialize(this);
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


