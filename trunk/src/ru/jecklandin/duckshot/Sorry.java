package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Sorry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sorry);
		Button rate = (Button) findViewById(R.id.rate);
		rate.setTypeface(DuckApplication.getCommonTypeface());
		rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setData(Uri.parse("market://search?q=pub:\"Jeck Landin\""));
				startActivity(i);
			}
		});
	}
	
}
