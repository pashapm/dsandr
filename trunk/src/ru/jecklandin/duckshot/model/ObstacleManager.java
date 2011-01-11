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
	
	public void addRock(Type type, int x, int wave) {
		Obstacle o;
		switch (type) {
		case ROCK1:
		case ROCK3:
			o = new Obstacle(mModel.mWaves.get(wave), x, type);
			mModel.mWaves.get(wave).mObstacles.add(o);
			break;
		case ROCK2:
			o = new Obstacle(mModel.mWaves.get(wave), x, type);
			mModel.mWaves.get(wave).mObstacles.add(o);
			if (wave > 0) {
				Obstacle o2 = new Obstacle(o, mModel.mWaves.get(wave));
				mModel.mWaves.get(wave-1).mObstacles.add(o2);
			}
		default:
			break;
		}
	}
}
