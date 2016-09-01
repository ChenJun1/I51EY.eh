package com.cvnavi.logistics.i51eyun.eh.activity.bean.order;

public class ConfirmTakeOrCompleteOrderRequestBean {

	public String Order_Key; // 订单key
	public String Serial_Oid; // 订单号
	public String TH_Photo; // 提货照片base64
	public String JH_Photo; // 交货照片base64
	public String Actual_Consignee; // 实际收货人
	//  收货地址市
	public String Consignee_City;
	// / 发货地址市
	public String Shipping_City;
	public String PhotoStatus; // 状态（AC：提货 AF：交货）

}
