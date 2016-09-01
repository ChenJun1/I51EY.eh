/**
 * Administrator2016-5-19
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.FollowCarListRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.historyorder.HistoryOrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FollowCarAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

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
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 下午4:59:44
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@ContentView(R.layout.c_follow_car)
public class C_FollowCarActivity extends BaseActivity implements
		RegionDataCallBack2, OnRefreshListener {

	@ViewInject(R.id.c_select_btn)
	private Button c_select_btn;

	@ViewInject(R.id.c_Shipping_City_et)
	private TextView c_Shipping_City_et;

	@ViewInject(R.id.c_Consignee_City_et)
	private TextView c_Consignee_City_et;

	private AlertDialog.Builder alertDialog;

	private C_FollowCarAdapter c_FollowCarAdapter;

	private List<FollowCarListRequestBean> c_followList;

	private CommonWaitDialog myDialog;

	private boolean isRefresh = false;

	private boolean isLoad = false;

	private boolean flag_Shipping = false;

	private boolean flag_Consignee = false;

	private Handler myFreshHandler = null;

	private HistoryOrderRequestBean orderBean;

	@ViewInject(R.id.followCar_lv)
	private MyListView followCar_lv;

	@ViewInject(R.id.c_refresh_history_view)
	private PullToRefreshLayout c_refresh_history_view;
	
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		init();
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		loadText();
		isRefresh = true;
		selectHistoryOreder(Constants.CarTrackList_URL);
	}



	private void init() {
		c_refresh_history_view.setOnRefreshListener(this);

		orderBean = new HistoryOrderRequestBean();
		myDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		c_followList = new ArrayList<FollowCarListRequestBean>();
		c_FollowCarAdapter = new C_FollowCarAdapter(c_followList, this);
		followCar_lv.setAdapter(c_FollowCarAdapter);

		followCar_lv.setOnItemClickListener(MyItemOnClickListener);
		followCar_lv.setEmptyView(empty_list_view);
		CallBackManager.getInstance().getRegionCallBackManager2()
				.addCallBack(this);

		orderBean.Page_StartNum = Constants.FindStartNum;
		orderBean.Page_EndNum = Constants.FindNum;

	}

	private OnItemClickListener MyItemOnClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(!NetWorkUtils.isNetWort(C_FollowCarActivity.this)){
				return;
			}
			
			FollowCarListRequestBean FollowCar = (FollowCarListRequestBean) c_FollowCarAdapter
					.getItem(position);
			if (FollowCar != null) {
				Intent intent = new Intent(C_FollowCarActivity.this,
						C_FollowLocationCarActivity.class);
				intent.putExtra(Constants.OrderKey, FollowCar.Order_Key);
				startActivity(intent);
			}
		}
	};

	private void initView() {
		this.titlt_textView.setText("车辆跟踪");

	}

	@Event(value = { R.id.c_select_btn, R.id.back_linearLayout,
			R.id.c_Shipping_City_et, R.id.c_Consignee_City_et }, type = View.OnClickListener.class)
	private void OnClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.c_select_btn:
			loadText();
			isRefresh = true;
			selectHistoryOreder(Constants.CarTrackList_URL);

			break;
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.c_Shipping_City_et:
			flag_Shipping = true;
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key,
					Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_Consignee_City_et:
			flag_Consignee = true;
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key,
					Constants.Province_City_Key);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void loadText() {
		if (TextUtils.isEmpty(c_Shipping_City_et.getText())) {
			orderBean.Shipping_City = "";
		}

		if (TextUtils.isEmpty(c_Consignee_City_et.getText())) {
			orderBean.Consignee_City = "";
		} 

	}

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (myDialog != null) {
				myDialog.dismiss();
			}

			if (myFreshHandler != null) {
				myFreshHandler.sendEmptyMessage(0);
			}

			switch (msg.what) {
			case Constants.Request_Success:
				@SuppressWarnings("unchecked")
				List<FollowCarListRequestBean> dataList = (List<FollowCarListRequestBean>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						c_followList.clear();
						isRefresh = false;
					}
					c_followList.addAll(dataList);
					c_FollowCarAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						isRefresh = false;
						c_followList.clear();
						c_FollowCarAdapter.notifyDataSetChanged();
					}
					if (isLoad) {
						Toast.makeText(C_FollowCarActivity.this, "当前没有查询到信息！",
								Toast.LENGTH_SHORT).show();
						isLoad = false;
					}
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(C_FollowCarActivity.this, "当前没有查询到信息！",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_FollowCarActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String
						.valueOf(msg.obj));
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

	private void selectHistoryOreder(final String Url) {
		myDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url);
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
					requestData.setDataValue(JsonUtils.toJsonData(orderBean));

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
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								List<FollowCarListRequestBean> dataList = JsonUtils
										.parseFollowList(String
												.valueOf(resultBase
														.getDataValue()));
								if (dataList != null) {
									msg.what = Constants.Request_Success;
									msg.obj = dataList;
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

	@SuppressLint("HandlerLeak")
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		orderBean.Page_StartNum = Constants.FindStartNum;
		orderBean.Page_EndNum = Constants.FindNum;
		isRefresh = true;
		selectHistoryOreder(Constants.CarTrackList_URL);

	}

	@SuppressLint("HandlerLeak")
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		orderBean.Page_StartNum = c_followList.size() + Constants.FindStartNum;
		orderBean.Page_EndNum = c_followList.size() + Constants.FindNum;
		isLoad = true;

		selectHistoryOreder(Constants.CarTrackList_URL);

	}

	@Override
	public void onRegionData(Object object) {
		RegionBean regionBean = (RegionBean) object;
		if (flag_Shipping) {
			flag_Shipping = false;
			StringBuffer sb = new StringBuffer();
			sb.append(regionBean.provinceBean.PName);
			orderBean.Shipping_Provice = regionBean.provinceBean.PName;
			if (regionBean.cityBean != null) {
				sb.append(" " + regionBean.cityBean.CName);
				orderBean.Shipping_City = regionBean.cityBean.CName;
			}
			c_Shipping_City_et.setText(sb.toString());

		} else if (flag_Consignee) {
			flag_Consignee = false;
			StringBuffer sb = new StringBuffer();
			sb.append(regionBean.provinceBean.PName);
			orderBean.Consignee_Provice = regionBean.provinceBean.PName;
			if (regionBean.cityBean != null) {
				sb.append(" " + regionBean.cityBean.CName);
				orderBean.Consignee_City = regionBean.cityBean.CName;
			}
			c_Consignee_City_et.setText(sb.toString());

		}
	}
}
