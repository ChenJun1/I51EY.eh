package com.cvnavi.logistics.i51eyun.eh.activity.cargo.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseFragment;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_AssessActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CarManageActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CommonConsigneeManagementActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CustomerServiceActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_HistoryOrderActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_PersonInfoActivity;
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
 * @description 货主——我——页面
 * @date 2016-5-17 下午1:06:31
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_me_fragment)
public class c_MeFrgment extends BaseFragment {

	@ViewInject(R.id.c_personal_information_layout)
	private RelativeLayout cPersonalInformationLayout;

	@ViewInject(R.id.c_common_consignee_management_layout)
	private RelativeLayout cCommonConsigneeManagementLayout;

	@ViewInject(R.id.c_common_car_management_layout)
	private RelativeLayout cCommonCarManagementLayout;

	@ViewInject(R.id.c_my_wallet_layout)
	private RelativeLayout cMyWalletLayout;

	@ViewInject(R.id.c_password_modification_layout)
	private RelativeLayout cPasswordModificationLayout;

	@ViewInject(R.id.c_history_order)
	private RelativeLayout c_history_order;

	@ViewInject(R.id.c_assess)
	private RelativeLayout c_assess;

	@ViewInject(R.id.c_Customer_Service)
	private RelativeLayout c_Customer_Service;

	@ViewInject(R.id.c_setting)
	private RelativeLayout c_setting;

	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.exit_but)
	private Button exit_but;

	public static c_MeFrgment newInstance() {
		return new c_MeFrgment();
	}

	private CommonWaitDialog waitDialog;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		waitDialog = new CommonWaitDialog(getActivity(),
				R.style.progress_dialog);

	}

	@Event(value = { R.id.c_personal_information_layout,
			R.id.c_common_consignee_management_layout,
			R.id.c_common_car_management_layout, R.id.c_my_wallet_layout,
			R.id.c_password_modification_layout, R.id.c_history_order,
			R.id.c_assess, R.id.c_Customer_Service, R.id.c_setting,
			R.id.exit_but }, type = OnClickListener.class)
	private void meOnclick(View view) {
		if (view.getId() == R.id.c_personal_information_layout||view.getId() == R.id.exit_but) {
			
		}else{
			if (MyApplication.getInstatnce().getUserTypeOid().equals("B") == false) {
				Toast.makeText(getActivity(), "请先完善货主个人信息！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}
		switch (view.getId()) {
		case R.id.c_personal_information_layout:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			showActivity(getActivity(), C_PersonInfoActivity.class);
			//showActivity(getActivity(), AppupdateDemo.class);
			break;
		case R.id.c_common_consignee_management_layout:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			showActivity(getActivity(),
					C_CommonConsigneeManagementActivity.class);
			break;
		case R.id.c_common_car_management_layout:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			showActivity(getActivity(), C_CarManageActivity.class);
			break;
		case R.id.c_my_wallet_layout:
			Toast.makeText(getActivity(), "此功能暂未开放，敬请期待！",Toast.LENGTH_SHORT).show();
			break;
		case R.id.c_password_modification_layout:
			Toast.makeText(getActivity(), "此功能暂未开放，敬请期待！",Toast.LENGTH_SHORT).show();
			break;
		case R.id.c_history_order:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			showActivity(getActivity(), C_HistoryOrderActivity.class);
			break;
		case R.id.c_assess:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			showActivity(getActivity(), C_AssessActivity.class);
			break;
		case R.id.c_Customer_Service:
			if(!NetWorkUtils.isNetWort(getActivity())){
				return;
			}
			showActivity(getActivity(), C_CustomerServiceActivity.class);
			break;
		case R.id.c_setting:
//			if(!NetWorkUtils.isNetWort(getActivity())){
//				return;
//			}
//			showActivity(getActivity(), C_SettingActivity.class);
			break;
		case R.id.exit_but:

			// if (!NetWorkUtils.isConnectedByState(getActivity())) {
			// alertDialog = new AlertDialog.Builder(getActivity());
			// alertDialog.setTitle("提示");
			// alertDialog.setMessage("请检查网络是否连接！");
			// alertDialog.setPositiveButton("确定", null);
			// alertDialog.create().show();
			// return;
			// }

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
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());

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
						msg.obj = TextUtils.isEmpty(arg0.getMessage()) == true ? "请求服务超时！请稍后再试！"
								: arg0.getMessage();
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
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								msg.what = Constants.Request_Success;
								myHandler.sendMessage(msg);
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

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}
			switch (msg.what) {
			case Constants.Request_Success:
				ActivityStackManager.getInstance().AppExit(getActivity());
				// ActivityStackManager.getInstance().finishActivity(getActivity());
				// Intent intent = new Intent();
				// intent.setClass(getActivity(), LoginActivity.class);
				// startActivity(intent);
				break;
			case Constants.Request_Fail:
				ActivityStackManager.getInstance().AppExit(getActivity());
				Toast.makeText(
						getActivity(),
						"请求服务错误！请联系客服！" + msg.obj == null ? "" : String
								.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
}
