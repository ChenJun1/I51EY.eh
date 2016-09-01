package com.cvnavi.logistics.i51eyun.eh.activity.bean.verifycode;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:00:18
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class VerifyCodeBase {

	// 唯一标识符
	@JsonProperty("UUID")
	private String UUID;

	// 验证码
	@JsonProperty("VerifyCode")
	private String VerifyCode;

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getVerifyCode() {
		return VerifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		VerifyCode = verifyCode;
	}

}
