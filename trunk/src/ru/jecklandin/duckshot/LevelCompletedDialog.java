package ru.jecklandin.duckshot;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.sax.StartElementListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class LevelCompletedDialog extends Dialog {

	@Override
	protected void onStart() {
		super.onStart();
		mMatch = DuckGame.getCurrentMatch();
		TextView timeV = (TextView) findViewById(R.id.lvltime);
		
		//TODO font crap
		if (mMatch.mInitialTime%60/10 == 1) {
			String time = mMatch.mInitialTime/60+":";
			time+="1 ";
			time+=mMatch.mInitialTime%60%10;
			timeV.setText(time);
		} else {
			timeV.setText(+mMatch.mInitialTime/60+":"+mMatch.mInitialTime%60);
		}
		
		TextView scoreV = (TextView) findViewById(R.id.lvlsc);
		scoreV.setText(mMatch.getScore()+"");
	}

	private Match mMatch;
	
	protected LevelCompletedDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView().setBackgroundResource(R.drawable.dialog);
		setContentView(R.layout.levelcompleted);
		
		
		
		//filling
		TextView views[] = new TextView[7];
		views[0] = (TextView) findViewById(R.id.lvlc);
		views[1] = (TextView) findViewById(R.id.lvltime_lab);
		views[2] = (TextView) findViewById(R.id.lvlav_lab);
		views[3] = (TextView) findViewById(R.id.lvlsc_lab);
		views[4] = (TextView) findViewById(R.id.lvlc);
		views[5] = (TextView) findViewById(R.id.lvltime);
		views[6] = (TextView) findViewById(R.id.lvlsc);
		
		for (TextView v:views) {
			v.setTypeface(DuckGame.s_instance.mHelsTypeface);
		}
		
		Button bretry = (Button) findViewById(R.id.retry);
		bretry.setTypeface(DuckGame.s_instance.mHelsTypeface);
		Button bnext = (Button) findViewById(R.id.nextlev);
		bnext.setTypeface(DuckGame.s_instance.mHelsTypeface);
		 
		setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Intent i = new Intent(getOwnerActivity(), MainMenu.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setAction(MainMenu.SHOW_MAIN_MENU);
				getOwnerActivity().startActivity(i);
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
		
	}

}
