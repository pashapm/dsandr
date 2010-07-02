package ru.jecklandin.duckshot;


import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.utils.FpsCounter;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DuckGame extends Activity {
    
	private static final String TAG = "DuckGame";
	
	GameField gf;
    Vibrator mVibro;
    Typeface mTypeface;
    DuckTimer timer;
    FPSPrinter fpspr;
    SlingView sling;
    
    boolean mShownDialog = false;
    
    SFGameField sf;
    
    static DuckGame s_instance;	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DuckGame.s_instance = this;
        mVibro = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ScreenProps.initialize(this);
		ImgManager.loadImages(this);
        gf = new GameField(this);
        
//        sf = new SFGameField(this);
       
        
        timer = new DuckTimer(gf);
        setContentView(gf);
        
        sling = new SlingView(this);
        getWindow().addContentView(sling, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        
        //debug btn
        LinearLayout lay = (LinearLayout)View.inflate(this, R.layout.btns, null);
        ImageButton imb = (ImageButton) lay.findViewById(R.id.ImageButton01);
        getWindow().addContentView(lay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        imb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DuckShotModel.getInstance().addRandomDuck();
				
			}
		}); 

        fpspr = new FPSPrinter();
        
        timer.start();
        fpspr.start();
        
        mTypeface = Typeface.createFromAsset(getAssets(), "Whypo.ttf");
        

		
		
        
    }

	@Override
	protected void onPause() {
		timer.setRunning(false);
		fpspr.setRunning(false);
		super.onPause();
	}

	@Override
	protected void onStop() {
		timer.setRunning(false);
		fpspr.setRunning(false);
		super.onStop();
	} 

	@Override
	protected void onResume() {
		timer.setRunning(!mShownDialog);
		fpspr.setRunning(!mShownDialog);
		super.onResume();
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		gf.touch(event);
//		return super.onTouchEvent(event);
//	}
	
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_MOVE) {  
    		sling.setXY(event.getX(), event.getY());
    	} else if (event.getAction() == MotionEvent.ACTION_UP) {
    		sling.shot((int)event.getX(), (int)event.getY()); 
    	}
    	


		return super.onTouchEvent(event);
	}  
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			showPauseDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showPauseDialog() {
		Handler han = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.d(TAG, "!!!!!!!!!");
				timer.setRunning(true);
				fpspr.setRunning(true);
				mShownDialog = false;
			}
		};
		
		timer.setRunning(false);
		fpspr.setRunning(false);
		mShownDialog = true;
		PauseDialog.show(this, han);
	}

} 

class DuckTimer extends Thread {
	
	View m_view;
	boolean mRunning = true;
	
	DuckTimer(View v) {
		m_view = v;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {

			if (mRunning) {
				ObjectDrawer.sLastTick = System.currentTimeMillis();
				FpsCounter.notifyDrawing();
				m_view.postInvalidate();
			}
			try {
				sleep(50);
			} catch (InterruptedException e) {
				Log.d("DuckTimer", "Stopping");
				// e.printStackTrace();
				return;
			}

		}
	}

	public void setRunning(boolean run) {
		mRunning = run;
	}
}

class FPSPrinter extends Thread {

	boolean mRunning = true;

	@Override
	public void run() {
		while (!isInterrupted()) {
			if (mRunning) {
				Log.d("Ducks ### FPS", FpsCounter.getFPS() + "");
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				Log.d("FPS", "Stopping");
				return;
			}

		}

	}

	public void setRunning(boolean run) {
		mRunning = run;
	}
}