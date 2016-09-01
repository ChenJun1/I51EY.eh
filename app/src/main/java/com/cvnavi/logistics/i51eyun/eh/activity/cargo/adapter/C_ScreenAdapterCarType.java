package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:05:10
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_ScreenAdapterCarType extends BaseAdapter {
	private Context mContext;

	private List<Dict_CarType_Sheet> carTypeList;

	public C_ScreenAdapterCarType(Context mContext,
			List<Dict_CarType_Sheet> carTypeList) {
		this.mContext = mContext;
		this.carTypeList = carTypeList;
	}

	@Override
	public int getCount() {
		return carTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return carTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Dict_CarType_Sheet mCarType_Sheet = carTypeList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.c_screen_item_cartype, null);
		}
		TextView tv = ViewHolder.get(convertView, R.id.c_screen_02_tv_item);

		tv.setText(mCarType_Sheet.CarType);
		return convertView;
	}
}
