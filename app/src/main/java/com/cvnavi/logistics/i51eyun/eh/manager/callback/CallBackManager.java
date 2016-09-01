package com.cvnavi.logistics.i51eyun.eh.manager.callback;

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
public class CallBackManager {

	private static CallBackManager ms_callbackManager = new CallBackManager();

	private ParkCallBackManager parkCallBackManager;
	private RegionCallBackManager regionCallBackManager;
	private CarCallBackManager carCallBackManager;
	private ConsigneeCallBackManager consigneeCallBackManager;
	private FilterCarCallBackManager filterCarCallBackManager;
	private OrderCallBackManager orderCallBackManager;
	private RegionCallBackManager2 regionCallBackManager2;

	private CallBackManager() {
		parkCallBackManager = new ParkCallBackManager();
		regionCallBackManager = new RegionCallBackManager();
		carCallBackManager = new CarCallBackManager(); 
		consigneeCallBackManager = new ConsigneeCallBackManager();
		filterCarCallBackManager = new FilterCarCallBackManager();
		orderCallBackManager= new OrderCallBackManager();
		regionCallBackManager2 = new RegionCallBackManager2();
	}

	public static synchronized CallBackManager getInstance() {
		return ms_callbackManager;
	}

	public ParkCallBackManager getParkCallBackManager() {
		return parkCallBackManager;
	}

	public RegionCallBackManager getRegionCallBackManager() {
		return regionCallBackManager;
	}

	public CarCallBackManager getCarCallBackManager() {
		return carCallBackManager;
	}

	public ConsigneeCallBackManager getConsigneeCallBackManager() {
		return consigneeCallBackManager;
	}

	public FilterCarCallBackManager getFilterCarCallBackManager() {
		return filterCarCallBackManager;
	}

	public OrderCallBackManager getOrderCallBackManager() {
		return orderCallBackManager;
	}

	public RegionCallBackManager2 getRegionCallBackManager2() {
		return regionCallBackManager2;
	}
	
	
	
	
}
