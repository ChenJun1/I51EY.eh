package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;//package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;
//
//import java.util.List;
//
//import android.content.Context;
//
//import com.cvnavi.logistics.i51eyun.eh.MyApplication;
//import com.cvnavi.logistics.i51eyun.eh.R;
//import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
//import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
//import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
//
//
///**
// * 
// * 版权所有 上海势航
// *
// * @author chenJun and johnnyYuan
// * @description
// * @date 2016-5-17 下午1:10:20
// * @version 1.0.0
// * @email yuanlunjie@163.com
// */
//public class LookingCargoAdapter extends CommonAdapter<WaitingOrderBaseInfo> {
//	public LookingCargoAdapter(Context context, int layoutId, List<WaitingOrderBaseInfo> datas) {
//		super(context, layoutId, datas);
//	}
//
//	@Override
//	public void convert(ViewHolder holder, WaitingOrderBaseInfo completeBean) {
//		final int color = 0xFF0089D2;
//		if (completeBean.CarCode_Key != null && !completeBean.CarCode_Key.equals("")) {
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.setbar_stytes_06);
//			holder.setTextColor(R.id.Operate_Code, 0xffff0000);
//		} else {
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.setbar_stytes05);
//			holder.setTextColor(R.id.Operate_Code, 0xff0089D2);
//		}
//		if (completeBean.Operate_Code.equals("AG")) {
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.delivery_order_status);
//			holder.setText(R.id.Operate_Code, "已完成");
//			holder.setTextColor(R.id.Operate_Code, 0xffffffff);
//		} else if (completeBean.Operate_Code.equals("AB")) {
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.setbar_stytes05);
//			holder.setText(R.id.Operate_Code, "交货");
//			holder.setTextColor(R.id.Operate_Code, color);
//		} else if (completeBean.Operate_Code.equals("AA")) {
//			holder.setText(R.id.Operate_Code, "待接单");
//		} else if (completeBean.Operate_Code.equals("AC")) {
//			holder.setText(R.id.Operate_Code, "已装车");
//		} else if (completeBean.Operate_Code.equals("AD")) {
//			holder.setText(R.id.Operate_Code, "运输中");
//		} else if (completeBean.Operate_Code.equals("AE")) {
//			holder.setText(R.id.Operate_Code, "已卸车");
//		} else if (completeBean.Operate_Code.equals("AF")) {
//			holder.setText(R.id.Operate_Code, "已交货");
//		}
//		Dict_GoodsType_Sheet fGoodsType_Sheet = MyApplication.getInstatnce().getBasicDataBuffer()
//				.getGoodTypeByKey(completeBean.GoodsType_Oid);
//		Dict_GoodsShape_Sheet fGoodsShape_Sheet = MyApplication.getInstatnce().getBasicDataBuffer()
//				.getGoodsShapeByKey(completeBean.GoodsShape_Oid);
//		holder.setText(R.id.Consignee_Provice, completeBean.Consignee_City);
//		holder.setText(R.id.Shipping_Provice, completeBean.Shipping_City);
//		holder.setText(R.id.OrderFee, completeBean.OrderFee);
//		if (fGoodsType_Sheet != null) {
//			holder.setText(R.id.GoodsType, fGoodsType_Sheet.GoodsType);// MyApplication.getInstatnce().getBasicDataBuffer().getGoodsShapeByKey().GoodsShape);//货品类型
//		}
//		holder.setText(R.id.time, completeBean.DeliveryTime);// 提货时间
//		if (fGoodsShape_Sheet != null) {
//			holder.setText(R.id.GoodsShapeGoodsType, fGoodsShape_Sheet.GoodsShape);// 形状
//		}
//		holder.setText(R.id.Weight, completeBean.Weight);// 重量
//		holder.setText(R.id.Volume, completeBean.Volume);// 放量
//	}
//}
