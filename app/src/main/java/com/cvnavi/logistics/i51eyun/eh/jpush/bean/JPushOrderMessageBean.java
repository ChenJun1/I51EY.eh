package com.cvnavi.logistics.i51eyun.eh.jpush.bean;

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
public class JPushOrderMessageBean extends JPushMessageBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1704163013577710527L;

	public String MessageType;

	public String DataValue;
	
	public String User_Key;
	
	public String UserType_Oid;
	
	public String Operate_Code;
	
	// 自定义消息内容，方便传值。跟服务端无关。
	public String Message_Content;
	
}
