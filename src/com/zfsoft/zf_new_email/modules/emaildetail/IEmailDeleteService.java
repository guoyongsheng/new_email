/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaildetail;

import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2016-12-16
 * @Description:邮件详情接口
 */
public interface IEmailDeleteService {

	/**
	 * 添加星标或者取消星标
	 * 
	 * @param messageNumber
	 *            邮件number
	 * @param status
	 *            0：取消星标 1:添加星标
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * @param listener
	 *            回调接口
	 */
	public void markOrUnMarkMail(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener);

	/**
	 * 把未读邮件置为已读
	 * 
	 * @param messageNumber
	 *            邮件number
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * 
	 * @param status
	 *            0:已读 1:未读
	 * 
	 * @param listener
	 *            回调接口
	 */
	void updateMailStatus(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener);

	/**
	 * 根据邮件id获取邮件详情
	 * 
	 * @param mailId
	 *            邮件id
	 * @param type
	 *            类型
	 * @param listener
	 *            会调接口
	 */
	void searchEmailById(String mailId, int type, int email_type, CallBackListener<Email> listener);
}
