/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author wesley
 * @date 2016-10-18上午10:05:12
 * @Description: 登录验证
 */
public class MyAuthenticator extends Authenticator {

	private String userName = null;
	private String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	/**
	 * 登入校验
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
