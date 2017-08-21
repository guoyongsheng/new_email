/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsearch;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.entity.HistoryInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author wesley
 * @date: 2016-11-23
 * @Description: 历史记录适配器
 */
public class EmailSearchHistoryAdapter extends BaseAdapter {

	private List<HistoryInfo> list = new ArrayList<>();
	private LayoutInflater inflater;
	private OnItemHistoryClickListener onItemHistoryClickListener;

	public EmailSearchHistoryAdapter(Context context) {
		if (context != null) {
			inflater = LayoutInflater.from(context);
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

	public void setOnItemHistoryClickListener(OnItemHistoryClickListener onItemHistoryClickListener) {
		this.onItemHistoryClickListener = onItemHistoryClickListener;
	}

	public List<HistoryInfo> getAllItems() {
		return list;
	}

	public void setDataSource(List<HistoryInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (list == null || list.size() <= position || inflater == null) {
			return null;
		}

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_email_search_history, null);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.item_history_content);
			viewHolder.ll_delete = (LinearLayout) convertView.findViewById(R.id.item_history_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final String content = list.get(position).getContent();
		viewHolder.tv_content.setText(content);
		viewHolder.ll_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (onItemHistoryClickListener != null) {
					onItemHistoryClickListener.onDeleteClick(position);
				}
			}
		});
		return convertView;
	}

	// 静态内部类
	private static class ViewHolder {
		private TextView tv_content; // 内容
		private LinearLayout ll_delete; // 删除
	}
}
