/**
 * Administrator2016-4-29
 */
package com.cvnavi.logistics.i51eyun.eh.activity.region;

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
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.AreaBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_AreaAdapter;
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
 * @date 2016-5-17 下午1:09:12
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_show_mdict_list)
public class AreaActivity extends BaseActivity {

	@ViewInject(R.id.operation_btn)
	private Button operation_btn;
	
	@ViewInject(R.id.c_show_mdict_listView)
	private ListView areaListView;

	private List<AreaBean> areaList;
	private C_AreaAdapter areaAdapter;
	
	private RegionBean regionBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.titlt_textView.setText("区");
		if(getIntent().getStringExtra(Constants.ISPCA) != null){
		}else{
			operation_btn.setText("完成");
			operation_btn.setVisibility(View.VISIBLE);
		}
		areaList = new ArrayList<AreaBean>();
		
		regionBean = (RegionBean) getIntent().getSerializableExtra(Constants.RegionKey);
		if (regionBean.cityBean == null || TextUtils.isEmpty(regionBean.cityBean.CName)) {
			Toast.makeText(this, "错误的城市KEY!", Toast.LENGTH_LONG).show();
			return;
		}

		List<AreaBean> dataList = RegionManager.getInstance().getmAreaBeanMap().get(regionBean.cityBean.CName.trim());
		if (dataList != null && dataList.size() > 0) {
			areaList.addAll(dataList);
		}else{
			AreaBean areaBean= new AreaBean();
			areaBean.AName="";
			regionBean.areaBean = areaBean;
			
			CallBackManager.getInstance().getRegionCallBackManager2().fireCallBack(regionBean);
			finish();
			ActivityStackManager.getInstance().finishActivity(CityActivity.class);
			ActivityStackManager.getInstance().finishActivity(ProvinceActivity.class);
		}
		areaAdapter = new C_AreaAdapter(areaList, this);
		areaListView.setAdapter(areaAdapter);

		areaListView.setOnItemClickListener(myListener);
	}

	OnItemClickListener myListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AreaBean bean = areaList.get(position);
			if (bean != null) {
				regionBean.areaBean = bean;
				
				CallBackManager.getInstance().getRegionCallBackManager2().fireCallBack(regionBean);
				finish();
				ActivityStackManager.getInstance().finishActivity(CityActivity.class);
				ActivityStackManager.getInstance().finishActivity(ProvinceActivity.class);
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
			ActivityStackManager.getInstance().finishActivity(CityActivity.class);
			ActivityStackManager.getInstance().finishActivity(ProvinceActivity.class);
			break;
		default:
			break;
		}
	}
}
