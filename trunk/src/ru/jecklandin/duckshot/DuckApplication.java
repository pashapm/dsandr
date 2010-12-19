package ru.jecklandin.duckshot;

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
    
    public static final int FPS = 24;
	
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
	
}
