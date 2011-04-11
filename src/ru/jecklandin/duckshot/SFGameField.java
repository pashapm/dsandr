package ru.jecklandin.duckshot;

import ru.jecklandin.utils.FpsCounter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class SFGameField extends SurfaceView implements SurfaceHolder.Callback
{

	/**
     * Область рисования
     */
    private SurfaceHolder mSurfaceHolder;

    /**
     * Конструктор
     * @param context
     * @param attrs
     */
    public SFGameField(Context context)
    {
        super(context);

        // подписываемся на события Surface
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    /**
     * Изменение области рисования
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    	GameManager man = new GameManager(holder, getContext());
    	man.setRunning(true);
    	man.start();
    	
    }

    @Override
    /**
     * Создание области рисования
     */
    public void surfaceCreated(SurfaceHolder holder)
    {
    }

    @Override
    /**
     * Уничтожение области рисования
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }
}


class GameManager extends Thread
{
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 250;

   /** Область, на которой будем рисовать */
    private SurfaceHolder mSurfaceHolder;

    /** Состояние потока (выполняется или нет. Нужно, чтобы было удобнее прибивать поток, когда потребуется) */
    private boolean mRunning;

    /** Стили рисования */
    private Paint mPaint;

    private ObjectDrawer mDrawer;
    
    /**
     * Конструктор
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     */
    public GameManager(SurfaceHolder surfaceHolder, Context context)
    {
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        mDrawer = ObjectDrawer.getInstance(context);
    }

    /**
     * Задание состояния потока
     * @param running
     */
    public void setRunning(boolean running)
    {
        mRunning = running;
    }

    @Override
    /** Действия, выполняемые в потоке */
    public void run()
    {
    	long lastTime = 0;
        while (mRunning)
        {
        	long c = System.currentTimeMillis();
            Canvas canvas = null;
            try
            {
                // подготовка Canvas-а
                canvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder)
                {
                    // собственно рисование
                	
                	mDrawer.drawObjects(canvas);
                	
                	FpsCounter.notifyDrawing();
                }
                
                
                sleep(20);
            }
            catch (Exception e) { }
            finally
            {
                if (canvas != null)
                {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
                lastTime = (System.currentTimeMillis() - c);
                Log.d("FRAME:", "" + lastTime);
            }
        }
    }
}