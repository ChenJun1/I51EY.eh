package com.cvnavi.logistics.i51eyun.eh.activity.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 上午11:24:24
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class CarCodeRegisterAddressAdapter extends BaseAdapter {

	private List<String> carCodeRegisterAddressList;
	private Context context;

	public CarCodeRegisterAddressAdapter(Context context, List<String> list) {
		this.carCodeRegisterAddressList = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return carCodeRegisterAddressList.size();
	}

	@Override
	public Object getItem(int position) {
		return carCodeRegisterAddressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.d_carcode_register_address_item, null);
			viewHolder.carCodeRegisterAddressTextView = (TextView) convertView.findViewById(R.id.item_tv);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final String value = carCodeRegisterAddressList.get(position);
		viewHolder.carCodeRegisterAddressTextView.setText(value);
		
		return convertView;
	}

	private static class ViewHolder {
		TextView carCodeRegisterAddressTextView;
	}

}
