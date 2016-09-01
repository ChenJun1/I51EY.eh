package com.cvnavi.logistics.i51eyun.eh.activity.bean.order;

import java.io.Serializable;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description 订单信息。
 * @date 2016-5-17 上午11:29:58
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class OrderBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2886333753902241698L;

	/**
	 * 订单Key
	 */
	public String Order_Key;
	
	/// <summary>
	/// 收货收藏联系人Key
	/// </summary>
	public String Consignee_Contacts_Key;

	// / <summary>
	// / 收货人的key
	public String Consignee_Key;

	// 收货人
	// / <summary>
	// / 收货人姓名
	// / </summary>
	public String Consignee_Name;

	// / <summary>
	// / 收货人电话
	// / </summary>
	public String Consignee_Tel;

	// / <summary>
	// / 收货地址
	// / </summary>
	public String Consignee_Address;

	// / <summary>
	// / 收货地址省
	// / </summary>
	public String Consignee_Provice;

	// / <summary>
	// / 收货地址市
	// / </summary>
	public String Consignee_City;

	// / <summary>
	// / 收货地址区
	// / </summary>
	public String Consignee_Area;

	/// <summary>
	/// 发货收藏联系人Key
	/// </summary>
	public String Shipper_Contacts_Key;

	// 发货人
	// / <summary>
	// / 发货人姓名
	// / </summary>
	public String Shipper_Name;

	// / <summary>
	// / 发货人电话
	// / </summary>
	public String Shipper_Tel;

	// / <summary>
	// / 发货地址
	// / </summary>
	public String Shipping_Address;

	// / <summary>
	// / 发货地址省
	// / </summary>
	public String Shipping_Provice;

	// / <summary>
	// / 发货地址市
	// / </summary>
	public String Shipping_City;

	// / <summary>
	// / 发货地址区
	// / </summary>
	public String Shipping_Area;

	// 货物信息
	// / <summary>
	// / 物品类型 普货：A 液体：B 化学制剂：C
	// / </summary>
	public String GoodsType;

	/**
	 * 物品类型ID
	 */
	public String GoodsType_Oid;

	// / <summary>
	// / 规则物品 A-规则物品 B-不规则物品
	// / </summary>
	public String GoodsShape;

	/**
	 * 规则物品ID
	 */
	public String GoodsShape_Oid;

	// / <summary>
	// / 重量
	// / </summary>
	public String Weight;

	// / <summary>
	// / 方量
	// / </summary>
	public String Volume;

	// / <summary>
	// / 运输费用
	// / </summary>
	public String OrderFee;

	// / <summary>
	// / 强制保险费
	// / </summary>
	public String CompulsoryInsurance;

	// / 总费用
	public String TotalFee;

	// / <summary>
	// / 提货时间
	// / </summary>
	public String DeliveryTime;

	// / <summary>
	// / 期望收货时间
	// / </summary>
	public String ReceivingTime;

	// / <summary>
	// / 时效
	// / </summary>
	public String EstimateTotalTime;

	// / <summary>
	// / 指定车辆Key
	// / </summary>
	public String CarCode_Key;

	/**
	 * 车牌号。
	 */
	public String CarCode;

	/**
	 * 件数。
	 */
	public String Number;

	// 是否提货拍照(0：否，1：是)
	public String IsTHPhoto;

	// 是否交货拍照(0：否，1：是)
	public String IsJHPhoto;

	/// 包装类型ID
	/// </summary>
	public String PackageType_Oid;

	/// 包装类型
	/// </summary>
	public String PackageType;

	// 包装类型名称
	public String PackageType_Name;

	/**
	 * 物品名称。
	 */
	public String GoodsName;
	public String GoodsName_Oid;
	
	

}
