package ru.jecklandin.duckshot;

import java.util.Vector;

public class Level {
	public static Vector<Level> sLevels = new Vector<Level>(10);
	static {
//		sLevels.add(new Level(num, time, mox))
	}
	
	public Level(int num,int time,int mox) {
		mDuckMoxie = mox;
		mDuckNumber = num;
		mMatchTime = time;
	}
	
	int mDuckNumber;
	int mMatchTime;
	int mDuckMoxie;
}
