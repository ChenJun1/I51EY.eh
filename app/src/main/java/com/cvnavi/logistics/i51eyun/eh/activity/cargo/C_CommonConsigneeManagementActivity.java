package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MDict_FavoriteContacts_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_CommonConsigneeManagementAdapter;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
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
 * @date 2016-5-17 下午1:07:34
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
@ContentView(R.layout.c_common_consignee_management)
public class C_CommonConsigneeManagementActivity extends BaseActivity implements
		OnRefreshListener {

	public static C_CommonConsigneeManagementActivity newInstance() {
		return new C_CommonConsigneeManagementActivity();
	}

	@ViewInject(R.id.c_refresh_fincar_view)
	private PullToRefreshLayout c_refresh_fincar_view;
	
	@ViewInject(R.id.operate_tx)
	private TextView operate_tx;
	
	private AlertDialog.Builder alertDialog;
	
	@ViewInject(R.id.c_common_consignee_management_list)
	private MyListView commonConsigneelist;

	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;
	
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	@ViewInject(R.id.operate_ll)
	private LinearLayout operate_ll;

	@ViewInject(R.id.Common_add_tv)
	private TextView Common_add_tv;

	private List<MDict_FavoriteContacts_Sheet> consigneeList;
	private C_CommonConsigneeManagementAdapter consigneeAdapter;

	private CommonWaitDialog waitDialog;
	private Boolean isRefresh = false;
	private boolean isLoad = false;

	private PagingBean pageBean;
	
	private Handler myFreshHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c_refresh_fincar_view.setOnRefreshListener(this);
		operate_tx.setVisibility(View.GONE);
		init();
	}

	private void loadData() {
		waitDialog.show();
		loadConsigneeRequest();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		isRefresh = true;
		loadData();
	}

	@Event(value = { R.id.Common_add_tv })
	private void onCklick(View v) {
		switch (v.getId()) {
		case R.id.Common_add_tv:
			showActivity(this, C_AddCommonConsigneeActivity.class);
			break;

		default:
			break;
		}
	}

	private void loadConsigneeRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Get_FavoriteContactsList_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(pageBean));

					params.addBodyParameter(null,
							JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						LogUtil.e("-->>请求取消");
					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						LogUtil.e("-->>请求失败");
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj=Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onFinished() {
						LogUtil.e("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.e("-->>请求成功" + str);

						DataResultBase resultBase;
						try {
							resultBase = JsonUtils.parseDataResultBase(str);
							Message message = Message.obtain();
							if (resultBase.isSuccess()) {
								List<MDict_FavoriteContacts_Sheet> dataList = JsonUtils
										.parseFavoriteContactsList(str);

								if (dataList != null) {
									message.what = Constants.Request_Success;
									message.obj = dataList;
									myHandler.sendMessage(message);
								}
							} else {
								message.what = Constants.Request_Fail;
								message.obj=resultBase.getErrorText();
								myHandler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private void init() {
		titlt_textView.setText("常用联系人管理");
		operate_ll.setVisibility(View.VISIBLE);
		Common_add_tv.setVisibility(View.VISIBLE);
		consigneeList = new ArrayList<MDict_FavoriteContacts_Sheet>();

		pageBean = new PagingBean();
		pageBean.Page_StartNum = Constants.FindStartNum;
		pageBean.Page_EndNum = Constants.FindNum;

		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		consigneeAdapter = new C_CommonConsigneeManagementAdapter(
				consigneeList, this);
		commonConsigneelist.setAdapter(consigneeAdapter);
		commonConsigneelist.setEmptyView(empty_list_view);
		commonConsigneelist.setOnItemLongClickListener(myItemLongListener);

		commonConsigneelist.setOnItemClickListener(MyOnclickListener);
		String extra = getIntent().getStringExtra(Constants.commonConsignee);
		if (!TextUtils.isEmpty(extra)
				&& extra.equals(Constants.commonConsignee)) {
			commonConsigneelist.setOnItemClickListener(myItimeListener);
		}
	}

	private OnItemClickListener MyOnclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final TextView tel = (TextView) view
					.findViewById(R.id.c_driver_phoneNum);
			tel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ContextUtil.callAlertDialog(tel.getText().toString(),
							C_CommonConsigneeManagementActivity.this);
				}
			});
		}

	};

	private OnItemLongClickListener myItemLongListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					C_CommonConsigneeManagementActivity.this);
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
					if (consigneeList.get(position) != null) {
						waitDialog.show();
						deleConsigneeItemRequest(consigneeList.get(position));
					}

				}
			});
			builder.create().show();
			return true;
		}
	};

	private OnItemClickListener myItimeListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			MDict_FavoriteContacts_Sheet bean = consigneeList.get(position);
			if (bean != null) {
				CallBackManager.getInstance().getConsigneeCallBackManager()
						.fireCallBack(bean);
				C_CommonConsigneeManagementActivity.this.finish();
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
				List<MDict_FavoriteContacts_Sheet> dataList = (List<MDict_FavoriteContacts_Sheet>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						consigneeList.clear();
						isRefresh = false;
					}
					consigneeList.addAll(dataList);
					consigneeAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						isRefresh = false;
						consigneeList.clear();
						consigneeAdapter.notifyDataSetChanged();
					}

					if (isLoad) {
						Toast.makeText(
								C_CommonConsigneeManagementActivity.this,
								"当前没有查询到信息", Toast.LENGTH_SHORT).show();
						isLoad = false;
					}
				}

				break;
			case Constants.Request_Delete:
				MDict_FavoriteContacts_Sheet consignee_Sheet = (MDict_FavoriteContacts_Sheet) msg.obj;
				consigneeList.remove(consignee_Sheet);
				consigneeAdapter.notifyDataSetChanged();
				Toast.makeText(C_CommonConsigneeManagementActivity.this,
						"删除成功", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_CommonConsigneeManagementActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
			default:
				break;
			}
		}
	};

	/**
	 * 删除
	 */
	private void deleConsigneeItemRequest(
			final MDict_FavoriteContacts_Sheet consignee_Sheet) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Del_FavoriteContacts_URL);
				try {
					String CollectKey = consignee_Sheet.Contacts_Key;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid()); // MyApplication.getInstatnce().getUserTypeOid()
					requestData.setDataValue(CollectKey);

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
						Message message = Message.obtain();
						message.what = Constants.Request_Fail;
						message.obj=Constants.errorMsg;
						myHandler.sendMessage(message);
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						DataResultBase resultBase;
						try {
							resultBase = JsonUtils.parseDataResultBase(str);
							Message message = Message.obtain();
							if (resultBase.isSuccess()) {
								message.what = Constants.Request_Delete;
								message.obj = consignee_Sheet;
								myHandler.sendMessage(message);
							} else {
								message.what = Constants.Request_Fail;
								myHandler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();

	}

	@Event(value = { R.id.back_linearLayout })
	private void dataClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
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
		
		pageBean.Page_StartNum = Constants.FindStartNum;
		pageBean.Page_EndNum = Constants.FindNum;
		isRefresh = true;
		loadConsigneeRequest();
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
		pageBean.Page_StartNum = consigneeList.size() + Constants.FindStartNum;
		pageBean.Page_EndNum = pageBean.Page_StartNum + Constants.FindNum;
		isLoad = true;
		loadConsigneeRequest();
	}
}
