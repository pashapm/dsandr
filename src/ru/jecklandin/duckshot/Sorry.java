package ru.jecklandin.duckshot;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Sorry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sorry);
		
		boolean last = getIntent().getBooleanExtra("last", false);
		
		TextView hi = (TextView) findViewById(R.id.sorry_hi);
		hi.setText(last ? "Sorry" : "Hi there");
		
//		TextView text = (TextView) findViewById(R.id.sorry_text);
//		text.setText(last ? R.string.sorry : R.string.rate);
		
		 
		Button rate = (Button) findViewById(R.id.rate);
		rate.setTypeface(DuckApplication.getCommonTypeface());
		rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FlurryAgent.onEvent("rate");
				Intent i = new Intent();
				i.setData(Uri.parse("market://search?q=pub:\"Jeck Landin\""));
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onStart() {
		FlurryAgent.onStartSession(this, DuckApplication.FLURRY_KEY);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		FlurryAgent.onEndSession(this);
		super.onStop();
	}
	
}
