package com.cvnavi.logistics.i51eyun.eh.jpush;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cvnavi.logistics.i51eyun.eh.R;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
@SuppressLint("HandlerLeak")
public class JPushActivity extends Activity implements OnClickListener {
	private static final String TAG = "JPush";
	public static boolean isForeground = false;

	private TextView msgText;

	private Button bt_tag;
	private Button bt_alias;

	private TextView appKey, imei, deviceid, registrationId;

	private ToggleButton mTogBtn;
	ToggleBtnStatus btnStatus = new ToggleBtnStatus();
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	private boolean first;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jpush_activity_main);
		init();
		setData();
		registerMessageReceiver();
		setListener();
	}

	private void setListener() {
		mTogBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					btnStatus.setOne(arg1);
					JPushInterface.resumePush(getApplicationContext());
				} else {
					btnStatus.setOne(arg1);
					JPushInterface.stopPush(getApplicationContext());
				}

			}
		});

	}

	private void setData() {
		preferences = getSharedPreferences("togglebuttonstatus", Context.MODE_PRIVATE);
		first = preferences.getBoolean("first", true);
		editor = preferences.edit();
		if (first) {
			getStatus();
		} else {
			btnStatus.one = preferences.getBoolean("s_one", false);
			setToggButonStatus(btnStatus);
		}
	}

	private void setToggButonStatus(ToggleBtnStatus data) {
		mTogBtn.setChecked(data.one);
	}

	private void getStatus() {
		btnStatus.one = mTogBtn.isChecked();
	}

	private void init() {
		registrationId = (TextView) findViewById(R.id.registrationId);
		String mRegId = JPushInterface.getRegistrationID(getApplicationContext());
		registrationId.setText(mRegId);

		appKey = (TextView) findViewById(R.id.appKey);
		String mAppKey = ExampleUtil.getAppKey(getApplicationContext());
		Log.d(TAG, "mAppKey--->>" + mAppKey);
		if (null == mAppKey)
			mAppKey = "AppKey异常";
		appKey.setText(mAppKey);

		imei = (TextView) findViewById(R.id.imei);
		String mImei = ExampleUtil.getImei(getApplicationContext(), "");
		Log.d(TAG, "mImei--->>" + mImei);
		if (null != mImei) {
			imei.setText(mImei);
		}

		deviceid = (TextView) findViewById(R.id.deviceid);
		String mDeviceid = ExampleUtil.getDeviceId(getApplicationContext());
		Log.d(TAG, "mDeviceid--->>" + mDeviceid);
		deviceid.setText(mDeviceid);

		msgText = (TextView) findViewById(R.id.msgText);

		mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn);
		bt_tag = (Button) findViewById(R.id.bt_tag);
		bt_alias = (Button) findViewById(R.id.bt_alias);
		bt_tag.setOnClickListener(this);
		bt_alias.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();

		JPushInterface.onResume(getApplicationContext());
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	@Override
	protected void onStop() {

		super.onStop();
		if (first) {
			editor.putBoolean("first", false);
		}

		editor.putBoolean("s_one", btnStatus.one);
		editor.commit();
	}

	// 从jpush服务器接收客户的信息
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.cvnavi.logistics.i51eyun.eh.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}
	}

	private void setCostomMsg(String msg) {
		if (null != msgText) {
			msgText.setText(msg);
			msgText.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.bt_tag:
			setTag();
			break;

		case R.id.bt_alias:
			setAlias();
			break;
		default:
			break;
		}
	}

	private void setTag() {
		EditText tagEdit = (EditText) findViewById(R.id.et_tag);
		String tag = tagEdit.getText().toString().trim();

		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(JPushActivity.this, "tag不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(JPushActivity.this, "格式不对", Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}

		// 调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}

	private void setAlias() {
		EditText aliasEdit = (EditText) findViewById(R.id.et_alias);
		String alias = aliasEdit.getText().toString().trim();
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(JPushActivity.this, "alias不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(JPushActivity.this, "格式不对", Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}

			ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}

			ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;
			case MSG_SET_TAGS:
				Log.d(TAG, "Set tags in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
				break;
			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
}
