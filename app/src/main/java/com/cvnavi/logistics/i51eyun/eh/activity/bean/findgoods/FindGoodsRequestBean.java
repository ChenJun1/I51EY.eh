package com.cvnavi.logistics.i51eyun.eh.activity.bean.findgoods;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;

public class FindGoodsRequestBean extends PagingBean {

	public String CarCode_Key;

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

}
