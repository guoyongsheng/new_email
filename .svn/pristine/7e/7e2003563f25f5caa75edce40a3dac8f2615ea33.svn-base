/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.Email;

/**
 * @author wesley
 * @date 2016-10-17下午3:46:50
 * @Description: 邮件列表协议接口
 */
public interface EmailListContract {

	interface View extends BaseView<EmailListPresenter> {

		/**
		 * 根据邮件类型来获取数据
		 * 
		 * @param emailType
		 *            邮件类型
		 * @param startPosition
		 *            开始位置
		 * 
		 * @param isRefreshing
		 *            true:下拉刷新 false:滚动加载
		 */
		void loadData(int emailType, int startPosition, boolean isRefreshing);

		/**
		 * 显示邮件数据
		 * 
		 * @param list
		 *            邮件集合
		 */
		void showData(ArrayList<Email> list);

		/**
		 * 显示错误的信息
		 * 
		 * @param errorMessage
		 *            错误信息
		 */
		void showErrorMessage(String errorMessage);

		/**
		 * 显示正确的信息
		 * 
		 * @param message
		 *            正确的信息
		 */
		void showSuccessMessage(String message);

		/**
		 * 跟新数据
		 * 
		 * @param position
		 *            在列表中的位置
		 */
		void updateData(int position);

		/**
		 * 改变邮件状态 把未读邮件置为已读
		 * 
		 * @param messageNumber
		 *            邮件number
		 * @param isNews
		 *            是否未读
		 * @param position
		 *            item在列表中的位置
		 * 
		 * @param email
		 *            要跟新的邮件
		 */
		void updateMailStatus(String messageId, int messageNumber, boolean isNews, int position, Email email);

		/**
		 * 改变邮件的状态
		 * 
		 * @param position
		 *            item在列表中的位置
		 * @param status
		 *            0:已读 1:未读
		 * 
		 * @param type
		 *            类型
		 * @param email
		 *            要跟新的邮件
		 */
		void updateMailStatus(int position, int status, int type, Email email);

		/**
		 * 同时改变多个邮件的状态
		 * 
		 * @param list
		 *            邮件集合
		 * @param status
		 *            0:已读 1:未读
		 * 
		 * @param listEmail
		 *            选中的多个邮件
		 */
		void updateMailGroupStatus(List<Integer> list, int status, List<Email> listEmail);

		/**
		 * 删除多个邮件后刷新界面
		 */
		void refresh();

		/**
		 * @param msg
		 *            对话框的信息
		 */
		void showProgress(String msg);

		/**
		 * 隱藏對話框
		 */
		void hideProgress();

		/**
		 * 显示没有邮件时的布局
		 */
		void showEmptyView();

		/**
		 * 标记或者取消标记成功
		 * 
		 * @param status
		 *            0:取消标记 1:添加标记
		 * @param position
		 *            在列表中的位置
		 * 
		 * @param email
		 */
		void markSuccess(int status, int position, Email email);

		/**
		 * 邮件删除失败
		 * 
		 * @param position
		 *            在列表中的位置
		 * 
		 * @param email
		 *            邮件
		 */
		void deleteFailure(int position, Email email);

		/**
		 * 同时添加多个星标
		 * 
		 * @param list
		 *            邮件集合
		 * @param status
		 *            0:取消星标 1：添加星标
		 * 
		 * @param listEmail
		 *            email list
		 */
		void markMailGroupStatus(List<Integer> list, int status, List<Email> listEmail);

		/**
		 * 加载OA邮箱中的邮件
		 * 
		 * @param type
		 *            1:收件箱 2：草稿箱 3:已发送
		 * @param start
		 *            开始的页数
		 * @param size
		 *            每页加载多少条数据
		 * @param endpoint
		 *            url
		 * @param app_token
		 *            token
		 * @param isRefreshing
		 *            是否是刷新
		 */
		void loadDataInOA(int type, int start, int size, String endpoint, String app_token, boolean isRefreshing);

		/**
		 * 显示列表数据 oa邮箱
		 * 
		 * @param data
		 *            邮件集合
		 */
		void showDataInOA(ArrayList<Email> data);

		/**
		 * 停止loading的加載
		 */
		public void stopLoading();

		/**
		 * 获取保存在本地的账号
		 * 
		 * @return
		 */
		public ArrayList<AccountInfo> getAccount();

		/**
		 * 获取oa邮件标记星标的url
		 * 
		 * @param type
		 *            0:星标邮件的url 1:标记未读和已读的url
		 * @return url
		 */
		String getUrl(int type);

		/**
		 * 获取凭证
		 * 
		 * @return token
		 */
		String getToken();
	}

	interface Presenter extends BasePresenter {

		/**
		 * 根据邮件类型来获取数据
		 * 
		 * @param emailType
		 *            邮件类型
		 * @param startPosition
		 *            开始位置
		 * 
		 * @param isRefreshing
		 *            是否是刷新
		 */
		void loadData(int emailType, int startPosition, int type, boolean isRefreshing);

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
		 * @param email
		 *            要刪除的邮件
		 */
		void deleteMailByMessageId(String id, int emailType, String messageId, int position, int type, Email email);

		/**
		 * 删除多个邮件
		 * 
		 * @param emailType
		 *            邮件类型
		 * @param list
		 *            邮件messageNumber集合
		 * @param type
		 *            类型
		 */
		void deleteMailGroundByMessageId(int emailType, ArrayList<Email> list, int type);

		/**
		 * 把未读邮件置为已读
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
		 * @param email
		 *            要跟新的邮件
		 */
		void updateMailStatus(String messageId, int messageNumber, int status, int type, int position, Email email);

		/**
		 * 改变邮件的状态
		 * 
		 * @param list
		 *            邮件集合
		 * @param status
		 *            0:标记已读 1:标记未读
		 * @param type
		 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
		 */
		void updateMailGroupStatus(List<Email> list, int status, int type);

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
		 * @param email
		 *            email to remark
		 */
		void markOrUnMarkMail(String messageId, int messageNumber, int status, int type, int position, Email email);

		/**
		 * 添加星标或者取消星标
		 * 
		 * @param list
		 *            邮件集合
		 * @param status
		 *            0: 取消星标 1:添加星标
		 * @param type
		 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
		 */
		void markOrUnmardMailGroup(List<Email> list, int status, int type);

		/**
		 * 加载OA邮箱中的邮件
		 * 
		 * @param type
		 *            1:收件箱 2：草稿箱 3:已发送
		 * @param start
		 *            开始的页数
		 * @param size
		 *            每页加载多少条数据
		 * @param endpoint
		 *            url
		 * @param app_token
		 *            token
		 * 
		 * @param isRefreshing
		 *            是否是刷新
		 * 
		 * @param cond
		 *            筛选条件
		 * 
		 */
		void loadDataInOA(int type, int start, int size, String endpoint, String app_token, boolean isRefreshing, String cond);

		/**
		 * 删除OA邮件
		 * 
		 * @param deleteItemId
		 *            邮件id数组
		 * @param curEmailType
		 *            收件箱 草稿箱 已发送
		 * @param url
		 *            网址
		 * @param token
		 *            凭证
		 * @param position
		 *            删除的位置 position < 0 删除多个邮件 position >= 0 刪除一封邮件
		 * @param email
		 *            要删除的邮件
		 * 
		 * @param deleteType
		 *            0:删除一封邮件 1:删除多个邮件
		 */
		void deleteMailInOA(String[] deleteItemId, String curEmailType, String url, String token, int position, Email email, int deleteType);

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
		 * @param position
		 *            跟新的位置
		 * @param email
		 *            要跟新的邮件
		 * 
		 * @param updateType
		 *            0:跟新一封 1:跟新多封
		 * 
		 * @param status
		 *            0:标记已读 1:标记未读
		 */
		void updateMailStatusInOA(String[] remarkReadItemId, String curEmailType, String url, String token, int position, Email email, int updateType, int status);

		/**
		 * @param list
		 * @param listSharedPreference
		 * @param account
		 * @return
		 */
		ArrayList<AccountInfo> getNoSmaeAccountList(ArrayList<AccountInfo> list, ArrayList<AccountInfo> listSharedPreference, String account);

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
		 * @param position
		 *            邮件在列表中的位置
		 * @param email
		 *            邮件
		 * 
		 * @param markType
		 *            0:标记一封 1:标记多封
		 */
		void markMailInOA(String[] array, int status, String url, String token, int position, Email email, int markType);
	}
}
