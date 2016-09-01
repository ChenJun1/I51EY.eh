package com.cvnavi.logistics.i51eyun.eh.activity.registered.presenter;

import android.os.Handler;

import com.cvnavi.logistics.i51eyun.eh.activity.registered.model.IRegisteredBiz;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.model.OnRegisteredListener;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.model.RegisteredBiz;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.view.IRegisteredView;

import org.json.JSONObject;

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
public class IRegisteredPresenter {
	private IRegisteredBiz iRegisteredBiz;

	private IRegisteredView iRegisteredView;

	private Handler handler;

	public IRegisteredPresenter(IRegisteredView iRegisteredView) {
		this.iRegisteredBiz = new RegisteredBiz();
		this.iRegisteredView = iRegisteredView;
		this.handler = new Handler();
	}

	/**
	 * 获取验证码
	 */
	public void getVerificationCode() {
		iRegisteredView.showLoading();
		iRegisteredBiz.getVerificationCode(iRegisteredView.getUserPhone(), new OnRegisteredListener() {
			@Override
			public void loginSuccess(final JSONObject json) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						iRegisteredView.hideLoading();
						iRegisteredView.toRegisteredActivity(json);
					}
				});
			}

			@Override
			public void loginFailed(JSONObject json) {

			}

			@Override
			public void loginError(final String json) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						iRegisteredView.hideLoading();
						iRegisteredView.error(json);
					}
				});
			}
		});
	}

	/**
	 * 提交验证码
	 */
	public void submit() {
		iRegisteredView.showLoading();
		iRegisteredBiz.submit(iRegisteredView.getUserPhone(), iRegisteredView.getVerification_code(),
				iRegisteredView.isisClick(), new OnRegisteredListener() {
					@Override
					public void loginSuccess(final JSONObject json) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								iRegisteredView.hideLoading();
								iRegisteredView.toRegisteredActivity(json);
							}
						});
					}

					@Override
					public void loginFailed(JSONObject json) {

					}

					@Override
					public void loginError(final String json) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								iRegisteredView.hideLoading();
								iRegisteredView.error(json);
							}
						});
					}
				});
	}

	/**
	 * 注册完成 登录
	 */
	public void login() {
		iRegisteredView.showLoading();
		iRegisteredBiz.login(iRegisteredView.getUserPhone(), iRegisteredView.getUserPasswordone(),
				iRegisteredView.getUserPasswordtwo(), iRegisteredView.getCar_goods(), new OnRegisteredListener() {
					@Override
					public void loginSuccess(final JSONObject json) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								iRegisteredView.hideLoading();
								iRegisteredView.toRegisteredActivity(json);
							}
						});
					}

					@Override
					public void loginFailed(JSONObject json) {

					}

					@Override
					public void loginError(final String json) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								iRegisteredView.hideLoading();
								iRegisteredView.error(json);
							}
						});
					}
				});
	}
}
