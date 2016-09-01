package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment;

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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.ConfirmTakeOrCompleteOrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.type.WorkStatusType;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.AdapterItemForTextViewCallBack;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_Delivery_GoodsActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_PickUp_GoodsActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_ReceivedOrderListAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_ReceivedOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.presenter.OrderPresenter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.view.OrderView;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
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
 * Created by Wangshy on 2016/4/22. 已接订单
 */

@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_receivedfragment)
public class ReceivedOrderFragment extends BaseFragment
		implements PullToRefreshLayout.OnRefreshListener, OrderView, AdapterItemForTextViewCallBack {

	public static ReceivedOrderFragment getFragment() {
		return new ReceivedOrderFragment();
	}

	@ViewInject(R.id.c_refresh_received_view)
	private PullToRefreshLayout c_refresh_received_view;
	@ViewInject(R.id.c_received_order_lv)
	private MyListView c_received_order_lv;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private D_ReceivedOrderListAdapter receivedOrderAdapter;
	private List<WaitingOrderBaseInfo> receivedOrderList;

	private OrderPresenter orderPresenter;

	private Context context;
	private CommonWaitDialog waitDialog;
	private AlertDialog.Builder alertDialog;

	private int startNum = Constants.FindStartNum;
	private int findNum = Constants.FindNum;
	private String requestTime = DateUtil.getCurDateStr();

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
	private Handler myFreshHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity();
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);

		orderPresenter = new OrderPresenter(ReceivedOrderFragment.this);

		orderEvaluationInfo = new OrderEvaluationInfo();
		orderEvaluationInfo.SJPJ_XXZS = "5";
		orderEvaluationInfo.SJPJ_THFB = "5";
		orderEvaluationInfo.SJPJ_HWTD = "5";

		receivedOrderList = new ArrayList<>();
		receivedOrderAdapter = new D_ReceivedOrderListAdapter(context, receivedOrderList, this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		c_refresh_received_view.setOnRefreshListener(this);
		c_received_order_lv.setAdapter(receivedOrderAdapter);
		c_received_order_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				final WaitingOrderBaseInfo orderInfo = receivedOrderList.get(position);
				if (orderInfo == null) {
					Toast.makeText(getActivity(), "运单数据错误！请联系客服！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (orderInfo.Operate_Code.equals("AB")) {
					final Intent intent = new Intent(getActivity(), D_ReceivedOrderDetailActivity.class);
					intent.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intent);
				} else if (orderInfo.Operate_Code.equals("AC")) {
					final Intent intentReceived = new Intent(getActivity(), D_ReceivedOrderDetailActivity.class);
					intentReceived.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intentReceived);
				} else if (orderInfo.Operate_Code.equals("AF")) {
					final Intent intentReceived = new Intent(getActivity(), D_ReceivedOrderDetailActivity.class);
					intentReceived.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intentReceived);
				}
			}
		});
		c_received_order_lv.setEmptyView(empty_list_view);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(getActivity())){
			return;
		}
		if (this.getView().getVisibility() == View.VISIBLE) {
			orderPresenter.getReceivedRefresh();
		}
	}

	// 交货。
	private void requestDeliveryOrder(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.Confirm_TakeOrCompleteOrderByDriver_URL);
				try {
					ConfirmTakeOrCompleteOrderRequestBean requestBean = new ConfirmTakeOrCompleteOrderRequestBean();
					requestBean.Order_Key = orderInfo.Order_Key;
					requestBean.Serial_Oid = orderInfo.Serial_Oid;
					requestBean.PhotoStatus = "AF";
					requestBean.Actual_Consignee = orderInfo.Consignee_Name;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
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
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								// 发送消息通知。
								msg.what = Constants.Request_OrderCome;
								myHandler.sendMessage(msg);

								orderPresenter.getReceivedRefresh();
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
			assesPopupWindowContentView = mLayoutInflater.inflate(R.layout.d_assess_popup, null);
			assesPopupWindow = new PopupWindow(assesPopupWindowContentView, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		ratingBar1 = ViewHolder.get(assesPopupWindowContentView, R.id.c_RatingBar1);
		ratingBar2 = ViewHolder.get(assesPopupWindowContentView, R.id.c_RatingBar2);
		ratingBar3 = ViewHolder.get(assesPopupWindowContentView, R.id.c_RatingBar3);

		fen1 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen1);
		fen2 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen2);
		fen3 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen3);
		assesRemark = ViewHolder.get(assesPopupWindowContentView, R.id.c_remark);
		assesSubmit = ViewHolder.get(assesPopupWindowContentView, R.id.c_submit);

		fen1.setText(String.valueOf((int) ratingBar1.getRating()));
		ratingBar1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				fen1.setText(String.valueOf((int) rating));
				orderEvaluationInfo.SJPJ_XXZS = String.valueOf((int) ratingBar.getRating());
			}
		});
		fen2.setText(String.valueOf((int) ratingBar2.getRating()));
		ratingBar2.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				fen2.setText(String.valueOf((int) rating));
				orderEvaluationInfo.SJPJ_THFB = String.valueOf((int) ratingBar.getRating());
			}
		});
		fen3.setText(String.valueOf((int) ratingBar3.getRating()));
		ratingBar3.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				fen3.setText(String.valueOf((int) rating));
				orderEvaluationInfo.SJPJ_HWTD = String.valueOf((int) ratingBar.getRating());
			}
		});
		assesRemark.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence strValue, int start, int before, int count) {
				orderEvaluationInfo.SJPJ_BZ = strValue.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = 0.4f;
		getActivity().getWindow().setAttributes(lp);

		assesPopupWindow.setOutsideTouchable(true);
		assesPopupWindow.setFocusable(true);
		assesPopupWindow.showAtLocation(assesPopupWindowContentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		assesPopupWindow.update();
		assesPopupWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
			}
		});
	}

	private boolean checkEvaluation() {
		if (ratingBar1.getRating() > 0 && ratingBar2.getRating() > 0 && ratingBar1.getRating() > 0) {
			return true;
		}
		return false;
	}

	// 评价订单
	private void requestOrderEvaluation(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.OrderEvaluation_URL);
				try {
					orderEvaluationInfo.Order_Key = orderInfo.Order_Key;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderEvaluationInfo));

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
						msg.obj = TextUtils.isEmpty(ex.getMessage()) == true ? "请求超时！请联系客服！" : ex.getMessage();
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
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								msg.what = Constants.Asses_Success;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Asses_Fail;
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

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}
			if(myFreshHandler!=null){
				myFreshHandler.sendEmptyMessage(0);
			}
			
			if (assesPopupWindow != null) {
				assesPopupWindow.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_OrderCome:
				Toast.makeText(getActivity(), "恭喜您！交货成功！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Asses_Success:
				Toast.makeText(getActivity(), "恭喜您！评价成功！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Asses_Fail:
				Toast.makeText(getActivity(), "评价失败！", Toast.LENGTH_SHORT).show();
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
			default:
				break;
			}
		};
	};

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		
		
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				receivedOrderAdapter.notifyDataSetChanged();
			}
		};
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		orderPresenter.getReceivedLoading();

	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

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
		orderPresenter.getReceivedRefresh();
	}

	@Override
	public void showLoading() {
		if (this.getView().getVisibility() == View.VISIBLE) {
			waitDialog.show();
		}
	}

	@Override
	public void hideLoading() {
		waitDialog.dismiss();
		if(myFreshHandler!=null){
			myFreshHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void toError(String error) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("提示");
		alertDialog.setMessage(error);
		alertDialog.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		alertDialog.create().show();
	}

	@Override
	public void toWaitingRefresh(List<WaitingOrderBaseInfo> dataList) {
	}

	@Override
	public void toWaitingLoading(List<WaitingOrderBaseInfo> dataList) {
	}

	@Override
	public void toCompletedLoading(List<WaitingOrderBaseInfo> dataList) {
	}

	@Override
	public void toCompletedRefresh(List<WaitingOrderBaseInfo> dataList) {
	}

	@Override
	public void toReceivedLoading(List<WaitingOrderBaseInfo> dataList) {
		if (dataList != null && dataList.size() > 0) {
			this.receivedOrderList.addAll(dataList);

			receivedOrderAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(getActivity(),Constants.NoDataMsg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void toReceivedRefresh(List<WaitingOrderBaseInfo> dataList) {
		if (dataList != null) {
			receivedOrderList.clear();
			receivedOrderList.addAll(dataList);

			receivedOrderAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void toDelete() {
	}

	@Override
	public int getPageStartNum(boolean isRefresh) {
		if (isRefresh) {
			startNum = Constants.FindStartNum;
			return startNum;
		} else {
			startNum = receivedOrderList.size() + startNum;
		}
		return startNum;
	}

	@Override
	public int getPageEndNum(boolean isRefresh) {
		if (isRefresh) {
			findNum = Constants.FindNum;
			return findNum;
		} else {
			findNum = receivedOrderList.size() + Constants.FindNum;
		}
		return findNum;
	}

	@Override
	public String getOperateCode() {
		return "AB";
	}

	@Override
	public String getTime(boolean isRefresh) {
		if (isRefresh) {
			requestTime = DateUtil.getCurDateStr();
			return requestTime;
		}
		return requestTime;
	}

	@Override
	public void onTextViewClick(int position, Object object) {
		if (object != null) {
			WaitingOrderBaseInfo orderInfo = (WaitingOrderBaseInfo) object;
			if (orderInfo.Operate_Code.equals("AB")) {
				if (TextUtils.isEmpty(orderInfo.IsTHPhoto) == false && orderInfo.IsTHPhoto.equals("1")) {
					// Toast.makeText(context, "AB提货！",
					// Toast.LENGTH_SHORT).show();

					if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == false
							&& Integer.valueOf(MyApplication.getInstatnce().getLoginData()
									.getWorkStatus()) == WorkStatusType.OffWork) {
						alertDialog = new AlertDialog.Builder(context);
						alertDialog.setCancelable(false);
						alertDialog.setTitle("提示");
						alertDialog.setMessage("您还未上班，不能提货！");
						alertDialog.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						alertDialog.show();
						return;
					}

					final Intent intentTH = new Intent(context, D_PickUp_GoodsActivity.class); // 提货界面。
					intentTH.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intentTH);
					return;
				} else if (TextUtils.isEmpty(orderInfo.IsTHPhoto) || orderInfo.IsTHPhoto.equals("0")) {

					if (TextUtils.isEmpty(orderInfo.IsJHPhoto) == false && orderInfo.IsJHPhoto.equals("1")) {
						// Toast.makeText(getActivity(), "AB交货需要拍照！",
						// Toast.LENGTH_SHORT).show();

						if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == false
								&& Integer.valueOf(MyApplication.getInstatnce().getLoginData()
										.getWorkStatus()) == WorkStatusType.OffWork) {
							alertDialog = new AlertDialog.Builder(getActivity());
							alertDialog.setCancelable(false);
							alertDialog.setTitle("提示");
							alertDialog.setMessage("您还未上班，不能交货！");
							alertDialog.setPositiveButton("确认", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							alertDialog.show();
							return;
						}

						final Intent intentJH = new Intent(getActivity(), D_Delivery_GoodsActivity.class); // 交货界面。
						intentJH.putExtra(Constants.OrderInfo, orderInfo);
						startActivity(intentJH);
						return;
					} else if (TextUtils.isEmpty(orderInfo.IsJHPhoto) || orderInfo.IsJHPhoto.equals("0")) {
						// Toast.makeText(getActivity(), "AB交货不需要拍照,直接评论！",
						// Toast.LENGTH_SHORT).show();

						if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == false
								&& Integer.valueOf(MyApplication.getInstatnce().getLoginData()
										.getWorkStatus()) == WorkStatusType.OffWork) {
							alertDialog = new AlertDialog.Builder(getActivity());
							alertDialog.setCancelable(false);
							alertDialog.setTitle("提示");
							alertDialog.setMessage("您还未上班，不能评价！");
							alertDialog.setPositiveButton("确认", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							alertDialog.show();
							return;
						}

						// 未开启评价功能的，直接交货。
						if (TextUtils.isEmpty(MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate())
								|| MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate().equals("0")) {
							requestDeliveryOrder(orderInfo);
						} else if (MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate().equals("1")) {
							// 评价成功的，不能重复评价。直接交货。
							if (TextUtils.isEmpty(orderInfo.IsDESuccess) || orderInfo.IsDESuccess.equals("0")) {
								assesPopuWindow(orderInfo);
							} else if (orderInfo.IsDESuccess.equals("1")) {
								requestDeliveryOrder(orderInfo);
							}
						}
					}
					return;
				}
			} else if (orderInfo.Operate_Code.equals("AC")) {
				if (TextUtils.isEmpty(orderInfo.IsJHPhoto) == false && orderInfo.IsJHPhoto.equals("1")) {
					// Toast.makeText(getActivity(), "AC交货需要拍照！",
					// Toast.LENGTH_SHORT).show();

					if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == false
							&& Integer.valueOf(MyApplication.getInstatnce().getLoginData()
									.getWorkStatus()) == WorkStatusType.OffWork) {
						alertDialog = new AlertDialog.Builder(getActivity());
						alertDialog.setCancelable(false);
						alertDialog.setTitle("提示");
						alertDialog.setMessage("您还未上班，不能交货！");
						alertDialog.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						alertDialog.show();
						return;
					}

					final Intent intentJH = new Intent(getActivity(), D_Delivery_GoodsActivity.class); // 交货界面。
					intentJH.putExtra(Constants.OrderInfo, orderInfo);
					startActivity(intentJH);

				} else if (TextUtils.isEmpty(orderInfo.IsJHPhoto) || orderInfo.IsJHPhoto.equals("0")) {
					// Toast.makeText(getActivity(), "AC交货不需要拍照,直接评论！",
					// Toast.LENGTH_SHORT).show();

					if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getWorkStatus()) == false
							&& Integer.valueOf(MyApplication.getInstatnce().getLoginData()
									.getWorkStatus()) == WorkStatusType.OffWork) {
						alertDialog = new AlertDialog.Builder(getActivity());
						alertDialog.setCancelable(false);
						alertDialog.setTitle("提示");
						alertDialog.setMessage("您还未上班，不能评价！");
						alertDialog.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						alertDialog.show();
						return;
					}

					// 未开启评价功能的，直接交货。
					if (TextUtils.isEmpty(MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate())
							|| MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate().equals("0")) {
						requestDeliveryOrder(orderInfo);
					} else if (MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate().equals("1")) {
						// 评价成功的，不能重复评价。直接交货。
						if (TextUtils.isEmpty(orderInfo.IsDESuccess) || orderInfo.IsDESuccess.equals("0")) {
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