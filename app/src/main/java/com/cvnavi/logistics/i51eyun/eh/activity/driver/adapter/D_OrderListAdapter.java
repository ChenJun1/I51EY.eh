package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;//package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;
//
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.cvnavi.logistics.i51eyun.eh.MyApplication;
//import com.cvnavi.logistics.i51eyun.eh.R;
//import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
//import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
//import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
//
///**
// * 
// * 版权所有 上海势航
// *
// * @author chenJun and johnnyYuan
// * @description
// * @date 2016-5-17 下午1:12:36
// * @version 1.0.0
// * @email yuanlunjie@163.com
// */
//@SuppressLint("HandlerLeak")
//public class D_OrderListAdapter extends CommonAdapter<WaitingOrderBaseInfo> {
//
//	private Context context;
//
//	public D_OrderListAdapter(Context context, int layoutId, List<WaitingOrderBaseInfo> datas) {
//		super(context, layoutId, datas);
//		this.context = context;
//	}
//
//	@Override
//	public void convert(final ViewHolder holder, final WaitingOrderBaseInfo waitingOrderBaseInfo) {
//		switch (waitingOrderBaseInfo.Operate_Code) {
//		case "AA":
//			if (!TextUtils.isEmpty(waitingOrderBaseInfo.CarCode_Key)) {
//				holder.setBackgroundRes(R.id.Operate_Code, R.drawable.waiting_order_appoint_status);
//				holder.setText(R.id.Operate_Code, "待接单");
//			} else {
//				holder.setBackgroundRes(R.id.Operate_Code, R.drawable.waiting_order_status);
//				holder.setText(R.id.Operate_Code, "待接单");
//			}
//			break;
//		case "AB":
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.order_received_stytes);
//			holder.setText(R.id.Operate_Code, "交货");   // 已接单
////			holder.setOnClickListener(R.id.Operate_Code, new OnClickListener() {
////				@Override
////				public void onClick(View v) {
////					new Thread() {
////						public void run() {
////							DataRequestData requestData = new DataRequestData();
////							requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
////							requestData.setToken(MyApplication.getInstatnce().getToken());
////							requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
////							requestData.setDataValue(waitingOrderBaseInfo.Order_Key);
////
////							RequestParams params = new RequestParams(Constants.Refuse_HadCarOrder_Url);
////							try {
////								params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
////							} catch (IOException e1) {
////								e1.printStackTrace();
////							}
////
////							x.http().post(params, new Callback.CommonCallback<String>() {
////								@Override
////								public void onSuccess(String jsonStr) {
////									LogUtil.d("-->>onSuccess：" + jsonStr);
////
////									DataResultBase resultBase;
////									try {
////										resultBase = JsonUtils.parseDataResultBase(jsonStr);
////										Message msg = Message.obtain();
////										if (resultBase.isSuccess()) {
////											// msg.what =
////											// Constants.Request_Success;
////											// myHandler.sendMessage(msg);
////
////											myHandler.post(new Runnable() {
////												@Override
////												public void run() {
////													holder.setText(R.id.Operate_Code, "已交货");
////													holder.setBackgroundRes(R.id.Operate_Code,
////															R.drawable.delivery_order_status);
////												}
////											});
////
////										} else {
////											msg.what = Constants.Request_Fail;
////											msg.obj = resultBase.getErrorText();
////											myHandler.sendMessage(msg);
////										}
////									} catch (Exception e) {
////										e.printStackTrace();
////									}
////								}
////
////								@Override
////								public void onError(Throwable throwable, boolean b) {
////									LogUtil.d("-->>onError：" + throwable.getMessage());
////									Message msg = Message.obtain();
////									msg.what = Constants.Request_Fail;
////									msg.obj = throwable.getMessage();
////									myHandler.sendMessage(msg);
////								}
////
////								@Override
////								public void onCancelled(CancelledException e) {
////									LogUtil.d("-->>onCancelled");
////								}
////
////								@Override
////								public void onFinished() {
////									LogUtil.d("-->>onFinished");
////								}
////							});
////						}
////					}.start();
////				}
////			});
//			break;
//		case "AC":
//			holder.setText(R.id.Operate_Code, "已装车");
//			break;
//		case "AD":
//			holder.setText(R.id.Operate_Code, "运输中");
//			break;
//		case "AE":
//			holder.setText(R.id.Operate_Code, "已卸车");
//			break;
//		case "AF":
//			holder.setText(R.id.Operate_Code, "已交货");
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.delivery_order_status);
//			break;
//		case "AG":
//			holder.setBackgroundRes(R.id.Operate_Code, R.drawable.completed_order_status);
//			holder.setText(R.id.Operate_Code, "已完成");
//			break;
//		}
//
//		Dict_GoodsType_Sheet fGoodsType_Sheet = MyApplication.getInstatnce().getBasicDataBuffer()
//				.getGoodTypeByKey(waitingOrderBaseInfo.GoodsType_Oid);
//		Dict_GoodsShape_Sheet fGoodsShape_Sheet = MyApplication.getInstatnce().getBasicDataBuffer()
//				.getGoodsShapeByKey(waitingOrderBaseInfo.GoodsShape_Oid);
//
//		holder.setText(R.id.Consignee_Provice, waitingOrderBaseInfo.Consignee_City);
//		holder.setText(R.id.Shipping_Provice, waitingOrderBaseInfo.Shipping_City);
//		holder.setText(R.id.OrderFee, waitingOrderBaseInfo.OrderFee);
//		if (fGoodsType_Sheet != null) {
//			holder.setText(R.id.GoodsType, fGoodsType_Sheet.GoodsType);
//		}
//		holder.setText(R.id.time, waitingOrderBaseInfo.DeliveryTime);
//		if (fGoodsShape_Sheet != null) {
//			holder.setText(R.id.GoodsShapeGoodsType, fGoodsShape_Sheet.GoodsShape);
//		}
//		holder.setText(R.id.Weight, (waitingOrderBaseInfo.Weight == null ? "0" : waitingOrderBaseInfo.Weight));
//		holder.setText(R.id.Volume, (waitingOrderBaseInfo.Volume == null ? "0" : waitingOrderBaseInfo.Volume));
//	}
//}
