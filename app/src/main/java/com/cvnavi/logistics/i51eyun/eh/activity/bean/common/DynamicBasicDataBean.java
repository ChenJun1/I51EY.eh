package com.cvnavi.logistics.i51eyun.eh.activity.bean.common;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;

import java.util.List;

public class DynamicBasicDataBean {

	public List<Dict_CarType_Sheet> CarTypeDic; // 车辆类型列表。
	public List<Dict_CarSize_Sheet> CarSizeDic; // 车辆尺寸列表。
	public List<MDict_Company_Sheet> CompanyDic; // 园区列表。

	public String CompulsoryInsurance;
	public String SquareWeightRatio;
	public String ISEvaluate;

}
