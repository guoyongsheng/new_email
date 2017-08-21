/**
 * 
 */
package com.zfsoft.zf_new_email.modules.imagepreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.util.GlideUtils;
import com.zfsoft.zf_new_email.util.SizeUtils;

/**
 * @author wesley
 * @date: 2017-1-5
 * @Description: 图片预览界面
 */
public class ImagePreviewActivity extends BaseActivity implements OnClickListener {

	private String path; // 路径
	private ImageView iv_attachment; // 圖片

	@Override
	public int getLayoutId() {
		return R.layout.activity_image_preview;
	}

	@Override
	public void initVariables() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				path = bundle.getString("path");
			}
		}
	}

	@Override
	public void initViews() {
		iv_attachment = (ImageView) findViewById(R.id.image_preview);
		int width = SizeUtils.getScreenWidth(this);
		LayoutParams params = new LayoutParams(width, width);
		iv_attachment.setLayoutParams(params);
		GlideUtils.loadImage(path, iv_attachment, this);

		iv_attachment.setOnClickListener(this);
	}

	@Override
	public void initPresenter() {

	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
