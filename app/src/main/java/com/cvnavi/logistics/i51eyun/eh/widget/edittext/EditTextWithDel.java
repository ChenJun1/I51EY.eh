package com.cvnavi.logistics.i51eyun.eh.widget.edittext;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.cvnavi.logistics.i51eyun.eh.R;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 自定义EditText文本框
 * @date 2016-5-17 下午1:28:30
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class EditTextWithDel extends EditText{
	private Drawable imgAble;
	private Context mContext;
	public EditTextWithDel(Context context) {
		super(context);
	}
	public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public EditTextWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	private void init() {
		imgAble = mContext.getResources().getDrawable(R.drawable.d_g);
		addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		setDrawable();
	}
	
	//设置删除图片
	private void setDrawable() {
		if(length() < 1)
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		else
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
	}
	
	 // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if(rect.contains(eventX, eventY)) 
            	setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
