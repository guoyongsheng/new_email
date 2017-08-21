package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import java.util.List;

import com.zfsoft.zf_new_email.base.BaseServiceImpl;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2016-12-14
 * @Description:邮件列表页接口
 */
public abstract class IEmailListService extends BaseServiceImpl {

	/**
	 * 根据邮件类型来获取数据
	 * 
	 * @param emailType
	 *            邮件类型
	 * @param startPosition
	 *            开始位置
	 * 
	 * @param type
	 *            0:收件箱 1:未读邮件
	 * 
	 * @param isRefreshing
	 *            true:下拉刷新 false:滚动加载
	 * 
	 * @param listener
	 *            回调接口
	 */
	abstract void loadData(int emailType, int startPosition, int type, boolean isRefreshing, CallBackListener<ArrayList<Email>> listener);

	/**
	 * 删除邮件
	 * 
	 * @param emailType
	 *            邮件类型
	 * @param messageId
	 *            邮件id
	 * @param position
	 *            邮件在列表中的位置
	 * 
	 * @param type
	 *            类型
	 * 
	 * @param listener
	 *            回调接口
	 */
	abstract void deleteMailByMessageId(String id, int emailType, String messageId, int position, int type, CallBackListener<Integer> listener);

	/**
	 * 删除多个邮件
	 * 
	 * @param emailType
	 *            邮件类型
	 * @param list
	 *            邮件messageNumber集合
	 * @param type
	 *            类型
	 * 
	 * @param listener
	 *            回调接口
	 */
	abstract void deleteMailGroundByMessageId(int emailType, ArrayList<Email> list, int type, CallBackListener<Boolean> listener);

	/**
	 * 把未读邮件置为已读 已读邮件置为未读邮件
	 * 
	 * @param messageNumber
	 *            邮件number
	 * @param status
	 *            0:标记已读 1:标记未读
	 * 
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * 
	 * @param position
	 *            在列表中的位置
	 * 
	 * @param listener
	 *            回调接口
	 */
	abstract void updateMailStatus(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener);

	/**
	 * 同时改变多个邮件的状态
	 * 
	 * @param list
	 *            邮件集合
	 * @param status
	 *            0:标记已读 1:标记未读
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * 
	 * @param listener
	 *            接口回调
	 */
	abstract void updateMailGroupStatus(List<Email> list, int status, int type, CallBackListener<Boolean> listener);

	/**
	 * 添加星标或者取消添加星标
	 * 
	 * @param messageNumber
	 *            邮件number
	 * @param status
	 *            0: 取消星标 1:添加星标
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * @param position
	 *            在列表中的位置
	 * 
	 * @param listener
	 *            回调接口
	 */
	abstract void markOrUnMarkMail(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener);

	/**
	 * 添加星标或者取消星标
	 * 
	 * @param list
	 *            邮件集合
	 * @param status
	 *            0: 取消星标 1:添加星标
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * 
	 * @param listener
	 *            接口回调
	 */
	abstract void markOrUnmardMailGroup(List<Email> list, int status, int type, CallBackListener<Boolean> listener);
}
