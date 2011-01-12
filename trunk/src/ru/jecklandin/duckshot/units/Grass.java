package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Grass extends GroundObject {

	public Grass(int x, int y, float speed, int wave_num) {
		super();
		this.x = x;
		this.y = y; 
		
		this.wave_num = wave_num;
		this.offset = x;
		this.speed = speed;
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		matrix.reset();
		matrix.postTranslate(0, y);
		c.drawBitmap(mGroundBitmap, matrix, p);  
		p.setColor(Color.parseColor("#5984c8"));
		c.drawRect(0, y+mGroundBitmap.getHeight()-2, ScrProps.screenWidth, y+mGroundBitmap.getHeight()+ScrProps.scale(50), p);
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
