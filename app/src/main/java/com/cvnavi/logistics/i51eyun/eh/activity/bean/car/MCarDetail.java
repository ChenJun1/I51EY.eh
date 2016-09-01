package com.cvnavi.logistics.i51eyun.eh.activity.bean.car;

import java.io.Serializable;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 车辆详情
 * @date 2016-5-17 上午11:24:53
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class MCarDetail extends MCar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1135330572620700342L;

	public String User_Key;

	public String User_Name;

	public String DrivingExperience;

	public String DriversType;

	public String VehicleID_Code;

	public String Company_Name;

	public String DriversLicense_Img;
}
