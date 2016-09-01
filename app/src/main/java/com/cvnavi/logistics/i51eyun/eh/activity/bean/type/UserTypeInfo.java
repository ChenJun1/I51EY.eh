package com.cvnavi.logistics.i51eyun.eh.activity.bean.type;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description 用户类型信息。
 * @date 2016-5-17 下午12:59:43
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class UserTypeInfo {

	public enum UserType {
		A, B, C, D,
	}

	public static String getUsetTypeName(UserType userType) {
		switch (userType) {
		case A:
			return "司机";
		case B:
			return "货主";
		case C:
			return "园区用户";
		case D:
			return "游客";
		default:
			return "用户类型错误！";
		}
	}
}
