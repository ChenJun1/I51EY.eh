package com.cvnavi.logistics.i51eyun.eh.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;

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
@SuppressLint("SetJavaScriptEnabled")
@ContentView(R.layout.help)
public class HelpActivity extends BaseActivity {

	@ViewInject(value = R.id.titlt_textView)
	private TextView titlt_textView;

	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;

	@ViewInject(R.id.webview)
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titlt_textView.setText("帮助手册");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setDefaultTextEncodingName("GBK");
		webview.loadUrl(Constants.HelpManual_URL);
	}

	@Event(value = R.id.back_linearLayout, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}
}
