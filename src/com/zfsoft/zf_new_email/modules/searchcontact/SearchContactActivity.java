package com.zfsoft.zf_new_email.modules.searchcontact;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * 搜索联系人界面
 * 
 * @author wesley
 * @date 2017-2-28
 * @Description:
 */
public class SearchContactActivity extends BaseActivity {

	private List<InnerContractsInfo> list;
	private TextView tv_title; // 标题
	private SearchContactFragment fragment;

	@Override
	public int getLayoutId() {
		return R.layout.activity_common;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initVariables() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				list = (List<InnerContractsInfo>) bundle.getSerializable("list");
			}
		}
	}

	@Override
	public void initViews() {
		tv_title = (TextView) findViewById(R.id.include_head_title);
		tv_title.setText(R.string.add_contact);

		FragmentManager manager = getSupportFragmentManager();
		fragment = (SearchContactFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = SearchContactFragment.newInstance(list);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}
	}

	@Override
	public void initPresenter() {
		new SearchContactPresenter(fragment);
	}
	
	@Override
	public void onBackPressed() {
		fragment.onbackPressed();
		super.onBackPressed();
	}
}
