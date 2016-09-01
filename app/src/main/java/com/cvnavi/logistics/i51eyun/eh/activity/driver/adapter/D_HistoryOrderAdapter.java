/**
 * Administrator2016-5-20
 */
package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-20 上午10:31:07
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@SuppressLint("UseSparseArrays")
public class D_HistoryOrderAdapter extends BaseAdapter {
	private HashMap<Integer, View> viewMaps = null;
	
	private List<WaitingOrderBaseInfo> historyOrderList;
	private LayoutInflater mInflater;

	public D_HistoryOrderAdapter(Context context, List<WaitingOrderBaseInfo> dataList) {
		this.historyOrderList = dataList;
		mInflater = LayoutInflater.from(context);
		viewMaps = new HashMap<Integer, View>();
	}

	@Override
	public int getCount() {
		return historyOrderList.size();
	}

	@Override
	public Object getItem(int position) {
		return historyOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final WaitingOrderBaseInfo orderInfo = historyOrderList.get(position);
//		if (viewMaps.get(position) == null) {
//			convertView = mInflater.inflate(R.layout.c_history_order_item, parent, false);
//			viewMaps.put(position, convertView);
//		} else {
//			convertView = viewMaps.get(position);
//		}
		
		convertView = mInflater.inflate(R.layout.d_order_item, parent, false);

		ImageView c_pic_img = ViewHolder.get(convertView, R.id.c_pic_img);
		TextView c_Shipping_City_tv = ViewHolder.get(convertView, R.id.c_Shipping_City_tv);
		TextView c_Consignee_City_tv = ViewHolder.get(convertView, R.id.c_Consignee_City_tv);
		TextView c_DeliveryTime_tv = ViewHolder.get(convertView, R.id.c_DeliveryTime_tv);
		TextView c_TotalFee_tv = ViewHolder.get(convertView, R.id.c_TotalFee_tv);
		TextView c_Number_tv = ViewHolder.get(convertView, R.id.c_Number_tv);
		TextView c_Weight_tv = ViewHolder.get(convertView, R.id.c_Weight_tv);
		TextView c_Volume_tv = ViewHolder.get(convertView, R.id.c_Volume_tv);
		TextView c_Operate_Code_tv = ViewHolder.get(convertView, R.id.c_Operate_Code_tv);
		TextView c_Operate_Code_btn = ViewHolder.get(convertView, R.id.c_Operate_Code_btn);

		if ((!TextUtils.isEmpty(orderInfo.IsJHPhoto) && orderInfo.IsJHPhoto.equals("1"))
				|| (!TextUtils.isEmpty(orderInfo.IsTHPhoto) && orderInfo.IsTHPhoto.equals("1"))) {
			c_pic_img.setImageResource(R.drawable.photohong);
		} else {
			c_pic_img.setImageResource(R.drawable.photo);
		}

		c_Shipping_City_tv.setText(orderInfo.Shipping_City);
		c_Consignee_City_tv.setText(orderInfo.Consignee_City);
		c_DeliveryTime_tv.setText(DateUtil.str2str(orderInfo.DeliveryTime));
		c_TotalFee_tv.setText(orderInfo.TotalFee);
		SetViewValueUtil.setNumberValue(c_Number_tv, orderInfo.Number);
		SetViewValueUtil.setWeightValue(c_Weight_tv, orderInfo.Weight);
		SetViewValueUtil.setVolumeValue(c_Volume_tv, orderInfo.Volume);
		switch (orderInfo.Operate_Code) {
		case "AA":
			c_Operate_Code_tv.setVisibility(View.VISIBLE);
			c_Operate_Code_tv.setText("待接单");
			if (TextUtils.isEmpty(orderInfo.Order_Type) == false && orderInfo.Order_Type.equals("B")) {
				c_Operate_Code_tv.setBackgroundResource(R.drawable.waiting_order_appoint_status);
			}
			c_Operate_Code_btn.setVisibility(View.GONE);
			break;
		case "AB":
			if (TextUtils.isEmpty(orderInfo.IsTHPhoto) == false && orderInfo.IsTHPhoto.equals("1")) {
				c_Operate_Code_tv.setVisibility(View.GONE);
				// c_Operate_Code_tv.setText("提货");
				// c_Operate_Code_tv.setBackgroundResource(R.drawable.order_received_stytes);

				c_Operate_Code_btn.setVisibility(View.VISIBLE);
				c_Operate_Code_btn.setText("提货");
				c_Operate_Code_btn.setBackgroundResource(R.drawable.order_received_stytes);
			} else if (TextUtils.isEmpty(orderInfo.IsTHPhoto) == false && orderInfo.IsTHPhoto.equals("0")) {
				c_Operate_Code_tv.setVisibility(View.GONE);

				c_Operate_Code_btn.setVisibility(View.VISIBLE);
				c_Operate_Code_btn.setText("交货");
				c_Operate_Code_btn.setBackgroundResource(R.drawable.order_received_stytes);
			}
			break;
		case "AC":
			c_Operate_Code_tv.setVisibility(View.GONE);
			// c_Operate_Code_tv.setText("交货");
			// c_Operate_Code_tv.setBackgroundResource(R.drawable.order_received_stytes);

			c_Operate_Code_btn.setVisibility(View.VISIBLE);
			c_Operate_Code_btn.setText("交货");
			c_Operate_Code_btn.setBackgroundResource(R.drawable.order_received_stytes);
			break;
		case "AD":
			c_Operate_Code_tv.setText("运输中");
			c_Operate_Code_tv.setBackgroundResource(R.drawable.completed_order_status);
			break;
		case "AE":
			c_Operate_Code_tv.setText("已卸车");
			c_Operate_Code_tv.setBackgroundResource(R.drawable.completed_order_status);
			break;
		case "AF":
			c_Operate_Code_btn.setVisibility(View.GONE);
			// c_Operate_Code_btn.setText("已交货");
			// c_Operate_Code_btn.setBackgroundResource(R.drawable.order_complete_stytes);

			c_Operate_Code_tv.setVisibility(View.VISIBLE);
			c_Operate_Code_tv.setText("已交货");
			c_Operate_Code_tv.setBackgroundResource(R.drawable.order_complete_stytes);
			break;
		case "AG":
			c_Operate_Code_tv.setText("已完成");
			break;
		default:
			break;
		}

		return convertView;
	}

}
