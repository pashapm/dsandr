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

public class PauseDialog extends Dialog {

	public PauseDialog(Context context, Handler h) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView().setBackgroundResource(R.drawable.dialog);
		
		setContentView(R.layout.pausedialog);
		setCancelMessage(Message.obtain(h));
		
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
				Intent i = new Intent(getOwnerActivity(), SettingsActivity.class);
				getOwnerActivity().startActivity(i);
				cancel();
			}
			
		});
		
	}
}
