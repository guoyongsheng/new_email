/**
 * 
 */
package com.zfsoft.zf_new_email.entity;

import java.io.Serializable;

/**
 * @author wesley
 * @date: 2016-11-26
 * @Description: 联系人对象
 */
public class ContractsInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean isSelected; // 是否被选中 true：是 false:没有
	private String name; // 姓名
	private String emailAddress; // 邮箱地址

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
