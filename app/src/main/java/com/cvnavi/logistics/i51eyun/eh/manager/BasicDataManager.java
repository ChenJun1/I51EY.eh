package com.cvnavi.logistics.i51eyun.eh.manager;

import android.content.Context;
import android.text.TextUtils;

import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.BasicDataBean;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;

public class BasicDataManager {

	private static BasicDataManager ms_BasicDataManager = new BasicDataManager();

	private BasicDataBean basicDataBean;
	public BasicDataBean getBasicDataBean() {
		return basicDataBean;
	}

	private BasicDataManager() {
		basicDataBean = new BasicDataBean();
	}

	public static synchronized BasicDataManager getInstance() {
		return ms_BasicDataManager;
	}

	public void init(final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LogUtil.d("-->>basicdata begin");

					InputStream inStream = context.getAssets().open("basicdata.txt");
					int size = inStream.available();
					byte[] buffer = new byte[size];
					inStream.read(buffer);
					inStream.close();

					String jsonStr = new String(buffer);
					if (TextUtils.isEmpty(jsonStr)) {
						return;
					}
					
					basicDataBean = JsonUtils.parseData2(jsonStr, BasicDataBean.class);
					if (basicDataBean != null) {
						MyApplication.getInstatnce().getBasicDataBuffer().setGoodsShapeList(basicDataBean.GoodsShape);
						MyApplication.getInstatnce().getBasicDataBuffer().setGoodTypeList(basicDataBean.GoodsType);
						MyApplication.getInstatnce().getBasicDataBuffer().setPackageTypeList(basicDataBean.PackageType);
						MyApplication.getInstatnce().getBasicDataBuffer().setTransportAreaList(basicDataBean.TransportArea);
						MyApplication.getInstatnce().getBasicDataBuffer().setConsignorTypeList(basicDataBean.ConsignorType);
						MyApplication.getInstatnce().getBasicDataBuffer().setUserGradeList(basicDataBean.UserGrade);
						MyApplication.getInstatnce().getBasicDataBuffer().setGoodsNameList(basicDataBean.GoodsName);
					}

					LogUtil.d("-->>basicdata end");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
