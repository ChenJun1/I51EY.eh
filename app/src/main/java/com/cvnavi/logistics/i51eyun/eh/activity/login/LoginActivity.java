package com.cvnavi.logistics.i51eyun.eh.activity.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivityForNoTitle;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.R.id;
import com.cvnavi.logistics.i51eyun.eh.activity.HelpActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.TransportAgreementActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.login.LoginData;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CarGoMainActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_DriverMainActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.login.bean.UserBean;
import com.cvnavi.logistics.i51eyun.eh.activity.login.presenter.UserLoginPresenter;
import com.cvnavi.logistics.i51eyun.eh.activity.login.view.IUserLoginView;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.RegisteredActivity;
import com.cvnavi.logistics.i51eyun.eh.service.DownAPKService;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.AppUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;

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
@ContentView(value = R.layout.activity_login)
public class LoginActivity extends BaseActivityForNoTitle implements
		IUserLoginView, OnClickListener {

	@ViewInject(value = R.id.help_layout)
	private LinearLayout helpLayout;

	private CheckBox isCheckBox;
	private EditText userName, userPassword;
	private Button loginButton;
	private TextView iscode;
	private TextView Iagreement_text;

	private UserLoginPresenter userLoginPresenter = null;

	private boolean ischeckbox = true;
	private LoginData beanloLoginData=null;
	private TimeCount time;
	private long backdownFlag;
	private SharedPreferences preferences;
	private AlertDialog.Builder alertDialog;
	private CommonWaitDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userLoginPresenter = new UserLoginPresenter(this,
				getApplicationContext());
		preferences = getSharedPreferences("Phone", Context.MODE_PRIVATE);
		initView();
		showLoading();
		userLoginPresenter.onTokenLogin(getUserTokenBean());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!NetWorkUtils.isNetWort(this)) {
			return;
		}
	}

	private void initView() {
		userName = (EditText) findViewById(id.mPhone);
		userPassword = (EditText) findViewById(id.mPassword);
		loginButton = (Button) findViewById(id.login_btn);
		Iagreement_text = (TextView) findViewById(R.id.Iagreement_text);
		iscode = (TextView) findViewById(id.iscode);
		Iagreement_text.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		waitDialog = new CommonWaitDialog(LoginActivity.this,
				R.style.progress_dialog);
		isCheckBox = (CheckBox) findViewById(id.isCheckBox);
		isCheckBox.setChecked(false);
		iscode.setOnClickListener(this);
		isCheckBox.setChecked(true);
		isCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean b) {
						if (b) {
							ischeckbox = b;
						} else {
							ischeckbox = b;
						}
					}
				});
		findViewById(R.id.checkbox_text).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (ischeckbox) {
							ischeckbox = false;
							isCheckBox.setChecked(false);
						} else {
							ischeckbox = true;
							isCheckBox.setChecked(true);
						}
					}
				});

		userName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (!TextUtils.isEmpty(userName.getText())
						&& userName.getText().toString().length() == 11) {
					loginButton
							.setBackgroundResource(R.drawable.order_received_stytes);
					loginButton.setEnabled(true);
				} else {
					loginButton
							.setBackgroundResource(R.drawable.completed_order_status);
					loginButton.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		userLoginPresenter.getUserBean();
	}

	@Event(value = { R.id.help_layout }, type = OnClickListener.class)
	private void onViewClick(View view) {
		switch (view.getId()) {
		case R.id.help_layout:
			showActivity(LoginActivity.this, HelpActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public String getUserName() {
		return userName.getText().toString().trim();
	}

	@Override
	public String getUserPassword() {
		return userPassword.getText().toString().trim();
	}

	@Override
	public boolean getIsCheck() {
		return ischeckbox;
	}

	@Override
	public void showLoading() {
		waitDialog.show();
	}

	@Override
	public void hideLoading() {
		waitDialog.dismiss();
	}

	/**
	 * 保存Token
	 */
	private void saveToken(LoginData bean) {
		// 存入数据
		Editor editor = preferences.edit();
		editor.putString(Constants.USERTOKEN_STRING, bean.getToken());
		editor.putString(Constants.USERTEL_STRING, bean.getUser_Tel());
		MyApplication.getInstatnce().setUserTel(bean.getUser_Tel());
		editor.putString(Constants.USERKEY_STRING, bean.getUser_Key());
		editor.putString(Constants.USERTYPE_STRING, bean.getUserType_Oid());
		editor.putString(Constants.USERCOMPANY_STRING, bean.getCompany_Oid());
		editor.commit();
	}


	@Override
	public void toLoginFailed(String errorStr) {
		alertDialog = new AlertDialog.Builder(LoginActivity.this);
		alertDialog.setCancelable(false);
		alertDialog.setTitle("提示");
		alertDialog.setMessage(errorStr);
		alertDialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	@Override
	public void loginError(String str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage(str);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.create().show();
	}

	@Override
	public void toRegistered(String str) {
		time = new TimeCount(60000, 1000);
		time.start();
	}

	@Override
	public void setUserName(String UserName) {
		userName.setText(UserName);
	}

	@Override
	public void setUserPassword(String pwd) {
		userPassword.setText(pwd);
	}

	@Override
	public void setIsCheck(boolean check) {
		isCheckBox.setChecked(true);
	}

	@Override
	public void getUserBean(UserBean user) {
		setUserName(user.getUserName().toString().trim());
		// setUserPassword(user.getUserPassword().toString().trim());
		setIsCheck(user.isCheckBox());
	}

	@Override
	public String getJPushRegistrationID() {
		return JPushInterface.getRegistrationID(getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			if (!NetWorkUtils.isNetWort(this)) {
				return;
			}
			userLoginPresenter.login();
			break;
		case R.id.iscode:
			if (!NetWorkUtils.isNetWort(this)) {
				return;
			}
			userLoginPresenter.getVerSion();
			break;

		case R.id.Iagreement_text:
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this,
					TransportAgreementActivity.class);
			startActivity(intent);
			break;
		}
	}

	private class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			iscode.setClickable(false);
			iscode.setTextSize(13);
			iscode.setText(millisUntilFinished / 1000 + "s");
		}

		@Override
		public void onFinish() {
			iscode.setText("获取验证码");
			iscode.setTextSize(13);
			iscode.setClickable(true);
		}
	}

	private boolean exitApplication() {
		if (backdownFlag == 0 ? true : false) {
			backdownFlag = System.currentTimeMillis();
			Toast.makeText(LoginActivity.this, "再按一次退出！", Toast.LENGTH_SHORT)
					.show();
			return true;
		} else {
			if (System.currentTimeMillis() - backdownFlag <= 2000 ? true
					: false) {
				ActivityStackManager.getInstance().AppExit(this);
				return true;
			} else {
				backdownFlag = 0;
				exitApplication();
				return true;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return exitApplication();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 成功获取版本
	@Override
	public void isGetVersionSuccess() {
		String curVerName = AppUtils.getVerName(this);
//		curVerName="3";
		if (TextUtils.isEmpty(MyApplication.getInstatnce().getAndroidVersion())
				|| (MyApplication.getInstatnce().getAndroidVersion()
						.equals(curVerName) == false)) {
			hideLoading();
			alertDialog = new AlertDialog.Builder(LoginActivity.this);
			alertDialog.setCancelable(false);
			alertDialog.setTitle("更新提示");
			alertDialog.setMessage("当前版本非最新版本，请下载最新版本！");
			alertDialog.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							showLoading();
							userLoginPresenter.getAppDownUrl();
						}
					});
			alertDialog.show();

		} else {
			userLoginPresenter.getVerificationCode();
		}
	}
	
	@Override
	public LoginData getUserTokenBean() {
		LoginData loginData = new LoginData();
		loginData.setToken(preferences
				.getString(Constants.USERTOKEN_STRING, ""));
		loginData.setUser_Key(preferences.getString(Constants.USERKEY_STRING,
				""));
		loginData.setUser_Tel(preferences.getString(Constants.USERTEL_STRING,
				""));
		loginData.setUserType_Oid(preferences.getString(
				Constants.USERTYPE_STRING, ""));
		loginData.setCompany_Oid(preferences.getString(
				Constants.USERCOMPANY_STRING, ""));
		return loginData;
	}
	
	

	
	public void toLoginActivity(Object object, Boolean isTokenlogin) {

		if (object == null) {
			LogUtil.e("-->>登录解析返回对象为null");
			return;
		}
		
		beanloLoginData= (LoginData) object;
		Constants.isLogin = true;
		if (!isTokenlogin) {
			saveToken(beanloLoginData);
		}
		userLoginPresenter.onLoadPic();
	}

	
	@Override
	public void successPicToHome() {
		hideLoading();
		if (!TextUtils.isEmpty(beanloLoginData.getUserType_Oid())) {
			if (beanloLoginData.getUserType_Oid().trim().equals("A")) { // 司机
				skipActivity(LoginActivity.this, D_DriverMainActivity.class);
			} else if (beanloLoginData.getUserType_Oid().trim().equals("B")) { // 货主
			
				skipActivity(LoginActivity.this, C_CarGoMainActivity.class);
				
			} else if (beanloLoginData.getUserType_Oid().trim().equals("C")) { // 园区用户,不能进入系统。
				alertDialog = new AlertDialog.Builder(LoginActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(Constants.LoginerrorMsg);
				alertDialog.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				alertDialog.show();
				return;
			} else if (beanloLoginData.getUserType_Oid().trim().equals("D")) { // 游客
				skipActivity(LoginActivity.this, RegisteredActivity.class);
			}
	}

}

	@Override
	public void successGetDwonAppUrl(AppUpdateBean appBean) {
		hideLoading();
		
		if(appBean!=null){
			alertDialog = new AlertDialog.Builder(LoginActivity.this);
			alertDialog.setCancelable(false);
			alertDialog.setTitle("下载更新");
			alertDialog.setMessage("开始后台下载，完成后自动安装");
			alertDialog.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					});
			alertDialog.show();
		DownAPKService.startService(this, "e货", appBean.AppUrl,Integer.parseInt(appBean.AppSize));
		}
	}
}
