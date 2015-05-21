package com.newer.myweather.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.newer.myweather.MainActivity;
import com.newer.myweather.entity.ForecastDay;
import com.newer.myweather.fragment.CurrentFragment;
import com.newer.myweather.fragment.FutureFragment;
import com.newer.myweather.fragment.RecentFragment;
import com.newer.myweather.util.UrlUtil;
import com.newer.myweather.util.WebUtil;

import android.R;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

public class WeatherService extends IntentService {

	private static final String TAG = "WeatherService";
	private static final String TENDAY = "tenday.js";
	private static final String FOURDAY = "fourday.js";
	private static final String RECENT = "recent.js";
	private String unit;
	private boolean flag;

	CurrentFragment currentFragment;
	FutureFragment futureFragment;
	RecentFragment recentFragment;
	UrlUtil urlUtil = new UrlUtil();

	public WeatherService() {
		super("");
		Log.d(TAG, "WeatherService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreate");

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		unit = sp.getString("unit", "N");

	}

	private ArrayList<ArrayList<HashMap<String, Object>>> doCurrent() {
		Log.d(TAG, "doCurrent");
		ArrayList<ArrayList<HashMap<String, Object>>> datas = new ArrayList<ArrayList<HashMap<String, Object>>>();

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> griddata = new ArrayList<HashMap<String, Object>>();

		data = parseCurrentUp();
		griddata = parseCurrentDown();

		datas.add(data);
		datas.add(griddata);
		Log.d(TAG, datas.toString());
		return datas;
	}

	// 更新CurrentFragment GridView之上的信息
	private ArrayList<HashMap<String, Object>> parseCurrentUp() {
		Log.d(TAG, "parseCurrentUp");
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String json = null;
		try {

			// 将json文件读取到buffer数组中
			// InputStream is = getApplication().getAssets().open("recent.js");
			// InputStream is =
			// getResources().openRawResource(com.newer.myweather.R.raw.recent);
			InputStream is = openFileInput("recent.js");
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int size;
			while (-1 != (size = is.read(buffer))) {
				bao.write(buffer, 0, size);
			}
			is.close();
			byte[] dataJson = bao.toByteArray();
			// 将字节数组转换为以GB2312编码的字符串
			json = new String(dataJson, "GB2312");

			// 将字符串json转换为json对象，以便于取出数据
			JSONObject jsonObject = new JSONObject(json);

			JSONObject current = jsonObject
					.getJSONObject("current_observation");
			String tempture = current.getString("temp_c") + "°";
			String weather = current.getString("weather");
			String relative_humidity = current.getString("relative_humidity");
			String wind_dir = current.getString("wind_dir");
			String wind_kph = current.getString("wind_kph");
			String humidity = "HUMIDITY" + relative_humidity;
			String wind = String.format("WIND %skm/h %s", wind_kph, wind_dir);
			String imageView1 = current.getString("icon") + ".gif";

			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put(currentFragment.TEMPERATURE, tempture);
			item.put(currentFragment.WEATHER, weather);
			item.put(currentFragment.HUMIDITY, humidity);
			item.put(currentFragment.WIND, wind);
			item.put(currentFragment.IMAGEVIEW1, imageView1);

			data.add(item);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	// 更新CurrentFragment GridView的信息
	private ArrayList<HashMap<String, Object>> parseCurrentDown() {
		Log.d(TAG, "parseCurrentDown");
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String json = null;
		try {
			// 将json文件读取到buffer数组中
			// InputStream is =
			// getApplication().getAssets().open("fourday.js");
			// InputStream is =
			// getResources().openRawResource(com.newer.myweather.R.raw.fourday);
			InputStream is = openFileInput("fourday.js");
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int size;
			while (-1 != (size = is.read(buffer))) {
				bao.write(buffer, 0, size);
			}
			is.close();
			byte[] dataJson = bao.toByteArray();
			// 将字节数组转换为以GB2312编码的字符串
			json = new String(dataJson, "GB2312");

			// 将字符串json转换为json对象，以便于取出数据
			JSONObject jsonObject = new JSONObject(json);

			JSONObject forecast = jsonObject.getJSONObject("forecast");

			StringBuffer strBuf = new StringBuffer();

			JSONObject simpleforecast = forecast
					.getJSONObject("simpleforecast");
			JSONArray array = simpleforecast.getJSONArray("forecastday");

			for (int i = 0; i < array.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject item = array.getJSONObject(i);
				JSONObject date = item.getJSONObject("date");
				String day = date.getString("weekday_short");
				Log.d("day=", day);

				JSONObject high = item.getJSONObject("high");
				String maxT = high.getString("celsius") + "°";
				Log.d("maxT=", maxT);

				JSONObject low = item.getJSONObject("low");
				String minT = low.getString("celsius") + "°";

				String weather = item.getString("conditions");
				String sky = item.getString("icon") + ".gif";

				map.put(currentFragment.KEY_DAY, day);
				map.put(currentFragment.KEY_MAXT, maxT);
				map.put(currentFragment.KEY_MINT, minT);
				map.put(currentFragment.KEY_WEATHER, weather);
				map.put(currentFragment.KEY_SKY, sky);
				data.add(map);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onHandleIntent");
		Log.d(TAG, "单位" + unit);

		flag = WebUtil.getNetState(getApplication());
		if (flag) {
			// showToast("有可用网络");
			Log.d(TAG, "有可用网络");
			downloadLocation(urlUtil.getURL_TENDAY(), TENDAY);
			downloadLocation(urlUtil.getURL_FOURDAY(), FOURDAY);
			downloadLocation(urlUtil.getURL_RECENT(), RECENT);
		} else {
			// showToast("没有可用网络");
			Log.d(TAG, "没有可用网络");
		}

		ArrayList<ArrayList<HashMap<String, Object>>> dataCurrent = doCurrent();
		ArrayList<ForecastDay> dataFuture = doFuture();

		Intent intentUIData = new Intent("com.newer.myweather.ACTION_UPDATE_UI");
		intentUIData.putExtra(MainActivity.UPDATE_UI_CURRENTFRAGMENT,
				dataCurrent);
		intentUIData
				.putExtra(MainActivity.UPDATE_UI_FUTUREFRAGMENT, dataFuture);
		sendBroadcast(intentUIData);
		Log.d(TAG, "onHandleIntent 发送 ");
		
		ArrayList<ForecastDay> dataRefreshFuture = doFuture();
		Intent refreshUIIntent = new Intent("com.newer.myweather.ACTION_UPDATE_FUTURE");
		refreshUIIntent.putExtra(FutureFragment.REFRESH_FUTUREFRAGMENT, dataRefreshFuture);
		sendBroadcast(refreshUIIntent);
	}

	private void downloadLocation(String uri, String urlname) {

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(uri);

		try {
			HttpResponse response = client.execute(get);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

				String data = EntityUtils.toString(response.getEntity(),
						"GB2312");

				// 内部存储
				FileOutputStream out = openFileOutput(urlname, MODE_PRIVATE);
				out.write(data.getBytes("GB2312"));
				out.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplication(), string, Toast.LENGTH_SHORT).show();
	}

	private ArrayList<HashMap<String, Object>> doRecent() {
		Log.d(TAG, "doRecent");
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		try {

			// 将json文件读取到buffer数组中
			InputStream is = getApplication().getAssets().open("fourday.js");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);

			// 将字节数组转换为以GB2312编码的字符串
			String json = new String(buffer, "GB2312");

			// 将字符串json转换为json对象，以便于取出数据
			JSONObject jsonObject = new JSONObject(json);

			JSONObject forecast = jsonObject.getJSONObject("forecast");

			StringBuffer strBuf = new StringBuffer();

			JSONObject simpleforecast = forecast
					.getJSONObject("simpleforecast");
			JSONArray array = simpleforecast.getJSONArray("forecastday");

			for (int i = 0; i < array.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject item = array.getJSONObject(i);
				JSONObject date = item.getJSONObject("date");
				String week = date.getString("weekday_short");
				Log.d("day=", week);

				JSONObject high = item.getJSONObject("high");
				String maxT = high.getString("celsius");
				Log.d("maxT=", maxT);

				JSONObject low = item.getJSONObject("low");
				String minT = low.getString("celsius");

				int count = (Integer.parseInt(maxT) + Integer.parseInt(minT)) / 2;
				String temperature = count + "°";

				String probability = item.getString("pop");

				map.put(recentFragment.KEY_WEEK, week);
				map.put(recentFragment.KEY_TIME, maxT);
				map.put(recentFragment.KEY_TEMPERATURE, temperature);
				map.put(recentFragment.KEY_PROBABILITY, probability);
				data.add(map);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public ArrayList<ForecastDay> doFuture() {
		Log.d(TAG, "doFuture");
		ArrayList<ForecastDay> data = new ArrayList<ForecastDay>();
		String json = null;
		try {
			// 将json文件读取到buffer数组中
			// 资源可以放在raw里，getResources().openRawResource(R.raw.current);
			// InputStream is =
			// getResources().openRawResource(com.newer.myweather.R.raw.tenday);
			Log.d(TAG, "代码执行");
			InputStream is = openFileInput("tenday.js");
			Log.d(TAG, "代码不执行");
			// InputStream is =
			// getApplication().getAssets().open("tenday.js");
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int size;
			while (-1 != (size = is.read(buffer))) {
				bao.write(buffer, 0, size);
			}
			is.close();
			byte[] dataJson = bao.toByteArray();
			// 将字节数组转换为以UTF-8编码的字符串
			json = new String(dataJson, "GB2312");

			// 将字符串json转换为json对象，以便于取出数据
			JSONObject jsonObject = new JSONObject(json);

			JSONObject forecast = jsonObject.getJSONObject("forecast");
			JSONObject simpleforecast = forecast
					.getJSONObject("simpleforecast");

			JSONArray array = simpleforecast.getJSONArray("forecastday");

			for (int i = 0; i < array.length(); i++) {
				ForecastDay forecastDay = new ForecastDay();

				JSONObject item = array.getJSONObject(i);
				JSONObject date = item.getJSONObject("date");
				String weekday = date.getString("weekday_short");
				String month = date.getString("month");
				String day = date.getString("day");
				String dates = String.format("%s月%s", month, day);

				JSONObject high = item.getJSONObject("high");
				String maxT = high.getString("celsius");
				JSONObject low = item.getJSONObject("low");
				String minT = low.getString("celsius");
				String temper = String.format("%s°/%s°", maxT, minT);

				String avehumidity = item.getString("avehumidity");
				String chance = avehumidity + "%";
				String imageView = item.getString("icon") + ".gif";

				forecastDay.setWeekday(weekday);
				forecastDay.setDate(dates);
				forecastDay.setTemper(temper);
				forecastDay.setChance(chance);
				forecastDay.setImageView(imageView);
				
				Log.d("URL # ", imageView);

				data.add(forecastDay);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.d(TAG, "onDestroy");
	}

}
