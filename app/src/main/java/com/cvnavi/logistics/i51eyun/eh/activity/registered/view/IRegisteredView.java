package com.cvnavi.logistics.i51eyun.eh.activity.registered.view;

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
public interface IRegisteredView {
    String getUserPhone();

    String getVerification_code();

    String getUserPasswordone();

    String getUserPasswordtwo();

    String getCar_goods();

    boolean isisClick();

    void showLoading();

    void hideLoading();

    void toRegisteredActivity(JSONObject json);//请求成功时调用

    void error(String json);//请求失败时调用
}
