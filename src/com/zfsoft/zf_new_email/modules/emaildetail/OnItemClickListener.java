/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaildetail;

/**
 * @author wesley
 * @date: 2017-1-5
 * @Description: 附件listview的item的点击事件
 */
public interface OnItemClickListener {

	void onItemClick(String path);

	void onItemClick(String attachmentId, String attachmentName, String attachmentSize);
}
