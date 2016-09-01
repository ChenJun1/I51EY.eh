package com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.jauker.widget.BadgeView;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:10:29
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class D_MyGridAdapter extends BaseAdapter {
	private Context mContext;

	private String[] img_text = null;
	private int[] imgs = null;
	private int isReadOrderRow;

	public D_MyGridAdapter(Context mContext, String[] str, int[] imgs) {
		super();
		this.mContext = mContext;
		this.img_text = str;
		this.imgs = imgs;
	}

	public void setIsReadOrderRow(int isReadOrderRow) {
		this.isReadOrderRow = isReadOrderRow;
	}

	@Override
	public int getCount() {
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item1, parent, false);
		}
		TextView tv = ViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = ViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		if (isReadOrderRow > 0) {
			if (position == 2) {
//				LinearLayout view = ViewHolder.get(convertView, R.id.img_ll);
				ImageView view = ViewHolder.get(convertView, R.id.iv_item);
				BadgeView badgeView = new BadgeView(mContext);
				badgeView.setTargetView(view);
				badgeView.setBadgeCount(isReadOrderRow);
			}
		}

		return convertView;
	}

}
