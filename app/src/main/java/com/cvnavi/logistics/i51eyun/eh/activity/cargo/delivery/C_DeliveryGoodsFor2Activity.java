package com.cvnavi.logistics.i51eyun.eh.activity.cargo.delivery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MCollect;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MDict_FavoriteContacts_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsName_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_PackageType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Consignor;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.AreaBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.CityBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.ProvinceBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.CarDataCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.ConsigneeDataCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CarGoMainActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CarManageActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CommonConsigneeManagementActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_FindCarActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.ToggleButton.ToggleButton;
import com.cvnavi.logistics.i51eyun.eh.widget.ToggleButton.ToggleButton.OnToggleChanged;
import com.cvnavi.logistics.i51eyun.eh.widget.datetimedialog.DateTimePickerDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.datetimedialog.DateTimePickerDialog.OnDateTimeSetListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:07:40
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(value = R.layout.c_delivery_goods2)
public class C_DeliveryGoodsFor2Activity extends BaseActivity implements
		ConsigneeDataCallBack, RegionDataCallBack2, CarDataCallBack,
		OnClickListener {

	@ViewInject(value = R.id.shouhuo_name_et)
	private EditText shouhuo_name_et;
	@ViewInject(value = R.id.shouhuo_contacts_tv)
	private TextView shouhuo_contacts_tv;
	@ViewInject(value = R.id.shouhuo_phone_et)
	private EditText shouhuo_phone_et;
	@ViewInject(value = R.id.shouhuo_region_tv)
	private TextView shouhuo_region_tv;
	@ViewInject(value = R.id.shouhuo_detail_address_et)
	private EditText shouhuo_detail_address_et;
	@ViewInject(value = R.id.fahuo_name_et)
	private EditText fahuo_name_et;
	@ViewInject(value = R.id.fahuo_contacts_tv)
	private TextView fahuo_contacts_tv;
	@ViewInject(value = R.id.fahuo_phone_et)
	private EditText fahuo_phone_et;
	@ViewInject(value = R.id.fahuo_region_tv)
	private TextView fahuo_region_tv;
	@ViewInject(value = R.id.fahuo_detail_address_et)
	private EditText fahuo_detail_address_et;

	@ViewInject(value = R.id.goods_name_et)
	private EditText goods_name_et;

	@ViewInject(value = R.id.goods_type_tv)
	private TextView goods_type_tv;
	@ViewInject(value = R.id.goods_type_ll)
	private LinearLayout goods_type_ll;

	@ViewInject(value = R.id.regulation_layout)
	private LinearLayout regulation_layout;
	@ViewInject(value = R.id.irregularity_layout)
	private LinearLayout irregularity_layout;
	@ViewInject(value = R.id.regulation_iv)
	private TextView regulation_iv;
	@ViewInject(value = R.id.irregularity_iv)
	private TextView irregularity_iv;

	@ViewInject(value = R.id.goods_weight_et)
	private EditText goods_weight_et;
	@ViewInject(value = R.id.goods_capacity_et)
	private EditText goods_capacity_et;
	@ViewInject(value = R.id.number_et)
	private EditText number_et;
	@ViewInject(value = R.id.packaging_type_et)
	private EditText packaging_type_et;
	@ViewInject(value = R.id.transportation_costs_et)
	private EditText transportation_costs_et;
	@ViewInject(value = R.id.strong_premium_et)
	private EditText strong_premium_et;
	@ViewInject(value = R.id.total_fee_tv)
	private TextView total_fee_tv;

	@ViewInject(value = R.id.delivery_time_tv)
	private TextView delivery_time_tv;
	@ViewInject(value = R.id.hope_time_tv)
	private TextView hope_time_tv;
	@ViewInject(value = R.id.aging_tv)
	private TextView aging_tv;

	@ViewInject(value = R.id.specified_car_et)
	private EditText specified_car_et;
	@ViewInject(value = R.id.specified_car_tv)
	private TextView specified_car_tv;
	@ViewInject(value = R.id.found_car_tv)
	private TextView found_car_tv;

	// @ViewInject(value = R.id.tihuo_img_layout)
	// private LinearLayout tihuo_img_layout;
	// @ViewInject(value = R.id.jiaohuo_img_layout)
	// private LinearLayout jiaohuo_img_layout;
	// @ViewInject(value = R.id.tihuo_img_iv)
	// private ImageView tihuo_img_iv;
	// @ViewInject(value = R.id.jiaohuo_img_iv)
	// private ImageView jiaohuo_img_iv;

	@ViewInject(value = R.id.save_btn)
	private Button save_btn;

	@ViewInject(R.id.tihuo_TB)
	private ToggleButton tihuo_TB;

	@ViewInject(R.id.jiaohuo_TB)
	private ToggleButton jiaohuo_TB;

	private CommonWaitDialog waitDialog;
	private Builder alertDialog;

	private MCar carBean;

	private RegionBean fahuoRegionBean;
	private RegionBean shouhuoRegionBean;

	private boolean isCheck_ShouhuoRegion = false;
	private boolean isCheck_FahuoRegion = false;

	private boolean isCheck_ShouhuoContacts = false;
	private boolean isCheck_FahuoContacts = false;

	private Dict_GoodsType_Sheet goodsTypeBean;
	private Dict_PackageType_Sheet PackageTypeSheetBean;
	private Dict_GoodsName_Sheet goodsNameBean;

	// private Dict_GoodsShape_Sheet goodsShapeBean;
	private boolean isCheck_Regulation = true;
	private boolean isCheck_Irregularity = false;

	private List<Dict_GoodsType_Sheet> goodsTypeList;
	private List<String> goodsTypeStringList;

	private List<String> packageTypeListStringList;
	private List<Dict_PackageType_Sheet> packageTypeList;

	private List<String> goodsNameStringList;
	private List<Dict_GoodsName_Sheet> goodsNameList;

	private WaitingOrderBaseInfo waiOrderBaseInfo;

	private boolean isCheck_TihuoImg = true;
	private boolean isCheck_JiaohuoImg = true;

	private String DriverGoodsWeight = MyApplication.getInstatnce()
			.getBasicDataBuffer().getSquareWeightRatio();

	private String DriverGoodsVolume = "1";

	// 重量（默认0），方量（默认0），件数（默认1），运输费用（默认0），强保费（默认0）后续加上文本监听。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isCheck_TihuoImg = true;
		isCheck_JiaohuoImg = true;

		this.titlt_textView.setText("我要发货");
		CallBackManager.getInstance().getCarCallBackManager().addCallBack(this);

		initView();

		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		fahuoRegionBean = new RegionBean();
		fahuoRegionBean.provinceBean = new ProvinceBean();
		fahuoRegionBean.cityBean = new CityBean();
		fahuoRegionBean.areaBean = new AreaBean();

		shouhuoRegionBean = new RegionBean();
		shouhuoRegionBean.provinceBean = new ProvinceBean();
		shouhuoRegionBean.cityBean = new CityBean();
		shouhuoRegionBean.areaBean = new AreaBean();

		goodsTypeBean = new Dict_GoodsType_Sheet();
		PackageTypeSheetBean = new Dict_PackageType_Sheet();
		goodsNameBean = new Dict_GoodsName_Sheet();
		// goodsShapeBean = new Dict_GoodsShape_Sheet();

		goodsTypeList = new ArrayList<Dict_GoodsType_Sheet>();
		goodsTypeStringList = new ArrayList<String>();

		packageTypeList = new ArrayList<Dict_PackageType_Sheet>();
		packageTypeListStringList = new ArrayList<String>();

		goodsNameList = new ArrayList<Dict_GoodsName_Sheet>();
		goodsNameStringList = new ArrayList<String>();

		MyApplication.getInstatnce().getOrderBaseBuffer()
				.setOrderBase(new WaitingOrderBaseInfo());

		CallBackManager.getInstance().getConsigneeCallBackManager()
				.addCallBack(this);
		CallBackManager.getInstance().getRegionCallBackManager2()
				.addCallBack(this);
		CallBackManager.getInstance().getCarCallBackManager().addCallBack(this);
		initData();// 初始化 数据

		carBean = (MCar) getIntent().getSerializableExtra(
				Constants.CarDetail_Info);
		if (carBean != null) {
			specified_car_et.setText(carBean.CarCode);
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CarCode = carBean.CarCode;
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CarCode_Key = carBean.CarCode_Key;
		} else {
			carBean = new MCar();
		}

		waiOrderBaseInfo = (WaitingOrderBaseInfo) getIntent()
				.getSerializableExtra(Constants.OrderInfo);
		if (waiOrderBaseInfo != null) {
			waitDialog.show();
			loadOrderInfo();
		} else {
			displayDefaultData();
		}
	}

	private void initView() {
		ContextUtil.money(goods_weight_et);
		ContextUtil.money(goods_capacity_et);

		tihuo_TB.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				if (on) {
					isCheck_TihuoImg = true;
				} else {
					isCheck_TihuoImg = false;
				}
			}
		});
		jiaohuo_TB.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				if (on) {
					isCheck_JiaohuoImg = true;
				} else {
					isCheck_JiaohuoImg = false;
				}
			}
		});

		// ContextUtil.money(transportation_costs_et);

		transportation_costs_et.addTextChangedListener(new TextWatcher() {
			private int count_decimal_points_ = 0; // 标识当前是不是已经有小数点了
			private int selection_start_; // 监听光标的位置
			private StringBuffer str_buf_; // 缓存当前的string，用以修改内容

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if (s.toString().contains(".")) {
					count_decimal_points_ = 1;
				} else {
					count_decimal_points_ = 0; // 因为可能存在如果是删除的话，把小数点删除的情况
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				str_buf_ = new StringBuffer(s.toString().trim());
				// 先判断输入的第一位不能是小数点
				if (before == 0 && s.length() == 1 && s.charAt(start) == '.') {
					transportation_costs_et.setText("");
				} else if (before == 0 && count_decimal_points_ == 1) {
					// 在判断如果当前是增加，并且已经有小数点了，就要判断输入是否合法；如果是减少不做任何判断
					// 注意在if语句中都是在else体内调用了设置光标监听位的方法，因为在调用setText之后会出现嵌套的情况
					// 非合法的输入包括： 1. 输入的依旧是小数点，2.小数点后位数已经达到两位了
					if (s.charAt(start) == '.'
							|| (start - str_buf_.indexOf(".") > 2)) {
						str_buf_.deleteCharAt(start);
						transportation_costs_et.setText(str_buf_);
					} else {
						selection_start_ = str_buf_.length(); // 设置光标的位置为结尾
					}
				} else {
					selection_start_ = str_buf_.length(); // 设置光标的位置为结尾
				}
				if (!TextUtils.isEmpty(transportation_costs_et.getText())) {
					total_fee_tv.setText(String.valueOf(Double
							.parseDouble(transportation_costs_et.getText()
									.toString())
							+ Double.parseDouble(TextUtils
									.isEmpty(strong_premium_et.getText()
											.toString()) == true ? "0"
									: strong_premium_et.getText().toString())));
				} else {
					total_fee_tv.setText(String.valueOf(+Double
							.parseDouble(TextUtils.isEmpty(strong_premium_et
									.getText().toString()) == true ? "0"
									: strong_premium_et.getText().toString())));
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				transportation_costs_et.setSelection(selection_start_);
				if (s != null) {
					try {
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// 计算方量
		goods_weight_et
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {

						} else {
							if (TextUtils.isEmpty(goods_capacity_et.getText())) {
								String volume = ContextUtil.getVolume(
										DriverGoodsWeight, goods_weight_et
												.getText().toString(),
										DriverGoodsVolume);
								goods_capacity_et.setText(volume);

							}
						}
					}
				});

		// 计算重量
		goods_capacity_et
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {

						} else {
							if (TextUtils.isEmpty(goods_weight_et.getText())) {
								String volume = ContextUtil.getWeight(
										DriverGoodsVolume, goods_capacity_et
												.getText().toString(),
										DriverGoodsWeight);
								goods_weight_et.setText(volume);
							}
						}
					}
				});
		// transportation_costs_et.setOnFocusChangeListener(new
		// View.OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		//
		// if(hasFocus){
		//
		// }else{
		// if(!TextUtils.isEmpty(transportation_costs_et.getText())){
		// total_fee_tv.setText(String.valueOf(Double.parseDouble(transportation_costs_et.getText().toString())
		// + Double.parseDouble(TextUtils
		// .isEmpty(strong_premium_et.getText().toString()) == true ? "0"
		// : strong_premium_et.getText().toString())));
		// }
		// }
		//
		// }
		// });

		// strong_premium_et.addTextChangedListener(new TextWatcher() {
		// @Override
		// public void onTextChanged(CharSequence str, int start, int before,
		// int count) {
		// if (TextUtils.isEmpty(str)) {
		// return;
		// }
		//
		// total_fee_tv.setText(String.valueOf(Double.parseDouble(str
		// .toString())
		// + Double.parseDouble(TextUtils
		// .isEmpty(transportation_costs_et.getText()
		// .toString()) == true ? "0"
		// : transportation_costs_et.getText().toString())));
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// }
		//
		// @Override
		// public void afterTextChanged(Editable editable) {
		// }
		// });
	}

	@Override
	protected void onDestroy() {
		CallBackManager.getInstance().getConsigneeCallBackManager()
				.removeCallBack(this);
		CallBackManager.getInstance().getRegionCallBackManager2()
				.removeCallBack(this);
		CallBackManager.getInstance().getCarCallBackManager()
				.removeCallBack(this);
		super.onDestroy();
	}

	@SuppressLint("ResourceAsColor")
	@Event(value = { R.id.back_linearLayout, R.id.shouhuo_contacts_tv,
			R.id.fahuo_contacts_tv, R.id.shouhuo_region_tv,
			R.id.fahuo_region_tv, R.id.regulation_layout,
			R.id.irregularity_layout, R.id.goods_type_ll,
			R.id.delivery_time_tv, R.id.hope_time_tv, R.id.specified_car_tv,
			R.id.found_car_tv, R.id.tihuo_img_layout, R.id.jiaohuo_img_layout,
			R.id.save_btn, R.id.packaging_type_et, R.id.goods_name_et }, type = View.OnClickListener.class)
	private void myOnClick(View view) {
		Intent intent = null;

		switch (view.getId()) {
		case R.id.back_linearLayout:
			this.finish();
			break;
		case R.id.shouhuo_contacts_tv:
			isCheck_ShouhuoContacts = true;
			isCheck_FahuoContacts = false;

			intent = new Intent(this, C_CommonConsigneeManagementActivity.class);
			intent.putExtra(Constants.commonConsignee,
					Constants.commonConsignee);
			startActivity(intent);
			break;
		case R.id.fahuo_contacts_tv:
			isCheck_FahuoContacts = true;
			isCheck_ShouhuoContacts = false;

			intent = new Intent(this, C_CommonConsigneeManagementActivity.class);
			intent.putExtra(Constants.commonConsignee,
					Constants.commonConsignee);
			startActivity(intent);
			break;
		case R.id.shouhuo_region_tv:
			isCheck_ShouhuoRegion = true;
			isCheck_FahuoRegion = false;

			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.ISPCA, Constants.ISPCA);
			startActivity(intent);
			break;
		case R.id.fahuo_region_tv:
			isCheck_ShouhuoRegion = false;
			isCheck_FahuoRegion = true;

			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.ISPCA, Constants.ISPCA);
			startActivity(intent);
			break;
		case R.id.goods_type_ll:
			choiceGoodsType();
			break;
		case R.id.packaging_type_et:
			choicepackagingType();
			break;
		case R.id.goods_name_et:
			choiceGoodsName();
			break;
		case R.id.regulation_layout:
			isCheck_Irregularity = false;
			irregularity_iv.setBackgroundResource(R.drawable.pretermit);
			if (isCheck_Regulation) {
				// isCheck_Regulation = false;
				// regulation_iv.setBackgroundResource(R.drawable.pretermit);
			} else {
				isCheck_Regulation = true;
				regulation_iv.setBackgroundResource(R.drawable.select);
			}
			break;
		case R.id.irregularity_layout:
			isCheck_Regulation = false;
			regulation_iv.setBackgroundResource(R.drawable.pretermit);
			if (isCheck_Irregularity) {
				// isCheck_Irregularity = false;
				// irregularity_iv.setBackgroundResource(R.drawable.pretermit);
			} else {
				isCheck_Irregularity = true;
				irregularity_iv.setBackgroundResource(R.drawable.select);
			}
			break;
		case R.id.delivery_time_tv:
			deliveryTimeShowDialog();
			break;
		case R.id.hope_time_tv:
			hopeTimeShowDialog();
			break;
		case R.id.specified_car_tv:
			intent = new Intent();
			intent.setClass(this, C_CarManageActivity.class);
			intent.putExtra(Constants.DeliveryGoods_FindCar,
					Constants.DeliveryGoods_FindCar);
			showActivity(this, intent);
			break;
		case R.id.found_car_tv:
			intent = new Intent();
			intent.setClass(this, C_FindCarActivity.class);
			intent.putExtra(Constants.DeliveryGoods_FindCar,
					Constants.DeliveryGoods_FindCar);
			showActivity(this, intent);
			break;
		// case R.id.tihuo_img_layout:
		// if (isCheck_TihuoImg) {
		// isCheck_TihuoImg = false;
		// tihuo_img_iv.setBackgroundResource(R.drawable.seckgey);
		// } else {
		// isCheck_TihuoImg = true;
		// tihuo_img_iv.setBackgroundResource(R.drawable.seck);
		// }
		// break;
		// case R.id.jiaohuo_img_layout:
		// if (isCheck_JiaohuoImg) {
		// isCheck_JiaohuoImg = false;
		// jiaohuo_img_iv.setBackgroundResource(R.drawable.seckgey);
		// } else {
		// isCheck_JiaohuoImg = true;
		// jiaohuo_img_iv.setBackgroundResource(R.drawable.seck);
		// }
		// break;
		case R.id.save_btn:
			if (verifyData() == false) {
				return;
			}

			Builder builder = new Builder(this);
			builder.setTitle("提示");
			builder.setMessage("是否确认保存?");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					waitDialog.show();
					saveOrder();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create().show();

			break;
		default:
			break;
		}
	}

	private boolean verifyData() {
		if (TextUtils.isEmpty(shouhuo_name_et.getText().toString())) {
			Toast.makeText(this, "收货人不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(shouhuo_phone_et.getText().toString())) {
			Toast.makeText(this, "收货人手机号不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!ContextUtil.isMobileNO(shouhuo_phone_et.getText().toString())) {
			Toast.makeText(this, "请输入正确收货人手机号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(shouhuo_region_tv.getText())) {
			Toast.makeText(this, "收货地址不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		// if
		// (TextUtils.isEmpty(shouhuoDetailAddressEditText.getText().toString()))
		// {
		// Toast.makeText(this, "收货人详细地址不能为空！", Toast.LENGTH_SHORT).show();
		// return false;
		// }

		if (TextUtils.isEmpty(fahuo_name_et.getText().toString())) {
			Toast.makeText(this, "发货人不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(fahuo_phone_et.getText().toString())) {
			Toast.makeText(this, "发货人手机号不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!ContextUtil.isMobileNO(fahuo_phone_et.getText().toString())) {
			Toast.makeText(this, "请输入正确发货人手机号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(fahuo_region_tv.getText())) {
			Toast.makeText(this, "发货地址不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		// if
		// (TextUtils.isEmpty(fahuoDetailAddressEditText.getText().toString()))
		// {
		// Toast.makeText(this, "发货人详细地址不能为空！", Toast.LENGTH_SHORT).show();
		// return false;
		// }

		// if (goods_type_tv.getText().toString().equals("请选择物品类型")) {
		// Toast.makeText(this, "请选择物品类型！", Toast.LENGTH_SHORT).show();
		// return false;
		// }
		 if (TextUtils.isEmpty(goods_name_et.getText())) {
			 Toast.makeText(this, "请选择物品名称！", Toast.LENGTH_SHORT).show();
		 return false;
		 }

		if (isCheck_Regulation == false && isCheck_Irregularity == false) {
			Toast.makeText(this, "请选择物品形状！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(goods_weight_et.getText())
				&& TextUtils.isEmpty(goods_capacity_et.getText())) {
			Toast.makeText(this, "请输入物品重量/方量！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Double.valueOf(String.valueOf(goods_weight_et.getText())) == 0d
				&& Double.valueOf(String.valueOf(goods_capacity_et.getText())) == 0d) {
			Toast.makeText(this, "请输入物品重量/方量！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (ContextUtil.CheckNumber(goods_weight_et.getText().toString(), 10) == false) {
			Toast.makeText(this, "请重新输入重量！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (ContextUtil.CheckNumber(goods_capacity_et.getText().toString(), 10) == false) {
			Toast.makeText(this, "请重新输入方量！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(number_et.getText())) {
			Toast.makeText(this, "请输入件数！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(transportation_costs_et.getText())) {
			Toast.makeText(this, "请输入运输费用！", Toast.LENGTH_SHORT).show();
			return false;
		}

		// if (TextUtils.isEmpty(strongPremiumEditText.getText())) {
		// Toast.makeText(this, "请输入强保费！", Toast.LENGTH_SHORT).show();
		// return false;
		// }
		if (ContextUtil.CheckNumber(strong_premium_et.getText().toString(), 10) == false) {
			Toast.makeText(this, "请重新输入强保费！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!TextUtils.isEmpty(delivery_time_tv.getText())) {

			String nowDate = ContextUtil.getStringDate(System
					.currentTimeMillis());
			long distanceTimehour = ContextUtil.getDistanceTimehour(nowDate,
					delivery_time_tv.getText().toString());
			if (distanceTimehour == -1) {
				Toast.makeText(this, "提货时间必须大于当前时间！", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			if (distanceTimehour < 1) {
				Toast.makeText(this, "提货时间必须大于当前时间两小时！", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			return true;
		}
		if (ContextUtil.compareDate(delivery_time_tv.getText().toString(),
				hope_time_tv.getText().toString()) > 0) {
			Toast.makeText(this, "提货时间不能大于期望收货时间！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!TextUtils.isEmpty(hope_time_tv.getText())) {
			String nowDate = ContextUtil.getStringDate(System
					.currentTimeMillis());
			long distanceTimehour = ContextUtil.getDistanceTimehour(nowDate,
					hope_time_tv.getText().toString());
			if (distanceTimehour == -1) {
				Toast.makeText(this, "交货时间必须大于当前时间！", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			if (distanceTimehour < 3) {
				Toast.makeText(this, "交货时间必须大于当前时间4小时！", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			return true;
		}

		// if (TextUtils.isEmpty(agingEditText.getText())) {
		// Toast.makeText(this, "时效不能为空！", Toast.LENGTH_SHORT).show();
		// return;
		// }

		if (TextUtils.isEmpty(aging_tv.getText()) == false
				&& Integer.valueOf(aging_tv.getText().toString()) < 0) {
			Toast.makeText(this, "时效不能小于0！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void initData() {
		goodsTypeList.addAll(MyApplication.getInstatnce().getBasicDataBuffer()
				.getGoodTypeList());
		packageTypeList.addAll(MyApplication.getInstatnce()
				.getBasicDataBuffer().getPackageTypeList());
		goodsNameList.addAll(MyApplication.getInstatnce().getBasicDataBuffer()
				.getGoodsNameList());
		for (int i = 0; i < goodsTypeList.size(); i++) {
			goodsTypeStringList.add(goodsTypeList.get(i).GoodsType);
		}
		for (int i = 0; i < packageTypeList.size(); i++) {
			packageTypeListStringList.add(packageTypeList.get(i).PackageType);
		}
		for (int i = 0; i < goodsNameList.size(); i++) {
			goodsNameStringList.add(goodsNameList.get(i).GoodsName);
		}

		if (goodsTypeList != null && goodsTypeList.size() > 0) {
			goodsTypeBean = goodsTypeList.get(0);
			goods_type_tv.setText(goodsTypeBean.GoodsType);
		}
		if (packageTypeList != null && packageTypeList.size() > 0) {
			PackageTypeSheetBean = packageTypeList.get(0);
			packaging_type_et.setText(PackageTypeSheetBean.PackageType);
		}
//		if (goodsNameList != null && goodsNameList.size() > 0) {
//			goodsNameBean = goodsNameList.get(goodsNameList.size()-1);
//			goods_name_et.setText(goodsNameBean.GoodsName);
//		}
	}

	private void choicepackagingType() {
		Builder builder = new Builder(this);
		builder.setTitle("请选择包装类型");
		builder.setItems(
				(String[]) packageTypeListStringList.toArray(new String[0]),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PackageTypeSheetBean = packageTypeList.get(which);
						packaging_type_et.setText(packageTypeListStringList
								.get(which));
					}
				});
		builder.create().show();
	}

	private void choiceGoodsName() {
		Builder builder = new Builder(this);
		builder.setTitle("请选择物品名称");
		builder.setItems((String[]) goodsNameStringList.toArray(new String[0]),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						goodsNameBean = goodsNameList.get(which);
						goods_name_et.setText(goodsNameStringList.get(which));
					}
				});
		builder.create().show();
	}

	private void choiceGoodsType() {
		Builder builder = new Builder(this);
		builder.setTitle("请选择物品类型");
		builder.setItems((String[]) goodsTypeStringList.toArray(new String[0]),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						goodsTypeBean = goodsTypeList.get(which);
						goods_type_tv.setText(goodsTypeStringList.get(which));
					}
				});
		builder.create().show();
	}

	private void loadOrderInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.OrderInfo_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Order_Key = waiOrderBaseInfo.Order_Key;
					orderRequestBean.Operate_Code = waiOrderBaseInfo.Operate_Code;

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
							.toJsonData(orderRequestBean));

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
							WaitingOrderBaseInfo orderBase = (WaitingOrderBaseInfo) JsonUtils
									.parseData(str, WaitingOrderBaseInfo.class);
							if (orderBase != null) {
								MyApplication.getInstatnce()
										.getOrderBaseBuffer()
										.setOrderBase(orderBase);

								Message msg = Message.obtain();
								msg.what = Constants.Request_Success_load;
								myHandler.sendMessage(msg);
							} else {
								myHandler
										.sendEmptyMessage(Constants.Request_NoData);
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

	private void displayDefaultData() {
		displayShouHuoUserInfo();
		displayFaHuoUserInfo();
		displayGoodsInfo();
		displayOtherOrderInfo();
	}

	private void displayShouHuoUserInfo() {
		String name = null, phone = null, pName = null, cName = null, aName = null, detailAddress = null;

		if (waiOrderBaseInfo == null) {
			// name = MyApplication.getInstatnce().getCargoData().Name;
			// phone = MyApplication.getInstatnce().getCargoData().Tel;
			// pName =
			// MyApplication.getInstatnce().getCargoData().Company_Provice;
			// cName = MyApplication.getInstatnce().getCargoData().Company_City;
			// aName = MyApplication.getInstatnce().getCargoData().Company_Area;
			// detailAddress =
			// MyApplication.getInstatnce().getCargoData().Company_Address;
		} else {
			name = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Consignee_Name;
			phone = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Consignee_Tel;
			pName = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Consignee_Provice;
			cName = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Consignee_City;
			aName = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Consignee_Area;
			detailAddress = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Consignee_Address;
		}

		setShouHuoDataValue(name, phone, pName, cName, aName, detailAddress);
	}

	private void setShouHuoDataValue(String name, String phone, String pName,
			String cName, String aName, String detailAddress) {
		shouhuoRegionBean.provinceBean.PName = pName;
		shouhuoRegionBean.cityBean.CName = cName;
		shouhuoRegionBean.areaBean.AName = aName;

		shouhuo_name_et.setText(name);
		shouhuo_phone_et.setText(phone);
		shouhuo_region_tv.setText(ContextUtil.getRegion(shouhuoRegionBean));
		shouhuo_detail_address_et.setText(detailAddress);
	}

	private void displayFaHuoUserInfo() {
		String name, phone, pName, cName, aName, detailAddress;

		AppRegData_Consignor bean = MyApplication.getInstatnce()
				.getCargoData();
		if (waiOrderBaseInfo == null&&bean!=null) {
			name = bean.Name;
			phone = bean.Tel;
			pName = bean.Company_Provice;
			cName = bean.Company_City;
			aName = bean.Company_Area;
			detailAddress = bean.Company_Address;
		} else {
			name = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Shipper_Name;
			phone = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Shipper_Tel;
			pName = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Shipping_Provice;
			cName = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Shipping_City;
			aName = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Shipping_Area;
			detailAddress = MyApplication.getInstatnce().getOrderBaseBuffer()
					.getOrderBase().Shipping_Address;
		}
		setFaHuoDataValue(name, phone, pName, cName, aName, detailAddress);
	}

	private void setFaHuoDataValue(String name, String phone, String pName,
			String cName, String aName, String detailAddress) {
		fahuoRegionBean.provinceBean.PName = pName;
		fahuoRegionBean.cityBean.CName = cName;
		fahuoRegionBean.areaBean.AName = aName;

		fahuo_name_et.setText(name);
		fahuo_phone_et.setText(phone);
		fahuo_region_tv.setText(ContextUtil.getRegion(fahuoRegionBean));
		fahuo_detail_address_et.setText(detailAddress);
	}

	private void displayGoodsInfo() {
		String GoodsShape_Oid;

		// goods_name_et.setText(MyApplication.getInstatnce().getOrderBaseBuffer()
		// .getOrderBase().Goods_Name);
		if (TextUtils.isEmpty(MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().GoodsName_Oid) == false) {
			goodsNameBean = MyApplication
					.getInstatnce()
					.getBasicDataBuffer()
					.getGoodsNameKey(
							MyApplication.getInstatnce().getOrderBaseBuffer()
									.getOrderBase().GoodsName_Oid);

			if (goodsNameBean != null) {
				goods_name_et.setText(goodsNameBean.GoodsName);
			}
		}

		if (TextUtils.isEmpty(MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().GoodsType_Oid) == false) {
			goodsTypeBean = MyApplication
					.getInstatnce()
					.getBasicDataBuffer()
					.getGoodTypeByKey(
							MyApplication.getInstatnce().getOrderBaseBuffer()
									.getOrderBase().GoodsType_Oid);

			if (goodsTypeBean != null) {
				goods_type_tv.setText(goodsTypeBean.GoodsType);
			}
		}

		if (TextUtils.isEmpty(MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().PackageType_Oid) == false) {
			PackageTypeSheetBean = MyApplication
					.getInstatnce()
					.getBasicDataBuffer()
					.getPackageTypeByKey(
							MyApplication.getInstatnce().getOrderBaseBuffer()
									.getOrderBase().PackageType_Oid);

			if (PackageTypeSheetBean != null) {
				packaging_type_et.setText(PackageTypeSheetBean.PackageType);
			}
		}

		GoodsShape_Oid = MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().GoodsShape_Oid;
		if (TextUtils.isEmpty(GoodsShape_Oid) == false) {
			if (GoodsShape_Oid.equals("A")) {
				isCheck_Regulation = true;
				isCheck_Irregularity = false;

				regulation_iv.setBackgroundResource(R.drawable.select);
				irregularity_iv.setBackgroundResource(R.drawable.pretermit);
			}

			if (GoodsShape_Oid.equals("B")) {
				isCheck_Regulation = false;
				isCheck_Irregularity = true;

				regulation_iv.setBackgroundResource(R.drawable.pretermit);
				irregularity_iv.setBackgroundResource(R.drawable.select);
			}
			// goodsRegulationsTextView.setText(MyApplication.getInstatnce().getBasicDataBuffer().getGoodsShapeByKey(
			// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsShape).GoodsShape);
		}

		goods_weight_et.setText(MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().Weight);
		goods_capacity_et.setText(MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().Volume);
		number_et.setText(MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().Number);
		// packaging_type_et.setText(MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().PackageType);
		String insurance = MyApplication.getInstatnce().getBasicDataBuffer()
				.getCompulsoryInsurance();
		if (!TextUtils.isEmpty(insurance)) {
			strong_premium_et.setText(MyApplication.getInstatnce()
					.getBasicDataBuffer().getCompulsoryInsurance());
		} else {
			strong_premium_et.setText("0");
		}
		transportation_costs_et.setText(MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().OrderFee);

		total_fee_tv.setText(MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().TotalFee);
	}

	private void displayOtherOrderInfo() {
		String deliveryTime = MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().DeliveryTime;
		String receivingTime = MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().ReceivingTime;
		String estimateTotalTime = MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().EstimateTotalTime;
		String default_delivery_time = DateUtil.getNextHour(
				DateUtil.FORMAT_YMDHMS, 2);
		String default_hope_time = DateUtil.getNextHour(DateUtil.FORMAT_YMDHMS,
				4);
		String IsTHPhoto = MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().IsTHPhoto;
		String IsJHPhoto = MyApplication.getInstatnce().getOrderBaseBuffer()
				.getOrderBase().IsJHPhoto;

		if (!TextUtils.isEmpty(IsTHPhoto) && IsTHPhoto.equals("1")) {
			isCheck_TihuoImg = true;
			tihuo_TB.setToggleOn();
			// tihuo_img_iv.setBackgroundResource(R.drawable.seck);
		} else if (!TextUtils.isEmpty(IsTHPhoto) && IsTHPhoto.equals("0")) {
			isCheck_TihuoImg = false;
			tihuo_TB.setToggleOff();
			// tihuo_img_iv.setBackgroundResource(R.drawable.seckgey);
		}
		if (!TextUtils.isEmpty(IsJHPhoto) && IsJHPhoto.equals("1")) {
			isCheck_JiaohuoImg = true;
			jiaohuo_TB.setToggleOn();
			// jiaohuo_img_iv.setBackgroundResource(R.drawable.seck);
		} else if (!TextUtils.isEmpty(IsJHPhoto) && IsJHPhoto.equals("0")) {
			isCheck_JiaohuoImg = false;
			jiaohuo_TB.setToggleOff();
			// jiaohuo_img_iv.setBackgroundResource(R.drawable.seckgey);
		}
		if (TextUtils.isEmpty(deliveryTime)) {
			delivery_time_tv.setText(default_delivery_time);
		} else {
			delivery_time_tv.setText(deliveryTime);
		}
		if (TextUtils.isEmpty(receivingTime)) {
			hope_time_tv.setText(default_hope_time);
		} else {
			hope_time_tv.setText(receivingTime);
		}
		if (TextUtils.isEmpty(estimateTotalTime) == false) {
			aging_tv.setText(estimateTotalTime);
		} else {
			calcHours();
		}
		specified_car_et.setText(MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().CarCode);
		carBean.CarCode_Key=MyApplication.getInstatnce()
				.getOrderBaseBuffer().getOrderBase().CarCode_Key;
		// if
		// (TextUtils.isEmpty(MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CarCode_Key)
		// == false) {
		// carCodeKey =
		// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CarCode_Key;
		// }
	}

	private void calcHours() {
		aging_tv.setText(String.valueOf(ContextUtil.getDistanceTimes(
				delivery_time_tv.getText().toString(), hope_time_tv.getText()
						.toString())));
	}

	private void deliveryTimeShowDialog() {
		DateTimePickerDialog dialog = new DateTimePickerDialog(this,
				System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			public void OnDateTimeSet(AlertDialog dialog, long date) {

				String nowData = ContextUtil.getStringDate(System
						.currentTimeMillis());
				long distanceTimehours = ContextUtil.getDistanceTimehour(
						nowData, ContextUtil.getStringDate(date));
				if (distanceTimehours == -1) {
					delivery_time_tv.setText(DateUtil.getNextHour(
							DateUtil.FORMAT_YMDHMS, 2));
				} else if (distanceTimehours < 1) {
					delivery_time_tv.setText(DateUtil.getNextHour(
							DateUtil.FORMAT_YMDHMS, 2));
				} else {
					delivery_time_tv.setText(ContextUtil.getStringDate(date));
				}
				calcHours();
			}
		});
		dialog.show();
	}

	private void hopeTimeShowDialog() {
		DateTimePickerDialog dialog = new DateTimePickerDialog(this,
				System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			public void OnDateTimeSet(AlertDialog dialog, long date) {

				String nowData = ContextUtil.getStringDate(System
						.currentTimeMillis());
				long distanceTimehour = ContextUtil.getDistanceTimehour(
						nowData, ContextUtil.getStringDate(date));

				if (distanceTimehour == -1) {
					hope_time_tv.setText(DateUtil.getNextHour(
							DateUtil.FORMAT_YMDHMS, 4));
				} else if (distanceTimehour < 3) {
					hope_time_tv.setText(DateUtil.getNextHour(
							DateUtil.FORMAT_YMDHMS, 4));
				} else {
					hope_time_tv.setText(ContextUtil.getStringDate(date));
				}

				calcHours();
			}
		});
		dialog.show();
	}

	private void saveOrder() {
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Name = shouhuo_name_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Tel = shouhuo_phone_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Provice = shouhuoRegionBean.provinceBean.PName;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_City = shouhuoRegionBean.cityBean.CName;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Area = shouhuoRegionBean.areaBean.AName;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Address = shouhuo_detail_address_et
				.getText().toString();

		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipper_Name = fahuo_name_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipper_Tel = fahuo_phone_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_Provice = fahuoRegionBean.provinceBean.PName;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_City = fahuoRegionBean.cityBean.CName;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_Area = fahuoRegionBean.areaBean.AName;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_Address = fahuo_detail_address_et
				.getText().toString();

		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsType = goodsTypeBean.GoodsType;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsType_Oid = goodsTypeBean.GoodsType_Oid;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().PackageType_Oid = PackageTypeSheetBean.PackageType_Oid;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().PackageType = PackageTypeSheetBean.PackageType;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsName_Oid = goodsNameBean.GoodsName_Oid;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsName = goodsNameBean.GoodsName;

		// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Goods_Name
		// = goods_name_et.getText().toString();
		if (isCheck_Regulation) {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsShape_Oid = "A";
		}
		if (isCheck_Irregularity) {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsShape_Oid = "B";
		}
		// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().GoodsShape
		// = goodsShapeBean.GoodsShape_Oid;
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Weight = goods_weight_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Volume = goods_capacity_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().OrderFee = transportation_costs_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CompulsoryInsurance = strong_premium_et
				.getText().toString();

		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Number = number_et
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().TotalFee = total_fee_tv
				.getText().toString();

		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().DeliveryTime = delivery_time_tv
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().ReceivingTime = hope_time_tv
				.getText().toString();
		MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().EstimateTotalTime = aging_tv
				.getText().toString();
		if (TextUtils.isEmpty(specified_car_et.getText()) == false) {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CarCode_Key = carBean.CarCode_Key;
		} else {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().CarCode_Key = null;
		}

		if (isCheck_TihuoImg) {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().IsTHPhoto = "1";
		} else {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().IsTHPhoto = "0";
		}
		if (isCheck_JiaohuoImg) {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().IsJHPhoto = "1";
		} else {
			MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().IsJHPhoto = "0";
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUser_Key(MyApplication.getInstatnce()
						.getUserKey());
				requestData.setUserType_Oid(MyApplication.getInstatnce()
						.getUserTypeOid());
				requestData.setCompany_Oid(MyApplication.getInstatnce()
						.getCompany_Oid());
				RequestParams params;
				if (TextUtils.isEmpty(MyApplication.getInstatnce()
						.getOrderBaseBuffer().getOrderBase().Order_Key)) {
					params = new RequestParams(Constants.Add_Order_Url);
				} else {
					params = new RequestParams(Constants.ModifyOrder_Url);
				}

				try {
					String jsonStr = JsonUtils
							.toJsonData(MyApplication.getInstatnce()
									.getOrderBaseBuffer().getOrderBase());
					LogUtil.d("-->>jsonStr:" + jsonStr);

					requestData.setDataValue(jsonStr);
				} catch (JsonGenerationException e1) {
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
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
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = ex.getMessage();
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
							DataResultBase bean = JsonUtils
									.parseDataResultBase(str);
							if (bean.isSuccess()) {
								Message msg = Message.obtain();
								msg.what = Constants.Result_Success;
								myHandler.sendMessage(msg);
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Result_Fail;
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

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_Success_load:
				displayDefaultData();
				break;
			case Constants.Result_Success:
				if (TextUtils.isEmpty(MyApplication.getInstatnce()
						.getOrderBaseBuffer().getOrderBase().Order_Key)) {
					Toast.makeText(C_DeliveryGoodsFor2Activity.this, "运单发布成功！",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(C_DeliveryGoodsFor2Activity.this, "运单修改成功！",
							Toast.LENGTH_LONG).show();
				}

				CallBackManager
						.getInstance()
						.getOrderCallBackManager()
						.fireCallBack(
								MyApplication.getInstatnce()
										.getOrderBaseBuffer().getOrderBase());
				ActivityStackManager.getInstance().finishActivity(
						C_FindCarActivity.class);
				skipActivity(C_DeliveryGoodsFor2Activity.this,
						C_CarGoMainActivity.class);
				break;
			case Constants.Result_Fail:
				if (TextUtils.isEmpty(MyApplication.getInstatnce()
						.getOrderBaseBuffer().getOrderBase().Order_Key)) {
					alertDialog = new Builder(C_DeliveryGoodsFor2Activity.this);
					alertDialog.setCancelable(false);
					alertDialog.setTitle("提示");
					alertDialog.setMessage("订单发布失败！" + msg.obj == null ? ""
							: String.valueOf(msg.obj));
					alertDialog.setPositiveButton("确认", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					alertDialog.show();
				} else {
					alertDialog = new Builder(C_DeliveryGoodsFor2Activity.this);
					alertDialog.setCancelable(false);
					alertDialog.setTitle("提示");
					alertDialog.setMessage("订单修改失败！" + msg.obj == null ? ""
							: String.valueOf(msg.obj));
					alertDialog.setPositiveButton("确认", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					alertDialog.show();

				}
				break;
			case Constants.Request_Fail:
				if (msg.obj != null) {
					alertDialog = new Builder(C_DeliveryGoodsFor2Activity.this);
					alertDialog.setCancelable(false);
					alertDialog.setTitle("提示");
					alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服"
							: String.valueOf(msg.obj));
					alertDialog.setPositiveButton("确认", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					alertDialog.show();
				}
				break;
			default:
				break;
			}
		};
	};

	// private void saveShouHuoUserInfo() {
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Name
	// = shouhuo_name_et.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Tel
	// = shouhuo_phone_et.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Provice
	// = shouhuo_region_tv.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_City
	// = shouhuoCityTextView.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Area
	// = shouhuoCountyTextView
	// .getText().toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Consignee_Address
	// = shouhuo_detail_address_et
	// .getText().toString();
	// }
	//
	// private void saveFaHuoUserInfo() {
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipper_Name
	// = fahuo_name_et.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipper_Tel
	// = fahuo_phone_et.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_Provice
	// = fahuo_region_tv.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_City
	// = fahuoCityTextView.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_Area
	// = fahuoCountyTextView.getText()
	// .toString();
	// MyApplication.getInstatnce().getOrderBaseBuffer().getOrderBase().Shipping_Address
	// = fahuo_detail_address_et
	// .getText().toString();
	// }

	@Override
	public void onConsigneeData(Object obj) {
		if (obj != null) {
			MDict_FavoriteContacts_Sheet bean = (MDict_FavoriteContacts_Sheet) obj;
			if (isCheck_ShouhuoContacts) {
				setShouHuoDataValue(bean.Contacts_Name, bean.Contacts_Tel,
						bean.Contacts_Provice, bean.Contacts_City,
						bean.Contacts_Area, bean.Contacts_Address);
			}

			if (isCheck_FahuoContacts) {
				setFaHuoDataValue(bean.Contacts_Name, bean.Contacts_Tel,
						bean.Contacts_Provice, bean.Contacts_City,
						bean.Contacts_Area, bean.Contacts_Address);
			}
		}
	}

	@Override
	public void onRegionData(Object obj) {
		if (obj != null) {
			if (isCheck_ShouhuoRegion) {
				shouhuoRegionBean = (RegionBean) obj;
				shouhuo_region_tv.setText(ContextUtil
						.getRegion(shouhuoRegionBean));
			}
			if (isCheck_FahuoRegion) {
				fahuoRegionBean = (RegionBean) obj;
				fahuo_region_tv.setText(ContextUtil.getRegion(fahuoRegionBean));
			}
		}
	}

	@Override
	public void onCarData(Object object) {
		if (object != null) {
			if (object instanceof MDict_CarCode_Sheet) {
				MDict_CarCode_Sheet bean = (MDict_CarCode_Sheet) object;
				carBean.CarCode = bean.CarCode;
				carBean.CarCode_Key = bean.CarCode_Key;
				specified_car_et.setText(bean.CarCode);
			} else if (object instanceof MCollect) {
				MCollect bean = (MCollect) object;
				carBean.CarCode = bean.CarCode;
				carBean.CarCode_Key = bean.CarCode_Key;

				specified_car_et.setText(bean.CarCode);
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

}
