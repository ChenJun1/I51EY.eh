package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:03:37
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_FragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentsList;

	/**
	 * 这个构造方法把ArrayList<Fragment>传过来
	 * 
	 * @param fm
	 * @param fragments
	 */
	public C_FragmentPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragmentsList = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentsList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentsList == null ? 0 : fragmentsList.size();
	}

}
