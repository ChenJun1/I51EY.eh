/**
 * Administrator2016-5-19
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MDict_FavoriteContacts_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.RegionBean;
import com.cvnavi.logistics.i51eyun.eh.activity.callback.RegionDataCallBack2;
import com.cvnavi.logistics.i51eyun.eh.activity.region.ProvinceActivity;
import com.cvnavi.logistics.i51eyun.eh.manager.callback.CallBackManager;
import com.cvnavi.logistics.i51eyun.eh.utils.ContextUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 下午5:12:51
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@ContentView(R.layout.c_add_common_consignee)
public class C_AddCommonConsigneeActivity extends BaseActivity implements
		RegionDataCallBack2 {

	@ViewInject(R.id.c_common_name)
	private TextView c_common_name;

	@ViewInject(R.id.c_common_tel)
	private TextView c_common_tel;

	@ViewInject(R.id.c_pca_et)
	private TextView c_pca_et;
	
	private AlertDialog.Builder alertDialog;

	@ViewInject(R.id.c_common_addr)
	private TextView c_common_addr;

	@ViewInject(R.id.c_submit_btn)
	private Button c_submit_btn;

	private CommonWaitDialog waitDialog;

	private MDict_FavoriteContacts_Sheet orderCommon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderCommon = new MDict_FavoriteContacts_Sheet();
		CallBackManager.getInstance().getRegionCallBackManager2()
				.addCallBack(this);

		initView();
	}

	private void initView() {
		this.titlt_textView.setText("新增常用联系人");
		waitDialog = new CommonWaitDialog(this, R.style.progress_dialog);
	}

	@Event(value = { R.id.back_linearLayout, R.id.c_submit_btn, R.id.c_pca_et, }, type = View.OnClickListener.class)
	private void OnClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.c_pca_et:
			intent = new Intent(this, ProvinceActivity.class);
			intent.putExtra(Constants.ISPCA, Constants.ISPCA);
			startActivity(intent);
			break;
		case R.id.c_submit_btn:
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			
			if (checkText()) {
				addCommon();
				if (orderCommon != null) {
					waitDialog.show();
					addOrderCommonRequest(Constants.Add_FavoriteContacts_URL);
				}
			}

			break;

		default:
			break;
		}
	}

	private Handler myHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			waitDialog.dismiss();
			switch (msg.what) {
			case Constants.Request_Success:
				Toast.makeText(C_AddCommonConsigneeActivity.this, "添加成功！",
						Toast.LENGTH_SHORT).show();
				C_AddCommonConsigneeActivity.this.finish();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(C_AddCommonConsigneeActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
				alertDialog.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
				break;
			default:
				break;
			}

		}
	};

	private void addOrderCommonRequest(final String Url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Url);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid()); // MyApplication.getInstatnce().getUserTypeOid()
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderCommon));

					params.addBodyParameter(null,
							JsonUtils.toJsonData(requestData));
				} catch (Exception e) {
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
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message message = Message.obtain();
							if (resultBase.isSuccess()) {
								message.what = Constants.Request_Success;
								myHandler.sendMessage(message);
							} else {
								message.what = Constants.Request_Fail;
								message.obj=resultBase.getErrorText();
								myHandler.sendMessage(message);
								
								LogUtil.e("-->>" + resultBase.getErrorText());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private void addCommon() {
		orderCommon.Contacts_Name = c_common_name.getText().toString();
		orderCommon.Contacts_Tel = c_common_tel.getText().toString();
		orderCommon.Contacts_Address = TextUtils.isEmpty(c_common_addr.getText())?"":c_common_addr.getText().toString();
		orderCommon.BLat = "0";
		orderCommon.BLng = "0";
	}

	/**
	 * 
	 */
	private boolean checkText() {
		if (TextUtils.isEmpty(c_common_name.getText())) {
			Toast.makeText(this, "请填写姓名！", Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(c_common_tel.getText())) {
			Toast.makeText(this, "请填写电话！", Toast.LENGTH_SHORT).show();
			return false;
		}else if(!ContextUtil.isMobileNO(c_common_tel.getText().toString())){
			Toast.makeText(this, "请填写正确电话！", Toast.LENGTH_SHORT).show();
			return false;
		}
//		else if (TextUtils.isEmpty(c_common_addr.getText())) {
//			Toast.makeText(this, "请填写详细地址！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
		else if (TextUtils.isEmpty(c_pca_et.getText())) {
			Toast.makeText(this, "请填写地址！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void onRegionData(Object object) {
		RegionBean regionBean = (RegionBean) object;
		if (regionBean != null) {
			StringBuffer sb = new StringBuffer();

			if (regionBean.provinceBean != null) {
				sb.append(regionBean.provinceBean.PName);
				orderCommon.Contacts_Provice = regionBean.provinceBean.PName;
			}
			if (regionBean.cityBean != null) {
				sb.append(regionBean.cityBean.CName);
				orderCommon.Contacts_City = regionBean.cityBean.CName;
			}
			if (regionBean.areaBean != null) {
				sb.append(regionBean.areaBean.AName);
				orderCommon.Contacts_Area = regionBean.areaBean.AName;
			}
			c_pca_et.setText(sb.toString());

		}
	}
}
