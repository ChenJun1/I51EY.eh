package com.cvnavi.logistics.i51eyun.eh.http;

import org.xutils.common.Callback.CancelledException;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:19:57
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public abstract class DataResponseBase {

	private int RequestMethodType;
	public int getRequestMethodType() {
		return RequestMethodType;
	}

	public void setRequestMethodType(int requestMethodType) {
		RequestMethodType = requestMethodType;
	}

	public enum RequestMethod{
		GET,
		POST,
		Delete,
		PATCH,
		PUT,
		HEAD,
		OPTIONS,
		TRACE,
	}
	
	
	protected abstract void onCancelled(CancelledException cancelledException);

	protected abstract void onError(Throwable ex, boolean isOnCallback);

	protected abstract void onFinished();

	protected abstract void onSuccess(String str);
	
}
