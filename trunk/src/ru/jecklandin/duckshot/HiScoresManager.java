package ru.jecklandin.duckshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import ru.jecklandin.duckshot.levels.Level;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

public class HiScoresManager {

	private static final int SCORES_NUMBER = 10;
	
	private static File getFileForLevel(Level level) {
		String fn = "level"+level.mLevelId+".xml";
//		return new File(Environment.getExternalStorageDirectory(),  fn);
		return new File(DuckApplication.getInstance().getDir("hiscores", Context.MODE_PRIVATE), fn);
	}
	
	/**
	 * Adds new score record for the current level
	 * @param score
	 * @return 
	 */
	public static boolean addScore(Score score) {
		
		Level curLevel = DuckApplication.getInstance().getCurrentLevel();
		File scoresFile = getFileForLevel(curLevel);
		
		Score[] objects = new Score[0];
        try {
        	List<Score> scores = HiScoresManager.readScores(curLevel);
        	if (scores == null) {
        		scores = new ArrayList<Score>();
        	}
        	scores.add(score);
        	objects = scores.toArray(objects);
        	Arrays.sort(objects);
        	if (scores.size() <= SCORES_NUMBER) {
        		writeScoresFile(objects, scoresFile); 
        		return true;
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//weak score
		if (objects[objects.length-1] == score) {
			return false;
		}
		
		Score[] newScores = new Score[10];
		System.arraycopy(objects, 0, newScores, 0, SCORES_NUMBER);
		try {
			writeScoresFile(newScores, scoresFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Reads score records for the current level
	 * @return
	 * @throws Exception
	 */
	static List<Score> readScores(Level lvl) throws Exception {
		
		File scoresFile = getFileForLevel(lvl);
		
		List<Score> scores = new ArrayList<Score>();
		if (! scoresFile.exists()) {
			return scores;
		}
		
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new FileInputStream(scoresFile), null);
        int eventType = parser.getEventType();
        Score currentScore = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
        	String name = null;
        	switch (eventType){
            case XmlPullParser.START_TAG:
            	 name = parser.getName();
            	 if (name.equalsIgnoreCase("score")) {
                	 String sname = parser.getAttributeValue(null, "name");
                	 int value = Integer.parseInt(parser.getAttributeValue(null, "score"));
                	 currentScore = new Score(sname, value);
                	 scores.add(currentScore);
                 }
        	}
        	eventType = parser.next();
        }
        
        return scores;
        
//        for (Score s : scores) {
//        	Log.d("!!!", s.name+" "+s.score);
//        }
	}
	
	private static void writeScoresFile(Score[] scores, File file) throws Exception {
		if (! file.exists()) {
			file.createNewFile();
		}
		
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", "elements");
		if (scores != null) {
			for (Score score : scores) {
				serializer.startTag("", "score");
				serializer.attribute("", "name", score.name);
				serializer.attribute("", "score", score.score + "");
				serializer.endTag("", "score");
			}
		}
		serializer.endTag("", "elements");
		serializer.endDocument();
		
		FileWriter fwr = new FileWriter(file);
		fwr.write(writer.toString());
		fwr.close();
	}
}
