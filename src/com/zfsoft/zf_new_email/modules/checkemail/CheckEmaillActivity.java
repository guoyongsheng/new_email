/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.checkemail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * @author wesley
 * @date 2016-10-17下午7:18:20
 * @Description: 邮箱校验界面
 */
public class CheckEmaillActivity extends BaseActivity implements OnClickListener {

	private int email_type; // 邮件类型
	private CheckEmailFragment fragment;

	@Override
	public int getLayoutId() {
		return R.layout.activity_common;
	}

	@Override
	public void initVariables() {
		handleIntent();
	}

	@Override
	public void initViews() {
		TextView tv_title = (TextView) findViewById(R.id.include_head_title);
		LinearLayout ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);

		tv_title.setText(R.string.login);
		ll_back.setOnClickListener(this);

		FragmentManager manager = getSupportFragmentManager();
		fragment = (CheckEmailFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = CheckEmailFragment.newInstance(email_type);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}
	}

	@Override
	public void initPresenter() {
		new CheckEmailPresenter(fragment);
	}

	// 处理intent
	private void handleIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				email_type = bundle.getInt(Constant.EMAIL_TYPE);
			}
		}
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
