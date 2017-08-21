/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsearch;

import java.util.ArrayList;
import java.util.List;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.entity.HistoryInfo;

/**
 * @author wesley
 * @date: 2016-11-22
 * @Description:协议接口
 */
public interface EmailSearchContract {

	interface View extends BaseView<EmailSearchPresenter> {

		/**
		 * 获取搜索历史
		 * 
		 * @return 搜索历史列表
		 */
		List<HistoryInfo> getHistory();

		/**
		 * 获取要搜索邮件的内容
		 * 
		 * @return 邮件内容
		 */
		String getSearchContent();

		/**
		 * 搜索邮件
		 * 
		 * @param startPosition
		 *            开始位置
		 * @param type
		 *            类型 0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除 5:星标邮件
		 * 
		 * @param status
		 *            0:发件人 1:收件人 2:主题 3:全部
		 * @param content
		 *            搜索的内容
		 * 
		 * @param isRefreshing
		 *            是否是刷新
		 */
		void searchMail(int startPosition, int type, int status, String content, boolean isRefreshing);

		/**
		 * 显示dialog
		 */
		void showProgressDialog();

		/**
		 * 隐藏dialog
		 */
		void hideProgressDialog();

		/**
		 * 提示错误的信息
		 * 
		 * @param errorMessage
		 *            错误的信息
		 */
		void showErrorMessage(String errorMessage);

		/**
		 * 搜索成功返回的结果
		 * 
		 * @param list
		 *            邮件集合
		 */
		void onSuccess(List<Email> list);

		/**
		 * 保存搜索历史
		 * 
		 * @param content
		 *            搜索的内容
		 */
		void saveSearchHistory(String content);

		/**
		 * 显示没有邮件时的布局
		 */
		void showEmptyView();

		/**
		 * 显示有邮件的布局
		 */
		void showCommonView();

		/**
		 * @param position
		 *            列表中的位置
		 * @param email
		 *            删除的邮件
		 */
		void deleteFailure(int position, Email email);

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
		 * 
		 * @param 搜索条件
		 */
		void loadDataInOA(int type, int start, int size, String endpoint, String app_token, boolean isRefreshing, String cond);

		/**
		 * 根据type 获取url
		 * 
		 * @param type
		 *            0: 搜索的url 1:删除的url
		 * @return url
		 */
		String getUrl(int type);

		/**
		 * 获取请求凭证
		 * 
		 * @return token
		 */
		String getToken();

		/**
		 * 显示列表数据 oa邮箱
		 * 
		 * @param data
		 *            邮件集合
		 */
		void showDataInOA(ArrayList<Email> data);
	}

	interface Presenter extends BasePresenter {

		/**
		 * 校驗搜索的内容是否为空
		 * 
		 * @param content
		 *            要搜索的内容
		 * @return trur:空 false:不为空
		 */
		boolean checkSearchContentIsEmpty(String content);

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
		 */
		void searchMail(int startPosition, int type, int status, String content, boolean isRefreshing);

		/**
		 * 判断是否有相同的搜索历史
		 * 
		 * @param list
		 *            搜索历史集合
		 * @param content
		 *            搜索的内容
		 * @return true:有 false:沒有
		 */
		boolean checkHasSameHistory(List<HistoryInfo> list, String content);

		/**
		 * 获取搜索历史
		 * 
		 * @param list
		 *            搜索历史集合
		 * @param content
		 *            搜索的内容
		 * @return 搜索历史
		 */
		List<HistoryInfo> getHistoryList(List<HistoryInfo> list, String content);

		/**
		 * 获取过滤后的历史列表
		 * 
		 * @param list
		 *            搜索历史集合
		 * @param content
		 *            搜索的内容
		 * @return 历史列表
		 */
		List<HistoryInfo> getDealHistoryList(List<HistoryInfo> list, String content);

		/**
		 * 删除邮件
		 * 
		 * @param emailType
		 *            邮件类型
		 * @param messageNumber
		 *            邮件id
		 * @param position
		 *            邮件在列表中的位置
		 * 
		 * @param type
		 *            类型
		 * 
		 * @param email
		 *            要刪除的邮件
		 * 
		 * @param status
		 *            按发件人 按收件人 按主题
		 * @param content
		 *            搜索的内容
		 * 
		 * @param startPosition
		 *            开始位置
		 */
		void deleteMailByMessageId(String messageId, int emailType, int messageNumber, int position, int type, Email email, int status, String content, int startPosition);

		/**
		 * 查詢oa邮件
		 * 
		 * @param type
		 *            类型
		 * @param start
		 *            开始位置
		 * @param size
		 *            每页查询的条数
		 * @param endpoint
		 *            url
		 * @param app_token
		 *            token
		 * @param cond
		 *            查询条件
		 */
		void loadDataInOA(int type, int start, int size, String endpoint, String app_token, String cond, boolean isRefreshing);
		
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
	}
}
