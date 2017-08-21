/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailattachmentdownload;

import android.os.AsyncTask;

import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.DownLoadManager;

/**
 * @author wesley
 * @date: 2017-1-13
 * @Description:
 */
public class EmailAttachmentDownLoadPresenter implements EmailAttachmentDownLoadContract.Presenter {

	private EmailAttachmentDownLoadContract.View view;
	private EmailAttachmentDownLoadServiceImpl impl;

	public EmailAttachmentDownLoadPresenter(EmailAttachmentDownLoadContract.View view) {
		this.view = view;
		view.setPresenter(this);
		impl = new EmailAttachmentDownLoadServiceImpl();
	}

	@Override
	public void cancelRequest() {

	}

	@Override
	public void downLoadAttachment(String attachmentId, String attachmentName, String type, String url, String token) {

		view.showDialog();
		impl.downLoadAttachment(attachmentId, attachmentName, type, url, token, new CallBackListener<Attachment>() {

			@Override
			public void onSuccess(Attachment data) {
				if (!view.isActive()) {
					return;
				}

				view.hideDialog();
				view.downLoadSuccess(data);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.hideDialog();
				view.downLoadFailure(errorMessage);
			}
		});
	}

	@Override
	public void downLoadAttachment(Attachment data) {
		new DownLoadAsync().execute(data);
	}

	// 内部类
	private class DownLoadAsync extends AsyncTask<Attachment, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Attachment... params) {
			if (params != null && params.length > 0) {
				return DownLoadManager.downLoadFile(params[0]);
			}
			return false;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (!view.isActive()) {
				return;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!view.isActive()) {
				return;
			}

		}
	}
}
