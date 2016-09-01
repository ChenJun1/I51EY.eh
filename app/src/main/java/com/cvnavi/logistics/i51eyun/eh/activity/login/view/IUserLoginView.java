package com.cvnavi.logistics.i51eyun.eh.activity.login.view;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.login.LoginData;
import com.cvnavi.logistics.i51eyun.eh.activity.login.bean.UserBean;

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
public interface IUserLoginView {
	String getUserName();

	String getUserPassword();

	boolean getIsCheck();

	void showLoading();

	void hideLoading();

	void toLoginActivity(Object object, Boolean isTokenlogin);

	void toLoginFailed(String json);

	void loginError(String json);

	void toRegistered(String str);
	
	void setUserName(String userName);

	void setUserPassword(String userPassword);

	void setIsCheck(boolean check);

	void getUserBean(UserBean user);

	String getJPushRegistrationID();
	
	void isGetVersionSuccess();
	
	LoginData getUserTokenBean();
	
	void	successPicToHome();
	
	void	successGetDwonAppUrl(AppUpdateBean appBean);
}
