package com.cvnavi.logistics.i51eyun.eh.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 规范Activity跳转的接口协议
 * @date 2016-5-17 下午1:19:57
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public interface ISkipActivity {
	/**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls);

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Intent it);

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras);

    /**
     * show a @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Class<?> cls);

    /**
     * show a @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Intent it);

    /**
     * show a @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Class<?> cls, Bundle extras);
}
