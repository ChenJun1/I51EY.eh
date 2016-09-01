package com.cvnavi.logistics.i51eyun.eh.activity.driver.me;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.address.CarCodeRegisterAddressActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.address.CarCodeRegisterAddressActivity.CarCodeRegisterAddressCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MDict_CarCode_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarSize_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.driver.DeirverRegisterResultBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Driver;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.ParkDataCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_ShowMDictListActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.RegisteredActivity;
import com.cvnavi.logistics.i51eyun.eh.cache.BasicDataBuffer;
import com.cvnavi.logistics.i51eyun.eh.cache.BasicDataBuffer.LoadBasicDataStatusCallBack;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.BitmapUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.VerifyBankCardUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.VerifyCarNumUtil;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(value = R.layout.d_personal_info)
public class D_Personal_InfoFor2Activity extends BaseActivity
		implements ParkDataCallBack, LoadBasicDataStatusCallBack, CarCodeRegisterAddressCallBack {

	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	@ViewInject(value = R.id.name_dt)
	private EditText name_dt;
	@ViewInject(value = R.id.phone_et)
	private EditText phone_et;
	@ViewInject(value = R.id.id_number_et)
	private EditText id_number_et;
	@ViewInject(value = R.id.bank_number_et)
	private EditText bank_number_et;

	@ViewInject(value = R.id.driving_type_tv)
	private TextView driving_type_tv;
	@ViewInject(value = R.id.driving_age_et)
	private EditText driving_age_et;

	@ViewInject(value = R.id.park_tv)
	private TextView park_tv;

	@ViewInject(value = R.id.car_code_register_address_et)
	private EditText car_code_register_address_et;
	@ViewInject(value = R.id.car_code_et)
	private EditText car_code_et;
	@ViewInject(value = R.id.car_id_code_et)
	private EditText car_id_code_et;

	@ViewInject(value = R.id.car_type_tv)
	private TextView car_type_tv;
	@ViewInject(value = R.id.car_size_tv)
	private TextView car_size_tv;

	@ViewInject(value = R.id.driving_license_img)
	private ImageView driving_license_img;

	@ViewInject(value = R.id.upload_img_tv)
	private TextView upload_img_tv;

	@ViewInject(value = R.id.approval_status_tv)
	private TextView approval_status_tv;
	@ViewInject(value = R.id.reject_reason_tv)
	private TextView reject_reason_tv;

	@ViewInject(value = R.id.complete_btn)
	private Button complete_btn;

	private Context context;
	private CommonWaitDialog waitDialog;
	private AlertDialog.Builder alertDialog;

	private Dict_CarType_Sheet carType;
	private Dict_CarSize_Sheet carSize;
	private MDict_Company_Sheet companyBean;

	private static String picFileFullName;
	private Bitmap drivingPicture = null;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public static AppRegData_Driver driverDataBean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		x.image().clearMemCache();
		x.image().clearCacheFiles();

		context = this;
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		this.titlt_textView.setText(R.string.driver_info_manage);
		phone_et.setText(MyApplication.getInstatnce().getUserTel());
		waitDialog.show();
		CallBackManager.getInstance().getParkCallBackManager().addCallBack(this);
		MyApplication.getInstatnce().getBasicDataBuffer().setLoadBasicDataStatusCallBack(this);
		CarCodeRegisterAddressActivity.CarCodeRegisterAddressCallBack = this;

		List<String> basicDataRequestList = new ArrayList<>();
		basicDataRequestList.add(BasicDataBuffer.CompanyDic_Extra);
		basicDataRequestList.add(BasicDataBuffer.CarTypeDic_Extra);
		basicDataRequestList.add(BasicDataBuffer.CarSizeDic_Extra);
		MyApplication.getInstatnce().getBasicDataBuffer().setBasicDataRequestList(basicDataRequestList);
		MyApplication.getInstatnce().getBasicDataBuffer().loadBasicData();

		car_id_code_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s)) {
					if (s.length() < 17) {
						return;
					}

					if (MyApplication.getInstatnce().getUserTypeOid() != null
							&& (MyApplication.getInstatnce().getUserTypeOid().equals("A"))) {
						waitDialog.show();
						loadCarByCodeData();
					}
				}
			}
		});

		driverDataBean = new AppRegData_Driver();
		if (MyApplication.getInstatnce().getUserTypeOid() != null
				&& MyApplication.getInstatnce().getUserTypeOid().equals("A")) {
			loadUserInfo();
		}
		// loadDate();
	}

	private void loadCarByCodeData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
				requestData.setDataValue(car_id_code_et.getText().toString().trim());

				RequestParams params = new RequestParams(Constants.Get_CarByCode_Url);
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
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

						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = Constants.errorMsg;
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
							if (resultBase.isSuccess()) {
								MDict_CarCode_Sheet bean = (MDict_CarCode_Sheet) JsonUtils.parseData(str,
										MDict_CarCode_Sheet.class);
								if (bean != null) {
									Message msg = Message.obtain();
									msg.what = Constants.Result_Success;
									msg.obj = bean;
									myHandler.sendMessage(msg);
								}
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private void loadUserInfo() {
		new Thread(new Runnable() {
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());

				RequestParams params = new RequestParams(Constants.Get_UserInfo_URL);
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
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

						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = Constants.errorMsg;
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
							if (resultBase.isSuccess()) {
								AppRegData_Driver driverData = (AppRegData_Driver) JsonUtils.parseData(str,
										AppRegData_Driver.class);
								if (driverData != null) {
									MyApplication.getInstatnce().setDriverData(driverData);
									myHandler.sendEmptyMessage(Constants.Refresh);
								} else {
									myHandler.sendEmptyMessage(Constants.Request_NoData);
								}
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
								msg.obj = Constants.errorMsg;
								myHandler.sendMessage(msg);
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		if(drivingPicture!=null){
			drivingPicture.recycle();
			drivingPicture=null;
		}
		CallBackManager.getInstance().getParkCallBackManager().removeCallBack(this);
		MyApplication.getInstatnce().getBasicDataBuffer().setLoadBasicDataStatusCallBack(null);
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if (TextUtils.isEmpty(driverDataBean.DriversLicense_Img) == false) {
					driverDataBean.IsModifyImage = "1";
				}

				setImageView(picFileFullName);
			} else if (resultCode == RESULT_CANCELED) { // 用户取消了图像捕获
			} else { // 图像捕获失败，提示用户
				LogUtil.d("拍照失败！");
			}
		} else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (data == null) {
				return;
			}
			showNativeImage(data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("ResourceAsColor")
	private void setViewValue() {
		if (MyApplication.getInstatnce().getDriverData() != null) {
			driverDataBean = MyApplication.getInstatnce().getDriverData();
			name_dt.setText(driverDataBean.Name);
			phone_et.setText(driverDataBean.Tel);
			id_number_et.setText(driverDataBean.IdentificationCard);
			bank_number_et.setText(driverDataBean.BankCode);
			driving_type_tv.setText(driverDataBean.DriversType);
			driving_age_et.setText(String.valueOf(driverDataBean.DrivingExperience));
			park_tv.setText(driverDataBean.Company_Oid_Name);

			car_code_register_address_et.setText(driverDataBean.CarCode.substring(0, 1));
			car_code_et.setText(driverDataBean.CarCode.substring(1,driverDataBean.CarCode.length()));
			car_id_code_et.setText(driverDataBean.VehicleID_Code);
			car_type_tv.setText(driverDataBean.CarType);
			car_size_tv.setText(String.valueOf(driverDataBean.Size));

			// 审核状态
			String userStatus = driverDataBean.UserStatus;
			if (TextUtils.isEmpty(userStatus) == false) {
				if (userStatus.equals("0")) {
					approval_status_tv.setText("审核通过");
					approval_status_tv.setTextColor(R.color.green);
				} else if (userStatus.equals("1")) {
					approval_status_tv.setText("未审核");
					approval_status_tv.setTextColor(R.color.yellow);
				} else if (userStatus.equals("2")) {
					approval_status_tv.setText("未通过");
					approval_status_tv.setTextColor(R.color.red);
				}
			}

			if (driverDataBean.Rejected_Content != null) {
				reject_reason_tv.setText(driverDataBean.Rejected_Content);
			}

			String drivingPictureStr = driverDataBean.DriversLicense_Img;
			if (TextUtils.isEmpty(drivingPictureStr) == false) {
				// ImageOptions imageOptions = new
				// ImageOptions.Builder().setCrop(true)
				// .setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.drawable.ic_launcher)//
				// 加载中默认显示图片
				// .setFailureDrawableId(R.drawable.ic_launcher)// 加载失败后默认显示图片
				// .build();
				// x.image().bind(driving_license_img, drivingPictureStr,
				// imageOptions);
				// x.image().bind(driving_license_img, drivingPictureStr);
				imageLoader.displayImage(drivingPictureStr, driving_license_img);
			}
		} else {
			phone_et.setText(MyApplication.getInstatnce().getUserTel());
		}
	}

	@Event(value = { R.id.back_linearLayout, R.id.driving_type_tv, R.id.park_tv, R.id.car_type_tv, R.id.car_size_tv,
			R.id.upload_img_tv, R.id.complete_btn,
			R.id.car_code_register_address_et }, type = View.OnClickListener.class)
	private void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.driving_type_tv:
			alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("选择驾照类型");
			alertDialog.setItems((String[]) MyApplication.getInstatnce().getBasicDataBuffer()
					.getDrivingLicenseTypeList().toArray(new String[0]), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							driving_type_tv.setText(MyApplication.getInstatnce().getBasicDataBuffer()
									.getDrivingLicenseTypeList().get(which));
						}
					});
			alertDialog.create().show();
			break;
		case R.id.park_tv:
			startActivity(new Intent(this, C_ShowMDictListActivity.class));
			break;
		case R.id.car_type_tv:
			final ArrayList<String> carTypeStringList = new ArrayList<String>();

			final List<Dict_CarType_Sheet> carTypeList = MyApplication.getInstatnce().getBasicDataBuffer()
					.getCarTypeList();
			if (carTypeList != null && carTypeList.size() > 0) {
				for (int i = 0; i < carTypeList.size(); i++) {
					carTypeStringList.add(carTypeList.get(i).CarType);
				}
			}

			alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("选择车型");
			alertDialog.setItems((String[]) carTypeStringList.toArray(new String[0]),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							car_type_tv.setText(carTypeStringList.get(which));
							carType = carTypeList.get(which);
						}
					});
			alertDialog.create().show();
			break;
		case R.id.car_size_tv:
			final ArrayList<String> carSizeStringList = new ArrayList<String>();

			final List<Dict_CarSize_Sheet> carSizeList = MyApplication.getInstatnce().getBasicDataBuffer()
					.getCarSizeList();
			if (carSizeList != null && carSizeList.size() > 0) {
				for (int i = 0; i < carSizeList.size(); i++) {
					carSizeStringList.add(String.valueOf(carSizeList.get(i).CarSize));
				}
			}

			alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("选择尺寸");
			alertDialog.setItems((String[]) carSizeStringList.toArray(new String[0]),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							car_size_tv.setText(carSizeStringList.get(which));
							carSize = carSizeList.get(which);
						}
					});
			alertDialog.create().show();
			break;
		case R.id.upload_img_tv:
			alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("上传照片");
			alertDialog.setItems(new String[] { "相机拍照", "本地照片" }, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

					switch (which) {
					case 0:
						photograph(); // 相机拍照。
						break;
					case 1:
						nativePicture(); // 本地照片
						break;
					default:
						break;
					}
				}
			}).setNegativeButton("取消", null);
			alertDialog.show();
			break;
		case R.id.complete_btn:
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			if (vefiryData() == false) {
				return;
			}

			waitDialog.show();
			saveData();
			break;
		case R.id.car_code_register_address_et:
			showActivity(this, CarCodeRegisterAddressActivity.class);
			break;
		default:
			break;
		}
	}

	private boolean vefiryData() {
		if (TextUtils.isEmpty(name_dt.getText().toString().trim())) {
			Toast.makeText(context, "请输入用户名！", Toast.LENGTH_SHORT).show();
			return false;
		}

		// if (TextUtils.isEmpty(phone_et.getText().toString().trim())) {
		// Toast.makeText(context, "请输入手机号！", Toast.LENGTH_SHORT).show();
		// return false;
		// }

		// if (!ContextUtil.isMobileNO(phone_et.getText().toString().trim())) {
		// Toast.makeText(context, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
		// return false;
		// }

		if (!ContextUtil.isIDCard(id_number_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入正确的身份证号码！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(driving_type_tv.getText().toString())) {
			Toast.makeText(context, "请选择您的驾照类型！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(driving_age_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入您的驾龄！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(park_tv.getText().toString())) {
			Toast.makeText(context, "请选择车辆停靠物流区！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(bank_number_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入银行卡号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!VerifyBankCardUtil.checkBankCard(bank_number_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入正确的银行卡号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(car_code_register_address_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入车牌注册地！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(car_code_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入车牌号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (VerifyCarNumUtil.checkCarNum(car_code_register_address_et.getText().toString().trim()
				+ car_code_et.getText().toString().trim()) == false) {
			Toast.makeText(context, "请输入正确的车牌号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(car_id_code_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入车辆识别码！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (car_id_code_et.getText().toString().trim().length() != 17) {
			Toast.makeText(context, "请输入正确的车辆识别码！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(car_type_tv.getText().toString())) {
			Toast.makeText(context, "请选择车型！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(car_size_tv.getText().toString())) {
			Toast.makeText(context, "请选择尺寸！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (MyApplication.getInstatnce().getUserTypeOid() != null
				&& MyApplication.getInstatnce().getUserTypeOid().equals("D")) {
			if (drivingPicture == null) {
				Toast.makeText(context, "请上传驾驶证照片！", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		return true;
	}

	private void saveData() {
		driverDataBean.Name = name_dt.getText().toString().trim();
		driverDataBean.Tel = phone_et.getText().toString().trim();
		driverDataBean.IdentificationCard = id_number_et.getText().toString().trim();
		driverDataBean.BankCode = bank_number_et.getText().toString().trim();
		driverDataBean.DriversType = driving_type_tv.getText().toString().trim();
		driverDataBean.DrivingExperience = Integer.parseInt(driving_age_et.getText().toString().trim());

		if (companyBean != null) {
			driverDataBean.Company_Oid = companyBean.Company_Oid;
			driverDataBean.Company_Oid_Name = companyBean.Company_Name;
		}

		driverDataBean.CarCode = car_code_register_address_et.getText().toString()
				+ car_code_et.getText().toString().trim();
		driverDataBean.VehicleID_Code = car_id_code_et.getText().toString().trim();

		if (carType != null) {
			driverDataBean.CarType_Oid = carType.CarType_Oid;
			driverDataBean.CarType = carType.CarType;
		}

		if (carSize != null) {
			driverDataBean.Size = Double.valueOf(carSize.CarSize);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = null;
				if (MyApplication.getInstatnce().getUserTypeOid() != null
						&& MyApplication.getInstatnce().getUserTypeOid().equals("A")) {
					params = new RequestParams(Constants.Modify_UserInfo_URL);

					if (TextUtils.isEmpty(driverDataBean.IsModifyImage) == false
							&& driverDataBean.IsModifyImage.equals("1")) {

						String drivingPicBase64 = getDrivingPicBase64();
						driverDataBean.DriversLicense_Img = drivingPicBase64;
						driverDataBean.DriversLicense_ImgType = "png";
					} else {
						driverDataBean.IsModifyImage = "0";
					}
				} else if (MyApplication.getInstatnce().getUserTypeOid() != null
						&& MyApplication.getInstatnce().getUserTypeOid().equals("D")) {
					params = new RequestParams(Constants.Add_UserInfo_URL);

					String drivingPicBase64 = getDrivingPicBase64();
					driverDataBean.IsModifyImage = "0";
					driverDataBean.DriversLicense_Img = drivingPicBase64;
					driverDataBean.DriversLicense_ImgType = "png";
				}

				try {

					DataRequestData requestData = new DataRequestData();
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setCompany_Oid(driverDataBean.Company_Oid);
					requestData.setUserType_Oid("A");
					requestData.setDataValue(JsonUtils.toJsonData(driverDataBean));

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
						msg.obj = Constants.errorMsg;
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
							DataResultBase bean = JsonUtils.parseDataResultBase(str);
							if (bean.isSuccess()) {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Success;
								msg.obj = bean.getDataValue() == null ? "" : String.valueOf(bean.getDataValue());
								myHandler.sendMessage(msg);
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
								msg.obj = bean.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private String getDrivingPicBase64() {
		Bitmap bitmap = null;
		if (drivingPicture != null) {
			bitmap = BitmapUtil.comp(drivingPicture);
		}
		String drivingPicBase64 = BitmapUtil.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 30);
		return drivingPicBase64;
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_Success:
				// 手动设置用户类型为“A”
				MyApplication.getInstatnce().setUserTypeOid("A");
				MyApplication.getInstatnce().getLoginData().setUserType_Oid("A");
				// 更新缓存。
				// driverDataBean.CarCode_Key = String.valueOf(msg.obj);
				MyApplication.getInstatnce().setDriverData(driverDataBean);

				if (TextUtils.isEmpty(String.valueOf(msg.obj)) == false) {
					try {
						DeirverRegisterResultBean bean = JsonUtils.parseData2(String.valueOf(msg.obj),
								DeirverRegisterResultBean.class);
						if (bean != null) {
							MyApplication.getInstatnce().getDriverData().CarCode = bean.CarCode;
							MyApplication.getInstatnce().getDriverData().CarCode_Key = bean.CarCode_Key;
							MyApplication.getInstatnce().getDriverData().Company_Oid = bean.Company_Oid;
							MyApplication.getInstatnce().setCompany_Oid(bean.Company_Oid);
							MyApplication.getInstatnce().getDriverData().FCompany_Oid = bean.FCompany_Oid;
							
							// 更新登录的缓存信息，供GPS传输数据使用。
							MyApplication.getInstatnce().getLoginData().setCarCode(bean.CarCode);
							MyApplication.getInstatnce().getLoginData().setCarCode_Key(bean.CarCode_Key);
							MyApplication.getInstatnce().getLoginData().setCompany_Oid(bean.Company_Oid);
							MyApplication.getInstatnce().getLoginData().setFCompany_Oid(bean.FCompany_Oid);
							
							if (TextUtils.isEmpty(bean.IsPrimaryDriver) == false && bean.IsPrimaryDriver.equals("1")) {
								MyApplication.getInstatnce().getDriverData().IsPrimaryDriver = bean.IsPrimaryDriver;
								
								MyApplication.getInstatnce().getLoginData().setIsPrimaryDriver(bean.IsPrimaryDriver);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
				ActivityStackManager.getInstance().finishActivity(RegisteredActivity.class);
				finish();
				
				break;
			case Constants.Result_Success:
				MDict_CarCode_Sheet bean = (MDict_CarCode_Sheet) msg.obj;
				car_size_tv.setText(String.valueOf(bean.Size));
				car_code_register_address_et.setText(bean.CarCode.substring(0, 1));
				car_code_et.setText(bean.CarCode.substring(1,bean.CarCode.length()));
				break;
			case Constants.Refresh:
				setViewValue();
				break;
			case Constants.Request_NoData:
				Toast.makeText(context, "请求数据异常！请联系客服！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				if (msg.obj != null) {
					Toast.makeText(context, "请求服务错误！" + msg.obj == null ? "" : String.valueOf(msg.obj),
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};

	// 打开本地相册
	private void nativePicture() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择照片"), PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	// 相机拍照
	public void photograph() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}

			File outFile = new File(outDir, System.currentTimeMillis()+"driver_license.png");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			Toast.makeText(context, "请确认是否插入SD卡！", Toast.LENGTH_SHORT).show();
		}
	}

	private void setImageView(String realPath) {
		try {
			
			drivingPicture = BitmapFactory.decodeFile(realPath);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		ImageOptions imageOptions = new ImageOptions.Builder()
        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
        .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
        .build();
		x.image().bind(driving_license_img, realPath,imageOptions);
//		imageLoader.displayImage(realPath, driving_license_img);
		// driving_license_img.setImageBitmap(drivingPicture);
	}

	/**
	 * 获取相册照片
	 */
	@SuppressWarnings("deprecation")
	private void showNativeImage(Intent data) {
		ContentResolver resolver = getContentResolver();

		Uri pictureUri = data.getData(); // 获得图片的uri
		try {
			drivingPicture = MediaStore.Images.Media.getBitmap(resolver, pictureUri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显得到bitmap图片
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(pictureUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);

		if (TextUtils.isEmpty(driverDataBean.DriversLicense_Img) == false) {
			driverDataBean.IsModifyImage = "1";
		}
		ImageOptions imageOptions = new ImageOptions.Builder()
        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
        .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
        .build();
		x.image().bind(driving_license_img, path,imageOptions);
		// driving_license_img.setImageBitmap(drivingPicture);
	}

	@Override
	public void onParkData(Object obj) {
		if (obj != null) {
			companyBean = (MDict_Company_Sheet) obj;
			park_tv.setText(companyBean.Company_Name);
		}
	}

	@Override
	public void onLoadBasicDataSuccess() {
		if (waitDialog != null) {
			waitDialog.dismiss();
		}
	}

	@Override
	public void onLoadBasicDataFail(Throwable ex) {
		Toast.makeText(context, "加载基础数据失败！请联系客服！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCarCodeRegisterAddress(Object object) {
		if (object != null) {
			car_code_register_address_et.setText(String.valueOf(object));
		}
	}
	
	
}
