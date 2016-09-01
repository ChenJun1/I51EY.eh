package com.cvnavi.logistics.i51eyun.eh.activity.registered.bean;

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
public class RegisteredBean {

    private String UserPhone;

    private boolean isClick;

    private String Verification_code;

    private String UserPasswordone;

    private String userPasswordtwo;

    private String Car_goods;

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getVerification_code() {
        return Verification_code;
    }

    public void setVerification_code(String verification_code) {
        Verification_code = verification_code;
    }

    public String getUserPasswordone() {
        return UserPasswordone;
    }

    public void setUserPasswordone(String userPasswordone) {
        UserPasswordone = userPasswordone;
    }

    public String getUserPasswordtwo() {
        return userPasswordtwo;
    }

    public void setUserPasswordtwo(String userPasswordtwo) {
        this.userPasswordtwo = userPasswordtwo;
    }

    public String getCar_goods() {
        return Car_goods;
    }

    public void setCar_goods(String car_goods) {
        Car_goods = car_goods;
    }
}
