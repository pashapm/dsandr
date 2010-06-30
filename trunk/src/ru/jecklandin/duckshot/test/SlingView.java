package ru.jecklandin.duckshot.test;

import ru.jecklandin.duckshot.ScreenProps;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class SlingView extends View {

	public int slx;
    public int sly;
	
   Bitmap sling;
   Bitmap socket;
   
   
   
	
	public SlingView(Context context) {
		super(context);
		sling = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.sling2 );
		socket = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.sling_socket );
	}
	
	@Override 
	public void draw(Canvas canvas) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.parseColor("#a76e21"));
		canvas.drawBitmap(sling, 80, ScreenProps.screenHeight  
				- sling.getHeight() + 20, p);
		canvas.drawBitmap(socket, slx - socket.getWidth() / 2, sly
				- socket.getHeight() / 2, p);

		// p.setPathEffect(new CornerPathEffect(15));

//		Path p0 = getRectangle(190, 190, 50, 100, 20);
//		Path p01 = getRectangle(200, 400, 100, 200, 20);
//		canvas.drawPath(p0, p);
//		canvas.drawPath(p01, p);

		Path p2 = getRectangle(90, ScreenProps.screenHeight - sling.getHeight()
				+ 20, slx - socket.getWidth() / 2, sly, 15);
		canvas.drawPath(p2, p);

		Path p3 = getRectangle(slx + socket.getWidth() / 2, sly, 270,
				ScreenProps.screenHeight - sling.getHeight() + 10, 15);
		canvas.drawPath(p3, p);

//		invalidate(0, ScreenProps.screenHeight - sling.getHeight(), 
//				ScreenProps.screenWidth, 
//				ScreenProps.screenWidth);
		invalidate();
	}
		
		private Path getRectangle(int x1, int y1, int x2, int y2, int l) {
			Path path = new Path();
			path.setLastPoint(x1, y1);
			path.lineTo(x2, y2);
			
			int b = x2 - x1;
			int a = y2 - y1;

			double alpha = getAngle(x2-x1, y2-y1);
			double beta = ((y2-y1) > 0 ? 1 : 3)*Math.PI/2 - alpha;
			
			double y = Math.sin(beta)*l;
			double x = Math.sqrt(l*l - y*y);
			
			path.lineTo((int)(x2+x), (int)(y2-y));
			
			//ok, one more point
			path.lineTo((int)(x1+x), (int)(y1-y));
			path.lineTo((int)(x1), (int)(y1));
			path.close();
			
			return path;
		}

		private double getAngle(int dx, int dy) {
	    	double alpha = Math.asin( Math.abs(dy) / Math.hypot(dx, dy) );
	    	if (dx > 0) {
	    		if (dy > 0) {
	    			//nothing
	    		} else {
	    			alpha *= -1; 
	    		}
	    	} else {
	    		if (dy > 0) {
	    			alpha = Math.PI - alpha;
	    		} else {
	    			alpha = Math.PI + alpha;
	    		}
	    	}
	    	return alpha;
		}
	
}