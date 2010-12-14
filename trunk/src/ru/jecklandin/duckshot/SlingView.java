package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.model.DuckShotModel;
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
   
   private static int SLING_X;
   private static int SLING_Y;
   
   static int SOCKET_DEFAULT_X;
   static int SOCKET_DEFAULT_Y;
   
   private static int SLING_AREA_HEIGHT;
   
   private boolean mIsGrabbed = false;
	
   Paint mPaint = new Paint();
   Path mPath = new Path();
   
	public SlingView(Context context) {
		super(context);
		sling = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.sling2 );
		socket = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.sling_socket );
		
		SLING_X = ScrProps.scale(80);
		SLING_Y = ScrProps.screenHeight - sling.getHeight() + ScrProps.scale(20);
		
		SOCKET_DEFAULT_X = SLING_X + ScrProps.scale(84);
		SOCKET_DEFAULT_Y = SLING_Y + ScrProps.scale(40);
		
		SLING_AREA_HEIGHT = ScrProps.screenHeight - SOCKET_DEFAULT_Y;
		
		slx = SOCKET_DEFAULT_X;
		sly = SOCKET_DEFAULT_Y;
		
		mPaint.setAntiAlias(true);
	}
	
	
	
	@Override 
	public void draw(Canvas canvas) {
		mPaint.setColor(Color.parseColor("#a76e21"));
		canvas.drawBitmap(sling, SLING_X, SLING_Y, mPaint);


		// p.setPathEffect(new CornerPathEffect(15));

		Path p2 = getRectangle(SLING_X + ScrProps.scale(28),
				SLING_Y + ScrProps.scale(24),
				slx - socket.getWidth() / 2 + ScrProps.scale(10),
				sly, ScrProps.scale(8));
		canvas.drawPath(p2, mPaint);
		
		Path p3 = getRectangle(slx + socket.getWidth() / 2 - ScrProps.scale(10), sly, SLING_X + ScrProps.scale(142), SLING_Y + ScrProps.scale(28), ScrProps.scale(8));
		canvas.drawPath(p3, mPaint);

//		invalidate(0, ScreenProps.screenHeight - sling.getHeight(), 
//				ScreenProps.screenWidth, 
//				ScreenProps.screenWidth);
		
		canvas.drawBitmap(socket, slx - socket.getWidth() / 2, sly
				- socket.getHeight() / 2, mPaint);
		
//		invalidate();
	}
		
		private Path getRectangle(int x1, int y1, int x2, int y2, int l) {
			
			mPath.reset();
			mPath.setLastPoint(x1, y1);
			mPath.lineTo(x2, y2);
			
			int b = x2 - x1;
			int a = y2 - y1;

			double alpha = getAngle(x2-x1, y2-y1);
			double beta = ((y2-y1) > 0 ? 1 : 3)*Math.PI/2 - alpha;
			
			double y = Math.sin(beta)*l;
			double x = Math.sqrt(l*l - y*y);
			
			mPath.lineTo((int)(x2+x), (int)(y2-y));
			
			//ok, one more point
			mPath.lineTo((int)(x1+x), (int)(y1-y));
			mPath.lineTo((int)(x1), (int)(y1));
			mPath.close();
			
			return mPath;
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

		public void setXY(float x, float y) {
			if (!mIsGrabbed) {
				return;
			}
			
	    	if ( y > ScrProps.scale(350)) {
	    		sly = (int) y;
	    	}
	    	slx = (int) x;
	    	 
	    	//setting sight
			int center = SOCKET_DEFAULT_X;
			int a1 = (int) Math.abs(SOCKET_DEFAULT_X - x);
			int sightx = x>center ? center-a1 : center+a1;
			int b1 = (int) (y<SOCKET_DEFAULT_Y ? 0 : y - SOCKET_DEFAULT_Y);
 
			int sighty = SOCKET_DEFAULT_Y - b1 * DuckShotModel.WAVES_HEIGHT / SLING_AREA_HEIGHT;
			Desk.getInstance().setSight(sightx, sighty); 
		} 

		public void shot(int x, int y) {
			if (!mIsGrabbed) {
				return;
			}
			int center = SOCKET_DEFAULT_X; 
			int a1 = Math.abs(center - x);
			int b1 = y<SOCKET_DEFAULT_Y ? 0 : y - SOCKET_DEFAULT_Y;
			int wave_num = DuckShotModel.WAVES_NUM - 1 - b1 * DuckShotModel.WAVES_NUM / SLING_AREA_HEIGHT;
			DuckShotModel.getInstance().launchStone(wave_num, x>center ? center-a1 : center+a1);
			slx = SOCKET_DEFAULT_X;
			sly = SOCKET_DEFAULT_Y;
			mIsGrabbed = false;
			Desk.getInstance().setSightVisibility(false);
		}

		public void grab(int x, int y) {
			if (Math.abs(x-SOCKET_DEFAULT_X) < ScrProps.scale(50)
					&&  Math.abs(y-SOCKET_DEFAULT_Y) < ScrProps.scale(40)) {
				mIsGrabbed = true;
				Desk.getInstance().setSightVisibility(true);
			} 
			
		}

	
}