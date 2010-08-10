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
	private ViewAnimator mAnimator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ImgManager.loadImages(this);
		ScreenProps.initialize(this);
		
		View ml = getLayoutInflater().inflate(R.layout.main, null);
		View sl = getLayoutInflater().inflate(R.layout.settings, null);
		 
		final ViewAnimator anim = new ViewAnimator(this);
		anim.setBackgroundResource(R.drawable.menubackt);
		mAnimator = anim;
		
		LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		Animation ina = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		Animation outa = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
		anim.setInAnimation(ina);
		anim.setOutAnimation(outa);
		
		anim.addView(ml, 0, pars);
		anim.addView(sl, 1, pars);
		
		setContentView(anim);
		
		ImageButton start = (ImageButton) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainMenu.this, DuckGame.class);
				MainMenu.this.startActivityForResult(i, 0);
			}
		});
		
		ImageButton sett = (ImageButton) findViewById(R.id.settings);
		sett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				anim.showNext();
			}
		});
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
		Log.d("!!!!!", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!W");
		if (resultCode == Activity.RESULT_CANCELED) {
			View res = getLayoutInflater().inflate(R.layout.resume, null);
			View sl = getLayoutInflater().inflate(R.layout.settings, null);
			LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			mAnimator.removeAllViews();
			mAnimator.addView(res, 0, pars);
			mAnimator.addView(sl, 1, pars);
			
			ImageButton sett = (ImageButton) findViewById(R.id.settings);
			sett.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mAnimator.showNext();
				}
			});
		}
	}

	
}
