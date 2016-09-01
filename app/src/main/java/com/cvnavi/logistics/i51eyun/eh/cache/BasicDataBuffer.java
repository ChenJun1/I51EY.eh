package com.cvnavi.logistics.i51eyun.eh.cache;

import android.text.TextUtils;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.AdvertPicBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarSize_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_ConsignorType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsName_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_PackageType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_TransportArea_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_UserGrade_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.DynamicBasicDataBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 基础数据缓存类。
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class BasicDataBuffer {

	// 基础数据查询参数列表
	private List<String> basicDataRequestList;

	public void setBasicDataRequestList(List<String> basicDataRequestList) {
		if (basicDataRequestList == null) {
			basicDataRequestList = new ArrayList<String>();
		}
		this.basicDataRequestList = basicDataRequestList;
	}

	// 基础数据查询参数
	public static String CarTypeDic_Extra = "CarTypeDic"; // 车辆类型
	public static String CarSizeDic_Extra = "CarSizeDic"; // 车辆尺寸
	public static String CompulsoryInsurance_Extra = "CompulsoryInsurance";// 强保费
	public static String SquareWeightRatio_Extra = "SquareWeightRatio"; // 方重比
	public static String ISEvaluate_Extra = "ISEvaluate"; // 是否开启评价
	public static String CompanyDic_Extra = "CompanyDic"; // 园区字典

	// static {
	// m_basicDataRequestList.add(CarTypeDic_Extra);
	// m_basicDataRequestList.add(CarSizeDic_Extra);
	// m_basicDataRequestList.add(CompulsoryInsurance_Extra);
	// m_basicDataRequestList.add(SquareWeightRatio_Extra);
	// m_basicDataRequestList.add(ISEvaluate_Extra);
	// m_basicDataRequestList.add(CompanyDic_Extra);
	// }

	// 动态加载。
	private List<Dict_CarType_Sheet> carTypeList; // 车辆类型列表。
	private List<Dict_CarSize_Sheet> carSizeList; // 车辆尺寸列表。
	private List<MDict_Company_Sheet> companyList; // 园区列表。

	private String CompulsoryInsurance;
	private String SquareWeightRatio;
	private String ISEvaluate;

	// 本地基础数据。
	private List<Dict_ConsignorType_Sheet> consignorTypeList; // 货主类型列表。
	private List<Dict_GoodsShape_Sheet> goodsShapeList; // 货物形状列表。
	private List<Dict_GoodsType_Sheet> goodsTypeList; // 货物类型列表。
	private List<Dict_TransportArea_Sheet> transportAreaList; // 运输区域列表。
	private List<Dict_PackageType_Sheet> packageTypeList; // 包装类型列表。
	private List<Dict_UserGrade_Sheet> userGradeList; // 用户等级列表。
	private List<Dict_GoodsName_Sheet> goodsNameList;//物品名称
//	private List<AdvertPicBean> advertPicList;//轮播广告
	
	
	


	// 静态数据。
	private List<String> drivingLicenseTypeList; // 驾照类型列表。

	private LoadBasicDataStatusCallBack loadBasicDataStatusCallBack;

	public void setLoadBasicDataStatusCallBack(LoadBasicDataStatusCallBack loadBasicDataStatusCallBack) {
		this.loadBasicDataStatusCallBack = loadBasicDataStatusCallBack;
	}

	public BasicDataBuffer() {
		drivingLicenseTypeList = new ArrayList<String>();
		drivingLicenseTypeList.add("A1");
		drivingLicenseTypeList.add("A2");
		drivingLicenseTypeList.add("B1");
		drivingLicenseTypeList.add("B2");
		drivingLicenseTypeList.add("C1");
		drivingLicenseTypeList.add("C2");

		basicDataRequestList = new ArrayList<String>();
		basicDataRequestList.add(CarTypeDic_Extra);
		basicDataRequestList.add(CarSizeDic_Extra);
		basicDataRequestList.add(CompulsoryInsurance_Extra);
		basicDataRequestList.add(SquareWeightRatio_Extra);
		basicDataRequestList.add(ISEvaluate_Extra);
		basicDataRequestList.add(CompanyDic_Extra);
	}

	public void init() {
		loadBasicData();
		//loadAdvertPic();
		// loadCartTypeData();
		// loadCarSizeData();
		// loadCompanyData();

		// loadConsignorTypeData();
		// loadGoodsShapeData();
		// loadGoodsTypeData();
		// loadTransportAreaData();
	}
	

	public void loadBasicData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.Get_DicAndSetData_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getLoginData().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(basicDataRequestList));

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
						if (loadBasicDataStatusCallBack != null) {
							loadBasicDataStatusCallBack.onLoadBasicDataFail(ex);
						}
						LogUtil.d("-->>请求错误:" + ex.getMessage());
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DynamicBasicDataBean dynamicBasicDataBean = JsonUtils.parseData(str,
									DynamicBasicDataBean.class);
							if (dynamicBasicDataBean != null) {
								if (dynamicBasicDataBean.CarTypeDic != null
										&& dynamicBasicDataBean.CarTypeDic.size() > 0) {
									setCarTypeList(dynamicBasicDataBean.CarTypeDic);
								}

								if (dynamicBasicDataBean.CarSizeDic != null
										&& dynamicBasicDataBean.CarSizeDic.size() > 0) {
									setCarSizeList(dynamicBasicDataBean.CarSizeDic);
								}

								if (dynamicBasicDataBean.CompanyDic != null
										&& dynamicBasicDataBean.CompanyDic.size() > 0) {
									setCompanyList(dynamicBasicDataBean.CompanyDic);
								}

								if (TextUtils.isEmpty(dynamicBasicDataBean.CompulsoryInsurance) == false) {
									setCompulsoryInsurance(dynamicBasicDataBean.CompulsoryInsurance);
								}

								if (TextUtils.isEmpty(dynamicBasicDataBean.SquareWeightRatio) == false) {
									setSquareWeightRatio(dynamicBasicDataBean.SquareWeightRatio);
								}

								if (TextUtils.isEmpty(dynamicBasicDataBean.ISEvaluate) == false) {
									setISEvaluate(dynamicBasicDataBean.ISEvaluate);
								}

								// 基础书查询成功后清除基础数据查询参数列表，以方便后续查询。
								basicDataRequestList.clear();
							}

							if (loadBasicDataStatusCallBack != null) {
								loadBasicDataStatusCallBack.onLoadBasicDataSuccess();
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}
	public void loadAdvertPic() {
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
					}
					
					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}
					
					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase daResultBase = JsonUtils.parseDataResultBase(str);
							if(daResultBase.isSuccess()){
							List<AdvertPicBean> advertPicList = JsonUtils.parseadvertPicList(daResultBase.getDataValue().toString());
							if(advertPicList != null&&advertPicList.size()>0){
								for(int i=0;i<advertPicList.size();i++){
									Constants.mImageUrl.add(advertPicList.get(i).AdvertPic);
								}
							}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
			}
		}).start();
	}

	private void loadCartTypeData() {
		if (carTypeList == null || carTypeList.size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					requestData.setDataValue("GetCarTypeList");

					RequestParams params = new RequestParams(Constants.Get_CarTypeList_Url);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<Dict_CarType_Sheet> dataList = JsonUtils.parseCarTypeList(str);
								if (dataList != null) {
									carTypeList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	private void loadConsignorTypeData() {
		if (consignorTypeList == null || consignorTypeList.size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					requestData.setDataValue("GetConsignorTypeList");

					RequestParams params = new RequestParams(Constants.Get_ConsignorTypeList_Url);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<Dict_ConsignorType_Sheet> dataList = JsonUtils.parseConsigneeTypeList(str);
								if (dataList != null) {
									consignorTypeList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	private void loadGoodsShapeData() {
		if (goodsShapeList == null || goodsShapeList.size() == 0) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					requestData.setDataValue("GetGoodsShapeList");

					RequestParams params = new RequestParams(Constants.Get_GoodsShapeList_Url);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<Dict_GoodsShape_Sheet> dataList = JsonUtils.parseGoodsShapeList(str);
								if (dataList != null) {
									goodsShapeList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	private void loadGoodsTypeData() {
		if (goodsTypeList == null || goodsTypeList.size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					requestData.setDataValue("GetGoodsTypeList");

					RequestParams params = new RequestParams(Constants.Get_GoodsTypeList_Url);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<Dict_GoodsType_Sheet> dataList = JsonUtils.parseGoodsTypeList(str);
								if (dataList != null) {
									goodsTypeList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	private void loadTransportAreaData() {
		if (transportAreaList == null || transportAreaList.size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					requestData.setDataValue("GetTransportAreaList");

					RequestParams params = new RequestParams(Constants.Get_TransportAreaList_Url);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<Dict_TransportArea_Sheet> dataList = JsonUtils.parseTransportAreaList(str);
								if (dataList != null) {
									transportAreaList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	private void loadCarSizeData() {
		if (carSizeList == null || carSizeList.size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					requestData.setDataValue("GetCarSizeList");

					RequestParams params = new RequestParams(Constants.Get_CarSizeList_Url);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<Dict_CarSize_Sheet> dataList = JsonUtils.parseCarSizeList(str);
								if (dataList != null) {
									carSizeList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	private void loadCompanyData() {
		if (companyList == null || companyList.size() == 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());

					RequestParams params = new RequestParams(Constants.Get_ParkList_URL);
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
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>请求成功:" + str);
							try {
								List<MDict_Company_Sheet> dataList = JsonUtils.parseParkList(str);
								if (dataList != null) {
									companyList = dataList;
								}
							} catch (JsonParseException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}
	}

	public List<Dict_CarType_Sheet> getCarTypeList() {
		return carTypeList;
	}

	public Dict_CarType_Sheet getCarTypeByKey(String key) {
		if (carTypeList == null || carTypeList.size() == 0) {
			return null;
		}

		for (Dict_CarType_Sheet bean : carTypeList) {
			if (bean.CarType_Oid.equals(key)) {
				return bean;
			}
		}
		return null;
	}

	public void setCarTypeList(List<Dict_CarType_Sheet> carTypeList) {
		this.carTypeList = carTypeList;
	}

	public List<Dict_ConsignorType_Sheet> getConsignorTypeList() {
		return consignorTypeList;
	}

	public Dict_ConsignorType_Sheet getConsignorTypeByKey(String key) {
		if (consignorTypeList == null || consignorTypeList.size() == 0) {
			return null;
		}

		for (Dict_ConsignorType_Sheet bean : consignorTypeList) {
			if (bean.ConsignorType_Oid.equals(key)) {
				return bean;
			}
		}
		return null;
	}

	public void setConsignorTypeList(List<Dict_ConsignorType_Sheet> consignorTypeList) {
		this.consignorTypeList = consignorTypeList;
	}

	public List<Dict_GoodsShape_Sheet> getGoodsShapeList() {
		return goodsShapeList;
	}

	public Dict_GoodsShape_Sheet getGoodsShapeByKey(String key) {
		if (goodsShapeList == null || goodsShapeList.size() == 0) {
			return null;
		}
		for (Dict_GoodsShape_Sheet bean : goodsShapeList) {
			if (bean.GoodsShape_Oid.equals(key)) {
				return bean;
			}
		}

		return null;
	}

	public void setGoodsShapeList(List<Dict_GoodsShape_Sheet> goodsShapeList) {
		this.goodsShapeList = goodsShapeList;
	}

	public List<Dict_GoodsType_Sheet> getGoodTypeList() {
		return goodsTypeList;
	}

	public Dict_GoodsType_Sheet getGoodTypeByKey(String key) {
		if (goodsTypeList == null || goodsTypeList.size() == 0) {
			return null;
		}
		for (Dict_GoodsType_Sheet bean : goodsTypeList) {
			if (bean.GoodsType_Oid.equals(key)) {
				return bean;
			}
		}

		return null;
	}

	public void setGoodTypeList(List<Dict_GoodsType_Sheet> goodTypeList) {
		this.goodsTypeList = goodTypeList;
	}

	public List<Dict_TransportArea_Sheet> getTransportAreaList() {
		return transportAreaList;
	}

	public Dict_TransportArea_Sheet getTransportAreaByKey(String key) {
		if (transportAreaList == null || transportAreaList.size() == 0) {
			return null;
		}
		for (Dict_TransportArea_Sheet bean : transportAreaList) {
			if (bean.TransportArea_Oid.equals(key)) {
				return bean;
			}
		}

		return null;
	}

	public void setTransportAreaList(List<Dict_TransportArea_Sheet> transportAreaList) {
		this.transportAreaList = transportAreaList;
	}

	public List<Dict_CarSize_Sheet> getCarSizeList() {
		return carSizeList;
	}

	public Dict_CarSize_Sheet getCarSizeByKey(int key) {
		if (carSizeList == null || carSizeList.size() == 0) {
			return null;
		}
		for (Dict_CarSize_Sheet bean : carSizeList) {
			if (bean.CarSize_Oid == key) {
				return bean;
			}
		}

		return null;
	}

	public void setCarSizeList(List<Dict_CarSize_Sheet> carSizeList) {
		this.carSizeList = carSizeList;
	}

	public List<String> getDrivingLicenseTypeList() {
		return drivingLicenseTypeList;
	}

	// public void setDrivingLicenseTypeList(List<String>
	// drivingLicenseTypeList) {
	// this.drivingLicenseTypeList = drivingLicenseTypeList;
	// }

	public List<MDict_Company_Sheet> getCompanyList() {
		return companyList;
	}

	public MDict_Company_Sheet getCompanyByKey(String key) {
		if (companyList == null || companyList.size() == 0) {
			return null;
		}
		for (MDict_Company_Sheet bean : companyList) {
			if (bean.Company_Oid.equals(key)) {
				return bean;
			}
		}

		return null;
	}

	public void setCompanyList(List<MDict_Company_Sheet> companyList) {
		this.companyList = companyList;
	}

	public List<Dict_PackageType_Sheet> getPackageTypeList() {
		return packageTypeList;
	}

	public Dict_PackageType_Sheet getPackageTypeByKey(String key) {
		if (packageTypeList == null || packageTypeList.size() == 0) {
			return null;
		}
		for (Dict_PackageType_Sheet bean : packageTypeList) {
			if (bean.PackageType_Oid.equals(key)) {
				return bean;
			}
		}

		return null;
	}

	public void setPackageTypeList(List<Dict_PackageType_Sheet> packageTypeList) {
		this.packageTypeList = packageTypeList;
	}

	public List<Dict_UserGrade_Sheet> getUserGradeList() {
		return userGradeList;
	}

	public Dict_UserGrade_Sheet getUserGradeByKey(String key) {
		if (userGradeList == null || userGradeList.size() == 0) {
			return null;
		}
		for (Dict_UserGrade_Sheet bean : userGradeList) {
			if (bean.Score.equals(key)) {
				return bean;
			}
		}

		return null;
	}
	public List<Dict_GoodsName_Sheet> getGoodsNameList() {
		return goodsNameList;
	}

	public void setGoodsNameList(List<Dict_GoodsName_Sheet> goodsNameList) {
		this.goodsNameList = goodsNameList;
	}
	public Dict_GoodsName_Sheet getGoodsNameKey(String key) {
		if (goodsNameList == null || goodsNameList.size() == 0) {
			return null;
		}
		for (Dict_GoodsName_Sheet bean : goodsNameList) {
			if (bean.GoodsName_Oid.equals(key)) {
				return bean;
			}
		}

		return null;
	}
	

	public void setUserGradeList(List<Dict_UserGrade_Sheet> userGradeList) {
		this.userGradeList = userGradeList;
	}

	public String getCompulsoryInsurance() {
		return CompulsoryInsurance;
	}

	public void setCompulsoryInsurance(String compulsoryInsurance) {
		CompulsoryInsurance = compulsoryInsurance;
	}

	public String getSquareWeightRatio() {
		return SquareWeightRatio;
	}

	public void setSquareWeightRatio(String squareWeightRatio) {
		SquareWeightRatio = squareWeightRatio;
	}

	public String getISEvaluate() {
		return ISEvaluate;
	}

	public void setISEvaluate(String iSEvaluate) {
		ISEvaluate = iSEvaluate;
	}

	public String getGradeName(String score) {
		if (TextUtils.isEmpty(score)) {
			return "1级";
		}

		for (int i = userGradeList.size() - 1; i > 0; i--) {
			if (Integer.valueOf(score) >= Integer.valueOf(userGradeList.get(i).Score)) {
				return userGradeList.get(i).GradeName;
			}
		}
		return "1级";
	}

	public interface LoadBasicDataStatusCallBack {
		public void onLoadBasicDataSuccess();

		public void onLoadBasicDataFail(Throwable ex);
	}

}
