package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.regdata.AppRegData_Consignor;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FragmentPagerAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage.C_HomePageFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.me.c_MeFrgment;
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

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:07:23
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint({ "InlinedApi", "HandlerLeak" })
@ContentView(R.layout.c_activity_main)
public class C_CarGoMainActivity extends FragmentActivity {

	@ViewInject(R.id.back)
	private ImageView backImageView;
	
	@ViewInject(R.id.operation_btn)
	private Button operation_btn;

	private C_HomePageFragment c_homepagefragment;
	private c_MeFrgment c_mefrgment;
	@ViewInject(R.id.vPager)
	private ViewPager vPager;
	private ArrayList<Fragment> fragmentsList;
	private C_FragmentPagerAdapter mAdapter;
	

	@ViewInject(R.id.c_homepager_layout)
	private RelativeLayout c_homepager_layout;
	@ViewInject(R.id.c_me_layout)
	private RelativeLayout c_me_layout;
	
	@ViewInject(R.id.c_homepager_txt)
	private TextView c_homepager_txt;
	@ViewInject(R.id.c_me_txt)
	private TextView c_me_txt;

	@ViewInject(R.id.c_homepager_img)
	private ImageView c_homepager_img;
	
	@ViewInject(R.id.c_me_img)
	private ImageView c_me_img;

	@ViewInject(R.id.titlt_textView)
	private TextView titlt_textView;

	private int Gray = 0xFF7B7873;
	private int Blue = 0xFF00ABED;
	public MyOnClick myclick;
	public MyPageChangeListener myPageChange;

	private long backdownFlag;
	private AlertDialog.Builder alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 头部沉侵
		}
		initViewPager();
		initViews();
		initState();
		ActivityStackManager.getInstance().addActivity(this);
		if (MyApplication.getInstatnce().getUserTypeOid() != null
				&& MyApplication.getInstatnce().getUserTypeOid().equals("B")) {
			loadUserInfo();
		}
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
	}



	private void initViewPager() {
		fragmentsList = new ArrayList<Fragment>();
		c_homepagefragment = C_HomePageFragment.newInstance();
		c_mefrgment = c_MeFrgment.newInstance();
		fragmentsList.add(c_homepagefragment);
		fragmentsList.add(c_mefrgment);
		mAdapter = new C_FragmentPagerAdapter(getSupportFragmentManager(), fragmentsList);
	}

	private void initViews() {
		backImageView.setVisibility(View.GONE);
		myclick = new MyOnClick();
		myPageChange = new MyPageChangeListener();
		vPager.setAdapter(mAdapter);
		vPager.setOnPageChangeListener(myPageChange);
		c_homepager_layout.setOnClickListener(myclick);
		c_me_layout.setOnClickListener(myclick);
	}

	private void initState() {
		c_homepager_img.setImageResource(R.drawable.home);
		c_homepager_txt.setTextColor(Blue);
		titlt_textView.setText("首页");
		vPager.setCurrentItem(0);
	}

	private void loadUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataRequestData requestData = new DataRequestData();
				requestData.setToken(MyApplication.getInstatnce().getToken());
				requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
				requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
				requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
				RequestParams params = new RequestParams(Constants.Get_UserInfo_URL);
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
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								AppRegData_Consignor cargoData = (AppRegData_Consignor) JsonUtils.parseData(str,
										AppRegData_Consignor.class);
								if (cargoData != null) {
									MyApplication.getInstatnce().setCargoData(cargoData);

									myHandler.sendEmptyMessage(Constants.Request_Success);
								} else {
									myHandler.sendEmptyMessage(Constants.Request_NoData);
								}
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();

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

	/**
	 * 底部菜单点击监听
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyOnClick implements OnClickListener {
		@Override
		public void onClick(View view) {
			clearChioce();
			iconChange(view.getId());
		}
	}

	/**
	 * 当page改变时的监听 改变时底部菜单随着改变
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 2) {
				int i = vPager.getCurrentItem();
				clearChioce();
				iconChange(i);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
		}
	}

	/**
	 * 建立一个清空选中状态的方法
	 */
	public void clearChioce() {
		c_homepager_img.setImageResource(R.drawable.home_grey);
		c_homepager_txt.setTextColor(Gray);
		c_me_img.setImageResource(R.drawable.me);
		c_me_txt.setTextColor(Gray);
	}

	/**
	 * 定义一个底部导航栏图标变化的方法
	 * 
	 * @param num
	 */
	public void iconChange(int num) {
		switch (num) {
		case R.id.c_homepager_layout:
		case 0:
			operation_btn.setVisibility(View.GONE);
			
			c_homepager_img.setImageResource(R.drawable.home);
			c_homepager_txt.setTextColor(Blue);
			titlt_textView.setText("首页");
			vPager.setCurrentItem(0);
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
			c_me_txt.setTextColor(Blue);
			titlt_textView.setText("我的信息");
			vPager.setCurrentItem(1);
			break;
		}

	}

	private boolean exitApplication() {
		if (backdownFlag == 0 ? true : false) {
			backdownFlag = System.currentTimeMillis();
			Toast.makeText(C_CarGoMainActivity.this, "再按一次退出！", Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (MyApplication.getInstatnce().getLoginData().getUserType_Oid().equals("B") == false) {
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
				alertDialog = new AlertDialog.Builder(C_CarGoMainActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
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
