/**
 * Administrator2016-5-23
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.RequestCarList;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_ScreenAdapterCarType;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.gridview.MyGridView;

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
import java.util.ArrayList;
import java.util.List;

/**
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-23 下午1:06:04
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_find_car_screen)
public class C_FindCarScreenActivity extends BaseActivity implements RegionDataCallBack2{

	@ViewInject(value = R.id.titlt_textView)
	private TextView titleTextView;

	@ViewInject(value = R.id.back_linearLayout)
	private LinearLayout backLinearLayout;

	@ViewInject(R.id.c_screen_gridview_02)
	private MyGridView carTypeGridView;

	@ViewInject(R.id.c_Shipping_City_et)
	private EditText c_Shipping_City_et;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.c_Consignee_City_et)
	private EditText c_Consignee_City_et;

	@ViewInject(R.id.c_submit_btn)
	private Button c_submit_btn;

	private List<Dict_CarType_Sheet> carTypeList;

	private C_ScreenAdapterCarType adapterCarType;

	private RequestCarList requestCar;
	
	private boolean  flag1=false;
	
	private boolean flag2=false;
	
	public static boolean flag3=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CallBackManager.getInstance().getRegionCallBackManager2().addCallBack(this);
		requestCar = new RequestCarList();
		requestCar.Page_StartNum=Constants.FindStartNum;
		requestCar.Page_EndNum=Constants.FindNum;

		carTypeList = new ArrayList<Dict_CarType_Sheet>();

		initView();
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
		loadCarTypeData();
	}

	@Event(value = { R.id.back_linearLayout, R.id.c_Shipping_City_et,
			R.id.c_Consignee_City_et, R.id.c_submit_btn }, type = View.OnClickListener.class)
	private void initOnclick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.back_linearLayout:
			this.finish();
			break;
		case R.id.c_Shipping_City_et:
			flag1=true;
			intent=new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key,Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_Consignee_City_et:
			flag2=true;
			intent=new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key,Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_submit_btn:
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			CallBackManager.getInstance().getFilterCarCallBackManager().fireCallBack(requestCar, true);
			flag3=true;
			this.finish();
			break;

		default:
			break;
		}
	}

	private void initView() {
		titleTextView.setText("筛选");

		adapterCarType = new C_ScreenAdapterCarType(this, carTypeList);
		carTypeGridView.setAdapter(adapterCarType);
		carTypeGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		// 按车型监听
		carTypeGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < parent.getCount(); i++) {
					View v = parent.getChildAt(i);
					if (position == i) { // 当前选中的Item改变背景颜色
						v.findViewById(R.id.c_screen_02_layout).setBackgroundResource(R.drawable.select);
						requestCar.CarType_Oid = carTypeList.get(i).CarType_Oid;
					} else {
						v.findViewById(R.id.c_screen_02_layout).setBackgroundResource(R.drawable.pretermit);
					}
				}
			}
		});

	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.Request_Success:
				carTypeList.clear();

				carTypeList.addAll(MyApplication.getInstatnce()
						.getBasicDataBuffer().getCarTypeList());
				adapterCarType.notifyDataSetChanged();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_FindCarScreenActivity.this);
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
			default:
				break;
			}
		};
	};

	/**
	 * 按车型
	 */
	private void loadCarTypeData() {
		if (MyApplication.getInstatnce().getBasicDataBuffer().getCarTypeList() == null
				|| MyApplication.getInstatnce().getBasicDataBuffer()
						.getCarTypeList().size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue("GetCarTypeList");

					RequestParams params = new RequestParams(
							Constants.Get_CarTypeList_Url);
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
					x.http().post(params,
							new Callback.CommonCallback<String>() {
								public void onCancelled(CancelledException arg0) {
									LogUtil.d("-->>请求取消");
								}

								@Override
								public void onError(Throwable ex,
										boolean isOnCallback) {
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
										if (resultBase.isSuccess()) {
											List<Dict_CarType_Sheet> dataList = (List<Dict_CarType_Sheet>) JsonUtils
													.parseCarTypeList(str);

											if (dataList != null) {
												MyApplication
														.getInstatnce()
														.getBasicDataBuffer()
														.setCarTypeList(
																dataList);

												Message msg = Message.obtain();
												msg.what = Constants.Request_Success;
												
												myHandler.sendMessage(msg);
											}
										} else {
											Message msg = Message.obtain();
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
		} else {
			carTypeList.addAll(MyApplication.getInstatnce()
					.getBasicDataBuffer().getCarTypeList());
			adapterCarType.notifyDataSetChanged();
		}
	}

	@Override
	public void onRegionData(Object object) {
		RegionBean regionBean=(RegionBean) object;
		if(flag1){
			flag1=false;
			StringBuffer sb=new StringBuffer();
			sb.append(regionBean.provinceBean.PName);
			requestCar.Provenance_Provice=regionBean.provinceBean.PName;
			if(regionBean.cityBean!=null){
				sb.append(" "+regionBean.cityBean.CName);
				requestCar.Provenance_City=regionBean.cityBean.CName;
			}
			c_Shipping_City_et.setText(sb.toString());
			
		}else if(flag2){
			flag2=false;
			StringBuffer sb=new StringBuffer();
			sb.append(regionBean.provinceBean.PName);
			requestCar.Destination_Provice=regionBean.provinceBean.PName;
			if(regionBean.cityBean!=null){
				sb.append(" "+regionBean.cityBean.CName);
				requestCar.Destination_City=regionBean.cityBean.CName;
			}
			c_Consignee_City_et.setText(sb.toString());
			
		}
	}
}
