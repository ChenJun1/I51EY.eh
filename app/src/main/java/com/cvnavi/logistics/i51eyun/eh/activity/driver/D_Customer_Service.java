/**
 * Administrator2016-5-19
 */
package com.cvnavi.logistics.i51eyun.eh.activity.driver;

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
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 下午5:49:22
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@ContentView(R.layout.d_customer_service)
public class D_Customer_Service extends BaseActivity {

	@ViewInject(R.id.c_submit_btn)
	private Button c_submit_btn;

	@ViewInject(R.id.c_tel_tv)
	private TextView c_tel;

	@ViewInject(R.id.c_context_et)
	private EditText c_context_et;

	private CommonWaitDialog waitDialog;
	private Context context;
	
	private AlertDialog.Builder alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		initView();
	}

	private void initView() {
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		this.titlt_textView.setText("联系客服");

		c_tel.setText(MyApplication.getInstatnce().getLoginData().getCustomerTel());
	}

	@Event(value = { R.id.c_submit_btn, R.id.back_linearLayout, R.id.c_tel_tv }, type = View.OnClickListener.class)
	private void OnClick(View view) {
		switch (view.getId()) {
		case R.id.c_submit_btn:
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			if (checkText()) {
				waitDialog.show();
				AddUserMessageRequest(Constants.Add_FavoriteContacts_URL);
			}
			break;
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.c_tel_tv:
			ContextUtil.callAlertDialog(c_tel.getText().toString(), this);
			break;
		default:
			break;
		}
	}

	private void AddUserMessageRequest(final String addusermessageUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(addusermessageUrl);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setDataValue(c_context_et.getText().toString());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());

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
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								msg.what = Constants.Request_Success;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);

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

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			switch (msg.what) {
			case Constants.Request_Success:
				Toast.makeText(context, "留言成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_Customer_Service.this);
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

	private boolean checkText() {
		if (TextUtils.isEmpty(c_context_et.getText())) {
			Toast.makeText(this, "请填写留言内容！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
