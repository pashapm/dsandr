package ru.jecklandin.duckshot;

public class Match extends Thread {

	private long mMatchMs; 
	private boolean mPaused = false;
	
	public Match(int seconds) {
		mMatchMs = seconds * 1000;
	}
	
	public void startMatch() {
		this.start();
	}
	
	public void pauseMatch() {
		mPaused = true;
	}
	
	public void resumeMatch() {
		mPaused = false;
	}
	
	public int secondsRemain() {
		return (int) (mMatchMs/1000);
	}

	@Override
	public void run() {
		while (! isInterrupted()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				return;
			}
			if (!mPaused && mMatchMs > 50) {
					mMatchMs-=50;
			}
		}
	}
}
