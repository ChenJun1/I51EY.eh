package com.cvnavi.logistics.i51eyun.eh.activity.bean.region;

import java.io.Serializable;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午12:59:23
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class AreaBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -805742483645567250L;

	/// <summary>
	/// 所属市Id
	/// </summary>
	public String ParentId;

	/// <summary>
	/// 区Id
	/// </summary>
	public String AreaCitySerial_Oid;

	/// <summary>
	/// 区名
	/// </summary>
	public String AName;
}
