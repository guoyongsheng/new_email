/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.checkemail;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.ResponseInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.EmailFormatUtils;
import android.text.TextUtils;

/**
 * @author wesley
 * @date 2016-10-17下午7:29:59
 * @Description: presenter
 */
public class CheckEmailPresenter implements CheckEmailContract.Presenter {

	private CheckEmailContract.View view;
	private CheckEmailServiceImpl impl;

	public CheckEmailPresenter(CheckEmailContract.View view) {
		this.view = view;
		view.setPresenter(this);
		impl = new CheckEmailServiceImpl();
	}

	@Override
	public boolean checkEmailAccountIsEmpty(String emailAccount) {
		return TextUtils.isEmpty(emailAccount) ? true : false;
	}

	@Override
	public boolean checkEmailAccountIsOk(String emailAccount, int emailType) {

		String emailFormat = null;
		switch (emailType) {
		/**
		 * 163邮箱
		 */
		case 1:
			emailFormat = "163";
			break;

		/**
		 * 126邮箱
		 */
		case 2:
			emailFormat = "126";
			break;

		/**
		 * 新浪
		 */
		case 3:
			emailFormat = "sina";
			break;

		/**
		 * qq邮箱
		 */
		case 4:
			emailFormat = "qq";
			break;

		/**
		 * 搜狐
		 */
		case 5:
			emailFormat = "sohu";
			break;

		default:
			break;
		}
		//return EmailFormatUtils.checkEmailIsOk(emailAccount, emailFormat) ? true : false;
		
		return true;
	}

	@Override
	public boolean checkEmailPassIsEmpty(String emailPassword) {
		return TextUtils.isEmpty(emailPassword) ? true : false;
	}

	@Override
	public void login(final String emailAccount, final String emailPassword, final int emailType) {

		if (checkEmailAccountIsEmpty(emailAccount)) {
			view.showErrorMessage(Constant.EMAIL_ACCOUNT_IS_NOT_EMPTY);
			return;
		}

		if (!checkEmailAccountIsOk(emailAccount, emailType)) {
			view.showErrorMessage(Constant.EMAIL_ACCOUNT_FORMART_IS_ERROR);
			return;
		}

		if (checkEmailPassIsEmpty(emailPassword)) {
			view.showErrorMessage(Constant.EMAIL_PASSWORD_IS_NOT_EMPTY);
			return;
		}

		view.showProgressDialog();
		impl.login(emailAccount, emailPassword, String.valueOf(emailType), new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {
				if (!view.isActive()) {
					return;
				}
				view.hideProgressDialog();
				view.saveAccount(emailAccount, emailPassword, emailType);
				view.loginSuccess();
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.hideProgressDialog();
				Gson gson = new Gson();
				ResponseInfo responseInfo = gson.fromJson(errorMessage, ResponseInfo.class);
				if (responseInfo != null) {
					int auCode = responseInfo.getAuthorizationCode();
					switch (auCode) {
					case 1:
						view.loginError(Constant.LOGIN_WITH_AUTHORIZETION_CODE);
						break;

					case Constant.EMAIL_TYPE_WANGYI_qq:
						view.showDialog(responseInfo.getUrl());
						break;

					default:
						view.loginError(Constant.LOGIN_ERROR);
						break;
					}
				}
			}
		});
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
	public void cancelRequest() {
		impl.clear();
	}
}
