package com.cvnavi.logistics.i51eyun.eh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.ui.ISkipActivity;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and jonyYuan
 * @description
 * @date 2016-5-17 上午11:19:11
 * @version 1.0.0
 * @email
 */

@SuppressLint("InlinedApi")
public abstract class BaseActivityForNoTitle extends Activity implements ISkipActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		ActivityStackManager.getInstance().addActivity(this);
		LogUtil.i("-->>onCreate");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 头部沉侵
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.i("-->>onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.i("-->>onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.i("-->>onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogUtil.i("-->>onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.i("-->>onDestroy");
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}
}
