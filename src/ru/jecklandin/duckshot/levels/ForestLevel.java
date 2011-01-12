package ru.jecklandin.duckshot.levels;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.jecklandin.duckshot.Environment;
import ru.jecklandin.duckshot.ImgManager;
import ru.jecklandin.duckshot.R;
import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.levels.WaterLevel.WaterEnvironment;
import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.units.CreatureObject;
import ru.jecklandin.duckshot.units.Duck;
import ru.jecklandin.duckshot.units.Grass;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Hedgehog;
import ru.jecklandin.duckshot.units.Grass.GRASS_TYPE;

public class ForestLevel extends WaterLevel {

	public ForestLevel(int levelId, int thumb, String name) {
		super(levelId, thumb, name);
		
		setDominatingColor(Color.parseColor("#234a25"));
		setBackgroundColor(Color.parseColor("#226c26"));
		mEnvironment = new ForestEnvironment(mBgColor);
	}

	@Override
	public void loadResources() {
		super.loadResources();
	}
	
	@Override
	public void initItemsBitmaps() {
		super.initItemsBitmaps();
		Hedgehog.initBitmaps();
		assignColorsToGrass(); 
	}
	
	@Override
	public GroundObject createGroundObject(int x, int y, float speed, int wave_num) {
		return new Grass(x, y, speed, wave_num);
	}
	
	public CreatureObject createCreatureObject(int x) {
		return new Hedgehog(x);
	}
	
	private void assignColorsToGrass() {
		ArrayList<GroundObject> grs = DuckShotModel.getInstance().mGrounds;
		for (int i=0; i<grs.size(); ++i) {
			Grass grass = (Grass) grs.get(i); 
			grass.setBitmap(i%2 != 0 ? GRASS_TYPE.AVERAGE : GRASS_TYPE.DARK);
			if (i==0 || i==6){
				grass.setBitmap(GRASS_TYPE.LIGHT);
			}
		}
	}
	
public static class ForestEnvironment extends Environment {
		
	    ForestEnvironment(int bgColor) {
	    	super(bgColor);
	    }
	    
		public void draw(Canvas c, Paint p) {
			c.drawColor(Color.parseColor("#234a25"));
			
			p.setColor(mBgColor); 
			c.drawRect(0, ScrProps.screenHeight-200, ScrProps.screenWidth, ScrProps.screenHeight, p);
		}

		@Override
		public void init() {
//			mCloud1 = ImgManager.getBitmap("cloud1");
//			mCloud2 = ImgManager.getBitmap("cloud2");
		}
	}

}
