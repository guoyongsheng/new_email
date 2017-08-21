/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.util.List;

import android.text.TextUtils;

import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-2-9
 * @Description:
 */
public class SelectInnerContactPresenter implements SelectInnerContactsContracts.Presenter {

	private SelectInnerContactsContracts.View view;
	private SelectInnerContactServiceImpl impl;
	private SelectInnerContactChildServiceImpl childImpl;
	private SelectInnerContactSearchServiceImpl searchImpl;

	public SelectInnerContactPresenter(SelectInnerContactsContracts.View view) {
		this.view = view;
		view.setPresenter(this);
		impl = new SelectInnerContactServiceImpl();
		childImpl = new SelectInnerContactChildServiceImpl();
		searchImpl = new SelectInnerContactSearchServiceImpl();
	}

	@Override
	public void cancelRequest() {

	}

	@Override
	public void loadData(String url, String token) {

		view.startAnimation();
		impl.loadData(url, token, new CallBackListener<List<InnerContractsInfo>>() {

			@Override
			public void onSuccess(List<InnerContractsInfo> data) {

				if (!view.isActive()) {
					return;
				}

				view.stopAnimation();
				view.loadSuccess(data);
			}

			@Override
			public void onFailure(String errorMessage) {

				if (!view.isActive()) {
					return;
				}

				view.stopAnimation();
				view.loadFailure(errorMessage);
			}
		});
	}

	@Override
	public void loadChildData(InnerContractsInfo info, String name, String sum, String url, String token) {

		view.setChildLoading(true);
		childImpl.loadChildData(info, name, sum, url, token, new CallBackListener<List<InnerContractsInfo>>() {

			@Override
			public void onSuccess(List<InnerContractsInfo> data) {
				if (!view.isActive()) {
					return;
				}

				view.setChildLoading(false);
				view.loadChildDataSuccess(data);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.setChildLoading(false);
				view.showErrorMessage(errorMessage);
			}
		});
	}

	@Override
	public void searchContact(String content, String url, String token) {

		if (checkSearchContentIsEmpty(content)) {
			view.searchFailure(Constant.PLEASE_INPUT_SEARCH_CONTENT);
			return;
		}
		view.showProgress(Constant.SEARCHING);
		searchImpl.searchContact(content, url, token, new CallBackListener<List<InnerContractsInfo>>() {

			@Override
			public void onSuccess(List<InnerContractsInfo> data) {
				if (!view.isActive()) {
					return;
				}
				view.hideProgress();
				view.searchSuccess(data);
			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}
				view.hideProgress();
				view.searchFailure(errorMessage);
			}
		});
	}

	@Override
	public boolean checkSearchContentIsEmpty(String content) {
		return TextUtils.isEmpty(content);
	}
}
