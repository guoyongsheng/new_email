/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-10
 * @Description: 跟新邮件状态的接口
 */
public interface IEmailListUpdateMailInOAService {

	/**
	 * 跟新邮件状态 未读-----已读
	 * 
	 * @param remarkReadItemId
	 *            邮件id数组
	 * @param curEmailType
	 *            收件箱 草稿箱 已发送
	 * @param url
	 *            网址
	 * @param token
	 *            凭证
	 * 
	 * @param listener
	 *            回调接口
	 */
	void updateMailStatus(String[] remarkReadItemId, String curEmailType, String url, String token, CallBackListener<Boolean> listener);
}
