package com.cvnavi.logistics.i51eyun.eh.activity.bean.driver;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description 司机实体类。
 * @date 2016-5-17 上午11:28:49
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class DriverInfoBase {
	
//	public String CarCode_Key;
	
	// / 姓名
	public String Name;

	// / 号码
	public String Tel;

	// / 身份证号
	public String IdentificationCard;

	// / 银行卡号
	public String BankCode;

	// / 银行类型
	public String BankType_Oid;

	// / 所属中心
	public String Company_Oid;

	// / 所属中心名称
	public String Company_Oid_Name;

	// / 驾龄
	public int DrivingExperience;

	// / 驾照类型
	public String DriversType;

	// / 驾驶证照片（base64）
	public String DriversLicense_Img;

	// / 驾驶证照片类型
	public String DriversLicense_ImgType;

	/**
	 * 是否修改驾驶证。 0 否 1是
	 */
	public String IsModifyImage;
	
	/**
	 * 审核状态（已审核：1/未审核：0/驳回)
	 */
	public String UserStatus;
	
	/**
	 * 驳回原因。
	 */
	public String Rejected_Content;
	
}
