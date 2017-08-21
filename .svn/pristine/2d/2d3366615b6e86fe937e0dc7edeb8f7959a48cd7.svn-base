/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaildetail;

import android.os.AsyncTask;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.MailHelper;

/**
 * @author wesley
 * @date: 2016-12-16
 * @Description:接口实现
 */
public class EmailDetailServiceImpl implements IEmailDeleteService {

	@Override
	public void markOrUnMarkMail(String messageid, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener) {
		new AsyncMarkMail(messageid, status, listener).execute(messageNumber, status, type);
	}

	// 内部类---标记
	private class AsyncMarkMail extends AsyncTask<Integer, Void, Boolean> {

		private String messageId;
		private CallBackListener<Boolean> listener;

		public AsyncMarkMail(String messageId, int status, CallBackListener<Boolean> listener) {
			this.listener = listener;
			this.messageId = messageId;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			return MailHelper.getInstance().markOrUnMarkmail(messageId, params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_MARKING_FAILURE);
			}
		}
	}

	@Override
	public void updateMailStatus(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener) {
		new UpdateAsyn(messageId, status, listener).execute(messageNumber, status, type);
	}

	// 内部类---跟新邮件
	private class UpdateAsyn extends AsyncTask<Integer, Void, Boolean> {

		private CallBackListener<Boolean> listener;
		private String messageId;

		public UpdateAsyn(String messageId, int status, CallBackListener<Boolean> listener) {
			this.listener = listener;
			this.messageId = messageId;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			return MailHelper.getInstance().updateMessageStatus(messageId, params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_MARKING_FAILURE);
			}
		}
	}

	@Override
	public void searchEmailById(String mailId, int type, int email_type, CallBackListener<Email> listener) {
		new SearchAsync(mailId, type, email_type, listener).execute();
	}

	// 搜索邮件详情
	private class SearchAsync extends AsyncTask<Void, Void, Email> {

		private String mailId;
		private int type;
		private int email_type;
		private CallBackListener<Email> listener;

		public SearchAsync(String mailId, int type, int email_type, CallBackListener<Email> listener) {
			this.mailId = mailId;
			this.type = type;
			this.email_type = email_type;
			this.listener = listener;
		}

		@Override
		protected Email doInBackground(Void... params) {
			if (email_type == Constant.EMAIL_TYPE_WANGYI_qq) {
				return MailHelper.getInstance().searchMailByIdWithSSL(mailId, type);
			}
			return MailHelper.getInstance().searchMailById(mailId, type);
		}

		@Override
		protected void onPostExecute(Email result) {
			super.onPostExecute(result);

			if (result == null) {
				listener.onFailure(Constant.MAIL_DETAIL_GET_FAILURE);
				return;
			}

			listener.onSuccess(result);
		}
	}

}
