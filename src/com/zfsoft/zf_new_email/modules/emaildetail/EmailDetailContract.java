/**
 * @date2016-10-19
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaildetail;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.Email;

/**
 * @author wesley
 * @date 2016-10-19下午7:43:02
 * @Description: 邮件详情协议接口
 */
public interface EmailDetailContract {

	interface View extends BaseView<EmailDetailPresenter> {

		/**
		 * 显示失败的信息
		 * 
		 * @param errorMessage
		 *            失败的信息
		 */
		void showDeteleError(String errorMessage);

		/**
		 * 显示成功的信息
		 * 
		 * @param message
		 *            成功的信息
		 */
		void showDeleteSuccess(String message);

		/**
		 * 跟新数据
		 * 
		 * @param position
		 *            位置
		 */
		void updateData(int position);

		/**
		 * 把未读邮件置为已读
		 * 
		 * @param messageNumber
		 *            邮件number
		 * 
		 * @param status
		 *            0:标为已读 1:标为未读
		 */
		void updateMailStatus(String messageId, int messageNumber, int status, int position);

		/**
		 * 判断邮件未读还是已读
		 * 
		 * @return true:未读 false:已读
		 */
		boolean isEmailNews();

		/**
		 * 改变邮件的状态
		 * 
		 * @param status
		 *            0:已读 1:未读
		 */
		void updateEmailStatus(int status, int position);

		/**
		 * 添加星标或者取消星标成功
		 * 
		 * @param status
		 *            0:取消标记 1:添加标记
		 */
		void markMailSuccess(int status, int position);

		/**
		 * 获取邮件详情
		 */
		void getMailDetailInOA();

		/**
		 * show oa mail detail message
		 * 
		 * @param data
		 *            mail instance
		 */
		void showMailDetailInOA(Email data);

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
		 * @param status
		 *            0:标记已读 1:标记未读
		 */
		void updateMailStatusInOA(String[] remarkReadItemId, String curEmailType, String url, String token, int status, int position);

		/**
		 * login token
		 * 
		 * @return token
		 */
		String getAppToken();

		/**
		 * get request url
		 * 
		 * @param type
		 *            0: not read 1:star
		 * @return
		 */
		String getUrl(int type);

		/**
		 * 根据邮件id获取邮件信息
		 * 
		 * @param mailId
		 *            邮件id
		 */
		void searchEmailById(String mailId);
	}

	interface Presenter extends BasePresenter {

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
		 */
		void updateMailStatus(String messageId, int messageNumber, int status, int type, int position);

		/**
		 * 添加星标或者取消星标
		 * 
		 * @param messageNumber
		 *            邮件number
		 * @param status
		 *            0：取消星标 1:添加星标
		 * @param type
		 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
		 */
		public void markOrUnMarkMail(String messageId, int messageNumber, int status, int type, int position);

		/**
		 * 根据邮件id获取oa邮件的详情
		 * 
		 * @param messageId
		 *            邮件id
		 * @param url
		 *            网址
		 * @param token
		 *            凭证
		 */
		void getMailDetailInOA(String messageId, String url, String token);

		/**
		 * 
		 * @param remarkReadItemId
		 * @param curEmailType
		 * @param url
		 * @param token
		 * @param status
		 */
		void updateMialStatusInOA(String[] remarkReadItemId, String curEmailType, String url, String token, int status, int position);

		/**
		 * 添加星标和取消星标 oa邮箱
		 * 
		 * @param array
		 *            数组
		 * @param status
		 *            0：取消星标 1:添加星标
		 * @param position
		 *            ：位置
		 * @param url
		 *            ：网址
		 * @param token
		 *            ：凭证
		 */
		void markOrUnMarkMailInOA(String[] array, int status, int position, String url, String token);

		/**
		 * 根据邮件的id获取邮件详情
		 * 
		 * @param mailId
		 *            邮件id
		 * @param type
		 *            0:收件箱
		 * 
		 * @param email_type
		 *            邮件类型
		 */
		void searchEmailById(String mailId, int type, int email_type);
	}
}
