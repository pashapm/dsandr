package ru.jecklandin.duckshot.model;

import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;
import ru.jecklandin.duckshot.units.Obstacle.Type;

public class ObstacleManager {

	private static ObstacleManager sInstance;
	
	private DuckShotModel mModel;
	private ObstacleManager() {
		mModel = DuckShotModel.getInstance();
	}
	
	public static synchronized ObstacleManager getInstance() {
		if (sInstance == null) {
			sInstance = new ObstacleManager();
		}
		return sInstance;
	}
	
	public Obstacle addObstacle(Type type, int x, int wave_num, int h_offset) {
		Obstacle o;
		GroundObject ground = mModel.getGround(wave_num);
		switch (type) {
		case TYPE1:
		case TYPE3:
			o = new Obstacle(ground, x, type, h_offset);
			ground.mObstacles.add(o);
			return o;
		case TYPE2:
			o = new Obstacle(ground, x, type, h_offset);
			ground.mObstacles.add(o);
			if (wave_num > 0) {
				GroundObject up_ground = mModel.getGround(wave_num-1);
				Obstacle o2 = new Obstacle(o, ground);
				up_ground.addObstacle(o2);
			}
			return o;
		default:
			break;
		}
		return null;
	}
	
	public Obstacle addObstacle(Type type, int x, int wave_num) {
		return addObstacle(type, x, wave_num, -1);
	}
	
	public void addStubObstacle(int wave_num, int x, int width, Obstacle.Type parentType) {
		if (wave_num < 0) {
			return;
		}
		GroundObject ground = mModel.getGround(wave_num);
		Obstacle o = new Obstacle(ground, x, width, parentType);
		ground.addObstacle(o);
	}
}
