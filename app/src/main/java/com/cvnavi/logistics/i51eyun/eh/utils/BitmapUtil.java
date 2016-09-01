package com.cvnavi.logistics.i51eyun.eh.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 图片转Base64 使用：Bitmap, Bitmap.CompressFormat.PNG, 100
 * @date 2016-5-17 下午1:23:14
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class BitmapUtil {

	/**
	 * 图片转Base64 使用：Bitmap, Bitmap.CompressFormat.PNG, 100
	 * 
	 * @param image
	 * @param compressFormat
	 * @param quality
	 * @return
	 */

	public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		image.compress(compressFormat, quality, byteArrayOS);
		return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}

	/**
	 * Base64转图片
	 * 
	 * @param input
	 * @return
	 */
	public static Bitmap decodeBase64(String input) {
		byte[] decodedBytes = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}

	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 500) { // 大于1M 就压缩
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 500f;
		float ww = 300f;

		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);
	}

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}
}
