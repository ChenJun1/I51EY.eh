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
import android.text.TextUtils;
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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.historyorder.HistoryOrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_HistoryOrderAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_CompletedOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.datetimedialog.DateTimePickerDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.datetimedialog.DateTimePickerDialog.OnDateTimeSetListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 下午5:23:06
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_history_order)
public class D_History_Order extends BaseActivity implements OnRefreshListener, RegionDataCallBack2 {

	@ViewInject(R.id.operation_btn)
	private Button operation_btn;
	@ViewInject(R.id.c_submit_btn)
	private Button c_submit_btn;

	@ViewInject(R.id.c_FindStartTime_et)
	private EditText c_FindStartTime_et;
	@ViewInject(R.id.c_FindEndTime_et)
	private EditText c_FindEndTime_et;

	@ViewInject(R.id.c_Shipping_City_et)
	private EditText c_Shipping_City_et;
	@ViewInject(R.id.c_Consignee_City_et)
	private EditText c_Consignee_City_et;

	@ViewInject(R.id.c_historyorder_lv)
	private MyListView c_historyorder_lv;
	@ViewInject(R.id.c_refresh_history_view)
	private PullToRefreshLayout c_refresh_history_view;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;
	
	private AlertDialog.Builder alertDialog;
	

	private D_HistoryOrderAdapter historyOrderAdapter;
	private List<WaitingOrderBaseInfo> historyOrderList;

	private CommonWaitDialog myDialog;
	private Context context;

	private boolean isRefresh = false;
	private boolean isLoad = false;
	private HistoryOrderRequestBean hisOrderBean;
	private boolean flag_Shipping = false;
	private boolean flag_Consignee = false;
	private Handler myFreshHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		initView();
		initData();
		
		myDialog.show();

		isRefresh = true;
		loadText();
		selectHistoryRequest(Constants.Get_OrderBaseListByHistory_URL);
	}

	private void initView() {
		this.titlt_textView.setText("历史运单");
		operation_btn.setVisibility(View.VISIBLE);
		operation_btn.setText("刷新");

		c_refresh_history_view.setOnRefreshListener(this);
		myDialog = new CommonWaitDialog(this, R.style.progress_dialog);

		c_FindStartTime_et.setText(DateUtil.getNextHour(DateUtil.FORMAT_YMDHMS, -7 * 24));
		c_FindEndTime_et.setText(DateUtil.getCurDateStr());
	}
	
	private void initData() {
		hisOrderBean = new HistoryOrderRequestBean();
		hisOrderBean.Page_StartNum = Constants.FindStartNum;
		hisOrderBean.Page_EndNum = Constants.FindNum;
		CallBackManager.getInstance().getRegionCallBackManager2().addCallBack(this);

		historyOrderList = new ArrayList<WaitingOrderBaseInfo>();
		historyOrderAdapter = new D_HistoryOrderAdapter(this, historyOrderList);
		c_historyorder_lv.setAdapter(historyOrderAdapter);
		c_historyorder_lv.setEmptyView(empty_list_view);
	}
	
	@Event(value = { R.id.back_linearLayout, R.id.c_submit_btn, R.id.c_FindStartTime_et, R.id.c_FindEndTime_et,
			R.id.c_Shipping_City_et, R.id.c_Consignee_City_et, R.id.operation_btn }, type = View.OnClickListener.class)
	private void OnClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.c_FindStartTime_et:
			startTimeShowDialog();
			break;
		case R.id.c_FindEndTime_et:
			endTimeShowDialog();
			break;
		case R.id.c_Shipping_City_et:
			flag_Shipping = true;
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key, Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_Consignee_City_et:
			flag_Consignee = true;
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key, Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.c_submit_btn:
			if (verifyData() == false) {
				return;
			}
			myDialog.show();

			isRefresh = true;
			loadText();
			selectHistoryRequest(Constants.Get_OrderBaseListByHistory_URL);
			break;
		case R.id.operation_btn:
			hisOrderBean.Shipping_Provice = "";
			hisOrderBean.Shipping_City = "";
			hisOrderBean.Consignee_Provice = "";
			hisOrderBean.Consignee_City = "";

			c_Shipping_City_et.setText("");
			c_Consignee_City_et.setText("");
			break;
		default:
			break;
		}
	}

	private boolean verifyData() {
		if (ContextUtil.compareDate(c_FindStartTime_et.getText().toString(), DateUtil.getCurDateStr()) > 0) {
			Toast.makeText(context, "下单时间不能大于当前时间！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (ContextUtil.compareDate(c_FindEndTime_et.getText().toString(), DateUtil.getCurDateStr()) > 0) {
			Toast.makeText(context, "下单时间不能大于当前时间！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (ContextUtil.compareDate(c_FindStartTime_et.getText().toString(),
				c_FindEndTime_et.getText().toString()) > 0) {
			Toast.makeText(context, "查询的开始时间不能大于查询的结束时间！", Toast.LENGTH_SHORT).show();
			return false;
		}

		long days = ContextUtil.getDistanceDays(c_FindStartTime_et.getText().toString(),
				c_FindEndTime_et.getText().toString());

		if (Integer.valueOf(String.valueOf(days)) > 90) {
			Toast.makeText(context, "系统只支持查询90天内的历史运单！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Event(value = R.id.c_historyorder_lv, type = AdapterView.OnItemClickListener.class)
	private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WaitingOrderBaseInfo bean = historyOrderList.get(position);
		if (bean != null) {
			Intent intent = new Intent(context, D_CompletedOrderDetailActivity.class);
			intent.putExtra(Constants.OrderInfo, bean);
			startActivity(intent);
		}
	}

	private void startTimeShowDialog() {
		DateTimePickerDialog dialog = new DateTimePickerDialog(this, System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			public void OnDateTimeSet(AlertDialog dialog, long date) {
				if(date>=System.currentTimeMillis()){
					c_FindStartTime_et.setText(ContextUtil.getStringDate(System.currentTimeMillis()));
				}else{
					c_FindStartTime_et.setText(ContextUtil.getStringDate(date));
				}
			}
		});
		dialog.show();
	}

	private void endTimeShowDialog() {
		DateTimePickerDialog dialog = new DateTimePickerDialog(this, System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			public void OnDateTimeSet(AlertDialog dialog, long date) {
				if(date>=System.currentTimeMillis()){
					c_FindEndTime_et.setText(ContextUtil.getStringDate(System.currentTimeMillis()));
				}else{
					c_FindEndTime_et.setText(ContextUtil.getStringDate(date));
				}
			}
		});
		dialog.show();
	}

	private void loadText() {
		if (!TextUtils.isEmpty(c_FindStartTime_et.getText())) {
			hisOrderBean.FindStartTime = c_FindStartTime_et.getText().toString();
		}
		if (!TextUtils.isEmpty(c_FindEndTime_et.getText())) {
			hisOrderBean.FindEndTime = c_FindEndTime_et.getText().toString();
		}
		if (!TextUtils.isEmpty(c_Shipping_City_et.getText())) {
			hisOrderBean.Shipping_City = c_Shipping_City_et.getText().toString();
		}
		if (!TextUtils.isEmpty(c_Consignee_City_et.getText())) {
			hisOrderBean.Consignee_City = c_Consignee_City_et.getText().toString();
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(myDialog!=null){
			myDialog.dismiss();
			}
			
			if(myFreshHandler!=null){
				myFreshHandler.sendEmptyMessage(0);
			}
			
			switch (msg.what) {
			case Constants.Request_Success:
				@SuppressWarnings("unchecked")
				List<WaitingOrderBaseInfo> dataList = (List<WaitingOrderBaseInfo>) msg.obj;
				if (dataList.size() > 0) {
					if (isRefresh) {
						historyOrderList.clear();
						isRefresh = false;
					}
					historyOrderList.addAll(dataList);
					historyOrderAdapter.notifyDataSetChanged();
				} else {
					if (isRefresh) {
						isRefresh = false;
						historyOrderList.clear();
						historyOrderAdapter.notifyDataSetChanged();
					}
					if (isLoad) {
						Toast.makeText(context, Constants.NoDataMsg, Toast.LENGTH_SHORT).show();
						isLoad = false;
					}
				}
				break;
			case Constants.Request_NoData:
				Toast.makeText(context, "当前没有查询到信息！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_History_Order.this);
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

	private void selectHistoryRequest(final String Url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url);
				hisOrderBean.Operate_Code = "AG";
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setDataValue(JsonUtils.toJsonData(hisOrderBean));

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
								List<WaitingOrderBaseInfo> dataList = JsonUtils.parseOrderBeanList(str);
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
		
		if(!NetWorkUtils.isNetWort(this)){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		
		hisOrderBean.Page_StartNum = Constants.FindStartNum;
		hisOrderBean.Page_EndNum = Constants.FindNum;
		isRefresh = true;

		selectHistoryRequest(Constants.Get_OrderBaseListByHistory_URL);

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
		
		hisOrderBean.Page_StartNum = historyOrderList.size() + Constants.FindStartNum;
		hisOrderBean.Page_EndNum = historyOrderList.size() + Constants.FindNum;
		isLoad = true;

		selectHistoryRequest(Constants.Get_OrderBaseListByHistory_URL);

	}

	@Override
	public void onRegionData(Object object) {
		if (object != null) {
			RegionBean regionBean = (RegionBean) object;
			if (flag_Shipping) {
				flag_Shipping = false;
				StringBuffer sb = new StringBuffer();
				sb.append(regionBean.provinceBean.PName);
				hisOrderBean.Shipping_Provice = regionBean.provinceBean.PName;
				if (regionBean.cityBean != null) {
					sb.append(" " + regionBean.cityBean.CName);
					hisOrderBean.Shipping_City = regionBean.cityBean.CName;
				}
				c_Shipping_City_et.setText(sb.toString());

			} else if (flag_Consignee) {
				flag_Consignee = false;
				StringBuffer sb = new StringBuffer();
				sb.append(regionBean.provinceBean.PName);
				hisOrderBean.Consignee_Provice = regionBean.provinceBean.PName;
				if (regionBean.cityBean != null) {
					sb.append(" " + regionBean.cityBean.CName);
					hisOrderBean.Consignee_City = regionBean.cityBean.CName;
				}
				c_Consignee_City_et.setText(sb.toString());
			}
		}
	}
}
