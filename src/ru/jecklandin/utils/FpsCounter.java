package ru.jecklandin.utils;

public class FpsCounter {

	private static int fps = 0;
	private static int draws = 0;
	private static long prevMillis = System.currentTimeMillis();

	public static void notifyDrawing() {
		long cur = System.currentTimeMillis();
		if (cur - prevMillis > 1000) {
			fps = draws;
			draws = 0;
			prevMillis = System.currentTimeMillis();
		} else {
			draws++;
		}
	}

	public static int getFPS() {
		return fps;
	}
}
