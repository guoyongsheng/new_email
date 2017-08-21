/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailattachmentdownload;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.Attachment;

/**
 * @author wesley
 * @date: 2017-1-13
 * @Description: 协议接口
 */
public interface EmailAttachmentDownLoadContract {

	interface View extends BaseView<EmailAttachmentDownLoadPresenter> {

		/**
		 * 下载附件
		 * 
		 * @param attachmentId
		 *            附件id
		 * @param attachmentName
		 *            附件名称
		 * @param type
		 *            1：oa 2：教务
		 * @param url
		 *            网址
		 * @param token
		 *            凭证
		 */
		void downLoadAttachment(String attachmentId, String attachmentName, String type, String url, String token);

		/**
		 * 获取url
		 * 
		 * @return url
		 */
		String getUrl();

		/**
		 * 获取凭证
		 * 
		 * @return token
		 */
		String getToken();

		/**
		 * 下载成功
		 * 
		 * @param data
		 *            附件
		 */
		void downLoadSuccess(Attachment data);

		/**
		 * 下载失败
		 * 
		 * @param errorMessage
		 *            失败的信息
		 */
		void downLoadFailure(String errorMessage);

		/**
		 * 显示对话框
		 */
		void showDialog();

		/**
		 * 隐藏对话框
		 */
		void hideDialog();
	}

	interface Presenter extends BasePresenter {

		/**
		 * 下载附件
		 * 
		 * @param attachmentId
		 *            附件id
		 * @param attachmentName
		 *            附件名称
		 * @param type
		 *            1：oa 2：教务
		 * @param url
		 *            网址
		 * @param token
		 *            凭证
		 */
		void downLoadAttachment(String attachmentId, String attachmentName, String type, String url, String token);

		/**
		 * 下载附件
		 * 
		 * @param data
		 *            附件
		 */
		void downLoadAttachment(Attachment data);
	}
}
