package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Grass extends GroundObject {

	public enum GRASS_TYPE {LIGHT, AVERAGE, DARK};
	
	private int mColor;
	
	private Bitmap mCurrentBitmap = GroundObject.mGroundBitmap;
	
	public Grass(int x, int y, float speed, int wave_num) {
		super();
		this.x = x;
		this.y = y; 
		
		this.wave_num = wave_num;
		this.offset = x;
		this.speed = speed;
		
		mColor = Color.parseColor("#226c26");
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		matrix.reset();
		matrix.postTranslate(0, y + ScrProps.scale(13));
		c.drawBitmap(mCurrentBitmap, matrix, p);  
		p.setColor(mColor);
		c.drawRect(0, y+mCurrentBitmap.getHeight() + ScrProps.scale(11),
				ScrProps.screenWidth, y + mCurrentBitmap.getHeight() + ScrProps.scale(50), p);
		
//		p.setColor(Color.BLACK);
//		c.drawLine(0, y, 500, y, p);
	}
	
	public void setBitmap(GRASS_TYPE type) {
		switch (type) {
		case LIGHT:
			mCurrentBitmap = ImgManager.getBitmap("grass_light");
			mColor = Color.parseColor("#25a32b");
			break;
		case DARK:
			mCurrentBitmap = ImgManager.getBitmap("grass_dark");
			mColor = Color.parseColor("#234a25");
			break;
		default:
			mCurrentBitmap = ImgManager.getBitmap("ground");
			mColor = Color.parseColor("#226c26");
			break;
		}
		 
	}
	
	@Override
	public boolean equals(Object o) {
		return this.id == ((Grass)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
