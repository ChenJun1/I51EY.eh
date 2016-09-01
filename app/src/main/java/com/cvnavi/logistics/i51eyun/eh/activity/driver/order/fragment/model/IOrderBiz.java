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
public interface IOrderBiz {

	void completedLoading(int startNum, int findNum, String operateCode, String requestTime, OrderListener listener); // 下拉加载

	void completedRefresh(int startNum, int findNum, String operateCode, String requestTime, OrderListener listener); // 上啦刷新

	void receivedLoading(int startNum, int findNum, String operateCode, String requestTime, OrderListener listener); // 下拉加载

	void receivedRefresh(int startNum, int findNum, String operateCode, String requestTime, OrderListener listener); // 上啦刷新

	void waitingRefresh(int startNum, int findNum, String operateCode, String requestTime, OrderListener listener); // 上拉刷新

	void waitingLoading(int startNum, int findNum, String operateCode, String requestTime, OrderListener listener); // 下拉加载

	void deletedOrder(String orderKey, OrderListener listener); // 删除订单

	void completedOrder(String orderKey, OrderListener listener); // 完成订单
}
