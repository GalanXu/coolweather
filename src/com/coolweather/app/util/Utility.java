package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;
import com.coolweather.app.model.Yubao;
import com.coolweather.app.model.Yubao.ToWeather;
import com.google.gson.Gson;

public class Utility {

	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			Gson gson = new Gson();
			Province province = gson.fromJson(response, Province.class);

			if ("200".equals(province.resultcode)
					&& "successed".equals(province.reason)) {
				for (City c : province.result) {
					coolWeatherDB.saveProvince(c);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	 */
	public static void handleWeatherResponse(Context context, String response) {
		if (!TextUtils.isEmpty(response)) {
			Gson gson = new Gson();
			Yubao yubao = gson.fromJson(response, Yubao.class);

			if ("200".equals(yubao.resultcode)
					&& "查询成功".equals(yubao.reason)) {
//				Log.i("MainActivity", yubao.result.today.city);
//				Log.i("MainActivity", yubao.result.today.temperature);
//				Log.i("MainActivity", yubao.result.today.date_y);
//				Log.i("MainActivity", yubao.result.today.weather);
//				Log.i("MainActivity", yubao.result.sk.time);
				ToWeather today = yubao.result.today;
				
				saveWeatherInfo(context,today.city,today.date_y,today.weather,today.temperature,yubao.result.sk.time);
			}
		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String date_y, String temp1, String weatherDesp,
			String publishTime) {
		Log.i("MainActivity", publishTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月 d日 ", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("date_y", date_y);
		editor.putString("temp1", temp1);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
