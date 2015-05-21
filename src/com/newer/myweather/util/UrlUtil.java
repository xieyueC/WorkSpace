package com.newer.myweather.util;

public class UrlUtil {

	private String API_KEY = "c02437bbbaa38cc6";
	private String URL_AUTOIP = "http://api.wunderground.com/api/c02437bbbaa38cc6/geolookup/lang:CN/q/autoip.json";
	private String URL_LOCATION = "http://api.wunderground.com/api/c02437bbbaa38cc6/geolookup/lang:CN/q/28.13725106,112.99305086.json";
	private String URL_RECENT = "http://api.wunderground.com/api/c02437bbbaa38cc6/conditions/lang:CN/q/zmw:00000.1.57679.json";
	private String URL_FOURDAY = "http://api.wunderground.com/api/c02437bbbaa38cc6/forecast/lang:CN/q/zmw:00000.1.57679.json";
	private String URL_TENDAY = "http://api.wunderground.com/api/c02437bbbaa38cc6/forecast10day/lang:CN/q/zmw:00000.1.57679.json";
	
	public String getAPI_KEY() {
		return API_KEY;
	}
	public String getURL_AUTOIP() {
		return URL_AUTOIP;
	}
	public String getURL_LOCATION() {
		return URL_LOCATION;
	}
	public String getURL_RECENT() {
		return URL_RECENT;
	}
	public String getURL_FOURDAY() {
		return URL_FOURDAY;
	}
	public String getURL_TENDAY() {
		return URL_TENDAY;
	}
	
}
