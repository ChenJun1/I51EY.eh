package com.cvnavi.logistics.i51eyun.eh.activity.login.model;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.AdvertPicBean;
import com.cvnavi.logistics.i51eyun.eh.activity.login.bean.UserBean;

import org.json.JSONObject;

import java.util.List;

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
public interface OnLoginListener {

	void loginFailed(JSONObject str);

	void loginError(String str);

	void loginSuccess(String str);

	void selectUserdb(UserBean userBean);
	
	void getAppVersion(String str);
	
	void successLoadPic(List<AdvertPicBean> advList);
	
	void successgetAppDwonUrl(AppUpdateBean appBean);
}
