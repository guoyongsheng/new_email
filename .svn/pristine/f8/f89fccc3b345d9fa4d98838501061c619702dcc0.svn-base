package com.zfsoft.zf_new_email.modules.searchcontact;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;

/**
 * 
 * @author wesley
 * @date 2017-2-28
 * @Description: ui
 */
public class SearchContactFragment extends BaseFragment<SearchContactPresenter> implements SearchContactContract.View, OnItemClickListener, OnClickListener {

	private List<InnerContractsInfo> list;
	private ListView lv_list_view;
	private LinearLayout ll_back; // 返回
	private LinearLayout ll_add_contact; // 添加联系人
	private SearchContactAdapter adapter;
	private ImageView iv_add_contact; // 添加聯係人

	@Override
	public int getLayoutId() {
		return R.layout.fragment_child_contact;
	}

	@Override
	public void initVariables() {
		handleArguments();

		adapter = new SearchContactAdapter(context);
	}

	@Override
	public void initViews(View view) {
		ll_back = (LinearLayout) ((SearchContactActivity) context).findViewById(R.id.inclue_head_back_linear);
		ll_add_contact = (LinearLayout) ((SearchContactActivity) context).findViewById(R.id.include_head_home_linear);
		iv_add_contact = (ImageView) ((SearchContactActivity) context).findViewById(R.id.incluce_head_home);
		lv_list_view = (ListView) view.findViewById(R.id.list_view);
		ll_add_contact.setVisibility(View.VISIBLE);
		iv_add_contact.setImageResource(R.drawable.image_icon_yes);
		lv_list_view.setAdapter(adapter);
		adapter.notifyDataSetChanged(list);

		ll_back.setOnClickListener(this);
		ll_add_contact.setOnClickListener(this);
		lv_list_view.setOnItemClickListener(this);
	}

	public static SearchContactFragment newInstance(List<InnerContractsInfo> list) {
		SearchContactFragment fragment = new SearchContactFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) list);
		fragment.setArguments(bundle);
		return fragment;
	}

	@SuppressWarnings("unchecked")
	private void handleArguments() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			list = (List<InnerContractsInfo>) bundle.getSerializable("list");
		}
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.inclue_head_back_linear) {
			// 返回
			onbackPressed();
		} else if (key == R.id.include_head_home_linear) {
			// 添加联系人
			addContact();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.changeSelectStatus(position);
	}

	public void onbackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("type", 0);
		intent.putExtras(bundle);
		context.setResult(Activity.RESULT_OK, intent);
		context.finish();
	}

	@Override
	public void addContact() {
		List<InnerContractsInfo> list = adapter.getSelectItems();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) list);
		bundle.putInt("type", 1);
		intent.putExtras(bundle);
		context.setResult(Activity.RESULT_OK, intent);
		context.finish();
	}
}
