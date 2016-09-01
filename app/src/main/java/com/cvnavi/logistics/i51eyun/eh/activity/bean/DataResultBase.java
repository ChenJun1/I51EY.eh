package com.cvnavi.logistics.i51eyun.eh.activity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:00:29
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class DataResultBase {

//	// 返回的数据为对应实体的json字符串
	@JsonProperty("DataValue")
	private Object DataValue;

	// 返回状态
	@JsonProperty("Success")
	private boolean Success;

	// 错误信息
	@JsonProperty("ErrorText")
	private String ErrorText;

	// 返回行数
	@JsonProperty("RowCount")
	private int RowCount;

	public Object getDataValue() {
		return DataValue;
	}

	public void setDataValue(Object dataValue) {
		DataValue = dataValue;
	}

	public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}

	public String getErrorText() {
		return ErrorText;
	}

	public void setErrorText(String errorText) {
		ErrorText = errorText;
	}

	public int getRowCount() {
		return RowCount;
	}

	public void setRowCount(int rowCount) {
		RowCount = rowCount;
	}
	
	

}
