package ru.jecklandin.duckshot;

import java.util.ArrayList;
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
	private int MIN_OFFSET = ScrProps.scale(-100);
	private static int MIN_DISTANCE_APPEARANCE = ScrProps.scale(80);
	
	static {
		Wave.waveBm = ImgManager.getBitmap("wave");
	}
	
	private boolean mMovingRight = (Math.random()-0.5d) > 0;
	public static Bitmap waveBm;  
	
	public ArrayList<Duck> mCreatures = new ArrayList<Duck>();
	
	public ArrayList<Obstacle> mObstacles = new ArrayList<Obstacle>();
	
	protected int wave_num;
	
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
	public OBJ_TYPE getRtti() {
		return OBJ_TYPE.WAVE;
	}  
	
	public synchronized void addDuck(Duck d) {
		d.y = this.y; 
		if (mCreatures.contains(d)) {
			assert(false);
		}
		mCreatures.add(d);
	}
	
	public synchronized void removeDuck(Duck d) {
		mCreatures.remove(d);
	}
	
	public Duck getDuck(int loc) {
		return mCreatures.get(loc);
	} 
 
	@Override
	public void draw(Canvas c, Paint p) {
		matrix.reset();
		matrix.postTranslate(getNextOffset(x), y);
		c.drawBitmap(waveBm, matrix, p);  
		p.setColor(Color.parseColor("#5984c8"));
		c.drawRect(0, y+waveBm.getHeight()-2, ScrProps.screenWidth, y+waveBm.getHeight()+ScrProps.scale(50), p);
	}     
  
	public boolean isPlaceFree(int x) {
		for (Duck d : mCreatures) {
			if (Math.abs(d.offset - x) < Wave.MIN_DISTANCE_APPEARANCE) {
				return false;
			}
		}  
		
		for (Obstacle obs : mObstacles) {
			if (x >= obs.getX() && x <= obs.getX() + obs.getWidth()) {
				return false;
			}
		}
		
		return true;
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
