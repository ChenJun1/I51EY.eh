package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:05:31
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_ViewPagerAdapter extends PagerAdapter {

	private ImageView[][] mImageViews;

	private int[] mImageRes;

	public C_ViewPagerAdapter(ImageView[][] imageViews, int[] imageRes) {
		this.mImageViews = imageViews;
		this.mImageRes = imageRes;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mImageRes.length == 1) {
			return mImageViews[position / mImageRes.length % 2][0];
		} else {
			((ViewPager) container).addView(mImageViews[position
					/ mImageRes.length % 2][position % mImageRes.length], 0);
		}
		switch (position) {
		case 0:
			
			break;
		case 1:

			break;
		case 2:

			break;

		default:
			break;
		}

		return mImageViews[position / mImageRes.length % 2][position
				% mImageRes.length];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mImageRes.length == 1) {
			((ViewPager) container).removeView(mImageViews[position
					/ mImageRes.length % 2][0]);
		} else {
			((ViewPager) container).removeView(mImageViews[position
					/ mImageRes.length % 2][position % mImageRes.length]);
		}
	}
}
