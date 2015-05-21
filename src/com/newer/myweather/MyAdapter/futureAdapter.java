package com.newer.myweather.MyAdapter;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newer.myweather.R;
import com.newer.myweather.entity.ForecastDay;

public class futureAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ForecastDay> data;
	private LayoutInflater layoutInflater;

	public futureAdapter(Context context,
			ArrayList<ForecastDay> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(this.context);
	}

	static class ViewHolder {
		TextView weekday;
		TextView date;
		TextView temper;
		TextView chance;
		ImageView imageView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.future_item, null);
			holder = new ViewHolder();
			holder.weekday = (TextView) convertView.findViewById(R.id.weekday);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.temper = (TextView) convertView.findViewById(R.id.temper);
			holder.chance = (TextView) convertView.findViewById(R.id.chance);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.weekday.setText(data.get(position).getWeekday());
		holder.date.setText(data.get(position).getDate());
		holder.temper.setText(data.get(position).getTemper());
		holder.chance.setText(data.get(position).getChance());
		
		try {
			holder.imageView.setImageBitmap(BitmapFactory.decodeStream(this.context.getAssets().open(data.get(position).getImageView())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Log.d("图片URL:", data.get(position).getImageView());
		
		return convertView;
	}

}