/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectcontracts;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.entity.ContractsInfo;
import com.zfsoft.zf_new_email.util.ActivityUtils;

/**
 * @author wesley
 * @date: 2016-11-26
 * @Description:选择联系人界面
 */
public class SelectContractsActivity extends BaseActivity implements OnClickListener {

	private LinearLayout ll_back; // 返回
	private LinearLayout ll_sure; // 确定
	private ImageView iv_sure; // 确定
	private TextView tv_title; // 标题
	private FragmentManager manager;
	private SelectContractsFragment fragment;
	private ArrayList<ContractsInfo> list; // 联系人集合

	@Override
	public int getLayoutId() {
		return R.layout.activity_common;
	}

	@Override
	public void initVariables() {
		manager = getSupportFragmentManager();
		handleIntent();
	}

	@Override
	public void initViews() {
		ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);
		ll_sure = (LinearLayout) findViewById(R.id.include_head_home_linear);
		iv_sure = (ImageView) findViewById(R.id.incluce_head_home);
		tv_title = (TextView) findViewById(R.id.include_head_title);
		tv_title.setText(R.string.select_contracts);
		fragment = (SelectContractsFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = SelectContractsFragment.newInstance(list);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}

		ll_sure.setVisibility(View.VISIBLE);
		iv_sure.setImageResource(R.drawable.image_icon_yes);
		ll_back.setOnClickListener(this);
		ll_sure.setOnClickListener(this);
	}

	@Override
	public void initPresenter() {
		new SelectContractsPresenter(fragment);
	}

	// 处理Intent
	@SuppressWarnings("unchecked")
	private void handleIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				list = (ArrayList<ContractsInfo>) bundle.getSerializable("list");
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
		} else if (key == R.id.include_head_home_linear) {
			fragment.setResult();
		} else {
		}
	}
}
