/**
 * Administrator2016-5-25
 */
package com.cvnavi.logistics.i51eyun.eh.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 版权所有 上海势航
 * 
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-25 下午1:52:47
 * @version 1.0.0
 * @param <ImageViewTouch>
 * @email yuanlunjie@163.com
 */

@ContentView(R.layout.pictrue)
public class PictrueActivity extends BaseActivity {

//	@ViewInject(R.id.image)
//	private ImageViewTouch image;
	@ViewInject(R.id.image)
	private ImageView image;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private CommonWaitDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.titlt_textView.setText("查看照片");
		dialog = new CommonWaitDialog(this, R.style.progress_dialog);
		dialog.show();

		String extra = getIntent().getStringExtra(Constants.Image_pic);
		if (!TextUtils.isEmpty(extra)) {
			dialog.dismiss();
//			 ImageOptions imageOptions = new ImageOptions.Builder().setCrop(true)
//			 .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//			 .setLoadingDrawableId(R.drawable.ic_launcher)//
//			 .setFailureDrawableId(R.drawable.ic_launcher)// 加载失败后默认显示图片
//			 .build();
//			x.image().bind(image, extra,imageOptions);
			imageLoader.displayImage(extra, image);
		}
	}

	@Event(value = { R.id.back_linearLayout })
	private void OnClick(View v) {
		switch (v.getId()) {
		case R.id.back_linearLayout:
			finish();
			break;
		default:
			break;
		}
	}

}
