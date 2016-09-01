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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MDict_CarCode_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.RequestCarList;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.FilterCarDataCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FindCarAdapter;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.pullableview.PullableListView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:08:03
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_activity_findcar)
public class C_FindCarActivity extends BaseActivity implements
		OnRefreshListener, FilterCarDataCallBack {

	@ViewInject(R.id.c_filter)
	private TextView cFilter;

	@ViewInject(R.id.c_car_list)
	private PullableListView carListView;

	@ViewInject(R.id.c_refresh_fincar_view)
	private PullToRefreshLayout c_refresh_fulfil_view;

	@ViewInject(R.id.titlt_textView)
	private TextView titleTextView;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout backLinearLayout;

	private List<MDict_CarCode_Sheet> carlList;
	private C_FindCarAdapter c_FindCarAdapter;

	private RequestCarList requestCar;
	private boolean isRefresh=false;

	private CommonWaitDialog waitDialog;
	
	@ViewInject(R.id.operation_btn)
	private Button operation_btn;
	
	private Handler myFreshHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		C_FindCarScreenActivity.flag3=true;
		titleTextView.setText("我要找车");
		operation_btn.setVisibility(View.VISIBLE);
		operation_btn.setText("筛选");
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		c_refresh_fulfil_view.setOnRefreshListener(this);
		CallBackManager.getInstance().getFilterCarCallBackManager()
				.addCallBack(this);

		requestCar = new RequestCarList();
		requestCar.Page_StartNum = Constants.FindStartNum;
		requestCar.Page_EndNum = Constants.FindNum;

		carlList = new ArrayList<MDict_CarCode_Sheet>();
		c_FindCarAdapter = new C_FindCarAdapter(carlList, this);
		carListView.setAdapter(c_FindCarAdapter);
		carListView.setOnItemClickListener(mylistViewListener);

		//loadCarListByCondition();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		isRefresh = true;
		if(C_FindCarScreenActivity.flag3){
			loadCarListByCondition();
			C_FindCarScreenActivity.flag3=false;
		}
		
	}

	private OnItemClickListener mylistViewListener = new OnItemClickListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {

			if(!NetWorkUtils.isNetWort(C_FindCarActivity.this)){
				return;
			}
			MDict_CarCode_Sheet car = carlList.get(position);
			
//				if (getIntent().getStringExtra(Constants.DeliveryGoods_FindCar)
//						.equals(Constants.DeliveryGoods_FindCar)) {
//					C_DeliveryGoodsFor2Activity.callback.onFindCarData(car);
//					C_FindCarActivity.this.finish();
//		
//				}
			if (car != null) {
				String flagString = getIntent().getStringExtra(
						Constants.DeliveryGoods_FindCar);

				if (flagString != null
						&& flagString.equals(Constants.DeliveryGoods_FindCar)) {
					CallBackManager.getInstance().getCarCallBackManager().fireCallBack(car);
					C_FindCarActivity.this.finish();
				} else {
					Intent intent = new Intent(C_FindCarActivity.this,
							C_CarDetailedInformationActivity.class);
					intent.putExtra("CarCode_Key", car.CarCode_Key);
					startActivity(intent);
				}
			}
		}
	};

	private Handler myHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			if(myFreshHandler!=null){
				myFreshHandler.sendEmptyMessage(0);
			}
			switch (msg.what) {
			case Constants.Request_Success:
				List<MDict_CarCode_Sheet> dataList = (List<MDict_CarCode_Sheet>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						carlList.clear();
						isRefresh = false;
					}
					carlList.addAll(dataList);
					c_FindCarAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						carlList.clear();
						c_FindCarAdapter.notifyDataSetChanged();
					}
					Toast.makeText(C_FindCarActivity.this, "当前没有查询到信息！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(C_FindCarActivity.this, "当前没有查询到信息！",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_FindCarActivity.this);
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
			default:
				break;
			}
		}
	};

	private void loadCarListByCondition() {
		waitDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Get_simpleCarListByCondition_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(requestCar));

					String jsonStr = JsonUtils.toJsonData(requestData);
					LogUtil.d("-->>请求参数：" + jsonStr);

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
								List<MDict_CarCode_Sheet> dataList = JsonUtils
										.parseCommonlyCarList(String
												.valueOf(resultBase
														.getDataValue()));
								if (dataList != null) {
									msg.what = Constants.Request_Success;
									msg.obj = dataList;

									myHandler.sendMessage(msg);
								} else {
									msg.what = Constants.Request_Fail;
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

	@Event(value = { R.id.back_linearLayout, R.id.c_filter,R.id.operation_btn }, type = View.OnClickListener.class)
	private void dataClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.operation_btn:
			 showActivity(C_FindCarActivity.this, C_FindCarScreenActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		requestCar=new RequestCarList();
		requestCar.Page_StartNum = Constants.FindStartNum;
		requestCar.Page_EndNum = Constants.FindNum;

		isRefresh = true;
		loadCarListByCondition();

	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		isRefresh=false;
		requestCar.Page_StartNum = carlList.size() + Constants.FindStartNum;
		requestCar.Page_EndNum = requestCar.Page_StartNum + Constants.FindNum;

		loadCarListByCondition();

	}

	@Override
	public void onFilterCar(final Object object, boolean isFilter) {
		if (isFilter) {
		
					if (object != null) {
						RequestCarList bean = (RequestCarList) object;
						isRefresh=true;

						requestCar.Provenance_Provice = bean.Provenance_Provice;
						requestCar.Provenance_City = bean.Provenance_City;
						requestCar.Destination_Provice = bean.Destination_Provice;
						requestCar.Destination_City = bean.Destination_City;
						requestCar.CarType_Oid = bean.CarType_Oid;
						requestCar.Page_StartNum = Constants.FindStartNum;
						requestCar.Page_EndNum = Constants.FindNum;
				}
		}
	}
}
