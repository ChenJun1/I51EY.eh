package com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.driver.DriverInfoBase;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午12:59:08
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class AppRegData_Driver extends DriverInfoBase {

	/// <summary>
	/// 车辆识别码
	/// </summary>
	public String VehicleID_Code;

	/// <summary>
	/// 车辆类型
	/// </summary>
	public String CarType_Oid;

	/// <summary>
	/// 车辆类型名称
	/// </summary>
	public String CarType;

	/// <summary>
	/// 车辆尺寸
	/// </summary>
	public double Size;

	/// <summary>
	/// 运输区域
	/// </summary>
	public String TransportArea_Oid;

	/// <summary>
	/// 车牌号
	/// </summary>
	public String CarCode;

	public String CarCode_Key;// 车辆key

	public String FCompany_Oid;// 园区分号

	public String IsPrimaryDriver;// 是否主司机

}
