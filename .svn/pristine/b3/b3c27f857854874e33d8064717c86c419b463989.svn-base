/**
 * 
 */
package com.zfsoft.zf_new_email.modules.authorizationcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * @author wesley
 * @date: 2017-1-4
 * @Description:授权码界面
 */
public class AuthorizationCodeActivity extends BaseActivity implements OnClickListener {

	private String url;
	private AuthorizationCodeFragment fragment;

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
				url = bundle.getString("url");
			}
		}
	}

	@Override
	public void initViews() {
		LinearLayout ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);
		TextView tv_title = (TextView) findViewById(R.id.include_head_title);
		tv_title.setText(R.string.open_imap_service);
		ll_back.setOnClickListener(this);

		FragmentManager manager = getSupportFragmentManager();
		fragment = (AuthorizationCodeFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = AuthorizationCodeFragment.newInstance(url);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}
	}

	@Override
	public void initPresenter() {
		new AuthorizationCodePresenter(fragment);
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.inclue_head_back_linear) {
			finish();
		}
	}
}
