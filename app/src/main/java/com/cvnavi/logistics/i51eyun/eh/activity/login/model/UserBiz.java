package com.cvnavi.logistics.i51eyun.eh.activity.login.model;

import android.text.TextUtils;
import android.util.Log;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.AdvertPicBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.login.LoginData;
import com.cvnavi.logistics.i51eyun.eh.activity.login.db.user_db;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class UserBiz implements IUserBiz {
	private static String TAG = "UserBiz";

	@Override
	public void onLogin(final String userPhone, final String verifyCode, final boolean isCheckBox,
			final OnLoginListener onLoginListener) {
		new Thread() {
			@Override
			public void run() {
				if (TextUtils.isEmpty(userPhone)) {
					onLoginListener.loginError("请输入手机号码！");
					return;
				}

				if (!ContextUtil.isMobileNO(userPhone)) {
					onLoginListener.loginError("请输入正确的手机号码！");
					return;
				}
				
				if (TextUtils.isEmpty(verifyCode)) {
					onLoginListener.loginError("请输入验证码！");
					return;
				}
				
				if (!ContextUtil.isNumber(verifyCode)) {
					onLoginListener.loginError("请输入正确的验证码！");
					return;
				}
				
				if (!isCheckBox) {
					onLoginListener.loginError("请先同意《e货APP使用服务协议》");
					return;
				}
				LoginData loginData = new LoginData();
				loginData.setUser_Tel(userPhone);
				loginData.setVerifyCode(verifyCode);
				loginData.setUUID(MyApplication.getInstatnce().getUUID());

				DataRequestData requestData = new DataRequestData();
				RequestParams params = new RequestParams(Constants.Login_URL);
				try {
					requestData.setDataValue(JsonUtils.toJsonData(loginData));
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						Log.d(TAG, "-->>取消请求");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						Log.d(TAG, "-->>请求错误:" + ex.getMessage());
						
						onLoginListener.loginError(Constants.errorMsg);
					}

					@Override
					public void onFinished() {
						Log.d(TAG, "-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						Log.d(TAG, "-->>请求成功：" + str);
						onLoginListener.loginSuccess(str);
					}
				});
			}
		}.start();
	}

	/**
	 * 获取验证码
	 *
	 * @param userPhone
	 * @param
	 */
	@Override
	public void getVerificationCode(final String userPhone, final OnLoginListener onLoginListener) {
		new Thread() {
			@Override
			public void run() {
				if (TextUtils.isEmpty(userPhone)) {
					onLoginListener.loginError("请输入手机号码！");
					return;
				}

				if (!ContextUtil.isMobileNO(userPhone)) {
					onLoginListener.loginError("请输入正确的手机号码！");
					return;
				}

				DataRequestData requestData = new DataRequestData();
				requestData.setDataValue(userPhone);

				RequestParams params = new RequestParams(Constants.VerifyCode_URL);
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						Log.d(TAG, "-->>取消请求");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						Log.d(TAG, "-->>请求失败:" + ex.getMessage());
						onLoginListener.loginError(ex.getMessage());
					}

					@Override
					public void onFinished() {
						Log.d(TAG, "-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						Log.d(TAG, "-->>请求成功：" + str);
						onLoginListener.loginSuccess(str);
					}
				});
			}
		}.start();
	}

	@Override
	public void getUsername(final boolean isCheckBox, final OnLoginListener onLoginListener) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				onLoginListener.selectUserdb(new user_db().getFindFirst(true));
			}
		}.start();
	}

	@Override
	public void onJPushForRegistrationID(final String registrationID, final OnLoginListener listener) {
		new Thread() {
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setDataValue(registrationID);

				RequestParams params = new RequestParams(Constants.RequestJPushID_URL);
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						Log.d(TAG, "-->>取消请求");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						Log.d(TAG, "-->>请求错误，error:" + ex.getMessage());
						listener.loginError(Constants.errorMsg);
					}

					@Override
					public void onFinished() {
						Log.d(TAG, "-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						Log.d(TAG, "-->>请求成功：" + str);
						listener.loginSuccess(str);
					}
				});
			}
		}.start();
	}
	
	/**
	 * 得到app版本
	 */
	@Override
	public void getAppVersion(final OnLoginListener listener) {
		new Thread() {
			public void run() {

				x.http().post(new RequestParams(Constants.GetAppVersion_URL), new Callback.CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						Log.d(TAG, "-->>取消请求");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						Log.d(TAG, "-->>请求错误，error:" + ex.getMessage());
						listener.loginError(Constants.errorMsg);
					}

					@Override
					public void onFinished() {
						Log.d(TAG, "-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						Log.d(TAG, "-->>请求成功：" + str);
						
						listener.getAppVersion(str);
					}
				});
			}
		}.start();
	}
	//七天免登陆
	@Override
	public void onTokenLogin(final LoginData beanData,final OnLoginListener listener) {
		new Thread() {
			
			@Override
			public void run() {
				LoginData loginData = new LoginData();
				loginData.setUser_Tel(beanData.getUser_Tel());

				RequestParams params = new RequestParams(Constants.LoginByValidToken);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(beanData.getUserType_Oid());
					requestData.setToken(beanData.getToken());
					requestData.setUser_Key(beanData.getUser_Key());
					requestData.setCompany_Oid(beanData.getCompany_Oid());
					JSONObject object=new JSONObject();
					object.put("User_Tel",beanData.getUser_Tel());
					requestData.setDataValue(object.toString());

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
						Log.d(TAG, "-->>取消请求");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						Log.d(TAG, "-->>请求错误:" + ex.getMessage());
						listener.loginError(Constants.errorMsg);
					}

					@Override
					public void onFinished() {
						Log.d(TAG, "-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						Log.d(TAG, "-->>请求成功：" + str);
						listener.loginSuccess(str);
					}
				});
			}
		}.start();
	}

	@Override
	public void loadAdvertPic(final OnLoginListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.GetAdvertPic_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getLoginData().getCompany_Oid());
					requestData.setDataValue(null);
					
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}
					
					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						listener.successLoadPic(null);
					}
					
					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}
					
					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase daResultBase = JsonUtils
									.parseDataResultBase(str);
							if (daResultBase.isSuccess()) {
								List<AdvertPicBean> advertPicList = JsonUtils
										.parseadvertPicList(daResultBase
												.getDataValue().toString());
								if (advertPicList != null) {
									if (advertPicList.size() > 0) {
										for (int i = 0; i < advertPicList.size(); i++) {
											Constants.mImageUrl.add(advertPicList.get(i).AdvertPic);
										}
									}
									listener.successLoadPic(null);
								} else {
									listener.successLoadPic(null);
								}
							} else {
								listener.successLoadPic(null);
							}
//								 Constants.mImageUrl.add("http://10.10.11.124:8181/ADVIMG/5ef2633f-8137-43b2-96c2-28f00966dd6f.png");
//								 Constants.mImageUrl.add("http://10.10.11.124:8181/ADVIMG/5e4deccd-db26-46db-9e92-6a7a935274ac.png");
//								 Constants.mImageUrl.add("http://10.10.11.124:8181/ADVIMG/e9531e54-82e6-49ca-bc8b-1c877a004798.png");
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
			}
		}).start();
	}

	@Override
	public void getAppDwonUrl(final OnLoginListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.GetAppDownPath_URL);
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}
					
					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						listener.loginError(Constants.errorMsg);
					}
					
					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}
					
					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase daResultBase = JsonUtils
									.parseDataResultBase(str);
							if (daResultBase.isSuccess()&&daResultBase.getDataValue()!=null) {
								 
								AppUpdateBean bean=JsonUtils.parseData2(daResultBase.getDataValue().toString(), AppUpdateBean.class);
								listener.successgetAppDwonUrl(bean);
							} else {
								listener.loginError(daResultBase.getErrorText());
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
			}
		}).start();
	}
	
}
