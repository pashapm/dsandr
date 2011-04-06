package ru.jecklandin.duckshot.units;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Cupcake extends CreatureObject {

	private static Bitmap[] mCupcake;
	
	@Override
	protected void drawNormal(Canvas c, Paint p) {
		
	}

	@Override
	protected void handleHit(int hps) {

	}

	@Override
	public void draw(Canvas c, Paint p) {
		drawNormal(c, p);
	}

}
