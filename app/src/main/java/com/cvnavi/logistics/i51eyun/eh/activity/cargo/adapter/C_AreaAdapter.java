/**
 * Administrator2016-4-29
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.AreaBean;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:02:14
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_AreaAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<AreaBean> AreaList;
	private Context mContext;

	public C_AreaAdapter(List<AreaBean> dataList, Context mContext) {
		this.AreaList = dataList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return AreaList.size();
	}

	@Override
	public Object getItem(int position) {
		return AreaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AreaBean areaBean = AreaList.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.c_show_mdict_list_item,
					null);
		}

		TextView areaName = ViewHolder.get(convertView, R.id.Company_Name);
		ImageView imageView= ViewHolder.get(convertView, R.id.img_right_arrow);
		imageView.setVisibility(View.GONE);
		areaName.setText(areaBean.AName);

		return convertView;
	}

}
