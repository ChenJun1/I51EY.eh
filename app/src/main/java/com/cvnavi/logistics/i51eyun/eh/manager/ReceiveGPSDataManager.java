package com.cvnavi.logistics.i51eyun.eh.manager;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.gps.APIGPSRequestBean;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReceiveGPSDataManager {

	private static ReceiveGPSDataManager ms_ReceiveGPSDataManager = new ReceiveGPSDataManager();

	private ReceiveGPSDataManager() {
	}

	public synchronized static ReceiveGPSDataManager getInstance() {
		return ms_ReceiveGPSDataManager;
	}

	private String longitude;
	private String latitude;

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void sendGPSData() {
		try {
			Runnable runnable = new Runnable() {
				public void run() {
					if (verifyData() == false) {
						return;
					}

					// 如果经纬度都为空，则不传输数据给服务端。
					if (TextUtils.isEmpty(longitude) && TextUtils.isEmpty(latitude)) {
						return;
					}

					RequestParams params = new RequestParams(Constants.ReceiveGPSData_URL);
					try {
						APIGPSRequestBean apigpsRequestBean = new APIGPSRequestBean();
						apigpsRequestBean.CarCode_Key = MyApplication.getInstatnce().getLoginData().getCarCode_Key();
						apigpsRequestBean.CarCode = MyApplication.getInstatnce().getLoginData().getCarCode();
						apigpsRequestBean.SIM = MyApplication.getInstatnce().getUserTel();
						apigpsRequestBean.Longitude = longitude; // 经度
						apigpsRequestBean.Latitude = latitude; // 纬度
						apigpsRequestBean.Company_Oid = MyApplication.getInstatnce().getLoginData().getCompany_Oid();
						apigpsRequestBean.FCompany_Oid = MyApplication.getInstatnce().getLoginData().getFCompany_Oid();

						DataRequestData requestData = new DataRequestData();
						requestData.setToken(MyApplication.getInstatnce().getToken());
						requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
						requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
						requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
						requestData.setDataValue(JsonUtils.toJsonData(apigpsRequestBean));

						String jsonStr = JsonUtils.toJsonData(requestData);
						params.addBodyParameter(null, jsonStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					x.http().post(params, new Callback.CommonCallback<String>() {
						public void onCancelled(CancelledException arg0) {
							LogUtil.d("-->>请求取消");
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							LogUtil.d("-->>GPS请求错误:" + ex.getMessage());
						}

						@Override
						public void onFinished() {
							LogUtil.d("-->>请求结束");
						}

						@Override
						public void onSuccess(String str) {
							LogUtil.d("-->>GPS请求成功:" + str);
						}
					});
				}
			};

			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
			service.scheduleAtFixedRate(runnable, 1000, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
		} catch (Exception ex) {
			LogUtil.e("-->>GPS数据传输错误：" + ex.getMessage());
		}
	}

	public boolean verifyData() {
		if (MyApplication.getInstatnce().getUserTypeOid().equals("A") == false) {
			return false;
		}

		if (MyApplication.getInstatnce().getLoginData() == null) {
			return false;
		}

		// 车牌号Key为空，不用传数据。
		if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getCarCode_Key())) {
			LogUtil.e("-->>GPS数据传输，车牌号Key为空");
			return false;
		}

		if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getIsPrimaryDriver())
				|| MyApplication.getInstatnce().getLoginData().getIsPrimaryDriver().equals("0")) {
			return false;
		}

		if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getFCompany_Oid())) {
			return false;
		}

		return true;
	}

	public void start() {
		MyApplication.getInstatnce().getLocationService().registerListener(mListener);
		MyApplication.getInstatnce().getLocationService().start();
	}

	public void stop() {
		MyApplication.getInstatnce().getLocationService().unregisterListener(mListener);
		MyApplication.getInstatnce().getLocationService().stop();
	}

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDLocationListener mListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				// StringBuffer sb = new StringBuffer(256);
				// sb.append("time : ");
				// /**
				// * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
				// * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
				// */
				// sb.append(location.getTime());
				// sb.append("\nerror code : ");
				// sb.append(location.getLocType());
				// sb.append("\nlatitude : ");
				// sb.append(location.getLatitude());
				// sb.append("\nlontitude : ");
				// sb.append(location.getLongitude());
				// sb.append("\nradius : ");
				// sb.append(location.getRadius());
				// sb.append("\ncitycode : ");
				// sb.append(location.getCityCode());
				// sb.append("\ncity : ");
				// sb.append(location.getCity());
				// sb.append("\nDistrict : ");
				// sb.append(location.getDistrict());
				// sb.append("\nStreet : ");
				// sb.append(location.getStreet());
				// sb.append("\naddr : ");
				// sb.append(location.getAddrStr());
				// sb.append("\nDescribe: ");
				// sb.append(location.getDirection());

				longitude = String.valueOf(location.getLongitude());
				latitude = String.valueOf(location.getLatitude());

				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					// sb.append("\nspeed : ");
					// sb.append(location.getSpeed());// 单位：km/h
					// sb.append("\nsatellite : ");
					// sb.append(location.getSatelliteNumber());
					// sb.append("\nheight : ");
					// sb.append(location.getAltitude());// 单位：米
					// sb.append("\ndescribe : ");
					// sb.append("gps定位成功");

					LogUtil.d("-->>gps定位成功");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					// // 运营商信息
					// sb.append("\noperationers : ");
					// sb.append(location.getOperators());
					// sb.append("\ndescribe : ");
					// sb.append("网络定位成功");

					LogUtil.d("-->>网络定位成功");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					// sb.append("\ndescribe : ");
					// sb.append("离线定位成功，离线定位结果也是有效的");

					LogUtil.d("-->>离线定位成功，离线定位结果也是有效的");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					// sb.append("\ndescribe : ");
					// sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

					LogUtil.d("-->>服务端网络定位失败");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					// sb.append("\ndescribe : ");
					// sb.append("网络不同导致定位失败，请检查网络是否通畅");

					LogUtil.d("-->>网络不同导致定位失败，请检查网络是否通畅");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					// sb.append("\ndescribe : ");
					// sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

					LogUtil.d("-->>无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}
			}
		}

	};
}
