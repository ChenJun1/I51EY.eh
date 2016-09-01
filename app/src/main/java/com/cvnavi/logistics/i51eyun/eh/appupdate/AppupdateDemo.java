/**
 * Administrator2016-6-12
 */
package com.cvnavi.logistics.i51eyun.eh.appupdate;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.cvnavi.logistics.i51eyun.eh.BaseActivity;
import com.cvnavi.logistics.i51eyun.eh.Constants;

import java.io.File;

/**
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-6-12 下午1:26:53
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

public class AppupdateDemo extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		downloadprogressfile();
	}
	
	private void downloadprogressfile() {
		//文件下载地址
		String url=Constants.GetAppDownPath_URL;
		//String url="http://imgserver.i51ey.com:48181/gmsimg/common/2.png";
		//文件保存在本地的路径
		String filepath=Environment.getExternalStorageDirectory()+"/I5i.eh.apk";
		XUtil.DownLoadFile(url, filepath,new MyProgressCallBack<File>(){

			@Override
			public void onSuccess(File result) {
				super.onSuccess(result);
				System.err.println("onSuccess");
				Log.e("onLoading", result.getAbsolutePath());
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				super.onError(ex, isOnCallback);
				String exString=ex.getMessage();
				Log.e("onLoading", exString);
			}

			@Override
			public void onLoading(long total, long current,
					boolean isDownloading) {
				super.onLoading(total, current, isDownloading);
				Log.e("onLoading", "total"+total+"current"+current+isDownloading);
			}

			@Override
			public void onCancelled(CancelledException cex) {
				// TODO Auto-generated method stub
				super.onCancelled(cex);
				Log.e("onCancelled","onCancelled"+cex.getMessage());
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				super.onFinished();
				Log.e("onFinished","onFinished");
			}

			@Override
			public void onWaiting() {
				// TODO Auto-generated method stub
				super.onWaiting();
				Log.e("onWaiting","onFinished");
			}

			@Override
			public void onStarted() {
				super.onStarted();
				Log.e("onStarted","onStarted");
			}
			
			

		});
	}

}
