package com.cvnavi.logistics.i51eyun.eh.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.C_OrderInfoWaitingActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage.C_HomePageFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.homepagerfragment.D_HomePagerFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_WaitingOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.login.LoginActivity;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushOrderMessageBean;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "-->>[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "-->>[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

			Log.d(TAG, "-->>[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

			Log.d(TAG, "-->>[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "-->>[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

			Log.d(TAG, "-->>[MyReceiver] 用户点击打开了通知");
			processNotificationCustomMessage(context, bundle);

			// 打开自定义的Activity
			// Intent i = new Intent(context, NotificationBarActivity.class);
			// i.putExtras(bundle); //
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

			Log.d(TAG, "-->>[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {

			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "-->>[MyReceiver]" + intent.getAction() + " 连接状态变化 " + connected);

		} else {

			Log.d(TAG, "-->>[MyReceiver] Unhandled intent - " + intent.getAction());

		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG + "-->>MyReceiver---->>printBundle", "此消息没有额外的数据");
					continue;
				}
				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();
					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "获得额外的JSON错误消息!");
				}
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * MA,//货主发指定订单 
	 * MB,//司机拒接指定订单 
	 * MC,//货主修改订单-已接 
	 * MD,//货主取消发货-已接 
	 * ME,//司机取消接单-已接
	 * MF,//司机提货 
	 * MG,//司机交货 
	 * MH,//货主确认收货 
	 * MI,//司机信息审核通过
	 * MJ,//司机信息审核驳回
	 * MK,//货主订单无人接取（80%）
	 * ML,//司机接单
	 * MM,//货主取消指定
	 * @param context
	 * @param bundle
	 */
	private void processNotificationCustomMessage(Context context, Bundle bundle) {
		String msgContent = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		JPushOrderMessageBean bean = null;
		try {
			bean = JsonUtils.parseJPushMessageBase(extras);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (bean != null) {
			if (bean.User_Key.equals(MyApplication.getInstatnce().getUserKey()) == false) {
				return;
			}
			bean.Message_Content = msgContent;

			Intent intent = null;
			if (!TextUtils.isEmpty(bean.MessageType) && bean.MessageType.equals("MA")) {// A是指定订单，跳车主详情
				if (Constants.isLogin) {
					intent = new Intent(context, D_WaitingOrderDetailActivity.class);
					intent.putExtra(Constants.JPushOrderKey, bean);
				} else {
					intent = new Intent(context, LoginActivity.class);
				}
			} else if (!TextUtils.isEmpty(bean.MessageType) && bean.MessageType.equals("MB")) {// B拒接订单，跳货主详情
				if (Constants.isLogin) {
					intent = new Intent(context, C_OrderInfoWaitingActivity.class);
					intent.putExtra(Constants.JPushOrderKey, bean);
				} else {
					intent = new Intent(context, LoginActivity.class);
				}
			}

			if (intent != null) {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

				context.startActivity(intent);
			}
		}
	}

	private void processCustomMessage(Context context, Bundle bundle) {
		String msgContent = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		JPushOrderMessageBean bean = null;
		try {
			bean = JsonUtils.parseJPushMessageBase(extras);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (bean != null) {
			if (bean.User_Key.equals(MyApplication.getInstatnce().getUserKey()) == false) {
				return;
			}
			bean.Message_Content = msgContent;

			if (MyApplication.getInstatnce().getUserTypeOid().equals("A")) { // 司机
				if (D_HomePagerFragment.isForeground) {
					Intent intent = new Intent(D_HomePagerFragment.MESSAGE_RECEIVED_ACTION);
					intent.putExtra(D_HomePagerFragment.KEY_EXTRAS, bean);
					context.sendBroadcast(intent);
				}
			} else if (MyApplication.getInstatnce().getUserTypeOid().equals("B")) { // 货主
				if (C_HomePageFragment.isForeground) {
					Intent intent = new Intent(D_HomePagerFragment.MESSAGE_RECEIVED_ACTION);
					intent.putExtra(D_HomePagerFragment.KEY_EXTRAS, bean);
					context.sendBroadcast(intent);
				}
			}
		}
	}
}
