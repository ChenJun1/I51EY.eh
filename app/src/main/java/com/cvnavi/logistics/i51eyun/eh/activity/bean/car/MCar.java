package com.cvnavi.logistics.i51eyun.eh.activity.bean.car;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;

import java.io.Serializable;

//import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;


/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 车辆列表
 * @date 2016-5-17 上午11:24:33
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressWarnings("serial")
public class MCar extends PagingBean implements Serializable {

	public String CarCode_Key;

	public String CarCode;

	public String CarType;

	public String CarType_Oid;

	public String Size;

	public String User_Tel;

	public String TransportArea;

	public String TransportArea_Oid;

}
