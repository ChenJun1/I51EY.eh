package com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.GridView;
import android.widget.ListView;
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
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_FindCarActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_FollowCarActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoCompletedActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoListActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoReceivedActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoWaitingActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_HomeAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_MyGridAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.delivery.C_DeliveryGoodsFor2Activity;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushOrderMessageBean;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.ImageCycleView;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:05:54
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint({ "HandlerLeak", "ShowToast" })
public class C_HomePageFragment extends BaseFragment implements
		OnRefreshListener, AdapterItemForTextViewCallBack {

	public String[] img_text = { "我要找车", "我要发货", "运单列表", "车辆跟踪" };
	public int[] imgs = { R.drawable.driver, R.drawable.fahuo,
			R.drawable.order_list, R.drawable.trace };

	private GridView gridview;
	private PullToRefreshLayout psl;

	private ListView orderListView;
	private List<WaitingOrderBaseInfo> orderDataList;
	private C_HomeAdapter orderAdapter;

	public static final String MESSAGE_RECEIVED_ACTION = "com.cvnavi.logistics.i51eyun.eh.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_EXTRAS = "extras";
	public static boolean isForeground = false;

	private MessageReceiver messageReceiver;

	private TextView empty_list_view;

	private AlertDialog.Builder alertDialog;

	private Context context;

	private boolean isRefresh = false;
	// 计算隐藏布局高度
	// private static int height = 0;
	// 是否是第一个item
	private boolean isLoad = false;

	private ImageCycleView imageCycleView;

	private PopupWindow assessPopuWindow;
	private PopupWindow jPushMessagePopuWindow;
	private View contentView;
	private View jPushMessagePopupWindowContentView;

	/**
	 * 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形
	 */
	private int stype = 0;
	private CommonWaitDialog waitDialog;
	private String HomeLoadTime = "";
	private OrderListRequestBean pageRequestBean;

	private RatingBar c_RatingBar1;
	private RatingBar c_RatingBar2;
	private RatingBar c_RatingBar3;

	private TextView c_fen1;
	private TextView c_fen2;
	private TextView c_fen3;

	private EditText c_remark;
	private Button c_submit;

	private int OrderRow;

	private OrderEvaluationInfo orderEvaluationInfo;

	private WaitingOrderBaseInfo orderInfo;

	private TextView order_title;

	private Handler myFreshHandler = null;

	public static C_HomePageFragment newInstance() {
		return new C_HomePageFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderEvaluationInfo = new OrderEvaluationInfo();
		orderDataList = new ArrayList<WaitingOrderBaseInfo>();

		orderAdapter = new C_HomeAdapter(orderDataList, getActivity(), this);
		context = getActivity();
		pageRequestBean = new OrderListRequestBean();
		waitDialog = new CommonWaitDialog(getActivity(),
				R.style.progress_dialog);
		registerMessageReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.c_homepage_fragment, container,
				false);

		initView(view);
		if (Constants.mImageUrl.size() > 0) {
			initImage();
		}
		return view;
	}

	/**
	 * 推送
	 */
	private void pushMessage(JPushOrderMessageBean dataInfo) {
		if (jPushMessagePopuWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
			jPushMessagePopupWindowContentView = mLayoutInflater.inflate(
					R.layout.c_layout_poppup, null);
			jPushMessagePopuWindow = new PopupWindow(
					jPushMessagePopupWindowContentView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		TextView data_text_tv = ViewHolder.get(
				jPushMessagePopupWindowContentView, R.id.data_text_tv);
		TextView close_text = ViewHolder.get(
				jPushMessagePopupWindowContentView, R.id.close_text);
		data_text_tv.setText(dataInfo.Message_Content);
		close_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (jPushMessagePopuWindow.isShowing()) {
					jPushMessagePopuWindow.dismiss();
				}
			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		jPushMessagePopuWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = 0.4f;
		getActivity().getWindow().setAttributes(lp);

		jPushMessagePopuWindow.setOutsideTouchable(true);
		jPushMessagePopuWindow.setFocusable(true);
		jPushMessagePopuWindow.showAtLocation(
				jPushMessagePopupWindowContentView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);

		jPushMessagePopuWindow.update();
		jPushMessagePopuWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getActivity().getWindow()
						.getAttributes();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
			}
		});

	}

	// ==================================================================广播
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				JPushOrderMessageBean messageBean = (JPushOrderMessageBean) intent
						.getSerializableExtra(KEY_EXTRAS);

				pushMessage(messageBean);
			}
		}
	}

	public void registerMessageReceiver() {
		messageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);

		context.registerReceiver(messageReceiver, filter);
	}

	@Override
	public void onPause() {
		isForeground = false;
		super.onPause();
	}

	// 获取未读信息数量
	private void initLoadOrderRow() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.GETISREADORDERROW_URL);
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

					requestData.setDataValue(null);

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
								msg.what = Constants.OrderRow_Success;
								msg.obj = resultBase.getDataValue();
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initPopuWindow1() {
		assessPopuWindow = null;
		if (assessPopuWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
			contentView = mLayoutInflater.inflate(R.layout.c_assess_popup,
					null);
			assessPopuWindow = new PopupWindow(contentView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		c_RatingBar1 = (RatingBar) contentView.findViewById(R.id.c_RatingBar1);
		c_RatingBar2 = (RatingBar) contentView.findViewById(R.id.c_RatingBar2);
		c_RatingBar3 = (RatingBar) contentView.findViewById(R.id.c_RatingBar3);
		// c_RatingBar2 = ViewHolder.get(contentView1, R.id.c_RatingBar2);
		// c_RatingBar3 = ViewHolder.get(contentView1, R.id.c_RatingBar3);

		c_fen1 = ViewHolder.get(contentView, R.id.c_fen1);
		c_fen1.setText(String.valueOf((int)c_RatingBar1.getRating()));
		orderEvaluationInfo.HZPJ_THZS = String.valueOf((int) c_RatingBar1
				.getRating());

		c_fen2 = ViewHolder.get(contentView, R.id.c_fen2);
		c_fen2.setText(String.valueOf((int)c_RatingBar2.getRating()));
		orderEvaluationInfo.HZPJ_HWWZ = String.valueOf((int) c_RatingBar2
				.getRating());

		c_fen3 = ViewHolder.get(contentView, R.id.c_fen3);
		c_fen3.setText(String.valueOf((int)c_RatingBar3.getRating()));
		orderEvaluationInfo.HZPJ_HWTD = String.valueOf((int) c_RatingBar3
				.getRating());

		c_remark = ViewHolder.get(contentView, R.id.c_remark);
		c_submit = ViewHolder.get(contentView, R.id.c_submit);

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

		c_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(c_remark.getText())) {
					orderEvaluationInfo.HZPJ_BZ = c_remark.getText().toString();
				}
				if (checkEvaluation()) {
					waitDialog.show();
					requestOrderEvaluation();
					RequestCompleteOrder();
				}
			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		assessPopuWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = 0.4f;
		getActivity().getWindow().setAttributes(lp);

		assessPopuWindow.setOutsideTouchable(true);
		assessPopuWindow.setFocusable(true);
		assessPopuWindow.showAtLocation(contentView, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);

		assessPopuWindow.update();
		assessPopuWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getActivity().getWindow()
						.getAttributes();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
				assessPopuWindow.dismiss();
			}
		});
	}

	private boolean checkEvaluation() {

		if (c_RatingBar1.getRating() > 0 && c_RatingBar2.getRating() > 0
				&& c_RatingBar1.getRating() > 0) {
			return true;
		}
		Toast.makeText(getActivity(), "请完善评分！", Toast.LENGTH_SHORT);
		return false;

	}

	/**
	 * ================================================广告条
	 */
	private void initImage() {

		// 轮播广告图片。
		imageCycleView.setImageResources(Constants.mImageUrl,
				new ImageCycleView.ImageCycleViewListener() {
					@Override
					public void onImageClick(int position, View imageView) {
					}
				}, 0);
	}

	/**
	 * 初始化控件
	 */
	private void initView(View view) {
		imageCycleView = (ImageCycleView) view.findViewById(R.id.ac_view);
		gridview = (GridView) view.findViewById(R.id.gridview);
		orderListView = (ListView) view.findViewById(R.id.content_view);
		psl = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		order_title = (TextView) view.findViewById(R.id.order_title);
		empty_list_view = (TextView) view.findViewById(R.id.empty_list_view);
		if (!TextUtils.isEmpty(MyApplication.getInstatnce().getUserTypeOid())&&MyApplication.getInstatnce().getUserTypeOid().equals("B") == false) {
			order_title.setText("推荐运单");
		}
		psl.setOnRefreshListener(this);
		gridviewClick();
		gridview.setAdapter(new C_MyGridAdapter(getActivity(), img_text, imgs,
				OrderRow));

		orderListView.setOnItemClickListener(itemClickListener);
		orderListView.setAdapter(orderAdapter);
		orderListView.setEmptyView(empty_list_view);

	}

	// ============================================== listViewItem点击监听
	OnItemClickListener itemClickListener = new OnItemClickListener() {
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
			if (MyApplication.getInstatnce().getUserTypeOid().equals("B") == false) {
				Toast.makeText(getActivity(), "请先完善货主个人信息！", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				Intent intent = null;
				WaitingOrderBaseInfo orderInfoItem = (WaitingOrderBaseInfo) orderAdapter
						.getItem(position);
				if (orderInfoItem != null) {
					if (orderInfoItem.Operate_Code.equals("AA")) {
						intent = new Intent(getActivity(),
								C_OrderInfoWaitingActivity.class);
						intent.putExtra(Constants.OrderKey, orderInfoItem);
						startActivity(intent);
					} else if (orderInfoItem.Operate_Code.equals("AB")
							|| orderInfoItem.Operate_Code.equals("AF")
							|| orderInfoItem.Operate_Code.equals("AC")) {

						intent = new Intent(getActivity(),
								C_OrderInfoReceivedActivity.class);
						intent.putExtra(Constants.OrderKey, orderInfoItem);
						startActivity(intent);

					} else if (orderInfoItem != null
							&& orderInfoItem.Operate_Code.equals("AG")) {
						intent = new Intent(getActivity(),
								C_OrderInfoCompletedActivity.class);
						intent.putExtra(Constants.OrderKey, orderInfoItem);
						startActivity(intent);
					}
				}
			}
		}
	};

	// ============================================================ 评价订单
	private void requestOrderEvaluation() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.OrderEvaluation_URL);
				orderEvaluationInfo.Order_Key = orderInfo.Order_Key;
				orderEvaluationInfo.Serial_Oid = orderInfo.Serial_Oid;
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

	// 确认收货
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
							Message msg = Message.obtain();
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

	@Override
	public void onResume() {
		super.onResume();
		if (!NetWorkUtils.isNetWort(getActivity())) {
			return;
		}
		isForeground = true;
		if (!TextUtils.isEmpty(MyApplication.getInstatnce().getUserTypeOid())&&MyApplication.getInstatnce().getUserTypeOid().equals("B") == false) {
			return;
		}
		initLoadOrderRow();
		
		onfreshen();

	}

	@Override
	public void onDestroy() {
		context.unregisterReceiver(messageReceiver);
		super.onDestroy();
	}

	private void onfreshen() {
		isRefresh = true;

		HomeLoadTime = DateUtil.getCurDateStr();
		pageRequestBean.Page_StartNum = Constants.FindStartNum;
		pageRequestBean.Page_EndNum = Constants.FindNum;
		pageRequestBean.FirstRequestTime = HomeLoadTime;
		waitDialog.show();
		loadHomepageList(Constants.Get_HomepageList_Url);
	}

	/**
	 * gridview点击事件
	 */
	private void gridviewClick() {
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (MyApplication.getInstatnce().getUserTypeOid().equals("B") == false) {
					Toast.makeText(getActivity(), "请先完善货主个人信息！",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!NetWorkUtils.isNetWort(getActivity())) {
					return;
				}

				if (parent.getAdapter().getItemId(position) == 0) {
					// showActivity(getActivity(), PictrueActivity.class);
					showActivity(getActivity(), C_FindCarActivity.class);
				} else if (parent.getAdapter().getItemId(position) == 1) {
					showActivity(getActivity(),
							C_DeliveryGoodsFor2Activity.class);
				} else if (parent.getAdapter().getItemId(position) == 2) {
					showActivity(getActivity(), C_OrderInfoListActivity.class);
				} else if (parent.getAdapter().getItemId(position) == 3) {
					showActivity(getActivity(), C_FollowCarActivity.class);
				}
			}
		});
	}

	public Handler myHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);


			if (assessPopuWindow != null && assessPopuWindow.isShowing()) {
				assessPopuWindow.dismiss();
			}
			if (myFreshHandler != null) {
				myFreshHandler.sendEmptyMessage(0);
			}

			switch (msg.what) {

			case Constants.Asses_Success:
				// RequestCompleteOrder();
				//onfreshen();
				Toast.makeText(getActivity(), "恭喜您！评价成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Asses_Fail:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				Toast.makeText(getActivity(), "评价失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Request_Success:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				List<WaitingOrderBaseInfo> dataList = (List<WaitingOrderBaseInfo>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						isRefresh = false;
						orderDataList.clear();
					}
					orderDataList.addAll(dataList);
					orderAdapter.notifyDataSetChanged();
					
				} else {
					if (isRefresh) {
						isRefresh = false;
						orderDataList.clear();
						orderDataList.addAll(dataList);
						orderAdapter.notifyDataSetChanged();
						return;
					}
					if (isLoad) {
						isLoad = false;
						Toast.makeText(getActivity(), "当前没有查询到信息！",
								Toast.LENGTH_SHORT).show();
					}
				}
				
				break;
			case Constants.Request_NoData:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				Toast.makeText(getActivity(), "当前没有查询到信息！", Toast.LENGTH_SHORT)
						.show();

				break;
			case Constants.Request_Fail:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
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
			case Constants.OrderRow_Success:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				OrderRow = (int) msg.obj;
				gridview.setAdapter(new C_MyGridAdapter(getActivity(),
						img_text, imgs, OrderRow));

				orderListView.setOnItemClickListener(itemClickListener);
				orderListView.setAdapter(orderAdapter);
				break;
			case Constants.Order_jiaojuo_Success:
				
				Toast.makeText(getActivity(), "成功收货！", Toast.LENGTH_SHORT)
				.show();
				onfreshen();
				break;
			default:
				break;
			}
		}
	};

	public void loadHomepageList(final String URL) {
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
					requestData.setDataValue(JsonUtils
							.toJsonData(pageRequestBean));

					params.addBodyParameter(null,
							JsonUtils.toJsonData(requestData));
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
							if (resultBase.isSuccess()) {
								List<WaitingOrderBaseInfo> dataList = JsonUtils
										.parseOrderBeanList(str);
								if (dataList != null) {
									Message msg = Message.obtain();
									msg.what = Constants.Request_Success;
									msg.obj = dataList;
									myHandler.sendMessage(msg);
								} else {
									Message msg = Message.obtain();
									msg.what = Constants.Request_Fail;
									myHandler.sendMessage(msg);
								}
							} else {
								Message msg = Message.obtain();
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
		onfreshen();
//		isRefresh = true;
//		HomeLoadTime = DateUtil.getCurDateStr();
//		pageRequestBean.Page_StartNum = Constants.FindStartNum;
//		pageRequestBean.Page_EndNum = Constants.FindNum;
//		pageRequestBean.FirstRequestTime = HomeLoadTime;
//
//		loadHomepageList(Constants.Get_HomepageList_Url);

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

		pageRequestBean.Page_StartNum = orderDataList.size()
				+ Constants.FindStartNum;
		pageRequestBean.Page_EndNum = orderDataList.size() + Constants.FindNum;
		pageRequestBean.FirstRequestTime = HomeLoadTime;
		isLoad = true;

		loadHomepageList(Constants.Get_HomepageList_Url);

	}

	@Override
	public void onTextViewClick(int position, Object object) {
		orderInfo = (WaitingOrderBaseInfo) object;
		if (orderInfo != null) {
			if (orderInfo.IsCESuccess.equals("1")) {
				RequestCompleteOrder();
				return;
			} else if (MyApplication.getInstatnce().getBasicDataBuffer()
					.getISEvaluate().equals("0")) {
				RequestCompleteOrder();
				return;
			} else if (orderInfo.Operate_Code.equals("AF")) {
				initPopuWindow1();
				return;
			}
		}

	}
}
