package com.cvnavi.logistics.i51eyun.eh.manager.callback;

import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack;

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
public class RegionCallBackManager {

	private List<RegionDataCallBack> callBackList;
	
	public RegionCallBackManager(){
		callBackList = new ArrayList<RegionDataCallBack>();
	}

	public synchronized void addCallBack(RegionDataCallBack callBack) {
		try {
			callBackList.add(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeCallBack(RegionDataCallBack callBack) {
		try {
			callBackList.remove(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireCallBack(Object obj) {
		try {
			for (RegionDataCallBack callBack : callBackList) {
				try {
					callBack.onRegionData(obj);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fireCallBackByTag(Object obj,String tag) {
		try {
			for (RegionDataCallBack callBack : callBackList) {
				try {
					callBack.onRegionDataByTag(obj, tag);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
