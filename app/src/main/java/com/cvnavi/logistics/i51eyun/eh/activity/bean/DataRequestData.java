package com.cvnavi.logistics.i51eyun.eh.activity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:00:24
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class DataRequestData {

	// 用户key 登陆后返回
	@JsonProperty("User_Key")
	private String User_Key;

	// 园区号
	@JsonProperty("Company_Oid")
	private String Company_Oid;

	// 操作令牌 登陆后返回
	@JsonProperty("Token")
	private String Token;

	// 请求参数实体（如注册数据类容实体）
	@JsonProperty("DataValue")
	private Object DataValue;

	// GMSApp、GMSWeb
	@JsonProperty("ActionSystem")
	private String ActionSystem = "GMSApp";

	@JsonProperty("UserType_Oid")
	private String UserType_Oid;

	public String getUser_Key() {
		return User_Key;
	}

	public void setUser_Key(String user_Key) {
		User_Key = user_Key;
	}

	public String getCompany_Oid() {
		return Company_Oid;
	}

	public void setCompany_Oid(String company_Oid) {
		Company_Oid = company_Oid;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public Object getDataValue() {
		return DataValue;
	}

	public void setDataValue(Object dataValue) {
		DataValue = dataValue;
	}

	public String getActionSystem() {
		return ActionSystem;
	}

	public void setActionSystem(String actionSystem) {
		// ActionSystem = actionSystem;
	}

	public String getUserType_Oid() {
		return UserType_Oid;
	}

	public void setUserType_Oid(String userType_Oid) {
		UserType_Oid = userType_Oid;
	}


}
