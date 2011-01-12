package ru.jecklandin.duckshot.levels;

import android.graphics.Bitmap;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.levels.WaterLevel.WaterEnvironment;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Duck;
import ru.jecklandin.duckshot.units.Grass;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Hedgehog;

public class ForestLevel extends WaterLevel {

	public ForestLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
	}

	@Override
	public void loadResources() {
		super.loadResources();
		
		
		
		Hedgehog.initBitmaps();
	}
	
	@Override
	public GroundObject createGroundObject(int x, int y, float speed, int wave_num) {
		return new Grass(x, y, speed, wave_num);
	}
	
	public CreatureObject createCreatureObject(int x) {
		return new Duck(x);
	}

}
