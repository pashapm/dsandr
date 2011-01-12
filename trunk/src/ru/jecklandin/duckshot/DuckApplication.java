package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.levels.Level;
import ru.jecklandin.duckshot.model.DuckShotModel;
import ru.jecklandin.duckshot.units.GroundObject;
import ru.jecklandin.duckshot.units.Obstacle;
import android.app.Application;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

public class DuckApplication extends Application {

	private Match mCurrentMatch;
    private static Typeface mCommonTypeface;
    private static SoundPool mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    private static DuckApplication sInstance;
    private static int MUSIC_ID;
    
    public static final String FLURRY_KEY = "TG7D2CT31BQI2GFNSLEP";
    public static final int FPS = 24;
	
    private Level mCurrentLevel;
    
	@Override
	public void onCreate() {
		super.onCreate();
		mCommonTypeface = Typeface.createFromAsset(getAssets(), "KOMIKAX_.ttf");
		DuckApplication.sInstance = this;
	}
	 
	public static DuckApplication getInstance() {
		return sInstance;
	}
	
	public static Typeface getCommonTypeface() {
		return mCommonTypeface;
	}
	
	public Match getCurrentMatch() {
		return mCurrentMatch;
	}
	
	public void newMatch(int seconds, Handler han) {
		mCurrentMatch = new Match(seconds, han);
	}

	public void setHandler(Handler han) {
		if (mCurrentMatch!=null) {
			mCurrentMatch.setHandler(han);
		}
	}
	
	public void setLevel(Level level) {
		mCurrentLevel = level;
		
		ImgManager.loadLevelResources(level); 
		SoundManager.getInstance().loadSounds(level);
		Obstacle.initBitmaps();
		
		DuckShotModel.getInstance().reinitialize(level);
		level.initItemsBitmaps();
	}
	
	public Level getCurrentLevel() {
		return mCurrentLevel;
	}
	
}
