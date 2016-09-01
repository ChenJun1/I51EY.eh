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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Consignor;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.AreaBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.CityBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.ProvinceBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.ParkDataCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.RegisteredActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.VerifyBankCardUtil;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

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

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:08:58
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_person_info2)
public class C_PersonInfoActivity extends BaseActivity implements ParkDataCallBack, RegionDataCallBack2 {

	@ViewInject(R.id.name_edittext)
	private EditText name_edittext;
	@ViewInject(R.id.phone_edittext)
	private EditText phone_edittext;
	@ViewInject(R.id.id_number_edittext)
	private EditText id_number_edittext;

	@ViewInject(R.id.single_customer_layout)
	private LinearLayout single_customer_layout;
	@ViewInject(R.id.company_customer_layout)
	private LinearLayout company_customer_layout;
	@ViewInject(R.id.single_customer_iv)
	private TextView single_customer_iv;
	@ViewInject(R.id.company_customer_iv)
	private TextView company_customer_iv;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.company_customer_name_layout)
	private LinearLayout company_customer_name_layout;

	@ViewInject(R.id.company_name_et)
	private EditText company_name_et;
	@ViewInject(value = R.id.address_tv)
	private TextView address_tv;
	@ViewInject(R.id.detail_address_et)
	private EditText detail_address_et;
	@ViewInject(R.id.bank_number_et)
	private EditText bank_number_et;

	@ViewInject(R.id.logistics_park_tv)
	private TextView logistics_park_tv;

	@ViewInject(R.id.save_btn)
	private Button save_btn;
	
	@ViewInject(R.id.view_xian)
	private View view_xian;


	private MDict_Company_Sheet companyBean;
	private RegionBean regionBean;

	private boolean single_customer = true;
	private boolean company_customer = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CallBackManager.getInstance().getParkCallBackManager().addCallBack(this);
		CallBackManager.getInstance().getRegionCallBackManager2().addCallBack(this);
		this.titlt_textView.setText("个人信息");
		phone_edittext.setText(MyApplication.getInstatnce().getLoginData().getUser_Tel());

		regionBean = new RegionBean();
		regionBean.provinceBean = new ProvinceBean();
		regionBean.cityBean = new CityBean();
		regionBean.areaBean = new AreaBean();

		companyBean = new MDict_Company_Sheet();


		if (MyApplication.getInstatnce().getCargoData() != null
				&& MyApplication.getInstatnce().getUserTypeOid().equals("B")) {
			loadPersonInfo(MyApplication.getInstatnce().getCargoData());
		}else if(MyApplication.getInstatnce().getUserTypeOid().equals("D")){
			view_xian.setVisibility(View.GONE);
		}
	}
	

	@Override
	protected void onDestroy() {
		CallBackManager.getInstance().getParkCallBackManager().removeCallBack(this);
		CallBackManager.getInstance().getRegionCallBackManager2().removeCallBack(this);
		super.onDestroy();
	}

	@Event(value = { R.id.back_linearLayout, R.id.single_customer_layout, R.id.company_customer_layout, R.id.address_tv,
			R.id.logistics_park_tv, R.id.save_btn }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.single_customer_layout:
			company_customer = false;
			company_customer_iv.setBackgroundResource(R.drawable.pretermit);
			company_customer_name_layout.setVisibility(View.GONE);
			view_xian.setVisibility(View.GONE);
			if (single_customer) {
//				single_customer = false;
//				single_customer_iv.setBackgroundResource(R.drawable.pretermit);
			} else {
				single_customer = true;
				single_customer_iv.setBackgroundResource(R.drawable.select);
			}
			break;
		case R.id.company_customer_layout:
			single_customer = false;
			single_customer_iv.setBackgroundResource(R.drawable.pretermit);
			company_customer_name_layout.setVisibility(View.VISIBLE);
			view_xian.setVisibility(View.VISIBLE);
			if (company_customer) {
//				company_customer = false;
//				company_customer_iv.setBackgroundResource(R.drawable.pretermit);
			} else {
				company_customer = true;
				company_customer_iv.setBackgroundResource(R.drawable.select);
			}
			break;
		case R.id.address_tv:
			Intent intent=new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.ISPCA, Constants.ISPCA);
			startActivity(intent);
			break;
		case R.id.logistics_park_tv:
			showActivity(this, C_ShowMDictListActivity.class);
			break;
		case R.id.save_btn:
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			
			if (verifyData() == false) {
				return;
			}

			saveUserInfo();
			break;
		default:
			break;
		}
	}

	private boolean verifyData() {
		if (TextUtils.isEmpty(name_edittext.getText().toString())) {
			Toast.makeText(this, "请输入姓名！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(id_number_edittext.getText().toString())) {
			Toast.makeText(this, "请输入身份证号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (ContextUtil.isIDCard(id_number_edittext.getText().toString()) == false) {
			Toast.makeText(this, "请输入正确的身份证号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (single_customer == false && company_customer == false) {
			Toast.makeText(this, "请选择用户类型！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (address_tv.getText().equals("省\t市\t区")) {
			Toast.makeText(this, "请选择省市区！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(bank_number_et.getText().toString()) == false
				&& VerifyBankCardUtil.checkBankCard(bank_number_et.getText().toString()) == false) {
			Toast.makeText(this, "请输入正确的银行卡号！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(logistics_park_tv.getText())) {
			Toast.makeText(this, "请选择园区！", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(company_customer&&TextUtils.isEmpty(company_name_et.getText())){
			Toast.makeText(this, "请输入公司名称！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}


	private void loadPersonInfo(AppRegData_Consignor bean) {
		name_edittext.setText(bean.Name);
//		phone_edittext.setText(MyApplication.getInstatnce().getLoginData().getUser_Tel());
		id_number_edittext.setText(bean.IdentificationCard);

		if (!TextUtils.isEmpty(bean.User_Type) && bean.User_Type.equals("A")) {

			single_customer_iv.setBackgroundResource(R.drawable.select);
			company_customer_iv.setBackgroundResource(R.drawable.pretermit);
			company_customer_name_layout.setVisibility(View.GONE);
			view_xian.setVisibility(View.GONE);
			single_customer = true;
			company_customer = false;
		} else if (!TextUtils.isEmpty(bean.User_Type) && bean.User_Type.equals("B")) {
			view_xian.setVisibility(View.VISIBLE);
			single_customer_iv.setBackgroundResource(R.drawable.pretermit);
			company_customer_iv.setBackgroundResource(R.drawable.select);
			company_customer_name_layout.setVisibility(View.VISIBLE);
			single_customer = false;
			company_customer = true;

			company_name_et.setText(bean.Company_Name);
		}


		address_tv.setText(ContextUtil.getAddress(bean.Company_Provice, bean.Company_City, bean.Company_Area));
		detail_address_et.setText(bean.Company_Address);
		bank_number_et.setText(bean.BankCode);
		logistics_park_tv.setText(bean.Company_Oid_Name);

		regionBean.provinceBean.PName = bean.Company_Provice;
		regionBean.cityBean.CName = bean.Company_City;
		regionBean.areaBean.AName = bean.Company_Area;
		companyBean.Company_Oid = bean.Company_Oid;
		companyBean.Company_Name = bean.Company_Oid_Name;
	}


	private void saveUserInfo() {

		final AppRegData_Consignor consignorBean = new AppRegData_Consignor();
		consignorBean.Tel = MyApplication.getInstatnce().getLoginData().getUser_Tel();
		consignorBean.Name = name_edittext.getText().toString();
		consignorBean.IdentificationCard = id_number_edittext.getText().toString();
		if (single_customer) {
			consignorBean.User_Type = "A";
		}
		if (company_customer) {
			consignorBean.User_Type = "B";
			consignorBean.Company_Name = company_name_et.getText().toString();
		}
		consignorBean.Company_Provice = regionBean.provinceBean.PName;
		consignorBean.Company_City = regionBean.cityBean.CName;
		consignorBean.Company_Area = regionBean.areaBean.AName;
		consignorBean.Company_Address = detail_address_et.getText().toString();
		consignorBean.BankCode = bank_number_et.getText().toString();
		consignorBean.Company_Oid = companyBean.Company_Oid;
		consignorBean.Company_Oid_Name = companyBean.Company_Name;

		RequestParams requestParams = null;
		if (MyApplication.getInstatnce().getLoginData().getUserType_Oid().equals("D")) {
			requestParams = new RequestParams(Constants.Add_UserInfo_URL);
		} else {
			requestParams = new RequestParams(Constants.Modify_UserInfo_URL);
		}
		try {
			DataRequestData requestData = new DataRequestData();
			requestData.setUser_Key(MyApplication.getInstatnce().getUserKey().toString());
			requestData.setToken(MyApplication.getInstatnce().getToken().toString());
			requestData.setUserType_Oid("B");
			requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
			requestData.setDataValue(JsonUtils.toJsonData(consignorBean));

			requestParams.addBodyParameter(null, JsonUtils.toJsonData(requestData));
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		x.http().post(requestParams, new Callback.CommonCallback<String>() {
			@Override
			public void onCancelled(CancelledException arg0) {
				LogUtil.d("-->>onCancelled");
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				LogUtil.d("-->>onError:" + ex.getMessage());

				Message msg = Message.obtain();
				msg.what = Constants.Request_Fail;
				msg.obj=ex.getMessage();
				myHandler.sendMessage(msg);
			}

			@Override
			public void onFinished() {
				LogUtil.d("-->>onFinished");
			}

			@Override
			public void onSuccess(String str) {
				LogUtil.d("-->>onSuccess：" + str);

				try {
					DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
					Message message = Message.obtain();
					if (resultBase.isSuccess()) {
						MyApplication.getInstatnce().getLoginData().setUserType_Oid("B");
						MyApplication.getInstatnce().setUserTypeOid("B");
						MyApplication.getInstatnce().setCargoData(consignorBean);
						MyApplication.getInstatnce().setCompany_Oid(companyBean.Company_Oid);

						message.what = Constants.Request_Success;
						myHandler.sendMessage(message);
					} else {
						LogUtil.e("保存失败");
						message.what = Constants.Request_Fail;
						message.obj=resultBase.getErrorText();
						myHandler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.Request_Success:
				Toast.makeText(C_PersonInfoActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
				ActivityStackManager.getInstance().finishActivity(RegisteredActivity.class);
				C_PersonInfoActivity.this.finish();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_PersonInfoActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new CommonWaitDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						C_PersonInfoActivity.this.finish();
					}
				});
				alertDialog.show();
			default:
				break;
			}
		}
	};

	@Override
	public void onParkData(Object obj) {
		if (obj != null) {
			companyBean = (MDict_Company_Sheet) obj;
			logistics_park_tv.setText(companyBean.Company_Name);
		}
	}

	@Override
	public void onRegionData(Object object) {
		if (object != null) {
			regionBean = (RegionBean) object;
			address_tv.setText(ContextUtil.getRegion(regionBean));
		}
	}
}
