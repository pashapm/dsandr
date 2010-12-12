package ru.jecklandin.duckshot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PauseDialog extends Dialog {

	public PauseDialog(Context context, Handler h) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView().setBackgroundResource(R.drawable.dialog);
		
		setContentView(R.layout.pausedialog);
		
		Message dismMess = Message.obtain(h);
		dismMess.what = 0;
		setDismissMessage(dismMess);
		
		Message cancMess = Message.obtain(h);
		cancMess.what = 1;
		setCancelMessage(cancMess);
		
		((TextView)findViewById(R.id.pauselab)).setTypeface(DuckApplication.getCommonTypeface());
		
		Button resume = (Button) findViewById(R.id.presume);
		resume.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancel();
			}
			
		});
		
		Button settings = (Button) findViewById(R.id.psettings);
		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getOwnerActivity(), MainMenu.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setAction(MainMenu.SHOW_SETTINGS);
				getOwnerActivity().startActivity(i);
				dismiss();
			}
			
		});
	}
	
	@Override
	protected void onStart() {
		SoundManager.getInstance().turnMusic(false);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		SoundManager.getInstance().turnMusic(true);
		super.onStop();
	}
}
