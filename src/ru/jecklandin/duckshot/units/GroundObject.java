package ru.jecklandin.duckshot.units;

import java.util.ArrayList;

import ru.jecklandin.duckshot.Desk;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.Desk.DigitType;
import ru.jecklandin.duckshot.model.DuckShotModel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class GroundObject extends GameObject {

	public ArrayList<Obstacle> mObstacles = new ArrayList<Obstacle>();
	
	protected static int MIN_DISTANCE_APPEARANCE = ScrProps.scale(80);
	
	public static Bitmap mGroundBitmap;  
	
	public ArrayList<CreatureObject> mCreatures = new ArrayList<CreatureObject>();;
	
	protected int wave_num;
	
	@Override
	public float getNextOffset(float curOffset) {
		return this.offset;
	}
	
	public boolean isPlaceFree(int x) {
		for (CreatureObject d : mCreatures) {
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
	
	public synchronized void addCreature(CreatureObject d) {
		d.y = this.y; 
		if (mCreatures.contains(d)) {
			assert(false);
		}
		mCreatures.add(d);
	}
	
	public synchronized void removeCreature(CreatureObject d) {
		mCreatures.remove(d);
	}
	
	public CreatureObject getCreature(int loc) {
		return mCreatures.get(loc);
	} 
	
	
}
