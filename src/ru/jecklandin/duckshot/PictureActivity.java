package ru.jecklandin.duckshot;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class PictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int res = getIntent().getIntExtra("pic", -1);
		ImageView img = new ImageView(this);
		img.setImageBitmap(BitmapFactory.decodeResource(getResources(), res));
		setContentView(img);
		
		
	}
	
	@Override
	protected void onStart() {
		FlurryAgent.onStartSession(this, DuckApplication.FLURRY_KEY);
		FlurryAgent.onEvent("hopeYouDie");
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		FlurryAgent.onEndSession(this);
		super.onStop();
	}
	
}
