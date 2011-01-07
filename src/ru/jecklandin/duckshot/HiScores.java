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

import ru.jecklandin.duckshot.levels.Level;
import ru.jecklandin.duckshot.levels.LevelManager;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


public class HiScores extends Activity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscores);
         
        findViewById(R.id.hiscores_lay).setBackgroundResource(R.drawable.underwater_dith);
        ((TextView)findViewById(R.id.hiscores_label)).setTypeface(DuckApplication.getCommonTypeface());
        
//        Spinner spin = (Spinner) findViewById(R.id.lvl_spinner);
//        LevelSpinnerAdapter spinnerAdapter = new LevelSpinnerAdapter(this, R.layout.select_lvl_elem, R.id.lvl_name, 
//        		LevelManager.getInstance().getLevels().toArray(new Level[0]));
//        spin.setAdapter(spinnerAdapter);
//        spin.setSelection(0, false);
        
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

class LevelSpinnerAdapter extends ArrayAdapter<Level> {

	public LevelSpinnerAdapter(Context context, int res, int textViewResourceId,
			Level[] objects) {
		super(context, res, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inf = LayoutInflater.from(getContext());
			convertView = inf.inflate(R.layout.select_lvl_elem, null);
		}
		TextView tw = (TextView) convertView.findViewById(R.id.lvl_name);
		tw.setTypeface(DuckApplication.getCommonTypeface());
		tw.setTextColor(Color.BLACK);
		tw.setText("Level "+getItem(position).mLevelId);
		return tw;
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
		
//		name.setTypeface(DuckApplication.getCommonTypeface());
		score.setTypeface(DuckApplication.getCommonTypeface());
		pos.setTypeface(DuckApplication.getCommonTypeface());
		
		if (position == 0) {
			score.setTextColor(Color.parseColor("#ff5700"));
		}
		
		return convertView;
	}

	public HiscoresAdapter(Context context, int textViewResourceId,
			Score[] objects) {
		super(context, textViewResourceId, objects);
		mScores = objects;
	}
	
}


