package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Wave extends GroundObject {
	
	private int MAX_OFFSET = 0;
	private int MIN_OFFSET = ScrProps.scale(-100);
	
	private boolean mMovingRight = (Math.random()-0.5d) > 0;
	
	public Wave(int x, int y, float speed, int wave_num) {
		super();
		this.x = x;
		this.y = y; 
		
		this.wave_num = wave_num;
		this.offset = x;
		this.speed = speed;
	}
	
	@Override
	public float getNextOffset(float curOffset) {
		if (mMovingRight) {
			offset +=1;
		} else {
			offset -=1;
		}
		
		if (offset < MIN_OFFSET) {
			mMovingRight = true;
		} else if (offset > MAX_OFFSET) {
			mMovingRight = false;
		}
		return offset;
	}

	@Override
	public void draw(Canvas c, Paint p) {
		matrix.reset();
		matrix.postTranslate(getNextOffset(x), y + ScrProps.scale(4));
		c.drawBitmap(mGroundBitmap, matrix, p);  
		p.setColor(Color.parseColor("#5984c8"));
		c.drawRect(0, y + mGroundBitmap.getHeight() + ScrProps.scale(2), 
				ScrProps.screenWidth, y + mGroundBitmap.getHeight() + ScrProps.scale(50), p);
	}
	
	@Override
	public boolean equals(Object o) {
		return this.id == ((Wave)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
