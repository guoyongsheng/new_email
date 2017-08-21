/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailattachmentdownload;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.zfsoft.core.data.WebserviceConf;
import com.zfsoft.core.utils.FileManager;
import com.zfsoft.core.utils.PreferenceHelper;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.entity.Attachment;

/**
 * @author wesley
 * @date: 2017-1-13
 * @Description: ui展示
 */
public class EmailAttachmentDownLoadFragment extends BaseFragment<EmailAttachmentDownLoadPresenter> implements EmailAttachmentDownLoadContract.View {

	private String attachmentId; // 附件id
	private String attachmentName; // 附件名称
	private String filePath; // 附件路径
	private String attachmentSize; // 附件大小
	private ImageView iv_attachment_icon; // 附件图标
	private TextView tv_attachment_name; // 附件名称
	private TextView tv_attachment_size; // 附件大小
	private ProgressBar pb_progress; // 进度条
	private String type; // 1:oa 2:教务
	private ProgressDialog dialog;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_email_attachment_down_load;
	}

	@Override
	public void initVariables() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			attachmentId = bundle.getString("attachmentId");
			attachmentName = bundle.getString("attachmentName");
			filePath = bundle.getString("filePath");
			attachmentSize = bundle.getString("attachmentSize");
			type = bundle.getString("type", "1");
		}
	}

	@Override
	public void initViews(View view) {
		iv_attachment_icon = (ImageView) view.findViewById(R.id.attachment_down_load_icon);
		tv_attachment_name = (TextView) view.findViewById(R.id.attachment_down_load_name);
		tv_attachment_size = (TextView) view.findViewById(R.id.attachment_down_load_size);
		pb_progress = (ProgressBar) view.findViewById(R.id.attachment_down_load_progress);

		iv_attachment_icon.setImageResource(FileManager.getFileIco(attachmentName));
		tv_attachment_name.setText(attachmentName);

		downLoadAttachment(attachmentId, attachmentName, type, getUrl(), getToken());
	}

	public static EmailAttachmentDownLoadFragment newInstance(String attachmentId, String attachmentName, String filePath, String attachmentSize) {
		EmailAttachmentDownLoadFragment fragment = new EmailAttachmentDownLoadFragment();
		Bundle bundle = new Bundle();
		bundle.putString("attachmentId", attachmentId);
		bundle.putString("attachmentName", attachmentName);
		bundle.putString("filePath", filePath);
		bundle.putString("attachmentSize", attachmentSize);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void downLoadAttachment(String attachmentId, String attachmentName, String type, String url, String token) {
		presenter.downLoadAttachment(attachmentId, attachmentName, type, url, token);
	}

	@Override
	public String getUrl() {
		if (type != null && type.equals("1")) {
			return FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
		} else if (type != null && type.equals("2")) {
			return FileManager.getIp(context) + WebserviceConf.ENDPOINT_JW;
		}
		return "";
	}

	@Override
	public String getToken() {
		return PreferenceHelper.token_read(context);
	}

	@Override
	public void downLoadSuccess(Attachment data) {
		presenter.downLoadAttachment(data);
	}

	@Override
	public void downLoadFailure(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showDialog() {

		if (dialog == null) {
			dialog = new ProgressDialog(context);
		}
		dialog.setMessage(getResources().getString(R.string.down_loading));
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	@Override
	public void hideDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
