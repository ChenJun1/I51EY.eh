package com.cvnavi.logistics.i51eyun.eh.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class SetViewValueUtil {

	public static void setTextViewValue(TextView textView, Object object) {
		if (object == null) {
			textView.setText("");
			return;
		}
		
		String value = String.valueOf(object);
		if (TextUtils.isEmpty(value)) {
			textView.setText("--");
			return;
		}
		textView.setText(value);
	}

	public static void setEditTextValue(EditText editText, Object object) {
		if (object == null) {
			editText.setText("");
			return;
		}
		
		String value = String.valueOf(object);
		if (TextUtils.isEmpty(value)) {
			editText.setText("--");
			return;
		}
		editText.setText(value);
	}

	// ======================================特定，只适用于本系统==============================
	public static void setNumberValue(TextView textView, Object object) {
		if (object == null) {
			textView.setText("--件");
			return;
		}
		
		String value = String.valueOf(object);
		if (TextUtils.isEmpty(value)) {
			textView.setText("--件");
			return;
		}
		textView.setText(value + "件");
	}

	public static void setWeightValue(TextView textView, Object object) {
		if (object == null) {
			textView.setText("--kg");
			return;
		}
		
		String value = String.valueOf(object);
		if (TextUtils.isEmpty(value)) {
			textView.setText("--kg");
			return;
		}
		textView.setText(value + "kg");
	}

	public static void setVolumeValue(TextView textView, Object object) {
		if (object == null) {
			textView.setText("--m³");
			return;
		}
		
		String value = String.valueOf(object);
		if (TextUtils.isEmpty(value)) {
			textView.setText("--m³");
			return;
		}
		textView.setText(value + "m³");
	}
	
	public static void setEvaluationScoreValue(TextView textView, Object object) {
		if (object == null) {
			textView.setText("--分");
			return;
		}
		
		String value = String.valueOf(object);
		if (TextUtils.isEmpty(value)) {
			textView.setText("--分");
			return;
		}
		textView.setText(value + "分");
	}
	
	public static void setRatingBar(RatingBar ratingBar, String value) {
		if (TextUtils.isEmpty(value)) {
			ratingBar.setRating(0f);
			return;
		}
		ratingBar.setRating(Float.valueOf(value));
	}

}
