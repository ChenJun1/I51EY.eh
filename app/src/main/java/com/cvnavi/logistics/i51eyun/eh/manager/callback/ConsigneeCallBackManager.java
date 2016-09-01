package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.ConsigneeDataCallBack;

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
public class ConsigneeCallBackManager {

	private List<ConsigneeDataCallBack> callBackList;
	
	public ConsigneeCallBackManager(){
		callBackList = new ArrayList<ConsigneeDataCallBack>();
	}

	public synchronized void addCallBack(ConsigneeDataCallBack callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(ConsigneeDataCallBack callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object obj) {
		try {
			for (ConsigneeDataCallBack callBack : callBackList) {
				try {
					callBack.onConsigneeData(obj);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
