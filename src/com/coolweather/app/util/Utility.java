package com.coolweather.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;
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
				Log.i("MainActivity", province.error_code);
				Log.i("MainActivity", province.resultcode);
				Log.i("MainActivity", province.reason);
				for (City c : province.result) {
					coolWeatherDB.saveProvince(c);
				}
				return true;
			}
		}
		return false;
	}
}
