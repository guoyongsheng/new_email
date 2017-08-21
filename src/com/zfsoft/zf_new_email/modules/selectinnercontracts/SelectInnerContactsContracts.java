/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.util.List;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;

/**
 * @author wesley
 * @date: 2017-2-9
 * @Description: 接口
 */
public interface SelectInnerContactsContracts {

	interface View extends BaseView<SelectInnerContactPresenter> {

		/**
		 * 获取请求地址
		 * 
		 * @return 网址
		 */
		String getUrl();

		/**
		 * 获取登录凭证
		 * 
		 * @return token
		 */
		String getToken();

		/**
		 * 加载数据
		 * 
		 * @param url
		 *            网址
		 * @param token
		 *            token
		 */
		void loadData(String url, String token);

		/**
		 * 开启动画
		 */
		void startAnimation();

		/**
		 * 停止动画
		 */
		void stopAnimation();

		/**
		 * 加载成功
		 * 
		 * @param list
		 *            联系人列表
		 */
		void loadSuccess(List<InnerContractsInfo> list);

		/**
		 * 加载失败
		 * 
		 * @param errorMessage
		 *            失败信息
		 */
		void loadFailure(String errorMessage);

		/**
		 * 初始化加载
		 */
		void initLoading();

		/**
		 * 初始化联系人
		 */
		void initContent();

		/**
		 * 获取下一级的数据
		 * 
		 * @param info
		 *            对象
		 * @param name
		 *            名字
		 * @param sum
		 *            数量
		 * @param url
		 *            网址
		 * @param token
		 *            登陆凭证
		 */
		void loadChildData(InnerContractsInfo info, String name, String sum, String url, String token);

		/**
		 * 显示错误信息
		 * 
		 * @param errorMessage
		 *            错误信息
		 */
		void showErrorMessage(String errorMessage);

		/**
		 * 加载下一级的数据成功
		 * 
		 * @param list
		 *            联系人列表
		 */
		void loadChildDataSuccess(List<InnerContractsInfo> list);

		/**
		 * 设置子节点是否正在加载
		 * 
		 * @param isChildLoading
		 *            是否正在加载
		 */
		void setChildLoading(boolean isChildLoading);

		/**
		 * 获取搜索的内容
		 * 
		 * @return 搜索的内容
		 */
		String getSearchContent();

		/**
		 * 搜索联系人
		 */
		void searchContacts();

		/**
		 * 搜索成功
		 * 
		 * @param list
		 *            联系人集合
		 */
		void searchSuccess(List<InnerContractsInfo> list);

		/**
		 * 搜索失敗
		 * 
		 * @param errorMessage
		 *            失败的信息
		 */
		void searchFailure(String errorMessage);

		/**
		 * 显示对话框
		 * 
		 * @param searching
		 */
		void showProgress(String searching);

		/**
		 * 隐藏对话框
		 */
		void hideProgress();

		/**
		 * 初始化搜索内容
		 */
		void initSearchContent();

		/**
		 * 添加联系人
		 */
		void addContacts();
	}

	interface Presenter extends BasePresenter {

		/**
		 * 加载数据
		 * 
		 * @param url
		 *            网址
		 * @param token
		 *            token
		 */
		void loadData(String url, String token);

		/**
		 * 获取下一级的数据
		 * 
		 * @param info
		 *            对象
		 * @param name
		 *            名字
		 * @param sum
		 *            数量
		 * @param url
		 *            网址
		 * @param token
		 *            登陆凭证
		 */
		void loadChildData(InnerContractsInfo info, String name, String sum, String url, String token);

		/**
		 * 搜索联系人
		 * 
		 * @param content
		 *            搜索内容
		 * @param url
		 *            网址
		 * @param token
		 *            凭证
		 */
		void searchContact(String content, String url, String token);

		/**
		 * 校驗搜索内容是否爲空
		 * 
		 * @param content
		 *            搜索内容
		 * @return true:空 false:不为空
		 */
		boolean checkSearchContentIsEmpty(String content);

	}
}
