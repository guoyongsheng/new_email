/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfsoft.core.data.WebserviceConf;
import com.zfsoft.core.utils.FileManager;
import com.zfsoft.core.utils.PreferenceHelper;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.modules.childcontracts.ChildContactActivity;
import com.zfsoft.zf_new_email.modules.searchcontact.SearchContactActivity;

/**
 * @author wesley
 * @date: 2017-2-9
 * @Description: ui展示
 */
public class SelectInnerContactFragment extends BaseFragment<SelectInnerContactPresenter> implements SelectInnerContactsContracts.View, OnClickListener, OnViewClickListener {

	private static final int CHILD_ACTIVITY_REQUEST_CODE = 1;
	private static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
	private LinearLayout ll_add_contact; // 添加联系人
	private LinearLayout ll_loading; // loading的布局
	private ImageView iv_loading; // loading的图片
	private AnimationDrawable animation; // 动画
	private TextView tv_loading; // loading的文字
	private LinearLayout ll_contact; // 联系人的布局
	private ListView lv_list_view; // listview
	private SelectInnerContactAdapter adapter; // 适配器
	private boolean isLoading; // 是否正在加载
	private boolean isChildLoading; // 子节点是否正在加载
	private boolean isSearching; // 是否正在搜索

	private EditText et_content; // 输入的内容
	private ImageButton ibt_search; // 搜索

	private ProgressDialog dialog;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_select_inner_contact;
	}

	@Override
	public void initVariables() {
		adapter = new SelectInnerContactAdapter(context);
		adapter.setOnViewClickListener(this);
	}

	@Override
	public void initViews(View view) {
		ll_add_contact = (LinearLayout) ((SelectInnerContactsActivity) context).findViewById(R.id.include_head_home_linear);
		ll_loading = (LinearLayout) view.findViewById(R.id.fragment_inner_contact_loading);
		iv_loading = (ImageView) view.findViewById(R.id.fragment_inner_contact_image_loading);
		tv_loading = (TextView) view.findViewById(R.id.fragment_inner_contact_tv_loading);
		ll_contact = (LinearLayout) view.findViewById(R.id.fragment_inner_contact_content);
		lv_list_view = (ListView) view.findViewById(R.id.fragment_inner_list_view);
		et_content = (EditText) view.findViewById(R.id.fragment_edit_content);
		ibt_search = (ImageButton) view.findViewById(R.id.fragment_inner_search);

		lv_list_view.setAdapter(adapter);
		animation = (AnimationDrawable) iv_loading.getBackground();
		ll_add_contact.setVisibility(View.VISIBLE);
		ll_add_contact.setOnClickListener(this);
		ll_loading.setOnClickListener(this);
		ibt_search.setOnClickListener(this);

		loadData(getUrl(), getToken());
	}

	public static SelectInnerContactFragment newInstance() {
		return new SelectInnerContactFragment();
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.include_head_home_linear) {
			// 添加联系人
			addContacts();
		} else if (key == R.id.fragment_inner_contact_loading) {
			// 重新加载
			loadData(getUrl(), getToken());
		} else if (key == R.id.fragment_inner_search) {
			// 搜索
			searchContacts();
		}
	}

	@Override
	public String getUrl() {
		return FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
	}

	@Override
	public String getToken() {
		return PreferenceHelper.token_read(context);
	}

	@Override
	public void loadData(String url, String token) {
		if (isLoading) {
			return;
		}
		presenter.loadData(url, token);
	}

	@Override
	public void startAnimation() {

		isLoading = true;
		initLoading();
		if (animation != null) {
			animation.start();
		}
	}

	@Override
	public void stopAnimation() {

		isLoading = false;
		if (animation != null) {
			animation.stop();
		}
	}

	@Override
	public void loadSuccess(List<InnerContractsInfo> list) {
		tv_loading.setText(R.string.loading);
		ll_loading.setVisibility(View.GONE);
		ll_contact.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged(list);
	}

	@Override
	public void loadFailure(String errorMessage) {
		initContent();
	}

	@Override
	public void initLoading() {
		iv_loading.setVisibility(View.VISIBLE);
		tv_loading.setText(R.string.loading);
		ll_loading.setVisibility(View.VISIBLE);
		ll_contact.setVisibility(View.GONE);
	}

	@Override
	public void initContent() {
		iv_loading.setVisibility(View.GONE);
		tv_loading.setText(R.string.loading_again);
		ll_loading.setVisibility(View.VISIBLE);
		ll_contact.setVisibility(View.GONE);
	}

	@Override
	public void onViewClick(int position) {
		adapter.changeSelectStatus(position);
	}

	@Override
	public void onItemClick(int position) {
		if (adapter.isCanExpand(position)) {
			if (adapter.isExpanded(position)) {
				adapter.setItemIsExpanded(position, false);
				adapter.setNotExpanded(position);
			} else {
				adapter.setItemIsExpanded(position, true);
				InnerContractsInfo info = adapter.getItemValue(position);
				if (info != null) {
					boolean isHasRootNote = info.isHasRootNode();
					if (isHasRootNote) {
						adapter.setItemIsExpanded(position, false);
						jumpToChildActivity(info, position);
					} else {
						loadChildData(info, info.getName(), info.getSum(), getUrl(), getToken());
					}
				}
			}
		} else {
			adapter.changeSelectStatus(position);
		}
	}

	@Override
	public void loadChildData(InnerContractsInfo info, String name, String sum, String url, String token) {
		if (isChildLoading) {
			return;
		}
		presenter.loadChildData(info, name, sum, url, token);
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void loadChildDataSuccess(List<InnerContractsInfo> list) {
		int currentPosition = adapter.getCurrentPosition(list);
		if (currentPosition != -1) {
			adapter.insertChildData(list, currentPosition);
		}
	}

	@Override
	public void setChildLoading(boolean isChildLoading) {
		this.isChildLoading = isChildLoading;
	}

	// 跳转到二级界面
	private void jumpToChildActivity(InnerContractsInfo info, int position) {
		Intent intent = new Intent(context, ChildContactActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", getUrl());
		bundle.putSerializable("info", info);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == CHILD_ACTIVITY_REQUEST_CODE || requestCode == SEARCH_ACTIVITY_REQUEST_CODE) && resultCode == Activity.RESULT_OK) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					int type = bundle.getInt("type");
					if (type == 0) {
						context.finish();
					} else {
						Intent intent = new Intent();
						intent.putExtras(bundle);
						context.setResult(resultCode, intent);
						context.finish();
					}
				}
			}
		}
	}

	@Override
	public String getSearchContent() {
		return et_content.getText().toString();
	}

	@Override
	public void searchContacts() {
		if (isSearching) {
			return;
		}
		isSearching = true;
		presenter.searchContact(getSearchContent(), getUrl(), getToken());
	}

	@Override
	public void searchSuccess(List<InnerContractsInfo> list) {
		isSearching = false;
		initSearchContent();
		Intent intent = new Intent(context, SearchContactActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) list);
		intent.putExtras(bundle);
		startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void searchFailure(String errorMessage) {
		isSearching = false;
		initSearchContent();
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showProgress(String searching) {

		if (dialog == null) {
			dialog = new ProgressDialog(context);
		}
		dialog.setMessage(searching);
		dialog.show();
	}

	@Override
	public void hideProgress() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void initSearchContent() {
		et_content.setText("");
	}

	@Override
	public void addContacts() {
		List<InnerContractsInfo> list = adapter.getSelectItems();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) list);
		intent.putExtras(bundle);
		context.setResult(Activity.RESULT_OK, intent);
		context.finish();
	}
}
