package com.coolweather.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.google.gson.Gson;

public class MainActivity extends Activity {
	private Button bt;
	private TextView tv;
	private ProgressDialog progressDialog;
	private CoolWeatherDB coolWeatherDB;

	public static String PROVINCE_URL = "http://v.juhe.cn/weather/citys?key=9bd43d62ba9657efa69a41744fa16161";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				queryFromServer();
				// 装个样子
			}
		});
		tv = (TextView) findViewById(R.id.tv);
	}

	private void queryFromServer() {
		showProgressDialog();
		HttpUtil.sendHttpRequest(PROVINCE_URL, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				closeProgressDialog();
				// Log.i("MainActivity", response);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Gson gson = new Gson();
						Province province = gson.fromJson(response,
								Province.class);

						if ("200".equals(province.resultcode)
								&& "successed".equals(province.reason)) {
							Log.i("MainActivity", province.error_code);
							Log.i("MainActivity", province.resultcode);
							Log.i("MainActivity", province.reason);
							for (City c : province.result) {
								coolWeatherDB.saveProvince(c);
							}
						}
 
					}
				});
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread() 方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
					}
				});
			}
		});
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

}
