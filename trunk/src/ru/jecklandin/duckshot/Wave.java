package ru.jecklandin.duckshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import ru.jecklandin.duckshot.model.DuckShotModel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class Wave extends GameObject {
	private int MAX_OFFSET = 0;
	private int MIN_OFFSET = -100;
	private static int MIN_DISTANCE_APPEARANCE = 80;
	
	
	static {
		Wave.waveBm = ImgManager.getBitmap("wave");
	}
	static int[] bbb;
	private boolean mMovingRight = (Math.random()-0.5d) > 0;
	private static Bitmap waveBm;  
	public Vector<Duck> ducks = new Vector<Duck>();
	
	public Wave(int x, int y, float speed) {
		super();
		this.x = x;
		this.y = y; 
		
		this.offset = x;
		this.speed = speed;
		matrix = new Matrix();
	}
	
	@Override
	public float getNextOffset(float curOffset) {
		if (mMovingRight) {
//			offset +=(1+speed/5);
			offset +=1;
		} else {
//			offset -=(1+speed/5);
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
	public OBJ_TYPE getRtti() {
		return OBJ_TYPE.WAVE;
	}
	
	public void addDuck(Duck d) {
		d.y = this.y;
		ducks.add(d);
	}
	
	public Duck getDuck(int loc) {
		return ducks.get(loc);
	} 
 
	@Override
	public void draw(Canvas c, Paint p) {
		matrix.reset();
		matrix.postTranslate(getNextOffset(x), y);
		c.drawBitmap(waveBm, matrix, p);  
		p.setColor(Color.parseColor("#5984c8"));
		c.drawRect(0, y+waveBm.getHeight()-1, ScreenProps.screenWidth, y+y+waveBm.getHeight()+50, p);
	}

	public boolean isPlaceFree(int randx) {
		boolean free = true;
		for (Duck d : ducks) {
			if (Math.abs(d.offset - randx) < Wave.MIN_DISTANCE_APPEARANCE) {
				free = false;
			}
		}
		return free;
	}
}
