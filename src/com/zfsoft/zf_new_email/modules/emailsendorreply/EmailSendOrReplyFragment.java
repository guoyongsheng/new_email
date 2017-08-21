/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.zfsoft.core.data.WebserviceConf;
import com.zfsoft.core.utils.FileManager;
import com.zfsoft.core.utils.PreferenceHelper;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.entity.ContractsInfo;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.modules.selectcontracts.SelectContractsActivity;
import com.zfsoft.zf_new_email.modules.selectinnercontracts.SelectInnerContactsActivity;
import com.zfsoft.zf_new_email.util.MailHelper;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;
import com.zfsoft.zf_new_email.widget.tagseditText.TagsEditText;
import com.zfsoft.zf_new_email.widget.tagseditText.TagsEditText.TagsEditListener;

/**
 * @author wesley
 * @date: 2016-10-24
 * @Description: fragment
 */
public class EmailSendOrReplyFragment extends BaseFragment<EmailSendOrReplyPresenter> implements OnClickListener, EmailSendOrReplyContract.View, OnItemClickListener, TagsEditListener {

	private static final int INNER_CONTACT_REQUEST_CODE = 1;
	private TagsEditText tv_addressee; // 收件人
	private TextView tv_cc; // 抄送人
	private EditText et_subject; // 主題
	private View viewLine_no_attachment; // 沒有附件时不显示
	private LinearLayout ll_no_attachment; // 没有附件时不显示
	private TextView tv_attachment_number; // 附件的数量
	private TextView tv_attachment_size; // 附件大小
	private ImageView iv_attachment_arrow; // 附件的下拉箭头
	private ListView lv_list_view; // 附件列表
	private TextView tv_other_attachment; // 其他附件
	private View viewLine_attachment; // 有附件时不显示
	private EditText et_content; // 内容
	private LinearLayout ll_send; // 发送
	private ImageView iv_send; // 发送的图标
	private LinearLayout ll_back; // 返回
	private ImageButton ibn_add_attachment; // 添加附件
	private EmailSendOrReplyAdapter adapter; // 适配器
	private ProgressDialog dialog; // 对话框
	private int type; // 类型 0:写邮件 1:回复 2:回复全部 3:转发 4:回复带附件 5:转发带附件 6:回复全部带附件
	private Email email; // 邮件对象
	private AlertDialog dialog_draft; // 草稿箱的对话框
	private int inbox_type; // 0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
	private ImageView iv_select_contracts; // 选择联系人
	private RelativeLayout rl_send; // 发件人的布局
	private AlertDialog alertDialog;
	private SelectSenderAdapter selectAdapter; // 选择联系人适配器
	private ArrayList<Attachment> listAttachment = new ArrayList<>();
	private TextView tv_reply_forward;// 回复和转发
	private int email_type;

	private List<InnerContractsInfo> listReceiver = new ArrayList<>(); // 内部收件人集合

	// private int type_in_oa;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_email_send_or_replay;
	}

	@Override
	public void initVariables() {
		handleIntent();
		adapter = new EmailSendOrReplyAdapter(context);
		adapter.setOnItemClickListener(this);
		adapter.setHide(true);
	}

	private void handleIntent() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			type = bundle.getInt("type");
			email_type = bundle.getInt("email_type");
			if (type != 0) {
				email = (Email) bundle.getSerializable("email");
				inbox_type = bundle.getInt("inbox_type");
				// type_in_oa = bundle.getInt("type_in_oa");
			}
		}
	}

	@Override
	public void initViews(View view) {
		tv_addressee = (TagsEditText) view.findViewById(R.id.email_send_or_reply_getter);
		tv_cc = (TextView) view.findViewById(R.id.email_send_or_reply_chao_send);
		et_subject = (EditText) view.findViewById(R.id.send_or_reply_mail_subject);
		viewLine_no_attachment = view.findViewById(R.id.email_no_attachment_line);
		ll_no_attachment = (LinearLayout) view.findViewById(R.id.email_send_or_reply_attachments);
		tv_attachment_number = (TextView) view.findViewById(R.id.email_send_or_reply_attachment_number);
		tv_attachment_size = (TextView) view.findViewById(R.id.email_send_or_reply_attachment_size);
		iv_attachment_arrow = (ImageView) view.findViewById(R.id.email_send_or_reply_attachment_arrow);
		lv_list_view = (ListView) view.findViewById(R.id.email_send_or_reply_recycler_view);
		tv_other_attachment = (TextView) view.findViewById(R.id.email_send_or_reply_other_attachment);
		viewLine_attachment = view.findViewById(R.id.email_send_or_reply_view_attachment);
		et_content = (EditText) view.findViewById(R.id.send_or_reply_mail_content);
		ll_send = (LinearLayout) ((EmailSendOrReplyActivity) context).findViewById(R.id.include_head_home_linear);
		iv_send = (ImageView) ((EmailSendOrReplyActivity) context).findViewById(R.id.incluce_head_home);
		ll_back = (LinearLayout) ((EmailSendOrReplyActivity) context).findViewById(R.id.inclue_head_back_linear);
		ibn_add_attachment = (ImageButton) ((EmailSendOrReplyActivity) context).findViewById(R.id.email_add_attachment);
		iv_select_contracts = (ImageView) view.findViewById(R.id.email_send_or_reply_add_getter);
		rl_send = (RelativeLayout) view.findViewById(R.id.email_send_relative);
		tv_reply_forward = (TextView) view.findViewById(R.id.send_or_reply);

		String[] arr = getHintList();
		if (arr != null && arr.length > 0) {
			tv_addressee.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, arr));
			tv_addressee.setThreshold(1);
		}
		lv_list_view.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv_list_view);
		if (type == 4 || type == 5) {
			listAttachment = email.getAttachments();
			adapter.addAllItems(listAttachment);
			setListViewHeightBasedOnChildren(lv_list_view);
			showOrHideOtherAttachment(adapter.getDataSource());
		}
		ll_send.setVisibility(View.VISIBLE);
		iv_send.setImageResource(R.drawable.image_icon_yes);
		initData();
		initReceivers();
		ll_send.setOnClickListener(this);
		ibn_add_attachment.setOnClickListener(this);
		iv_attachment_arrow.setOnClickListener(this);
		tv_other_attachment.setOnClickListener(this);
		ll_back.setOnClickListener(this);
		iv_select_contracts.setOnClickListener(this);
		tv_addressee.setTagsListener(this);
		rl_send.setOnClickListener(this);
	}

	// 初始化数据
	private void initData() {
		switch (type) {
		/**
		 * 写邮件
		 */
		case 0:
			MailInfo mailInfoWrite = SharedPreferencedUtis.getMailInfo(context);
			if (mailInfoWrite != null) {
				tv_cc.setText(mailInfoWrite.getUserName());
			}
			break;

		/**
		 * 回复邮件
		 */
		case 1:
		case 4:
			String senderName = email.getSendderAddress(); // 发件人
			if (email_type == Constant.EMAIL_TYPE_OA) {
				senderName = email.getSenderName();
				String sendId = email.getSendId();
				InnerContractsInfo info = new InnerContractsInfo();
				info.setId(sendId);
				info.setName(senderName);
				listReceiver = new ArrayList<>();
				listReceiver.add(info);
			}
			String subject = email.getSubject(); // 主题
			tv_addressee.setText(senderName);
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(context);
			if (mailInfo != null) {
				tv_cc.setText(mailInfo.getUserName());
			}
			et_subject.setText(getResources().getString(R.string.reply) + " " + subject);
			rl_send.setClickable(false);
			rl_send.setEnabled(false);
			tv_reply_forward.setVisibility(View.VISIBLE);
			tv_reply_forward.setText(MailHelper.getInstance().mailInfoToString(email, 1));
			break;

		/**
		 * 回复全部
		 */
		case 2:
		case 6:
			String senderNameAll = email.getSendderAddress(); // 发件人
			ArrayList<String> receiveNameAll = email.getToAddress(); // 收件人集合
			String subjectAll = email.getSubject(); // 主题
			if (email_type == Constant.EMAIL_TYPE_OA) {
				ArrayList<String> id = email.getToId();
				ArrayList<String> name = email.getToName();
				tv_addressee.setTags(listToArray(name));
				initListReceiver(id, name);
			} else {
				MailInfo mailInfoAll = SharedPreferencedUtis.getMailInfo(context);
				if (mailInfoAll != null) {
					String name = mailInfoAll.getUserName();
					tv_cc.setText(name);
					if (receiveNameAll != null) {
						ArrayList<String> list = new ArrayList<>();
						list.add(senderNameAll);
						int size = receiveNameAll.size();
						for (int i = 0; i < size; i++) {
							String receiveName = receiveNameAll.get(i);
							if (receiveName != null && !receiveName.equals(name)) {
								list.add(receiveName);
							}
						}
						tv_addressee.setTags(listToArray(list));
					}
				}
			}
			et_subject.setText(getResources().getString(R.string.reply) + " " + subjectAll);
			tv_reply_forward.setVisibility(View.VISIBLE);
			tv_reply_forward.setText(MailHelper.getInstance().mailInfoToString(email, 1));
			break;

		/**
		 * 转发
		 */
		case 3:
		case 5:
			String subjectForward = email.getSubject(); // 主题
			MailInfo mailInfoForward = SharedPreferencedUtis.getMailInfo(context);
			if (mailInfoForward != null) {
				tv_cc.setText(mailInfoForward.getUserName());
			}
			et_subject.setText(getResources().getString(R.string.forwards) + " " + subjectForward);
			rl_send.setClickable(false);
			rl_send.setEnabled(false);
			tv_reply_forward.setVisibility(View.VISIBLE);
			tv_reply_forward.setText(MailHelper.getInstance().mailInfoToString(email, 1));
			break;

		default:
			break;
		}
	}

	private void initListReceiver(ArrayList<String> id, ArrayList<String> name) {
		if (id != null && name != null && id.size() == name.size()) {
			int length = id.size();
			listReceiver = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				InnerContractsInfo info = new InnerContractsInfo();
				String sendId = id.get(i);
				String sendName = name.get(i);
				info.setId(sendId);
				info.setName(sendName);
				listReceiver.add(info);
			}
		}
	}

	// 初始化收件人是否可以输入
	private void initReceivers() {
		if (email_type == Constant.EMAIL_TYPE_OA) {
			tv_addressee.setFocusable(false);
			ibn_add_attachment.setVisibility(View.GONE);
			rl_send.setClickable(false);
			rl_send.setEnabled(false);
		}
	}

	public static EmailSendOrReplyFragment newInstance(int type, Email email, int inbox_type, int email_type, int type_in_oa) {
		EmailSendOrReplyFragment fragment = new EmailSendOrReplyFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putSerializable("email", email);
		bundle.putInt("inbox_type", inbox_type);
		bundle.putInt("email_type", email_type);
		bundle.putInt("type_in_oa", type_in_oa);
		fragment.setArguments(bundle);
		return fragment;
	}

	// list转数组
	private String[] listToArray(ArrayList<String> list) {
		if (list == null) {
			return null;
		}

		int size = list.size();
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = list.get(i);
		}

		return array;
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.include_head_home_linear) {
			sendEmail(false);
		} else if (key == R.id.email_add_attachment) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			// intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
			startActivityForResult(intent, Constant.REQUEST_CODE_ADD_ATTACHMENT);

		} else if (key == R.id.email_send_or_reply_attachment_arrow || key == R.id.email_send_or_reply_other_attachment) {

			int tag = Integer.parseInt(iv_attachment_arrow.getTag().toString());
			if (tag == 0) {
				adapter.setHide(false);
				adapter.notifyDataSetChanged();
				tv_other_attachment.setVisibility(View.GONE);
				iv_attachment_arrow.setTag("1");
			} else {
				adapter.setHide(true);
				adapter.notifyDataSetChanged();
				tv_other_attachment.setVisibility(View.VISIBLE);
				iv_attachment_arrow.setTag("0");
			}
			setListViewHeightBasedOnChildren(lv_list_view);
		} else if (key == R.id.inclue_head_back_linear) {
			if (presenter.isShowDraftDialog(tv_addressee.getText().toString(), null, getSubject(), adapter.getDataSource(), getContent())) {
				showDraftDialog();
			} else {
				context.finish();
			}
		} else if (key == R.id.dialog_cancle) {
			if (dialog_draft != null && dialog_draft.isShowing()) {
				dialog_draft.dismiss();
			}
		} else if (key == R.id.dialog_save_draft) {
			dialog_draft.dismiss();
			sendEmail(true);
		} else if (key == R.id.dialog_leave) {
			if (dialog_draft != null && dialog_draft.isShowing()) {
				dialog_draft.dismiss();
			}
			context.finish();
		} else if (key == R.id.email_send_or_reply_add_getter) {
			if (email_type == Constant.EMAIL_TYPE_OA) {
				Intent intent = new Intent(context, SelectInnerContactsActivity.class);
				startActivityForResult(intent, INNER_CONTACT_REQUEST_CODE);
			} else {
				Intent intentSelectContracts = new Intent(context, SelectContractsActivity.class);
				Bundle bundleSelectContracts = new Bundle();
				bundleSelectContracts.putSerializable("list", presenter.stringToObject(tv_addressee.getText().toString()));
				intentSelectContracts.putExtras(bundleSelectContracts);
				startActivityForResult(intentSelectContracts, Constant.REQUEST_CODE_SELECT_CONTRACTS);
			}
		} else if (key == R.id.email_send_relative) {
			showAlertDialog(getAccountInfo(), getSenderAddress());
		}
	}

	// 发送邮件
	public void sendEmail(boolean isDraft) {
		switch (type) {
		/**
		 * 发送邮件
		 */
		case 0:
			ArrayList<String> receiverId = presenter.stringToArrayList(tv_addressee.getText().toString());
			if (email_type == Constant.EMAIL_TYPE_OA) {
				if (isDraft) {
					presenter.saveEmailInOA("", getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), getUrl(), getToken());
				} else {
					presenter.sendEmailInOA("", getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), 1, getUrl(), getToken());
				}
			} else {
				presenter.sendEmail(receiverId, getSenderAddress(), getSubject(), adapter.getDataSource(), getContent(), isDraft);
			}
			break;

		/**
		 * 回复邮件
		 */
		case 1:
		case 4:
			if (email_type == Constant.EMAIL_TYPE_OA) {
				if (isDraft) {
					presenter.saveEmailInOA(email.getMessageID(), getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), getUrl(), getToken());
				} else {
					presenter.sendEmailInOA(email.getMessageID(), getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), 2, getUrl(), getToken());
				}
			} else {
				ArrayList<String> listReceive = presenter.stringToArrayList(tv_addressee.getText().toString());
				presenter.replyEmail(email.getMessageID(), email.getMessageNumber(), getSubject(), getContent(), 0, isDraft, inbox_type, listReceive, adapter.getDataSource());
			}
			break;

		/**
		 * 回复全部
		 */
		case 2:
		case 6:
			if (email_type == Constant.EMAIL_TYPE_OA) {
				presenter.sendEmailInOA(email.getMessageID(), getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), 2, getUrl(), getToken());
			} else {
				ArrayList<String> listReceiveAll = presenter.stringToArrayList(tv_addressee.getText().toString());
				presenter.replyEmail(email.getMessageID(), email.getMessageNumber(), getSubject(), getContent(), 1, isDraft, inbox_type, listReceiveAll, adapter.getDataSource());
			}
			break;

		/**
		 * 转发邮件
		 */
		case 3:
		case 5:
			if (email_type == Constant.EMAIL_TYPE_OA) {
				if (isDraft) {
					presenter.saveEmailInOA(email.getMessageID(), getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), getUrl(), getToken());
				} else {
					presenter.sendEmailInOA(email.getMessageID(), getReceiverIdInOA(), getReceiverNameInOA(), "", "", getSubject(), getContent(), 2, getUrl(), getToken());
				}
			} else {
				ArrayList<String> listReceiveForward = presenter.stringToArrayList(tv_addressee.getText().toString());
				presenter.forwardEmail(email.getMessageID(), email.getMessageNumber(), getSubject(), getContent(), inbox_type, isDraft, listReceiveForward, adapter.getDataSource());
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constant.REQUEST_CODE_ADD_ATTACHMENT && resultCode == Activity.RESULT_OK) {
			Uri uri = null;
			if (data != null) {
				uri = data.getData();
			}
			if (uri == null) {
				return;
			}

			String path = uri.getPath();
			if (uri.toString().contains("content://media/external/images/media/")) {
				path = getRealPathFromUri(context, uri);
			}
			if (presenter.checkAttachmentIsTooBig(path)) {
				Toast.makeText(context, getResources().getString(R.string.attachment_is_too_big), Toast.LENGTH_SHORT).show();
				return;
			}
			Attachment attachment = Attachment.GetFileInfo(path);
			adapter.addItems(attachment);
			setListViewHeightBasedOnChildren(lv_list_view);
			showOrHideOtherAttachment(adapter.getDataSource());
		} else if (requestCode == Constant.REQUEST_CODE_SELECT_CONTRACTS && resultCode == Constant.REQUEST_CONDE_SELECT_CONTRACTS_BACK_SEND) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					@SuppressWarnings("unchecked")
					ArrayList<ContractsInfo> list = (ArrayList<ContractsInfo>) bundle.getSerializable("list");
					tv_addressee.setTags(presenter.listToArray(list));
				}
			}
		} else if (requestCode == INNER_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					@SuppressWarnings("unchecked")
					List<InnerContractsInfo> list = (List<InnerContractsInfo>) bundle.getSerializable("list");
					boolean isContainsAll = presenter.contractsListContainsInputContractsInner(list, listReceiver);
					if (isContainsAll) {
						listReceiver = list;
					} else if (presenter.contractsListNotContainsInputContractsInner(list, listReceiver)) {
						listReceiver.addAll(list);
					} else {
						listReceiver.addAll(presenter.getNotContainsListInner(listReceiver, list));
					}
					tv_addressee.setTags(presenter.listToArrayInner(listReceiver));
				}
			}
		}
	}

	// 图片路径转化为真正的路径
	public String getRealPathFromUri(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	// 显示或者隐藏附件
	private void showOrHideOtherAttachment(List<Attachment> list) {

		if (list == null || list.size() == 0) {
			viewLine_no_attachment.setVisibility(View.GONE);
			viewLine_attachment.setVisibility(View.VISIBLE);
			ll_no_attachment.setVisibility(View.GONE);
			lv_list_view.setVisibility(View.GONE);
			tv_other_attachment.setVisibility(View.GONE);
		} else {
			viewLine_no_attachment.setVisibility(View.VISIBLE);
			viewLine_attachment.setVisibility(View.VISIBLE);
			ll_no_attachment.setVisibility(View.VISIBLE);
			lv_list_view.setVisibility(View.VISIBLE);
			tv_attachment_number.setText(list.size() + getResources().getString(R.string.number_of_attachment));
			tv_attachment_size.setText(getTotalAttachmentSize(list));
			if (list == null || list.size() <= 2) {
				tv_other_attachment.setVisibility(View.GONE);
				iv_attachment_arrow.setVisibility(View.GONE);
			} else {
				int tag = Integer.parseInt(iv_attachment_arrow.getTag().toString());
				if (tag == 0) {
					tv_other_attachment.setVisibility(View.VISIBLE);
				}
				tv_other_attachment.setText(getResources().getString(R.string.other_attachment, String.valueOf(list.size() - 2)));
				iv_attachment_arrow.setVisibility(View.VISIBLE);
			}
		}
	}

	// 计算listview的高度
	private void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int count = listAdapter.getCount();
		int totalHeight = 0;
		for (int i = 0, len = count; i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (count - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	// 获取附件的总大小
	private String getTotalAttachmentSize(List<Attachment> list) {
		long value = 0;
		if (list == null) {
			return null;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			long fileLength = list.get(i).getFileLength();
			value += fileLength;
		}
		return Attachment.convertStorage(value);
	}

	@Override
	public void onItemClick(int position) {
		if (adapter.removeItem(position)) {
			setListViewHeightBasedOnChildren(lv_list_view);
			showOrHideOtherAttachment(adapter.getDataSource());
		}
	}

	@Override
	public String getSubject() {
		return et_subject.getText().toString();
	}

	@Override
	public String getContent() {
		return et_content.getText().toString();
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showMessage(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showProgressDialog(String message) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(message);
		dialog.show();
	}

	@Override
	public void hideProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void destroryView() {
		context.setResult(Activity.RESULT_OK);
		((EmailSendOrReplyActivity) context).finish();
	}

	@Override
	public void showDraftDialog() {

		if (dialog_draft == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_draft, null);
			builder.setView(view);
			TextView tv_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
			TextView tv_save_draft = (TextView) view.findViewById(R.id.dialog_save_draft);
			TextView tv_leave = (TextView) view.findViewById(R.id.dialog_leave);
			dialog_draft = builder.create();

			tv_cancle.setOnClickListener(this);
			tv_save_draft.setOnClickListener(this);
			tv_leave.setOnClickListener(this);
		}
		dialog_draft.show();
	}

	// 返回
	public void onBackPressed() {
		if (presenter.isShowDraftDialog(tv_addressee.getText().toString(), null, getSubject(), adapter.getDataSource(), getContent())) {
			if (email_type == Constant.EMAIL_TYPE_OA) {
				if (type != 2 && type != 6) {
					showDraftDialog();
				}
			} else {
				showDraftDialog();
			}
		} else {
			context.finish();
		}
	}

	@Override
	public void onTagsChanged(Collection<String> tags) {

	}

	@Override
	public void onEditingFinished() {

	}

	@Override
	public void saveReceives() {
		String receivers = tv_addressee.getText().toString();
		MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(context);
		if (mailInfo != null) {
			String name = mailInfo.getUserName();
			ArrayList<ContractsInfo> listSharedPreference = getSharedPreferencesValue(name);
			ArrayList<ContractsInfo> lists = presenter.stringToObject(receivers);
			ArrayList<ContractsInfo> newList = presenter.getNewList(listSharedPreference, lists);
			SharedPreferencedUtis.saveValue(context, Constant.NAME, Constant.KEY + name, newList);
		}
	}

	@Override
	public String getSenderAddress() {
		return tv_cc.getText().toString();
	}

	@Override
	public ArrayList<ContractsInfo> getSharedPreferencesValue(String name) {
		Type type = new TypeToken<ArrayList<ContractsInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(context, Constant.NAME, Constant.KEY + name, type);
	}

	@Override
	public ArrayList<AccountInfo> getAccountInfo() {

		Type type = new TypeToken<ArrayList<AccountInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(context, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, type);
	}

	@Override
	public void showAlertDialog(ArrayList<AccountInfo> list, String currentAccount) {
		if (list == null) {
			return;
		}

		if (alertDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_sender, null);
			ListView listView = (ListView) view.findViewById(R.id.dialog_list_view);
			selectAdapter = new SelectSenderAdapter(context, list, currentAccount);
			listView.setAdapter(selectAdapter);
			listView.setOnItemClickListener(new OnItemClickListView());
			alertDialog = builder.create();
			alertDialog.setView(view);
		} else {
			selectAdapter.setDataSource(list, currentAccount);
			selectAdapter.notifyDataSetChanged();
		}
		alertDialog.show();
	}

	// 内部类---listview的item点击事件
	private class OnItemClickListView implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ArrayList<AccountInfo> list = selectAdapter.getDataSource();
			if (list != null && list.size() > position) {
				tv_cc.setText(list.get(position).getAccount());
				alertDialog.dismiss();
			}
		}
	}

	@Override
	public String[] getHintList() {
		MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(context);
		if (mailInfo != null) {
			ArrayList<ContractsInfo> list = getSharedPreferencesValue(mailInfo.getUserName());
			if (list != null) {
				int size = list.size();
				String[] arr = new String[size];
				for (int i = 0; i < size; i++) {
					arr[i] = list.get(i).getEmailAddress();
				}
				return arr;
			}
		}
		return null;
	}

	@Override
	public String getUrl() {
		return FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
	}

	@Override
	public String getToken() {
		return PreferenceHelper.token_read(context);
	}

	@Override
	public void onItemRemoved(int position) {
		if (listReceiver != null && listReceiver.size() > position) {
			listReceiver.remove(position);
		}
	}

	@Override
	public String getReceiverIdInOA() {
		String eidArray = "";
		if (listReceiver != null) {
			int size = listReceiver.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = listReceiver.get(i);
				if (info != null) {
					eidArray += info.getId().trim() + ",";
				}
			}
			eidArray = eidArray.substring(0, eidArray.length() - 1);
		}
		return eidArray;
	}

	@Override
	public String getReceiverNameInOA() {
		String eidArray = "";
		if (listReceiver != null) {
			int size = listReceiver.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = listReceiver.get(i);
				if (info != null) {
					eidArray += info.getName().trim() + ",";
				}
			}
			eidArray = eidArray.substring(0, eidArray.length() - 1);
		}
		return eidArray;
	}
}
