/**
 * Administrator2016-5-19
 */
package com.cvnavi.logistics.i51eyun.eh.activity.driver;

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
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationListInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationListInfoRequest;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_MyEvaluateAdapter;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.codehaus.jackson.JsonParseException;
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
 * @date 2016-5-19 下午5:26:54
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_assess)
public class D_AssessActivity extends BaseActivity implements OnRefreshListener {

	@ViewInject(R.id.score_tv)
	private TextView score_tv;

	@ViewInject(R.id.refresh_layout)
	private PullToRefreshLayout refresh_layout;
	@ViewInject(R.id.listview)
	private MyListView listview;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private List<OrderEvaluationListInfo> dataList;
	private D_MyEvaluateAdapter adapter;

	private Context context;
	private AlertDialog.Builder alertDialog;
	private CommonWaitDialog waitDialog;

	private int startNum = Constants.FindStartNum;
	private int findNum = Constants.FindNum;
	private String requestTime = DateUtil.getCurDateStr();
	private boolean isRefresh = false;
	
	private Handler myFreshHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.titlt_textView.setText("我的评价");
		context = this;
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);

		dataList = new ArrayList<>();
		adapter = new D_MyEvaluateAdapter(context, R.layout.d_assess_item, dataList);
		listview.setAdapter(adapter);
		listview.setEmptyView(empty_list_view);

		SetViewValueUtil.setEvaluationScoreValue(score_tv,
				MyApplication.getInstatnce().getLoginData().getEvaluation_Score());
	}

	@Override
	protected void onResume() {
		super.onResume();

		isRefresh = true;
		waitDialog.show();
		loadData();
	}

	@Event(value = { R.id.back_linearLayout }, type = View.OnClickListener.class)
	private void onViewClick(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}

	@Event(value = { R.id.listview }, type = AdapterView.OnItemClickListener.class)
	private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		OrderEvaluationListInfo bean = dataList.get(position);
		if (bean != null) {
			Intent intent = new Intent(context, D_Assess_DedailsActivity.class);
			intent.putExtra(Constants.AssessKey, bean);
			startActivity(intent);
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				refresh_layout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		startNum = Constants.FindStartNum;
		findNum = Constants.FindNum;
		requestTime = DateUtil.getCurDateStr();
		isRefresh = true;

		loadData();

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				refresh_layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		startNum = dataList.size() + Constants.FindStartNum;
		findNum = dataList.size() + Constants.FindNum;
		isRefresh = false;

		loadData();

	}

	private void loadData() {
		new Thread(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Get_OrderEvaluationList_URL);
				try {
					OrderEvaluationListInfoRequest requestBean = new OrderEvaluationListInfoRequest();
					requestBean.Page_StartNum = startNum;
					requestBean.Page_EndNum = findNum;
					requestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(requestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
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
							if (resultBase.isSuccess()) {
								List<OrderEvaluationListInfo> list = JsonUtils
										.parseOrderEvaluationList(String.valueOf(resultBase.getDataValue()));

								Message msg = Message.obtain();
								msg.what = Constants.Request_Success;
								msg.obj = list;

								myHandler.sendMessage(msg);
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
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

	private Handler myHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}
			
			if(myFreshHandler!=null){
				myFreshHandler.sendEmptyMessage(0);
			}

			switch (msg.what) {
			case Constants.Request_Success:
				if (msg.obj != null) {
					if (isRefresh) {
						dataList.clear();
					}
					dataList.addAll((List<OrderEvaluationListInfo>) msg.obj);
					adapter.notifyDataSetChanged();
				}
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_AssessActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
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
}
