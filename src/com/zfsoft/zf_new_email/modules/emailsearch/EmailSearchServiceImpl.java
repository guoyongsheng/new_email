/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsearch;

import java.util.List;

import android.os.AsyncTask;

import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.MailHelper;

/**
 * @author wesley
 * @date: 2016-12-15
 * @Description:接口实现
 */
public class EmailSearchServiceImpl extends IEmailSearchService {

	private AsyncSearch asyncSearch;

	@Override
	public void searchMail(int startPosition, int type, int status, String content, boolean isRefreshing, CallBackListener<List<Email>> listener) {

		if (!checkAsyncIsCancel(asyncSearch)) {
			cancelRequest(asyncSearch);
		}
		asyncSearch = new AsyncSearch(startPosition, type, status, content, isRefreshing, listener);
		add(asyncSearch);
		asyncSearch.execute();
	}

	// 内部类---搜索
	private class AsyncSearch extends AsyncTask<Void, Void, List<Email>> {

		private int type;
		private int status;
		private int startPosition;
		private String content;
		private CallBackListener<List<Email>> listener;

		public AsyncSearch(int startPosition, int type, int status, String content, boolean isRefreshing, CallBackListener<List<Email>> listener) {
			this.startPosition = startPosition;
			this.type = type;
			this.status = status;
			this.content = content;
			this.listener = listener;
		}

		@Override
		protected List<Email> doInBackground(Void... params) {
			if (isCancelled()) {
				return null;
			}
			return MailHelper.getInstance().searchMail(startPosition, type, status, content);
		}

		@Override
		protected void onPostExecute(List<Email> result) {
			super.onPostExecute(result);
			listener.onSuccess(result);
		}
	}

	@Override
	public void deleteMialInSearch(String messageId, int emailType, int messageNumber, int position, int type, Email email, int status, String content, int startPosition, CallBackListener<Boolean> listener) {
		new AsyncDelete(messageId, emailType, messageNumber, type, status, content, startPosition, listener).execute();
	}

	// 内部类---删除
	private class AsyncDelete extends AsyncTask<Void, Void, Boolean> {

		private String messageId;
		private int emailType;
		private int messageNumber;
		private int type;
		private int status;
		private String content;
		private int startPosition;
		private CallBackListener<Boolean> listener;

		public AsyncDelete(String messageId, int emailType, int messageNumber, int type, int status, String content, int startPosition, CallBackListener<Boolean> listener) {
			this.messageId = messageId;
			this.emailType = emailType;
			this.messageNumber = messageNumber;
			this.type = type;
			this.status = status;
			this.content = content;
			this.startPosition = startPosition;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if (emailType == Constant.EMAIL_TYPE_WANGYI_qq) {
				return MailHelper.getInstance().deleteMialInSearchBySSL(messageId, messageNumber, type, status, content, startPosition);
			}
			return MailHelper.getInstance().deleteMialInSearchByImap(messageId, messageNumber, type, status, content, startPosition, emailType);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_DELETE_FAILURE);
			}
		}
	}
}
