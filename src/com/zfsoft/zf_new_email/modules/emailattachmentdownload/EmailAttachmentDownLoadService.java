/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailattachmentdownload;

import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-13
 * @Description: 下载附件的接口
 */
public interface EmailAttachmentDownLoadService {

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
	 * 
	 * @param listener
	 *            接口回調
	 */
	void downLoadAttachment(String attachmentId, String attachmentName, String type, String url, String token, CallBackListener<Attachment> listener);
}
