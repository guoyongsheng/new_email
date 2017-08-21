/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.MailHelper;

/**
 * @author wesley
 * @date: 2016-12-16
 * @Description:接口实现
 */
public class EmailSendOrReplyImpl implements IEmailSendOrReplyService {

	@Override
	public void sendEmail(List<String> listReceiver, String senderName, String subject, List<Attachment> listAttachmenet, String content, boolean isDraft, CallBackListener<Boolean> listener) {
		new AsyncSendEmail(listReceiver, senderName, subject, listAttachmenet, content, 1, isDraft, listener).execute();
	}

	// 发送邮件
	private class AsyncSendEmail extends AsyncTask<Void, Void, Boolean> {

		private int loginFrom;
		private List<String> listReceiver;
		private String senderName;
		private String subject;
		private List<Attachment> listAttachmenet;
		private String content;
		private boolean isDraft;
		private CallBackListener<Boolean> listener;

		public AsyncSendEmail(List<String> listReceiver, String senderName, String subject, List<Attachment> listAttachmenet, String content, int loginFrom, boolean isDraft, CallBackListener<Boolean> listener) {
			this.loginFrom = loginFrom;
			this.listReceiver = listReceiver;
			this.subject = subject;
			this.listAttachmenet = listAttachmenet;
			this.content = content;
			this.isDraft = isDraft;
			this.senderName = senderName;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			MailInfo mailInfo = new MailInfo();
			int sizeReceiver = listReceiver.size();
			String[] receive = new String[sizeReceiver];
			for (int i = 0; i < sizeReceiver; i++) {
				receive[i] = listReceiver.get(i);
			}
			mailInfo.setReceivers(receive);
			mailInfo.setUserName(senderName);
			mailInfo.setSubject(subject);
			mailInfo.setContent(content);
			mailInfo.setAttachmentInfos(listAttachmenet);
			return MailHelper.getInstance().sendTextMail(mailInfo, loginFrom, isDraft);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure("");
			}
		}
	}

	@Override
	public void replyEmail(String messageId, int messageNumber, String subject, String content, int type, boolean isDraft, int inbox_type, ArrayList<String> receiveList, List<Attachment> listAttachment, CallBackListener<Boolean> listener) {
		new AsyncReply(messageId, receiveList, listAttachment, isDraft, listener).execute(String.valueOf(messageNumber), subject, content, String.valueOf(type), String.valueOf(inbox_type));
	}

	// 回复邮件
	private class AsyncReply extends AsyncTask<String, Void, Boolean> {

		private boolean isDraft;
		private ArrayList<String> list;
		private List<Attachment> listAttachment;
		private String messageId;
		private CallBackListener<Boolean> listener;

		public AsyncReply(String messageId, ArrayList<String> list, List<Attachment> listAttachment, boolean isDraft, CallBackListener<Boolean> listener) {
			this.list = list;
			this.listAttachment = listAttachment;
			this.isDraft = isDraft;
			this.messageId = messageId;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return MailHelper.getInstance().replyMail(messageId, params[0], params[1], params[2], Integer.parseInt(params[3]), Integer.parseInt(params[4]), list, listAttachment, isDraft);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(result);
			} else {
				String message = null;
				if (isDraft) {
					message = Constant.MAIL_SAVE_FAILURE;
				} else {
					message = Constant.MAIL_REPLY_FAILURE;
				}
				listener.onFailure(message);
			}
		}
	}

	@Override
	public void forwardEmail(String messageId, int messageNumber, String subject, String content, int type, boolean isDraft, ArrayList<String> forwardTo, List<Attachment> listAttachment, CallBackListener<Boolean> listener) {
		new AsyncForward(messageId, isDraft, forwardTo, listAttachment, listener).execute(String.valueOf(messageNumber), subject, content, String.valueOf(type));
	}

	// 转发邮件
	private class AsyncForward extends AsyncTask<String, Void, Boolean> {
		private String messageId;
		private boolean isDraft;
		private ArrayList<String> list;
		private List<Attachment> listAttachment;
		private CallBackListener<Boolean> listener;

		public AsyncForward(String messageId, boolean isDraft, ArrayList<String> list, List<Attachment> listAttachment, CallBackListener<Boolean> listener) {
			this.messageId = messageId;
			this.isDraft = isDraft;
			this.list = list;
			this.listAttachment = listAttachment;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return MailHelper.getInstance().forwardMail(messageId, params[0], params[1], params[2], Integer.parseInt(params[3]), isDraft, list, listAttachment);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				listener.onSuccess(result);
			} else {

				String message = null;
				if (isDraft) {
					message = Constant.MAIL_SAVE_FAILURE;
				} else {
					message = Constant.MAIL_FORWARD_FAILURE;
				}
				listener.onFailure(message);
			}
		}
	}
}
