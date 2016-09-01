package com.cvnavi.logistics.i51eyun.eh.activity.address;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.R;

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
 * @date 2016-5-17 上午11:24:15
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(value = R.layout.d_carcode_register_address)
public class CarCodeRegisterAddressActivity extends BaseActivity {

	@ViewInject(value = R.id.carcode_register_address_lv)
	private ListView carcode_register_address_lv;

	private List<String> dataList;
	private CarCodeRegisterAddressAdapter adapter;

	public static CarCodeRegisterAddressCallBack CarCodeRegisterAddressCallBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.titlt_textView.setText("选择车牌注册地简称");

		dataList = new ArrayList<String>();
		dataList.add("京");
		dataList.add("津");
		dataList.add("冀");
		dataList.add("晋");
		dataList.add("蒙");
		dataList.add("辽");
		dataList.add("吉");
		dataList.add("黑");
		dataList.add("沪");
		dataList.add("苏");
		dataList.add("浙");
		dataList.add("皖");
		dataList.add("闽");
		dataList.add("赣");
		dataList.add("鲁");
		dataList.add("豫");
		dataList.add("鄂");
		dataList.add("湘");
		dataList.add("粤");
		dataList.add("桂");
		dataList.add("琼");
		dataList.add("渝");
		dataList.add("川");
		dataList.add("贵");
		dataList.add("黔");
		dataList.add("云");
		dataList.add("藏");
		dataList.add("陕");
		dataList.add("甘");
		dataList.add("青");
		dataList.add("宁");
		dataList.add("新");
		dataList.add("港");
		dataList.add("澳");
		dataList.add("台");

		adapter = new CarCodeRegisterAddressAdapter(this, dataList);
		carcode_register_address_lv.setAdapter(adapter);
	}

	@Event(value = { R.id.back_linearLayout, }, type = View.OnClickListener.class)
	private void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}

	@Event(value = { R.id.carcode_register_address_lv, }, type = AdapterView.OnItemClickListener.class)
	private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String value = dataList.get(position);
		if (TextUtils.isEmpty(value)) {
			return;
		}

		if (CarCodeRegisterAddressCallBack != null) {
			CarCodeRegisterAddressCallBack.onCarCodeRegisterAddress(value);
			finish();
		}
	}

	public interface CarCodeRegisterAddressCallBack {
		public void onCarCodeRegisterAddress(Object object);
	}

}
