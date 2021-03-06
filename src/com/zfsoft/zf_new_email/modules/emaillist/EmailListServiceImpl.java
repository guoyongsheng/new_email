/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.MailHelper;

/**
 * @author wesley
 * @date: 2016-12-14
 * @Description:邮件列表的实现类
 */
public class EmailListServiceImpl extends IEmailListService {

	private AsyncLoadData asyncLoadData;

	@Override
	public void loadData(int emailType, int startPosition, int type, boolean isRefreshing, CallBackListener<ArrayList<Email>> listener) {

		if (!checkAsyncIsCancel(asyncLoadData)) {
			cancelRequest(asyncLoadData);
		}
		asyncLoadData = new AsyncLoadData(listener);
		add(asyncLoadData);
		asyncLoadData.execute(emailType, startPosition, type);
	}

	// 获取邮件列表
	private class AsyncLoadData extends AsyncTask<Integer, Void, ArrayList<Email>> {

		private CallBackListener<ArrayList<Email>> listener;

		public AsyncLoadData(CallBackListener<ArrayList<Email>> listener) {
			this.listener = listener;
		}

		@Override
		protected ArrayList<Email> doInBackground(Integer... params) {

			if (isCancelled()) {
				return null;
			}
			int emilType = params[0];
			int startPosition = params[1];
			int type = params[2];
			if (emilType == Constant.EMAIL_TYPE_WANGYI_qq) {
				return MailHelper.getInstance().getAllMailBySSL(startPosition, type);
			} else {
				return MailHelper.getInstance().getMailReceiveListByImap(startPosition, type);
			}
		}

		@Override
		protected void onPostExecute(ArrayList<Email> result) {
			super.onPostExecute(result);
			listener.onSuccess(result);
		}
	}

	@Override
	public void deleteMailByMessageId(String id, int emailType, String messageId, int position, int type, CallBackListener<Integer> listener) {
		new AsyncDeleteMail(id, emailType, messageId, position, type, listener).execute();
	}

	// 刪除邮件
	private class AsyncDeleteMail extends AsyncTask<String, Void, Boolean> {

		private int emailType;
		private String messageId;
		private int position;
		private int type;
		private String id;
		private CallBackListener<Integer> listener;

		public AsyncDeleteMail(String id, int emailType, String messageId, int position, int type, CallBackListener<Integer> listener) {
			this.id = id;
			this.emailType = emailType;
			this.messageId = messageId;
			this.position = position;
			this.type = type;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean isDelte = false;
			if (emailType == Constant.EMAIL_TYPE_WANGYI_qq) {
				isDelte = MailHelper.getInstance().deleteMailBySSL(id, messageId, type);
			} else {
				isDelte = MailHelper.getInstance().deleteMailByMessageId(id, messageId, type);
			}
			return isDelte;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(position);
			} else {
				listener.onFailure(Constant.MAIL_DELETE_FAILURE);
			}
		}
	}

	@Override
	public void deleteMailGroundByMessageId(int emailType, ArrayList<Email> list, int type, CallBackListener<Boolean> listener) {
		new AsyncDeleteGroup(emailType, list, type, listener).execute();
	}

	// 内部类---删除多个邮件
	private class AsyncDeleteGroup extends AsyncTask<Void, Void, Boolean> {

		private int emailType;
		private ArrayList<Email> list;
		private int type;
		private CallBackListener<Boolean> listener;

		public AsyncDeleteGroup(int emailType, ArrayList<Email> list, int type, CallBackListener<Boolean> listener) {
			this.emailType = emailType;
			this.list = list;
			this.type = type;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if (emailType == Constant.EMAIL_TYPE_WANGYI_qq) {
				return MailHelper.getInstance().deleteMailGroupBySSL(list, type);
			}
			return MailHelper.getInstance().deleteMailGroupByMessageId(list, type);
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

	@Override
	public void updateMailStatus(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener) {
		new UpdateAsyn(messageId, listener).execute(messageNumber, status, type);
	}

	// 内部类---跟新邮件
	private class UpdateAsyn extends AsyncTask<Integer, Void, Boolean> {

		private CallBackListener<Boolean> listener;
		private String messageId;

		public UpdateAsyn(String messageId, CallBackListener<Boolean> listener) {
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
	public void updateMailGroupStatus(List<Email> list, int status, int type, CallBackListener<Boolean> listener) {
		new AsyncUpdateMailGroup(list, status, type, listener).execute();
	}

	// 内部类---改变邮件的状态
	private class AsyncUpdateMailGroup extends AsyncTask<Void, Void, Boolean> {

		private List<Email> list;
		private int status;
		private int type;
		private CallBackListener<Boolean> listener;

		public AsyncUpdateMailGroup(List<Email> list, int status, int type, CallBackListener<Boolean> listener) {
			this.list = list;
			this.status = status;
			this.type = type;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return MailHelper.getInstance().updateMessageGroupStatus(list, status, type);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_UPDATE_FAILURE);
			}
		}
	}

	@Override
	public void markOrUnMarkMail(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener) {
		new AsyncMark(messageId, messageNumber, status, type, position, listener).execute();
	}

	// 内部类---标记或者取消标记
	private class AsyncMark extends AsyncTask<Void, Void, Boolean> {

		private String messageId;
		private int messageNumber;
		private int status;
		private int type;
		private CallBackListener<Boolean> listener;

		public AsyncMark(String messageId, int messageNumber, int status, int type, int position, CallBackListener<Boolean> listener) {
			this.messageId = messageId;
			this.messageNumber = messageNumber;
			this.status = status;
			this.type = type;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return MailHelper.getInstance().markOrUnMarkmail(messageId, messageNumber, status, type);
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
	public void markOrUnmardMailGroup(List<Email> list, int status, int type, CallBackListener<Boolean> listener) {
		new AsyncMrakGroup(list, status, type, listener).execute();
	}

	// 内部类---标记或者取消标记
	private class AsyncMrakGroup extends AsyncTask<Void, Void, Boolean> {

		private List<Email> list;
		private int status;
		private int type;
		private CallBackListener<Boolean> listener;

		public AsyncMrakGroup(List<Email> list, int status, int type, CallBackListener<Boolean> listener) {
			this.list = list;
			this.status = status;
			this.type = type;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return MailHelper.getInstance().markOrUnMarkMailGroup(list, status, type);
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
}
