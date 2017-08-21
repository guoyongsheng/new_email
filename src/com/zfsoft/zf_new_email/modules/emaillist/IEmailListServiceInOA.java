/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-6
 * @Description:OA邮箱邮件列表
 */
public interface IEmailListServiceInOA {

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
	 * @param listener
	 *            接口回调
	 * 
	 */
	void loadDataInOA(int type, int start, int size, String endpoint, String app_token, String cond, CallBackListener<ArrayList<Email>> listener);
}
