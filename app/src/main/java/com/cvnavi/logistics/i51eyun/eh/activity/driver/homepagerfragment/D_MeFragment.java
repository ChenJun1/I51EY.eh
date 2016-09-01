package com.cvnavi.logistics.i51eyun.eh.activity.driver.homepagerfragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseFragment;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_AssessActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_CommonlyLineManagerActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_Customer_Service;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_History_Order;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.me.D_Personal_InfoFor2Activity;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:12:26
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@SuppressLint("HandlerLeak")
@ContentView(value = R.layout.d_me_fragment)
public class D_MeFragment extends BaseFragment {

	@ViewInject(value = R.id.person_info_layout)
	private LinearLayout personInfoLayout;
	@ViewInject(value = R.id.history_order_layout)
	private LinearLayout historyOrderLayout;
	@ViewInject(value = R.id.my_evaluation_layout)
	private LinearLayout myEvaluationLayout;
	@ViewInject(value = R.id.commonly_line_manager_layout)
	private LinearLayout commonlyLineManagerLayout;
	@ViewInject(value = R.id.contact_customer_service_layout)
	private LinearLayout contactCustomerServiceLayout;
	@ViewInject(value = R.id.setting_layout)
	private LinearLayout settingLayout;

	@ViewInject(value = R.id.exit_btn)
	private Button exitButton;

	private CommonWaitDialog waitDialog;
	private AlertDialog.Builder alertDialog;
	private Context context;

	public static D_MeFragment getInstance() {
		return new D_MeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		
		waitDialog = new CommonWaitDialog(context,R.style.progress_dialog);
	}

	@Event(value = { R.id.person_info_layout, R.id.history_order_layout, R.id.my_evaluation_layout,
			R.id.commonly_line_manager_layout, R.id.contact_customer_service_layout, R.id.setting_layout,
			R.id.exit_btn }, type = View.OnClickListener.class)
	private void onViewClick(View view) {
		switch (view.getId()) {
		case R.id.person_info_layout:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			startActivity(new Intent(context, D_Personal_InfoFor2Activity.class));
			break;
		case R.id.history_order_layout:
			if (MyApplication.getInstatnce().getUserTypeOid().equals("A") == false) {
				Toast.makeText(getActivity(), "请先完善司机个人信息！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			startActivity(new Intent(context, D_History_Order.class));
			break;
		case R.id.my_evaluation_layout:
			if (MyApplication.getInstatnce().getUserTypeOid().equals("A") == false) {
				Toast.makeText(getActivity(), "请先完善司机个人信息！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			startActivity(new Intent(context, D_AssessActivity.class));
			break;
		case R.id.commonly_line_manager_layout:
			if (MyApplication.getInstatnce().getUserTypeOid().equals("A") == false) {
				Toast.makeText(getActivity(), "请先完善司机个人信息！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			startActivity(new Intent(context, D_CommonlyLineManagerActivity.class));
			break;
		case R.id.contact_customer_service_layout:
			startActivity(new Intent(context, D_Customer_Service.class));
			break;
		case R.id.setting_layout:
//			startActivity(new Intent(context, D_Setting.class));
			break;
		case R.id.exit_btn:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("提示");
			builder.setMessage("是否确认退出?");
			builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					waitDialog.show();
					loadConsigneeRequest();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create().show();
			break;
		default:
			break;
		}
	}

	private void loadConsigneeRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.OutLogin_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}

				x.http().post(params, new CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						LogUtil.e("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean arg1) {
						LogUtil.e("-->>请求失败");
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请稍后再试！" : ex.getMessage();
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
								message.what = Constants.Request_Success;
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

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_Success:
				ActivityStackManager.getInstance().AppExit(context);
				break;
			case Constants.Request_Fail:
				Toast.makeText(getActivity(), "请求服务错误！请联系客服！" + msg.obj == null ? "" : String.valueOf(msg.obj),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

}
