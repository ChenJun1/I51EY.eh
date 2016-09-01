package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.model;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public interface OrderListener {
	
	void OrderError(String str);

	void OrderSuccess(String str);
}
