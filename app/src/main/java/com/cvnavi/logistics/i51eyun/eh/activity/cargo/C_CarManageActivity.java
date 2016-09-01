/**
 * Administrator2016-4-25
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MDict_CarCode_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_CarManageAdapter;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.codehaus.jackson.JsonGenerationException;
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
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:07:29
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_activity_car_manage)
public class C_CarManageActivity extends BaseActivity implements
		OnRefreshListener {
	private final static String TAG = "C_CarManageActivity";

	public static C_CarManageActivity newInstance() {
		return new C_CarManageActivity();
	}

	@ViewInject(R.id.c_car_listview)
	private MyListView carListView;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout backLinearLayout;

	@ViewInject(R.id.c_refresh_fincar_view)
	private PullToRefreshLayout c_refresh_fincar_view;
	
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	public static boolean isRefresh = false;
	private boolean isLoad=false;
	
	private AlertDialog.Builder alertDialog;
	
	private PagingBean pageBean;

	private C_CarManageAdapter carManageAdapter;

	private List<MDict_CarCode_Sheet> carList;
	private Context mContext;
	private CommonWaitDialog waitDialog;
	
	private Handler myFreshHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		c_refresh_fincar_view.setOnRefreshListener(this);
		// 初始化
		initView();
		// 初始化列表
		initLoad();

	}

	/**
	 * 加载常用车辆
	 */
	private void initLoad() {
		waitDialog.show();
		loadHDeriverList();
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
	}

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
						isRefresh=false;
						carList.clear();
					}
					carList.addAll(dataList);
					carManageAdapter.notifyDataSetChanged();
				}else {
					if(isRefresh){
						isRefresh = false;
						carList.clear();
						carManageAdapter.notifyDataSetChanged();
					}
					
					if(isLoad){
						Toast.makeText(C_CarManageActivity.this, "当前没有查询到信息!", Toast.LENGTH_SHORT)
							.show();
						isLoad=false;
					}
			}
				break;
			case Constants.Request_Delete:
				MDict_CarCode_Sheet mCollect = (MDict_CarCode_Sheet) msg.obj;
				carList.remove(mCollect);
				carManageAdapter.notifyDataSetChanged();
				Toast.makeText(C_CarManageActivity.this, "删除成功!",
						Toast.LENGTH_SHORT).show();
				break;

			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_CarManageActivity.this);
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

	/**
	 * 加载常用车辆
	 */
	private void loadHDeriverList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Get_DeriverList_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid()); // MyApplication.getInstatnce().getUserTypeOid()
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(pageBean));

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

						List<MDict_CarCode_Sheet> dataList;
						try {
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message message = Message.obtain();
							if (resultBase.isSuccess()) {
								dataList = (List<MDict_CarCode_Sheet>) JsonUtils
										.parseCommonlyCarList(String.valueOf(resultBase.getDataValue()));
								if (dataList != null) {
									message.obj = dataList;
									message.what = Constants.Request_Success;
									myHandler.sendMessage(message);
								}else{
									message.obj = dataList;
									message.what = Constants.Request_Success;
									myHandler.sendMessage(message);
								}
							} else {
								message.what = Constants.Request_Fail;
								message.obj=resultBase.getErrorText();
								myHandler.sendMessage(message);
								LogUtil.e("-->>" + resultBase.getErrorText());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private void initView() {

		titlt_textView.setText("常用车辆管理");
		carList = new ArrayList<MDict_CarCode_Sheet>();
		pageBean = new PagingBean();
		pageBean.Page_StartNum =Constants.FindStartNum;
		pageBean.Page_EndNum =Constants.FindNum;

		carManageAdapter = new C_CarManageAdapter(carList, mContext);
		waitDialog = new CommonWaitDialog(mContext, R.style.progress_dialog);
		carListView.setAdapter(carManageAdapter);
		carListView.setEmptyView(empty_list_view);
		carListView.setOnItemLongClickListener(MyItemListener);
		String extra = getIntent().getStringExtra(
				Constants.DeliveryGoods_FindCar);
		if (!TextUtils.isEmpty(extra)
				&& extra.equals(Constants.DeliveryGoods_FindCar)) {
			carListView.setOnItemClickListener(myItemCListener);
		}

	}

  private OnItemClickListener myItemCListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (carList.get(position) != null) {
				MDict_CarCode_Sheet bean = carList.get(position);
				CallBackManager.getInstance().getCarCallBackManager()
						.fireCallBack(bean);
				C_CarManageActivity.this.finish();
			}
		}
	};
	
	private OnItemLongClickListener MyItemListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("提示");
			builder.setMessage("确认删除?");
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				}
			});
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					if (carList.get(position) != null) {
						DeleteDeriverList(carList.get(position));
					}

				}
			});
			builder.create().show();
			return true;
		}
	};

	/*OnItemLongClickListener MyItemListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			new AlertDialog(mContext).builder().setTitle("提示")
					.setMsg("确定要删除该车主吗？")
					.setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).setPositiveButton("确定", new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (carList.get(position) != null) {
								DeleteDeriverList(carList.get(position));
							}
						}
					}).show();
			return false;
		}
	};*/

	@Event(value = { R.id.back_linearLayout }, type = View.OnClickListener.class)
	private void myOnClick(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}

	private void DeleteDeriverList(final MDict_CarCode_Sheet mCollect) {
		waitDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Delete_DeriverList_URL);
				try {
					String CollectKey = mCollect.CarCode_Key;

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
								message.obj = mCollect;
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
		pageBean.Page_EndNum =Constants.FindNum;
		isRefresh = true;
		loadHDeriverList();

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
			return;
		}
	
		pageBean.Page_StartNum = carList.size() + Constants.FindStartNum;
		pageBean.Page_EndNum = pageBean.Page_StartNum + Constants.FindNum;
		isLoad=true;
		loadHDeriverList();

	}
}
