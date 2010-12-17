package ru.jecklandin.duckshot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class HiScores extends Activity {

	private List<Score> readScores(File file) throws Exception {
		if (! file.exists()) {
			file.createNewFile();
			writeScoresFile(null, file);
			return null;
		}
		
		List<Score> scores = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new FileInputStream(file), null);
        int eventType = parser.getEventType();
        Score currentScore = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
        	String name = null;
        	switch (eventType){
            case XmlPullParser.START_TAG:
            	 name = parser.getName();
            	 if (name.equalsIgnoreCase("elements")){
            		 scores = new ArrayList<Score>();
                 } else if (name.equalsIgnoreCase("score")) {
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
	
	private void writeScoresFile(List<Score> scores, File file) throws Exception {
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
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscores);
        
//        List<Score> scores = new ArrayList<Score>();
//        scores.add(new Score("sdds", 243));
//        scores.add(new Score("ds", 343));
//        scores.add(new Score("sdgfsds", 53));
//        scores.add(new Score("ddf", 56));
        
      //File file = new File(getDir("hires", MODE_WORLD_READABLE), "scores.xml");
		File file = new File(Environment.getExternalStorageDirectory(), "scores.xml");
        
//		try {
//			writeScoresFile(scores, file);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		Score[] objects = new Score[0];
        try {
        	List<Score> scores = readScores(file);
			objects = scores.toArray(objects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Arrays.sort(objects, new Comparator<Score>() {

			@Override
			public int compare(Score object1, Score object2) {
				if (object1.score > object2.score) {
					return -1;
				} else if (object1.score < object2.score) {
					return 1;
				} 
				return 0;
			}
		});
		
        HiscoresAdapter adap = new HiscoresAdapter(this, R.layout.scorestring, objects);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adap);
	}
}


class HiscoresAdapter extends ArrayAdapter<Score> {

	protected Score[] mScores;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(getContext(), R.layout.scorestring, null);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView score = (TextView) convertView.findViewById(R.id.score);
		if (getItem(position) != null && getItem(position).name != null) {
			name.setText(getItem(position).name);
			score.setText(getItem(position).score + "");
		} else { 
			name.setText("Name");
			score.setText("Score");
		}
		return convertView;
	}

	public HiscoresAdapter(Context context, int textViewResourceId,
			Score[] objects) {
		super(context, textViewResourceId, objects);
		mScores = objects;
	}
	
}


