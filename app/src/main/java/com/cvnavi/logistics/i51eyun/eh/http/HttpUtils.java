package com.cvnavi.logistics.i51eyun.eh.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 未完待续 
 * @date 2016-5-17 下午1:19:57
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class HttpUtils {

	public static void postRequest() {
		JSONObject mjson = new JSONObject();
		try {
			mjson.put("Action", "HttpUserLogin");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestParams params = new RequestParams("http://api.dangqian.com/apidiqu2/api.asp?id=220000000000");
		params.addBodyParameter(null, mjson.toString());

		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
				} else {
				}
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(String str) {
			}
		});
	}

	public static void getRequest() {
		RequestParams reParams = new RequestParams("http://apis.baidu.com/apistore/weatherservice/citylist");
		x.http().get(reParams, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(String str) {
			}
		});
	}

}
