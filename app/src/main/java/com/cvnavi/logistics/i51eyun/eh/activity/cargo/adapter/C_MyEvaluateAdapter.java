package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationListInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.CommonAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.ViewHolder;

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
public class C_MyEvaluateAdapter extends CommonAdapter<OrderEvaluationListInfo> {

	public C_MyEvaluateAdapter(Context context, int layoutId, List<OrderEvaluationListInfo> dataList) {
		super(context, layoutId, dataList);
	}

	@Override
	public void convert(ViewHolder holder, OrderEvaluationListInfo bean) {
		holder.setText(R.id.order_num_tv, bean.Serial_Oid);
		holder.setText(R.id.car_code_tv, bean.CarCode);
		holder.setText(R.id.start_end_city_tv, bean.Shipping_City + "-" + bean.Consignee_City);
		// 货主得分为司机评价，司机得分为货主评价。
		holder.setText(R.id.my_score_tv, bean.SJPJ_SCORE);
		holder.setText(R.id.driver_score_tv, bean.HZPJ_SCORE);
	}
}
