package com.cvnavi.logistics.i51eyun.eh.activity.driver.order;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter.C_FragmentPagerAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.CompletedOrderFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.ReceivedOrderFragment;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.WaitingOrderFragment;
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
 * @date 2016-5-17 下午1:12:36
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("InlinedApi")
@ContentView(R.layout.d_activity_order)
public class D_OrderListActivity extends FragmentActivity {

	@ViewInject(R.id.titlt_textView)
	private TextView titlTextView;
	@ViewInject(R.id.back_linearLayout)
	private LinearLayout back_linearLayout;

	@ViewInject(R.id.id_tab_line_iv)
	private View mTabLineIv;
	// 定义一个ViewPager容器
	@ViewInject(R.id.c_order_vPager)
	private ViewPager mPager;

	private int currentIndex;
	private int screenWidth;

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
	private int Gray = 0xFF999999;
	private int Green = 0xFF0089D2;
	// 定义颜色值
	// 定义FragmentManager对象
	public FragmentManager fManager;

	private MyOnClick myclick;

	private MyPageChangeListener myPageChange;

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

	private void initState() {
		c_waiting_order_txt.setTextColor(Green);
		mPager.setCurrentItem(0);
	}

	private void initViews() {
		titlTextView.setText("运单列表");
		myclick = new MyOnClick();

		myPageChange = new MyPageChangeListener();
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(myPageChange);
		mPager.setOffscreenPageLimit(3);
		c_waiting_order_layout.setOnClickListener(myclick);
		c_received_order_layout.setOnClickListener(myclick);
		c_fulfil_order_layout.setOnClickListener(myclick);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!NetWorkUtils.isNetWort(this)) {
			return;
		}
	}

	private void initViewPager() {
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(WaitingOrderFragment.getFragment());
		fragmentsList.add(ReceivedOrderFragment.getFragment());
		fragmentsList.add(CompletedOrderFragment.getFragment());
		mAdapter = new C_FragmentPagerAdapter(fManager, fragmentsList);
	}

	public class MyOnClick implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			clearChioce();
			iconChange(view.getId());
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

	public void iconChange(int num) {
		switch (num) {
		case R.id.c_waiting_order_layout:
		case 0:
			c_waiting_order_txt.setTextColor(Green);
			mPager.setCurrentItem(0);
			break;
		case R.id.c_received_order_layout:
		case 1:
			c_received_order_txt.setTextColor(Green);
			mPager.setCurrentItem(1);
			break;
		case R.id.c_fulfil_order_layout:
		case 2:
			c_fulfil_order_txt.setTextColor(Green);
			mPager.setCurrentItem(2);
			break;
		}
	}

	public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float offset, int i1) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
					.getLayoutParams();

			Log.e("offset:", offset + "");
			/**
			 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来 设置mTabLineIv的左边距
			 * 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1; 1->0
			 */

			if (currentIndex == 0 && position == 0)// 0->1
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));

			} else if (currentIndex == 1 && position == 0) // 1->0
			{
				lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));

			} else if (currentIndex == 1 && position == 1) // 1->2
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));
			} else if (currentIndex == 2 && position == 1) // 2->1
			{
				lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));
			} else if (currentIndex == 2 && position == 2) // 0->2
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
						* (screenWidth / 3));
			} 
			mTabLineIv.setLayoutParams(lp);
		}

		@Override
		public void onPageSelected(int i) {
			currentIndex = i;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 2) {
				int i = mPager.getCurrentItem();
				clearChioce();
				iconChange(i);
			}
		}
	}

	@Event(value = R.id.back_linearLayout, type = View.OnClickListener.class)
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
