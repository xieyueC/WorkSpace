package com.newer.myweather.broadcast;

import com.newer.myweather.service.WeatherService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.d(TAG, "onReceive action " + action);
		
		// 获得选项信息
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);

		// 设置系统闹铃服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		//定时执行一个事件
		PendingIntent operation = PendingIntent.getService(context, 1,
				new Intent(context, WeatherService.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
				AlarmManager.INTERVAL_HOUR, operation);

//		manager.cancel(operation);//取消
	}

}
