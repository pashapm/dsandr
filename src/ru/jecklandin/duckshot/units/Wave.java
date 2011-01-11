package ru.jecklandin.duckshot.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.model.DuckShotModel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class Wave extends GroundObject {
	
	private int MAX_OFFSET = 0;
	private int MIN_OFFSET = ScrProps.scale(-100);
	
	static {
		GroundObject.mGroundBitmap = ImgManager.getBitmap("wave");
	}
	
	private boolean mMovingRight = (Math.random()-0.5d) > 0;
	
	public Wave(int x, int y, float speed, int wave_num) {
		super();
		this.x = x;
		this.y = y; 
		
		this.wave_num = wave_num;
		this.offset = x;
		this.speed = speed;
		matrix = new Matrix();
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
		matrix.postTranslate(getNextOffset(x), y);
		c.drawBitmap(mGroundBitmap, matrix, p);  
		p.setColor(Color.parseColor("#5984c8"));
		c.drawRect(0, y+mGroundBitmap.getHeight()-2, ScrProps.screenWidth, y+mGroundBitmap.getHeight()+ScrProps.scale(50), p);
	}     
}
