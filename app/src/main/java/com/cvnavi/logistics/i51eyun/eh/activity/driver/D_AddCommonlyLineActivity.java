package com.cvnavi.logistics.i51eyun.eh.activity.driver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.commonlyline.AddCommonlyLineRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.ToggleButton.ToggleButton;
import com.cvnavi.logistics.i51eyun.eh.widget.ToggleButton.ToggleButton.OnToggleChanged;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

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
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_add_commonly_line)
public class D_AddCommonlyLineActivity extends BaseActivity implements RegionDataCallBack2 {

	@ViewInject(value = R.id.start_address_tv)
	private TextView start_address_tv;
	@ViewInject(value = R.id.end_address_tv)
	private TextView end_address_tv;

	@ViewInject(value = R.id.once_layout)
	private LinearLayout once_layout;
	@ViewInject(value = R.id.once_iv)
	private TextView once_iv;

	@ViewInject(value = R.id.go_and_back_layout)
	private LinearLayout go_and_back_layout;
	@ViewInject(value = R.id.go_and_back_iv)
	private TextView go_and_back_iv;

	@ViewInject(value = R.id.save_btn)
	private Button save_btn;
	
	@ViewInject(R.id.TB_default)
	private ToggleButton TB_default;

	private Context context;
	private AlertDialog.Builder alertDialog;

	private boolean isCheck_StartAddress = false;
	private boolean isCheck_EndAddress = false;
	private RegionBean startRegionBean;
	private RegionBean endRegionBean;

	private boolean isCheckOnce = true;
	private boolean isCheckGoAndBack = false;
	private Boolean isDefault=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		this.titlt_textView.setText("添加线路");
		init();
		CallBackManager.getInstance().getRegionCallBackManager2().addCallBack(this);
	}

	/**
	 * 
	 */
	private void init() {
		TB_default.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if(on){
					isDefault = true;
				}else{
					isDefault = false;
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		CallBackManager.getInstance().getRegionCallBackManager2().removeCallBack(this);
		super.onDestroy();
	}

	@Event(value = { R.id.back_linearLayout, R.id.start_address_tv, R.id.end_address_tv, R.id.once_layout,
			R.id.go_and_back_layout, R.id.save_btn }, type = View.OnClickListener.class)
	private void onViewClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.start_address_tv:
			isCheck_StartAddress = true;
			isCheck_EndAddress = false;

			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key, Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.end_address_tv:
			isCheck_StartAddress = false;
			isCheck_EndAddress = true;

			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.Province_City_Key, Constants.Province_City_Key);
			startActivity(intent);
			break;
		case R.id.once_layout:
			isCheckGoAndBack = false;
			go_and_back_iv.setBackgroundResource(R.drawable.pretermit);

			if (isCheckOnce) {
//				isCheckOnce = false;
//				once_iv.setBackgroundResource(R.drawable.seckgey);
			} else {
				isCheckOnce = true;
				once_iv.setBackgroundResource(R.drawable.select);
			}
			break;
		case R.id.go_and_back_layout:
			isCheckOnce = false;
			once_iv.setBackgroundResource(R.drawable.pretermit);

			if (isCheckGoAndBack) {
//				isCheckGoAndBack = false;
//				go_and_back_iv.setBackgroundResource(R.drawable.seckgey);
			} else {
				isCheckGoAndBack = true;
				go_and_back_iv.setBackgroundResource(R.drawable.select);
			}
			break;
		case R.id.save_btn:
			// if (TextUtils.isEmpty(start_address_tv.getText())) {
			// Toast.makeText(this, "请选择始发地！", Toast.LENGTH_SHORT).show();
			// return;
			// }

			if (startRegionBean == null || startRegionBean.provinceBean == null || startRegionBean.cityBean == null) {
				Toast.makeText(this, "请选择始发地省市！", Toast.LENGTH_SHORT).show();
				return;
			}

			// if (TextUtils.isEmpty(end_address_tv.getText())) {
			// Toast.makeText(this, "请选择目的地！", Toast.LENGTH_SHORT).show();
			// return;
			// }

			if (endRegionBean == null || endRegionBean.provinceBean == null || endRegionBean.cityBean == null) {
				Toast.makeText(this, "请选择目的地省市！", Toast.LENGTH_SHORT).show();
				return;
			}

			if (isCheckOnce == false && isCheckGoAndBack == false) {
				Toast.makeText(this, "请选择行程！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			saveData();
			break;
		default:
			break;
		}
	}

	private void saveData() {
		new Thread(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Create_Line_Url);
				try {
					AddCommonlyLineRequestBean requestBean = new AddCommonlyLineRequestBean();
					requestBean.Provenance_Provice = startRegionBean.provinceBean.PName;
					requestBean.Provenance_City = startRegionBean.cityBean.CName;
					requestBean.Destination_Provice = endRegionBean.provinceBean.PName;
					requestBean.Destination_City = endRegionBean.cityBean.CName;
					requestBean.User_Key=MyApplication.getInstatnce().getUserKey();
					if (isCheckOnce) {
						requestBean.RouteType = "0";
					}
					if (isCheckGoAndBack) {
						requestBean.RouteType = "1";
					}
					if(isDefault){
						requestBean.IsDefault="1";
					}else{
						requestBean.IsDefault="0";
					}

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(requestBean));

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				x.http().post(params, new Callback.CommonCallback<String>() {
					public void onCancelled(CancelledException arg0) {
						LogUtil.d("-->>请求取消");
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						LogUtil.d("-->>请求错误:" + ex.getMessage());

						Message msg = Message.obtain();
						msg.what = Constants.Request_Fail;
						msg.obj=Constants.errorMsg;
						myHandler.sendMessage(msg);
					}

					@Override
					public void onFinished() {
						LogUtil.d("-->>请求结束");
					}

					@Override
					public void onSuccess(String str) {
						LogUtil.d("-->>请求成功:" + str);
						try {
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								myHandler.sendEmptyMessage(Constants.Request_Success);
							} else {
								Message msg = Message.obtain();
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.Request_Success:
				Toast.makeText(context, "恭喜您！添加线路成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_AddCommonlyLineActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				alertDialog.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onRegionData(Object object) {
		if (object != null) {
			if (isCheck_StartAddress) {
				startRegionBean = (RegionBean) object;
				start_address_tv.setText(ContextUtil.getRegion(startRegionBean));
			}

			if (isCheck_EndAddress) {
				endRegionBean = (RegionBean) object;
				end_address_tv.setText(ContextUtil.getRegion(endRegionBean));
			}
		}
	}

}
