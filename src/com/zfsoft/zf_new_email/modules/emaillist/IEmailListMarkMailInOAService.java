/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-18
 * @Description: 添加星标和取消星标的接口
 */
public interface IEmailListMarkMailInOAService {

	/**
	 * 添加星标和取消星标
	 * 
	 * @param array
	 *            邮件id
	 * @param status
	 *            0:取消星标 1:添加星标
	 * @param url
	 *            网址
	 * @param token
	 *            凭证
	 * 
	 * @param listener
	 *            回調接口
	 */
	void markMailInOA(String[] array, int status, String url, String token, CallBackListener<Boolean> listener);
}
