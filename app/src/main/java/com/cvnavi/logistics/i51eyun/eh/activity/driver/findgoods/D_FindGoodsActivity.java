package com.cvnavi.logistics.i51eyun.eh.activity.driver.findgoods;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.findgoods.FindGoodsRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_FindGoodsAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_WaitingOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
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
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.activity_find_goods)
public class D_FindGoodsActivity extends BaseActivity implements
		OnRefreshListener, RegionDataCallBack2 {

	@ViewInject(R.id.operation_btn)
	private Button operation_btn;

	@ViewInject(R.id.c_Shipping_City_et)
	private EditText c_Shipping_City_et;
	@ViewInject(R.id.c_Consignee_City_et)
	private EditText c_Consignee_City_et;
	@ViewInject(R.id.c_select_btn)
	private Button c_select_btn;

	@ViewInject(R.id.d_find_goods_lv)
	private MyListView listView;
	@ViewInject(R.id.d_refresh_findgoods_view)
	private PullToRefreshLayout refreshLayout;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private List<WaitingOrderBaseInfo> dataList;
	private D_FindGoodsAdapter adapter;

	private CommonWaitDialog waitDialog;
	private AlertDialog.Builder alertDialog;

	private boolean isRefresh = false;
	private boolean isCheck_StartAddress = false;
	private boolean isCheck_EndAddress = false;
	private FindGoodsRequestBean findGoodsRequestBean;

	private RegionBean startRegionBean;
	private RegionBean endRegionBean;

	private Context context;

	private boolean isOnfreshen = true;

	private Handler myFreshHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		CallBackManager.getInstance().getRegionCallBackManager2()
				.addCallBack(this);
		findGoodsRequestBean = new FindGoodsRequestBean();

		findGoodsRequestBean.FirstRequestTime = DateUtil.getCurDateStr();
		findGoodsRequestBean.Page_StartNum = Constants.FindStartNum;
		findGoodsRequestBean.Page_EndNum = Constants.FindNum;

		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}

		if (isOnfreshen) {
			isOnfreshen = true;
			isRefresh = true;
			findGoodsRequestBean.Page_StartNum = Constants.FindStartNum;
			findGoodsRequestBean.Page_EndNum = Constants.FindNum;
			findGoodsRequestBean.FirstRequestTime = DateUtil.getCurDateStr();

			waitDialog.show();
			findGoods();
		}
	}

	@Override
	protected void onDestroy() {
		CallBackManager.getInstance().getRegionCallBackManager2()
				.removeCallBack(this);
		super.onDestroy();
	}

	private void initView() {
		this.titlt_textView.setText("我要找货");
		operation_btn.setText("刷新");
		operation_btn.setVisibility(View.VISIBLE);
		refreshLayout.setOnRefreshListener(this);

		dataList = new ArrayList<>();
		adapter = new D_FindGoodsAdapter(this, R.layout.d_order_item, dataList);
		listView.setAdapter(adapter);
		listView.setEmptyView(empty_list_view);
	}

	@Event(value = { R.id.back_linearLayout, R.id.operation_btn,
			R.id.c_Shipping_City_et, R.id.c_Consignee_City_et,
			R.id.c_select_btn }, type = View.OnClickListener.class)
	private void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.operation_btn:
			alertDialog = new AlertDialog.Builder(context);
			alertDialog.setCancelable(false);
			alertDialog.setTitle("提示");
			alertDialog.setMessage("确定刷新吗！");
			alertDialog.setPositiveButton("确认",
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							c_Shipping_City_et.setText(null);
							c_Consignee_City_et.setText(null);
							startRegionBean = null;
							endRegionBean = null;
							isRefresh = true;
							findGoodsRequestBean.Page_StartNum = Constants.FindStartNum;
							findGoodsRequestBean.Page_EndNum = Constants.FindNum;
							findGoodsRequestBean.FirstRequestTime = DateUtil
									.getCurDateStr();

							waitDialog.show();

							findGoods();
						}
					});
			alertDialog.setNegativeButton("取消",
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.show();
			break;
		case R.id.c_Shipping_City_et:
			isCheck_StartAddress = true;
			isCheck_EndAddress = false;
			isOnfreshen = false;
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key,
					Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_Consignee_City_et:
			isCheck_StartAddress = false;
			isCheck_EndAddress = true;
			isOnfreshen = false;
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key,
					Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_select_btn:
//			if (verifyData() == false) {
//				return;
//			}
			isOnfreshen = true;
			isRefresh = true;
			findGoodsRequestBean.Page_StartNum = Constants.FindStartNum;
			findGoodsRequestBean.Page_EndNum = Constants.FindNum;
			findGoodsRequestBean.FirstRequestTime = DateUtil.getCurDateStr();

			waitDialog.show();
			findGoods();
			break;
		default:
			break;
		}
	}

	@Event(value = R.id.d_find_goods_lv, type = AdapterView.OnItemClickListener.class)
	private void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		WaitingOrderBaseInfo bean = dataList.get(position);
		if (bean != null) {
			Intent intent = new Intent(context,
					D_WaitingOrderDetailActivity.class);
			intent.putExtra(Constants.OrderInfo, bean);
			startActivity(intent);
		}
	}

	private boolean verifyData() {
		if (startRegionBean == null) {
			Toast.makeText(context, "请选择始发地！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (endRegionBean == null) {
			Toast.makeText(context, "请选择目的地！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void findGoods() {
		new Thread() {
			public void run() {
				RequestParams pans = new RequestParams(
						Constants.LookupWaitingOrder);

				try {
					if (startRegionBean != null) {
						findGoodsRequestBean.Provenance_Provice = startRegionBean.provinceBean.PName;
						if (startRegionBean.cityBean != null) {
							findGoodsRequestBean.Provenance_City = startRegionBean.cityBean.CName;
						}
					}else{
						findGoodsRequestBean.Provenance_Provice=null;
						findGoodsRequestBean.Provenance_City=null;
						
					}

					if (endRegionBean != null) {
						findGoodsRequestBean.Destination_Provice = endRegionBean.provinceBean.PName;
						if (endRegionBean.cityBean != null) {
							findGoodsRequestBean.Destination_City = endRegionBean.cityBean.CName;
						}
					}else{
						findGoodsRequestBean.Destination_Provice=null;
						findGoodsRequestBean.Destination_City=null;
					}

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
							.toJsonData(findGoodsRequestBean));

					pans.addBodyParameter(null,
							JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(pans, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String str) {
						try {
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								List<WaitingOrderBaseInfo> list = JsonUtils
										.parseOrderBeanList(str);
								if (list != null) {
									msg.what = Constants.Request_Success;
									msg.obj = list;
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
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable ex, boolean b) {
						LogUtil.d("-->>onError:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj=Constants.errorMsg;
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

	public Handler myHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			if (myFreshHandler != null) {
				myFreshHandler.sendEmptyMessage(0);
			}

			switch (msg.what) {
			case Constants.Request_Success:
				if (msg.obj != null) {
					List<WaitingOrderBaseInfo> list = (List<WaitingOrderBaseInfo>) msg.obj;
					if (isRefresh) {
						isRefresh = false;

						dataList.clear();
						adapter.notifyDataSetChanged();
					}

					dataList.addAll(list);
					adapter.notifyDataSetChanged();
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(context, "暂无数据！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_FindGoodsActivity.this);
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
		}
	};

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		isRefresh = true;
		findGoodsRequestBean.Page_StartNum = Constants.FindStartNum;
		findGoodsRequestBean.Page_EndNum = Constants.FindNum;
		findGoodsRequestBean.FirstRequestTime = DateUtil.getCurDateStr();
		findGoods();
		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		isRefresh = false;
		findGoodsRequestBean.Page_StartNum = dataList.size()
				+ Constants.FindStartNum;
		findGoodsRequestBean.Page_EndNum = dataList.size() + Constants.FindNum;
		findGoods();
		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
	}

	@Override
	public void onRegionData(Object object) {
		if (object != null) {
			if (isCheck_StartAddress) {
				startRegionBean = (RegionBean) object;
				c_Shipping_City_et.setText(ContextUtil
						.getRegion(startRegionBean));
			}

			if (isCheck_EndAddress) {
				endRegionBean = (RegionBean) object;
				c_Consignee_City_et.setText(ContextUtil
						.getRegion(endRegionBean));
			}
		}
	}

}
