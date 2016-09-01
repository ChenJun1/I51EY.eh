package com.cvnavi.logistics.i51eyun.eh.activity.guid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.AppUpdateBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.login.LoginActivity;
import com.cvnavi.logistics.i51eyun.eh.service.DownAPKService;
import com.cvnavi.logistics.i51eyun.eh.utils.AppUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 上午9:18:54
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class WelcomeActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 1500;// 延迟3秒
	private SharedPreferences preferences;
	private Editor editor;
	private AlertDialog.Builder alertDialog;
	private CommonWaitDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		preferences = getSharedPreferences("Phone", Context.MODE_PRIVATE);
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
		waitDialog.show();
		getVison(Constants.GetAppVersion_URL);
	}

	private void toLogin() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (preferences.getBoolean("firststart", true)) {
					editor = preferences.edit();
					// 将登录标志位设置为false，下次登录时不在显示首次登录界面
					editor.putBoolean("firststart", false);
					editor.commit();
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this,
							GuidViewPagerActivity.class);
					WelcomeActivity.this.startActivity(intent);
					WelcomeActivity.this.finish();
				} else {
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, LoginActivity.class);
					WelcomeActivity.this.startActivity(intent);
					WelcomeActivity.this.finish();
				}

			}
		}, SPLASH_DISPLAY_LENGHT);
	}

	private Handler myHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(waitDialog!=null){
				waitDialog.dismiss();
			}
			switch (msg.what) {
			case Constants.Request_Success:
				String vison = (String) msg.obj;
				String curVerName = AppUtils.getVerName(WelcomeActivity.this);
				if (!curVerName.equals(vison)) {
					alertDialog = new AlertDialog.Builder(WelcomeActivity.this);
					alertDialog.setCancelable(false);
					alertDialog.setTitle("更新提示");
					alertDialog.setMessage("当前版本非最新版本，请下载最新版本！");
					alertDialog.setPositiveButton("确定",
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getAppDownUrl(Constants.GetAppDownPath_URL);
								}

							});
					alertDialog.show();
				} else {
					toLogin();
				}

				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(WelcomeActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "获取新版本失败，请联系客服！"
						: String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			case Constants.Request_Success_load:
				AppUpdateBean bean = (AppUpdateBean) msg.obj;
				File file = null;
				if (bean != null) {//判断是否存在安装包
					String filePath = DownAPKService.getStoragePath()
							+ bean.AppName;
					file = new File(filePath);
					if (file.exists()) {
						boolean delete = file.delete();
						if (delete) {
							successGetDwonAppUrl(bean);
						}
					} else {
						successGetDwonAppUrl(bean);
					}

				}
				break;
			default:
				break;
			}

		}
	};

	private void getAppDownUrl(final String Url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url);

				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");

					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);

						Message msg = Message.obtain();
						DataResultBase daResultBase;
						try {
							daResultBase = JsonUtils.parseDataResultBase(str);
							if (daResultBase.isSuccess()
									&& daResultBase.getDataValue() != null) {

								AppUpdateBean bean = JsonUtils.parseData2(
										daResultBase.getDataValue().toString(),
										AppUpdateBean.class);
								msg.what = Constants.Request_Success_load;
								msg.obj = bean;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = Constants.errorMsg;
								myHandler.sendMessage(msg);
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private void getVison(final String Url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url);

				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj = Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");

					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);

						DataResultBase dataResultBase;
						try {
							dataResultBase = JsonUtils.parseDataResultBase(str);

							if (dataResultBase.isSuccess()) {
								JSONObject object = new JSONObject(String
										.valueOf(dataResultBase.getDataValue()));
								String AndroidVersion = (String) object
										.get("AndroidVersion");
								if (AndroidVersion != null
										&& !"".equals(AndroidVersion)) {

									Message msg = Message.obtain();
									msg.what = Constants.Request_Success;
									msg.obj = AndroidVersion;
									myHandler.sendMessage(msg);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	public void successGetDwonAppUrl(final AppUpdateBean appBean) {

		if (appBean != null) {
			alertDialog = new AlertDialog.Builder(WelcomeActivity.this);
			alertDialog.setCancelable(false);
			alertDialog.setTitle("下载更新");
			alertDialog.setMessage("开始后台下载，完成后自动安装");
			alertDialog.setPositiveButton("确认",
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DownAPKService.startService(WelcomeActivity.this,
									"e货", appBean.AppUrl,
									Integer.parseInt(appBean.AppSize));
						}
					});
			alertDialog.show();
		}
	}

}
