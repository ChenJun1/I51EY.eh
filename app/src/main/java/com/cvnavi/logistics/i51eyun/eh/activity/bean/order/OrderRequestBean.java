package com.cvnavi.logistics.i51eyun.eh.activity.bean.order;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;

public class OrderRequestBean extends PagingBean {

	public String Order_Key;// 订单key

	public String Shipping_City;// 发货地址
	public String Consignee_City;// 收货地址
	public String Shipping_Provice;// 发货地址省
	public String Consignee_Provice;// 收货地址省

	public String Operate_Code;// 订单状态AA，AB，AG

}
