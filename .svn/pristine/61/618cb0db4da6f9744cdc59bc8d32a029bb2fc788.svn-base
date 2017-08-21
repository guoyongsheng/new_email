/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.core.utils.FileManager;
import com.zfsoft.zf_new_email.R;
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
 * @date: 2016-10-27
 * @Description: 附件适配器
 */
public class EmailSendOrReplyAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Attachment> list = new ArrayList<>();
	private OnItemClickListener onItemClickListener;
	private boolean isHide = true;

	public EmailSendOrReplyAdapter(Context context) {
		this.context = context;
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}
	}

	@Override
	public int getCount() {

		if (list == null) {
			return 0;
		}

		int size = list.size();
		if (size > 2) {
			if (isHide) {
				return 2;
			}
			return size;
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return (list != null && list.size() > position) ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void addItems(Attachment attachment) {
		list.add(attachment);
		notifyDataSetChanged();
	}

	public void addAllItems(List<Attachment> list) {
		if (list != null) {
			this.list.addAll(list);
			notifyDataSetChanged();
		}
	}

	public List<Attachment> getDataSource() {
		return list;
	}

	public boolean removeItem(int position) {
		if (list == null || list.size() <= position) {
			return false;
		}
		list.remove(position);
		notifyDataSetChanged();
		return true;
	}

	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (list == null || list.size() <= position || inflater == null) {
			return null;
		}

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_email_detail, null);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_email_detail_attachment_icon);
			viewHolder.tv_attachment_name = (TextView) convertView.findViewById(R.id.item_email_detail_attachment_name);
			viewHolder.tv_attachment_size = (TextView) convertView.findViewById(R.id.item_email_detail_attachment_size);
			viewHolder.tv_delete = (TextView) convertView.findViewById(R.id.item_eamil_detail_attachment_open);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Attachment attachment = list.get(position);
		//String path = attachment.getFilePath();
		String name = attachment.getFileName();
		String size = attachment.getFileSize();

		viewHolder.iv_icon.setImageResource(FileManager.getPostfixIcon(context, name));
		//GlideUtils.loadImage(path, viewHolder.iv_icon, context);
		viewHolder.tv_attachment_name.setText(name);
		viewHolder.tv_attachment_size.setText(size);
		viewHolder.tv_delete.setText(R.string.delete);

		viewHolder.tv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				onItemClickListener.onItemClick(position);
			}
		});
		return convertView;
	}

	// 静态内部类
	private static class ViewHolder {
		private ImageView iv_icon; // 附件图片
		private TextView tv_attachment_name; // 附件名称
		private TextView tv_attachment_size; // 附件大小
		private TextView tv_delete; // 删除
	}
}
