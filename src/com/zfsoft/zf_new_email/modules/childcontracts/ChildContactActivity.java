package com.zfsoft.zf_new_email.modules.childcontracts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * @author wesley
 * @date 2017-2-27
 * @Description: 二级联系人界面
 */
public class ChildContactActivity extends BaseActivity {

	private int position; // 当前item在列表中的位置
	private String url; // 网址
	private InnerContractsInfo info; // 联系人对象
	private TextView tv_title; // 标题
	private ChildContactFragment fragment; // ui

	@Override
	public int getLayoutId() {
		return R.layout.activity_common;
	}

	@Override
	public void initVariables() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				position = bundle.getInt("position");
				url = bundle.getString("url");
				info = (InnerContractsInfo) bundle.getSerializable("info");
			}
		}
	}

	@Override
	public void initViews() {
		tv_title = (TextView) findViewById(R.id.include_head_title);
		tv_title.setText(R.string.add_contact);

		FragmentManager manager = getSupportFragmentManager();
		fragment = (ChildContactFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = ChildContactFragment.newInstance(info, url, position);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}
	}

	@Override
	public void initPresenter() {
		new ChildContactPresenter(fragment);
	}

	@Override
	public void onBackPressed() {
		fragment.onBackPressed();
		super.onBackPressed();
	}
}
