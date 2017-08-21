/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emailtype;

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
 * @date 2016-10-17下午12:47:54
 * @Description: 邮件类型界面
 */
public class EmailTypeActivity extends BaseActivity implements OnClickListener {

	private EmailTypeFragment fragment;
	private int type;

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
				type = bundle.getInt("type");
			}
		}
	}

	@Override
	public void initViews() {
		TextView tv_title = (TextView) findViewById(R.id.include_head_title);
		LinearLayout ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);
		FragmentManager manager = getSupportFragmentManager();
		fragment = (EmailTypeFragment) manager.findFragmentById(R.id.include_content);
		tv_title.setText(R.string.title_email);

		if (fragment == null) {
			fragment = EmailTypeFragment.newInstance(type);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}

		ll_back.setOnClickListener(this);
	}

	@Override
	public void initPresenter() {
		new EmailTypePresenter(fragment);
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
