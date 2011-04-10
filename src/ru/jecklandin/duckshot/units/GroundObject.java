package ru.jecklandin.duckshot.units;

import java.util.ArrayList;

import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.ScrProps;
import android.graphics.Bitmap;

public abstract class GroundObject extends GameObject {

	public ArrayList<Obstacle> mObstacles = new ArrayList<Obstacle>();
	
	protected static int MIN_DISTANCE_APPEARANCE = ScrProps.scale(80);
	
	public ArrayList<CreatureObject> mCreatures = new ArrayList<CreatureObject>();;
	
	protected int wave_num;
	
	public static Bitmap mGroundBitmap;  
	
	public static void initBitmaps() {
		GroundObject.mGroundBitmap = ImgManager.getBitmap("ground");
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
	
	public void addObstacle(Obstacle obs) {
		mObstacles.add(obs);
	}
}
