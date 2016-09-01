package com.cvnavi.logistics.i51eyun.eh.activity.registered.model;

import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;

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
public class RegisteredBiz implements IRegisteredBiz {
	/**
	 * 获取验证码调用
	 * 
	 * @param userPhone
	 * @param onRegisteredListener
	 */
	@Override
	public void getVerificationCode(String userPhone, OnRegisteredListener onRegisteredListener) {
		try {
			if (!ContextUtil.isMobileNO(userPhone)) {
				onRegisteredListener.loginError("请输入正确的格式的手机号！");
				return;
			}
			// 模拟请求网络
			Thread.sleep(2000);
			JSONObject successjson = new JSONObject();
			if (userPhone.equals("15072210627")) {
				onRegisteredListener.loginSuccess(successjson);
			} else {
				// onRegisteredListener.loginerror(successjson);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交验证码调用
	 * 
	 * @param userPhone
	 * @param Verification_code
	 * @param isClick
	 * @param onRegisteredListener
	 */
	@Override
	public void submit(String userPhone, String Verification_code, boolean isClick,
			OnRegisteredListener onRegisteredListener) {
		new Thread(new Runnable() {
			@Override
			public void run() {

			}
		}).start();
	}

	/**
	 * 登录时调用
	 * 
	 * @param userPhone
	 * @param userPasswordone
	 * @param userPasswordtwo
	 * @param Car_goods
	 * @param onRegisteredListener
	 */
	@Override
	public void login(String userPhone, String userPasswordone, String userPasswordtwo, String Car_goods,
			OnRegisteredListener onRegisteredListener) {

	}
}
