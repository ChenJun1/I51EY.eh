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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:04:10
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_mDictCompanyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MDict_Company_Sheet> MDictList;

	public C_mDictCompanyAdapter(List<MDict_Company_Sheet> dataList, Context mContext) {
		this.MDictList = dataList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return MDictList.size();
	}

	@Override
	public Object getItem(int position) {
		return MDictList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MDict_Company_Sheet bean = MDictList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.c_show_mdict_list_item, null);
		}
		TextView Company_Name = ViewHolder.get(convertView, R.id.Company_Name);

		Company_Name.setText(bean.Company_Name);
		return convertView;
	}

}
