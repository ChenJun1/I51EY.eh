package com.cvnavi.logistics.i51eyun.eh.activity.bean.region;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午12:59:35
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class ProvinceBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1608322636173380562L;

	/// <summary>
    /// 省Id
    /// </summary>
	public String ProvincesSerial_Oid ;

	/// <summary>
    /// 省名
    /// </summary>
	public String PName;

	public List<CityBean> ListCity;
}
