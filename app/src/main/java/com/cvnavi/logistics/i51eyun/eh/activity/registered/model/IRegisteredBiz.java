package com.cvnavi.logistics.i51eyun.eh.activity.registered.model;

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
public interface IRegisteredBiz {
    public void getVerificationCode(String userPhone, OnRegisteredListener onRegisteredListener);

    public void submit(String userPhone, String Verification_code, boolean isClick, OnRegisteredListener onRegisteredListener);

    public void login(String userPhone, String userPasswordone, String userPasswordtwo, String Car_goods, OnRegisteredListener onRegisteredListener);

}
