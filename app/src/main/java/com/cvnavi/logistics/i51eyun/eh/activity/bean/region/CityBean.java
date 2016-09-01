package com.cvnavi.logistics.i51eyun.eh.activity.bean.region;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午12:59:30
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class CityBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3493322356216933941L;

	/// <summary>
	/// 所属省Id
	/// </summary>
	public String ParentId;

	/// <summary>
    /// 市Id
    /// </summary>
	public String CitySerial_Oid;

	/// <summary>
    /// 市名
    /// </summary>
	public String CName;

	public List<AreaBean> List_Area;
}
