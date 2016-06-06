package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	private static CoolWeatherDB coolWeatherDB;

	private SQLiteDatabase db;

	/**
	 * 将构造方法私有化
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取CoolWeatherDB的实例。
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库。
	 */
	public void saveProvince(City citys) {
		if (citys != null) {
			ContentValues values = new ContentValues();
			values.put("id", citys.id);
			values.put("province", citys.province);
			values.put("city", citys.city);
			values.put("district", citys.district);
			db.insert("Province", null, values);
		}
	}

	/**
	 * 从数据库读取全国所有的省份信息。
	 */
	public List<City> loadProvinces() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City citys = new City();
				citys.id = cursor.getInt(cursor.getColumnIndex("id"));
				citys.province = cursor.getString(cursor.getColumnIndex("province"));
				citys.city = cursor.getString(cursor.getColumnIndex("city"));
				citys.district = cursor.getString(cursor.getColumnIndex("district"));
				list.add(citys);
			} while (cursor.moveToNext());
		}
		return list;
	}



}
