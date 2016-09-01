/**
 * Administrator2016-4-21
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

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
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:05:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("UseSparseArrays")
public class C_WaitingOrderAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private HashMap<Integer, View> lmap = null;
	private LayoutInflater mInflater;
	private List<WaitingOrderBaseInfo> WaitingOrderList;
	private Context mContext;

	public C_WaitingOrderAdapter(List<WaitingOrderBaseInfo> dataList,
			Context mContext) {
		super();
		this.WaitingOrderList = dataList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
		lmap = new HashMap<Integer, View>();
	}

	@Override
	public int getCount() {
		return WaitingOrderList.size();
	}

	@Override
	public Object getItem(int position) {
		return WaitingOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WaitingOrderBaseInfo orderInfo = WaitingOrderList.get(position);
//		if (lmap.get(position) == null) {
//			lmap.put(position, convertView);
//		} else {
//			convertView = lmap.get(position);
//		}
		if(convertView==null){
		convertView = mInflater.inflate(R.layout.c_order_item,
				parent, false);
		}

		TextView c_Shipping_City_tv = ViewHolder.get(convertView,R.id.c_Shipping_City_tv);
		TextView c_Consignee_City_tv = ViewHolder.get(convertView,R.id.c_Consignee_City_tv);
		TextView c_DeliveryTime_tv = ViewHolder.get(convertView,R.id.c_DeliveryTime_tv);
		TextView c_TotalFee_tv = ViewHolder.get(convertView, R.id.c_TotalFee_tv);
		TextView c_Number_tv = ViewHolder.get(convertView, R.id.c_Number_tv);
		TextView c_Weight_tv = ViewHolder.get(convertView, R.id.c_Weight_tv);
		TextView c_Volume_tv = ViewHolder.get(convertView, R.id.c_Volume_tv);
		TextView c_Operate_Code_tv = ViewHolder.get(convertView,R.id.c_Operate_Code_tv);
		TextView c_Operate_Code_btn= ViewHolder.get(convertView, R.id.c_Operate_Code_btn);
		ImageView c_pic_img= ViewHolder.get(convertView, R.id.c_pic_img);
		
		
		if(TextUtils.isEmpty(orderInfo.IsJHPhoto)&&TextUtils.isEmpty(orderInfo.IsTHPhoto)){
			c_pic_img.setImageResource(R.drawable.photo);
		}else if(orderInfo.IsJHPhoto.equals("1")||orderInfo.IsTHPhoto.equals("1")){
			c_pic_img.setImageResource(R.drawable.photohong);
		}else{
			c_pic_img.setImageResource(R.drawable.photo);
		}
		
		c_Shipping_City_tv.setText(orderInfo.Shipping_City);
		c_Consignee_City_tv.setText(orderInfo.Consignee_City);
		c_DeliveryTime_tv.setText((TextUtils.isEmpty(DateUtil.str2str(orderInfo.DeliveryTime))==true?"": DateUtil.str2str(orderInfo.DeliveryTime))+" ");
		c_TotalFee_tv.setText(orderInfo.TotalFee);
//		c_Number_tv.setText((orderInfo.Number == null ? "0" : orderInfo.Number));
//		c_Weight_tv.setText((orderInfo.Weight == null ? "0" : orderInfo.Weight));
//		c_Volume_tv.setText((orderInfo.Volume == null ? "0" : orderInfo.Volume));

		SetViewValueUtil.setNumberValue(c_Number_tv, orderInfo.Number);
		SetViewValueUtil.setWeightValue(c_Weight_tv, orderInfo.Weight);
		SetViewValueUtil.setVolumeValue(c_Volume_tv, orderInfo.Volume);
		
			c_Operate_Code_tv.setText("待接单");
			c_Operate_Code_tv.setVisibility(View.VISIBLE);
			c_Operate_Code_btn.setVisibility(View.GONE);
			c_Operate_Code_tv
					.setBackgroundResource(R.drawable.completed_order_status);

		/*
		 * TextView statusReceived = ViewHolder.get(convertView,
		 * R.id.status_received); TextView statusReceivedCar =
		 * ViewHolder.get(convertView, R.id.status_received_car); TextView
		 * statustRaffic = ViewHolder.get(convertView, R.id.status_traffic);
		 * TextView statusUnloadCar = ViewHolder.get(convertView,
		 * R.id.status_unload_car); TextView statusFulfil =
		 * ViewHolder.get(convertView, R.id.status_fulfil);
		 */

		/*
		 * statusReceived.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Toast.makeText(mContext,
		 * "点击了已接单", Toast.LENGTH_SHORT).show(); } });
		 * 
		 * statusReceivedCar.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Toast.makeText(mContext,
		 * "点击了已装车", Toast.LENGTH_SHORT).show(); } });
		 * statustRaffic.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Toast.makeText(mContext,
		 * "点击了运输中", Toast.LENGTH_SHORT).show(); } });
		 * statusUnloadCar.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Toast.makeText(mContext,
		 * "点击了已卸车", Toast.LENGTH_SHORT).show(); } });
		 * statusFulfil.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Toast.makeText(mContext,
		 * "点击了已完成", Toast.LENGTH_SHORT).show(); } });
		 * 
		 * orderName.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Toast.makeText(mContext,
		 * "点击订单详情", Toast.LENGTH_SHORT).show(); Intent intent = new Intent();
		 * intent.setClass(mContext, C_OrderInfoWaitingActivity.class);
		 * mContext.startActivity(intent); } });
		 */
		return convertView;
	}

}
