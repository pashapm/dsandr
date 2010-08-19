package ru.jecklandin.duckshot;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	enum Sound {PUNCH, QUACK};
	
	private static SoundManager sInstance;
	
	private SoundPool mPool;
	private Context mCtx;
	
	private SoundManager() {
		super();
		sInstance = this;
		mPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		mPool.load(mCtx, R.raw.punch, 1);
	}
	
	public void initialize(Context ctx) {
		sInstance = new SoundManager();
		sInstance.mCtx = ctx;
	}
	
	public static SoundManager getInstance() {
		return sInstance;
	}
	
	public void play(Sound sound) {
		
	}
}
