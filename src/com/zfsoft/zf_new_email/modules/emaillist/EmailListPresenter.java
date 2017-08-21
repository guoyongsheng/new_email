/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date 2016-10-17下午3:47:58
 * @Description: 邮件列表的presenter
 */
public class EmailListPresenter implements EmailListContract.Presenter {

	private EmailListContract.View view;
	private EmailListServiceImpl impl;
	private EmailListServeceOAImpl oaImpl;
	private EmailListDeleteOAImpl deleteOA;
	private EmialListUpdateInOAServiceImpl updateOA;
	private EmailListMarkMailInOAServiceImpl markOA;

	public EmailListPresenter(EmailListContract.View view) {
		this.view = view;
		view.setPresenter(this);
		impl = new EmailListServiceImpl();
		oaImpl = new EmailListServeceOAImpl();
		deleteOA = new EmailListDeleteOAImpl();
		updateOA = new EmialListUpdateInOAServiceImpl();
		markOA = new EmailListMarkMailInOAServiceImpl();
	}

	@Override
	public void loadData(int emailType, int startPosition, int type, final boolean isRefreshing) {

		impl.loadData(emailType, startPosition, type, isRefreshing, new CallBackListener<ArrayList<Email>>() {

			@Override
			public void onSuccess(ArrayList<Email> data) {

				if (!view.isActive()) {
					return;
				}
				if ((data == null || data.size() == 0) && isRefreshing) {
					view.showEmptyView();
				}
				view.showData(data);
			}

			@Override
			public void onFailure(String errorMessage) {

			}
		});
	}

	@Override
	public void deleteMailByMessageId(String id, int emailType, String messageId, final int position, int type, final Email email) {
		impl.deleteMailByMessageId(id, emailType, messageId, position, type, new CallBackListener<Integer>() {

			@Override
			public void onSuccess(Integer data) {
				if (!view.isActive()) {
					return;
				}
				view.updateData(data);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.showErrorMessage(errorMessage);
				view.deleteFailure(position, email);
			}
		});
	}

	@Override
	public void updateMailStatus(String messageId, int messageNumber, final int status, final int type, final int position, final Email email) {
		impl.updateMailStatus(messageId, messageNumber, status, type, position, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.showErrorMessage(errorMessage);
				switch (status) {
				/**
				 * 标为已读
				 */
				case 0:
					view.updateMailStatus(position, 1, type, email);
					break;

				/**
				 * 标为未读
				 */
				case 1:
					view.updateMailStatus(position, 0, type, email);
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void deleteMailGroundByMessageId(int emailType, ArrayList<Email> list, int type) {

		if (list == null || list.size() == 0) {
			view.showErrorMessage(Constant.PLEASE_SELECT_DELETE_MAIL);
			return;
		}

		impl.deleteMailGroundByMessageId(emailType, list, type, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {
				if (!view.isActive()) {
					return;
				}
				view.refresh();
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.refresh();
				view.showErrorMessage(errorMessage);
			}
		});
	}

	@Override
	public void updateMailGroupStatus(List<Email> list, int status, int type) {

		if (list == null || list.size() == 0) {
			view.showErrorMessage(Constant.SELECT_MAIL_PLEASE_TO_MARK);
			return;
		}

		impl.updateMailGroupStatus(list, status, type, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {
				if (!view.isActive()) {
					return;
				}
				view.refresh();
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.refresh();
				view.showErrorMessage(errorMessage);
			}
		});
	}

	@Override
	public void markOrUnMarkMail(String messageId, int messageNumber, final int status, int type, final int position, final Email email) {

		impl.markOrUnMarkMail(messageId, messageNumber, status, type, position, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.showErrorMessage(errorMessage);
				switch (status) {
				case 0:
					view.markSuccess(1, position, email);
					break;

				case 1:
					view.markSuccess(0, position, email);
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void markOrUnmardMailGroup(List<Email> list, int status, int type) {

		if (list == null || list.size() == 0) {
			view.showErrorMessage(Constant.SELECT_MAIL_PLEASE_TO_MARK);
			return;
		}

		impl.markOrUnmardMailGroup(list, status, type, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {
				if (!view.isActive()) {
					return;
				}
				view.refresh();
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.refresh();
				view.showErrorMessage(errorMessage);
			}
		});
	}

	@Override
	public void cancelRequest() {
		impl.clear();
	}

	@Override
	public void loadDataInOA(int type, int start, int size, String endpoint, String app_token, final boolean isRefresing, String cond) {
		oaImpl.loadDataInOA(type, start, size, endpoint, app_token, cond, new CallBackListener<ArrayList<Email>>() {

			@Override
			public void onSuccess(ArrayList<Email> data) {
				if (!view.isActive()) {
					return;
				}
				if ((data == null || data.size() == 0) && isRefresing) {
					view.showEmptyView();
				}
				view.showDataInOA(data);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.stopLoading();
				view.showErrorMessage(errorMessage);
			}
		});
	}

	@Override
	public void deleteMailInOA(String[] deleteItemId, String curEmailType, String url, String token, final int position, final Email email, final int deleteType) {

		deleteOA.deleteMailInOA(deleteItemId, curEmailType, url, token, position, email, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.showErrorMessage(errorMessage);
				switch (deleteType) {
				/**
				 * 刪除一封邮件
				 */
				case 0:
					view.deleteFailure(position, email);
					break;

				/**
				 * 删除多个邮件
				 */
				case 1:
					view.refresh();
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void updateMailStatusInOA(String[] remarkReadItemId, final String curEmailType, String url, String token, final int position, final Email email, final int updateType, final int status) {

		updateOA.updateMailStatus(remarkReadItemId, curEmailType, url, token, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.showErrorMessage(errorMessage);
				if (updateType == 0) {
					switch (status) {
					/**
					 * 标为已读
					 */
					case 0:
						view.updateMailStatus(position, 1, Integer.parseInt(curEmailType), email);
						break;

					/**
					 * 标为未读
					 */
					case 1:
						view.updateMailStatus(position, 0, Integer.parseInt(curEmailType), email);
						break;

					default:
						break;
					}
				} else {
					view.refresh();
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
	public void markMailInOA(String[] array, final int status, String url, String token, final int position, final Email email, final int markType) {

		markOA.markMailInOA(array, status, url, token, new CallBackListener<Boolean>() {

			@Override
			public void onSuccess(Boolean data) {

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.showErrorMessage(errorMessage);

				if (markType == 0) {
					switch (status) {
					case 0:
						view.markSuccess(1, position, email);
						break;

					case 1:
						view.markSuccess(0, position, email);
						break;

					default:
						break;
					}
				} else if (markType == 1) {
					view.refresh();
				}
			}
		});
	}
}
