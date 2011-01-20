package ru.jecklandin.duckshot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.flurry.android.FlurryAgent;
import com.nullwire.trace.ExceptionHandler;

public class MainMenu extends Activity implements OnClickListener {
	
	public static final String SHOW_RESUME = "ru.jecklandin.duckshot.SHOW_RESUME"; 
	public static final String SHOW_MAIN_MENU = "ru.jecklandin.duckshot.SHOW_MAIN_MENU"; 
	public static final String SHOW_SETTINGS = "ru.jecklandin.duckshot.SHOW_SETTINGS"; 
	 
	private ViewAnimator mAnimator;
	private View mMl;
	private View mSl;
	private View mRl;
	
	private DucksSeekBar mSoundBar;
	private DucksSeekBar mEffectsBar;
	private CheckBox mVibroCheck;
	
	enum State {MAIN, RESUME};
	State mState = State.MAIN;
	
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
		mVibroCheck = ((CheckBox) findViewById(R.id.vibro));
		
		((ImageButton) findViewById(R.id.soundon)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.soundoff)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.effectson)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.effectsoff)).setOnClickListener(this);
		
		restoreSettings();
		registerExceptions();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mAnimator.getDisplayedChild() == 1) {
				commitSettings();
				SoundManager.getInstance().readSettings();
				mAnimator.showPrevious();
				return true;
			} else if (mState == State.RESUME) {
				setStartMode();
				return true;
			}
		} 
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED 
				&& DuckApplication.getInstance().getCurrentMatch() != null) { // BACK was pressed in game
			setResumeMode();
		} else {
			setStartMode();
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
		
		mState = State.MAIN;
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
		
		mState = State.RESUME;
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
			FlurryAgent.onEvent("newGame");
			Intent i3 = new Intent(MainMenu.this, LevelChooser.class);
			i3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivityForResult(i3, 0);
			break;
		case R.id.more:
			FlurryAgent.onEvent("onMoreScreen");
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
		boolean vibrate = mVibroCheck.isChecked();
		SharedPreferences prefs = getSharedPreferences("ducks", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("sound", sound);
		editor.commit();
		editor.putInt("effects", effects);
		editor.commit();
		editor.putBoolean("vibrate", vibrate);
		editor.commit();
	}
	
	private void restoreSettings() {
		SharedPreferences prefs = getSharedPreferences("ducks", Context.MODE_PRIVATE);
		mSoundBar.setProgress(prefs.getInt("sound", 4));
		mEffectsBar.setProgress(prefs.getInt("effects", 4));
		mVibroCheck.setChecked(prefs.getBoolean("vibrate", true));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	protected void onStart() {
		FlurryAgent.onStartSession(this, DuckApplication.FLURRY_KEY);
		super.onStart();
	}
	
	private void registerExceptions() {
		if (ExceptionHandler.register(this, new Handler())) {

			AlertDialog.Builder b = new Builder(this);
			b.setCancelable(false);
			TextView message = new TextView(this);
			message.setText(R.string.error_report);
			message.setPadding(10, 10, 10, 10);
			b.setView(message);

			b.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							FlurryAgent.onEvent("sendReport");
							ExceptionHandler.submitStackTraces();
						}
					}).start();
				}
			});
			b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					FlurryAgent.onEvent("dontSendReport");
					ExceptionHandler.deleteStackTrace();
					dialog.cancel();
				}
			});
			b.show();
		}
	}
}
