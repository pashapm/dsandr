package ru.jecklandin.duckshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainMenu extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ImageButton start = (ImageButton) findViewById(R.id.start);
		ImageButton settings = (ImageButton) findViewById(R.id.settings);
		ImageButton more = (ImageButton) findViewById(R.id.more);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainMenu.this, DuckGame.class);
				MainMenu.this.startActivity(i);
			}
		});
	}
}
