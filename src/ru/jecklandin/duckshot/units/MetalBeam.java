package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.ScrProps;
import ru.jecklandin.duckshot.model.DuckShotModel;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MetalBeam extends GroundObject {

	public MetalBeam(int y, int wave_num) {
		super();
		this.y = y; 
		
		this.wave_num = wave_num;
		this.offset = x;
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		matrix.reset();
		matrix.postTranslate(ScrProps.scale(-3), y + DuckShotModel.GROUNDS_GAP/2);
		c.drawBitmap(mGroundBitmap, matrix, p); 
		c.drawLine(0, this.y, 500, this.y, p);
	}
	
	@Override
	public boolean equals(Object o) {
		return this.id == ((MetalBeam)o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
