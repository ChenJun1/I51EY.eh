package com.cvnavi.logistics.i51eyun.eh.manager;

import android.content.Context;
import android.text.TextUtils;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.AreaBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.CityBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.ProvinceBean;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:30:30
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class RegionManager {

	private static RegionManager ms_RegionManager = new RegionManager();

	/**
	 * 省份列表。
	 */
	private List<ProvinceBean> mProvinceBeanList;
	/**
	 * 市列表
	 */
	private Map<String, List<CityBean>> mCityBeanMap = null; // Map<Serial_Oid,List<CityBean>>
	/**
	 * 区列表
	 */
	private Map<String, List<AreaBean>> mAreaBeanMap = null; // Map<CitySerial_Oid,List<AreaBean>>

	public List<ProvinceBean> getmProvinceBeanList() {
		return mProvinceBeanList;
	}

	public Map<String, List<CityBean>> getmCityBeanMap() {
		return mCityBeanMap;
	}

	public Map<String, List<AreaBean>> getmAreaBeanMap() {
		return mAreaBeanMap;
	}

	private RegionManager() {
		mProvinceBeanList = new ArrayList<ProvinceBean>();
		mCityBeanMap = new HashMap<String, List<CityBean>>();
		mAreaBeanMap = new HashMap<String, List<AreaBean>>();
	}

	public static synchronized RegionManager getInstance() {
		return ms_RegionManager;
	}

	public void init(final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LogUtil.d("-->>region begin");

					InputStream inStream = context.getAssets().open(
							"region.txt");
					int size = inStream.available();
					byte[] buffer = new byte[size];
					inStream.read(buffer);
					inStream.close();

					String jsonStr = new String(buffer);
					if (TextUtils.isEmpty(jsonStr)) {
						return;
					}

					mProvinceBeanList = JsonUtils.parseProvinceList(jsonStr);
					for (int i = 0; i < mProvinceBeanList.size(); i++) {
						ProvinceBean provinceBean = mProvinceBeanList.get(i);
						if (provinceBean.ListCity == null || provinceBean.ListCity.size() ==0) {
							continue;
						}

						List<CityBean> cityBeanList = provinceBean.ListCity;
						if (cityBeanList == null || cityBeanList.size() == 0) {
							continue;
						}
						mCityBeanMap.put(provinceBean.PName.trim(),
								cityBeanList);

						for (int j = 0; j < cityBeanList.size(); j++) {
							CityBean cityBean = cityBeanList.get(j);
							if (cityBean.List_Area == null || cityBean.List_Area.size() == 0) {
								continue;
							}

							List<AreaBean> areaList = cityBean.List_Area;
							mAreaBeanMap.put(cityBean.CName.trim(), areaList);
						}
					}

					LogUtil.d("-->>region end");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
