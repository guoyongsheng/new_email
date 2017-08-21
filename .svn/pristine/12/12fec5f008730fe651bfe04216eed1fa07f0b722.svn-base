package com.zfsoft.zf_new_email.modules.childcontracts;

import java.util.List;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;

/**
 * 
 * @author wesley
 * @date 2017-2-28
 * @Description: 接口协议
 */
public interface ChildContactContract {

	interface View extends BaseView<ChildContactPresenter> {

		/**
		 * 加载子节点数据
		 */
		void loadChildData();

		/**
		 * @return 用户登录的token
		 */
		String getToken();

		/**
		 * 加载失败
		 * 
		 * @param errorMessage
		 */
		void loadFailure(String errorMessage);

		/**
		 * 加载成功
		 * 
		 * @param list
		 */
		void loadSuccess(List<InnerContractsInfo> list);
		
		/**
		 * 添加联系人
		 */
		void addContact();
	}

	interface Presenter extends BasePresenter {

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
	}

}
