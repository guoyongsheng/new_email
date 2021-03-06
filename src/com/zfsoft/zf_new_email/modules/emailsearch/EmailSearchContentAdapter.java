/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsearch;

import java.util.ArrayList;
import java.util.List;

import com.zfsoft.core.view.textdrawable.ColorGenerator;
import com.zfsoft.core.view.textdrawable.TextDrawable;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.util.SizeUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @author wesley
 * @date: 2016-11-23
 * @Description:邮件搜索内容的适配器
 */
public class EmailSearchContentAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Email> list = new ArrayList<>();
	private LayoutParams paramsNoRead;
	private LayoutParams params;
	private ColorGenerator colorGenerator;
	private int inbox_type;
	private boolean isRefreshing; // 是否是刷新

	public EmailSearchContentAdapter(Context context) {
		if (context != null) {
			inflater = LayoutInflater.from(context);

			paramsNoRead = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramsNoRead.addRule(RelativeLayout.RIGHT_OF, R.id.item_email_list_no_read);
			paramsNoRead.leftMargin = SizeUtils.dp2px(context, 8);
			paramsNoRead.rightMargin = SizeUtils.dp2px(context, 160);

			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF, R.id.item_email_list_icon);
			params.leftMargin = SizeUtils.dp2px(context, 8);
			params.rightMargin = SizeUtils.dp2px(context, 160);

			colorGenerator = ColorGenerator.MATERIAL;
		}
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (list != null && list.size() > position) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setInboxType(int inbox_type) {
		this.inbox_type = inbox_type;
	}

	public void setDataSource(List<Email> list) {
		if (isRefreshing) {
			this.list = list;
		} else {
			this.list.addAll(list);
		}
		notifyDataSetChanged();
	}

	public List<Email> getAllItems() {
		return list;
	}

	public void clearItems() {
		this.list.clear();
		notifyDataSetChanged();
	}

	public void setIsRefreshing(boolean isRefreshing) {
		this.isRefreshing = isRefreshing;
	}

	public void removeItem(int position) {
		if (list != null && list.size() > position) {
			list.remove(position);
			notifyDataSetChanged();
		}
	}

	public void insertItem(int position, Email email) {
		if (list != null && list.size() >= position && position >= 0) {
			list.add(position, email);
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (list == null || list.size() <= position || inflater == null) {
			return null;
		}
		ViewHoldr viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHoldr();
			convertView = inflater.inflate(R.layout.item_email_list, null);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_email_list_icon);
			viewHolder.tv_no_read = (TextView) convertView.findViewById(R.id.item_email_list_no_read);
			viewHolder.tv_sender = (TextView) convertView.findViewById(R.id.item_email_list_user);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.item_email_list_time);
			viewHolder.iv_attachment = (ImageView) convertView.findViewById(R.id.item_email_list_attachment);
			viewHolder.tv_subject = (TextView) convertView.findViewById(R.id.item_email_list_subject);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.item_email_list_content);
			viewHolder.iv_star = (ImageView) convertView.findViewById(R.id.item_email_iconn_star);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHoldr) convertView.getTag();
		}

		Email email = list.get(position);
		if (email == null) {
			return null;
		}

		boolean isNew = email.isNews(); // 是否是未读 true:未读 false:已读
		String senderName = email.getSenderName(); // 发送人姓名
		String tiem = email.getSentdata(); // 发送时间
		String subject = email.getSubject(); // 主题
		String content = email.getContent(); // 内容
		boolean isHtml = email.isHtml();
		boolean isFlaged = email.isFlaged(); // 是否是星标邮件
		if (isHtml) {
			content = "这是一封网页邮件";
		}
		boolean isHasAttachment = email.isHasAttachment();
		if (TextUtils.isEmpty(senderName)) {
			String address = email.getSendderAddress();
			viewHolder.tv_sender.setText(address);
		} else {
			viewHolder.tv_sender.setText(senderName);
		}
		viewHolder.tv_time.setText(tiem);
		viewHolder.tv_subject.setText(subject);
		viewHolder.tv_content.setText(content);
		if (TextUtils.isEmpty(content)) {
			viewHolder.tv_content.setVisibility(View.GONE);
		} else {
			viewHolder.tv_content.setVisibility(View.VISIBLE);
			viewHolder.tv_content.setText(content);
		}
		if (inbox_type == 2) {
			viewHolder.tv_no_read.setVisibility(View.GONE);
			viewHolder.tv_sender.setLayoutParams(params);
		} else {
			if (isNew) {
				viewHolder.tv_no_read.setVisibility(View.VISIBLE);
				viewHolder.tv_sender.setLayoutParams(paramsNoRead);
			} else {
				viewHolder.tv_no_read.setVisibility(View.GONE);
				viewHolder.tv_sender.setLayoutParams(params);
			}

			if (isFlaged) {
				viewHolder.iv_star.setVisibility(View.VISIBLE);
			} else {
				viewHolder.iv_star.setVisibility(View.GONE);
			}
		}
		if (TextUtils.isEmpty(senderName)) {
			senderName = "邮";
		}
		viewHolder.iv_attachment.setVisibility(isHasAttachment ? View.VISIBLE : View.GONE);
		TextDrawable drawable = TextDrawable.builder().buildRound("邮", colorGenerator.getColor(senderName));
		viewHolder.iv_icon.setImageDrawable(drawable);
		return convertView;
	}

	// 内部类
	private static class ViewHoldr {
		private ImageView iv_icon; // 图标
		private TextView tv_no_read; // 已读或者未读
		private TextView tv_sender; // 发件人
		private TextView tv_time; // 时间
		private ImageView iv_attachment; // 附件
		private TextView tv_subject; // 主题
		private TextView tv_content; // 内容
		private ImageView iv_star; // 星标
	}
}
