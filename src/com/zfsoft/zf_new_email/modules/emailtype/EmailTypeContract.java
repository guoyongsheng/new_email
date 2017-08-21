/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emailtype;

import java.util.ArrayList;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.AccountInfo;

/**
 * @author wesley
 * @date 2016-10-17下午1:56:56
 * @Description: 协议接口
 */
public interface EmailTypeContract {

	interface View extends BaseView<EmailTypePresenter> {

		/**
		 * 保存OA邮箱的账号和密码
		 * 
		 * @param account
		 *            邮箱账号
		 * @param password
		 *            密码
		 * @param emailType
		 *            类型
		 */
		public void saveAccount(String account, String password, int emailType);

		/**
		 * 获取保存在本地的账号
		 * 
		 * @return
		 */
		public ArrayList<AccountInfo> getAccount();

		/**
		 * 登录
		 */
		void login();

		/**
		 * 登录失败
		 * 
		 * @param errorMessage
		 *            失败的信息
		 */
		void loginError(String errorMessage);

		/**
		 * 登录成功
		 * 
		 * @param account
		 *            账号
		 * @param password
		 *            密码
		 * @param emailType
		 *            类型
		 */
		void loginSuccess(String account, String password, int emailType);
	}

	interface Presenter extends BasePresenter {

		/**
		 * 获取不重复的账号集合
		 * 
		 * @param list
		 *            集合
		 * @param listSharedPreference
		 *            本地的账号
		 * @param account
		 *            账号
		 * @return 不重复的账号集合
		 */
		ArrayList<AccountInfo> getNoSmaeAccountList(ArrayList<AccountInfo> list, ArrayList<AccountInfo> listSharedPreference, String account);

		/**
		 * 登录
		 * 
		 * @param emailAccount
		 *            邮箱账号
		 * @param emailPassword
		 *            邮箱密码
		 * 
		 * @param emailType
		 *            邮箱类型
		 * 
		 */
		void login(String emailAccount, String emailPassword, int emailType);
	}
}
