package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.flurry.android.FlurryAgent;

public class MoreScreen extends Activity implements OnClickListener {

//	static BitmapDrawable mUnderwaterBg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.more);
		
//		BitmapFactory.Options opts = new Options();
//		opts.inPreferredConfig = Config.RGB_565;
//		opts.inDither = false;
//		mUnderwaterBg = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.underwater, opts)) ;
//		findViewById(R.id.more_lay).setBackgroundDrawable(mUnderwaterBg);
		
		findViewById(R.id.more_lay).setBackgroundResource(R.drawable.underwater_dith);
		
		ImageButton about = (ImageButton) findViewById(R.id.about);
		about.setOnClickListener(this);
		
		ImageButton hiscores = (ImageButton) findViewById(R.id.hiscores);
		hiscores.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about:
			FlurryAgent.onEvent("about");
			Intent i0 = new Intent(this, About.class);
			startActivity(i0);
			break;
		case R.id.hiscores:
			FlurryAgent.onEvent("hiscores");
			Intent i = new Intent(this, HiScores.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	protected void onStart() {
		FlurryAgent.onStartSession(this, DuckApplication.FLURRY_KEY);
		super.onStart();
	}
	
}
