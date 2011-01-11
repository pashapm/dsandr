package ru.jecklandin.duckshot.model;

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
	
	public void addObstacle(Type type, int x, int wave) {
		Obstacle o;
		switch (type) {
		case TYPE1:
		case TYPE3:
			o = new Obstacle(mModel.mGrounds.get(wave), x, type);
			mModel.mGrounds.get(wave).mObstacles.add(o);
			break;
		case TYPE2:
			o = new Obstacle(mModel.mGrounds.get(wave), x, type);
			mModel.mGrounds.get(wave).mObstacles.add(o);
			if (wave > 0) {
				Obstacle o2 = new Obstacle(o, mModel.mGrounds.get(wave));
				mModel.mGrounds.get(wave-1).mObstacles.add(o2);
			}
		default:
			break;
		}
	}
}
