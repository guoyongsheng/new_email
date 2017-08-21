/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * @author wesley
 * @date: 2016-11-22
 * @Description:邮件搜索界面
 */
public class EmailSearchActivity extends BaseActivity {

	private FragmentManager manager;
	private EmailSearchFragment fragment;
	private int type; // 0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除 5:星标邮件
	private int email_type; // 邮件类型 qq邮件 163邮件
	private int type_in_oa;

	@Override
	public int getLayoutId() {
		return R.layout.activity_email_search;
	}

	@Override
	public void initVariables() {
		manager = getSupportFragmentManager();

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				type = bundle.getInt("type");
				email_type = bundle.getInt("email_type");
				type_in_oa = bundle.getInt("type_in_oa");
			}
		}
	}

	@Override
	public void initViews() {
		fragment = (EmailSearchFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = EmailSearchFragment.newInstance(type, email_type, type_in_oa);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}
	}

	@Override
	public void initPresenter() {
		new EmailSearchPresenter(fragment);
	}
}
