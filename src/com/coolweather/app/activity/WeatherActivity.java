package com.coolweather.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	/**
	 * 用于显示城市名
	 * */
	private TextView cityNameText;
	/**
	 * 用于显示发布时间
	 */
	private TextView publishText;
	/**
	 * 用于显示天气描述信息
	 */
	private TextView weatherDespText;
	/**
	 * 用于显示气温1
	 */
	private TextView temp1Text;
	/**
	 * 用于显示当前日期
	 */
	private TextView currentDateText;
	/**
	 * 切换城市按钮
	 */
	private Button switchCity;
	/**
	 * 更新天气按钮
	 */
	private Button refreshWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// 初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		currentDateText = (TextView) findViewById(R.id.current_date);
//		switchCity = (Button) findViewById(R.id.switch_city);
//		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		String countyCode = getIntent().getStringExtra("county_code");
		queryWeatherCode(countyCode);
	}

	/**
	 * 查询县级代号所对应的天气代号。
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://v.juhe.cn/weather/index?format=2&cityname="
				+ countyCode + "&dtype=&key=9bd43d62ba9657efa69a41744fa16161";
		queryFromServer(address);
	}
	//http://v.juhe.cn/weather/index?format=2&cityname=14&dtype=&key=9bd43d62ba9657efa69a41744fa16161

	private void queryFromServer(final String address) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						showWeather();
					}
				});

			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread() 方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
					}
				});
			}
		});
	}

	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("date_y", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View arg0) {

	}

}
