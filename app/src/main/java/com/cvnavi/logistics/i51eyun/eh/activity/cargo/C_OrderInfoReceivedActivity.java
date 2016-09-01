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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.PictrueActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.cancelorder.CancelOrder;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_PackageType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.delivery.C_DeliveryGoodsFor2Activity;
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
 * @date 2016-5-17 下午1:08:46
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_received_order_detail)
public class C_OrderInfoReceivedActivity extends BaseActivity {

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;

	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.c_cancle_btn)
	private Button c_cancle_btn;

	@ViewInject(R.id.c_cancel_btn_ll)
	private LinearLayout c_cancel_btn_Layout;

	@ViewInject(R.id.cancle_txll)
	private LinearLayout cancle_txll;
	
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

	@ViewInject(R.id.operate_tx)
	private TextView operate_tx;

	@ViewInject(R.id.AB_img)
	private ImageView aB_imgView;

	@ViewInject(R.id.AC_img)
	private ImageView aC_imgView;

	@ViewInject(R.id.AD_img)
	private ImageView aD_imgView;

	@ViewInject(R.id.AF_img)
	private ImageView aF_imgView;

	@ViewInject(R.id.AG_img)
	private ImageView aG_imgView;

	@ViewInject(R.id.AB_tv)
	private TextView aB_tv;

	@ViewInject(R.id.AC_tv)
	private TextView aC_tv;

	@ViewInject(R.id.AD_tv)
	private TextView aD_tv;

	@ViewInject(R.id.AF_tv)
	private TextView aF_tv;

	@ViewInject(R.id.AG_tv)
	private TextView aG_tv;
	
	@ViewInject(R.id.AB_v)
	private View aB_v;

	@ViewInject(R.id.AC_v)
	private View aC_v;

	@ViewInject(R.id.AD_v)
	private View aD_v;

	@ViewInject(R.id.AF_v)
	private View aF_v;
	

	@ViewInject(R.id.Received_Orders_Time)
	private TextView Received_Orders_Time;

	@ViewInject(R.id.tihuoshijian)
	private TextView tihuoshijian;
	
	@ViewInject(R.id.AD_time_tv)
	private TextView AD_time_tv;

	@ViewInject(R.id.Goods_Unload_Time)
	private TextView Goods_Unload_Time;

	// @ViewInject(R.id.completed_orders_time)
	// private TextView Completed_Orders_Time;

	@ViewInject(R.id.TH_Photo)
	private EditText TH_Photo;
	

	@ViewInject(R.id.JH_Photo)
	private EditText JH_Photo;
	
	@ViewInject(R.id.c_CalShipperRule_tv)
	private TextView c_CalShipperRule_tv;

	@ViewInject(R.id.c_waiticancle_btn)
	private Button c_cancel_btn;

	private CommonWaitDialog waitDialog;

	private WaitingOrderBaseInfo waiOrderBaseInfo;

	private OrderInfo orderInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		titlt_textView.setText("运单明细信息");
		init();
		initLoadData();
	}

	private void initLoadData() {
		loadWaitinOrderRequest(Constants.OrderInfo_Url);
	}

	private void init() {
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		if (getIntent().getSerializableExtra(Constants.OrderKey) != null) {
			waiOrderBaseInfo = (WaitingOrderBaseInfo) getIntent()
					.getSerializableExtra(Constants.OrderKey);
		}
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
	}



	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			switch (msg.what) {
			
			case Constants.Request_cancelOrder:
				Toast.makeText(C_OrderInfoReceivedActivity.this, "取消发货成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case Constants.Request_Success:
				orderInfo = (OrderInfo) msg.obj;
				setViewText();
				break;
			case Constants.Request_NoData:
				Toast.makeText(C_OrderInfoReceivedActivity.this, "当前没有查询到信息！",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_OrderInfoReceivedActivity.this);
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
			case Constants.Result_cancelTime:
				Toast.makeText(C_OrderInfoReceivedActivity.this, "超过取消发货时间，不能取消发货！",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	public void loadWaitinOrderRequest(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				// WaitingOrderBaseInfo orderInfo = new WaitingOrderBaseInfo();
				// orderInfo.Order_Key = waiOrderBaseInfo.Order_Key;
				// orderInfo.Operate_Code = "AB";
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

	@Event(value = { R.id.back_linearLayout, R.id.TH_Photo, R.id.c_cancle_btn,
			R.id.operate_tx, R.id.c_Consignee_Tel, R.id.c_Shipper_Tel,R.id.TH_Photo,R.id.JH_Photo }, type = View.OnClickListener.class)
	private void OnClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.operate_tx:
			intent = new Intent(this, C_DeliveryGoodsFor2Activity.class);
			intent.putExtra(Constants.OrderInfo, waiOrderBaseInfo);
			startActivity(intent);
			break;
		case R.id.c_cancle_btn:
			cancelOrder();
			
			break;
		case R.id.c_Consignee_Tel:
			ContextUtil.callAlertDialog(c_Consignee_Tel.getText().toString(),
					this);
			break;
		case R.id.c_Shipper_Tel:
			ContextUtil.callAlertDialog(c_Shipper_Tel.getText().toString(),
					this);
			break;
		case R.id.TH_Photo:
			intent=new Intent(this, PictrueActivity.class);
			intent.putExtra(Constants.Image_pic, orderInfo.TH_Photo);
			startActivity(intent);
			break;
		case R.id.JH_Photo:
			intent=new Intent(this, PictrueActivity.class);
			intent.putExtra(Constants.Image_pic, orderInfo.JH_Photo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 取消发货
	 */
	private void cancelOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.CancelOrder_URL);
//				OrderInfo info = new OrderInfo();
//				info.Order_Key=waiOrderBaseInfo.Order_Key;
				CancelOrder cancelOrder = new CancelOrder();
				cancelOrder.DeliveryTime=orderInfo.DeliveryTime;
				cancelOrder.Operate_Code=orderInfo.Operate_Code;
				cancelOrder.Order_Key=orderInfo.Order_Key;
				cancelOrder.Received_Orders_Time=orderInfo.Received_Orders_Time;
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(cancelOrder));

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
							} else if(!TextUtils.isEmpty(resultBase.getErrorText())&&resultBase.getErrorText().equals("已超过取消时间")) {
								msg.what = Constants.Result_cancelTime;
								myHandler.sendMessage(msg);
							}else{
								msg.what = Constants.Request_Fail;
								msg.obj=resultBase.getErrorText();
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

	@SuppressLint("ResourceAsColor")
	private void setViewText() {

		if (orderInfo != null) {

			SetViewValueUtil.setTextViewValue(c_Shipping_City_tv,
					orderInfo.Shipping_City);
			SetViewValueUtil.setTextViewValue(c_Consignee_City_tv,
					orderInfo.Consignee_City);
			SetViewValueUtil.setTextViewValue(c_DeliveryTime_tv,
					orderInfo.DeliveryTime);
			SetViewValueUtil.setTextViewValue(Received_Orders_Time, orderInfo.Received_Orders_Time);
			SetViewValueUtil.setTextViewValue(tihuoshijian, orderInfo.Goods_Loading_Time);
			//SetViewValueUtil.setTextViewValue(AD_time_tv, orderInfo.Goods_Unload_Time);
			SetViewValueUtil.setTextViewValue(Goods_Unload_Time, orderInfo.ApplyCompleted_Orders_Time);

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
			// SetViewValueUtil.setTextViewValue(c_CarCode, orderInfo.CarCode);
			if(!TextUtils.isEmpty(orderInfo.CalShipperRule)){
				double CalShipperRule=Double.valueOf(orderInfo.CalShipperRule);
				c_CalShipperRule_tv.setText("该订单可以在（提货时间-接单时间）*"+(CalShipperRule*100)+"%前取消");
			}
			
			if(!orderInfo.Operate_Code.equals("AB")){
				operate_tx.setVisibility(View.GONE);
				c_cancle_btn.setVisibility(View.GONE);
				cancle_txll.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(orderInfo.IsTHPhoto)
					&& orderInfo.IsTHPhoto.equals("1")) {
				c_IsPhoto.setImageResource(R.drawable.photohong);

			}
			if (!TextUtils.isEmpty(orderInfo.IsJHPhoto)
					&& orderInfo.IsJHPhoto.equals("1")) {
				c_IsPhoto.setImageResource(R.drawable.photohong);
			}
			
			switch (orderInfo.Operate_Code) {

			case "AB":
				aB_tv.setTextColor(Constants.BLUE_COLOR);
				aB_imgView.setImageResource(R.drawable.cirxle_green);
				break;
			case "AC":
				aB_v.setBackgroundColor(Constants.BLUE_COLOR);
				aB_tv.setTextColor(Constants.BLUE_COLOR);
				aB_imgView.setImageResource(R.drawable.cirxle_green);
				aC_tv.setTextColor(Constants.BLUE_COLOR);
				aC_imgView.setImageResource(R.drawable.cirxle_green);
				if (!TextUtils.isEmpty(orderInfo.IsTHPhoto)
						&& orderInfo.IsTHPhoto.equals("1")) {
					TH_Photo.setVisibility(View.VISIBLE);

				}
				break;
			case "AD":
				aB_v.setBackgroundColor(Constants.BLUE_COLOR);
				aC_v.setBackgroundColor(Constants.BLUE_COLOR);
				aB_tv.setTextColor(Constants.BLUE_COLOR);
				aB_imgView.setImageResource(R.drawable.cirxle_green);
				aC_tv.setTextColor(Constants.BLUE_COLOR);
				aC_imgView.setImageResource(R.drawable.cirxle_green);
				aD_tv.setTextColor(Constants.BLUE_COLOR);
				aD_imgView.setImageResource(R.drawable.cirxle_green);
				break;
			case "AF":
				if (!TextUtils.isEmpty(orderInfo.IsTHPhoto)
						&& orderInfo.IsTHPhoto.equals("1")) {
					TH_Photo.setVisibility(View.VISIBLE);

				}
				if (!TextUtils.isEmpty(orderInfo.IsJHPhoto)
						&& orderInfo.IsJHPhoto.equals("1")) {
					JH_Photo.setVisibility(View.VISIBLE);
				}
				aB_v.setBackgroundColor(Constants.BLUE_COLOR);
				aC_v.setBackgroundColor(Constants.BLUE_COLOR);
				aD_v.setBackgroundColor(Constants.BLUE_COLOR);
				aB_tv.setTextColor(Constants.BLUE_COLOR);
				aB_imgView.setImageResource(R.drawable.cirxle_green);
				aC_tv.setTextColor(Constants.BLUE_COLOR);
				aC_imgView.setImageResource(R.drawable.cirxle_green);
				aD_tv.setTextColor(Constants.BLUE_COLOR);
				aD_imgView.setImageResource(R.drawable.cirxle_green);
				aF_tv.setTextColor(Constants.BLUE_COLOR);
				aF_imgView.setImageResource(R.drawable.cirxle_green);
				break;
			case "AG":
				if (!TextUtils.isEmpty(orderInfo.IsTHPhoto)
						&& orderInfo.IsTHPhoto.equals("1")) {
					TH_Photo.setVisibility(View.VISIBLE);

				}
				if (!TextUtils.isEmpty(orderInfo.IsJHPhoto)
						&& orderInfo.IsJHPhoto.equals("1")) {
					JH_Photo.setVisibility(View.VISIBLE);
				}
				aB_v.setBackgroundColor(Constants.BLUE_COLOR);
				aC_v.setBackgroundColor(Constants.BLUE_COLOR);
				aD_v.setBackgroundColor(Constants.BLUE_COLOR);
				aF_v.setBackgroundColor(Constants.BLUE_COLOR);
				aB_tv.setTextColor(Constants.BLUE_COLOR);
				aB_imgView.setImageResource(R.drawable.cirxle_green);
				aC_tv.setTextColor(Constants.BLUE_COLOR);
				aC_imgView.setImageResource(R.drawable.cirxle_green);
				aD_tv.setTextColor(Constants.BLUE_COLOR);
				aD_imgView.setImageResource(R.drawable.cirxle_green);
				aF_tv.setTextColor(Constants.BLUE_COLOR);
				aF_imgView.setImageResource(R.drawable.cirxle_green);
				aG_tv.setTextColor(Constants.BLUE_COLOR);
				aG_imgView.setImageResource(R.drawable.cirxle_green);
				
				break;
			default:
				break;
			}

		}
		
	}

}
