package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.view;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;

import java.util.List;

public interface OrderView {
	int getPageStartNum(boolean isRefresh);// 起始数

	int getPageEndNum(boolean isRefresh);// 结束数

	String getOperateCode();// 订单状态

	String getTime(boolean isRefresh);

	void showLoading();

	void hideLoading();

	void toError(String errorStr);

	void toCompletedLoading(List<WaitingOrderBaseInfo> dataList); // 已完成订单下拉加载

	void toCompletedRefresh(List<WaitingOrderBaseInfo> dataList); // 已完成订单上啦刷新

	void toReceivedLoading(List<WaitingOrderBaseInfo> dataList); // 已接订单下拉加载

	void toReceivedRefresh(List<WaitingOrderBaseInfo> dataList); // 已接订单上啦刷新

	void toWaitingRefresh(List<WaitingOrderBaseInfo> dataList); // 未接订单上啦刷新

	void toWaitingLoading(List<WaitingOrderBaseInfo> dataList); // 未接订单下拉加载

	void toDelete(); // 删除成功
}
