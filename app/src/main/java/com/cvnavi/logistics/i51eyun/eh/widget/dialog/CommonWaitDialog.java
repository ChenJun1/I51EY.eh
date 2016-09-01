package com.cvnavi.logistics.i51eyun.eh.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cvnavi.logistics.i51eyun.eh.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description  通用等待框
 * @date 2016-5-17 下午1:27:21
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("HandlerLeak")
public class CommonWaitDialog extends Dialog {

	private long mTimeOut = 0;// 默认timeOut为0即无限大
	private OnTimeOutListener mTimeOutListener = null;// timeOut后的处理器
	private Timer mTimer = null;// 定时器
	private Context mContext;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismiss();
			if (mTimeOutListener != null) {
				mTimeOutListener.onTimeOut(CommonWaitDialog.this);
				dismiss();
			}
		}
	};

	public CommonWaitDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public CommonWaitDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c_progress_dialog);
		setCanceledOnTouchOutside(true);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setCancelable(false);
	}

	public void setBadMessage(CharSequence message) {
		dismiss();
	}

	/**
	 * 设置timeOut长度，和处理器
	 *
	 * @param t
	 *            timeout时间长度
	 * @param timeOutListener
	 *            超时后的处理器
	 */
	public void setTimeOut(long t, OnTimeOutListener timeOutListener) {
		mTimeOut = t;
		if (timeOutListener != null) {
			this.mTimeOutListener = timeOutListener;
		}
	}

	public static CommonWaitDialog getProgressDialog(Context context, long time, OnTimeOutListener listener) {
		CommonWaitDialog commonWaitDialog = new CommonWaitDialog(context);
		if (time != 0) {
			commonWaitDialog.setTimeOut(time, listener);
		}
		return commonWaitDialog;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mTimeOut != 0) {
			mTimer = new Timer();
			TimerTask timerTast = new TimerTask() {
				@Override
				public void run() {
					Message msg = mHandler.obtainMessage();
					mHandler.sendMessage(msg);
				}
			};
			mTimer.schedule(timerTast, mTimeOut);
		}
	}

	/**
	 *
	 * 处理超时的的接口
	 *
	 */
	public interface OnTimeOutListener {
		/**
		 * 当progressDialog超时时调用此方法
		 */
		void onTimeOut(CommonWaitDialog dialog);
	}
}
