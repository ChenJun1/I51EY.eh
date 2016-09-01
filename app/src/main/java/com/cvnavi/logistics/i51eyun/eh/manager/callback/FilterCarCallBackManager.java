package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.FilterCarDataCallBack;

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
public class FilterCarCallBackManager {

	private List<FilterCarDataCallBack> callBackList;
	
	public FilterCarCallBackManager(){
		callBackList = new ArrayList<FilterCarDataCallBack>();
	}

	public synchronized void addCallBack(FilterCarDataCallBack callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(FilterCarDataCallBack callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object obj,boolean isFilter) {
		try {
			for (FilterCarDataCallBack callBack : callBackList) {
				try {
					callBack.onFilterCar(obj,isFilter);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
