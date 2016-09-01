package com.cvnavi.logistics.i51eyun.eh.activity.registered;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_CarGoMainActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.D_DriverMainActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.registered.view.IRegisteredView;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(value = R.layout.activity_registered1)
//@ContentView(value = R.layout.d_order_info_complete)
public class RegisteredActivity extends BaseActivity implements IRegisteredView {
	// private CommonWaitDialog waitDialog;

	@ViewInject(value = R.id.back_linearLayout)
	private LinearLayout backLinearLayout;
	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	/*@ViewInject(value = R.id.driver_btn)
	private Button driverButton;
	@ViewInject(value = R.id.cargo_btn)
	private Button cargoButton;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 头部沉侵
		// }
		// ActivityStackManager.instance().addActivity(this);
		titlt_textView.setText("选择司机/货主");
		backLinearLayout.setVisibility(View.GONE);

		// findViewById(R.id.c_role_but).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// startActivity(new Intent(RegisteredActivity.this,
		// CarGoMainActivity.class));
		// }
		// });
		// findViewById(R.id.d_role_but).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// startActivity(new Intent(RegisteredActivity.this,
		// DriverMainActivity.class));
		// }
		// });
		// initData();
	}

	// private void initData() {
	// waitDialog = new CommonWaitDialog(RegisteredActivity.this,
	// R.style.progress_dialog);
	// }

	@Event(value = { R.id.driver_btn, R.id.cargo_btn }, type = View.OnClickListener.class)
	private void onViewClick(View view) {
		switch (view.getId()) {
		case R.id.driver_btn:
			showActivity(RegisteredActivity.this, D_DriverMainActivity.class);
			break;
		case R.id.cargo_btn:
			showActivity(RegisteredActivity.this, C_CarGoMainActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public String getUserPhone() {
		return null;
	}

	@Override
	public String getVerification_code() {
		return null;
	}

	@Override
	public String getUserPasswordone() {
		return null;
	}

	@Override
	public String getUserPasswordtwo() {
		return null;
	}

	@Override
	public String getCar_goods() {
		return null;
	}

	@Override
	public boolean isisClick() {
		return false;
	}

	@Override
	public void showLoading() {
		// waitDialog.show();
	}

	@Override
	public void hideLoading() {
		// waitDialog.dismiss();
	}

	/**
	 * 请求成功
	 * 
	 * @param json
	 */
	@Override
	public void toRegisteredActivity(JSONObject json) {

	}

	/**
	 * 发现错误
	 * 
	 * @param json
	 */
	@Override
	public void error(String json) {

	}
}
