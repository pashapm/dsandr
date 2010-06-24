package ru.jecklandin.duckshot.test;



import ru.jecklandin.duckshot.ScreenProps;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SlingActivity extends Activity {

	SlingView v;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v  = new SlingView(this);
        setContentView(v);
        ScreenProps.initialize(this);
    }
	
	

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		v.x = (int) event.getX();
		v.y = (int) event.getY();
		return super.onTouchEvent(event);
	}

}


class SlingView extends View {

    int x;
    int y;
	
   Bitmap sling;
   Bitmap left_el;
   Bitmap right_el;
   Bitmap socket;
   
   
   
	
	public SlingView(Context context) {
		super(context);
		sling = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.sling );
		left_el = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.elastic_l );
		right_el = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.elastic_r );
		socket = BitmapFactory.decodeResource(context.getResources(), ru.jecklandin.duckshot.R.drawable.sling_socket );
	}
	
		@Override 
	public void draw(Canvas canvas) {
//			Log.d("?????", "!!!!!"+ScreenProps.screenHeight);
			Paint p = new  Paint();
			p.setAntiAlias(true);
			p.setColor(Color.RED);
			canvas.drawBitmap(sling, 80, ScreenProps.screenHeight-sling.getHeight(), p);
//			Matrix m = new Matrix();
//			m.setTranslate(80, ScreenProps.screenHeight-sling.getHeight());
//			canvas.drawBitmap(left_el, m, p);
//			m.postTranslate(160, 0);
//			canvas.drawBitmap(right_el, m, p);
//			m.setTranslate(100, ScreenProps.screenHeight-sling.getHeight()+left_el.getHeight());
			canvas.drawBitmap(socket, x-socket.getWidth()/2, y-socket.getHeight()/2, p);
			
			Path path = new Path();
			path.setLastPoint(0, 0);
			
			int b = 200;
			int a = 200;
			path.lineTo(b, a);
			
			double alpha = Math.asin(a / Math.sqrt(a*a + b*b));
			Log.d("alpha", ""+(a / Math.sqrt(a*a + b*b)));
			  
			double beta = Math.PI/2 - alpha;
			
			double l = 50;
			double y = Math.sin(beta)*l;
			double x = Math.sqrt(l*l - y*y);
			
			path.lineTo((int)(b+x), (int)(a-y));
			
			Path p2 = getRectangle(150, 250, 50, 50, 100);
			p.setPathEffect(new CornerPathEffect(15));
			canvas.drawPath(p2, p);
			invalidate();
	}
		
		private Path getRectangle(int x1, int y1, int x2, int y2, int l) {
			Path path = new Path();
			path.setLastPoint(x1, y1);
			path.lineTo(x2, y2);
			
			int b = x2 - x1;
			int a = y2 - y1;
			
			double alpha = Math.asin(a / Math.sqrt(a*a + b*b));
			double beta = Math.PI/2 - alpha;
			
			double y = Math.sin(beta)*l;
			double x = Math.sqrt(l*l - y*y);
			
			path.lineTo((int)(x2+x), (int)(y2-y));
			
			//ok, one more point
			path.lineTo((int)(x1+x), (int)(y1-y));
			path.lineTo((int)(x1), (int)(y1));
			path.close();
			
			return path;
		}

	
}