package com.zfsoft.zf_new_email.modules.childcontracts;

import java.util.List;

import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.modules.selectinnercontracts.SelectInnerContactChildServiceImpl;

public class ChildContactPresenter implements ChildContactContract.Presenter {

	private ChildContactContract.View view;
	private SelectInnerContactChildServiceImpl childImpl;

	public ChildContactPresenter(ChildContactContract.View view) {
		this.view = view;
		view.setPresenter(this);
		childImpl = new SelectInnerContactChildServiceImpl();
	}

	@Override
	public void cancelRequest() {

	}

	@Override
	public void loadChildData(InnerContractsInfo info, String name, String sum, String url, String token) {

		childImpl.loadChildData(info, name, sum, url, token, new CallBackListener<List<InnerContractsInfo>>() {

			@Override
			public void onSuccess(List<InnerContractsInfo> data) {

				if (!view.isActive()) {
					return;
				}

				view.loadSuccess(data);

			}

			@Override
			public void onFailure(String errorMessage) {
				if (!view.isActive()) {
					return;
				}

				view.loadFailure(errorMessage);
			}
		});
	}
}
