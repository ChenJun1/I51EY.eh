package com.cvnavi.logistics.i51eyun.eh.activity.bean.commonlyline;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description 添加常用线路请求实体类。
 * @date 2016-5-17 上午11:28:49
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class AddCommonlyLineRequestBean {

	/**
	 * 用户Key
	 */
	public String User_Key;

	/// <summary>
	/// 始发省
	/// </summary>
	public String Provenance_Provice;

	/// <summary>
	/// 始发市
	/// </summary>
	public String Provenance_City;

	/// <summary>
	/// 目的省
	/// </summary>
	public String Destination_Provice;

	/// <summary>
	/// 目的市
	/// </summary>
	public String Destination_City;

	/// <summary>
	/// 行程（0：单边，1：往返）
	/// </summary>
	public String RouteType;
	//默认线路（0：否1：是）
	public String IsDefault;
	

}
