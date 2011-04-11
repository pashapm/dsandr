package ru.jecklandin.duckshot;

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
	
}
