package ru.jecklandin.duckshot.units;

import ru.jecklandin.duckshot.ScrProps;
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
		matrix.postTranslate(ScrProps.scale(-3), y);
		c.drawBitmap(mGroundBitmap, matrix, p);  
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
