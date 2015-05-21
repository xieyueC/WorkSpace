package com.newer.myweather.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.newer.myweather.MainActivity;
import com.newer.myweather.R;
import com.newer.myweather.MyAdapter.currentAdapter;
import com.newer.myweather.service.WeatherService;

public class CurrentFragment extends Fragment implements OnRefreshListener {

	public static final String KEY_DAY = "day";
	public static final String KEY_MAXT = "maxt";
	public static final String KEY_MINT = "mint";
	public static final String KEY_WEATHER = "weather";
	public static final String KEY_SKY = "sky";

	private static final String TAG = "CurrentFragment";

	public static final String TEMPERATURE = "Temperature";
	public static final String WEATHER = "Weather";
	public static final String HUMIDITY = "Humidity";
	public static final String WIND = "Wind";
	public static final String IMAGEVIEW1 = "imageView1";

	GridView gridView;
	currentAdapter adapter;
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> gridData = new ArrayList<HashMap<String, Object>>();

	private TextView Temperature;
	private TextView Weather;
	private TextView Humidity;
	private TextView Wind;
	private ImageView imageView;
	private boolean flag = true;
	private boolean bool = false;
	View view;
	private SwipeRefreshLayout refreshLayout;
	private ScrollView scrollView;
	private Intent intentService;
	private LinearLayout layoutProgress;
	private LinearLayout layoutContent;

	private BroadcastReceiver CurrentDataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ArrayList<ArrayList<HashMap<String, Object>>> dataCurrent = new ArrayList<ArrayList<HashMap<String, Object>>>();
			dataCurrent = (ArrayList<ArrayList<HashMap<String, Object>>>) intent
					.getSerializableExtra(MainActivity.UPDATE_UI_CURRENTFRAGMENT);
			Current(dataCurrent);
			Log.d(TAG, "刷新 dataCurrent.size = " + dataCurrent.size() + "");

			// 数据更新
			adapter.notifyDataSetChanged();
			// 动画结束
			refreshLayout.setRefreshing(false);
			Log.d(TAG, "onReceive 刷新成功");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_current, container, false);

		gridView = (GridView) view.findViewById(R.id.gridView);

		Temperature = (TextView) view.findViewById(R.id.tv_nowTemperture);
		Weather = (TextView) view.findViewById(R.id.tv_nowWeather);
		Humidity = (TextView) view.findViewById(R.id.tv_humidity);
		Wind = (TextView) view.findViewById(R.id.tv_wind);
		imageView = (ImageView) view.findViewById(R.id.imageView1);
		
		layoutProgress = (LinearLayout) view.findViewById(R.id.layout_progress_current);
		layoutContent = (LinearLayout) view.findViewById(R.id.layout_content_current);

		refreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.layout_current);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);

		refreshLayout
				.setColorScheme(R.color.blue, R.color.gray, R.color.blue, R.color.gray);
		refreshLayout.setOnRefreshListener(this);

		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void Current(ArrayList<ArrayList<HashMap<String, Object>>> datas) {
		Log.d(TAG, "Current");
		Log.d("datas # ", datas.size() + "");
		Log.d("datas # + ", datas.toString());
		// 设置数据
		this.data = datas.get(0);
		Log.d(TAG, "data" + data.size());
		HashMap<String, Object> item1 = data.get(0);

		Temperature.setText(item1.get(CurrentFragment.TEMPERATURE).toString());
		Weather.setText(item1.get(CurrentFragment.WEATHER).toString());
		Humidity.setText(item1.get(CurrentFragment.HUMIDITY).toString());
		Wind.setText(item1.get(CurrentFragment.WIND).toString());
		try {
			imageView.setImageBitmap(BitmapFactory.decodeStream(getActivity()
					.getAssets().open(
							item1.get(CurrentFragment.IMAGEVIEW1).toString())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.gridData = datas.get(1);
		
		layoutProgress.setVisibility(View.INVISIBLE);
		layoutContent.setVisibility(View.VISIBLE);

		adapter = new currentAdapter(view.getContext(), gridData);
		gridView.setAdapter(adapter);
		Log.d("adapter # ", adapter.getCount() + "");
		flag = false;
		Log.d(TAG, "flag #" + flag);
		Log.d(TAG, "gridData length #" + gridData.size());
		// adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");

//		 注销广播接收器
//		getActivity().getApplicationContext().unregisterReceiver(
//				CurrentDataReceiver);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
//		 注销广播接收器
		getActivity().getApplicationContext().unregisterReceiver(
				CurrentDataReceiver);
		Log.d(TAG, "onDestroy 注销广播接收器");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onRefresh");
		// 注册接收更新UI的数据的广播
		IntentFilter filter = new IntentFilter(
				"com.newer.myweather.ACTION_UPDATE_UI");
		getActivity().getApplicationContext().registerReceiver(
				CurrentDataReceiver, filter);

		intentService = new Intent(getActivity(), WeatherService.class);
		getActivity().getApplicationContext().startService(intentService);
	}

}
