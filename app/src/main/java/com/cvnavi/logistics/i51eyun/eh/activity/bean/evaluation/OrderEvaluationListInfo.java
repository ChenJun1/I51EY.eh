package com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation;

import java.io.Serializable;

/**
 * 我的评价列表信息。
 * @author JohnnyYuan
 *
 */
public class OrderEvaluationListInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8559307338125366146L;
	/// <summary>
	/// 订单号
	/// </summary>
	public String Serial_Oid;
	/// <summary>
	/// 订单key
	/// </summary>
	public String Order_Key;
	/// <summary>
	/// 货主评价-得分
	/// </summary>
	public String HZPJ_SCORE;
	/// <summary>
	/// 司机评价-得分
	/// </summary>
	public String SJPJ_SCORE;
	/// <summary>
	/// 车牌号
	/// </summary>
	public String CarCode;
	/// <summary>
	/// 发货市
	/// </summary>
	public String Shipping_City;
	/// <summary>
	/// 收货市
	/// </summary>
	public String Consignee_City;

}
