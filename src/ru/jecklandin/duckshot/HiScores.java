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

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscores);
        
        ((TextView)findViewById(R.id.hiscores_label)).setTypeface(DuckApplication.getCommonTypeface());
        
		try {
			Score[] objects = HiScoresManager.readScores().toArray(new Score[0]);
			HiscoresAdapter adap = new HiscoresAdapter(this, R.layout.scorestring, objects);
	        ListView list = (ListView) findViewById(android.R.id.list);
	        list.setAdapter(adap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


class HiscoresAdapter extends ArrayAdapter<Score> {

	protected Score[] mScores;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.scorestring, null);
		} 		
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView score = (TextView) convertView.findViewById(R.id.score);
		TextView pos = (TextView) convertView.findViewById(R.id.num);
		
		name.setText(getItem(position).name);
		score.setText(getItem(position).score + "");
		pos.setText((position+1)+".");
		
		name.setTypeface(DuckApplication.getCommonTypeface());
		score.setTypeface(DuckApplication.getCommonTypeface());
		pos.setTypeface(DuckApplication.getCommonTypeface());
		
		return convertView;
	}

	public HiscoresAdapter(Context context, int textViewResourceId,
			Score[] objects) {
		super(context, textViewResourceId, objects);
		mScores = objects;
	}
	
}


