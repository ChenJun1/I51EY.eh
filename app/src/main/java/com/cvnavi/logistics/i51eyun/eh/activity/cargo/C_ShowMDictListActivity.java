/**
 * Administrator2016-4-29
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_mDictCompanyAdapter;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
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
 * @date 2016-5-17 下午1:09:23
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_show_mdict_list)
public class C_ShowMDictListActivity extends BaseActivity {

	private static final String TAG = C_ShowMDictListActivity.class.getName();

	@ViewInject(R.id.c_show_mdict_listView)
	private ListView mdictListView;

	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout backImageView;
	
	private AlertDialog.Builder alertDialog;


	private List<MDict_Company_Sheet> companyList;
	private C_mDictCompanyAdapter companyAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titlt_textView.setText("园区列表");

		companyList = new ArrayList<MDict_Company_Sheet>();
		companyAdapter = new C_mDictCompanyAdapter(companyList, this);
		mdictListView.setAdapter(companyAdapter);
		mdictListView.setOnItemClickListener(myListener);

		loadData();
	}

	OnItemClickListener myListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (companyAdapter.getItem(position) != null) {
				MDict_Company_Sheet bean = (MDict_Company_Sheet) companyAdapter.getItem(position);
				CallBackManager.getInstance().getParkCallBackManager().fireCallBack(bean);
				finish();
			}
		}
	};

	@Event(value = { R.id.back_linearLayout })
	private void MyOnCklic(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}

	private void loadData() {
		if (MyApplication.getInstatnce().getBasicDataBuffer().getCompanyList() == null
				|| MyApplication.getInstatnce().getBasicDataBuffer().getCompanyList().size() == 0) {
			DataRequestData dataRequestData = new DataRequestData();
			dataRequestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
			dataRequestData.setToken(MyApplication.getInstatnce().getToken());

			RequestParams params = new RequestParams(Constants.Get_ParkList_URL);
			try {
				params.addBodyParameter(null, JsonUtils.toJsonData(dataRequestData));
			} catch (Exception e) {
				e.printStackTrace();
			}
			x.http().post(params, new Callback.CommonCallback<String>() {
				@Override
				public void onCancelled(CancelledException arg0) {
					Log.d(TAG, "-->>onCancelled");
				}

				@Override
				public void onError(Throwable ex, boolean isOnCallback) {
					Log.d(TAG, "-->>onError:" + ex.getMessage());
					Message msg = Message.obtain();
					msg.what = Constants.Request_Fail;
					msg.obj=Constants.errorMsg;
					myHandler.sendMessage(msg);
				}

				@Override
				public void onFinished() {
					Log.d(TAG, "-->>onFinished");
				}

				@Override
				public void onSuccess(String str) {
					Log.d(TAG, "-->>onSuccess" + str);
					try {
						List<MDict_Company_Sheet> dataList = (List<MDict_Company_Sheet>) JsonUtils.parseParkList(str);
						if (dataList != null) {
							MyApplication.getInstatnce().getBasicDataBuffer().setCompanyList(dataList);

							Message message = Message.obtain();
							message.what = Constants.Request_Success;
							myHandler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			companyList.clear();
			companyList.addAll(MyApplication.getInstatnce().getBasicDataBuffer().getCompanyList());
			companyAdapter.notifyDataSetChanged();
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.Request_Success:
				companyList.clear();
				companyList.addAll(MyApplication.getInstatnce().getBasicDataBuffer().getCompanyList());
				companyAdapter.notifyDataSetChanged();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_ShowMDictListActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new CommonWaitDialog.OnClickListener() {

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
			super.handleMessage(msg);
		}
	};

}
