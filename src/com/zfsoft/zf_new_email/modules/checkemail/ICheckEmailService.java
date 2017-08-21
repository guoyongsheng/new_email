/**
 * 
 */
package com.zfsoft.zf_new_email.modules.checkemail;

import com.zfsoft.zf_new_email.base.BaseServiceImpl;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2016-12-14
 * @Description:校验邮箱界面的所有接口
 */
public abstract class ICheckEmailService extends BaseServiceImpl {

	/**
	 * 邮箱登陆
	 * 
	 * @param emailAddress
	 *            邮箱地址
	 * @param password
	 *            授权码
	 * @param login_from
	 *            登陆来源
	 */
	abstract void login(String emailAddress, String password, String login_from, CallBackListener<Boolean> listener);
}
