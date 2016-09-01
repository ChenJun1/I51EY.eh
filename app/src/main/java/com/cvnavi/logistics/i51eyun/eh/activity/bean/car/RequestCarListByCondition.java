package com.cvnavi.logistics.i51eyun.eh.activity.bean.car;


import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 上午11:25:23
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class RequestCarListByCondition extends PagingBean {

	public String Size;
	
	public String CarType;
	
	/**
	 * 运输区域。传代号。
	 */
	public String TransportArea;
	
}
