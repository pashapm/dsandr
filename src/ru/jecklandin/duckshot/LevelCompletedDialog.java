package ru.jecklandin.duckshot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;

public class LevelCompletedDialog extends Dialog {

	private Match mMatch;
	
	protected LevelCompletedDialog(Context context, Match match) {
		super(context);
		mMatch = match;
		setTitle("Level completed");
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent i = new Intent(getOwnerActivity(), MainMenu.class);
				getOwnerActivity().startActivity(i);
			}
		});
		
		
	}

}
