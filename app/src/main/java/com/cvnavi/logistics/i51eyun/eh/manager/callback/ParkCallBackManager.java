package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.ParkDataCallBack;

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
public class ParkCallBackManager {

	private List<ParkDataCallBack> callBackList;
	
	public ParkCallBackManager(){
		callBackList = new ArrayList<ParkDataCallBack>();
	}

	public synchronized void addCallBack(ParkDataCallBack callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(ParkDataCallBack callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object obj) {
		try {
			for (ParkDataCallBack callBack : callBackList) {
				try {
					callBack.onParkData(obj);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
