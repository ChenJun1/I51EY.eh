package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;

import android.content.Context;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.commonlyline.MDict_Line_Sheet;

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
public class D_CommonlyLineManagerAdapter extends CommonAdapter<MDict_Line_Sheet> {

	public D_CommonlyLineManagerAdapter(Context context, int layoutId, List<MDict_Line_Sheet> dataList) {
		super(context, layoutId, dataList);
	}

	@Override
	public void convert(ViewHolder holder, MDict_Line_Sheet bean) {
		holder.setText(R.id.start_tv, bean.Provenance_Provice + " > " + bean.Provenance_City);
		holder.setText(R.id.end_tv, bean.Destination_Provice + " > " + bean.Destination_City);
		holder.setText(R.id.route_tv, bean.RouteType.equals("0") == true ? "单边" : "往返");
		holder.setText(R.id.default_Lin, bean.IsDefault.equals("1") == true ? "默认路线" : "");
	}
}
