package com.cvnavi.logistics.i51eyun.eh.activity.login.model;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.login.LoginData;

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
public interface IUserBiz {
	public void onLogin(String userName, String userPassword, boolean isCheckBox, OnLoginListener listener);

	public void onJPushForRegistrationID(String registrationID, OnLoginListener listener);

	public void getVerificationCode(String userPhone, OnLoginListener listener);

	public void getUsername(boolean isCheckBox, OnLoginListener listener);
	
	public void getAppVersion(OnLoginListener listener);
	
	public void onTokenLogin(LoginData bean, OnLoginListener listener);
	
	public void loadAdvertPic(OnLoginListener listener);
	
	public void getAppDwonUrl(OnLoginListener listener);
}
