/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectcontracts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.ContractsInfo;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;

/**
 * @author wesley
 * @date: 2016-11-26
 * @Description: ui展示
 */
public class SelectContractsFragment extends BaseFragment<SelectContractsPresenter> implements SelectContractsContract.View {

	private ArrayList<ContractsInfo> listSelect; // 选中的联系人集合
	private ListView listView;
	private SelectContractsAdapter adapter;
	private ArrayList<ContractsInfo> list; // 所有的联系人集合
	private LinearLayout ll_empty_view; // 沒有联系人时的view
	private TextView tv_no_contracts; // 没有联系人

	@Override
	public int getLayoutId() {
		return R.layout.fragment_select_contracts;
	}

	@Override
	public void initVariables() {
		handleIntent();
		list = getSharedPreferenceValue();
		adapter = new SelectContractsAdapter(context);
	}

	@Override
	public void initViews(View view) {
		listView = (ListView) view.findViewById(R.id.select_contracts_list);
		ll_empty_view = (LinearLayout) view.findViewById(R.id.common_empty_view);
		tv_no_contracts = (TextView) view.findViewById(R.id.commom_no_value);
		listView.setAdapter(adapter);
		adapter.setDataSource(presenter.getContractsInfoList(list, listSelect));
		if (list == null || list.size() == 0) {
			tv_no_contracts.setText(R.string.no_contracts);
			ll_empty_view.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
	}

	public static SelectContractsFragment newInstance(ArrayList<ContractsInfo> list) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", list);
		SelectContractsFragment fragment = new SelectContractsFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	// 处理intent
	@SuppressWarnings("unchecked")
	public void handleIntent() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			listSelect = (ArrayList<ContractsInfo>) bundle.getSerializable("list");
		}
	}

	/**
	 * 获取所有的联系人
	 * 
	 * @return 所有联系人
	 */
	public ArrayList<ContractsInfo> getSharedPreferenceValue() {
		MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(context);
		if (mailInfo != null) {
			String name = mailInfo.getUserName();
			Type type = new TypeToken<ArrayList<ContractsInfo>>() {
			}.getType();
			return SharedPreferencedUtis.getValue(context, Constant.NAME, Constant.KEY + name, type);
		}
		return null;
	}

	public void setResult() {
		ArrayList<ContractsInfo> listSharedPreference = getSharedPreferenceValue();
		ArrayList<ContractsInfo> list_selected = presenter.getSelectedContractsList(list);
		boolean isContainsAll = presenter.contractsListContainsInputContracts(listSharedPreference, listSelect);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		if (isContainsAll && context != null) {
			bundle.putSerializable("list", list_selected);
			intent.putExtras(bundle);
			context.setResult(Constant.REQUEST_CONDE_SELECT_CONTRACTS_BACK_SEND, intent);
			context.finish();
		} else if (presenter.contractsListNotContainsInputContracts(listSharedPreference, listSelect) && context != null) {
			list_selected.addAll(0, listSelect);
			bundle.putSerializable("list", list_selected);
			intent.putExtras(bundle);
			context.setResult(Constant.REQUEST_CONDE_SELECT_CONTRACTS_BACK_SEND, intent);
			context.finish();
		} else if (context != null) {
			list_selected.addAll(0, presenter.getNotContainsList(listSharedPreference, listSelect));
			bundle.putSerializable("list", list_selected);
			intent.putExtras(bundle);
			context.setResult(Constant.REQUEST_CONDE_SELECT_CONTRACTS_BACK_SEND, intent);
			context.finish();
		}
	}
}
