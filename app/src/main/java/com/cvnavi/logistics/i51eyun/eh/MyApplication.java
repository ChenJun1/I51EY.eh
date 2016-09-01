package com.cvnavi.logistics.i51eyun.eh;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.login.LoginData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Consignor;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Driver;
import com.cvnavi.logistics.i51eyun.eh.cache.BasicDataBuffer;
import com.cvnavi.logistics.i51eyun.eh.cache.OrderBaseBuffer;
import com.cvnavi.logistics.i51eyun.eh.manager.BasicDataManager;
import com.cvnavi.logistics.i51eyun.eh.manager.RegionManager;
import com.cvnavi.logistics.i51eyun.eh.service.LocationService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:30:24
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class MyApplication extends Application {

	private static MyApplication ms_MyApplication;
	private static DbManager.DaoConfig config;
	
	private LocationService locationService;
	public LocationService getLocationService() {
		return locationService;
	}

	private String UUID;
	private String userKey;
	private String token;
	private String userTypeOid;
	private String userTel;
	private String Company_Oid;
	
	@JsonProperty("AndroidVersion")
	private String AndroidVersion; // 安卓版本

	@JsonProperty("IOSVersion")
	private String IOSVersion; // IOS版本
	
	public String getAndroidVersion() {
		return AndroidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		AndroidVersion = androidVersion;
	}

	public String getIOSVersion() {
		return IOSVersion;
	}

	public void setIOSVersion(String iOSVersion) {
		IOSVersion = iOSVersion;
	}

	public String getCompany_Oid() {
		return Company_Oid;
	}

	public void setCompany_Oid(String company_Oid) {
		Company_Oid = company_Oid;
	}

	private AppRegData_Driver driverData;
	private AppRegData_Consignor cargoData;
	private LoginData loginData;
	
	private BasicDataBuffer basicDataBuffer;
	private OrderBaseBuffer orderBaseBuffer;

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserTypeOid() {
		return userTypeOid;
	}

	public void setUserTypeOid(String userTypeOid) {
		this.userTypeOid = userTypeOid;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public AppRegData_Driver getDriverData() {
		return driverData;
	}

	public void setDriverData(AppRegData_Driver driverData) {
		this.driverData = driverData;
	}

	public AppRegData_Consignor getCargoData() {
		return cargoData;
	}

	public void setCargoData(AppRegData_Consignor cargoData) {
		this.cargoData = cargoData;
	}

	public LoginData getLoginData() {
		return loginData;
	}

	public void setLoginData(LoginData loginData) {
		this.loginData = loginData;
	}

	public BasicDataBuffer getBasicDataBuffer() {
		return basicDataBuffer;
	}

	public OrderBaseBuffer getOrderBaseBuffer() {
		return orderBaseBuffer;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		ms_MyApplication = this;
		basicDataBuffer = new BasicDataBuffer();
		orderBaseBuffer = new OrderBaseBuffer();

		try {
			x.Ext.init(this);
			x.Ext.setDebug(BuildConfig.DEBUG);
		} catch (Exception e) {
			LogUtil.e("Init xutils fail,error:" + e.getMessage());
		}
		
		BasicDataManager.getInstance().init(getApplicationContext());
		RegionManager.getInstance().init(getApplicationContext());

		// 初始化地图
		locationService = new LocationService(getApplicationContext());
		locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		SDKInitializer.initialize(getApplicationContext());
		JPushInterface.setDebugMode(true);
		JPushInterface.init(getApplicationContext());
		config = new DbManager.DaoConfig().setDbName("db_i51eh").setDbVersion(1)
				.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager dbManager, int i, int i1) {
						// 数据库更新操作
					}
				});
		
		initImageLoader(getApplicationContext());
	}

	public static synchronized MyApplication getInstatnce() {
		return ms_MyApplication;
	}

	public static DbManager.DaoConfig getConfig() {
		return config;
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
	
	
}
