/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.util.ArrayList;
import java.util.List;

import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2016-12-16
 * @Description: 发送邮件的接口
 */
public interface IEmailSendOrReplyService {

	/**
	 * 发送邮件
	 * 
	 * @param listReceiver
	 *            收件人集合
	 * @param senderName
	 *            发送者地址
	 * @param subject
	 *            主题
	 * @param listAttachmenet
	 *            附件集合
	 * @param conetnt
	 *            内容
	 * @param isDraft
	 *            是否保存的草稿箱
	 * 
	 * @param listener
	 *            回调接口
	 */
	void sendEmail(List<String> listReceiver, String senderName, String subject, List<Attachment> listAttachmenet, String conetnt, boolean isDraft, CallBackListener<Boolean> listener);

	/**
	 * 回复邮件
	 * 
	 * @param messageNumber
	 *            被回复邮件的number
	 * @param subject
	 *            邮件的主题
	 * @param content
	 *            邮件的内容
	 * @param type
	 *            0:回复 1:回复全部
	 * 
	 * @param isDraft
	 *            是否存入草稿箱
	 * 
	 * @param inbox_type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
	 * 
	 * @param receiveList
	 *            接收者邮箱地址集合
	 * 
	 * @param listAttachment
	 *            附件集合
	 * 
	 * @param listener
	 *            回调接口
	 */
	void replyEmail(String messageId, int messageNumber, String subject, String content, int type, boolean isDraft, int inbox_type, ArrayList<String> receiveList, List<Attachment> listAttachment, CallBackListener<Boolean> listener);

	/**
	 * 转发邮件
	 * 
	 * @param messageNumber
	 *            被回复邮件的number
	 * @param subject
	 *            邮件的主题
	 * @param content
	 *            邮件的内容
	 * @param type
	 *            收件箱 垃圾箱 已发送
	 * @param isDraft
	 *            是否存入草稿箱 true:是 false:否
	 * 
	 * @param forwardTo
	 *            转发给谁
	 * 
	 * @param listAttachment
	 *            附件集合
	 * 
	 * @param listener
	 *            回调接口
	 */
	void forwardEmail(String messageId, int messageNumber, String subject, String content, int type, boolean isDraft, ArrayList<String> forwardTo, List<Attachment> listAttachment, CallBackListener<Boolean> listener);
}
