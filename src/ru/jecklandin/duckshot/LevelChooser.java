package ru.jecklandin.duckshot;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LevelChooser extends ListActivity {

	private LevelAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.level_chooser);
		((TextView) findViewById(R.id.TextView01)).setTypeface(DuckApplication.getCommonTypeface());
		
		Level[] lvls = new Level[5];
		
		lvls[0] = new Level(1, R.drawable.level_thumb1, "Rocky Lakes");
		lvls[1] = new Level(0, R.drawable.level_thumb0, "Unknown");
		lvls[2] = new Level(0, R.drawable.level_thumb0, "Unknown");
		lvls[3] = new Level(0, R.drawable.level_thumb0, "Unknown");
		lvls[4] = new Level(0, R.drawable.level_thumb0, "Unknown");
		
		mAdapter = new LevelAdapter(lvls);
		getListView().setAdapter(mAdapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i1 = new Intent(LevelChooser.this, DuckGame.class);
				i1.setAction(DuckGame.NEW_MATCH);
				startActivity(i1);
			}
		});
		
	}
	
	
	class LevelAdapter extends BaseAdapter {

		private Level[] mLevels;
		
		LevelAdapter(Level[] lvls) {
			mLevels = lvls;
		}
		
		@Override
		public int getCount() {
			return mLevels.length;
		}

		@Override
		public Object getItem(int arg0) {
			return mLevels[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int pos, View arg1, ViewGroup arg2) {
			LinearLayout elem = (LinearLayout) arg1;
			if (elem == null) {
				elem = (LinearLayout) getLayoutInflater().inflate(R.layout.level_chooser_elem, null);
			} 
			
			Level lvl = (Level) getItem(pos);
			
			((ImageView) elem.findViewById(R.id.lvl_thumb)).setImageResource(lvl.mThumb);
			((ImageView) elem.findViewById(R.id.lvl_thumb)).setBackgroundColor(Color.parseColor(lvl.mLevelId == 0 ? "#855b22" : "#180e4e"));
			
			TextView lvl_name = (TextView) elem.findViewById(R.id.lvl_name);
			lvl_name.setText(lvl.mName);
			lvl_name.setTypeface(DuckApplication.getCommonTypeface());
			lvl_name.setTextColor(Color.parseColor(lvl.mLevelId == 0 ? "#ffaf3c" : "#88bae4"));
			
			return elem;
		}
		
	}
	
}
