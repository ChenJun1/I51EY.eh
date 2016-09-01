package com.cvnavi.logistics.i51eyun.eh.activity.cargo;///**
// * Administrator2016-5-19
// */
//package com.cvnavi.logistics.i51eyun.eh.activity.cargo;
//
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.Event;
//import org.xutils.view.annotation.ViewInject;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.ToggleButton;
//
//import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
//import com.cvnavi.logistics.i51eyun.eh.R;
//
///**
// * 版权所有 上海势航
// * 
// * @author chenJun and johnnyYuan
// * @description
// * @date 2016-5-19 下午5:24:59
// * @version 1.0.0
// * @email yuanlunjie@163.com
// */
//
//@ContentView(R.layout.c_setting)
//public class C_SettingActivity extends BaseActivity {
//
//	@ViewInject(value = R.id.diabolo_tbtn)
//	private ToggleButton diabolo_tbtn;
//	@ViewInject(value = R.id.shake_tbtn)
//	private ToggleButton shake_tbtn;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		this.titlt_textView.setText("设置");
//
//	}
//
//	@Event(value = { R.id.back_linearLayout }, type = View.OnClickListener.class)
//	private void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.back_linearLayout:
//			finish();
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Event(value = { R.id.diabolo_tbtn, R.id.shake_tbtn }, type = CompoundButton.OnCheckedChangeListener.class)
//	private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		switch (buttonView.getId()) {
//		case R.id.diabolo_tbtn:
//			if (isChecked) {
//
//			} else {
//				
//			}
//			break;
//		case R.id.shake_tbtn:
//			if (isChecked) {
//
//			} else {
//
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//}
