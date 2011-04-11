package ru.jecklandin.duckshot;
import ru.jecklandin.duckshot.levels.Level;
import ru.jecklandin.duckshot.levels.LevelManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class HiScores extends Activity {

	private Spinner mSpinner;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscores);
         
        findViewById(R.id.hiscores_lay).setBackgroundResource(R.drawable.underwater_dith);
        ((TextView)findViewById(R.id.hiscores_label)).setTypeface(DuckApplication.getCommonTypeface());
        
        mSpinner = (Spinner) findViewById(R.id.lvl_spinner);
        final LevelSpinnerAdapter spinnerAdapter = new LevelSpinnerAdapter(this, R.layout.select_lvl_elem, R.id.lvl_name, 
        		LevelManager.getInstance().getAvailableLevels().toArray(new Level[0]));
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setSelection(0, false); 
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				loadScores(spinnerAdapter.getItem(pos));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	private void loadScores(Level lvl) {
		try {
			Score[] objects = HiScoresManager.readScores(lvl).toArray(new Score[0]);
			HiscoresAdapter adap = new HiscoresAdapter(this, R.layout.scorestring, objects);
	        ListView list = (ListView) findViewById(android.R.id.list);
	        list.setAdapter(adap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onStart() {
		mSpinner.setSelection(0);
		loadScores(LevelManager.getInstance().getLevels().get(0));
		super.onStart();
	}
}

class LevelSpinnerAdapter extends ArrayAdapter<Level> {

	public LevelSpinnerAdapter(Context context, int res, int textViewResourceId,
			Level[] objects) {
		super(context, res, textViewResourceId, objects);
	}

	@Override
	public boolean isEnabled(int position) {
		return getItem(position).mLevelId != 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inf = LayoutInflater.from(getContext());
			convertView = inf.inflate(R.layout.select_lvl_elem, null);
		}
		TextView tw = (TextView) convertView.findViewById(R.id.lvl_name);
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


