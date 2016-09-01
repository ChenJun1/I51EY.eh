package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.OrderDataCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.delivery.C_DeliveryGoodsFor2Activity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage.C_WaitingOrderFragment;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushOrderMessageBean;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:08:52
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_waiting_order_detail)
public class C_OrderInfoWaitingActivity extends BaseActivity implements
		OrderDataCallBack {

	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;

//	@ViewInject(R.id.c_waiticancle_btn)
//	private Button c_cancel_btn;
	
	private AlertDialog.Builder alertDialog;
	
	@ViewInject(R.id.c_waiticancle_btn)
	private Button c_waiticancle_btn;
	@ViewInject(R.id.c_IsPhoto)
	private ImageView c_IsPhoto;

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

	@ViewInject(R.id.operate_tx)
	private TextView operate_tx;

	@ViewInject(R.id.c_CarCode)
	private TextView c_CarCode;

	private CommonWaitDialog waitDialog;
	private OrderInfo orderInfo;
	private WaitingOrderBaseInfo waiOrderBaseInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		CallBackManager.getInstance().getOrderCallBackManager()
				.addCallBack(this);

		init();
		// initData();
	}

	/**
	 * 
	 */
	private void initView() {
		titlt_textView.setText("运单明细信息");
	}
	
	

	private void init() {
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		if (getIntent().getSerializableExtra(Constants.OrderKey) != null) {
			waiOrderBaseInfo = (WaitingOrderBaseInfo) getIntent()
					.getSerializableExtra(Constants.OrderKey);
		} 
		
		if (getIntent().getSerializableExtra(Constants.JPushOrderKey) != null) {
			waiOrderBaseInfo = new WaitingOrderBaseInfo();
			JPushOrderMessageBean messageBean = (JPushOrderMessageBean)getIntent().getSerializableExtra(Constants.JPushOrderKey);
			if (messageBean != null) {
				waiOrderBaseInfo.Order_Key = messageBean.DataValue;
				waiOrderBaseInfo.Operate_Code = messageBean.Operate_Code;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		initData();

		if (C_WaitingOrderFragment.usCallBack != null) {
			C_WaitingOrderFragment.usCallBack.onRefreshData();
		}
	}

	private void initData() {
		waitDialog.show();
		loadOrderInfoRequest(Constants.OrderInfo_Url);
	}

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			switch (msg.what) {
			case Constants.Request_Success:
				orderInfo = (OrderInfo) msg.obj;
				if (orderInfo != null) {
					setViewText();
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(C_OrderInfoWaitingActivity.this, "当前没有查询到信息！",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_OrderInfoWaitingActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new CommonWaitDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				alertDialog.show();
				
				break;
			case Constants.Request_cancelOrder:
				Toast.makeText(C_OrderInfoWaitingActivity.this, "取消指定成功",
						Toast.LENGTH_SHORT).show();
				if (C_WaitingOrderFragment.usCallBack != null) {
					C_WaitingOrderFragment.usCallBack
							.onUpdateData(waiOrderBaseInfo);
				}
				C_OrderInfoWaitingActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};

	private void loadOrderInfoRequest(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				// WaitingOrderBaseInfo orderInfo = new WaitingOrderBaseInfo();
				// orderInfo.Order_Key = waiOrderBaseInfo.Order_Key;
				// orderInfo.Operate_Code = "AA";
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce()
							.getCompany_Oid());
					requestData.setDataValue(JsonUtils
							.toJsonData(waiOrderBaseInfo));

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
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								OrderInfo dataBean = JsonUtils.parseData(str,
										OrderInfo.class);
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

	@Event(value = { R.id.back_linearLayout, R.id.operate_tx,
			R.id.c_waiticancle_btn, R.id.c_Consignee_Tel, R.id.c_Shipper_Tel }, type = View.OnClickListener.class)
	private void OnClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.c_Consignee_Tel:
			ContextUtil.callAlertDialog(c_Consignee_Tel.getText().toString(),
					this);
			break;
		case R.id.c_Shipper_Tel:
			ContextUtil.callAlertDialog(c_Shipper_Tel.getText().toString(),
					this);
			break;
		case R.id.operate_tx:
			Intent intent = new Intent(this, C_DeliveryGoodsFor2Activity.class);
			intent.putExtra(Constants.OrderInfo, waiOrderBaseInfo);
			startActivity(intent);
			break;
		case R.id.c_waiticancle_btn:
			if (!TextUtils.isEmpty(orderInfo.CarCode)) {
				cancelOrder(Constants.Refuse_HadCarOrder_Url);

			} else if (!TextUtils.isEmpty(orderInfo.CarCode)) {
				Toast.makeText(this, "已取消指定", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 取消订单
	 */
	private void cancelOrder(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(waiOrderBaseInfo.Order_Key);

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
							Message msg = Message.obtain();
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								msg.what = Constants.Request_cancelOrder;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Request_Fail;
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

	private void setViewText() {

		if (TextUtils.isEmpty(orderInfo.CarCode_Key)) {
			c_waiticancle_btn.setVisibility(View.GONE);
		}else{
			c_waiticancle_btn.setVisibility(View.VISIBLE);
		}
		if (orderInfo != null) {

			SetViewValueUtil.setTextViewValue(c_Shipping_City_tv,
					orderInfo.Shipping_City);
			SetViewValueUtil.setTextViewValue(c_Consignee_City_tv,
					orderInfo.Consignee_City);
			SetViewValueUtil.setTextViewValue(c_DeliveryTime_tv,orderInfo.DeliveryTime);

			Dict_GoodsType_Sheet goodTypeByKey = MyApplication.getInstatnce()
					.getBasicDataBuffer()
					.getGoodTypeByKey(orderInfo.GoodsType_Oid);
			Dict_GoodsShape_Sheet goodShapeKey = MyApplication.getInstatnce()
					.getBasicDataBuffer()
					.getGoodsShapeByKey(orderInfo.GoodsShape_Oid);
			Dict_PackageType_Sheet packageTypeByKey = MyApplication
					.getInstatnce().getBasicDataBuffer()
					.getPackageTypeByKey(orderInfo.PackageType_Oid);

			SetViewValueUtil.setTextViewValue(c_PackageType_Name,
					packageTypeByKey.PackageType);
			SetViewValueUtil.setTextViewValue(c_GoodsType,
					goodTypeByKey.GoodsType);
			SetViewValueUtil.setTextViewValue(c_GoodsShape,
					goodShapeKey.GoodsShape);

			SetViewValueUtil.setWeightValue(c_Weight, orderInfo.Weight);
			SetViewValueUtil.setVolumeValue(c_Volume, orderInfo.Volume);
			SetViewValueUtil.setTextViewValue(c_TotalFee, orderInfo.TotalFee);
			SetViewValueUtil.setTextViewValue(c_Serial_Oid,
					orderInfo.Serial_Oid);

			SetViewValueUtil.setNumberValue(c_Number, orderInfo.Number);
			SetViewValueUtil.setTextViewValue(c_PackageType_Name,
					orderInfo.PackageType);
			SetViewValueUtil.setTextViewValue(c_Consignee_Name,
					orderInfo.Consignee_Name);
			SetViewValueUtil.setTextViewValue(c_Consignee_Tel,
					orderInfo.Consignee_Tel);

			SetViewValueUtil.setTextViewValue(c_Consignee_Address,
					ContextUtil.getDetailAddress(orderInfo.Consignee_Provice, orderInfo.Consignee_City, orderInfo.Consignee_Area, orderInfo.Consignee_Address));
			SetViewValueUtil.setTextViewValue(c_Shipper_Name,
					orderInfo.Shipper_Name);
			SetViewValueUtil.setTextViewValue(c_Shipper_Tel,
					orderInfo.Shipper_Tel);

			SetViewValueUtil.setTextViewValue(c_Shipping_Address,
					ContextUtil.getDetailAddress(orderInfo.Shipping_Provice, orderInfo.Shipping_City, orderInfo.Shipping_Area, orderInfo.Shipping_Address));

			SetViewValueUtil.setTextViewValue(c_Shipper_Name_x,
					orderInfo.Shipper_Name);
			SetViewValueUtil.setTextViewValue(c_ReleaseTime,
					orderInfo.ReleaseTime);
			SetViewValueUtil.setTextViewValue(c_ReceivingTime,
					orderInfo.ReceivingTime);
			SetViewValueUtil.setTextViewValue(c_CarCode, orderInfo.CarCode);
			
			boolean bool=false;
			if (orderInfo.IsTHPhoto != null && orderInfo.IsTHPhoto.equals("1")) {
				c_IsTHPhoto.setImageResource(R.drawable.seck);
				bool=true;
			}else{
				c_IsTHPhoto.setImageResource(R.drawable.seckgey);
			}
			if (orderInfo.IsJHPhoto != null && orderInfo.IsJHPhoto.equals("1")) {
				c_IsJHPhoto.setImageResource(R.drawable.seck);
				bool=true;
			}else{
				c_IsJHPhoto.setImageResource(R.drawable.seckgey);
			}
			if(bool){
				c_IsPhoto.setImageResource(R.drawable.photohong);	
			}
		}
	}

	@Override
	public void onRefresh(Object object) {
		if (object != null) {

			final OrderBase orderBase = (OrderBase) object;
			waiOrderBaseInfo.Shipping_City = orderBase.Shipping_City;
			waiOrderBaseInfo.Consignee_City = orderBase.Consignee_City;
			waiOrderBaseInfo.DeliveryTime = orderBase.DeliveryTime;
			waiOrderBaseInfo.TotalFee = orderBase.OrderFee;
			waiOrderBaseInfo.GoodsType_Oid = orderBase.GoodsType_Oid;
			waiOrderBaseInfo.GoodsShape_Oid = orderBase.GoodsShape_Oid;
			waiOrderBaseInfo.Weight = orderBase.Weight;
			waiOrderBaseInfo.Volume = orderBase.Volume;
			myHandler.post(new Runnable() {

				@Override
				public void run() {
					if (!TextUtils.isEmpty(orderBase.CarCode_Key)) {
						c_waiticancle_btn.setVisibility(View.VISIBLE);
					}
				}
			});

		}
	}
}
