package com.coolweather.app.activity;

import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.model.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter{
	private List<City> mFileList;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	
	public CityAdapter(Context context,List<City> FileList){
		mFileList = FileList;
		mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
	}

	public void refreshAdapter(List<City> FileList)
	{
		mFileList = FileList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mFileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView != null ) {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}else {
			view = mLayoutInflater.inflate(R.layout.city_item, null);
			holder = new ViewHolder();
			holder.tv_citysitem = (TextView) view.findViewById(R.id.tv_citysitem);

			// 保存对应关系
			view.setTag(holder);
		}
		holder.tv_citysitem.setText(mFileList.get(position).district);
		return view;
	}
	
	static class ViewHolder {
		TextView tv_citysitem;
	}

}
