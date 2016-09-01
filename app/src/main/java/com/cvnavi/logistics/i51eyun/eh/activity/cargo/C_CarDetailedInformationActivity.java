/**
 * Administrator2016-4-26
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MCar;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MDict_CarCode_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.CollectDriverBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MDict_ConsignorCollect_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.C_CarDetailedCllBack;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.delivery.C_DeliveryGoodsFor2Activity;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:07:14
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_activity_findcar_detail2)
public class C_CarDetailedInformationActivity extends BaseActivity {

	@ViewInject(R.id.titlt_textView)
	private TextView titleTextView;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;

	@ViewInject(R.id.c_name_f)
	private TextView c_name_f;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.c_tel_f)
	private TextView c_tel_f;

	@ViewInject(R.id.c_age_f)
	private TextView c_age_f;

	@ViewInject(R.id.c_driving_type_f)
	private TextView c_driving_type_f;

	@ViewInject(R.id.jsz_imgView_f)
	private ImageView jsz_imgView_f;

	@ViewInject(R.id.jsz_imgView)
	private ImageView jsz_imgView;

	@ViewInject(R.id.c_name)
	private TextView c_name;

	@ViewInject(R.id.c_tel)
	private TextView c_tel;

	@ViewInject(R.id.c_age)
	private TextView c_age;

	@ViewInject(R.id.c_driving_type)
	private TextView c_driving_type;

	@ViewInject(R.id.c_car_code)
	private TextView c_car_code;

	@ViewInject(R.id.c_car_type)
	private TextView c_car_type;

	@ViewInject(R.id.c_encrypt)
	private TextView c_encrypt;

	@ViewInject(R.id.c_car_size)
	private TextView c_car_size;

	@ViewInject(R.id.c_Company_Name)
	private TextView c_Company_Name;

	/*@ViewInject(R.id.c_collect_car)
	private TextView c_collect_car;*/

	/*@ViewInject(R.id.c_sure)
	private TextView c_sure;*/

	@ViewInject(R.id.c_fahuo_btn)
	private TextView c_fahuo_btn;

	@ViewInject(R.id.c_driver_ll)
	private LinearLayout c_driver_ll;

	private String CollectKey;
	private String CarCode_Key;
	private MDict_CarCode_Sheet mCar;

	private CommonWaitDialog waitDialog;

	public static C_CarDetailedCllBack carDetailedCllBack;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private MCar car=new MCar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		initData();
	}

	/**
	 * 加载
	 */
	private void initData() {
		CarCode_Key = getIntent().getStringExtra("CarCode_Key");
		if (!TextUtils.isEmpty(CarCode_Key)) {
			waitDialog.show();
			loadCarListByCondition();
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		titleTextView.setText("车辆详细信息");
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
	}

	private void loadCarListByCondition() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Get_CarMsgListByCondition_URL);

				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(CarCode_Key);

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
								MDict_CarCode_Sheet carDetail = JsonUtils
										.parseData(str,
												MDict_CarCode_Sheet.class);
								if (carDetail != null) {
									msg.what = Constants.Request_Success;
									msg.obj = carDetail;
									myHandler.sendMessage(msg);
								} else {
									msg.what = Constants.Request_NoData;
									myHandler.sendMessage(msg);
								}
							} else {
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

	@Event(value = { R.id.back_linearLayout, R.id.c_collect_car,
			R.id.c_fahuo_btn }, type = View.OnClickListener.class)
	private void myOnCklick(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			this.finish();
			break;
		case R.id.c_collect_car://功能隐藏
			Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
			addCarRequest();
			break;
		/*case R.id.c_sure:
			finish();
			break;*/
		case R.id.c_fahuo_btn:
			
			 Intent intent = new Intent(this, C_DeliveryGoodsFor2Activity.class);
			 intent.putExtra(Constants.CarDetail_Info,car);
			 startActivity(intent);
			 finish();
			break;
		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			switch (msg.what) {
			
			case Constants.Request_Success:
				mCar = (MDict_CarCode_Sheet) msg.obj;
				setViewText();
				break;
			case Constants.Result_Fail:
				alertDialog = new AlertDialog.Builder(C_CarDetailedInformationActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				alertDialog.show();
				break;
			case Constants.Request_NoData:
				Toast.makeText(C_CarDetailedInformationActivity.this,
						"当前没有查询到数据！", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 收藏车辆
	 * 
	 */
	private void addCarRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Create_DeriverList_URL);
				try {
					MDict_ConsignorCollect_Sheet bean = new MDict_ConsignorCollect_Sheet();
					bean.User_Key = MyApplication.getInstatnce().getUserKey();
					bean.CollectKey = CollectKey;
					bean.CollectType = "A";

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid("B"); // MyApplication.getInstatnce().getUserTypeOid()
					requestData.setDataValue(JsonUtils.toJsonData(bean));

					params.addBodyParameter(null,
							JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						DataResultBase resultBase;
						try {
							resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
			}
		}).start();
	}

	public void setViewText() {

		if (mCar != null) {
			car.CarCode=mCar.CarCode;
			car.CarCode_Key=mCar.CarCode_Key;
			
			c_car_code.setText(mCar.CarCode);
			c_encrypt.setText(mCar.VehicleID_Code);
			c_car_type.setText(mCar.CarType);
			c_car_size.setText(Double.toString(mCar.Size));
			// CollectKey = mCar.CarCode_Key;
			List<CollectDriverBean> driver = mCar.Driver;
			if (driver != null && driver.size() > 0) {

				c_name.setText(driver.get(0).User_Name);
				c_tel.setText(driver.get(0).User_Tel);
				c_age.setText(driver.get(0).DrivingExperience);
				c_driving_type.setText(driver.get(0).DriversType);
				if (driver.get(0).IsPrimaryDriver.equals("1")) {
					c_Company_Name.setText(driver.get(0).Company_Name);
				}
				if (!TextUtils.isEmpty(driver.get(0).DriversLicense_Img)) {
					imageLoader.displayImage(driver.get(0).DriversLicense_Img, jsz_imgView);
//					x.image().bind(jsz_imgView,
//							driver.get(0).DriversLicense_Img);
				}
				if (driver != null && driver.size() > 1) {
					c_driver_ll.setVisibility(View.VISIBLE);
					c_name_f.setText(driver.get(1).User_Name);
					c_tel_f.setText(driver.get(1).User_Tel);
					c_age_f.setText(driver.get(1).DrivingExperience);
					c_driving_type_f.setText(driver.get(1).DriversType);
					if (driver.get(0).IsPrimaryDriver.equals("1")) {
						c_Company_Name.setText(driver.get(0).Company_Name);
					}
					if (!TextUtils.isEmpty(driver.get(1).DriversLicense_Img)) {
//						x.image().bind(jsz_imgView_f,
//								driver.get(1).DriversLicense_Img);
						imageLoader.displayImage(driver.get(1).DriversLicense_Img, jsz_imgView_f);
					}
				}
			}
		} else {
			LogUtil.e("-->>C_CarDetailedInformation--setViewText--");
		}
	}
}
