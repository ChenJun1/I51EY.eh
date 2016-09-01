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

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.ProvinceBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_ProvinceAdapter;
import com.cvnavi.logistics.i51eyun.eh.manager.RegionManager;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;

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
 * @date 2016-5-17 下午1:09:33
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_show_mdict_list)
public class ProvinceActivity extends BaseActivity {
	
	@ViewInject(R.id.operation_btn)
	private Button operation_btn;
	
	@ViewInject(R.id.c_show_mdict_listView)
	private ListView provinceListView;

	private List<ProvinceBean> provinceList;
	private C_ProvinceAdapter provinceAdapter;
	
	private RegionBean regionBean;
	private String province_key;
	private String province_city_key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.titlt_textView.setText("省");
//		operation_btn.setText("完成");
//		operation_btn.setVisibility(View.VISIBLE);
		
		provinceList = new ArrayList<ProvinceBean>();
		regionBean = new RegionBean();
		province_key = getIntent().getStringExtra(Constants.Province_Key);
		province_city_key = getIntent().getStringExtra(Constants.Province_City_Key);
		
		List<ProvinceBean> provinceBeanList = RegionManager.getInstance().getmProvinceBeanList();
		if (provinceBeanList != null && provinceBeanList.size() > 0) {
			provinceList.addAll(provinceBeanList);
		}
		provinceAdapter = new C_ProvinceAdapter(provinceList, this);
		provinceListView.setAdapter(provinceAdapter);

		provinceListView.setOnItemClickListener(myListener);
	}

	OnItemClickListener myListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ProvinceBean bean = provinceList.get(position);
			if (bean != null) {
				regionBean.provinceBean = bean;
				
				if (TextUtils.isEmpty(province_key) == false) {
					CallBackManager.getInstance().getRegionCallBackManager2().fireCallBack(regionBean);
					finish();
					return;
				}
				
				Intent intent = new Intent(ProvinceActivity.this,CityActivity.class);
				intent.putExtra(Constants.RegionKey, regionBean);
				intent.putExtra(Constants.Province_City_Key, province_city_key);
				if(ProvinceActivity.this.getIntent().getStringExtra(Constants.ISPCA) != null){
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
			finish();
			break;
		default:
			break;
		}
	}
}
