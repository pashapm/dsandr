package ru.jecklandin.duckshot;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	public static int SOUND_PUNCH = 0;
	
	private static SoundManager sInstance;
	
	private SoundPool mPool;
	private Context mCtx;
	private int[] mSounds = new int[3];
	
	private SoundManager(Context ctx) {
		super();
		mCtx = ctx;
		sInstance = this;
		mPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		mSounds[0] = mPool.load(mCtx, R.raw.quack1, 1);
		mSounds[1] = mPool.load(mCtx, R.raw.quack2, 1);
		mSounds[2] = mPool.load(mCtx, R.raw.quack3, 1);
	}
	
	public static void initialize(Context ctx) {
		sInstance = new SoundManager(ctx);
	}
	
	public static SoundManager getInstance() {
		return sInstance;
	}
	
	public void play() {
		int s = (int) (Math.random()*3);
		mPool.play(mSounds[s], 1, 1, 1, 0, 1);
	}
}
