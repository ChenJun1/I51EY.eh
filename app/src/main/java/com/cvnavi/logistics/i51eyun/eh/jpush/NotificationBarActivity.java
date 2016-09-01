package com.cvnavi.logistics.i51eyun.eh.jpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:19:57
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class NotificationBarActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView view = new TextView(this);
		view.setText("自定义的Activity");
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			view.setText("title:" + title + "content:" + content);
		}

		addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}
}
