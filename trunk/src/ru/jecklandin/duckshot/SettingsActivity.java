package ru.jecklandin.duckshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle; 
import android.widget.LinearLayout;

public class SettingsActivity extends Activity {
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImgManager.loadImages(this);
		setContentView(R.layout.settings);
		((LinearLayout)findViewById(R.id.sett_lay)).setBackgroundResource(R.drawable.menubackt);
	}
	
}
