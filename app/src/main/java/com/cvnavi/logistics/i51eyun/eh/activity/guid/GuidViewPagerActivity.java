package com.cvnavi.logistics.i51eyun.eh.activity.guid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 上午9:18:48
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class GuidViewPagerActivity extends BaseActivity implements OnClickListener,OnPageChangeListener {

	private ViewPager viewPager;
	private GuidViewPagerAdapter adapter;
	private List<View> views;
	private Button button;
	//图片资源
	
	/**底部小点图片*/
	private ImageView[] dots;
	
	/**记录当前选中位置**/
	private int currentIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guid);
		initview();
	}
	private void initview() {
		button=(Button) findViewById(R.id.button);
		button.setOnClickListener(this);
		views = new ArrayList<View>();
		
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		//初始化引导图片列表
		for (int i = 0; i < Constants.pics.length; i++) {
			ImageView imageView=new ImageView(this);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageResource(Constants.pics[i]);
			views.add(imageView);
		}
		
//		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		//初始化引导图片列表
//		for (int i = 0; i < Constants.pics.length; i++) {
//			ImageView imageView=new ImageView(this);
//			imageView.setLayoutParams(layoutParams);
//			imageView.setScaleType(ImageView.ScaleType.CENTER);
//			imageView.setImageResource(Constants.pics[i]);
//			views.add(imageView);
//		}
		
		//初始化adapter
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter=new GuidViewPagerAdapter(views);
		viewPager.setAdapter(adapter);
		//绑定回调
		viewPager.setOnPageChangeListener(this);
		//初始化底部小圆点 
        initDots();
        
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GuidViewPagerActivity.this, LoginActivity.class);
				GuidViewPagerActivity.this.startActivity(intent);
				finish();
				
			}
		});
	}
	/**
	 * 初始化底部小圆点
	 */
	private void initDots() {
	
		LinearLayout ll=(LinearLayout) findViewById(R.id.ll);
		dots=new ImageView[Constants.pics.length];
		
		//循环取得小圆点图片
		for (int i = 0; i < Constants.pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);//都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);//设置位置tag,方便取出与当前位置对应
			
		}
		currentIndex=0;
		dots[currentIndex].setEnabled(false);//设置为白色,即选中状态
		
	}
	/**
	 * 设置当前的引导页
	 * @param position
	 */
	private void setCurView(int position){
		if (position<0||position>=Constants.pics.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}
	/**
	 * 设置当前引导小点的位置
	 * @param position
	 */
	private void setCurDot(int position){
		if (position<0||position>=Constants.pics.length||currentIndex==position) {
			return;
		}
		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		
		currentIndex=position;
	}
	 //当滑动状态改变时调用 
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	//当当前页面被滑动时调用 
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	//当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		//设置底部小点选中状态
		setCurDot(arg0);
		if (arg0==dots.length-1) {//根据引导页的数量-1对比
			button.setVisibility(View.VISIBLE);
		}else {
			button.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int position=(Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}
}
