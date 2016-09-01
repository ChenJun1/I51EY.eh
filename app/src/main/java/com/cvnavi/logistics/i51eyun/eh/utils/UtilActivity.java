package com.cvnavi.logistics.i51eyun.eh.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 解决ScrollView和ListView的冲突
 *          在setadapter()后调用 添加两个方法解决listView展开冲突
 * @date 2016-5-17 下午1:27:02
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class UtilActivity {
	public int cont = 0;
	public int first = 0;
	public int last = 0;

	/**
	 * listView和scrollView适应高度
	 * 
	 * @param listView
	 * @param itemHeight
	 */
	public void setListViewHeightBasedOnChildren(ListView listView,
			int itemHeight) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			// 当只有一条数据的时候
			if (i == 0) {
				first = listItem.getMeasuredHeight();
			}
			totalHeight += itemHeight;
			/*
			 * Log.e("UtilActivity=======", listItem.getMeasuredHeight() - 291 +
			 * "===============");
			 */
			Log.i("UtilActivity", listItem.getMeasuredHeight()
					+ "===============" + totalHeight);
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	// 加布局
	public void setListViewHeightAdd(ListView listView, int itemHeight) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			Log.i("setListViewHeightAdd", listItem.getMeasuredHeight()
					+ "===============");
		}
		if (listAdapter.getCount() > 1) {
			totalHeight += itemHeight * cont;
		} else {
			totalHeight += first - last;
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	// 减布局
	public void setListViewHeightJian(ListView listView, int itemHeight) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			if (i == 0) {
				last = listItem.getMeasuredHeight();
			}
		}

		totalHeight += itemHeight * cont;

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	// 获取隐藏的视图在不同设备上的高度
	public int getListIetmHeight(ListView listView) {
		int height = 0;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter.getCount() > 1) {
			View listItem1 = listAdapter.getView(0, null, listView);
			listItem1.measure(0, 0);
			int i1 = listItem1.getMeasuredHeight();
			View listItem2 = listAdapter.getView(1, null, listView);
			listItem2.measure(0, 0);
			int i2 = listItem2.getMeasuredHeight();

			height = i1 - i2;
		}
		return height;
	}

}
