package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.model;

import android.text.TextUtils;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.OrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;

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
public class OrderBiz implements IOrderBiz {

	@Override
	public void waitingRefresh(final int startNum, final int findNum, final String operateCode,
			final String requestTime, final OrderListener listener) {
		new Thread() {
			public void run() {
				RequestParams pans = new RequestParams(Constants.Order_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Page_StartNum = startNum;
					orderRequestBean.Page_EndNum = findNum;
					orderRequestBean.Operate_Code = operateCode;
					orderRequestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					pans.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(pans, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String str) {
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								listener.OrderSuccess(str);
							} else {
								listener.OrderError(resultBase.getErrorText());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable ex, boolean b) {
						listener.OrderError(
								TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
					}

					@Override
					public void onCancelled(CancelledException e) {
					}

					@Override
					public void onFinished() {
					}
				});
			};
		}.start();
	}

	@Override
	public void waitingLoading(final int startNum, final int findNum, final String operateCode,
			final String requestTime, final OrderListener listener) {
		new Thread() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Order_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Page_StartNum = startNum;
					orderRequestBean.Page_EndNum = findNum;
					orderRequestBean.Operate_Code = operateCode;
					orderRequestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
					}

					@Override
					public void onError(Throwable ex, boolean arg1) {
						listener.OrderError(
								TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
					}

					@Override
					public void onFinished() {
					}

					@Override
					public void onSuccess(String str) {
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								listener.OrderSuccess(str);
							} else {
								listener.OrderError(resultBase.getErrorText());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}

			;
		}.start();
	}

	@Override
	public void receivedRefresh(final int startNum, final int findNum, final String operateCode,
			final String requestTime, final OrderListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.Order_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Page_StartNum = startNum;
					orderRequestBean.Page_EndNum = findNum;
					orderRequestBean.Operate_Code = operateCode;
					orderRequestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {
					}

					@Override
					public void onError(Throwable ex, boolean arg1) {
						listener.OrderError(
								TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
					}

					@Override
					public void onFinished() {
					}

					@Override
					public void onSuccess(String str) {
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								listener.OrderSuccess(str);
							} else {
								listener.OrderError(resultBase.getErrorText());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void receivedLoading(final int startNum, final int findNum, final String operateCode,
			final String requestTime, final OrderListener listener) {
		new Thread() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Order_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Page_StartNum = startNum;
					orderRequestBean.Page_EndNum = findNum;
					orderRequestBean.Operate_Code = operateCode;
					orderRequestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
					}

					@Override
					public void onError(Throwable ex, boolean arg1) {
						listener.OrderError(
								TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
					}

					@Override
					public void onFinished() {
					}

					@Override
					public void onSuccess(String arg0) {
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(arg0);
							if (resultBase.isSuccess()) {
								listener.OrderSuccess(arg0);
							} else {
								listener.OrderError(resultBase.getErrorText());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			};
		}.start();
	}

	@Override
	public void completedRefresh(final int startNum, final int findNum, final String operateCode,
			final String requestTime, final OrderListener listener) {
		new Thread() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Order_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Page_StartNum = startNum;
					orderRequestBean.Page_EndNum = findNum;
					orderRequestBean.Operate_Code = operateCode;
					orderRequestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new CommonCallback<String>() {
					@Override
					public void onCancelled(CancelledException arg0) {
					}

					@Override
					public void onError(Throwable ex, boolean arg1) {
						listener.OrderError(
								TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
					}

					@Override
					public void onFinished() {
					}

					@Override
					public void onSuccess(String arg0) {
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(arg0);
							if (resultBase.isSuccess()) {
								listener.OrderSuccess(arg0);
							} else {
								listener.OrderError(resultBase.getErrorText());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			};
		}.start();
	}

	
	@Override
	public void completedLoading(final int startNum, final int findNum, final String operateCode,
			final String requestTime, final OrderListener listener) {
		new Thread() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Order_Url);
				try {
					OrderRequestBean orderRequestBean = new OrderRequestBean();
					orderRequestBean.Page_StartNum = startNum;
					orderRequestBean.Page_EndNum = findNum;
					orderRequestBean.Operate_Code = operateCode;
					orderRequestBean.FirstRequestTime = requestTime;

					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderRequestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
					x.http().post(params, new CommonCallback<String>() {
						@Override
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean arg1) {
							listener.OrderError(
									TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
						}

						@Override
						public void onFinished() {
						}

						@Override
						public void onSuccess(String str) {
							try {
								DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
								if (resultBase.isSuccess()) {
									listener.OrderSuccess(str);
								} else {
									listener.OrderError(resultBase.getErrorText());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	public void deletedOrder(final String orderKey, final OrderListener listener) {
		// new Thread() {
		// public void run() {
		// RequestParams params = new RequestParams(Constants.DeleteOrder_Url);
		// try {
		//
		// DataRequestData requestData = new DataRequestData();
		// requestData.setToken(MyApplication.getInstatnce().getToken());
		// requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
		// requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
		//
		// requestData.setDataValue(orderKey);
		// params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// x.http().post(params, new CommonCallback<String>() {
		//
		// @Override
		// public void onCancelled(CancelledException arg0) {
		// }
		//
		// @Override
		// public void onError(Throwable arg0, boolean arg1) {
		// listener.OrderError("请求失败");
		// }
		//
		// @Override
		// public void onFinished() {
		// }
		//
		// @Override
		// public void onSuccess(String arg0) {
		// Log.e("请求成功------", "数据为====>>>>>" + arg0);
		// try {
		// DataResultBase resultBase = JsonUtils.parseDataResultBase(arg0);
		// if (resultBase.isSuccess()) {
		// listener.OrderSuccess("");
		// } else {
		// listener.OrderError(resultBase.getErrorText());
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// });
		// };
		// }.start();
	}

	@Override
	public void completedOrder(final String orderKey, final OrderListener listener) {
//		new Thread() {
//			public void run() {
//				RequestParams params = new RequestParams(Constants.ConfirmCompleteOrderByDriver_Url);
//				try {
//					DataRequestData requestData = new DataRequestData();
//					requestData.setToken(MyApplication.getInstatnce().getToken());
//					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
//					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
//					requestData.setDataValue(orderKey);
//					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
//
//					params.addBodyParameter(null, JsonUtils.toJsonData(requestData).toString());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				x.http().post(params, new CommonCallback<String>() {
//					@Override
//					public void onCancelled(CancelledException arg0) {
//					}
//
//					@Override
//					public void onError(Throwable ex, boolean arg1) {
//						listener.OrderError(
//								TextUtils.isEmpty(ex.getMessage()) == true ? "请求服务超时！请联系客服！" : ex.getMessage());
//					}
//
//					@Override
//					public void onFinished() {
//					}
//
//					@Override
//					public void onSuccess(String str) {
//						try {
//							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
//							if (resultBase.isSuccess()) {
//								listener.OrderSuccess("true");
//							} else {
//								listener.OrderError(resultBase.getErrorText());
//							}
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				});
//			};
//		}.start();
	}
}
