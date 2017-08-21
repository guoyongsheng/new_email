/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-19
 * @Description: oa邮箱发送邮件的接口
 */
public interface IEmailSendOrReplyInOAService {

	/**
	 * 发送oa邮件
	 * 
	 * @param emailId
	 *            邮件id
	 * @param receiverId
	 *            接收者的id集合
	 * @param receiverName
	 *            接收者的姓名集合
	 * @param ccId
	 *            抄送者的id集合
	 * @param ccName
	 *            抄送者的姓名集合
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param mailType
	 *            邮件类型
	 * @param url
	 *            网址
	 * @param token
	 *            凭证
	 * 
	 * @param listener
	 *            回调接口
	 */
	void sendEmailInOA(String emailId, String receiverId, String receiverName, String ccId, String ccName, String subject, String content, int mailType, String url, String token, CallBackListener<Boolean> listener);
}
