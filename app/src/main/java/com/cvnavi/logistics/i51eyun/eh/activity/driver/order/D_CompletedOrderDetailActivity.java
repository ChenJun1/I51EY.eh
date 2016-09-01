package com.cvnavi.logistics.i51eyun.eh.activity.driver.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.PictrueActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_PackageType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
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
 * @date 2016-5-17 下午1:08:09
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
@ContentView(R.layout.d_completed_order_detail)
public class D_CompletedOrderDetailActivity extends BaseActivity {

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

	@ViewInject(R.id.AB_img)
	private ImageView aB_imgView;
	@ViewInject(R.id.AB_tv)
	private TextView aB_tv;
	@ViewInject(R.id.AB_v)
	private View aB_v;
	@ViewInject(R.id.Received_Orders_Time)
	private TextView Received_Orders_Time;

	@ViewInject(R.id.AC_img)
	private ImageView aC_imgView;
	@ViewInject(R.id.AC_tv)
	private TextView aC_tv;
	@ViewInject(R.id.AC_v)
	private View aC_v;
	@ViewInject(R.id.tihuoshijian)
	private TextView tihuoshijian;
	@ViewInject(R.id.TH_Photo)
	private TextView TH_Photo;

	@ViewInject(R.id.AD_img)
	private ImageView aD_imgView;
	@ViewInject(R.id.AD_tv)
	private TextView aD_tv;
	@ViewInject(R.id.AD_v)
	private View aD_v;
	@ViewInject(R.id.AD_time_tv)
	private TextView AD_time_tv;

	@ViewInject(R.id.AF_img)
	private ImageView aF_imgView;
	@ViewInject(R.id.AF_tv)
	private TextView aF_tv;
	@ViewInject(R.id.AF_v)
	private View aF_v;
	@ViewInject(R.id.Goods_Unload_Time)
	private TextView Goods_Unload_Time;
	@ViewInject(R.id.JH_Photo)
	private TextView JH_Photo;

	@ViewInject(R.id.AG_img)
	private ImageView aG_imgView;
	@ViewInject(R.id.AG_tv)
	private TextView aG_tv;
	@ViewInject(R.id.Completed_Orders_Time)
	private TextView Completed_Orders_Time;

	@ViewInject(R.id.c_Actual_Consignee)
	private TextView c_Actual_Consignee;

	private Context context;
	private CommonWaitDialog waitDialog;
	
	private AlertDialog.Builder alertDialog;

	private OrderInfo orderDetailInfo;
	private WaitingOrderBaseInfo waiOrderBaseInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.titlt_textView.setText("运单明细");
		context = this;

		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		if (getIntent().getSerializableExtra(Constants.OrderInfo) != null) {
			waiOrderBaseInfo = (WaitingOrderBaseInfo) getIntent().getSerializableExtra(Constants.OrderInfo);
		}

		loadData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
	}

	private void loadData() {
		waitDialog.show();
		loadCompletedOrderRequest(Constants.OrderInfo_Url);
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
				if (msg.obj != null) {
					orderDetailInfo = (OrderInfo) msg.obj;
					setViewValue();
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(D_CompletedOrderDetailActivity.this, "加载数据为空！请联系客服！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_CompletedOrderDetailActivity.this);
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

	private void loadCompletedOrderRequest(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);

				try {
					OrderRequestBean orderInfo = new OrderRequestBean();
					orderInfo.Order_Key = waiOrderBaseInfo.Order_Key;
					orderInfo.Operate_Code = "AG";

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderInfo));

					// String jsonStr = JsonUtils.toJsonData(requestData);
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
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

	@Event(value = { R.id.back_linearLayout, R.id.TH_Photo, R.id.JH_Photo, R.id.c_Consignee_Tel,
			R.id.c_Shipper_Tel }, type = View.OnClickListener.class)
	private void onClick(View v) {
		Intent intent = null;

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
		case R.id.TH_Photo:
			if (orderDetailInfo == null) {
				Toast.makeText(context, "运单详情信息为空！不能查看照片！", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(orderDetailInfo.TH_Photo)) {
				Toast.makeText(context, "暂未上传提货照片！不能查看！", Toast.LENGTH_SHORT).show();
				return;
			}

			intent = new Intent(this, PictrueActivity.class);
			intent.putExtra(Constants.Image_pic, orderDetailInfo.TH_Photo);
			showActivity(this, intent);
			break;
		case R.id.JH_Photo:
			if (orderDetailInfo == null) {
				Toast.makeText(context, "运单详情信息为空！不能查看照片！", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(orderDetailInfo.TH_Photo)) {
				Toast.makeText(context, "暂未上传交货照片！不能查看！", Toast.LENGTH_SHORT).show();
				return;
			}

			intent = new Intent(this, PictrueActivity.class);
			intent.putExtra(Constants.Image_pic, orderDetailInfo.JH_Photo);
			showActivity(this, intent);
			break;
		default:
			break;
		}
	}

	private void setViewValue() {
		c_IsPhoto.setImageResource(
				ContextUtil.getDeliveryNeedPhoto(orderDetailInfo.IsTHPhoto, orderDetailInfo.IsJHPhoto));
		c_Operate_Code.setText(ContextUtil.getOperateCodeContent(orderDetailInfo.Operate_Code,
				orderDetailInfo.IsTHPhoto, orderDetailInfo.IsJHPhoto, MyApplication.getInstatnce().getUserTypeOid()));
		c_Operate_Code.setBackgroundResource(
				ContextUtil.getOperateStatusBg(orderDetailInfo.Operate_Code, orderDetailInfo.Order_Type));
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

		c_Consignee_Name.setText(
				TextUtils.isEmpty(orderDetailInfo.Consignee_Name) == true ? "--" : orderDetailInfo.Consignee_Name);
		c_Consignee_Tel.setText(
				TextUtils.isEmpty(orderDetailInfo.Consignee_Tel) == true ? "--" : orderDetailInfo.Consignee_Tel);
		c_Consignee_Address.setText(ContextUtil.getDetailAddress(orderDetailInfo.Consignee_Provice,
				orderDetailInfo.Consignee_City, orderDetailInfo.Consignee_Area, orderDetailInfo.Consignee_Address));

		c_Shipper_Name
				.setText(TextUtils.isEmpty(orderDetailInfo.Shipper_Name) == true ? "--" : orderDetailInfo.Shipper_Name);
		c_Shipper_Tel
				.setText(TextUtils.isEmpty(orderDetailInfo.Shipper_Tel) == true ? "--" : orderDetailInfo.Shipper_Tel);
		c_Shipping_Address.setText(ContextUtil.getDetailAddress(orderDetailInfo.Shipping_Provice,
				orderDetailInfo.Shipping_City, orderDetailInfo.Shipping_Area, orderDetailInfo.Shipping_Address));

		c_Shipper_Name_x
				.setText(TextUtils.isEmpty(orderDetailInfo.Shipper_Name) == true ? "--" : orderDetailInfo.Shipper_Name);
		c_ReleaseTime
				.setText(TextUtils.isEmpty(orderDetailInfo.ReleaseTime) == true ? "--" : orderDetailInfo.ReleaseTime);

		c_ReceivingTime.setText(
				TextUtils.isEmpty(orderDetailInfo.ReceivingTime) == true ? "--" : orderDetailInfo.ReceivingTime);

		SetViewValueUtil.setTextViewValue(c_Actual_Consignee, orderDetailInfo.Actual_Consignee);

		if (TextUtils.isEmpty(orderDetailInfo.IsTHPhoto) == false && orderDetailInfo.IsTHPhoto.equals("1")) {
			TH_Photo.setVisibility(View.VISIBLE);
			// if (TextUtils.isEmpty(orderInfo.TH_Photo)) {
			// TH_Photo.setText("上传图片");
			// }
		} else {
			TH_Photo.setVisibility(View.GONE);
		}

		if (TextUtils.isEmpty(orderDetailInfo.IsJHPhoto) == false && orderDetailInfo.IsJHPhoto.equals("1")) {
			JH_Photo.setVisibility(View.VISIBLE);
			// if (TextUtils.isEmpty(orderInfo.JH_Photo)) {
			// JH_Photo.setText("上传图片");
			// }
		} else {
			JH_Photo.setVisibility(View.GONE);
		}

		switch (orderDetailInfo.Operate_Code) {
		case "AG":
			aB_tv.setTextColor(getResources().getColor(R.color.orderblue));
			aB_imgView.setImageResource(R.drawable.cirxle_green);
			aB_v.setBackgroundColor(getResources().getColor(R.color.orderblue));

			aC_tv.setTextColor(getResources().getColor(R.color.orderblue));
			aC_imgView.setImageResource(R.drawable.cirxle_green);
			aC_v.setBackgroundColor(getResources().getColor(R.color.orderblue));

			aD_tv.setTextColor(getResources().getColor(R.color.orderblue));
			aD_imgView.setImageResource(R.drawable.cirxle_green);
			aD_v.setBackgroundColor(getResources().getColor(R.color.orderblue));

			aF_tv.setTextColor(getResources().getColor(R.color.orderblue));
			aF_imgView.setImageResource(R.drawable.cirxle_green);
			aF_v.setBackgroundColor(getResources().getColor(R.color.orderblue));

			aG_tv.setTextColor(getResources().getColor(R.color.orderblue));
			aG_imgView.setImageResource(R.drawable.cirxle_green);

			Received_Orders_Time.setText(TextUtils.isEmpty(orderDetailInfo.Received_Orders_Time) == true ? "--"
					: orderDetailInfo.Received_Orders_Time);
			tihuoshijian.setText(TextUtils.isEmpty(orderDetailInfo.Goods_Loading_Time) == true ? "--"
					: orderDetailInfo.Goods_Loading_Time);
			Goods_Unload_Time.setText(TextUtils.isEmpty(orderDetailInfo.ApplyCompleted_Orders_Time) == true ? "--"
					: orderDetailInfo.ApplyCompleted_Orders_Time);
			Completed_Orders_Time.setText(TextUtils.isEmpty(orderDetailInfo.Completed_Orders_Time) == true ? "--"
					: orderDetailInfo.Completed_Orders_Time);
			break;
		default:
			break;
		}

	}
}
