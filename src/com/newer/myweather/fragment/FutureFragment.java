package com.newer.myweather.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.newer.myweather.R;
import com.newer.myweather.MyAdapter.futureAdapter;
import com.newer.myweather.entity.ForecastDay;
import com.newer.myweather.service.WeatherService;

public class FutureFragment extends Fragment implements OnRefreshListener {

	public static final String REFRESH_FUTUREFRAGMENT = "refresh_futurefragment";
	public static final String KEY_WEEKDAY = "weekday";
	public static final String KEY_DATE = "date";
	public static final String KEY_TEMPER = "temper";
	public static final String KEY_CHANCE = "chance";
	public static final String KEY_IMAGEVIEW = "imageView";
	private static final String TAG = "FutureFragment";

	private SwipeRefreshLayout refreshLayout;
	private ListView lv_future;
	SimpleAdapter adapter;
	ArrayList<ForecastDay> data = new ArrayList<ForecastDay>();
	private boolean flag = true;
	futureAdapter futureAdapter;
	View view;
	private boolean bool = false;
	private Intent intentService;
	private LinearLayout layoutProgress;
	private LinearLayout layoutContent;

	private BroadcastReceiver FutureDataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onReceive 接收数据");
			ArrayList<ForecastDay> dataFuture = new ArrayList<ForecastDay>();
			dataFuture = (ArrayList<ForecastDay>) intent
					.getSerializableExtra(REFRESH_FUTUREFRAGMENT);
			Future(dataFuture);
			Log.d(TAG, "onReceive dataFuture.size = " + dataFuture.size());
			// 数据更新
			futureAdapter.notifyDataSetChanged();

			// 动画结束
			refreshLayout.setRefreshing(false);
			Log.d(TAG, "FutureFragment 刷新成功");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_future, container, false);

		refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout);
		lv_future = (ListView) view.findViewById(R.id.lv_future);
		layoutProgress = (LinearLayout) view.findViewById(R.id.layout_progress);
		layoutContent = (LinearLayout) view.findViewById(R.id.layout_content);

		refreshLayout.setColorScheme(R.color.gray, R.color.blue, R.color.gray,
				R.color.blue);
		refreshLayout.setOnRefreshListener(this);

		return view;
	}

	public void Future(ArrayList<ForecastDay> data) {
		Log.d(TAG, "Future");

		this.data = data;
		Log.d(TAG, "" + data.size());

		layoutProgress.setVisibility(View.INVISIBLE);
		layoutContent.setVisibility(View.VISIBLE);
		futureAdapter = new futureAdapter(view.getContext(), data);
		lv_future.setAdapter(futureAdapter);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		Log.d(TAG, data.size() + "");
		if (bool == true) {
			Future(data);
		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		bool = true;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// 注销广播接收器
		getActivity().getApplicationContext().unregisterReceiver(
				FutureDataReceiver);
		Log.d(TAG, "注销广播接收器");
	}

	@Override
	public void onRefresh() {
		Log.d(TAG, "onRefresh");

		// 注册接收更新UI的数据的广播
		IntentFilter filter = new IntentFilter(
				"com.newer.myweather.ACTION_UPDATE_FUTURE");
		getActivity().getApplicationContext().registerReceiver(
				FutureDataReceiver, filter);

		intentService = new Intent(getActivity(), WeatherService.class);
		getActivity().getApplicationContext().startService(intentService);
	}

}