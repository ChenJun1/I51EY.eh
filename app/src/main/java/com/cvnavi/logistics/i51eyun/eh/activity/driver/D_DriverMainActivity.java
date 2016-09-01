package com.cvnavi.logistics.i51eyun.eh.activity.driver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Driver;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FragmentPagerAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.homepagerfragment.D_HomePagerFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.homepagerfragment.D_MeFragment;
import com.cvnavi.logistics.i51eyun.eh.manager.ReceiveGPSDataManager;
import com.cvnavi.logistics.i51eyun.eh.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

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
@SuppressLint({ "InlinedApi", "HandlerLeak" })
@ContentView(value = R.layout.d_activity_main)
public class D_DriverMainActivity extends FragmentActivity {

	@ViewInject(R.id.operation_btn)
	private Button operation_btn;

	// 下面每个Layout对象
	@ViewInject(R.id.c_homepager_layout)
	private RelativeLayout c_homepager_layout;
	@ViewInject(R.id.c_me_layout)
	private RelativeLayout c_me_layout;

	// 获得TextView
	@ViewInject(R.id.c_homepager_txt)
	private TextView c_homepager_txt;
	@ViewInject(R.id.c_me_txt)
	private TextView c_me_txt;

	// 获得ImageView
	@ViewInject(R.id.c_homepager_img)
	private ImageView c_homepager_img;
	@ViewInject(R.id.c_me_img)
	private ImageView c_me_img;

	// 获得title标题
	@ViewInject(value = R.id.back_linearLayout)
	private LinearLayout back_linearLayout;
	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	// 定义颜色值
	private int Gray = 0xFF7B7873;
	@ViewInject(R.id.viewPager)
	private ViewPager viewPager;
	private ArrayList<Fragment> fragmentsList;
	private C_FragmentPagerAdapter mAdapter;

	private long backdownFlag; // 点击两次 退出
	private AlertDialog.Builder alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 头部沉侵
		}

		ActivityStackManager.getInstance().addActivity(this);

		initViewPager();
		initViews();
		initState();

		if (MyApplication.getInstatnce().getUserTypeOid() != null
				&& MyApplication.getInstatnce().getUserTypeOid().equals("A")) {
			loadUserInfo();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		ReceiveGPSDataManager.getInstance().start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onStop() {
		ReceiveGPSDataManager.getInstance().stop();
		
		super.onStop();
	}

	private void initViews() {
		back_linearLayout.setVisibility(View.GONE);
		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {
			}

			@Override
			public void onPageSelected(int i) {
			}

			@Override
			public void onPageScrollStateChanged(int i) {
				if (i == 2) {
					int a = viewPager.getCurrentItem();
					clearChioce();
					iconChange(a);
				}
			}
		});
		c_homepager_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clearChioce();
				iconChange(view.getId());
			}
		});
		c_me_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!NetWorkUtils.isConnectedByState(D_DriverMainActivity.this)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(D_DriverMainActivity.this);
					builder.setTitle("提示");
					builder.setMessage("请检查网络是否连接！");
					builder.setPositiveButton("确定", null);
					builder.create().show();
					return;
				}
				clearChioce();
				iconChange(view.getId());
			}
		});
	}

	private void initState() {
		c_homepager_img.setImageResource(R.drawable.home);
		c_homepager_txt.setTextColor(0xFF00ABED);
		titlt_textView.setText("首页");
		viewPager.setCurrentItem(0);
	}

	private void initViewPager() {
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(D_HomePagerFragment.getInstance());
		fragmentsList.add(D_MeFragment.getInstance());
		mAdapter = new C_FragmentPagerAdapter(getSupportFragmentManager(), fragmentsList);
	}

	private void loadUserInfo() {
		new Thread(new Runnable() {
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());

				RequestParams params = new RequestParams(Constants.Get_UserInfo_URL);
//				RequestParams params = new RequestParams("http://10.10.11.124:8083/api/lps/v2/CarPosition/GM0001/784C624F-2736-478B-AE36-09EBAF8298C5");
				
				try {
					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());
						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj=Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								AppRegData_Driver driverData = (AppRegData_Driver) JsonUtils.parseData(str,
										AppRegData_Driver.class);
								if (driverData != null) {
									MyApplication.getInstatnce().setDriverData(driverData);

									myHandler.sendEmptyMessage(Constants.Request_Success);
								} else {
									myHandler.sendEmptyMessage(Constants.Request_NoData);
								}
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
								msg.obj = "账号数据异常！请联系客服！";

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

	private void clearChioce() {
		c_homepager_img.setImageResource(R.drawable.home_grey);
		c_homepager_txt.setTextColor(Gray);
		c_me_img.setImageResource(R.drawable.me);
		c_me_txt.setTextColor(Gray);
	}

	public void iconChange(int num) {
		switch (num) {
		case R.id.c_homepager_layout:
		case 0:
			operation_btn.setVisibility(View.GONE);

			c_homepager_img.setImageResource(R.drawable.home);
			c_homepager_txt.setTextColor(0xFF00ABED);
			titlt_textView.setText("首页");
			viewPager.setCurrentItem(0);
			break;
		case R.id.c_me_layout:
		case 1:
			if (MyApplication.getInstatnce().getLoginData() == null) {
				return;
			}

			if (TextUtils.isEmpty(MyApplication.getInstatnce().getLoginData().getISEvaluate())
					|| MyApplication.getInstatnce().getLoginData().getISEvaluate().equals("0")) {
				operation_btn.setVisibility(View.GONE);
			} else {
				operation_btn.setVisibility(View.VISIBLE);
				operation_btn.setTextSize(12f);
				operation_btn.setText("等级：" + MyApplication.getInstatnce().getBasicDataBuffer()
						.getGradeName(MyApplication.getInstatnce().getLoginData().getEvaluation_Score()));
			}

			c_me_img.setImageResource(R.drawable.tab_me);
			c_me_txt.setTextColor(0xFF00ABED);
			titlt_textView.setText("我的信息");
			viewPager.setCurrentItem(1);
			break;
		}
	}

	private boolean exitApplication() {
		if (backdownFlag == 0 ? true : false) {
			backdownFlag = System.currentTimeMillis();
			Toast.makeText(D_DriverMainActivity.this, "再按一次退出！", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			if (System.currentTimeMillis() - backdownFlag <= 2000 ? true : false) {
				ActivityStackManager.getInstance().AppExit(this);
				return true;
			} else {
				backdownFlag = 0;
				exitApplication();
				return true;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (MyApplication.getInstatnce().getLoginData().getUserType_Oid().equals("A") == false) {
				return super.onKeyDown(keyCode, event);
			}

			return exitApplication();
		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.Request_Success:
				LogUtil.d("-->>获取用户信息成功！");
				break;
			case Constants.Request_NoData:
				LogUtil.d("-->>获取用户信息无数据！");
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_DriverMainActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			default:
				break;
			}
		}
	};
}
