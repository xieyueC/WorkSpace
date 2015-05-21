package com.newer.myweather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.newer.myweather.entity.ForecastDay;
import com.newer.myweather.fragment.CurrentFragment;
import com.newer.myweather.fragment.FutureFragment;
import com.newer.myweather.fragment.RecentFragment;
import com.newer.myweather.service.WeatherService;

public class MainActivity extends Activity implements OnNavigationListener{

	private static final String TAG = "MainActivity";
	public static final String UPDATE_UI_CURRENTFRAGMENT = "update_ui_currentfragment";
	public static final String UPDATE_UI_FUTUREFRAGMENT = "update_ui_futurefragment";
	public static final String UPDATE_UI_RECENTFRAGMENT = "update_ui_recentfragment";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	CurrentFragment currentFragment;
	RecentFragment recentFragment;
	FutureFragment futureFragment;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
//	PagerTabStrip
//	PagerTitleStrip
	
	ViewPager mViewPager;

	ActionBar actionBar;

	private Intent intentService;

	private ArrayAdapter<String> abAdapter;
	private ArrayList<String> abData = new ArrayList<String>();

	// 动态广播接收器
	private BroadcastReceiver uiDataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onReceive");
			ArrayList<ArrayList<HashMap<String, Object>>> dataCurrent = new ArrayList<ArrayList<HashMap<String, Object>>>();
			dataCurrent = (ArrayList<ArrayList<HashMap<String, Object>>>) intent
					.getSerializableExtra(UPDATE_UI_CURRENTFRAGMENT);
			currentFragment.Current(dataCurrent);
			Log.d("dataCurrent @ ", dataCurrent.size() + "");

			ArrayList<ForecastDay> dataFuture = new ArrayList<ForecastDay>();
			dataFuture = (ArrayList<ForecastDay>) intent
					.getSerializableExtra(UPDATE_UI_FUTUREFRAGMENT);
			futureFragment.Future(dataFuture);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate");

		currentFragment = new CurrentFragment();
		recentFragment = new RecentFragment();
		futureFragment = new FutureFragment();
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// 选中中间的位置
		mViewPager.setCurrentItem(1, true);

		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		abData.add("Changsha,China");
		abData.add("Yueyang,China");
		abAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, abData);
		abAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(abAdapter, this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");

		// 注册接收更新UI的数据的广播
		IntentFilter filter = new IntentFilter(
				"com.newer.myweather.ACTION_UPDATE_UI");
		registerReceiver(uiDataReceiver, filter);

		// 启动更新服务
		intentService = new Intent(this, WeatherService.class);
		startService(intentService);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
		// 注销广播接收器
		unregisterReceiver(uiDataReceiver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected");

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.action_refresh:
			doRefresh();

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void doRefresh() {
		Log.d(TAG, "doRefresh");
		// TODO Auto-generated method stub
		startService(new Intent(this, WeatherService.class));
	}
	
	/**
	 * ViewPager用的适配器，用于切换碎片 A {@link FragmentPagerAdapter} that returns a
	 * fragment corresponding to one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private static final String TAGS = "SectionsPagerAdapter";

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			Log.d(TAGS, "SectionsPagerAdapter");
		}

		@Override
		public Fragment getItem(int position) {
			// Log.d(TAGS, "getItem");

			switch (position) {
			case 0:

				return recentFragment;
			case 1:

				return currentFragment;
			case 2:

				return futureFragment;

			}

			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			// Log.d(TAGS, "getCount");

			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Log.d(TAGS, "getPageTitle");

			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	// ActionBar下拉列表的监听事件
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onNavigationItemSelected");

		// Toast.makeText(this, abData.get(itemPosition), Toast.LENGTH_SHORT)
		// .show();
		return true;
	}

}
