package com.cvnavi.logistics.i51eyun.eh.activity.guid;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 上午9:18:38
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class GuidViewPagerAdapter extends PagerAdapter {

	private List<View> views;

	public GuidViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	/** 移除position的位置的界面时调用 */
	@Override
	public void destroyItem(View view, int position, Object object) {
		((ViewPager) view).removeView(views.get(position));

	}

	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化position位置的界面
	@Override
	public Object instantiateItem(View view, int position) {

		((ViewPager) view).addView(views.get(position), 0);
		return views.get(position);

	}

	//判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0==arg1);
	}

}
