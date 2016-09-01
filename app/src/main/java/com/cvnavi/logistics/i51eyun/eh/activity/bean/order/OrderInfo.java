package com.cvnavi.logistics.i51eyun.eh.activity.bean.order;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description 订单明细表。
 * @date 2016-5-17 上午11:30:11
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class OrderInfo extends OrderBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1347422969907678764L;

	// / <summary>
	// / 订单流水号
	// / </summary>
	public String Serial_Oid;

	// / <summary>
	// / 订单状态
	// / </summary>
	public String Operate_Code;

	// / 订单状态 A - 公共 B - 指定
	// / </summary>
	public String Order_Type;

	// / <summary>
	// / 接单司机姓名
	// / </summary>
	public String Received_Orders_Driver_Name;

	// / <summary>
	// / 接单司机号码
	// / </summary>
	public String Received_Orders_Driver_Tel;

	// / <summary>
	// / 订单发布时间
	// / </summary>
	public String ReleaseTime;

	// / <summary>
	// / 订单接取时间
	// / </summary>
	public String Received_Orders_Time;

	// / <summary>
	// / 订单完成时间
	// / </summary>
	public String Completed_Orders_Time;
	
	/**
	 * 已提货时间。（司机实际提货时间）
	 */
	public String Goods_Loading_Time;

	/**
	 * 交货时间。
	 */
	public String Goods_Unload_Time;

	/**
	 * 实际收货人。
	 */
	public String Actual_Consignee;

	/**
	 * 发布订单人。
	 */
	public String Name;

	/**
	 * 提货照片。
	 */
	public String TH_Photo;

	/**
	 * 交货照片。
	 */
	public String JH_Photo;

	/**
	 * 交货完成时间
	 */
	public String ApplyCompleted_Orders_Time;

	/**
	 * 司机是否评价成功0 否 1 是
	 */
	public String IsDESuccess;

	/**
	 * 货主是否评价成功0 否 1 是
	 */
	public String IsCESuccess;
	
    /// 货主取消发货时间
    public String CalShipperRule;
    
    /// 司机取消接单时间
    public String CalAcceptOrdRule;


}
