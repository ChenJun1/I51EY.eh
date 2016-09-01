/**
 * Administrator2016-5-19
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOvelray;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.followlocation.CarLocationBean;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 下午5:10:16
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@SuppressLint("ResourceAsColor")
@ContentView(R.layout.c_car_location)
public class C_FollowLocationCarActivity extends BaseActivity implements OnMarkerClickListener {

	@ViewInject(R.id.Consignee_Address_f)
	private TextView Consignee_Address;

	@ViewInject(R.id.Car_Address_f)
	private TextView Car_Address;

	@ViewInject(R.id.Shipping_Address_f)
	private TextView Shipping_Address;
	
	@ViewInject(R.id.Shipping_Address_ll)
	private LinearLayout Shipping_Address_ll;
	
	@ViewInject(R.id.Consignee_Address_ll)
	private LinearLayout Consignee_Address_ll;
	
	@ViewInject(R.id.Car_Address_ll)
	private LinearLayout Car_Address_ll;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.c_time)
	private TextView Date;

	private MapView c_mapView;

	private LocationClient mLocClient;

	private BaiduMap baiduMap;

	private MyLocationListenner myListener = new MyLocationListenner();

	private boolean isFirstLoc = true;

	private double latitude = 0.0;

	private double longitude =0.0;

	private CarLocationBean carLocationBean = null;

	
	private LinearLayout popll;

	private CommonWaitDialog myDialog;
	
	InfoWindow mInfoWindow=null;

	private String orderKey;

	private BitmapDescriptor mBitmapDescriptor = BitmapDescriptorFactory
			.fromResource(R.drawable.marker_inside_pink);

	private BitmapDescriptor orgBitmapDescriptor = BitmapDescriptorFactory
			.fromResource(R.drawable.marker_inside_azure);

	private LatLng Shippin;
	private LatLng Consignee;

	// 路线规划对象
	private RoutePlanSearch mSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		ininMap();

		if (getIntent().getStringExtra(Constants.OrderKey) != null) {
			orderKey = getIntent().getStringExtra(Constants.OrderKey);
		}
	//	loadCarLocation(Constants.CarPosition_URL);

		requestLoadData(Constants.CarTrack_Detailed_URL);
	}

	/**
	 * 
	 */
	private void loadCarLocation(final String Url) {
		myDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url+"/"+MyApplication.getInstatnce()
						.getCompany_Oid()+"/"+orderKey);
				try {
//					DataRequestData requestData = new DataRequestData();
//					requestData.setCompany_Oid(MyApplication.getInstatnce()
//							.getCompany_Oid());
//					requestData.setDataValue(orderKey);
//
//					String jsonStr = JsonUtils.toJsonData(requestData);
	//				params.addBodyParameter(null, jsonStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");

					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = Constants.errorMsg;
						myHandler.sendMessage(msg);

					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								CarLocationBean dataBean = JsonUtils.parseData(
										str, CarLocationBean.class);
								if (dataBean != null) {
									msg.what = Constants.Request_Success;
									msg.obj = dataBean;
									myHandler.sendMessage(msg);
								} else {
									msg.what = Constants.Request_Fail;
									msg.obj = resultBase.getErrorText();
									myHandler.sendMessage(msg);
								}
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	/**
	 * 发起路线规划
	 */
	public void routePlan(LatLng start, LatLng end) {
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(listener);
		// 起点与终点
		PlanNode stNode = PlanNode.withLocation(start);
		PlanNode enNode = PlanNode.withLocation(end);

		// 驾车路线规划
		mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(
				enNode));
	}

	/**
	 * 路线规划结果监听
	 */
	OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
		/**
		 * 步行
		 */
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
		}

		public void onGetTransitRouteResult(TransitRouteResult result) {
			// 获取公交换乘路径规划结果
		}

		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// 获取驾车线路规划结果
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(C_FollowLocationCarActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				
				DrivingRouteOvelray overlay = new MyDrivingRouteOverlay(
						baiduMap);
				baiduMap.setOnMarkerClickListener(overlay);
				DrivingRouteLine routeLine = result.getRouteLines().get(0);
				overlay.setData(routeLine);
				overlay.addToMap();
				overlay.zoomToSpan();
			}
		}
	};

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOvelray {
		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory
					.fromResource(R.drawable.marker_inside_azure);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory
					.fromResource(R.drawable.marker_inside_pink);
		}

		@Override
		public boolean onRouteNodeClick(int arg0) {
			super.onRouteNodeClick(arg0);
			return false;
		}
	}

	/**
	 * 
	 */
	private void requestLoadData(final String Url) {
		myDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce()
							.getCompany_Oid());
					requestData.setDataValue(orderKey);

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
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = Constants.errorMsg;
						myHandler.sendMessage(msg);

					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								CarLocationBean dataBean = JsonUtils.parseData(
										str, CarLocationBean.class);
								if (dataBean != null) {
									msg.what = Constants.Request_Success;
									msg.obj = dataBean;
									myHandler.sendMessage(msg);
								} else {
									msg.what = Constants.Request_Fail;
									msg.obj = resultBase.getErrorText();
									myHandler.sendMessage(msg);
								}
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	/**
	 * 
	 */
	private void ininMap() {
		// 开启定位图层

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
	}

	// 初始化视图
	private void initView() {
		myDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		c_mapView = (MapView) findViewById(R.id.c_bmapView);
		baiduMap = c_mapView.getMap();
		baiduMap.setMyLocationEnabled(true);
		baiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(8f).build()));
		View child = c_mapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			myDialog.dismiss();
			switch (msg.what) {
			case Constants.Request_Success:
				carLocationBean = (CarLocationBean) msg.obj;
				setData();
				break;
			case Constants.Request_NoData:
				Toast.makeText(C_FollowLocationCarActivity.this, "没有更多数据！",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_FollowLocationCarActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new CommonWaitDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				alertDialog.show();
				break;
			default:
				break;
			}
		}

	};


	private void setData() {
		titlt_textView.setText("车牌号：" + carLocationBean.CarCode);
		if (carLocationBean != null) {
			Shippin = new LatLng(Double.valueOf(carLocationBean.SBLat),
					Double.valueOf(carLocationBean.SBLng));
			Consignee = new LatLng(Double.valueOf(carLocationBean.CBLat),
					Double.valueOf(carLocationBean.CBLng));
			latitude = Double.valueOf(carLocationBean.SBLat);
			longitude = Double.valueOf(carLocationBean.SBLng);
			Consignee_Address.setText(ContextUtil.getDetailAddress(
					carLocationBean.Consignee_Provice,
					carLocationBean.Consignee_City, "",
					carLocationBean.Consignee_Address));
			Shipping_Address.setText(ContextUtil.getDetailAddress(
					carLocationBean.Shipping_Provice,
					carLocationBean.Shipping_City, "",
					carLocationBean.Shipping_Address));
			Date.setText(carLocationBean.Date);
//			if (mLocClient != null && mLocClient.isStarted()) {
//				mLocClient.requestLocation();
//
//			} else if (mLocClient != null) {
//				mLocClient.start();
//			}
			routePlan(Shippin, Consignee);
//			showOverlay();
			showPop3();

		}
	}

	@Event(value = { R.id.back_linearLayout, R.id.Shipping_Address_ll,
			R.id.Consignee_Address_ll,R.id.Car_Address_ll }, type = View.OnClickListener.class)
	private void OnClick(View view) {
		LatLng poLatLng = null;
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.Shipping_Address_ll:
			poLatLng = new LatLng(Double.valueOf(carLocationBean.SBLng),
					Double.valueOf(carLocationBean.SBLat));
			latitude = Double.valueOf(carLocationBean.SBLat);
			longitude = Double.valueOf(carLocationBean.SBLng);
			if (mLocClient != null && mLocClient.isStarted()) {
				isFirstLoc=true;
				mLocClient.requestLocation();
			} else if (mLocClient != null) {
				mLocClient.start();
			}
			showPop2();
			break;
		case R.id.Consignee_Address_ll:
			poLatLng = new LatLng(Double.valueOf(carLocationBean.CBLat),
					Double.valueOf(carLocationBean.CBLat));
			latitude = Double.valueOf(carLocationBean.CBLat);
			longitude = Double.valueOf(carLocationBean.CBLng);
			if (mLocClient != null && mLocClient.isStarted()) {
				isFirstLoc=true;
				mLocClient.requestLocation();
			} else if (mLocClient != null) {
				mLocClient.start();
			}
			showPop3();
			break;
		case R.id.Car_Address_ll:
			poLatLng = new LatLng(Double.valueOf(carLocationBean.CBLat),
					Double.valueOf(carLocationBean.CBLat));
			latitude = Double.valueOf(carLocationBean.CBLat);
			longitude = Double.valueOf(carLocationBean.CBLng);
			mLocClient.requestLocation();
			showPop(poLatLng, "收货地址");
			break;

		default:
			break;
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || c_mapView == null) {
				return;
			}
			try {

				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(latitude).longitude(longitude)
						.build();
				
				baiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(latitude, longitude);
					MapStatus.Builder builder = new MapStatus.Builder();
					builder.target(ll).zoom(8.f);
					baiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder.build()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 停车地点点击覆盖物，弹出导航选择列表
	 */
	@SuppressLint("ResourceAsColor")
	private void showPop(LatLng position, String makTitle) {
		// 创建InfoWindow展示的view
		if(TextUtils.isEmpty(makTitle)){
			return;
		}
		View popup = View.inflate(this, R.layout.activity_fragment_2_showpop,
				null);
		popll= ViewHolder.get(popup, R.id.popll);
		if (makTitle.equals("发货地址")) {
			popll.setBackgroundResource(R.drawable.shape_geer);
		}else{
			popll.setBackgroundResource(R.drawable.shape_red);
		}
		OnInfoWindowClickListener listener1 = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				// 隐藏mInfoWindow
				baiduMap.hideInfoWindow();
			}
		};
		InfoWindow mInfoWindow = new InfoWindow(popup, position, listener1);
		baiduMap.showInfoWindow(mInfoWindow);
			
		// 创建InfoWindow
		// 显示InfoWindow
		
	}
	private void showPop2() {
		// 创建InfoWindow展示的view
		View popup = View.inflate(this, R.layout.activity_fragment_2_showpop,
				null);
		popll= ViewHolder.get(popup, R.id.popll);
		popll.setBackgroundResource(R.drawable.shape_geer);
		OnInfoWindowClickListener listener1 = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				// 隐藏mInfoWindow
				baiduMap.hideInfoWindow();
			}
		};
			
			InfoWindow mInfoWindow1 = new InfoWindow(popup, Shippin, listener1);
			baiduMap.showInfoWindow(mInfoWindow1);
		// 创建InfoWindow
		// 显示InfoWindow
		
	}
	
	private void showPop3() {
		// 创建InfoWindow展示的view
		View popup = View.inflate(this, R.layout.activity_fragment_2_showpop,
				null);
		popll= ViewHolder.get(popup, R.id.popll);
		popll.setBackgroundResource(R.drawable.shape_red);
		OnInfoWindowClickListener listener1 = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				// 隐藏mInfoWindow
				baiduMap.hideInfoWindow();
			}
		};
		
		InfoWindow mInfoWindow2 = new InfoWindow(popup, Consignee, listener1);
		baiduMap.showInfoWindow(mInfoWindow2);
		// 创建InfoWindow
		// 显示InfoWindow
		
	}


	//
	private void showOverlay() {
		MarkerOptions moo = new MarkerOptions().position(Shippin)
				.icon(mBitmapDescriptor).zIndex(9);
		moo.title("发货地址");
		MarkerOptions moo1 = new MarkerOptions().position(Consignee)
				.icon(orgBitmapDescriptor).zIndex(9);
		moo1.title("收货地址");

		Overlay overlay = baiduMap.addOverlay(moo);

		Overlay overlay2 = baiduMap.addOverlay(moo1);
		
		baiduMap.setOnMarkerClickListener(MerkerClickLs);
	}
	
	OnMarkerClickListener MerkerClickLs=new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			showPop(marker.getPosition(), marker.getTitle());
			return false;
		}
	};
	
	
	@Override
	protected void onDestroy() {
		orgBitmapDescriptor.recycle();
		mBitmapDescriptor.recycle();
		c_mapView.onDestroy();
		if (mLocClient != null) {
			mLocClient.stop();
		}
		if (mSearch != null) {
			mSearch.destroy();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		c_mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		c_mapView.onPause();

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {

		return false;
	}
}
