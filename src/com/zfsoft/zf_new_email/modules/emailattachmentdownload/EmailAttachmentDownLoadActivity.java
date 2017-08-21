/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailattachmentdownload;

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
 * @date: 2017-1-13
 * @Description:附件下载
 */
public class EmailAttachmentDownLoadActivity extends BaseActivity implements OnClickListener {

	private String attachmentId; // 附件id
	private String attachmentName; // 附件名称
	private String filePath; // 附件路径
	private String attachmentSize; //附件大小
	private EmailAttachmentDownLoadFragment fragment;

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
				attachmentId = bundle.getString("attachmentId");
				attachmentName = bundle.getString("attachmentName");
				filePath = bundle.getString("filePath");
				attachmentSize = bundle.getString("attachmentSize");
			}
		}
	}

	@Override
	public void initViews() {
		LinearLayout ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);
		TextView tv_title = (TextView) findViewById(R.id.include_head_title);

		tv_title.setText(R.string.attachment_down_load);
		ll_back.setOnClickListener(this);

		FragmentManager manager = getSupportFragmentManager();
		fragment = (EmailAttachmentDownLoadFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = EmailAttachmentDownLoadFragment.newInstance(attachmentId, attachmentName, filePath, attachmentSize);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}
	}

	@Override
	public void initPresenter() {
		new EmailAttachmentDownLoadPresenter(fragment);
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
