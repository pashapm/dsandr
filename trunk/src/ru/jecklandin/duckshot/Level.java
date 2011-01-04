package ru.jecklandin.duckshot;

import java.util.Vector;

public class Level {
	
	public int mThumb;
	public String mName;
	public int mLevelId;
	
	public Level(int level_id, int thumb, String name) {
		mThumb = thumb;
		mName = name;
		mLevelId = level_id;
	}
}
