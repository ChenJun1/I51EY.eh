package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;

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
public class RegionCallBackManager2 {

	private List<RegionDataCallBack2> callBackList;

	public RegionCallBackManager2() {
		callBackList = new ArrayList<RegionDataCallBack2>();
	}

	public synchronized void addCallBack(RegionDataCallBack2 callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(RegionDataCallBack2 callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object object) {
		try {
			for (RegionDataCallBack2 callBack : callBackList) {
				try {
					callBack.onRegionData(object);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
