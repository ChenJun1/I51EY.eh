package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MDict_FavoriteContacts_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 常用收货人管理Adapter
 * @date 2016-5-17 下午1:02:46
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_CommonConsigneeManagementAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	Context context;
	private List<MDict_FavoriteContacts_Sheet> consigneeList;

	public C_CommonConsigneeManagementAdapter(
			List<MDict_FavoriteContacts_Sheet> consigneeList, Context context) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.consigneeList = consigneeList;
	}

	@Override
	public int getCount() {
		return consigneeList.size();
	}

	@Override
	public Object getItem(int position) {
		return consigneeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MDict_FavoriteContacts_Sheet oSheet = consigneeList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.c_common_consignee_management_item, parent, false);
		}

		TextView CDriverNameText = ViewHolder.get(convertView,
				R.id.c_driver_name_text);
		TextView CDriverPhoneNum = ViewHolder.get(convertView,
				R.id.c_driver_phoneNum);
		TextView CDriverAddress = ViewHolder.get(convertView,
				R.id.c_driver_address);
		
		SetViewValueUtil.setTextViewValue(CDriverNameText, oSheet.Contacts_Name);
		SetViewValueUtil.setTextViewValue(CDriverPhoneNum, oSheet.Contacts_Tel);
//		CDriverNameText.setText(oSheet.Contacts_Name);
//		CDriverPhoneNum.setText(oSheet.Contacts_Tel==null?"":oSheet.Contacts_Tel);
		CDriverAddress.setText((oSheet.Contacts_Provice==null?"":oSheet.Contacts_Provice) +" "+ oSheet.Contacts_City+" "
				+ (oSheet.Contacts_Area==null?"":oSheet.Contacts_Area)+" " + (oSheet.Contacts_Address==null?"":oSheet.Contacts_Address));

		return convertView;
	}
}
