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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseFragment;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderListRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.AdapterItemForTextViewCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoReceivedActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_ReceivedOrderAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
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
 * @date 2016-5-17 下午1:06:03
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_receivedorderfragment)
public class C_ReceivedOrderFragment extends BaseFragment implements
		OnRefreshListener, AdapterItemForTextViewCallBack {

	// @ViewInject(value = R.id.c_received_order_lv)
	// private PullableListView c_received_order_lv;

	@ViewInject(value = R.id.c_received_order_lv)
	private MyListView c_received_order_lv;

	@ViewInject(value = R.id.c_refresh_receive_view)
	private PullToRefreshLayout c_refresh_received_view;

	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private C_ReceivedOrderAdapter receivedOrderAdapter;

	private List<WaitingOrderBaseInfo> receivedList;

	private Context mContext;

	private boolean isRefresh = false;
	private boolean isLoad = false;
	private CommonWaitDialog waitDialog;

	private AlertDialog.Builder alertDialog;

	private String ReceivedLoadTime;

	private OrderListRequestBean pageRequestBean;

	private RatingBar c_RatingBar1;
	private RatingBar c_RatingBar2;
	private RatingBar c_RatingBar3;

	private TextView c_fen1;
	private TextView c_fen2;
	private TextView c_fen3;

	private EditText c_remark;
	private Button c_submit;
	private PopupWindow popuWindow1;
	private View contentView1;

	private OrderEvaluationInfo orderEvaluationInfo;
	private WaitingOrderBaseInfo orderInfo;

	private Handler myFreshHandler = null;

	public static C_ReceivedOrderFragment newInstance() {
		return new C_ReceivedOrderFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
		init();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		c_received_order_lv.setAdapter(receivedOrderAdapter);
		c_received_order_lv.setOnItemClickListener(itemClickListener);
		c_refresh_received_view.setOnRefreshListener(this);
		c_received_order_lv.setEmptyView(empty_list_view);
	}

	// 初始化请求实体
	private void init() {

		receivedList = new ArrayList<WaitingOrderBaseInfo>();
		receivedOrderAdapter = new C_ReceivedOrderAdapter(receivedList,
				mContext, this);
		pageRequestBean = new OrderListRequestBean();
		orderEvaluationInfo = new OrderEvaluationInfo();
		initLoadData();

	}

	/**
	 * 
	 */
	private void initLoadData() {
		waitDialog = new CommonWaitDialog(mContext, R.style.progress_dialog);
		waitDialog.show();
		ReceivedLoadTime = DateUtil.getCurDateStr();
		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum = Constants.FindNum;
		pageRequestBean.FirstRequestTime = ReceivedLoadTime;
		loadWaitinOrderRequest(Constants.Order_Url);

	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(getActivity())){
			return;
		}
	}

	/**
	 * 评价窗口
	 * 
	 * @param parent
	 */
	private void initPopuWindow1() {
		popuWindow1 = null;
		if (popuWindow1 == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
			contentView1 = mLayoutInflater.inflate(R.layout.c_assess_popup,
					null);
			popuWindow1 = new PopupWindow(contentView1,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		c_RatingBar1 = (RatingBar) contentView1.findViewById(R.id.c_RatingBar1);
		c_RatingBar2 = (RatingBar) contentView1.findViewById(R.id.c_RatingBar2);
		c_RatingBar3 = (RatingBar) contentView1.findViewById(R.id.c_RatingBar3);
		// c_RatingBar2 = ViewHolder.get(contentView1, R.id.c_RatingBar2);
		// c_RatingBar3 = ViewHolder.get(contentView1, R.id.c_RatingBar3);

		c_fen1 = ViewHolder.get(contentView1, R.id.c_fen1);
		c_fen1.setText(String.valueOf((int)c_RatingBar1.getRating()));
		orderEvaluationInfo.HZPJ_THZS = String.valueOf((int) c_RatingBar1
				.getRating());

		c_fen2 = ViewHolder.get(contentView1, R.id.c_fen2);
		c_fen2.setText(String.valueOf((int)c_RatingBar2.getRating()));
		orderEvaluationInfo.HZPJ_HWWZ = String.valueOf((int) c_RatingBar2
				.getRating());

		c_fen3 = ViewHolder.get(contentView1, R.id.c_fen3);
		c_fen3.setText(String.valueOf((int)c_RatingBar3.getRating()));
		orderEvaluationInfo.HZPJ_HWTD = String.valueOf((int) c_RatingBar3
				.getRating());

		c_remark = ViewHolder.get(contentView1, R.id.c_remark);
		c_submit = ViewHolder.get(contentView1, R.id.c_submit);

		c_RatingBar1
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						c_fen1.setText(String.valueOf((int) rating));
						orderEvaluationInfo.HZPJ_THZS = String
								.valueOf((int) ratingBar.getRating());
					}
				});
		c_RatingBar2
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						c_fen2.setText(String.valueOf((int) rating));
						orderEvaluationInfo.HZPJ_HWWZ = String
								.valueOf((int) ratingBar.getRating());
					}
				});
		c_RatingBar3
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						c_fen3.setText(String.valueOf((int) rating));
						orderEvaluationInfo.HZPJ_HWTD = String
								.valueOf((int) ratingBar.getRating());
					}
				});
		if (!TextUtils.isEmpty(c_remark.getText())) {
			orderEvaluationInfo.HZPJ_BZ = c_remark.getText().toString();
		}

		c_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkEvaluation()) {
					requestOrderEvaluation();
					if (popuWindow1.isShowing()) {
						popuWindow1.dismiss();
					}
				}
			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindow1.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = 0.4f;
		getActivity().getWindow().setAttributes(lp);

		popuWindow1.setOutsideTouchable(true);
		popuWindow1.setFocusable(true);
		popuWindow1.showAtLocation(contentView1, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);

		popuWindow1.update();
		popuWindow1.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getActivity().getWindow()
						.getAttributes();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
				popuWindow1.dismiss();
			}
		});
	}

	/**
	 * 检查提交参数
	 * 
	 * @return
	 */
	private boolean checkEvaluation() {

		if (c_RatingBar1.getRating() > 0 && c_RatingBar2.getRating() > 0
				&& c_RatingBar1.getRating() > 0) {
			return true;
		}
		Toast.makeText(getActivity(), "请完善评分！", Toast.LENGTH_SHORT).show();
		return false;

	}

	/**
	 * 评价请求
	 */
	private void requestOrderEvaluation() {
		waitDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.OrderEvaluation_URL);
				orderEvaluationInfo.Order_Key = orderInfo.Order_Key;
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
							.toJsonData(orderEvaluationInfo));

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

							Message msg = Message.obtain();
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								msg.what = Constants.Asses_Success;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Asses_Fail;
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

	// ==================
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			
			orderInfo = (WaitingOrderBaseInfo) receivedOrderAdapter
					.getItem(position);
			if (orderInfo != null) {
				Intent intent = new Intent(getActivity(),
						C_OrderInfoReceivedActivity.class);
				intent.putExtra(Constants.OrderKey, orderInfo);
				startActivity(intent);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	public Handler myHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			if (myFreshHandler != null) {
				myFreshHandler.sendEmptyMessage(0);
			}
			switch (msg.what) {
			case Constants.Request_Success:
				List<WaitingOrderBaseInfo> dataList = (List<WaitingOrderBaseInfo>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						receivedList.clear();
						isRefresh = false;
					}
					receivedList.addAll(dataList);
					receivedOrderAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						isRefresh = false;
						receivedList.clear();
						receivedOrderAdapter.notifyDataSetChanged();
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
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服" : String
						.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			case Constants.Asses_Success:
				RequestCompleteOrder();
				Toast.makeText(getActivity(), "恭喜您！评价成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Asses_Fail:
				Toast.makeText(getActivity(), "评价失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Order_jiaojuo_Success:

				isRefresh = true;
				receivedList.clear();
				receivedOrderAdapter.notifyDataSetChanged();
				loadWaitinOrderRequest(Constants.Order_Url);
				Toast.makeText(getActivity(), "成功收货！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 确认收货请求
	 */
	private void RequestCompleteOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.ConfirmCompleteOrderByConsignor_Url);
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
					requestData.setDataValue(orderInfo.Order_Key);

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
						Message msg = Message.obtain();
						try {
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								msg.what = Constants.Order_jiaojuo_Success;
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

	// 加载
	public void loadWaitinOrderRequest(final String URL) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(URL);
				pageRequestBean.Operate_Code = "AB";
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

		if (!NetWorkUtils.isNetWort(getActivity())) {
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		ReceivedLoadTime = DateUtil.getCurDateStr();
		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum = Constants.FindNum;
		pageRequestBean.FirstRequestTime = ReceivedLoadTime;
		isRefresh = true;
		loadWaitinOrderRequest(Constants.Order_Url);
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

		if (!NetWorkUtils.isNetWort(getActivity())) {
			myFreshHandler.sendEmptyMessage(0);
			return;
		}

		pageRequestBean.Page_StartNum = receivedList.size()
				+ Constants.FindStartNum;
		pageRequestBean.Page_EndNum = receivedList.size() + Constants.FindNum;
		pageRequestBean.FirstRequestTime = ReceivedLoadTime;
		isLoad = true;
		loadWaitinOrderRequest(Constants.Order_Url);

	}

	@Override
	public void onTextViewClick(int position, Object object) {
		orderInfo = (WaitingOrderBaseInfo) object;
		if(orderInfo!=null){
		if (!TextUtils.isEmpty(orderInfo.IsCESuccess)&&orderInfo.IsCESuccess.equals("1")) {
			Toast.makeText(getActivity(), "请勿重复评价！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData()
				.getISEvaluate())
				&& MyApplication.getInstatnce().getLoginData().getISEvaluate()
						.equals("1")) {
			if (orderInfo != null) {
				if (orderInfo.Operate_Code.equals("AF")) {
					initPopuWindow1();
					return;
				}
			} else {
				if (orderInfo.Operate_Code.equals("AF")) {
					RequestCompleteOrder();
					return;
				}
			}

		}
	}
	}

}
