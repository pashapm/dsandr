package ru.jecklandin.duckshot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PauseDialog {

	
	
	public static void show(Context ctx, Handler h) {
		final Dialog dialog = new Dialog(ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pausedialog);
		Button resume = (Button) dialog.findViewById(R.id.resume);


		dialog.setCancelMessage(Message.obtain(h));
		
		resume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		
		dialog.show();
	}
	
}
