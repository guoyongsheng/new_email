/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-2-8
 * @Description: 保存邮件到草稿箱的service
 */
public interface EmailSendOrReplyInOASaveMailService {

	/**
	 * 保存邮件到草稿箱
	 * 
	 * @param emailId
	 * @param receiverId
	 * @param receiverName
	 * @param ccId
	 * @param ccName
	 * @param subject
	 * @param content
	 * @param url
	 * @param token
	 * @param listener
	 */
	void saveEmailInOA(String emailId, String receiverId, String receiverName, String ccId, String ccName, String subject, String content, String url, String token, CallBackListener<Boolean> listener);
}
