package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ViewAnimator;

public class MainMenu extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ImgManager.loadImages(this);
		
		View ml = getLayoutInflater().inflate(R.layout.main, null);
		View sl = getLayoutInflater().inflate(R.layout.settings, null);
		
		final ViewAnimator anim = new ViewAnimator(this);
		LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		anim.addView(ml, 0, pars);
		anim.addView(sl, 1, pars);
		
		setContentView(anim);
		
		ImageButton start = (ImageButton) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainMenu.this, DuckGame.class);
				MainMenu.this.startActivity(i);
			}
		});
		
		ImageButton sett = (ImageButton) findViewById(R.id.settings);
		sett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				anim.showNext();
			}
		});
			
			
//		setContentView(R.layout.main);
//		
//		ImageButton start = (ImageButton) findViewById(R.id.start);
//		ImageButton settings = (ImageButton) findViewById(R.id.settings);
//		ImageButton more = (ImageButton) findViewById(R.id.more);
//		
//		start.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent();
//				i.setClass(MainMenu.this, DuckGame.class);
//				MainMenu.this.startActivity(i);
//			}
//		});
//		
//		settings.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent();
//				i.setClass(MainMenu.this, SettingsActivity.class);
//				MainMenu.this.startActivity(i);
//			}
//		});
	}
}
