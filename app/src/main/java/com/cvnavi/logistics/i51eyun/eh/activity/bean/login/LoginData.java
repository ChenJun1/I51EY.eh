package com.cvnavi.logistics.i51eyun.eh.activity.bean.login;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.verifycode.VerifyCodeBase;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 登录实体
 * @date 2016-5-17 上午11:29:31
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class LoginData extends VerifyCodeBase {

	/**
	 * 每次请求必须的。
	 */
	@JsonProperty("User_Key")
	private String User_Key;

	/**
	 * 每次请求必须的。
	 */
	@JsonProperty("Token")
	private String Token;

	/**
	 * 用户电话。
	 */
	@JsonProperty("User_Tel")
	private String User_Tel;

	// @JsonProperty("Role_Oid")
	// private int Role_Oid;

	@JsonProperty("UserType_Oid")
	private String UserType_Oid;

	// @JsonProperty("MenuList")
	// private List<Object> MenuList;

	@JsonProperty("RegistrationID")
	private String RegistrationID;

	@JsonProperty("WorkStatus")
	private String WorkStatus;

	@JsonProperty("Company_Oid")
	private String Company_Oid;

	/**
	 * 评分
	 */
	@JsonProperty("Evaluation_Score")
	private String Evaluation_Score;

	/**
	 * 司机接货重量
	 */
	@JsonProperty("DriverGoodsWeight")
	private String DriverGoodsWeight;

	/**
	 * 司机接货方量
	 */
	@JsonProperty("DriverGoodsVolume")
	private String DriverGoodsVolume;

	/**
	 * 是否开启评价（0：未开启；1：开启）
	 */
	@JsonProperty("ISEvaluate")
	private String ISEvaluate;

	/**
	 * 用户名
	 */
	@JsonProperty("User_Name")
	private String User_Name;

	@JsonProperty("CarCode_Key")
	private String CarCode_Key;// 车辆key

	@JsonProperty("CarCode")
	private String CarCode;// 车牌号

	@JsonProperty("FCompany_Oid")
	private String FCompany_Oid;// 园区分号

	@JsonProperty("IsPrimaryDriver")
	private String IsPrimaryDriver;// 是否主司机

	/**
	 * 客服电话
	 */
	@JsonProperty("CustomerTel")
	private String CustomerTel;


	public String getUser_Key() {
		return User_Key;
	}

	public void setUser_Key(String user_Key) {
		User_Key = user_Key;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getUser_Tel() {
		return User_Tel;
	}

	public void setUser_Tel(String user_Tel) {
		User_Tel = user_Tel;
	}

	// public int getRole_Oid() {
	// return Role_Oid;
	// }
	//
	// public void setRole_Oid(int role_Oid) {
	// Role_Oid = role_Oid;
	// }

	public String getUserType_Oid() {
		return UserType_Oid;
	}

	public void setUserType_Oid(String userType_Oid) {
		UserType_Oid = userType_Oid;
	}

	// public List<Object> getMenuList() {
	// return MenuList;
	// }
	//
	// public void setMenuList(List<Object> menuList) {
	// MenuList = menuList;
	// }

	public String getRegistrationID() {
		return RegistrationID;
	}

	public void setRegistrationID(String registrationID) {
		RegistrationID = registrationID;
	}

	public String getWorkStatus() {
		return WorkStatus;
	}

	public void setWorkStatus(String workStatus) {
		WorkStatus = workStatus;
	}

	public String getCompany_Oid() {
		return Company_Oid;
	}

	public void setCompany_Oid(String company_Oid) {
		Company_Oid = company_Oid;
	}

	public String getEvaluation_Score() {
		return Evaluation_Score;
	}

	public void setEvaluation_Score(String evaluation_Score) {
		Evaluation_Score = evaluation_Score;
	}

	public String getDriverGoodsWeight() {
		return DriverGoodsWeight;
	}

	public void setDriverGoodsWeight(String driverGoodsWeight) {
		DriverGoodsWeight = driverGoodsWeight;
	}

	public String getDriverGoodsVolume() {
		return DriverGoodsVolume;
	}

	public void setDriverGoodsVolume(String driverGoodsVolume) {
		DriverGoodsVolume = driverGoodsVolume;
	}

	public String getISEvaluate() {
		return ISEvaluate;
	}

	public void setISEvaluate(String iSEvaluate) {
		ISEvaluate = iSEvaluate;
	}

	public String getUser_Name() {
		return User_Name;
	}

	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}

	public String getCarCode_Key() {
		return CarCode_Key;
	}

	public void setCarCode_Key(String carCode_Key) {
		CarCode_Key = carCode_Key;
	}

	public String getCarCode() {
		return CarCode;
	}

	public void setCarCode(String carCode) {
		CarCode = carCode;
	}

	public String getFCompany_Oid() {
		return FCompany_Oid;
	}

	public void setFCompany_Oid(String fCompany_Oid) {
		FCompany_Oid = fCompany_Oid;
	}

	public String getIsPrimaryDriver() {
		return IsPrimaryDriver;
	}

	public void setIsPrimaryDriver(String isPrimaryDriver) {
		IsPrimaryDriver = isPrimaryDriver;
	}

	public String getCustomerTel() {
		return CustomerTel;
	}

	public void setCustomerTel(String customerTel) {
		CustomerTel = customerTel;
	}

}
