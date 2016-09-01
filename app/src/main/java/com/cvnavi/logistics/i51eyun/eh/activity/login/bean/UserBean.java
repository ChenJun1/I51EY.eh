package com.cvnavi.logistics.i51eyun.eh.activity.login.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

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
@Table(name = "users")
public class UserBean {

	@Column(name ="Id", isId =true)
	private int Id;

	@Column(name = "UserName")
	private String UserName;

	@Column(name = "UserPassword")
	private String UserPassword;

	@Column(name = "isCheckBox")
	private boolean isCheckBox;

	public boolean isCheckBox() {
		return isCheckBox;
	}

	public void setCheckBox(boolean checkBox) {
		isCheckBox = checkBox;
	}

	public String getUserName() {
		return UserName;
	}

	public UserBean setUserName(String userName) {
		UserName = userName;
		return null;
	}

	public String getUserPassword() {
		return UserPassword;
	}

	public void setUserPassword(String userPassword) {
		UserPassword = userPassword;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
}
