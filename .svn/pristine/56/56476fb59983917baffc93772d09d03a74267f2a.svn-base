package com.zfsoft.zf_new_email.modules.childcontracts;

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
import android.widget.Toast;

import com.zfsoft.core.utils.PreferenceHelper;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;

/**
 * @author wesley
 * @date 2017-2-28
 * @Description: ui展示
 */
public class ChildContactFragment extends BaseFragment<ChildContactPresenter> implements ChildContactContract.View, OnClickListener, OnItemClickListener {

	private String url; // 网址
	private InnerContractsInfo info;

	private LinearLayout ll_back; // 返回
	private LinearLayout ll_add_contact; // 添加联系人
	private ImageView iv_add_contact; // 添加聯係人
	private ListView lv_list_view;
	private ChildContactAdapter adapter; // 適配器

	@Override
	public int getLayoutId() {
		return R.layout.fragment_child_contact;
	}

	@Override
	public void initVariables() {
		handleArguments();

		adapter = new ChildContactAdapter(context);
	}

	@Override
	public void initViews(View view) {
		ll_back = (LinearLayout) ((ChildContactActivity) context).findViewById(R.id.inclue_head_back_linear);
		ll_add_contact = (LinearLayout) ((ChildContactActivity) context).findViewById(R.id.include_head_home_linear);
		iv_add_contact = (ImageView) ((ChildContactActivity) context).findViewById(R.id.incluce_head_home);
		lv_list_view = (ListView) view.findViewById(R.id.list_view);
		lv_list_view.setAdapter(adapter);

		ll_add_contact.setVisibility(View.VISIBLE);
		iv_add_contact.setImageResource(R.drawable.image_icon_yes);
		ll_back.setOnClickListener(this);
		ll_add_contact.setOnClickListener(this);
		lv_list_view.setOnItemClickListener(this);

		loadChildData();
	}

	/**
	 * 实例化对象
	 * 
	 * @param info
	 *            联系人对象
	 * @param url
	 *            网址
	 * @param position
	 *            item在列表中的位置
	 * @return 当前对象
	 */
	public static ChildContactFragment newInstance(InnerContractsInfo info, String url, int position) {
		ChildContactFragment fragment = new ChildContactFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putString("url", url);
		bundle.putSerializable("info", info);
		fragment.setArguments(bundle);
		return fragment;
	}

	// 处理参数
	private void handleArguments() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			//position = bundle.getInt("position");
			url = bundle.getString("url");
			info = (InnerContractsInfo) bundle.getSerializable("info");
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
			onBackPressed();
		} else if (key == R.id.include_head_home_linear) {
			// 添加联系人
			addContact();
		}
	}

	@Override
	public void loadChildData() {
		if (info == null) {
			return;
		}
		presenter.loadChildData(info, info.getName(), info.getSum(), url, getToken());
	}

	@Override
	public String getToken() {
		return PreferenceHelper.token_read(context);
	}

	@Override
	public void loadFailure(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void loadSuccess(List<InnerContractsInfo> list) {
		adapter.notifyDataSetChanged(list);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.changeSelectStatus(position);
	}

	public void onBackPressed() {
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
