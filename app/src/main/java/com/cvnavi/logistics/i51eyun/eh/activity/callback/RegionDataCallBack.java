package com.cvnavi.logistics.i51eyun.eh.activity.callback;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 行政区数据回调接口。
 * @date 2016-5-17 下午1:01:44
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public interface RegionDataCallBack {

	public void onRegionData(Object obj);
	
	public void onRegionDataByTag(Object obj, String tag);
}
