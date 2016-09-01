package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;

import android.content.Context;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;

import java.util.List;

/**
 *  
 * 
 * @author 作者：fanxb
 * @version 创建时间：2016-5-24 下午7:31:42  类说明 
 */
public class D_FindGoodsAdapter extends CommonAdapter<WaitingOrderBaseInfo> {

	public D_FindGoodsAdapter(Context context, int layoutId, List<WaitingOrderBaseInfo> dataList) {
		super(context, layoutId, dataList);
	}

	@Override
	public void convert(ViewHolder holder, WaitingOrderBaseInfo orderInfo) {
		holder.setImageResource(R.id.c_pic_img,
				ContextUtil.getDeliveryNeedPhoto(orderInfo.IsTHPhoto, orderInfo.IsJHPhoto));
		holder.setText(R.id.c_Shipping_City_tv, orderInfo.Shipping_City);
		holder.setText(R.id.c_Consignee_City_tv, orderInfo.Consignee_City);
		holder.setText(R.id.c_DeliveryTime_tv, DateUtil.str2str(orderInfo.DeliveryTime));

		holder.setText(R.id.c_Operate_Code_tv, ContextUtil.getOperateCodeContent(orderInfo.Operate_Code,
				orderInfo.IsTHPhoto, orderInfo.IsJHPhoto, MyApplication.getInstatnce().getUserTypeOid()));
		holder.setBackgroundRes(R.id.c_Operate_Code_tv,
				ContextUtil.getOperateStatusBg(orderInfo.Operate_Code, orderInfo.Order_Type));
		holder.setText(R.id.c_TotalFee_tv, orderInfo.TotalFee);
		SetViewValueUtil.setNumberValue((TextView) holder.getView(R.id.c_Number_tv), orderInfo.Number);
		SetViewValueUtil.setWeightValue((TextView) holder.getView(R.id.c_Weight_tv), orderInfo.Weight);
		SetViewValueUtil.setVolumeValue((TextView) holder.getView(R.id.c_Volume_tv), orderInfo.Volume);
	}
}
