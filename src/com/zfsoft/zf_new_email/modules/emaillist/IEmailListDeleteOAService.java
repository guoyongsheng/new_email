/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-9
 * @Description:删除OA邮件的接口
 */
public interface IEmailListDeleteOAService {

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
	 * @param listener
	 *            回调接口
	 */
	void deleteMailInOA(String[] deleteItemId, String curEmailType, String url, String token, int position, Email email, CallBackListener<Boolean> listener);
}
