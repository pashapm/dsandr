package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ViewAnimator;

public class MainMenu extends Activity {
	
	public static final String SHOW_RESUME = "ru.jecklandin.duckshot.SHOW_RESUME"; 
	public static final String SHOW_MAIN_MENU = "ru.jecklandin.duckshot.SHOW_MAIN_MENU"; 
	 
	private ViewAnimator mAnimator;
	private View mMl;
	private View mSl;
	private View mRl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ImgManager.loadImages(this);
		SoundManager.initialize(getApplicationContext());
		ScrProps.initialize(this);
		
		mMl = getLayoutInflater().inflate(R.layout.main, null);
		mSl = getLayoutInflater().inflate(R.layout.settings, null);
		mRl = getLayoutInflater().inflate(R.layout.resume, null);
		
		final ViewAnimator anim = new ViewAnimator(this);
		anim.setBackgroundResource(R.drawable.menubackt);
		mAnimator = anim;
		Animation ina = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		Animation outa = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
		anim.setInAnimation(ina);
		anim.setOutAnimation(outa);
		
		setContentView(anim);
		setStartMode();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mAnimator.getDisplayedChild() == 1) {
			mAnimator.showPrevious();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) { // BACK was pressed in game
			setResumeMode();
		}
	}
	
	private void setStartMode() {
		mAnimator.removeAllViews();
		LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mAnimator.addView(mMl, 0, pars);
		mAnimator.addView(mSl, 1, pars);
		
		ImageButton start = (ImageButton) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainMenu.this, DuckGame.class);
				i.setAction(DuckGame.NEW_MATCH);
				MainMenu.this.startActivityForResult(i, 0);
			}
		});
		
		ImageButton sett = (ImageButton) findViewById(R.id.settings);
		sett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAnimator.showNext();
			}
		});
	}

	private void setResumeMode() {
		LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mAnimator.removeAllViews();
		mAnimator.addView(mRl, 0, pars);
		mAnimator.addView(mSl, 1, pars);
		
		ImageButton resume = (ImageButton) findViewById(R.id.mresume);
		resume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainMenu.this, DuckGame.class);
				i.setAction(DuckGame.RESUME_MATCH);
				MainMenu.this.startActivityForResult(i, 0);
			}
		});
		
		ImageButton sett = (ImageButton) findViewById(R.id.msettings);
		sett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAnimator.showNext();
			}
		});
	}

	/**
	 * Called from LevelCompletedDialog
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getAction().equals(SHOW_MAIN_MENU)) {
			setStartMode();
		} else {
			setResumeMode();
		}
	}
	
}
