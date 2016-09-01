package com.cvnavi.logistics.i51eyun.eh.activity.bean.collection;

//import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 常用收货人信息。
 * @date 2016-5-17 上午11:26:18
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class Orders_Consignee_Sheet extends PagingBean{
	public String Consignee_Key;

	public String Consignee_Name;

	public String Consignee_Tel;

	public String Consignee_Provice;

	public String Consignee_City;

	public String Consignee_Area;

	public String Consignee_Address;
	
	//经度
	public String BLng;
	
	//维度
	public String BLat;
}
