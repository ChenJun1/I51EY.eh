/**
 * Administrator2016-4-25
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo.adapter;

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

import java.util.List;

/**
 * 
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-17 下午1:02:24
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class C_CarManageAdapter extends BaseAdapter {
	private List<MDict_CarCode_Sheet> carList;
	private Context mContext;
	private LayoutInflater mlInflater;

	public C_CarManageAdapter(List<MDict_CarCode_Sheet> carList,
			Context mContext) {
		super();
		this.carList = carList;
		this.mContext = mContext;
		this.mlInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return carList.size();
	}

	@Override
	public Object getItem(int position) {

		return carList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int positiion, View convertView, ViewGroup parent) {
		MDict_CarCode_Sheet mCollect = carList.get(positiion);
		if (convertView == null) {
			convertView = mlInflater.inflate(R.layout.c_car_manage_item,
					parent, false);
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

		if (mCollect.Line != null && mCollect.Line.size() > 0) {
			if(TextUtils.isEmpty(mCollect.Line.get(0).Provenance_City)||TextUtils.isEmpty(mCollect.Line.get(0).Destination_City)){
				c_city.setText("不限");
			}else{
				c_city.setText(mCollect.Line.get(0).Provenance_City  + "-"
						+mCollect.Line.get(0).Destination_City);
			}
			
		}

		c_car_code.setText(mCollect.CarCode);
		c_car_type.setText(mCollect.CarType);
		c_car_size.setText(Double.toString((mCollect.Size)));

		if (mCollect.Driver != null && mCollect.Driver.size() > 0) {
			List<CollectDriverBean> driver = mCollect.Driver;

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
