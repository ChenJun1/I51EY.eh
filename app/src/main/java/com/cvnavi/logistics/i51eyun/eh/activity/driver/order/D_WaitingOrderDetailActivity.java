package com.cvnavi.logistics.i51eyun.eh.activity.driver.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_PackageType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.type.WorkStatusType;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushOrderMessageBean;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:08:09
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_waiting_order_detail)
public class D_WaitingOrderDetailActivity extends BaseActivity { // implements
																	// OrderDataCallBack

	@ViewInject(R.id.operation_btn)
	private Button operation_btn;

	@ViewInject(R.id.c_IsPhoto)
	private ImageView c_IsPhoto;
	@ViewInject(R.id.c_Operate_Code)
	private TextView c_Operate_Code;

	@ViewInject(R.id.c_Shipping_City_tv)
	private TextView c_Shipping_City_tv;
	@ViewInject(R.id.c_Consignee_City_tv)
	private TextView c_Consignee_City_tv;
	@ViewInject(R.id.c_DeliveryTime_tv)
	private TextView c_DeliveryTime_tv;
	@ViewInject(R.id.c_GoodsType)
	private TextView c_GoodsType;
	@ViewInject(R.id.c_GoodsShape)
	private TextView c_GoodsShape;
	@ViewInject(R.id.c_Weight)
	private TextView c_Weight;
	@ViewInject(R.id.c_Volume)
	private TextView c_Volume;
	@ViewInject(R.id.c_TotalFee)
	private TextView c_TotalFee;

	@ViewInject(R.id.c_Serial_Oid)
	private TextView c_Serial_Oid;
	@ViewInject(R.id.c_Number)
	private TextView c_Number;
	@ViewInject(R.id.c_PackageType_Name)
	private TextView c_PackageType_Name;

	@ViewInject(R.id.c_Consignee_Name)
	private TextView c_Consignee_Name;
	@ViewInject(R.id.c_Consignee_Tel)
	private TextView c_Consignee_Tel;
	@ViewInject(R.id.c_Consignee_Address)
	private TextView c_Consignee_Address;

	@ViewInject(R.id.c_Shipper_Name)
	private TextView c_Shipper_Name;
	@ViewInject(R.id.c_Shipper_Tel)
	private TextView c_Shipper_Tel;
	@ViewInject(R.id.c_Shipping_Address)
	private TextView c_Shipping_Address;

	@ViewInject(R.id.c_Shipper_Name_x)
	private TextView c_Shipper_Name_x;
	@ViewInject(R.id.c_ReleaseTime)
	private TextView c_ReleaseTime;

	@ViewInject(R.id.c_ReceivingTime)
	private TextView c_ReceivingTime;

	@ViewInject(R.id.c_IsTHPhoto)
	private ImageView c_IsTHPhoto;
	@ViewInject(R.id.c_IsJHPhoto)
	private ImageView c_IsJHPhoto;

	@ViewInject(R.id.appoint_car_layout)
	private LinearLayout appoint_car_layout;
	@ViewInject(R.id.c_CarCode)
	private TextView c_CarCode;

	private CommonWaitDialog waitDialog;
	private AlertDialog.Builder alertDialog;

	private OrderInfo orderDetailInfo;
	private WaitingOrderBaseInfo waiOrderBaseInfo;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);

		if (getIntent().getSerializableExtra(Constants.OrderInfo) != null) {
			waiOrderBaseInfo = (WaitingOrderBaseInfo) getIntent().getSerializableExtra(Constants.OrderInfo);
		}

		if (getIntent().getSerializableExtra(Constants.JPushOrderKey) != null) {
			waiOrderBaseInfo = new WaitingOrderBaseInfo();
			JPushOrderMessageBean messageBean = (JPushOrderMessageBean) getIntent()
					.getSerializableExtra(Constants.JPushOrderKey);
			if (messageBean != null) {
				waiOrderBaseInfo.Order_Key = messageBean.DataValue;
				waiOrderBaseInfo.Operate_Code = messageBean.Operate_Code;
			}
		}

		initView();
		loadData();
	}

	private void initView() {
		this.titlt_textView.setText("运单明细");
		operation_btn.setText("拒绝接单");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		// loadData();
	}

	private void loadData() {
		waitDialog.show();

		loadOrderInfoData(Constants.OrderInfo_Url);
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_Success:
				orderDetailInfo = (OrderInfo) msg.obj;
				if (orderDetailInfo != null) {
					setViewValue();
				}
				break;
			case Constants.Request_OrderCome:
				Toast.makeText(context, "恭喜您，接单成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case Constants.Request_RejectOrder:
				Toast.makeText(context, "拒绝接单成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case Constants.Request_NoData:
				Toast.makeText(context, "加载运单无数据，请联系客服！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_WaitingOrderDetailActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			default:
				break;
			}
		}
	};

	private void loadOrderInfoData(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Order_Key = waiOrderBaseInfo.Order_Key;
					orderRequestBean.Operate_Code = waiOrderBaseInfo.Operate_Code;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					String jsonStr = JsonUtils.toJsonData(requestData);
					params.addBodyParameter(null, jsonStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj=Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								OrderInfo dataBean = JsonUtils.parseData(str, OrderInfo.class);
								if (dataBean != null) {
									msg.what = Constants.Request_Success;
									msg.obj = dataBean;
									myHandler.sendMessage(msg);
								} else {
									msg.what = Constants.Request_NoData;
									myHandler.sendMessage(msg);
								}
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	@Event(value = { R.id.back_linearLayout, R.id.receive_order_btn, R.id.operation_btn, R.id.c_Consignee_Tel,
			R.id.c_Shipper_Tel }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.c_Consignee_Tel:
			ContextUtil.callAlertDialog(c_Consignee_Tel.getText().toString(), this);
			break;
		case R.id.c_Shipper_Tel:
			ContextUtil.callAlertDialog(c_Shipper_Tel.getText().toString(), this);
			break;
		case R.id.operation_btn:
			alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("提示");
			alertDialog.setMessage("确认拒绝接单吗？");
			alertDialog.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

					rejectOrder();
				}
			});
			alertDialog.show();
			break;
		case R.id.receive_order_btn:
			if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == false && Integer
					.valueOf(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == WorkStatusType.OffWork) {
				alertDialog = new AlertDialog.Builder(context);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage("您还未上班，不能接单！");
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				return;
			}

			receivingOrder();
			break;
		default:
			break;
		}
	}

	private void receivingOrder() {
		waitDialog.show();
		new Thread() {
			public void run() {
				JSONObject datavalue = new JSONObject();
				try {
					datavalue.put("Order_Type", orderDetailInfo.Order_Type);
					datavalue.put("Order_Key", orderDetailInfo.Order_Key);
				} catch (JSONException e2) {
					e2.printStackTrace();
				}

				DataRequestData requestData = new DataRequestData();
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
				requestData.setDataValue(datavalue.toString());
				requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());

				RequestParams params = new RequestParams(Constants.Receive_WaitingOrder_Url);
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String jsonStr) {
						LogUtil.d("-->>onSuccess：" + jsonStr);

						DataResultBase resultBase;
						try {
							resultBase = JsonUtils.parseDataResultBase(jsonStr);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								msg.what = Constants.Request_OrderCome;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable throwable, boolean b) {
						LogUtil.d("-->>onError");
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = throwable.getMessage();
						myHandler.sendMessage(msg);
					}

					@Override
					public void onCancelled(CancelledException e) {
						LogUtil.d("-->>onCancelled");
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>onFinished");
					}
				});
			};
		}.start();
	}

	private void rejectOrder() {
		waitDialog.show();
		new Thread() {
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
				requestData.setDataValue(orderDetailInfo.Order_Key);
				requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());

				RequestParams params = new RequestParams(Constants.Refuse_HadCarOrder_Url);
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String jsonStr) {
						LogUtil.d("-->>onSuccess：" + jsonStr);

						DataResultBase resultBase;
						try {
							resultBase = JsonUtils.parseDataResultBase(jsonStr);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								msg.what = Constants.Request_RejectOrder;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable throwable, boolean b) {
						LogUtil.d("-->>onError：" + throwable.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj=Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onCancelled(CancelledException e) {
						LogUtil.d("-->>onCancelled");
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>onFinished");
					}
				});
			}
		}.start();
	}

	// orderInfo != null时调用。
	private void setViewValue() {
		// 公共订单不能拒绝接单，无指定车辆；指定订单可以拒绝接单，有指定车辆。
		if (TextUtils.isEmpty(orderDetailInfo.Order_Type) || orderDetailInfo.Order_Type.equals("A")) {
			operation_btn.setVisibility(View.GONE);
			appoint_car_layout.setVisibility(View.GONE);
		} else if (orderDetailInfo.Order_Type.equals("B")) {
			appoint_car_layout.setVisibility(View.VISIBLE);
			operation_btn.setVisibility(View.VISIBLE);
			// 设置指定车辆
			c_CarCode.setText(orderDetailInfo.CarCode);
		}

		c_IsPhoto.setImageResource(ContextUtil.getDeliveryNeedPhoto(orderDetailInfo.IsTHPhoto, orderDetailInfo.IsJHPhoto));
		c_Operate_Code.setText(ContextUtil.getOperateCodeContent(orderDetailInfo.Operate_Code, orderDetailInfo.IsTHPhoto,
				orderDetailInfo.IsJHPhoto, MyApplication.getInstatnce().getUserTypeOid()));
		c_Operate_Code
				.setBackgroundResource(ContextUtil.getOperateStatusBg(orderDetailInfo.Operate_Code, orderDetailInfo.Order_Type));
		SetViewValueUtil.setTextViewValue(c_Shipping_City_tv, orderDetailInfo.Shipping_City);
		SetViewValueUtil.setTextViewValue(c_Consignee_City_tv, orderDetailInfo.Consignee_City);
		SetViewValueUtil.setTextViewValue(c_DeliveryTime_tv,orderDetailInfo.DeliveryTime);

		Dict_GoodsType_Sheet goodTypeByKey = MyApplication.getInstatnce().getBasicDataBuffer()
				.getGoodTypeByKey(orderDetailInfo.GoodsType_Oid);
		Dict_GoodsShape_Sheet goodShapeKey = MyApplication.getInstatnce().getBasicDataBuffer()
				.getGoodsShapeByKey(orderDetailInfo.GoodsShape_Oid);

		c_GoodsType.setText(goodTypeByKey == null ? "--" : goodTypeByKey.GoodsType);
		c_GoodsShape.setText(goodShapeKey == null ? "--" : goodShapeKey.GoodsShape);

		SetViewValueUtil.setWeightValue(c_Weight, orderDetailInfo.Weight);
		SetViewValueUtil.setVolumeValue(c_Volume, orderDetailInfo.Volume);
		SetViewValueUtil.setNumberValue(c_Number, orderDetailInfo.Number);
		SetViewValueUtil.setTextViewValue(c_TotalFee, orderDetailInfo.TotalFee);

		SetViewValueUtil.setTextViewValue(c_Serial_Oid, orderDetailInfo.Serial_Oid);

		Dict_PackageType_Sheet packageType = MyApplication.getInstatnce().getBasicDataBuffer()
				.getPackageTypeByKey(orderDetailInfo.PackageType_Oid);
		c_PackageType_Name.setText(packageType == null ? "--" : packageType.PackageType);

		c_Consignee_Name.setText(TextUtils.isEmpty(orderDetailInfo.Consignee_Name) == true ? "--" : orderDetailInfo.Consignee_Name);
		c_Consignee_Tel.setText(TextUtils.isEmpty(orderDetailInfo.Consignee_Tel) == true ? "--" : orderDetailInfo.Consignee_Tel);
		c_Consignee_Address.setText(ContextUtil.getDetailAddress(orderDetailInfo.Consignee_Provice, orderDetailInfo.Consignee_City,
				orderDetailInfo.Consignee_Area, orderDetailInfo.Consignee_Address));

		c_Shipper_Name.setText(TextUtils.isEmpty(orderDetailInfo.Shipper_Name) == true ? "--" : orderDetailInfo.Shipper_Name);
		c_Shipper_Tel.setText(TextUtils.isEmpty(orderDetailInfo.Shipper_Tel) == true ? "--" : orderDetailInfo.Shipper_Tel);
		c_Shipping_Address.setText(ContextUtil.getDetailAddress(orderDetailInfo.Shipping_Provice, orderDetailInfo.Shipping_City,
				orderDetailInfo.Shipping_Area, orderDetailInfo.Shipping_Address));

		c_Shipper_Name_x.setText(TextUtils.isEmpty(orderDetailInfo.Shipper_Name) == true ? "--" : orderDetailInfo.Shipper_Name);
		c_ReleaseTime.setText(TextUtils.isEmpty(orderDetailInfo.ReleaseTime) == true ? "--" : orderDetailInfo.ReleaseTime);

		c_ReceivingTime.setText(TextUtils.isEmpty(orderDetailInfo.ReceivingTime) == true ? "--" : orderDetailInfo.ReceivingTime);

		if (TextUtils.isEmpty(orderDetailInfo.IsTHPhoto) == false && orderDetailInfo.IsTHPhoto.equals("1")) {
			c_IsTHPhoto.setImageResource(R.drawable.seck);
		} else {
			c_IsTHPhoto.setImageResource(R.drawable.seckgey);
		}

		if (TextUtils.isEmpty(orderDetailInfo.IsJHPhoto) == false && orderDetailInfo.IsJHPhoto.equals("1")) {
			c_IsJHPhoto.setImageResource(R.drawable.seck);
		} else {
			c_IsJHPhoto.setImageResource(R.drawable.seckgey);
		}
	}

}
