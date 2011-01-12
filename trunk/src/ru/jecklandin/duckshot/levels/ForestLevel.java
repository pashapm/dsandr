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
	
public static class ForestEnvironment implements Environment {
		
		// Bitmaps
		private static Bitmap mCloud1;
		private static Bitmap mCloud2;
		
		Matrix m = new Matrix();
		float rot_degree = 0;
		float x_offset1 = 0;
		float x_offset2 = 0;
		
		public void draw(Canvas c, Paint p) {
			m.setTranslate(x_offset2, 0 );
			c.drawBitmap(mCloud2, m, p);
			if (x_offset2 > ScrProps.screenWidth * 1.2) {
				x_offset2 = -mCloud2.getWidth();
			} else {
				x_offset2+=0.3;
			}
			
			m.setTranslate(x_offset1, 30);
			c.drawBitmap(mCloud1, m, p);
			if (x_offset1 > ScrProps.screenWidth * 1.1) {
				x_offset1 = -mCloud1.getWidth();
			} else {
				x_offset1+=0.1;
			}
			
			p.setColor(Color.parseColor("#216221")); 
			c.drawRect(0, ScrProps.screenHeight-200, ScrProps.screenWidth, ScrProps.screenHeight, p);
		}

		@Override
		public void init() {
			mCloud1 = ImgManager.getBitmap("cloud1");
			mCloud2 = ImgManager.getBitmap("cloud2");
		}
	}

}
