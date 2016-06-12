package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class MainActivity extends Activity {
	private Button bt;
	private ProgressDialog progressDialog;
	private CoolWeatherDB coolWeatherDB;
	private ListView listView;
	private CityAdapter cityAdapter;
	private List<City> dataList = new ArrayList<City>();

	public static String PROVINCE_URL = "http://v.juhe.cn/weather/citys?key=9bd43d62ba9657efa69a41744fa16161";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list_view);
		
//		City c = new City();
//		c.id =1;
//		c.province ="hu";
//		c.city ="huang";
//		c.district ="da";
//		dataList.add(c); 
		
		cityAdapter = new CityAdapter(this, dataList);
		if (dataList != null) {
			listView.setAdapter(cityAdapter);
		}
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				String countyCode = dataList.get(index).id;
				Log.i("MainActivity", "countyCode"+countyCode);
				Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
				intent.putExtra("county_code", countyCode);
				startActivity(intent);
				finish();
			}
		});
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				queryCities();
			}
		});
		queryCities();
	}

	/**
	 * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
	 */
	private void queryCities() {
		dataList = coolWeatherDB.loadProvinces();
		if (dataList.size() > 0) {
			cityAdapter.refreshAdapter(dataList);
			listView.setSelection(0);
		} else {
			queryFromServer();
		}
	}

	private void queryFromServer() {
		showProgressDialog();
		HttpUtil.sendHttpRequest(PROVINCE_URL, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				boolean result = false;

				result = Utility.handleProvincesResponse(coolWeatherDB,
						response);
				if (result) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO
							closeProgressDialog();
							queryCities();
						}
					});
				}

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
