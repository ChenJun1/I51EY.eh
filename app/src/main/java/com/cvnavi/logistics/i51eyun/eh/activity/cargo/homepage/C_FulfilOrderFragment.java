/**
 * Administrator2016-4-21
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseFragment;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderListRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoCompletedActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FulfilOrderAdapter;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
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
 * @date 2016-5-17 下午1:05:48
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_fulfilorderfragment)
public class C_FulfilOrderFragment extends BaseFragment implements
		OnRefreshListener {
	@SuppressWarnings("unused")
	private final static String TAG = "C_FulfilOrderFragment";

//	@ViewInject(R.id.c_fulfil_order_lv)
//	private PullableListView c_fulfil_order_lv;
	
	@ViewInject(R.id.c_fulfil_order_lv)
	private MyListView c_fulfil_order_lv;

	@ViewInject(R.id.c_refresh_fulfil_view)
	private PullToRefreshLayout c_refresh_fulfil_view;
	
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private List<WaitingOrderBaseInfo> fulfilList;

	private C_FulfilOrderAdapter fulfilOrderAdapter;

	private Context mContext;

	private boolean isRefresh = false;
	private boolean isLoad = false;
	private CommonWaitDialog waitDialog;

	private String FulfilLoadTime;

	private OrderListRequestBean pageRequestBean;
	
	private AlertDialog.Builder alertDialog;
	
	private Handler myFreshHandler = null;

	public static C_FulfilOrderFragment newInstance() {
		return new C_FulfilOrderFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		waitDialog = new CommonWaitDialog(getActivity(),
				R.style.progress_dialog);
		init();

		fulfilOrderAdapter = new C_FulfilOrderAdapter(fulfilList, mContext);
		c_refresh_fulfil_view.setOnRefreshListener(this);
		c_fulfil_order_lv.setOnItemClickListener(itemClickListener);
		c_fulfil_order_lv.setAdapter(fulfilOrderAdapter);
		c_fulfil_order_lv.setEmptyView(empty_list_view);
		initLoadData();
	}

	private void initLoadData() {
		if(!NetWorkUtils.isNetWort(getActivity())){
			return;
		}
		waitDialog.show();
		loadWaitinOrderRequest(Constants.Order_Url);
	}

	private void init() {
		FulfilLoadTime = DateUtil.getCurDateStr();
		fulfilList = new ArrayList<WaitingOrderBaseInfo>();
		mContext = this.getActivity();
		pageRequestBean = new OrderListRequestBean();

		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum = Constants.FindNum;
		pageRequestBean.FirstRequestTime = FulfilLoadTime;
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			
			WaitingOrderBaseInfo orderInfo = (WaitingOrderBaseInfo) fulfilOrderAdapter
					.getItem(position);
			if (orderInfo != null) {
				Intent intent = new Intent(getActivity(),
						C_OrderInfoCompletedActivity.class);
				intent.putExtra(Constants.OrderKey, orderInfo);
				startActivity(intent);
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
				List<WaitingOrderBaseInfo> dataList = (List<WaitingOrderBaseInfo>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						fulfilList.clear();
						isRefresh = false;
					}
					fulfilList.addAll(dataList);
					fulfilOrderAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						fulfilList.clear();
						fulfilOrderAdapter.notifyDataSetChanged();
					}
					if (isLoad) {
						Toast.makeText(getActivity(), "当前没有查询到信息！",
								Toast.LENGTH_SHORT).show();
						isLoad = false;
					}
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(getActivity(), "没当前没有查询到信息！", Toast.LENGTH_SHORT)
						.show();

				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服" : String.valueOf(msg.obj));
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
	

	@Override
	public void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(getActivity())){
			return;
		}
	}

	// 加载
	private void loadWaitinOrderRequest(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				pageRequestBean.Operate_Code = "AG";
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils
							.toJsonData(pageRequestBean));

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
								List<WaitingOrderBaseInfo> dataList = JsonUtils
										.parseOrderBeanList(str);
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

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		FulfilLoadTime = DateUtil.getCurDateStr();
		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum =Constants.FindNum;
		pageRequestBean.FirstRequestTime = FulfilLoadTime;

		isRefresh = true;
		loadWaitinOrderRequest(Constants.Order_Url);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
			
		pageRequestBean.Page_StartNum =fulfilList.size()+ Constants.FindStartNum;
		pageRequestBean.Page_EndNum = fulfilList.size()
				+ Constants.FindNum;
		pageRequestBean.FirstRequestTime = FulfilLoadTime;

		isLoad = true;
		loadWaitinOrderRequest(Constants.Order_Url);

	}

}
