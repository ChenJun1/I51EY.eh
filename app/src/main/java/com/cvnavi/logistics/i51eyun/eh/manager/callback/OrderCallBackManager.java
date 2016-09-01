package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.OrderDataCallBack;

import java.util.ArrayList;
import java.util.List;

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
public class OrderCallBackManager {

	private List<OrderDataCallBack> callBackList;

	public OrderCallBackManager() {
		callBackList = new ArrayList<OrderDataCallBack>();
	}

	public synchronized void addCallBack(OrderDataCallBack callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(OrderDataCallBack callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object obj) {
		try {
			for (OrderDataCallBack callBack : callBackList) {
				try {
					callBack.onRefresh(obj);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
