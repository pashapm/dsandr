package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MoreScreen extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		
		ImageButton about = (ImageButton) findViewById(R.id.about);
		about.setOnClickListener(this);
		
		ImageButton hiscores = (ImageButton) findViewById(R.id.hiscores);
		hiscores.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about:

			break;
		case R.id.hiscores:
			Intent i = new Intent(this, HiScores.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}
	
}
