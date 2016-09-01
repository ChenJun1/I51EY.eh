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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseFragment;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderListRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.UnassignCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoWaitingActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_WaitingOrderAdapter;
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
 * @date 2016-5-17 下午1:06:10
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_waitingorderfragment)
public class C_WaitingOrderFragment extends BaseFragment implements
		OnRefreshListener, UnassignCallBack {

	// @ViewInject(R.id.c_waiting_order_lv)
	// private PullableListView c_waiting_order_lv;
	@ViewInject(R.id.c_waiting_order_lv)
	private MyListView c_waiting_order_lv;

	@ViewInject(R.id.c_refresh_waiting_view)
	private PullToRefreshLayout c_refresh_waiting_view;

	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private List<WaitingOrderBaseInfo> waitingList;

	private C_WaitingOrderAdapter waitingOrderAdapter;

	private Context mContext;

	private boolean isRefresh = false;

	private boolean isLoad = false;

	private CommonWaitDialog waitDialog;

	private String WaitingLoadTime = "";

	private AlertDialog.Builder alertDialog;

	private OrderListRequestBean pageRequestBean;

	public static UnassignCallBack usCallBack;

	private Handler myFreshHandler = null;

	public static C_WaitingOrderFragment newInstance() {
		return new C_WaitingOrderFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
		usCallBack = this;
		init();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		c_refresh_waiting_view.setOnRefreshListener(this);

		c_waiting_order_lv.setAdapter(waitingOrderAdapter);

		c_waiting_order_lv.setOnItemLongClickListener(itemLongClickListener);

		c_waiting_order_lv.setOnItemClickListener(itemClickListener);

		c_waiting_order_lv.setEmptyView(empty_list_view);

	}

	private void init() {

		waitingList = new ArrayList<WaitingOrderBaseInfo>();
		waitingOrderAdapter = new C_WaitingOrderAdapter(waitingList, mContext);
		pageRequestBean = new OrderListRequestBean();

		initLoad();
	}

	private void initLoad() {
		waitDialog = new CommonWaitDialog(mContext, R.style.progress_dialog);
		waitDialog.show();
		WaitingLoadTime = DateUtil.getCurDateStr();
		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum = Constants.FindNum;
		pageRequestBean.FirstRequestTime = WaitingLoadTime;
		loadWaitinOrderRequest(Constants.Order_Url);
	}

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			if (myFreshHandler != null) {
				myFreshHandler.sendEmptyMessage(0);
			}
			switch (msg.what) {
			case Constants.Request_Success:
				@SuppressWarnings("unchecked")
				List<WaitingOrderBaseInfo> dataList = (List<WaitingOrderBaseInfo>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						waitingList.clear();
						isRefresh = false;
					}
					waitingList.addAll(dataList);
					waitingOrderAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						isRefresh = false;
						waitingList.clear();
						waitingOrderAdapter.notifyDataSetChanged();
					}
					if (isLoad) {
						Toast.makeText(getActivity(), "当前没有查询到信息！",
								Toast.LENGTH_SHORT).show();
						isLoad = false;
					}
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(getActivity(), "当前没有查询到信息！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String
						.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			case Constants.Request_Delete:
				WaitingOrderBaseInfo orderInfo = (WaitingOrderBaseInfo) msg.obj;
				waitingList.remove(orderInfo);
				waitingOrderAdapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	private void loadWaitinOrderRequest(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				pageRequestBean.Operate_Code = "AA";
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

	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {

			if (!NetWorkUtils.isConnectedByState(getActivity())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("提示");
				builder.setMessage("请检查网络是否连接！");
				builder.setPositiveButton("确定", null);
				builder.create().show();
				return true;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("提示");
			builder.setMessage("是否确认删除?");
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				}
			});
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					WaitingOrderBaseInfo waiInfo = (WaitingOrderBaseInfo) waitingOrderAdapter
							.getItem(position);
					if (waiInfo != null) {
						DeleteWaitinOrderRequest(Constants.DeleteOrder_Url,
								waiInfo);
					}
				}
			});
			builder.create().show();
			return true;
		}
	};

	/**
	 * 删除
	 */
	private void DeleteWaitinOrderRequest(final String URL,
			final WaitingOrderBaseInfo waiInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);

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
					OrderRequestBean bean = new OrderRequestBean();
					bean.Order_Key = waiInfo.Order_Key;
					requestData.setDataValue(JsonUtils.toJsonData(bean));

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
							Message msg = Message.obtain();
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								msg.what = Constants.Request_Delete;
								msg.obj = waiInfo;
								myHandler.sendMessage(msg);
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

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!NetWorkUtils.isConnectedByState(getActivity())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("提示");
				builder.setMessage("请检查网络是否连接！");
				builder.setPositiveButton("确定", null);
				builder.create().show();
				return;
			}
			WaitingOrderBaseInfo orderInfo = (WaitingOrderBaseInfo) waitingOrderAdapter
					.getItem(position);
			if (orderInfo != null) {
				Intent intent = new Intent(getActivity(),
						C_OrderInfoWaitingActivity.class);
				intent.putExtra(Constants.OrderKey, orderInfo);
				startActivity(intent);
			}
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		usCallBack = null;
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		WaitingLoadTime = DateUtil.getCurDateStr();
		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum = Constants.FindNum;
		pageRequestBean.FirstRequestTime = WaitingLoadTime;
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
		
		pageRequestBean.Page_StartNum = waitingList.size()
				+ Constants.FindStartNum;
		pageRequestBean.Page_EndNum = waitingList.size() + Constants.FindNum;
		pageRequestBean.FirstRequestTime = WaitingLoadTime;
		isLoad = true;

		loadWaitinOrderRequest(Constants.Order_Url);

	}

	@Override
	public void onRefreshData() {
		isRefresh = true;
		initLoad();
	}

	@Override
	public void onUpdateData(Object object) {
		WaitingOrderBaseInfo orderBaseInfo = (WaitingOrderBaseInfo) object;
		for (WaitingOrderBaseInfo orderInfo : waitingList) {
			if (orderBaseInfo.Order_Key.equals(orderInfo.Order_Key)) {
				orderInfo.Order_Type = "A";
			}
		}
		waitingOrderAdapter.notifyDataSetChanged();
	}
}
