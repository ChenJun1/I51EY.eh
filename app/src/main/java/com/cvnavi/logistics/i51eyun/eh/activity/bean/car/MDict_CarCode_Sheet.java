package com.cvnavi.logistics.i51eyun.eh.activity.bean.car;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.CollectDriverBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.commonlyline.MDict_Line_Sheet;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 车信息
 * @date 2016-5-17 上午11:25:09
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class MDict_CarCode_Sheet implements Serializable{

	public String CarCode_Key;

	public String CarCode;

	public String VehicleID_Code;

	public String CarType_Oid;

	public String CarType;

	public double Size;

	public String TransportArea_Oid;

	public String TransportArea;

	/**
	 * 收藏车辆的司机列表。
	 */
	public List<CollectDriverBean> Driver;

	/**
	 * 收藏车辆的线路列表。
	 */
	public List<MDict_Line_Sheet> Line;

}
