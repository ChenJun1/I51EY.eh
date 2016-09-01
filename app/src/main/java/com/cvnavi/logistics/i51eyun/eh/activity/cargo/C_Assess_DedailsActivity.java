/**
 * Administrator2016-5-19
 */
package com.cvnavi.logistics.i51eyun.eh.activity.cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationListInfo;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

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
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-19 下午5:32:53
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

@SuppressLint("HandlerLeak")
@ContentView(R.layout.c_assess_details)
public class C_Assess_DedailsActivity extends BaseActivity {

	@ViewInject(R.id.myevaluate_thzs_rb)
	private RatingBar myevaluate_thzs_rb;
	@ViewInject(R.id.myevaluate_fwwz_rb)
	private RatingBar myevaluate_fwwz_rb;
	@ViewInject(R.id.myevaluate_fwtd_rb)
	private RatingBar myevaluate_fwtd_rb;
	
	@ViewInject(R.id.myevaluate_thzs_tv)
	private TextView myevaluate_thzs_tv;
	@ViewInject(R.id.myevaluate_fwwz_tv)
	private TextView myevaluate_fwwz_tv;
	@ViewInject(R.id.myevaluate_fwtd_tv)
	private TextView myevaluate_fwtd_tv;
	
	@ViewInject(R.id.myevaluate_remark)
	private TextView myevaluate_remark;
	
	
	
	@ViewInject(R.id.evaluatemy_xxzq_rb)
	private RatingBar evaluatemy_xxzq_rb;
	@ViewInject(R.id.evaluatemy_thfb_rb)
	private RatingBar evaluatemy_thfb_rb;
	@ViewInject(R.id.evaluatemy_fwtd_rb)
	private RatingBar evaluatemy_fwtd_rb;
	
	@ViewInject(R.id.evaluatemy_xxzq_tv)
	private TextView evaluatemy_xxzq_tv;
	@ViewInject(R.id.evaluatemy_thfb_tv)
	private TextView evaluatemy_thfb_tv;
	@ViewInject(R.id.evaluatemy_fwtd_tv)
	private TextView evaluatemy_fwtd_tv;
	
	@ViewInject(R.id.evaluatemy_remark)
	private TextView evaluatemy_remark;
	
	private Context context;
	private AlertDialog.Builder alertDialog;
	private CommonWaitDialog waitDialog;
	
	private OrderEvaluationListInfo orderEvaluationListInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.titlt_textView.setText("评分信息");
		context = this;
		orderEvaluationListInfo = (OrderEvaluationListInfo)getIntent().getSerializableExtra(Constants.AssessKey);
		
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);
		waitDialog.show();
		loadData();
	}

	@Event(value = { R.id.back_linearLayout }, type = View.OnClickListener.class)
	private void OnClick(View view) {
		switch (view.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
	}

	private void loadData() {
		new Thread(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Get_OrderEvaluationInfo_URL);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(orderEvaluationListInfo.Order_Key);

					params.addBodyParameter(null, JsonUtils.toJsonData(requestData));
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
						msg.obj =Constants.errorMsg;
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
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								OrderEvaluationInfo bean = JsonUtils.parseData2(String.valueOf(resultBase.getDataValue()), OrderEvaluationInfo.class);

								msg.what = Constants.Request_Success;
								msg.obj = bean;
								myHandler.sendMessage(msg);
							} else {
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
			if (waitDialog != null) {
				waitDialog.dismiss();
			}
			
			switch (msg.what) {
			case Constants.Request_Success:
				if (msg.obj != null) {
					setData((OrderEvaluationInfo)msg.obj);
				}
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(context);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj== null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
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
	
	private void setData(OrderEvaluationInfo bean){
		SetViewValueUtil.setRatingBar(myevaluate_thzs_rb, bean.HZPJ_THZS);
		SetViewValueUtil.setRatingBar(myevaluate_fwwz_rb, bean.HZPJ_HWWZ);
		SetViewValueUtil.setRatingBar(myevaluate_fwtd_rb, bean.HZPJ_HWTD);
		
		SetViewValueUtil.setTextViewValue(myevaluate_thzs_tv, bean.HZPJ_THZS);
		SetViewValueUtil.setTextViewValue(myevaluate_fwwz_tv, bean.HZPJ_HWWZ);
		SetViewValueUtil.setTextViewValue(myevaluate_fwtd_tv, bean.HZPJ_HWTD);
		
		SetViewValueUtil.setTextViewValue(myevaluate_remark, bean.HZPJ_BZ);
		
		
		SetViewValueUtil.setRatingBar(evaluatemy_xxzq_rb, bean.SJPJ_XXZS);
		SetViewValueUtil.setRatingBar(evaluatemy_thfb_rb, bean.SJPJ_THFB);
		SetViewValueUtil.setRatingBar(evaluatemy_fwtd_rb, bean.SJPJ_HWTD);
		
		SetViewValueUtil.setTextViewValue(evaluatemy_xxzq_tv, bean.SJPJ_XXZS);
		SetViewValueUtil.setTextViewValue(evaluatemy_thfb_tv, bean.SJPJ_THFB);
		SetViewValueUtil.setTextViewValue(evaluatemy_fwtd_tv, bean.SJPJ_HWTD);
		
		SetViewValueUtil.setTextViewValue(evaluatemy_remark, bean.SJPJ_BZ);
		
		
	}
	
}
