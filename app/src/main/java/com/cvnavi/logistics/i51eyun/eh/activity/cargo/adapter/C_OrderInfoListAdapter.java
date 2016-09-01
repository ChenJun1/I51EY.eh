/**
 * Administrator2016-4-21
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:04:25
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("UseSparseArrays")
public class C_OrderInfoListAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private HashMap<Integer, View> lmap = null;
	private LayoutInflater mInflater;
	private List<String> strList;
	private Context mContext;

	public C_OrderInfoListAdapter(List<String> dataList, Context context) {
		this.mContext = context;
		this.strList = dataList;
		mInflater = LayoutInflater.from(mContext);
		lmap = new HashMap<Integer, View>();
	}

	@Override
	public int getCount() {
		return strList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int opsition) {
		return opsition;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.c_homepage_fragment_item,
					parent, false);
		}

		TextView carCode = ViewHolder.get(convertView, R.id.c_car_code);
		TextView carType = ViewHolder.get(convertView, R.id.c_car_type);
		TextView carLength = ViewHolder.get(convertView, R.id.c_car_length);
		TextView carTel = ViewHolder.get(convertView, R.id.c_car_tel);

		carCode.setText("沪A9527");
		carType.setText("箱货");
		carLength.setText("9.6m");
		carTel.setText("17774530300");

		return convertView;
	}

}
