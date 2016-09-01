package com.cvnavi.logistics.i51eyun.eh.activity.driver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.ConfirmTakeOrCompleteOrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.viewholder.ViewHolder;
import com.cvnavi.logistics.i51eyun.eh.utils.BitmapUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/** 
 * @author  作者：fanxb 
 * @version 创建时间：2016-5-25 下午8:13:38 
 * 类说明 
 */

/**
 * 交货信息录入。
 * 
 * @author JohnnyYuan
 *
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_delivery_goods)
public class D_Delivery_GoodsActivity extends BaseActivity {

	@ViewInject(R.id.d_delivery_goods_edit)
	private EditText d_delivery_goods_edit;
	@ViewInject(R.id.d_delivery_goods_imageview)
	private ImageView d_delivery_goods_imageview;
	@ViewInject(R.id.d_delivery_goods_imag_text)
	private TextView d_delivery_goods_imag_text;

	@ViewInject(R.id.d_delivery_goods_btn)
	private Button d_delivery_goods_btn;

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;

	private Context context;
	private AlertDialog.Builder alertDialog;
	private CommonWaitDialog waitDialog;

	private Bitmap JHPicture=null;
	private BitmapFactory.Options options = new BitmapFactory.Options();
	private static String picFileFullName;

	private PopupWindow assesPopupWindow;
	private View assesPopupWindowContentView;
	private RatingBar ratingBar1;
	private RatingBar ratingBar2;
	private RatingBar ratingBar3;
	private TextView fen1;
	private TextView fen2;
	private TextView fen3;
	private EditText assesRemark;
	private Button assesSubmit;
	private OrderEvaluationInfo orderEvaluationInfo;

	private WaitingOrderBaseInfo orderInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		x.image().clearMemCache();
		x.image().clearCacheFiles();

		context = this;
		this.titlt_textView.setText("交货信息录入");
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);

		orderEvaluationInfo = new OrderEvaluationInfo();
		orderEvaluationInfo.SJPJ_XXZS = "5";
		orderEvaluationInfo.SJPJ_THFB = "5";
		orderEvaluationInfo.SJPJ_HWTD = "5";

		if (getIntent().getSerializableExtra(Constants.OrderInfo) != null) {
			orderInfo = (WaitingOrderBaseInfo) getIntent().getSerializableExtra(Constants.OrderInfo);
			d_delivery_goods_edit.setText(orderInfo.Consignee_Name);
		}
	}

	@Event(value = { R.id.back_linearLayout, R.id.d_delivery_goods_imag_text,
			R.id.d_delivery_goods_btn }, type = View.OnClickListener.class)
	private void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.d_delivery_goods_imag_text:
			showImageView();
			break;
		case R.id.d_delivery_goods_btn:
			if(!NetWorkUtils.isNetWort(this)){
				return;
			}
			if (verifyData() == false) {
				return;
			}

			// 未开启评价功能的，直接交货。
			if (TextUtils.isEmpty(MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate())
					|| MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate().equals("0")) {
				requestDeliveryOrder(orderInfo);
			} else if (MyApplication.getInstatnce().getBasicDataBuffer().getISEvaluate().equals("1")) {
				// 评价成功的，不能重复评价。直接交货。
				if (TextUtils.isEmpty(orderInfo.IsDESuccess) || orderInfo.IsDESuccess.equals("0")) {
					assesPopuWindow(orderInfo);
				} else if (orderInfo.IsDESuccess.equals("1")) {
					requestDeliveryOrder(orderInfo);
				}
			}
			break;
		default:
			break;
		}
	}

	private boolean verifyData() {
		if (TextUtils.isEmpty(d_delivery_goods_edit.getText().toString())) {
			Toast.makeText(context, "请输入实际收货人！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (JHPicture == null) {
			Toast.makeText(context, "请上传交货照片！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void showImageView() {
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("上传照片");
		alertDialog.setItems(new String[] { "相机拍照", "本地照片" }, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				switch (which) {
				case 0:
					photograph(); // 相机拍照。
					break;
				case 1:
					nativePicture(); // 本地照片
					break;
				default:
					break;
				}
			}
		}).setNegativeButton("取消", null);
		alertDialog.show();
	}

	private String getDrivingPicBase64() {
		Bitmap bitmap = null;
		if (JHPicture != null) {
			bitmap = BitmapUtil.comp(JHPicture);
		}
		String drivingPicBase64 = BitmapUtil.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 30);
		return drivingPicBase64;
	}

	// 相机拍照
	public void photograph() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}

			File outFile = new File(outDir, System.currentTimeMillis()+"driver_license.png");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			Toast.makeText(context, "请确认是否插入SD卡！", Toast.LENGTH_SHORT).show();
		}
	}

	// 打开本地相册
	private void nativePicture() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择照片"), PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				setImageView(picFileFullName);
			} else if (resultCode == RESULT_CANCELED) { // 用户取消了图像捕获
			} else { // 图像捕获失败，提示用户
			}
		} else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if (data == null) {
					return;
				}

				showNativeImage(data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("deprecation")
	private void showNativeImage(Intent data) {
		ContentResolver resolver = getContentResolver();

		Uri pictureUri = data.getData(); // 获得图片的uri
		try {
			JHPicture = MediaStore.Images.Media.getBitmap(resolver, pictureUri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显得到bitmap图片
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(pictureUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		
		ImageOptions imageOptions = new ImageOptions.Builder()
        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
        .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
        .build();
		x.image().bind(d_delivery_goods_imageview, path,imageOptions);
		
		//x.image().bind(d_delivery_goods_imageview, path);
	}

	// /**
	// * 读取照片exif信息中的旋转角度<br/>
	// * http://www.eoeandroid.com/thread-196978-1-1.html
	// *
	// * @param path
	// * 照片路径
	// * @return角度
	// */
	// private static int readPictureDegree(String path) {
	// int degree = 0;
	// try {
	// ExifInterface exifInterface = new ExifInterface(path);
	// int orientation =
	// exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	// ExifInterface.ORIENTATION_NORMAL);
	// switch (orientation) {
	// case ExifInterface.ORIENTATION_ROTATE_90:
	// degree = 90;
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_180:
	// degree = 180;
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_270:
	// degree = 270;
	// break;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return degree;
	// }

	private void setImageView(String realPath) {
		options.inSampleSize = 2;
		try {

			JHPicture = BitmapFactory.decodeFile(realPath);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		ImageOptions imageOptions = new ImageOptions.Builder()
				.setRadius(DensityUtil.dip2px(5))// ImageView圆角半径
				.setImageScaleType(ImageView.ScaleType.FIT_CENTER).build();
		x.image().bind(d_delivery_goods_imageview, realPath, imageOptions);
	}

	// 交货。
	private void requestDeliveryOrder(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.Confirm_TakeOrCompleteOrderByDriver_URL);
				try {
					String JHPictureStrBase64 = getDrivingPicBase64();

					ConfirmTakeOrCompleteOrderRequestBean requestBean = new ConfirmTakeOrCompleteOrderRequestBean();
					requestBean.Order_Key = orderInfo.Order_Key;
					requestBean.Serial_Oid = orderInfo.Serial_Oid;
					requestBean.Consignee_City=orderInfo.Consignee_City;
					requestBean.Shipping_City=orderInfo.Shipping_City;
					requestBean.PhotoStatus = "AF";
					requestBean.Actual_Consignee = d_delivery_goods_edit.getText().toString();
					requestBean.JH_Photo = JHPictureStrBase64;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setDataValue(JsonUtils.toJsonData(requestBean));

					String jsonStr = JsonUtils.toJsonData(requestData);
					params.addBodyParameter(null, jsonStr);
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
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								// 发送消息通知。
								msg.what = Constants.Request_OrderCome;
								myHandler.sendMessage(msg);

							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	private void assesPopuWindow(final WaitingOrderBaseInfo orderInfo) {
		assesPopupWindow = null;
		if (assesPopupWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(context);
			assesPopupWindowContentView = mLayoutInflater.inflate(R.layout.d_assess_popup, null);
			assesPopupWindow = new PopupWindow(assesPopupWindowContentView, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		ratingBar1 = ViewHolder.get(assesPopupWindowContentView, R.id.c_RatingBar1);
		ratingBar2 = ViewHolder.get(assesPopupWindowContentView, R.id.c_RatingBar2);
		ratingBar3 = ViewHolder.get(assesPopupWindowContentView, R.id.c_RatingBar3);

		fen1 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen1);
		fen2 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen2);
		fen3 = ViewHolder.get(assesPopupWindowContentView, R.id.c_fen3);
		assesRemark = ViewHolder.get(assesPopupWindowContentView, R.id.c_remark);
		assesSubmit = ViewHolder.get(assesPopupWindowContentView, R.id.c_submit);

		fen1.setText(String.valueOf((int) ratingBar1.getRating()));
		ratingBar1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				fen1.setText(String.valueOf((int) rating));
				orderEvaluationInfo.SJPJ_XXZS = String.valueOf((int) ratingBar.getRating());
			}
		});

		fen2.setText(String.valueOf((int) ratingBar2.getRating()));
		ratingBar2.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				fen2.setText(String.valueOf((int) rating));
				orderEvaluationInfo.SJPJ_THFB = String.valueOf((int) ratingBar.getRating());
			}
		});

		fen3.setText(String.valueOf((int) ratingBar3.getRating()));
		ratingBar3.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				fen3.setText(String.valueOf((int) rating));
				orderEvaluationInfo.SJPJ_HWTD = String.valueOf((int) ratingBar.getRating());
			}
		});
		assesRemark.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence strValue, int start, int before, int count) {
				orderEvaluationInfo.SJPJ_BZ = strValue.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// if (s != null && !s.equals("")) {
				// if (s.length() > 30) {
				// return;
				// }
				// orderEvaluationInfo.SJPJ_BZ = s.toString();
				// }
			}
		});
		assesSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkEvaluation()) {
					waitDialog.show();
					requestOrderEvaluation(orderInfo);
					requestDeliveryOrder(orderInfo);
				}
			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		assesPopupWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.alpha = 0.4f;
		this.getWindow().setAttributes(lp);

		assesPopupWindow.setOutsideTouchable(true);
		assesPopupWindow.setFocusable(true);
		assesPopupWindow.showAtLocation(assesPopupWindowContentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		assesPopupWindow.update();
		assesPopupWindow.setOnDismissListener(new OnDismissListener() {
			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
				lp.alpha = 1f;
				((Activity) context).getWindow().setAttributes(lp);
			}
		});
	}

	private boolean checkEvaluation() {
		if (ratingBar1.getRating() > 0 && ratingBar2.getRating() > 0 && ratingBar1.getRating() > 0) {
			return true;
		}
		return false;
	}

	// 评价订单
	private void requestOrderEvaluation(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(Constants.OrderEvaluation_URL);
				try {
					orderEvaluationInfo.Order_Key = orderInfo.Order_Key;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce().getToken());
					requestData.setUser_Key(MyApplication.getInstatnce().getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce().getUserTypeOid());
					requestData.setCompany_Oid(MyApplication.getInstatnce().getCompany_Oid());
					requestData.setDataValue(JsonUtils.toJsonData(orderEvaluationInfo));

					String jsonStr = JsonUtils.toJsonData(requestData);
					params.addBodyParameter(null, jsonStr);
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
							Message msg = Message.obtain();
							DataResultBase resultBase = JsonUtils.parseDataResultBase(str);
							if (resultBase.isSuccess()) {
								msg.what = Constants.Asses_Success;
								myHandler.sendMessage(msg);
							} else {
								msg.what = Constants.Asses_Fail;
								msg.obj = resultBase.getErrorText();
								myHandler.sendMessage(msg);
							}
						} catch (Exception e) {
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

			if (assesPopupWindow != null) {
				assesPopupWindow.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_OrderCome:
				Toast.makeText(context, "恭喜您！交货成功！", Toast.LENGTH_SHORT).show();
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				finish();
				break;
			case Constants.Asses_Success:
				Toast.makeText(context, "恭喜您！评价成功！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Asses_Fail:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				Toast.makeText(context, "评价失败！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.Request_Fail:
				if (waitDialog != null) {
					waitDialog.dismiss();
				}
				alertDialog = new AlertDialog.Builder(D_Delivery_GoodsActivity.this);
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
		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(JHPicture!=null){
			JHPicture.recycle();
			JHPicture=null;
		}
	}
	
	

}
