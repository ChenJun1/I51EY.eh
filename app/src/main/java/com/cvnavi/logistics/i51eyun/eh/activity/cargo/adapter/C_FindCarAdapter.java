/**
 * Administrator2016-4-26
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MDict_CarCode_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.CollectDriverBean;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:03:03
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
@SuppressLint("UseSparseArrays")
public class C_FindCarAdapter extends BaseAdapter {
	private List<MDict_CarCode_Sheet> dataList;
	private Context mContext;
	private LayoutInflater mInflater;
	private Map<Integer, View> conViewMap;

	public C_FindCarAdapter(List<MDict_CarCode_Sheet> dataList, Context mContext) {
		super();
		this.dataList = dataList;
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		conViewMap = new HashMap<Integer, View>();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MDict_CarCode_Sheet beanCar=(MDict_CarCode_Sheet) getItem(position);
		if (conViewMap.get(position) == null) {
			convertView = mInflater.inflate(R.layout.c_activity_findcar_item,
					parent, false);
			conViewMap.put(position, convertView);
		} else {
			convertView = conViewMap.get(position);
		}
		TextView c_car_code = ViewHolder.get(convertView, R.id.c_car_code);
		TextView c_car_type = ViewHolder.get(convertView, R.id.c_car_type);
		TextView c_car_size = ViewHolder.get(convertView, R.id.c_car_size);
		TextView c_city = ViewHolder.get(convertView, R.id.c_city);

		TextView c_car_tel = ViewHolder.get(convertView, R.id.c_car_tel);
		TextView c_driver_Name = ViewHolder
				.get(convertView, R.id.c_driver_Name);
		ImageView c_car_status = ViewHolder.get(convertView, R.id.c_car_status);

		TextView c_car_tel_f = ViewHolder.get(convertView, R.id.c_car_tel_f);
		TextView c_driver_Name_f = ViewHolder.get(convertView,
				R.id.c_driver_Name_f);
		ImageView c_car_status_f = ViewHolder.get(convertView,
				R.id.c_car_status_f);

		if (beanCar.Line != null && beanCar.Line.size() > 0) {
			if(TextUtils.isEmpty(beanCar.Line.get(0).Provenance_City)||TextUtils.isEmpty(beanCar.Line.get(0).Destination_City)){
				c_city.setText("不限");
			}else{
				c_city.setText(beanCar.Line.get(0).Provenance_City+ "-"
						+ beanCar.Line.get(0).Destination_City);
			}
		}

		c_car_code.setText(beanCar.CarCode);
		c_car_type.setText(beanCar.CarType);
		c_car_size.setText(Double.toString((beanCar.Size))+"m");

		if (beanCar.Driver != null && beanCar.Driver.size() > 0) {
			List<CollectDriverBean> driver = beanCar.Driver;

			c_car_tel.setText(driver.get(0).User_Tel);
			c_driver_Name.setText(driver.get(0).User_Name);
			if (driver.get(0).IsWork.equals("0")) {
				c_car_status.setImageResource(R.drawable.duty);
			} else {
				c_car_status.setImageResource(R.drawable.work);
			}

			if (driver.size() > 1) {

				c_car_tel_f.setText(driver.get(1).User_Tel);
				c_driver_Name_f.setText(driver.get(1).User_Name);
				if (driver.get(1).IsWork.equals("0")) {
					c_car_status_f.setImageResource(R.drawable.duty);
				} else {
					c_car_status_f.setImageResource(R.drawable.work);
				}
			}
		}
		return convertView;
	}

}
