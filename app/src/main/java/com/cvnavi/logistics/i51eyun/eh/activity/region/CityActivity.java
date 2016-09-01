/**
 * Administrator2016-4-29
 */
package com.cvnavi.logistics.i51eyun.eh.activity.region;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.CityBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_CityAdapter;
import com.cvnavi.logistics.i51eyun.eh.manager.RegionManager;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:09:17
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_show_mdict_list)
public class CityActivity extends BaseActivity {

	@ViewInject(R.id.operation_btn)
	private Button operation_btn;
	
	@ViewInject(R.id.c_show_mdict_listView)
	private ListView cityListView;
	
	private C_CityAdapter cityAdapter;
	private List<CityBean> cityList;

	private RegionBean regionBean;
	public static String province_city_key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.titlt_textView.setText("市");
		if(getIntent().getStringExtra(Constants.ISPCA) != null){
		}else{
			operation_btn.setText("完成");
			operation_btn.setVisibility(View.VISIBLE);
		}
		
		cityList = new ArrayList<CityBean>();
		province_city_key = getIntent().getStringExtra(Constants.Province_City_Key);

		regionBean = (RegionBean) getIntent().getSerializableExtra(Constants.RegionKey);
		if (regionBean.provinceBean == null || TextUtils.isEmpty(regionBean.provinceBean.PName)) {
			Toast.makeText(this, "错误的省份KEY!", Toast.LENGTH_LONG).show();
			return;
		}

		List<CityBean> dataList = RegionManager.getInstance().getmCityBeanMap().get(regionBean.provinceBean.PName.trim());
		if (dataList != null && dataList.size() > 0) {
			cityList.addAll(dataList);
		}
		cityAdapter = new C_CityAdapter(cityList, this);
		cityListView.setAdapter(cityAdapter);

		cityListView.setOnItemClickListener(myListener);
	}

	OnItemClickListener myListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CityBean bean = cityList.get(position);
			if (bean != null) {
				regionBean.cityBean = bean;
				
				if (TextUtils.isEmpty(province_city_key) == false) {
					CallBackManager.getInstance().getRegionCallBackManager2().fireCallBack(regionBean);
					finish();
					ActivityStackManager.getInstance().finishActivity(ProvinceActivity.class);
					return;
				}
				
				Intent intent = new Intent(CityActivity.this, AreaActivity.class);
				intent.putExtra(Constants.RegionKey, regionBean);
				if(CityActivity.this.getIntent().getStringExtra(Constants.ISPCA) != null){
					intent.putExtra(Constants.ISPCA, Constants.ISPCA);
				}
				startActivity(intent);
			}
		}
	};

	@Event(value = { R.id.back_linearLayout,R.id.operation_btn }, type = View.OnClickListener.class)
	private void MyOnCklic(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.operation_btn:
			CallBackManager.getInstance().getRegionCallBackManager2().fireCallBack(regionBean);
			finish();
			ActivityStackManager.getInstance().finishActivity(ProvinceActivity.class);
			break;
		default:
			break;
		}
	}
}
