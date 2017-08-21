/**
 * @date2016-10-20
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaildetail;

import java.util.List;
import com.zfsoft.core.utils.FileManager;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Attachment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wesley
 * @date 2016-10-20上午10:18:53
 * @Description: 附件适配器
 */
public class EmailDetailAttachmentAdapter extends BaseAdapter {

	private List<Attachment> list;
	private LayoutInflater inflater;
	private boolean isHide = true;
	private Context context;
	private OnItemClickListener listener;
	private int email_type;

	public EmailDetailAttachmentAdapter(Context context, List<Attachment> list) {
		this.list = list;
		this.context = context;
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	public void setEmailType(int email_type) {
		this.email_type = email_type;
	}

	@Override
	public int getCount() {
		if (list != null) {
			int size = list.size();
			if (isHide) {
				if (size > 2) {
					return 2;
				}
				return size;
			} else {
				return size;
			}
		}
		return 0;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null || list == null || list.size() <= position && listener != null) {
			return null;
		}

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_email_detail, null);
			viewHolder.iv_attachment_icon = (ImageView) convertView.findViewById(R.id.item_email_detail_attachment_icon);
			viewHolder.tv_attachment_name = (TextView) convertView.findViewById(R.id.item_email_detail_attachment_name);
			viewHolder.tv_attachment_size = (TextView) convertView.findViewById(R.id.item_email_detail_attachment_size);
			viewHolder.tv_open = (TextView) convertView.findViewById(R.id.item_eamil_detail_attachment_open);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Attachment attachment = list.get(position);
		final String id = attachment.getAttachmentId(); // 附件id
		final String attactName = attachment.getFileName();// 文件名
		final String filePath = attachment.getFilePath(); // 文件路径
		final String fileSize = attachment.getFileSize(); // 文件大小
		viewHolder.tv_attachment_name.setText(attactName);
		viewHolder.tv_attachment_size.setText(fileSize);
		viewHolder.iv_attachment_icon.setImageResource(FileManager.getPostfixIcon(context, attactName));
		viewHolder.tv_open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				switch (email_type) {
				case Constant.EMAIL_TYPE_OA:
					listener.onItemClick(id, attactName, fileSize);
					break;

				default:
					listener.onItemClick(filePath);
					break;
				}
			}
		});
		return convertView;
	}

	// 静态内部类
	private static class ViewHolder {
		private ImageView iv_attachment_icon;
		private TextView tv_attachment_name;
		private TextView tv_attachment_size;
		private TextView tv_open;
	}
}
