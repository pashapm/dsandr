package ru.jecklandin.duckshot;

import android.app.Application;
import android.os.Handler;

public class DuckApplication extends Application {

	private Match mCurrentMatch;
	
	@Override
	public void onCreate() {
		super.onCreate();
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
