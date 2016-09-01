package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * @date 2016-5-17 下午1:04:19
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_MyGridAdapter extends BaseAdapter {
	private Context mContext;
	private int mOrderRow;
	public String[] img_text = null;
	public int[] imgs = null;

	public C_MyGridAdapter(Context mContext,String[] img_text,int[] imgs,int OrderRow) {
		super();
		this.mContext = mContext;
		this.img_text=img_text;
		this.mOrderRow=OrderRow;
		this.imgs=imgs;
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
		}
		TextView tv = ViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = ViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		if(mOrderRow>0){
		if(position==2){
			LinearLayout img_ll= ViewHolder.get(convertView, R.id.img_ll);
			BadgeView badgeView = new BadgeView(mContext);
			badgeView.setTargetView(img_ll);  
			badgeView.setBadgeCount(mOrderRow); 
		}
		}
		return convertView;
	}

}
