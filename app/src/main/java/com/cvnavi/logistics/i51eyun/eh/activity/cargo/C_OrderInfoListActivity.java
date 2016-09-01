/**
 * Administrator2016-4-21
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FragmentPagerAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage.C_FulfilOrderFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage.C_ReceivedOrderFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.homepage.C_WaitingOrderFragment;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:08:17
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@ContentView(R.layout.c_orderinfolist)
public class C_OrderInfoListActivity extends FragmentActivity {
	
	/**
	 * Tab的那个引导线
	 */
	@ViewInject(R.id.id_tab_line_iv)
	private View mTabLineIv;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	
	@ViewInject(R.id.titlt_textView)
	private TextView titlTextView;
	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;
	// 定义三Fragment
	private C_WaitingOrderFragment c_WaitingOrderFragment;
	private C_ReceivedOrderFragment c_ReceivedOrderFragment;
	private C_FulfilOrderFragment c_FulfilOrderFragment;
	// 定义一个ViewPager容器
	@ViewInject(R.id.c_order_vPager)
	private ViewPager mPager;
	
	private ArrayList<Fragment> fragmentsList;
	private C_FragmentPagerAdapter mAdapter;
	// 下面每个Layout对象
	@ViewInject(R.id.c_waiting_order_layout)
	private RelativeLayout c_waiting_order_layout;
	
	@ViewInject(R.id.c_received_order_layout)
	private RelativeLayout c_received_order_layout;
	
	@ViewInject(R.id.c_fulfil_order_layout)
	private RelativeLayout c_fulfil_order_layout;
	// 依次TextView
	@ViewInject(R.id.c_waiting_order_txt)
	private TextView c_waiting_order_txt;
	
	@ViewInject(R.id.c_received_order_txt)
	private TextView c_received_order_txt;
	
	@ViewInject(R.id.c_fulfil_order_txt)
	private TextView c_fulfil_order_txt;
	// 定义颜色值
	private int Gray = 0xFF73716C;
	private int Blue = 0xFF0089D2;
	// 定义FragmentManager对象
	public FragmentManager fManager;
	// 定义一个Onclick全局对象
	public MyOnClick myclick;
	public MyPageChangeListener myPageChange;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 头部沉侵
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//底部沉侵
		}
		fManager = getSupportFragmentManager();// 得到FragmentManager对象（Activity要继承FragmentActivity）
		initViewPager();// 实例化ViewPager及其内容
		initViews();// 实例化窗口中的其他控件
		initState();// 还原初始状态

		initTabLineWidth();
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
		c_WaitingOrderFragment = new C_WaitingOrderFragment();
		c_ReceivedOrderFragment = new C_ReceivedOrderFragment();
		c_FulfilOrderFragment = new C_FulfilOrderFragment();
		fragmentsList.add(c_WaitingOrderFragment);
		fragmentsList.add(c_ReceivedOrderFragment);
		fragmentsList.add(c_FulfilOrderFragment);
		mAdapter = new C_FragmentPagerAdapter(fManager, fragmentsList);
	}

	private void initViews() {
		titlTextView.setText("运单列表");
		myclick = new MyOnClick();
		myPageChange = new MyPageChangeListener();
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(myPageChange);
		c_waiting_order_layout.setOnClickListener(myclick);
		c_received_order_layout.setOnClickListener(myclick);
		c_fulfil_order_layout.setOnClickListener(myclick);
	}

	private void initState() {
		c_waiting_order_txt.setTextColor(Blue);
		mPager.setCurrentItem(0);
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
				int i = mPager.getCurrentItem();
				clearChioce();
				iconChange(i);
			}
		}

		@Override
		public void onPageScrolled(int position, float offset, int offsetPixels) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
					.getLayoutParams();

			Log.e("offset:", offset + "");
			/**
			 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
			 * 设置mTabLineIv的左边距 滑动场景：
			 * 记3个页面,
			 * 从左到右分别为0,1,2 
			 * 0->1; 1->2; 2->1; 1->0
			 */

			if (currentIndex == 0 && position == 0)// 0->1
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));

			} else if (currentIndex == 1 && position == 0) // 1->0
			{
				lp.leftMargin = (int) (-(1 - offset)
						* (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));

			} else if (currentIndex == 1 && position == 1) // 1->2
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));
			} else if (currentIndex == 2 && position == 1) // 2->1
			{
				lp.leftMargin = (int) (-(1 - offset)
						* (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));
			}else if (currentIndex == 2 && position == 2) // 0->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
				} 
			mTabLineIv.setLayoutParams(lp);
		}

		@Override
		public void onPageSelected(int position) {
			
			currentIndex = position;
		}

	}

	/**
	 * 建立一个清空选中状态的方法
	 */
	public void clearChioce() {
		c_waiting_order_txt.setTextColor(Gray);
		c_received_order_txt.setTextColor(Gray);
		c_fulfil_order_txt.setTextColor(Gray);
	}

	/**
	 * 定义一个底部导航栏图标变化的方法
	 * 
	 * @param num
	 */
	public void iconChange(int num) {
		switch (num) {
		case R.id.c_waiting_order_layout:
		case 0:
			c_waiting_order_txt.setTextColor(Blue);
			mPager.setCurrentItem(0);
			break;
		case R.id.c_received_order_layout:
		case 1:
			c_received_order_txt.setTextColor(Blue);
			mPager.setCurrentItem(1);
			break;
		case R.id.c_fulfil_order_layout:
		case 2:
			c_fulfil_order_txt.setTextColor(Blue);
			mPager.setCurrentItem(2);
			break;
		}

	}

	@Event(value = R.id.back_linearLayout, type = OnClickListener.class)
	private void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			this.finish();
			break;
		default:
			break;
		}
	}

	
	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 3;
		mTabLineIv.setLayoutParams(lp);
		
	}
}
