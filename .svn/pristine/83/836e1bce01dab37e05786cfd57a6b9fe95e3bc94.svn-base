/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emailtype;

import java.util.ArrayList;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.modules.checkemail.CheckEmailServiceImpl;

/**
 * @author wesley
 * @date 2016-10-17下午1:56:22
 * @Description: presenter
 */
public class EmailTypePresenter implements EmailTypeContract.Presenter {

	private EmailTypeContract.View view;
	private CheckEmailServiceImpl impl;

	public EmailTypePresenter(EmailTypeContract.View view) {
		this.view = view;
		view.setPresenter(this);
		impl = new CheckEmailServiceImpl();
	}

	@Override
	public void cancelRequest() {

	}

	@Override
	public ArrayList<AccountInfo> getNoSmaeAccountList(ArrayList<AccountInfo> list, ArrayList<AccountInfo> listSharedPreference, String account) {
		if (listSharedPreference != null && list != null) {
			int size = listSharedPreference.size();
			for (int i = 0; i < size; i++) {
				String value = listSharedPreference.get(i).getAccount();
				if (value != null && value.equals(account)) {
					listSharedPreference.remove(i);
					break;
				}
			}
			list.addAll(listSharedPreference);
		}
		return list;
	}

	@Override
	public void login(final String emailAccount, final String emailPassword, final int emailType) {

		impl.login(emailAccount, emailPassword, String.valueOf(emailType), new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {
				if (!view.isActive()) {
					return;
				}
				view.loginSuccess(emailAccount, emailPassword, emailType);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.loginError(errorMessage);
			}
		});
	}
}
