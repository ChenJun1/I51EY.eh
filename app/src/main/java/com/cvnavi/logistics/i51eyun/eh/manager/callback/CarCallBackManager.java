package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.CarDataCallBack;

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
public class CarCallBackManager {

	private List<CarDataCallBack> callBackList;
	
	public CarCallBackManager(){
		callBackList = new ArrayList<CarDataCallBack>();
	}

	public synchronized void addCallBack(CarDataCallBack callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(CarDataCallBack callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object obj) {
		try {
			for (CarDataCallBack callBack : callBackList) {
				try {
					callBack.onCarData(obj);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
