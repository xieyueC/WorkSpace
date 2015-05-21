package com.newer.myweather.entity;

import java.io.Serializable;

public class ForecastDay implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String weekday;
	private String date;
	private String temper;
	private String chance;
	private String imageView;
	
	public ForecastDay() {
		
	}
	
	public String getWeekday() {
		return weekday;
	}
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTemper() {
		return temper;
	}
	public void setTemper(String temper) {
		this.temper = temper;
	}
	public String getChance() {
		return chance;
	}
	public void setChance(String chance) {
		this.chance = chance;
	}
	public String getImageView() {
		return imageView;
	}
	public void setImageView(String imageView) {
		this.imageView = imageView;
	}

	@Override
	public String toString() {
		return "ForecastDay [weekday=" + weekday + ", date=" + date
				+ ", temper=" + temper + ", chance=" + chance + ", imageView="
				+ imageView + "]";
	}
	
}
