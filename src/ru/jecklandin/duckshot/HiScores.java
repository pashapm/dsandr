package ru.jecklandin.duckshot;
import java.io.UTFDataFormatException;
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

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class HiScores extends Activity {

	public void addScore(Score score) {
		
		Score[] scores = getScores();
		Score[] newscores = new Score[scores.length+1];
		newscores[scores.length] = score;
		for (int i=0; i<scores.length; ++i) {
			newscores[i] = scores[i];
		}

		Arrays.sort(newscores, new Comparator<Score>() {

			@Override
			public int compare(Score object1, Score object2) {
				if (object1.score < object2.score) {
					return 1;
				} else if (object1.score > object2.score) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		
		
		SharedPreferences prefs = getSharedPreferences("DuckShot", MODE_PRIVATE);
		Editor ed = prefs.edit();
		for (int i=0; i<newscores.length; ++i) {
			ed.putString(""+i, newscores[i]+"");
			Log.d("#######", ""+i + " " + newscores[i]+"");
			ed.commit();
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscores);
//        addScore(new Score("QQQ", 423));
//        addScore(new Score("QQ1Q", 42546));
        addScore(new Score("QQ2Q", 5000));
//        
//        addScore(new Score("QQ3Q", 8423));
//        addScore(new Score("QQ4Q", 4263));
//        addScore(new Score("QQ5Q", 1323));
//        addScore(new Score("QQ6Q", 4273));
        Score[] scores = getScores();
        
        HiscoresAdapter adap = new HiscoresAdapter(this, R.layout.scorestring, scores);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adap);
	}
	
	public Score[] getScores() {
		List<Score> scores = new LinkedList<Score>();
		SharedPreferences prefs = getSharedPreferences("DuckShot", MODE_PRIVATE);
        for (int i=0; i<20; ++i) {
        	String sc = prefs.getString(""+i, "");
        	if (sc != "") {
        		Pattern pat = Pattern.compile("(.*) (\\d+)");
        		Matcher mat = pat.matcher(sc);
        		mat.find();
        		scores.add(new Score(mat.group(1), Integer.parseInt(mat.group(2))));
        	} 
        }
        return scores.toArray(new Score[scores.size()]); 
	}
	
}


class HiscoresAdapter extends ArrayAdapter<Score> {

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
		
	}
	
}


