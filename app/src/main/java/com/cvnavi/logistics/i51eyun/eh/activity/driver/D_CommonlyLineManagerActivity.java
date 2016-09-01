package com.cvnavi.logistics.i51eyun.eh.activity.driver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.commonlyline.MDict_Line_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_CommonlyLineManagerAdapter;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

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
import java.util.ArrayList;
import java.util.List;

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
@ContentView(R.layout.d_commonly_line_manager)
public class D_CommonlyLineManagerActivity extends BaseActivity {

	@ViewInject(R.id.operation_btn)
	private Button operationBtn;

	@ViewInject(R.id.listview)
	private ListView listView;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private D_CommonlyLineManagerAdapter adapter;
	private List<MDict_Line_Sheet> dataList;
	private Context context;

	private CommonWaitDialog waitDialog;
	private AlertDialog.Builder alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		this.titlt_textView.setText("常用线路管理");
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);

		operationBtn.setText("+");
		 operationBtn.setVisibility(View.VISIBLE);
		operationBtn.setTextSize(30);

		dataList = new ArrayList<>();
		adapter = new D_CommonlyLineManagerAdapter(context, R.layout.d_commonly_line_manager_itme, dataList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(MyOnItemClickListener);
		listView.setEmptyView(empty_list_view);
	}
	OnItemClickListener MyOnItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("提示");
			builder.setMessage("确定设为首选路线?");
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				}
			});
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					MDict_Line_Sheet bean = dataList.get(position);
					if (bean != null) {
						waitDialog.show();
						UpdateCommonlyLineData(bean);
					}
				}
			});
			builder.create().show();
			return;
		}
	
	};

	@Override
	protected void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(this)){
			return;
		}
		waitDialog.show();
		loadCommonlyLineData();
	}

	@Event(value = { R.id.back_linearLayout, R.id.operation_btn }, type = View.OnClickListener.class)
	private void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.operation_btn:
			showActivity((Activity) context, D_AddCommonlyLineActivity.class);
			break;
		default:
			break;
		}
	}

	@Event(value = R.id.listview, type = AdapterView.OnItemLongClickListener.class)
	private boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("确认删除线路?");
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				MDict_Line_Sheet bean = dataList.get(position);
				if (bean != null) {
					waitDialog.show();
					deleteCommonlyLineData(bean);
				}
			}
		});
		builder.create().show();
		return true;
	}

	private void loadCommonlyLineData() {
		new Thread(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Get_LineList_Url);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
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
						msg.obj = Constants.errorMsg;

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
								List<MDict_Line_Sheet> list = JsonUtils
										.parseCommonlyLineList(String.valueOf(resultBase.getDataValue()));

								Message msg = Message.obtain();
								msg.what = Constants.Request_Success;
								msg.obj = list;

								myHandler.sendMessage(msg);
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

	private void deleteCommonlyLineData(final MDict_Line_Sheet bean) {
		new Thread(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Delete_Line_Url);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(bean.Line_Key);

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
								Message msg = Message.obtain();
								msg.what = Constants.Delete_Success;

								myHandler.sendMessage(msg);
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
	private void UpdateCommonlyLineData(final MDict_Line_Sheet bean) {
		new Thread(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams(Constants.Update_Line_Url);
				try {
					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(bean.Line_Key);
					
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
								Message msg = Message.obtain();
								msg.what = Constants.Update_Success;
								
								myHandler.sendMessage(msg);
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
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_Success:
				if (msg.obj != null) {
					dataList.clear();
					dataList.addAll((List<MDict_Line_Sheet>) msg.obj);
					if (dataList.size() >2) { // 当前线路有3条时，不显示添加按钮。
						operationBtn.setVisibility(View.GONE);
					} else {
						operationBtn.setVisibility(View.VISIBLE);
					}
					adapter.notifyDataSetChanged();
				}
				break;
			case Constants.Delete_Success:
				Toast.makeText(context, "删除成功！", Toast.LENGTH_LONG).show();
				loadCommonlyLineData();
				break;
			case Constants.Update_Success:
				Toast.makeText(context, "设置成功！", Toast.LENGTH_LONG).show();
				loadCommonlyLineData();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(D_CommonlyLineManagerActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String.valueOf(msg.obj));
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

}
