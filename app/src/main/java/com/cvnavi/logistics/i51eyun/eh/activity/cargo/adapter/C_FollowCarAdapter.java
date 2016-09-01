/**
 * Administrator2016-5-22
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.FollowCarListRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.List;

/**
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-22 下午2:36:34
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

public class C_FollowCarAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<FollowCarListRequestBean> orderList;
	private Context mContext;

	public C_FollowCarAdapter(List<FollowCarListRequestBean> dataList,
			Context context) {
		this.mContext = context;
		this.orderList = dataList;
		mInflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FollowCarListRequestBean orderInfo = orderList.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.c_follow_car_item, null);
		}
		
		TextView c_car_code_tv= ViewHolder.get(convertView, R.id.c_car_code_tv);
		TextView c_Shipping_City_tv= ViewHolder.get(convertView, R.id.c_Shipping_City_tv);
		TextView c_Consignee_City_tv= ViewHolder.get(convertView, R.id.c_Consignee_City_tv);
		
		c_car_code_tv.setText(orderInfo.CarCode);
		c_Shipping_City_tv.setText(orderInfo.Shipping_City);
		c_Consignee_City_tv.setText(orderInfo.Consignee_City);
		
		return convertView;
	}
}
