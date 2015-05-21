package com.newer.myweather.MyAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.newer.myweather.R;
import com.newer.myweather.fragment.CurrentFragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class currentAdapter extends BaseAdapter{

	private static final String TAG = "currentAdapter";
	private Context context;
	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater layoutInflater;
	
	public currentAdapter(Context context,ArrayList<HashMap<String, Object>> data){
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(this.context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder{
		//GridView
		ImageView sky;
		TextView day;
		TextView maxt;
		TextView mint;
		TextView weather;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "getView");
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.current_item, null);
			holder = new ViewHolder();
			holder.day = (TextView) convertView.findViewById(R.id.tv_day);
			holder.maxt = (TextView) convertView.findViewById(R.id.tv_maxT);
			holder.mint = (TextView) convertView.findViewById(R.id.tv_minT);
			holder.weather = (TextView) convertView.findViewById(R.id.tv_weather);
			holder.sky = (ImageView) convertView.findViewById(R.id.iv_sky);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.d(TAG, "data length # " + data.size());
		
		//设置数据
		holder.day.setText(data.get(position).get(CurrentFragment.KEY_DAY).toString());
		holder.maxt.setText(data.get(position).get(CurrentFragment.KEY_MAXT).toString());
		holder.mint.setText(data.get(position).get(CurrentFragment.KEY_MINT).toString());
		holder.weather.setText(data.get(position).get(CurrentFragment.KEY_WEATHER).toString());
		try {
			holder.sky.setImageBitmap(BitmapFactory.decodeStream(this.context
					.getAssets().open(
							data.get(position).get(CurrentFragment.KEY_SKY)
									.toString())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}

}
