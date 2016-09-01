package com.cvnavi.logistics.i51eyun.eh.activity.bean.locationtracking;


public class LocationTrackingBean {
	/// <summary>
	/// 订单Key
	/// </summary>
	public String Order_Key;
	/// <summary>
	/// 车牌
	/// </summary>
	public String CarCode;
	/// <summary>
	/// 订单号
	/// </summary>
	public String Serial_Oid;

	public String Shipping_City;// 发货地址
	public String Consignee_City;// 收货地址
	public String Shipping_Provice;// 发货地址省
	public String Consignee_Provice;// 收货地址省
}
