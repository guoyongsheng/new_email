/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * @author wesley
 * @date: 2017-2-9
 * @Description: 内部联系人界面
 */
public class SelectInnerContactsActivity extends BaseActivity implements OnClickListener {

	private LinearLayout ll_back; // 返回
	private TextView tv_title; // 标题
	private ImageView iv_add_contact; // 添加联系人图标

	private SelectInnerContactFragment fragment;
	private FragmentManager manager;

	@Override
	public int getLayoutId() {
		return R.layout.activity_common;
	}

	@Override
	public void initVariables() {
		manager = getSupportFragmentManager();
	}

	@Override
	public void initViews() {
		ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);
		tv_title = (TextView) findViewById(R.id.include_head_title);
		iv_add_contact = (ImageView) findViewById(R.id.incluce_head_home);

		fragment = (SelectInnerContactFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = SelectInnerContactFragment.newInstance();
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}

		tv_title.setText(R.string.add_contact);
		iv_add_contact.setImageResource(R.drawable.image_icon_yes);
		ll_back.setOnClickListener(this);
	}

	@Override
	public void initPresenter() {
		new SelectInnerContactPresenter(fragment);
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
