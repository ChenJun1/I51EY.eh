package com.cvnavi.logistics.i51eyun.eh.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyCarNumUtil {

	public static boolean checkCarNum(String carNum) {
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}");
		
		Matcher matcher = pattern.matcher(carNum);
		if (!matcher.matches()) {
			return false;
		} else {
			return true;
		}
	}
	
}
