/**
 * @date2016-10-19
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaildetail;


import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.modules.emaillist.EmailListMarkMailInOAServiceImpl;
import com.zfsoft.zf_new_email.modules.emaillist.EmialListUpdateInOAServiceImpl;

/**
 * @author wesley
 * @date 2016-10-19下午7:42:36
 * @Description: presenter
 */
public class EmailDetailPresenter implements EmailDetailContract.Presenter {

	private EmailDetailContract.View view;
	private EmailDetailServiceImpl impl;
	private EmailDetailInOAServiceImpl detailInOAServiceImpl;
	private EmialListUpdateInOAServiceImpl updateInOAServiceImpl;
	private EmailListMarkMailInOAServiceImpl markImpl;

	public EmailDetailPresenter(EmailDetailContract.View view) {
		this.view = view;
		view.setPresenter(this);
		this.impl = new EmailDetailServiceImpl();
		detailInOAServiceImpl = new EmailDetailInOAServiceImpl();
		updateInOAServiceImpl = new EmialListUpdateInOAServiceImpl();
		markImpl = new EmailListMarkMailInOAServiceImpl();
	}

	@Override
	public void updateMailStatus(String messageId, int messageNumber, final int status, int type, final int position) {

		impl.updateMailStatus(messageId, messageNumber, status, type, position, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.updateEmailStatus(status, position);
				view.showDeteleError(errorMessage);
			}
		});
	}

	@Override
	public void markOrUnMarkMail(String messageId, int messageNumber, final int status, int type, final int position) {

		impl.markOrUnMarkMail(messageId, messageNumber, status, type, position, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.markMailSuccess(status, position);
				view.showDeteleError(errorMessage);
			}
		});
	}

	@Override
	public void cancelRequest() {

	}

	@Override
	public void getMailDetailInOA(String messageId, String url, String token) {

		detailInOAServiceImpl.getMailDetailInOA(messageId, url, token, new CallBackListener<Email>() {

			@Override
			public void onSuccess(Email data) {
				if (!view.isActive()) {
					return;
				}

				view.showMailDetailInOA(data);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.showDeteleError(errorMessage);
			}
		});
	}

	@Override
	public void updateMialStatusInOA(String[] remarkReadItemId, String curEmailType, String url, String token, final int status, final int position) {
		updateInOAServiceImpl.updateMailStatus(remarkReadItemId, curEmailType, url, token, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.updateEmailStatus(status, position);
				view.showDeteleError(errorMessage);
			}
		});
	}

	@Override
	public void markOrUnMarkMailInOA(String[] array, final int status, final int position, String url, String token) {

		markImpl.markMailInOA(array, status, url, token, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.markMailSuccess(status, position);
				view.showDeteleError(errorMessage);
			}
		});
	}

	@Override
	public void searchEmailById(String mailId, int type, int email_type) {

		impl.searchEmailById(mailId, type, email_type, new CallBackListener<Email>() {

			@Override
			public void onSuccess(Email data) {

				if (!view.isActive()) {
					return;
				}
				
				view.showMailDetailInOA(data);
			}

			@Override
			public void onFailure(String errorMessage) {

				if (!view.isActive()) {
					return;
				}
				
				view.showDeteleError(errorMessage);
			}
		});
	}
}
