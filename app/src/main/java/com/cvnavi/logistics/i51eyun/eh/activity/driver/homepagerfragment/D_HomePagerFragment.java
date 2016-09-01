package com.cvnavi.logistics.i51eyun.eh.activity.driver.homepagerfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.AdvertPicBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.ConfirmTakeOrCompleteOrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.type.WorkStatusType;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.AdapterItemForTextViewCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_Delivery_GoodsActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_LocationTrackingActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_PickUp_GoodsActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_HomeAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_MyGridAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.findgoods.D_FindGoodsActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_OrderListActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_ReceivedOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_WaitingOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushOrderMessageBean;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.ImageCycleView;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.gridview.MyGridView;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
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
 * @date 2016-5-17 下午1:12:19
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(value = R.layout.d_orders_fragment)
public class D_HomePagerFragment extends BaseFragment implements
		OnRefreshListener, AdapterItemForTextViewCallBack {

	private static final String[] menuStrList = { "我要上班", "我要找货", "运单列表",
			"位置跟踪" };
	private static final int[] menuImgSources = { R.drawable.driver,
			R.drawable.fahuo, R.drawable.order_list, R.drawable.position };

	public static D_HomePagerFragment getInstance() {
		return new D_HomePagerFragment();
	}

	public static boolean isForeground = false;
	public static final String MESSAGE_RECEIVED_ACTION = "com.cvnavi.logistics.i51eyun.eh.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_EXTRAS = "extras";
	private MessageReceiver messageReceiver;

	@ViewInject(value = R.id.gridview)
	private MyGridView gridview;
	@ViewInject(value = R.id.ad_view)
	private ImageCycleView imageCycleView;
	@ViewInject(value = R.id.refresh_view)
	private PullToRefreshLayout pullToRefreshLayout;
	@ViewInject(value = R.id.homepager_Listview)
	private MyListView orderList;
	@ViewInject(value = R.id.empty_list_view)
	private TextView empty_list_view;

	@ViewInject(value = R.id.home_order_title)
	private TextView home_order_title;

	private D_MyGridAdapter myGridAdapter;

	private D_HomeAdapter orderAdapter;
	private List<WaitingOrderBaseInfo> orderInfoList;

	private Context context;
	private CommonWaitDialog waitDialog;

	private int startNum = Constants.FindStartNum;
	private int findNum = Constants.FindNum;
	private String requestTime = DateUtil.getCurDateStr();
	private boolean isRefresh = false;

	private AlertDialog.Builder alertDialog;

	private PopupWindow assesPopupWindow;
	private View assesPopupWindowContentView;
	private RatingBar ratingBar1;
	private RatingBar ratingBar2;
	private RatingBar ratingBar3;
	private TextView fen1;
	private TextView fen2;
	private TextView fen3;
	private EditText assesRemark;
	private Button assesSubmit;

	private OrderEvaluationInfo orderEvaluationInfo;

	private PopupWindow jPushMessagePopuWindow;
	private View jPushMessagePopupWindowContentView;

	private Handler myFreshHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);

		orderInfoList = new ArrayList<WaitingOrderBaseInfo>();
		orderAdapter = new D_HomeAdapter(context, orderInfoList, this);

		orderEvaluationInfo = new OrderEvaluationInfo();
		orderEvaluationInfo.SJPJ_XXZS = "5";
		orderEvaluationInfo.SJPJ_THFB = "5";
		orderEvaluationInfo.SJPJ_HWTD = "5";
		registerMessageReceiver();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pullToRefreshLayout.setOnRefreshListener(this);

		if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData()
				.getWorkStatus()) == false) {
			String workStatusStr = ContextUtil.getWorkStatusStr(Integer
					.valueOf(MyApplication.getInstatnce().getLoginData()
							.getWorkStatus()));
			menuStrList[0] = workStatusStr;
		}
		myGridAdapter = new D_MyGridAdapter(context, menuStrList,
				menuImgSources);
		gridview.setAdapter(myGridAdapter);

		orderList.setAdapter(orderAdapter);

		if (MyApplication.getInstatnce().getUserTypeOid().equals("A")) {
			home_order_title.setText("我的运单");
		} else {
			home_order_title.setText("推荐运单");
		}
		if(Constants.mImageUrl.size()>0){
			initImage();
		}
		orderList.setEmptyView(empty_list_view);
	}

	// 加载广告图
	private void initImage() {
//		 Constants.mImageUrl.add("http://10.10.11.124:8181/ADVIMG/5ef2633f-8137-43b2-96c2-28f00966dd6f.png");
//		 Constants.mImageUrl.add("http://10.10.11.124:8181/ADVIMG/5e4deccd-db26-46db-9e92-6a7a935274ac.png");
//		 Constants.mImageUrl.add("http://10.10.11.124:8181/ADVIMG/e9531e54-82e6-49ca-bc8b-1c877a004798.png");
//		Constants.mImageUrl.add(Constants.imageUrl1);
//		Constants.mImageUrl.add(Constants.imageUrl2);
		// 轮播广告图片。
		imageCycleView.setImageResources(Constants.mImageUrl,
				new ImageCycleView.ImageCycleViewListener() {
					@Override
					public void onImageClick(int position, View imageView) {
						return;
					}
				}, 0);
	};

	@Override
	public void onResume() {
		super.onResume();

		isForeground = true;
		if (!NetWorkUtils.isNetWort(getActivity())) {
			return;
		}

		if (!TextUtils.isEmpty(MyApplication.getInstatnce().getUserTypeOid())&&MyApplication.getInstatnce().getUserTypeOid().equals("A")) {
			loadIsReadOrderRow();
		}

		isRefresh = true;
		startNum = Constants.FindStartNum;
		findNum = Constants.FindNum;
		requestTime = DateUtil.getCurDateStr();

		loadData();
	}

	@Override
	public void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	public void onDestroy() {
		context.unregisterReceiver(messageReceiver);
		super.onDestroy();
	}

	@Event(value = { R.id.gridview, R.id.homepager_Listview }, type = AdapterView.OnItemClickListener.class)
	private void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getAdapter() == null) {
			return;
		}

		if (MyApplication.getInstatnce().getUserTypeOid().equals("A") == false) {
			Toast.makeText(getActivity(), "请先完善司机个人信息！", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (parent.getAdapter() instanceof D_MyGridAdapter) {
			switch (position) {
			case 0: // 我要上班
				if (menuStrList[0].equals("我要下班")) {
					alertDialog = new AlertDialog.Builder(
							getActivity());
					alertDialog.setCancelable(false);
					alertDialog.setTitle("提示");
					alertDialog.setMessage("确定下班吗？");
					alertDialog.setNeutralButton("取消",
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					alertDialog.setPositiveButton("确认",
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

									setWorkStatus();
								}
							});
					alertDialog.show();
				} else {
					setWorkStatus();
				}
				break;
			case 1: // 我要找货
				showActivity((Activity) context, D_FindGoodsActivity.class);
				break;
			case 2: // 订单列表
				showActivity((Activity) context, D_OrderListActivity.class);
				break;
			case 3: // 位置跟踪
				showActivity((Activity) context,
						D_LocationTrackingActivity.class);
				break;
			default:
				break;
			}
		} else if (parent.getAdapter() instanceof D_HomeAdapter) {
			final WaitingOrderBaseInfo orderInfo = (WaitingOrderBaseInfo) orderAdapter
					.getItem(position);
			if (orderInfo == null) {
				Toast.makeText(getActivity(), "运单数据错误！请联系客服！",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (orderInfo.Operate_Code.equals("AA")) { // 待接单。
				final Intent intent = new Intent(getActivity(),
						D_WaitingOrderDetailActivity.class);
				intent.putExtra(Constants.OrderInfo, orderInfo);
				startActivity(intent);
			} else if (orderInfo.Operate_Code.equals("AB")) { // 司机已接单。
				final Intent intent = new Intent(getActivity(),
						D_ReceivedOrderDetailActivity.class);
				intent.putExtra(Constants.OrderInfo, orderInfo);
				startActivity(intent);
			} else if (orderInfo.Operate_Code.equals("AC")) {
				final Intent intentReceived = new Intent(getActivity(),
						D_ReceivedOrderDetailActivity.class);
				intentReceived.putExtra(Constants.OrderInfo, orderInfo);
				startActivity(intentReceived);

			} else if (orderInfo.Operate_Code.equals("AF")) {
				final Intent intentReceived = new Intent(getActivity(),
						D_ReceivedOrderDetailActivity.class);
				intentReceived.putExtra(Constants.OrderInfo, orderInfo);
				startActivity(intentReceived);
			}
		}
	}

	// 交货
	private void requestDeliveryOrder(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Confirm_TakeOrCompleteOrderByDriver_URL);
				try {
					ConfirmTakeOrCompleteOrderRequestBean requestBean = new ConfirmTakeOrCompleteOrderRequestBean();
					requestBean.Order_Key = orderInfo.Order_Key;
					requestBean.Serial_Oid = orderInfo.Serial_Oid;
					requestBean.PhotoStatus = "AF";
					requestBean.Actual_Consignee = orderInfo.Consignee_Name;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setDataValue(JsonUtils.toJsonData(requestBean));

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
								// 发送消息通知。
								msg.what = Constants.Request_OrderCome;
								myHandler.sendMessage(msg);

								startNum = Constants.FindStartNum;
								findNum = Constants.FindNum;
								requestTime = DateUtil.getCurDateStr();
								isRefresh = true;

								loadData();
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

	private void assesPopuWindow(final WaitingOrderBaseInfo orderInfo) {
		if (assesPopupWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
			assesPopupWindowContentView = mLayoutInflater.inflate(
					R.layout.d_assess_popup, null);
			assesPopupWindow = new PopupWindow(assesPopupWindowContentView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		ratingBar1 = ViewHolder.get(assesPopupWindowContentView,
				R.id.c_RatingBar1);
		ratingBar2 = ViewHolder.get(assesPopupWindowContentView,
				R.id.c_RatingBar2);
		ratingBar3 = ViewHolder.get(assesPopupWindowContentView,
				R.id.c_RatingBar3);

		fen1 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen1);
		fen2 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen2);
		fen3 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen3);
		assesRemark = ViewHolder
				.get(assesPopupWindowContentView, R.id.c_remark);
		assesSubmit = ViewHolder
				.get(assesPopupWindowContentView, R.id.c_submit);

		fen1.setText(String.valueOf((int) ratingBar1.getRating()));
		ratingBar1
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						fen1.setText(String.valueOf((int) rating));
						orderEvaluationInfo.SJPJ_XXZS = String
								.valueOf((int) ratingBar.getRating());
					}
				});

		fen2.setText(String.valueOf((int) ratingBar2.getRating()));
		ratingBar2
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						fen2.setText(String.valueOf((int) rating));
						orderEvaluationInfo.SJPJ_THFB = String
								.valueOf((int) ratingBar.getRating());
					}
				});

		fen3.setText(String.valueOf((int) ratingBar3.getRating()));
		ratingBar3
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						fen3.setText(String.valueOf((int) rating));
						orderEvaluationInfo.SJPJ_HWTD = String
								.valueOf((int) ratingBar.getRating());
					}
				});
		assesRemark.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence strValue, int start,
					int before, int count) {
				orderEvaluationInfo.SJPJ_BZ = strValue.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// if (s != null && !s.equals("")) {
				// if (s.length() > 30) {
				// return;
				// }
				// orderEvaluationInfo.SJPJ_BZ = s.toString();
				// }
			}
		});
		assesSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkEvaluation()) {
					requestOrderEvaluation(orderInfo);
					requestDeliveryOrder(orderInfo);
				}
			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		assesPopupWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = 0.4f;
		getActivity().getWindow().setAttributes(lp);

		assesPopupWindow.setOutsideTouchable(true);
		assesPopupWindow.setFocusable(true);
		assesPopupWindow.showAtLocation(assesPopupWindowContentView,
				Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		assesPopupWindow.update();
		assesPopupWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getActivity().getWindow()
						.getAttributes();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
			}
		});
	}

	private boolean checkEvaluation() {
		if (ratingBar1.getRating() > 0 && ratingBar2.getRating() > 0
				&& ratingBar1.getRating() > 0) {
			return true;
		}
		return false;
	}

	// 评价订单
	private void requestOrderEvaluation(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.OrderEvaluation_URL);
				try {
					orderEvaluationInfo.Order_Key = orderInfo.Order_Key;

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
						msg.obj =Constants.errorMsg;
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
								msg.obj =resultBase.getErrorText();
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

	private void setWorkStatus() {
		waitDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final DataRequestData requestData = new DataRequestData();
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUser_Key(MyApplication.getInstatnce()
						.getUserKey());
				requestData.setUserType_Oid(MyApplication.getInstatnce()
						.getUserTypeOid());
				requestData.setDataValue(ContextUtil
						.getWorkStatus(menuStrList[0]));

				RequestParams params = new RequestParams(
						Constants.OnWork_OffWork_Url);
				try {
					String jsonStr = JsonUtils.toJsonData(requestData);
					LogUtil.d("-->>jsonStr" + jsonStr);

					params.addBodyParameter(null, jsonStr); // JsonUtils.toJsonData(requestData)
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
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								MyApplication
										.getInstatnce()
										.getLoginData()
										.setWorkStatus(
												String.valueOf(requestData
														.getDataValue()));
								msg.what = Constants.ChangeWorkStatus_Success;
								msg.obj = requestData.getDataValue();
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.ChangeWorkStatus_Fail;
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

	private void loadData() {
		waitDialog.show();
		new Thread() {
			public void run() {
				RequestParams pans = new RequestParams(
						Constants.Get_HomepageList_Url);

				try {
					JSONObject dataValue = new JSONObject();
					dataValue.put("Page_StartNum", startNum);
					dataValue.put("Page_EndNum", findNum);
					dataValue.put("FirstRequestTime", requestTime);

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce()
							.getCompany_Oid());
					requestData.setDataValue(dataValue.toString());

					pans.addBodyParameter(null,
							JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}

				x.http().post(pans, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String jsonStr) {
						DataResultBase resultBase;
						try {
							resultBase = JsonUtils.parseDataResultBase(jsonStr);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								List<WaitingOrderBaseInfo> dataList = JsonUtils
										.parseOrderBeanList(jsonStr);
								if (dataList == null) {
									dataList = new ArrayList<>();
								}
								msg.what = Constants.Request_Success;
								msg.obj = dataList;
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

					@Override
					public void onError(Throwable ex, boolean b) {
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = ex.getMessage();
						myHandler.sendMessage(msg);
					}

					@Override
					public void onCancelled(CancelledException e) {
					}

					@Override
					public void onFinished() {
					}
				});
			};
		}.start();
	}

	// 获取未读信息数量
	private void loadIsReadOrderRow() {
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
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

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

		isRefresh = true;
		startNum = Constants.FindStartNum;
		findNum = Constants.FindNum;
		requestTime = DateUtil.getCurDateStr();

		loadData();

	}

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

		startNum = orderInfoList.size() + Constants.FindStartNum;
		findNum = orderInfoList.size() + Constants.FindNum;
		isRefresh = false;

		loadData();

	}

	private Handler myHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			if (assesPopupWindow != null) {
				assesPopupWindow.dismiss();
			}
			if (myFreshHandler != null) {
				myFreshHandler.sendEmptyMessage(0);
			}

			switch (msg.what) {
			case Constants.Request_Success:
				List<WaitingOrderBaseInfo> dataList = (List<WaitingOrderBaseInfo>) msg.obj;
				if (isRefresh) {
					orderInfoList.clear();
					// orderAdapter.notifyDataSetInvalidated();
				}

				orderInfoList.addAll(dataList);
				orderAdapter.notifyDataSetInvalidated();
				break;
			case Constants.ChangeWorkStatus_Success:
				if (menuStrList[0].equals("我要上班")) {
					menuStrList[0] = "我要下班";
				} else if (menuStrList[0].equals("我要下班")) {
					menuStrList[0] = "我要上班";
				}
				myGridAdapter.notifyDataSetChanged();
				break;
			case Constants.OrderRow_Success:
				if (msg.obj != null) {
					myGridAdapter = new D_MyGridAdapter(context, menuStrList,
							menuImgSources);
					myGridAdapter.setIsReadOrderRow(Integer.valueOf(String
							.valueOf(msg.obj)));
					gridview.setAdapter(myGridAdapter);
					// int i=Integer.valueOf(String.valueOf(msg.obj));
					// myGridAdapter.notifyDataSetChanged();
				}
				break;
			case Constants.Request_OrderCome:
				Toast.makeText(getActivity(), "恭喜您！交货成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Asses_Success:
				Toast.makeText(getActivity(), "恭喜您！评价成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.Asses_Fail:
				Toast.makeText(getActivity(), "评价失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.ChangeWorkStatus_Fail:
				if (msg.obj == null) {
					Toast.makeText(getActivity(), "请求服务错误！请联系客服！",
							Toast.LENGTH_SHORT).show();
					break;
				} else {
					Toast.makeText(getActivity(),
							"请求服务错误！请联系客服！" + String.valueOf(msg.obj),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(getActivity(), Constants.NoDataMsg,
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			case Constants.Request_LoadImg:
				List<AdvertPicBean> picList = (List<AdvertPicBean>) msg.obj;
				if (picList.size() > 0) {
					 for(int i=0;i<picList.size();i++){
					 Constants.mImageUrl.add(picList.get(i).AdvertPic);
					 }
				}
				break;
			default:
				break;
			}
		}

	};

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				JPushOrderMessageBean messageBean = (JPushOrderMessageBean) intent
						.getSerializableExtra(KEY_EXTRAS);
				if (messageBean != null) {
					// if (jPushMessagePopuWindow != null) {
					// jPushMessagePopuWindow.dismiss();
					// }

					pushMessage(messageBean.Message_Content);
				}
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

	private void pushMessage(String pushMessageInfo) {
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
		data_text_tv.setText(pushMessageInfo);
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

	@Override
	public void onTextViewClick(int position, Object object) {
		if (object != null) {
			WaitingOrderBaseInfo orderInfo = (WaitingOrderBaseInfo) object;
			if (orderInfo.Operate_Code.equals("AB")) {
				if (TextUtils.isEmpty(orderInfo.IsTHPhoto) == false
						&& orderInfo.IsTHPhoto.equals("1")) {
					// Toast.makeText(context, "AB提货！",
					// Toast.LENGTH_SHORT).show();

					if (TextUtils.isEmpty(MyApplication.getInstatnce()
							.getLoginData().getWorkStatus()) == false
							&& Integer.valueOf(MyApplication.getInstatnce()
									.getLoginData().getWorkStatus()) == WorkStatusType.OffWork) {
						alertDialog = new AlertDialog.Builder(
								context);
						alertDialog.setCancelable(false);
						alertDialog.setTitle("提示");
						alertDialog.setMessage("您还未上班，不能提货！");
						alertDialog.setPositiveButton("确认",
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alertDialog.show();
						return;
					}

					final Intent intentTH = new Intent(context,
							D_PickUp_GoodsActivity.class); // 提货界面。
					intentTH.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intentTH);
					return;
				} else if (TextUtils.isEmpty(orderInfo.IsTHPhoto)
						|| orderInfo.IsTHPhoto.equals("0")) {

					if (TextUtils.isEmpty(orderInfo.IsJHPhoto) == false
							&& orderInfo.IsJHPhoto.equals("1")) {
						// Toast.makeText(getActivity(), "AB交货需要拍照！",
						// Toast.LENGTH_SHORT).show();

						if (TextUtils.isEmpty(MyApplication.getInstatnce()
								.getLoginData().getWorkStatus()) == false
								&& Integer.valueOf(MyApplication.getInstatnce()
										.getLoginData().getWorkStatus()) == WorkStatusType.OffWork) {
							alertDialog = new AlertDialog.Builder(
									getActivity());
							alertDialog.setCancelable(false);
							alertDialog.setTitle("提示");
							alertDialog.setMessage("您还未上班，不能交货！");
							alertDialog.setPositiveButton("确认",
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							alertDialog.show();
							return;
						}

						final Intent intentJH = new Intent(getActivity(),
								D_Delivery_GoodsActivity.class); // 交货界面。
						intentJH.putExtra(Constants.OrderInfo, orderInfo);
						startActivity(intentJH);
						return;
					} else if (TextUtils.isEmpty(orderInfo.IsJHPhoto)
							|| orderInfo.IsJHPhoto.equals("0")) {
						// Toast.makeText(getActivity(), "AB交货不需要拍照,直接评论！",
						// Toast.LENGTH_SHORT).show();

						if (TextUtils.isEmpty(MyApplication.getInstatnce()
								.getLoginData().getWorkStatus()) == false
								&& Integer.valueOf(MyApplication.getInstatnce()
										.getLoginData().getWorkStatus()) == WorkStatusType.OffWork) {
							alertDialog = new AlertDialog.Builder(
									getActivity());
							alertDialog.setCancelable(false);
							alertDialog.setTitle("提示");
							alertDialog.setMessage("您还未上班，不能评价！");
							alertDialog.setPositiveButton("确认",
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							alertDialog.show();
							return;
						}

						// 未开启评价功能的，直接交货。
						if (TextUtils.isEmpty(MyApplication.getInstatnce()
								.getBasicDataBuffer().getISEvaluate())
								|| MyApplication.getInstatnce()
										.getBasicDataBuffer().getISEvaluate()
										.equals("0")) {
							requestDeliveryOrder(orderInfo);
						} else if (MyApplication.getInstatnce()
								.getBasicDataBuffer().getISEvaluate()
								.equals("1")) {
							// 评价成功的，不能重复评价。直接交货。
							if (TextUtils.isEmpty(orderInfo.IsDESuccess)
									|| orderInfo.IsDESuccess.equals("0")) {
								assesPopuWindow(orderInfo);
							} else if (orderInfo.IsDESuccess.equals("1")) {
								requestDeliveryOrder(orderInfo);
							}
						}
					}
					return;
				}
			} else if (orderInfo.Operate_Code.equals("AC")) {
				if (TextUtils.isEmpty(orderInfo.IsJHPhoto) == false
						&& orderInfo.IsJHPhoto.equals("1")) {
					// Toast.makeText(getActivity(), "AC交货需要拍照！",
					// Toast.LENGTH_SHORT).show();

					if (TextUtils.isEmpty(MyApplication.getInstatnce()
							.getLoginData().getWorkStatus()) == false
							&& Integer.valueOf(MyApplication.getInstatnce()
									.getLoginData().getWorkStatus()) == WorkStatusType.OffWork) {
						alertDialog = new AlertDialog.Builder(
								getActivity());
						alertDialog.setCancelable(false);
						alertDialog.setTitle("提示");
						alertDialog.setMessage("您还未上班，不能交货！");
						alertDialog.setPositiveButton("确认",
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alertDialog.show();
						return;
					}

					final Intent intentJH = new Intent(getActivity(),
							D_Delivery_GoodsActivity.class); // 交货界面。
					intentJH.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intentJH);

				} else if (TextUtils.isEmpty(orderInfo.IsJHPhoto)
						|| orderInfo.IsJHPhoto.equals("0")) {
					// Toast.makeText(getActivity(), "AC交货不需要拍照,直接评论！",
					// Toast.LENGTH_SHORT).show();

					if (TextUtils.isEmpty(MyApplication.getInstatnce()
							.getLoginData().getWorkStatus()) == false
							&& Integer.valueOf(MyApplication.getInstatnce()
									.getLoginData().getWorkStatus()) == WorkStatusType.OffWork) {
						alertDialog = new AlertDialog.Builder(
								getActivity());
						alertDialog.setCancelable(false);
						alertDialog.setTitle("提示");
						alertDialog.setMessage("您还未上班，不能评价！");
						alertDialog.setPositiveButton("确认",
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alertDialog.show();
						return;
					}

					// 未开启评价功能的，直接交货。
					if (TextUtils.isEmpty(MyApplication.getInstatnce()
							.getBasicDataBuffer().getISEvaluate())
							|| MyApplication.getInstatnce()
									.getBasicDataBuffer().getISEvaluate()
									.equals("0")) {
						requestDeliveryOrder(orderInfo);
					} else if (MyApplication.getInstatnce()
							.getBasicDataBuffer().getISEvaluate().equals("1")) {
						// 评价成功的，不能重复评价。直接交货。
						if (TextUtils.isEmpty(orderInfo.IsDESuccess)
								|| orderInfo.IsDESuccess.equals("0")) {
							assesPopuWindow(orderInfo);
						} else if (orderInfo.IsDESuccess.equals("1")) {
							requestDeliveryOrder(orderInfo);
						}
					}
				}
				return;
			}
		}
	}

}
