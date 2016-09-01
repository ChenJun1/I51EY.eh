package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;

import android.content.Context;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.locationtracking.LocationTrackingBean;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:10:20
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class D_LocationTrackingAdapter extends CommonAdapter<LocationTrackingBean> {

	public D_LocationTrackingAdapter(Context context, int layoutId, List<LocationTrackingBean> dataList) {
		super(context, layoutId, dataList);
	}

	@Override
	public void convert(ViewHolder holder, LocationTrackingBean bean) {
		holder.setText(R.id.d_car_code_tv, bean.Serial_Oid);
		holder.setText(R.id.d_Shipping_City_tv, bean.Shipping_City);
		holder.setText(R.id.d_Consignee_City_tv, bean.Consignee_City);
	}
}
