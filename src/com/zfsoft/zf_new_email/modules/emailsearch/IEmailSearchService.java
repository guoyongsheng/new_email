/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsearch;

import java.util.List;

import com.zfsoft.zf_new_email.base.BaseServiceImpl;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2016-12-15
 * @Description: 搜索邮件界面的接口
 */
public abstract class IEmailSearchService extends BaseServiceImpl{

	/**
	 * 搜索邮件
	 * 
	 * @param startPosition
	 *            开始位置
	 * @param type
	 *            类型 0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除 5:星标邮件
	 * @param status
	 *            0:发件人 1:收件人 2:主题 3:全部
	 * @param content
	 *            搜索的内容
	 * @param isRefreshing
	 *            是否是刷新
	 * 
	 * @param listener
	 *            回调接口
	 */
	abstract void searchMail(int startPosition, int type, int status, String content, boolean isRefreshing, CallBackListener<List<Email>> listener);

	/**
	 * 删除搜索出来的邮件
	 * 
	 * @param emailType
	 *            邮件类型
	 * @param messageNumber
	 *            邮件的number
	 * @param position
	 *            邮件在列表中的位置
	 * @param type
	 *            收件箱 草稿箱
	 * @param email
	 *            邮件
	 * @param status
	 *            按收件人 按发件人 按主题 全部
	 * @param content
	 *            搜索内容
	 * @param listener
	 */
	abstract void deleteMialInSearch(String messageId, int emailType, int messageNumber, int position, int type, Email email, int status, String content, int startPosition, CallBackListener<Boolean> listener);
}
