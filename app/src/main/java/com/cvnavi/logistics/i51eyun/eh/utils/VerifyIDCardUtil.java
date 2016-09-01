package com.cvnavi.logistics.i51eyun.eh.utils;

import android.text.TextUtils;

public class VerifyIDCardUtil {

	public static boolean isIDCard(String IDCardNum) {
		String isIDCard = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
		if (TextUtils.isEmpty(IDCardNum)) {
			return false;
		}

		return IDCardNum.matches(isIDCard);
	}
	
}
