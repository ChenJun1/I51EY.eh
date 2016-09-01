package com.cvnavi.logistics.i51eyun.eh.activity.login.presenter;

import android.content.Context;
import android.os.Handler;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.AdvertPicBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.login.LoginData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.verifycode.VerifyCodeBase;
import com.cvnavi.logistics.i51eyun.eh.activity.login.bean.UserBean;
import com.cvnavi.logistics.i51eyun.eh.activity.login.db.user_db;
import com.cvnavi.logistics.i51eyun.eh.activity.login.model.IUserBiz;
import com.cvnavi.logistics.i51eyun.eh.activity.login.model.OnLoginListener;
import com.cvnavi.logistics.i51eyun.eh.activity.login.model.UserBiz;
import com.cvnavi.logistics.i51eyun.eh.activity.login.view.IUserLoginView;
import com.cvnavi.logistics.i51eyun.eh.manager.ReceiveGPSDataManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

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
public class UserLoginPresenter {
	private IUserBiz iUserBiz;
	private IUserLoginView iUserLoginView;

	private Handler mHandler = new Handler();
	private user_db db;
	private Context context;

	public UserLoginPresenter(IUserLoginView iUserLoginView, Context context) {
		this.iUserBiz = new UserBiz();
		this.iUserLoginView = iUserLoginView;
		db = new user_db();
		this.context = context;
	}

	public void login() {
		iUserLoginView.showLoading();
		iUserBiz.onLogin(iUserLoginView.getUserName(), iUserLoginView.getUserPassword(), iUserLoginView.getIsCheck(),
				new OnLoginListener() {
					@Override
					public void loginSuccess(final String str) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (iUserLoginView.getIsCheck()) {
									if (db.getFindFirst(iUserLoginView.getUserName())) {
										UserBean user = new UserBean();
										user.setUserName(iUserLoginView.getUserName());
										user.setUserPassword(iUserLoginView.getUserPassword());
										user.setCheckBox(iUserLoginView.getIsCheck());
										db.addUser(user);
									}
								}

								try {
									DataResultBase dataResultBase = JsonUtils.parseDataResultBase(str);
									if (dataResultBase.isSuccess()) {
										LoginData bean = (LoginData) JsonUtils.parseData(str, LoginData.class);
										if (bean != null) {
												bean.setUser_Tel(iUserLoginView.getUserName());
												MyApplication.getInstatnce().setUserKey(bean.getUser_Key());
												MyApplication.getInstatnce().setToken(bean.getToken());
												MyApplication.getInstatnce().setUserTypeOid(bean.getUserType_Oid());
												MyApplication.getInstatnce().setCompany_Oid(bean.getCompany_Oid());
												MyApplication.getInstatnce().setUserTel(iUserLoginView.getUserName());
												MyApplication.getInstatnce().setLoginData(bean);

												MyApplication.getInstatnce().getBasicDataBuffer().init();
												// 发送GPS数据。
												ReceiveGPSDataManager.getInstance().sendGPSData();

												// 获取极光推送ID，传给服务端。
												jPushForRegistrationID(iUserLoginView.getJPushRegistrationID());
												iUserLoginView.hideLoading();
												iUserLoginView.toLoginActivity(bean,false);
											}
									} else {
										iUserLoginView.hideLoading();
										iUserLoginView.toLoginFailed(Constants.LoginerrorMsg);
									}
										
								} catch (JsonParseException e1) {
									e1.printStackTrace();
								} catch (JsonMappingException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
					}

					@Override
					public void selectUserdb(UserBean userBean) {
					}

					@Override
					public void loginFailed(final JSONObject json) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								iUserLoginView.hideLoading();
								iUserLoginView.toLoginFailed("服务端返回过来的");
							}
						});
					}

					@Override
					public void loginError(final String str) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								iUserLoginView.hideLoading();
								iUserLoginView.loginError(str);
							}
						});
					}

					@Override
					public void getAppVersion(String str) {
						
					}

					@Override
					public void successLoadPic(List<AdvertPicBean> advList) {
						
					}

					@Override
					public void successgetAppDwonUrl(AppUpdateBean appBean) {
						
					}
				});
	}

	public void getVerificationCode() {
		iUserLoginView.showLoading();
		iUserBiz.getVerificationCode(iUserLoginView.getUserName(), new OnLoginListener() {
			@Override
			public void loginError(final String str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();
						iUserLoginView.loginError(str);
					}
				});
			}

			public void loginSuccess(final String str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();

						try {
							DataResultBase dataResultBase = JsonUtils.parseDataResultBase(str);
							if (dataResultBase.isSuccess()) {
								VerifyCodeBase bean = (VerifyCodeBase) JsonUtils.parseData(str, VerifyCodeBase.class);
								if (bean != null) {
									MyApplication.getInstatnce().setUUID(bean.getUUID());
									iUserLoginView.setUserPassword(bean.getVerifyCode());
									// 预留方法。
									iUserLoginView.toRegistered(bean.getUUID());
								}
							} else {
								iUserLoginView.toLoginFailed(dataResultBase.getErrorText());
								//iUserLoginView.toLoginFailed(Constants.LoginerrorMsg);
							}
						} catch (JsonParseException e1) {
							e1.printStackTrace();
						} catch (JsonMappingException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
			}

			@Override
			public void selectUserdb(UserBean userBean) {
			}

			@Override
			public void loginFailed(JSONObject str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();
						iUserLoginView.toLoginFailed("请求服务失败！");
					}
				});
			}

			@Override
			public void getAppVersion(String str) {
				
			}

			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
				
			}

			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
				
			}
		});
	}
	
	public void getVerSion() {
		iUserLoginView.showLoading();
		iUserBiz.getAppVersion(new OnLoginListener() {
			
			@Override
			public void selectUserdb(UserBean userBean) {
				
			}
			
			@Override
			public void loginSuccess(final String str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();
						iUserLoginView.loginError(str);
					}
				});
			}
			
			@Override
			public void loginFailed(JSONObject str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();
						iUserLoginView.toLoginFailed("请求服务失败！");
					}
				});
			}
			
			@Override
			public void loginError(String str) {
				iUserLoginView.hideLoading();
				iUserLoginView.toLoginFailed(str);
			}
			
			@Override
			public void getAppVersion(String str) {
				DataResultBase dataResultBase;
				try {
					dataResultBase = JsonUtils.parseDataResultBase(str);
				
					if (dataResultBase.isSuccess()) {
							JSONObject object=new JSONObject(String.valueOf(dataResultBase.getDataValue()));
							String AndroidVersion = (String) object.get("AndroidVersion");
							MyApplication.getInstatnce().setAndroidVersion(AndroidVersion);
					}
				} catch( Exception e) {
					e.printStackTrace();
				}
				iUserLoginView.isGetVersionSuccess();
				
			}

			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
			}

			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
				
			}
		});
	}

	/**
	 * 判断是否有保存过的用户名
	 */
	public void getUserBean() {
		iUserBiz.getUsername(true, new OnLoginListener() {
			@Override
			public void loginFailed(JSONObject str) {
			}

			@Override
			public void loginError(String str) {
			}

			@Override
			public void loginSuccess(String str) {
			}

			@Override
			public void selectUserdb(UserBean userBean) {
				if (userBean != null) {
					iUserLoginView.getUserBean(userBean);
				}
			}

			@Override
			public void getAppVersion(String str) {
			}

			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
				
			}

			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
				
			}
		});
	}

	private void jPushForRegistrationID(String registrationID) {
		iUserBiz.onJPushForRegistrationID(registrationID, new OnLoginListener() {
			@Override
			public void selectUserdb(UserBean userBean) {
			}

			@Override
			public void loginSuccess(String str) {
				LogUtil.d("-->>请求成功:" + str);
			}

			@Override
			public void loginFailed(JSONObject str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.toLoginFailed("请求服务失败！");
					}
				});
			}

			@Override
			public void loginError(final String str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.toLoginFailed("请求服务失败！原因：" + str);
					}
				});
			}

			@Override
			public void getAppVersion(String str) {
			}

			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
			}

			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
				
			}
		});
	}
	
	@SuppressWarnings("unused")
	public void onTokenLogin(LoginData bean) {
		iUserBiz.onTokenLogin( bean,new OnLoginListener() {
			
			@Override
			public void selectUserdb(UserBean userBean) {
				
			}
			
			@Override
			public void loginSuccess( final String str) {
			
				mHandler.post(new Runnable() {
					@Override
					public void run() {
					
						try {
							DataResultBase dataResultBase = JsonUtils.parseDataResultBase(str);
							if (dataResultBase.isSuccess()) {
								LoginData bean = (LoginData) JsonUtils.parseData(str, LoginData.class);
								if (bean != null) {
										bean.setUser_Tel(iUserLoginView.getUserName());
										MyApplication.getInstatnce().setUserKey(bean.getUser_Key());
										MyApplication.getInstatnce().setToken(bean.getToken());
										MyApplication.getInstatnce().setUserTypeOid(bean.getUserType_Oid());
										MyApplication.getInstatnce().setCompany_Oid(bean.getCompany_Oid());
										MyApplication.getInstatnce().setUserTel(iUserLoginView.getUserName());
										MyApplication.getInstatnce().setLoginData(bean);

										MyApplication.getInstatnce().getBasicDataBuffer().init();
										// 发送GPS数据。
										ReceiveGPSDataManager.getInstance().sendGPSData();

										// 获取极光推送ID，传给服务端。
										jPushForRegistrationID(iUserLoginView.getJPushRegistrationID());
										//iUserLoginView.hideLoading();
										iUserLoginView.toLoginActivity(bean,true);
									}
//								}
							} else {
								iUserLoginView.hideLoading();
							}
								
						} catch (JsonParseException e1) {
							e1.printStackTrace();
						} catch (JsonMappingException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
			
			}
			
			@Override
			public void loginFailed(JSONObject str) {
				
			}
			
			@Override
			public void loginError(final String str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();
						iUserLoginView.loginError(str);
					}
				});
			}
			
			@Override
			public void getAppVersion(String str) {
				
			}

			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
				
			}

			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
			}
		});
	}
	public void onLoadPic() {
		iUserBiz.loadAdvertPic(new OnLoginListener() {
			
			@Override
			public void selectUserdb(UserBean userBean) {
				
			}
			
			@Override
			public void loginSuccess( final String str) {
				
			}
			
			@Override
			public void loginFailed(JSONObject str) {
				
			}
			
			@Override
			public void loginError(final String str) {
			}
			
			@Override
			public void getAppVersion(String str) {
			}
			
			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.successPicToHome();
					}
				});
			}

			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
			}
		});
	}
	public void getAppDownUrl() {
		iUserBiz.getAppDwonUrl(new OnLoginListener() {
			
			@Override
			public void successgetAppDwonUrl(AppUpdateBean appBean) {
				iUserLoginView.successGetDwonAppUrl(appBean);
			}
			
			@Override
			public void successLoadPic(List<AdvertPicBean> advList) {
				
			}
			
			@Override
			public void selectUserdb(UserBean userBean) {
				
			}
			
			@Override
			public void loginSuccess(String str) {
				
			}
			
			@Override
			public void loginFailed(JSONObject str) {
				
			}
			
			@Override
			public void loginError(final String str) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						iUserLoginView.hideLoading();
					}
				});
			}
			
			@Override
			public void getAppVersion(String str) {
				
			}
		});
	}
	
	
	

}
