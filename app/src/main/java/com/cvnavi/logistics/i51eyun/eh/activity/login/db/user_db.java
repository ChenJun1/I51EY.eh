package com.cvnavi.logistics.i51eyun.eh.activity.login.db;



import android.util.Log;

import com.cvnavi.logistics.i51eyun.eh.MyApplication;
import com.cvnavi.logistics.i51eyun.eh.activity.login.bean.UserBean;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

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
public class user_db {
    private DbManager db = x.getDb(MyApplication.getConfig());;

    /**
     * 添加用户信息
     * @param userBean
     */
    public void addUser(UserBean userBean) {
        try
        {
            db.save(userBean);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否记住用户信息
     * @param isCheck
     * @return
     */
    public UserBean getFindFirst(boolean isCheck){
        try {
            List<UserBean> users =db.selector(UserBean.class).where("isCheckBox", "=",isCheck).orderBy("Id", true).findAll();
            if (users != null && users.size()>0)
            {
                return users.get(0);
            }
            return null;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否存在该用户
     * @param username
     * @return
     */
    public boolean getFindFirst(String username){
        try {
        	if (db.selector(UserBean.class).where("UserName", "=",username).findFirst() != null)
            {
        		int a = db.delete(UserBean.class, WhereBuilder.b("UserName", "=",username));
        		Log.d("---------------", "------------a"+a);
				return true;
			}
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  true;
    }

    /**
     * 查询全部用户
     * @return List<UserBean>
     */
    public List<UserBean> getFindAll(){
        try {
            List<UserBean> users =  db.selector(UserBean.class).findAll();
            if (users != null)
            {
                return users;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
