package com.newer.myweather.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.newer.myweather.R;
import com.newer.myweather.R.id;
import com.newer.myweather.R.layout;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class RecentFragment extends Fragment {
	
	public static final String KEY_WEEK = "week";
	public static final String KEY_TIME = "time";
	public static final String KEY_TEMPERATURE = "temperature";
	public static final String KEY_PROBABILITY = "probability";
	ListView listView;
	SimpleAdapter adapter;
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
	
	public RecentFragment(){
		
		String [] week = {"周四","周五"};
		String [] time = {"早晨","下午","晚间","夜晚"};
		for (int i = 0; i < 8; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put(KEY_WEEK, week[i<4?0:1]);
			item.put(KEY_TIME, time[i%4]);
			
			data.add(item);
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_recent, container, false);
		
		listView = (ListView) view.findViewById(R.id.lv_recent);
		String[] from = {KEY_WEEK,KEY_TIME};
		int[] to = {R.id.week,R.id.time};
		adapter = new SimpleAdapter(getActivity(), data, R.layout.recent_item, from, to);
		
		listView.setAdapter(adapter);
		return view;
	}
	
	
}