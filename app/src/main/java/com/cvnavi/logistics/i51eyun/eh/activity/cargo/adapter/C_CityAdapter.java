/**
 * Administrator2016-4-29
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.CityBean;
import com.cvnavi.logistics.i51eyun.eh.activity.region.CityActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:02:31
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_CityAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CityBean> cictList;
	private Context mContext;

	public C_CityAdapter(List<CityBean> dataList, Context mContext) {
		this.cictList = dataList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return cictList.size();
	}

	@Override
	public Object getItem(int position) {
		return cictList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.c_show_mdict_list_item,
					null);
		}
		CityBean cityBean = cictList.get(position);
		TextView cityName = ViewHolder.get(convertView, R.id.Company_Name);
		ImageView imageView= ViewHolder.get(convertView, R.id.img_right_arrow);
		if(!TextUtils.isEmpty(CityActivity.province_city_key)){
			imageView.setVisibility(View.GONE);
		}
		cityName.setText(cityBean.CName);
		return convertView;
	}
}
