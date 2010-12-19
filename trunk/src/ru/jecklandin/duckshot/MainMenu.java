package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewAnimator;

public class MainMenu extends Activity implements OnClickListener {
	
	public static final String SHOW_RESUME = "ru.jecklandin.duckshot.SHOW_RESUME"; 
	public static final String SHOW_MAIN_MENU = "ru.jecklandin.duckshot.SHOW_MAIN_MENU"; 
	public static final String SHOW_SETTINGS = "ru.jecklandin.duckshot.SHOW_SETTINGS"; 
	 
	private ViewAnimator mAnimator;
	private View mMl;
	private View mSl;
	private View mRl;
	
	DucksSeekBar mSoundBar;
	DucksSeekBar mEffectsBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrProps.initialize(this);
		SoundManager.initialize(getApplicationContext());
		ImgManager.loadImages(this);	
		
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
		
		mSoundBar = ((DucksSeekBar) findViewById(R.id.soundbar));
		mEffectsBar = ((DucksSeekBar) findViewById(R.id.effectsbar));
		
		((ImageButton) findViewById(R.id.soundon)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.soundoff)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.effectson)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.effectsoff)).setOnClickListener(this);
		
		restoreSettings();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mAnimator.getDisplayedChild() == 1) {
			commitSettings();
			SoundManager.getInstance().readSettings();
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
		start.setOnClickListener(this);
		
		ImageButton sett = (ImageButton) findViewById(R.id.settings);
		sett.setOnClickListener(this);
		
		ImageButton more = (ImageButton) findViewById(R.id.more);
		more.setOnClickListener(this);
	}

	private void setResumeMode() {
		LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mAnimator.removeAllViews();
		mAnimator.addView(mRl, 0, pars);
		mAnimator.addView(mSl, 1, pars);
		
		ImageButton resume = (ImageButton) findViewById(R.id.mresume);
		resume.setOnClickListener(this);
		
		ImageButton sett = (ImageButton) findViewById(R.id.msettings);
		sett.setOnClickListener(this);
		
		ImageButton newg = (ImageButton) findViewById(R.id.mnewgame);
		newg.setOnClickListener(this);
	}

	/**
	 * Called from LevelCompletedDialog
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		
		if (intent.getAction() == null) {
			return;
		}
		
		if (intent.getAction().equals(SHOW_MAIN_MENU)) {
			setStartMode();
		} else if (intent.getAction().equals(SHOW_SETTINGS)) {
			setResumeMode();
			mAnimator.showNext();
		} else {
			setResumeMode();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.msettings:
		case R.id.settings:
			mAnimator.showNext();
			break;
		case R.id.mresume:
			Intent i = new Intent(MainMenu.this, DuckGame.class);
			i.setAction(DuckGame.RESUME_MATCH);
			MainMenu.this.startActivityForResult(i, 0);
			break;
		case R.id.start:
		case R.id.mnewgame:
			Intent i1 = new Intent(MainMenu.this, DuckGame.class);
			i1.setAction(DuckGame.NEW_MATCH);
			MainMenu.this.startActivityForResult(i1, 0);
			break;
		case R.id.more:
			Intent i2 = new Intent(this, MoreScreen.class);
			startActivity(i2);
			break;
		case R.id.soundon:
			mSoundBar.setProgress(8);
			break;
		case R.id.soundoff:
			mSoundBar.setProgress(0);
			break;
		case R.id.effectson:
			mEffectsBar.setProgress(8);
			break;
		case R.id.effectsoff:
			mEffectsBar.setProgress(0);
			break;
		default:
			break;
		}
	}
	
	private void commitSettings() {
		int sound = mSoundBar.getProgress();
		int effects = mEffectsBar.getProgress();
		SharedPreferences prefs = getSharedPreferences("ducks", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("sound", sound);
		editor.commit();
		editor.putInt("effects", effects);
		editor.commit();
	}
	
	private void restoreSettings() {
		SharedPreferences prefs = getSharedPreferences("ducks", Context.MODE_PRIVATE);
		mSoundBar.setProgress(prefs.getInt("sound", 4));
		mEffectsBar.setProgress(prefs.getInt("effects", 4));
	}
	
}
