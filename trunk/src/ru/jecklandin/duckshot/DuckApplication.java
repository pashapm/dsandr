package ru.jecklandin.duckshot;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Handler;

public class DuckApplication extends Application {

	private Match mCurrentMatch;
    private static Typeface mCommonTypeface;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mCommonTypeface = Typeface.createFromAsset(getAssets(), "KOMIKAX_.ttf");
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
