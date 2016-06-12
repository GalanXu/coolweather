package com.coolweather.app.model;

import java.util.List;

/**
 * Created by zhouwk on 2016/6/7 0007.
 */
public class Yubao {

	public String resultcode;
	public String reason;
	public int error_code;
	public Fst result;

	public class Fst {
		public ToWeather today;
		public SkWeather sk;
		public List<FuWeather> future;
	}

	public class ToWeather {
		public String city;
		public String comfort_index;
		public String date_y;
		public String week;
		public String temperature;
		public String weather;
		public Weather_Id weather_id;
		public String wind;
		public String dressing_index;
		public String dressing_advice;
		public String uv_index;
		public String wash_index;
		public String travel_index;
		public String exercise_index;
		public String drying_index;
	}

	public class SkWeather {
		public String temp;
		public String wind_direction;
		public String wind_strength;
		public String humidity;
		public String time;
	}

	public class FuWeather {
		public String temperature;
		public String weather;
		public Weather_Id weather_id;
		public String wind;
		public String week;
		public String date;
	}

	public class Weather_Id {
		public String fa;
		public String fb;
	}
}
