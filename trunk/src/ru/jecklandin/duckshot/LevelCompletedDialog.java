package ru.jecklandin.duckshot;

import java.util.HashMap;
import java.util.Map;

import ru.jecklandin.duckshot.levels.LevelManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

public class LevelCompletedDialog extends Dialog {

	private Match mMatch;
	
	private AwardsView mAwardsView;
	private boolean mSubmitted = false;
	
	private boolean go_next_lvl = false;
	
	private LevelManager mLvlManager;
	
	@Override
	protected void onStart() {
		super.onStart();
		mMatch = DuckGame.getCurrentMatch();
		
		mLvlManager = LevelManager.getInstance();
		
		TextView timeV = (TextView) findViewById(R.id.lvltime);
		timeV.setText(+mMatch.mInitialTime/60+":"+mMatch.mInitialTime%60);
		
		int pointsToComplete = DuckApplication.getInstance().getCurrentLevel().mPointsToComplete;
		TextView scoreV = (TextView) findViewById(R.id.lvlsc);
		scoreV.setText(mMatch.getScore() + " / " + pointsToComplete);
		
		TextView levelCompl = (TextView) findViewById(R.id.lvlc);
		if (mMatch.getScore() < pointsToComplete) {
			levelCompl.setText("Level failed");
			levelCompl.setTextColor(Color.parseColor("#ff5660"));
			findViewById(R.id.nextlev).setVisibility(View.INVISIBLE);
			findViewById(R.id.or_btn).setVisibility(View.INVISIBLE);
		} else {
			levelCompl.setText("Level completed");
			levelCompl.setTextColor(Color.parseColor("#f8e000"));
			
			if (! mLvlManager.isNextLevelUnlocked()) {
				mLvlManager.unlockNextLevel();
			}
			mLvlManager.unlockNextLevel();
			findViewById(R.id.nextlev).setVisibility(View.VISIBLE);
			findViewById(R.id.or_btn).setVisibility(View.VISIBLE);
		}
		
		mAwardsView.mAwards = mMatch.getAwards();
		if (mMatch.getAwards().size() == 0) {
			findViewById(R.id.awards_lay).setVisibility(View.GONE);
		}
		 
		findViewById(R.id.submit_lay).setVisibility(mMatch.getScore() == 0 ? View.INVISIBLE : View.VISIBLE);
		
		FlurryAgent.onStartSession(getContext(), DuckApplication.FLURRY_KEY);
	} 
	   
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(getContext());
	} 

	private void submitScore() {
		EditText ed = (EditText) findViewById(R.id.pname);
		String name = TextUtils.isEmpty(ed.getText().toString()) 
			? "Unknown Player" : ed.getText().toString();
		HiScoresManager.addScore(new Score(name, mMatch.getScore()));
		findViewById(R.id.submit_lay).setVisibility(View.INVISIBLE);
		mSubmitted = true;
		
		SharedPreferences prefs = getContext().getSharedPreferences("duckshot", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("name", name);
		editor.commit();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("score", mMatch.getScore()+"");
		params.put("awards", mMatch.getAwards().size()+"");
		FlurryAgent.onEvent("scoreSubmit", params);
	}
	
	
	protected LevelCompletedDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView().setBackgroundResource(R.drawable.dialog);
		setContentView(R.layout.levelcompleted);
		 
		//filling
		TextView views[] = new TextView[8];
		views[0] = (TextView) findViewById(R.id.lvlc);
		views[1] = (TextView) findViewById(R.id.lvltime_lab);
		views[2] = (TextView) findViewById(R.id.lvlav_lab);
		views[3] = (TextView) findViewById(R.id.lvlsc_lab);
		views[4] = (TextView) findViewById(R.id.lvlc);
		views[5] = (TextView) findViewById(R.id.lvltime);
		views[6] = (TextView) findViewById(R.id.lvlsc);
		views[7] = (TextView) findViewById(R.id.submit);
		
		for (TextView v:views) {
			v.setTypeface(DuckApplication.getCommonTypeface());
		}
		
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitScore();
			}
		});
		
		mAwardsView = (AwardsView)findViewById(R.id.awards);
		
		Button bretry = (Button) findViewById(R.id.retry);
		bretry.setTypeface(DuckApplication.getCommonTypeface());
		
		Button bnext = (Button) findViewById(R.id.nextlev);
		bnext.setTypeface(DuckApplication.getCommonTypeface());
		 
		Button bor = (Button) findViewById(R.id.or_btn);
		bor.setTypeface(DuckApplication.getCommonTypeface());
		
		setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (!go_next_lvl) {
					Intent i = new Intent(getOwnerActivity(), MainMenu.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					i.setAction(MainMenu.SHOW_MAIN_MENU);
					getOwnerActivity().startActivity(i);
				} else {
					Intent i1 = new Intent(getOwnerActivity(), DuckGame.class);
					i1.setAction(DuckGame.NEW_MATCH);
					i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					getOwnerActivity().startActivity(i1);
				}
			}
		});
		
		bretry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getOwnerActivity(), DuckGame.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setAction(DuckGame.NEW_MATCH);
				getOwnerActivity().startActivity(i);
				dismiss();
			}
		});
		
		bnext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (LevelManager.getInstance().isNextLevelAvailable()) {
					LevelManager.getInstance().loadNextLevel();
					go_next_lvl = true;
					cancel();
				} else {
					Intent i = new Intent(getContext(), Sorry.class);
					i.putExtra("last", true);
					getOwnerActivity().startActivity(i);
				}
			}
		});
		
		bor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), Sorry.class);
				getOwnerActivity().startActivity(i);
			}

		});
		
		SharedPreferences prefs = getContext().getSharedPreferences("duckshot", Context.MODE_PRIVATE);
		String pname = prefs.getString("name", "Unknown Player");
		((TextView) findViewById(R.id.pname)).setText(pname);
	}

}
