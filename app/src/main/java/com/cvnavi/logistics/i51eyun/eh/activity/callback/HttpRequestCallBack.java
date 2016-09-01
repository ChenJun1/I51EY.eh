/**
 * Administrator2016-4-22
 */
package com.cvnavi.logistics.i51eyun.eh.activity.callback;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:01:26
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public interface HttpRequestCallBack {
	void oSuccessList(List<Object> dataList);
	
	void oSuccessBean(Object dataList);
	
	void onError(Throwable ex, boolean isOnCallback);
	
	void onFinished();

}
