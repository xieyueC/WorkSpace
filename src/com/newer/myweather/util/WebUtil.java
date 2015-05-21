package com.newer.myweather.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WebUtil {

	// 记住网络状态
	public static boolean getNetState(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		} else {
			Log.d("Net_Type", info.getTypeName());
			return true;
		}
	}

	// HttpClient中的POST发送数据
	public static String clientPost(String urlStr, Map<String, String> map) {

		String responseData = "";

		try {
			HttpPost httpPost = new HttpPost(urlStr);

			// 准备要发送的数据List<BasicNameValuePair>
			List<BasicNameValuePair> post = new ArrayList<BasicNameValuePair>();

			for (Map.Entry<String, String> entry : map.entrySet()) {

				BasicNameValuePair bp = new BasicNameValuePair(entry.getKey(),
						entry.getValue());
				post.add(bp);
			}
			//将数据压入到HttpEntity对象，并设置给httpPost
			HttpEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
			httpPost.setEntity(entity);
			//访问网络的对象
			HttpClient client = new DefaultHttpClient();
			//利用访问网络的对象发送httpPost
			HttpResponse httpResponse = client.execute(httpPost);
			//判断服务器是否正确响应
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				responseData = EntityUtils.toString(httpResponse.getEntity());
			} else {
				responseData = "-1";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseData;
	}
}
