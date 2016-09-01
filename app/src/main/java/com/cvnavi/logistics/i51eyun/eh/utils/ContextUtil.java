package com.cvnavi.logistics.i51eyun.eh.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.type.WorkStatusType;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:23:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("SimpleDateFormat")
public class ContextUtil {
	/**
	 * 手机正则表达式 Mobile phone a regular expression
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][7358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 密码正则表达式 Password regular expressions
	 * 
	 * @param pwd
	 * @return
	 */
	public static boolean isPassword(String pwd) {
		String telRegex = "^[0-9A-Za-z]{4,12}$";
		if (TextUtils.isEmpty(pwd))
			return false;
		else
			return pwd.matches(telRegex);
	}

	public static boolean isNumber(String pwd) {
		String telRegex = "^\\d{4}$";
		if (TextUtils.isEmpty(pwd))
			return false;
		else
			return pwd.matches(telRegex);
	}

	public static boolean isIDCard(String IDCardNum) {
		String isIDCard = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
		if (TextUtils.isEmpty(IDCardNum)) {
			return false;
		}

		return IDCardNum.matches(isIDCard);
	}

	// AA 待接单
	// AB 已接单
	// AC 已装车
	// AD 运输中
	// AE 已卸车
	// AF 已交货
	// AG 已完成
	public static String getOperateCodeContent(String operateCode, String isTHPhoto, String isJHPhoto,
			String userTypeOid) {
		if (TextUtils.isEmpty(operateCode)) {
			return "";
		}

		if (operateCode.equals("AA")) {
			return "待接单";
		} else if (operateCode.equals("AB")) {
			if (userTypeOid.equals("A")) { // 司机。
				if (TextUtils.isEmpty(isTHPhoto) || isTHPhoto.equals("0")) {
					return "交货"; // 交货
				}
				
				if (isTHPhoto.equals("1")) {
					return "提货"; // 提货。
				}
			} else if (userTypeOid.equals("B")) { // 货主
				return "已接单";
			}
		} else if (operateCode.equals("AC")) {
			if (userTypeOid.equals("A")) { // 司机。交货。
				return "交货";
			} else if (userTypeOid.equals("B")) { // 货主
				return "已接单";
			}
		} else if (operateCode.equals("AD")) {
			return "运输中";
		} else if (operateCode.equals("AE")) {
			return "已卸车";
		} else if (operateCode.equals("AF")) {
			if (userTypeOid.equals("A")) { // 司机。 已交货。
				return "已交货";
			} else if (userTypeOid.equals("B")) { // 货主。待确认。
				return "待确认";
			}
		} else if (operateCode.equals("AG")) {
			return "已完成";
		}
		return "";

		// if (TextUtils.isEmpty(operateCode)) {
		// return "";
		// }
		//
		// if (operateCode.equals("AA")) {
		// return "待接单";
		// } else if (operateCode.equals("AB")) {
		// if (userTypeOid.equals("A")) {
		// return "交货";
		// } else if (userTypeOid.equals("B")) {
		// return "已接单";
		// }
		// } else if (operateCode.equals("AC")) {
		// return "已装车";
		// } else if (operateCode.equals("AD")) {
		// return "运输中";
		// } else if (operateCode.equals("AE")) {
		// return "已卸车";
		// } else if (operateCode.equals("AF")) {
		// if (userTypeOid.equals("A")) {
		// return "已交货";
		// } else if (userTypeOid.equals("B")) {
		// return "待确认";
		// }
		// } else if (operateCode.equals("AG")) {
		// return "已完成";
		// }
		// return "";
	}

	public static String getOperateCode(String operateCodeContent) {
		if (TextUtils.isEmpty(operateCodeContent)) {
			return "";
		}

		if (operateCodeContent.equals("待接单")) {
			return "AA";
		} else if (operateCodeContent.equals("交货") || operateCodeContent.equals("已接单")) {
			return "AB";
		} else if (operateCodeContent.equals("已装车")) {
			return "AC";
		} else if (operateCodeContent.equals("运输中")) {
			return "AD";
		} else if (operateCodeContent.equals("已卸车")) {
			return "AE";
		} else if (operateCodeContent.equals("已交货") || operateCodeContent.equals("待确认")) {
			return "AF";
		} else if (operateCodeContent.equals("已完成")) {
			return "AG";
		}
		return "";
	}

	// orderType A：公共订单 B： 指定订单。
	public static int getOperateStatusBg(String operateCode, String orderType) {
		if (operateCode.equals("AA")) {
			if (TextUtils.isEmpty(orderType)) {
				return R.drawable.waiting_order_status;
			} else if (orderType.equals("A")) {
				return R.drawable.waiting_order_status;
			} else if (orderType.equals("B")) {
				return R.drawable.waiting_order_appoint_status;
			}
		} else if (operateCode.equals("AB")) {
		} else if (operateCode.equals("AC")) {
			return R.drawable.order_received_stytes;
		} else if (operateCode.equals("AD")) {
		} else if (operateCode.equals("AE")) {
		} else if (operateCode.equals("AF")) {
			return R.drawable.delivery_order_status;
		} else if (operateCode.equals("AG")) {
			return R.drawable.completed_order_status;
		}
		return R.drawable.completed_order_status;
	}

	public static boolean isEnableOperate(String operateCode, String isTHPhoto, String isJHPhoto, String userTypeOid) {
		if (TextUtils.isEmpty(operateCode)) {
			return false;
		}

		if (operateCode.equals("AA")) {
			return false;
		} else if (operateCode.equals("AB")) {
			if (userTypeOid.equals("A")) { // 司机。
				if (TextUtils.isEmpty(isTHPhoto) || isTHPhoto.equals("0")) {
					return true; // 交货
				}
				
				if (isTHPhoto.equals("1")) {
					return true; // 提货。
				}
			} else if (userTypeOid.equals("B")) { // 货主
				return false;
			}
		} else if (operateCode.equals("AC")) {
			if (userTypeOid.equals("A")) { // 司机。交货。
				return true;
			} else if (userTypeOid.equals("B")) { // 货主
				return false;
			}
		} else if (operateCode.equals("AD")) {
			return false;
		} else if (operateCode.equals("AE")) {
			return false;
		} else if (operateCode.equals("AF")) {
			if (userTypeOid.equals("A")) { // 司机。 已交货。
				return false;
			} else if (userTypeOid.equals("B")) { // 货主。待确认。
				return true;
			}
		} else if (operateCode.equals("AG")) {
			return false;
		}
		return false;
	}

	/**
	 * 取小数点后两位
	 * 
	 * @param number
	 * @return
	 */
	public static String getDouble(String number) {
		if (TextUtils.isEmpty(number)) {
			return "";
		}
		Double numbers = Double.valueOf(number);
		numbers = (double) Math.round(numbers * 100) / 100;
		String strNumber = String.valueOf(numbers);
		return strNumber;
	}

	public static String getStringDate(Long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getStringDate2(Long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static long getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		// long day = 0;
		long hour = 0;
		// long min = 0;
		// long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			// day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000)); // - day * 24
			// min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			// sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min *
			// 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// long[] times = { day, hour, min, sec };
		// return times;
		return hour;
	}

	public static long getDistanceDays(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		// long hour = 0;
		// long min = 0;
		// long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			// hour = (diff / (60 * 60 * 1000)); // - day * 24
			// min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			// sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min *
			// 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// long[] times = { day, hour, min, sec };
		// return times;
		return day;
	}
	/**
	 *	比较当前时间
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static long getDistanceTimehour(String nowTime, String dataTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		// long day = 0;
		long hour = 0;
		// long min = 0;
		// long sec = 0;
		try {
			one = df.parse(nowTime);
			two = df.parse(dataTime);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				return -1;
			}
			// day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000)); // - day * 24
			// min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			// sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min *
			// 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// long[] times = { day, hour, min, sec };
		// return times;
		return hour;
	}
	

	public static int compareDate(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(str1);
			Date dt2 = df.parse(str2);
			if (dt1.getTime() > dt2.getTime()) { // str1>str2
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {  // str1<str2
				return -1;
			} else {
				return 0; // str1=str2
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	public static int compareDate2(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date dt1 = df.parse(str1);
			Date dt2 = df.parse(str2);
			if (dt1.getTime() > dt2.getTime()) { // str1>str2
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {  // str1<str2
				return -1;
			} else {
				return 0; // str1=str2
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static String getWorkStatusStr(int workStatus) {
		switch (workStatus) {
		case WorkStatusType.OffWork:
			return "我要上班";
		case WorkStatusType.OnWork:
			return "我要下班";
		default:
			break;
		}
		return "我要上班";
	}

	public static int getWorkStatus(String workStatus) {
		switch (workStatus) {
		case "我要下班":
			return WorkStatusType.OffWork;
		case "我要上班":
			return WorkStatusType.OnWork;
		default:
			break;
		}
		return WorkStatusType.OnWork;
	}
	
	/**
	 * 去空字符
	 * @param provice
	 * @param city
	 * @param area
	 * @return
	 */
	public static String getAddress(String provice, String city, String area) {
		if (TextUtils.isEmpty(provice)) {
			provice = "";
		}
		
		if (TextUtils.isEmpty(city)) {
			city = "";
		}
		
		if (TextUtils.isEmpty(area)) {
			area = "";
		}
		
		return MessageFormat.format("{0}{1}{2}", provice, city, area);
	}
	/**
	 * 去空字符
	 * @param provice
	 * @param city
	 * @param area
	 * @param address
	 * @return
	 */
	public static String getDetailAddress(String provice, String city, String area, String address) {
		if (TextUtils.isEmpty(provice)) {
			provice = "";
		}
		
		if (TextUtils.isEmpty(city)) {
			city = "";
		}
		
		if (TextUtils.isEmpty(area)) {
			area = "";
		}
		
		if (TextUtils.isEmpty(address)) {
			address = "";
		}
		
		return MessageFormat.format("{0}{1}{2}{3}", provice, city, area, address);
	}

	public static void callAlertDialog(final String callNumber, final Context context) {
		AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
		dBuilder.setTitle("提示");
		dBuilder.setMessage("拨号：" + callNumber);
		dBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));
				context.startActivity(in);
			}
		});
		dBuilder.setNegativeButton("取消", null);
		dBuilder.create();
		dBuilder.show();
	}

	public static String getRegion(RegionBean bean) {
		StringBuilder strBuilder = new StringBuilder();

		if (bean.provinceBean != null && TextUtils.isEmpty(bean.provinceBean.PName) == false) {
			strBuilder.append(bean.provinceBean.PName);
		}

		if (bean.cityBean != null && TextUtils.isEmpty(bean.cityBean.CName) == false) {
			strBuilder.append(bean.cityBean.CName);
		}

		if (bean.areaBean != null && TextUtils.isEmpty(bean.areaBean.AName) == false) {
			strBuilder.append(bean.areaBean.AName);
		}
		return strBuilder.toString();
	}

	public static int getDeliveryNeedPhoto(String THPhoto, String JHPhoto) {
		if ((TextUtils.isEmpty(THPhoto) == false && THPhoto.equals("1"))
				|| (TextUtils.isEmpty(JHPhoto) == false && JHPhoto.equals("1"))) {
			return R.drawable.photohong;
		}

		return R.drawable.photo;
	}
	
	/**
	 * 
	 * @param num
	 * @param maxInt
	 * @return
	 */
	public static boolean CheckNumber(String num,int maxInt){
		
		int indexOf = num.indexOf(".");
		if(indexOf>0){
			String string = num.substring(0, indexOf);
			 String string2 =num.substring(indexOf, num.length());
			if(string.length()+string2.length()<=maxInt&&string2.length()>1){
				return true;
			}else{
				return false;
			}
		}else{
			if(num.length()>maxInt-3){
				return false;
			}
		}
		
		return true;
	}
	/**
	 * 计算重量
	 * @param DriverGoodsVolume
	 * @param inputVolume
	 * @param DriverGoodsWeight
	 * @return
	 */
	public static String getWeight(String DriverGoodsVolume,String  inputVolume,String DriverGoodsWeight){
		if(!TextUtils.isEmpty(DriverGoodsWeight)&&!TextUtils.isEmpty(inputVolume)&&!TextUtils.isEmpty(DriverGoodsVolume)){
			Double dVolume = Double.valueOf(inputVolume);
			Double GoodsWeight = Double.valueOf(DriverGoodsWeight);
			Double Volume = Double.valueOf(DriverGoodsVolume);
			Double getWeight=dVolume*GoodsWeight/Volume;
			double i=Math.round(getWeight * 100);
			return String.valueOf(i/ 100);
		}
		
		return "";
	}
	/**
	 * 计算方量
	 * @param DriverGoodsWeight
	 * @param inputWeight
	 * @param DriverGoodsVolume
	 * @return
	 */
	public static String getVolume(String DriverGoodsWeight,String inputWeight,String DriverGoodsVolume){
		if(!TextUtils.isEmpty(DriverGoodsWeight)&&!TextUtils.isEmpty(inputWeight)&&!TextUtils.isEmpty(DriverGoodsVolume)){
			Double dWeight = Double.valueOf(inputWeight);
			Double GoodsWeight = Double.valueOf(DriverGoodsWeight);
			Double Volume = Double.valueOf(DriverGoodsVolume);
			Double getVolume=dWeight*Volume/GoodsWeight;
			double i= Math.round(getVolume * 100);
			return String.valueOf(i/ 100);
		}
		
		return "";
		
	}
	/**
	 * 限制小数点两位
	 * @param text
	 */
	public static void money(final EditText text) {
        //    money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        text.addTextChangedListener(new TextWatcher() {
            private int count_decimal_points_ = 0;  // 标识当前是不是已经有小数点了
            private int selection_start_;           // 监听光标的位置
            private StringBuffer str_buf_;          // 缓存当前的string，用以修改内容

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().contains(".")) {
                    count_decimal_points_ = 1;
                } else {
                    count_decimal_points_ = 0;  // 因为可能存在如果是删除的话，把小数点删除的情况
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str_buf_ = new StringBuffer(s.toString().trim());
                // 先判断输入的第一位不能是小数点
                if (before == 0 && s.length() == 1 && s.charAt(start) == '.') {
                    text.setText("");
                } else if (before == 0 && count_decimal_points_ == 1) {
                    // 在判断如果当前是增加，并且已经有小数点了，就要判断输入是否合法；如果是减少不做任何判断
                    // 注意在if语句中都是在else体内调用了设置光标监听位的方法，因为在调用setText之后会出现嵌套的情况
                    // 非合法的输入包括： 1. 输入的依旧是小数点，2.小数点后位数已经达到两位了
                    if (s.charAt(start) == '.' || (start - str_buf_.indexOf(".") > 2)) {
                        str_buf_.deleteCharAt(start);
                        text.setText(str_buf_);
                    } else {
                        selection_start_ = str_buf_.length();       // 设置光标的位置为结尾
                    }
                } else {
                    selection_start_ = str_buf_.length();       // 设置光标的位置为结尾
                }
            }

			@Override
			public void afterTextChanged(Editable s) {
				 text.setSelection(selection_start_);
	                if (s != null) {
	                    try {
	                    } catch (NumberFormatException e) {
	                        e.printStackTrace();
	                    }
	                }
			}
        });
    }
}
