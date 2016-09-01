/**
 * Administrator2016-4-29
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.ProvinceBean;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:04:32
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_ProvinceAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ProvinceBean> provinceList;
	private Context mContext;

	public C_ProvinceAdapter(List<ProvinceBean> dataList, Context mContext) {
		this.provinceList = dataList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return provinceList.size();
	}

	@Override
	public Object getItem(int position) {
		return provinceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ProvinceBean provinceBean = provinceList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.c_show_mdict_list_item,
					null);
		}
		TextView provinceName = ViewHolder.get(convertView, R.id.Company_Name);
		provinceName.setText(provinceBean.PName);

		return convertView;
	}

}
