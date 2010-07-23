package ru.jecklandin.duckshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle; 

public class SettingsActivity extends Activity {
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImgManager.loadImages(this);
		setContentView(R.layout.settings);
	}
	
}
