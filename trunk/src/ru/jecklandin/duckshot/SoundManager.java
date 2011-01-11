package ru.jecklandin.duckshot;

import ru.jecklandin.duckshot.levels.Level;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;

public class SoundManager {
	
	public static int SOUND_PUNCH = 0;
	
	private static SoundManager sInstance;
	
	private Level mLevel;
	
	private SoundPool mPool;
	private Context mCtx;
	private int[] mSounds = new int[6];
	
	private int mSound = 4;
	private int mEffects = 4;
	
	private MediaPlayer mPlayer = new MediaPlayer();
	
	private SoundManager(Context ctx) {
		super();
		mCtx = ctx;
		sInstance = this;
		readSettings();
		
		mPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		
		try {
			mPlayer.setDataSource(mCtx, Uri.parse("android.resource://ru.jecklandin.duckshot/"+R.raw.music));
			mPlayer.setLooping(true);
			mPlayer.prepare();
			mPlayer.setVolume(mSound/8f, mSound/8f);
        } catch (Exception e) {
			e.printStackTrace(); 
		} 
	} 
	
	public static void initialize(Context ctx) {
		if (sInstance == null) {
			sInstance = new SoundManager(ctx);
		}
	}
	 
	public static SoundManager getInstance() {
		return sInstance;
	}
	
	public void playQuack() {
		int s = (int) (Math.random()*3);
		play(mSounds[s]);
	}
	
	public void playHit() {
		play(mSounds[3]);
	}
	
	public void playRockHit() {
		play(mSounds[4]);
	}
	
	public void playBulk() {
		play(mSounds[5]);
	}
	
	private void play(int id) {
		mPool.play(id, mEffects/8f, mEffects/8f, 1, 0, 1);
	}
	
	public void readSettings() {
		SharedPreferences prefs = mCtx.getSharedPreferences("ducks", Context.MODE_PRIVATE);
		mSound = prefs.getInt("sound", 4);
		mEffects = prefs.getInt("effects", 4);
		mPlayer.setVolume(mSound/8f, mSound/8f);
	}
	
	public void turnMusic(boolean on) {
		if (on) {
			mPlayer.start();
		} else {
			mPlayer.pause();
		}
	}
	
	public void seekAtZero() {
		mPlayer.seekTo(0);
	}
	
	public void loadSounds(Level level) {
		mLevel = level;
		mSounds[0] = mPool.load(mCtx, mLevel.mCreatureVoices[0], 1);
		mSounds[1] = mPool.load(mCtx, mLevel.mCreatureVoices[1], 1);
		mSounds[2] = mPool.load(mCtx, mLevel.mCreatureVoices[2], 1);
		mSounds[3] = mPool.load(mCtx, mLevel.mPunch, 1);
		
		mSounds[4] = mPool.load(mCtx, mLevel.mStoneHit, 1);
		mSounds[5] = mPool.load(mCtx, R.raw.bulk, 1);
	}
	
}
