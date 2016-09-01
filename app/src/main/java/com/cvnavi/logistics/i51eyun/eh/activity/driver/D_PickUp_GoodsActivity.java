package com.cvnavi.logistics.i51eyun.eh.activity.driver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataRequestData;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.ConfirmTakeOrCompleteOrderRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
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
 *  
 * 
 * @author 作者：fanxb
 * @version 创建时间：2016-5-25 下午7:53:51  类说明 
 */

/**
 * 提货信息录入。
 * 
 * @author JohnnyYuan
 * 
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_pick_up_goods)
public class D_PickUp_GoodsActivity extends BaseActivity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;

	@ViewInject(R.id.d_pick_up_goods_image_text)
	private TextView d_pick_up_goods_image_text;
	@ViewInject(R.id.d_pick_up_imageview)
	private ImageView d_pick_up_imageview;

	@ViewInject(R.id.d_pick_up_btn)
	private Button d_pick_up_btn;

	private Bitmap THPicture=null;
	private BitmapFactory.Options options = new BitmapFactory.Options();
	private static String picFileFullName;

	private Context context;
	private AlertDialog.Builder alertDialog;
	private CommonWaitDialog waitDialog;

	private WaitingOrderBaseInfo orderInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		x.image().clearMemCache();
		x.image().clearCacheFiles();

		context = this;
		this.titlt_textView.setText("提货信息录入");
		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);

		if (getIntent().getSerializableExtra(Constants.OrderInfo) != null) {
			orderInfo = (WaitingOrderBaseInfo) getIntent()
					.getSerializableExtra(Constants.OrderInfo);
		}
	}

	@Event(value = { R.id.back_linearLayout, R.id.d_pick_up_goods_image_text,
			R.id.d_pick_up_btn }, type = View.OnClickListener.class)
	private void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		case R.id.d_pick_up_goods_image_text:
			showImageView();
			break;
		case R.id.d_pick_up_btn:
			if (!NetWorkUtils.isNetWort(this)) {
				return;
			}
			if (verifyData() == false) {
				return;
			}

			waitDialog.show();
			requestPickUpOrder(orderInfo);
			break;
		default:
			break;
		}
	}

	private boolean verifyData() {
		if (THPicture == null) {
			Toast.makeText(context, "请上传提货照片！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	// 提货。
	private void requestPickUpOrder(final WaitingOrderBaseInfo orderInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams(
						Constants.Confirm_TakeOrCompleteOrderByDriver_URL);
				try {
					String THPictureStrBase64 = getDrivingPicBase64();

					ConfirmTakeOrCompleteOrderRequestBean requestBean = new ConfirmTakeOrCompleteOrderRequestBean();
					requestBean.Order_Key = orderInfo.Order_Key;
					requestBean.Serial_Oid = orderInfo.Serial_Oid;
					requestBean.Consignee_City=orderInfo.Consignee_City;
					requestBean.Shipping_City=orderInfo.Shipping_City;
					requestBean.PhotoStatus = "AC";
					requestBean.Actual_Consignee = orderInfo.Consignee_Name;
					requestBean.TH_Photo = THPictureStrBase64;

					DataRequestData requestData = new DataRequestData();
					requestData.setToken(MyApplication.getInstatnce()
							.getToken());
					requestData.setUser_Key(MyApplication.getInstatnce()
							.getUserKey());
					requestData.setUserType_Oid(MyApplication.getInstatnce()
							.getUserTypeOid());
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
							DataResultBase resultBase = JsonUtils
									.parseDataResultBase(str);
							Message msg = Message.obtain();
							if (resultBase.isSuccess()) {
								// 发送消息通知。
								msg.what = Constants.Request_Success;
								myHandler.sendMessage(msg);

							} else {
								msg.what = Constants.Request_Fail;
								msg.obj = resultBase.getDataValue();
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
			if (waitDialog != null) {
				waitDialog.dismiss();
			}

			switch (msg.what) {
			case Constants.Request_Success:
				Toast.makeText(context, "恭喜您！提货成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case Constants.Request_Fail:
				alertDialog = new AlertDialog.Builder(
						D_PickUp_GoodsActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(msg.obj == null ? "系统错误，请联系客服！" : String
						.valueOf(msg.obj));
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

	private String getDrivingPicBase64() {
		Bitmap bitmap = null;
		if (THPicture != null) {
			bitmap = BitmapUtil.comp(THPicture);
		}
		String drivingPicBase64 = BitmapUtil.encodeToBase64(bitmap,
				Bitmap.CompressFormat.PNG, 50);
		return drivingPicBase64;
	}

	private void showImageView() {
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("上传照片");
		alertDialog.setItems(new String[] { "相机拍照", "本地照片" },
				new OnClickListener() {
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

	// 相机拍照
	public void photograph() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			File outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}

			File outFile = new File(outDir, System.currentTimeMillis()
					+ "driver_license.png");
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
		startActivityForResult(Intent.createChooser(intent, "选择照片"),
				PICK_IMAGE_ACTIVITY_REQUEST_CODE);
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
			THPicture = MediaStore.Images.Media.getBitmap(resolver, pictureUri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显得到bitmap图片
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(pictureUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		
		
		ImageOptions imageOptions = new ImageOptions.Builder()
        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
        .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
        .build();
		x.image().bind(d_pick_up_imageview, path,imageOptions);
	}

//	/**
//	 * 读取照片exif信息中的旋转角度<br/>
//	 * http://www.eoeandroid.com/thread-196978-1-1.html
//	 * 
//	 * @param path
//	 *            照片路径
//	 * @return角度
//	 */
//	public static int readPictureDegree(String path) {
//		int degree = 0;
//		try {
//			ExifInterface exifInterface = new ExifInterface(path);
//			int orientation = exifInterface.getAttributeInt(
//					ExifInterface.TAG_ORIENTATION,
//					ExifInterface.ORIENTATION_NORMAL);
//			switch (orientation) {
//			case ExifInterface.ORIENTATION_ROTATE_90:
//				degree = 90;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_180:
//				degree = 180;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_270:
//				degree = 270;
//				break;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return degree;
//	}

	private void setImageView(String realPath) {
		options.inSampleSize = 1;
		try {

			THPicture = BitmapFactory.decodeFile(realPath);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		ImageOptions imageOptions = new ImageOptions.Builder()
				.setRadius(DensityUtil.dip2px(5))// ImageView圆角半径
				.setImageScaleType(ImageView.ScaleType.FIT_CENTER).build();
		x.image().bind(d_pick_up_imageview, realPath, imageOptions);

//		THPicture = BitmapFactory.decodeFile(realPath, options);
//		x.image().bind(d_pick_up_imageview, realPath);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(THPicture!=null){
			THPicture.recycle();
			THPicture=null;
		}
	}
}
