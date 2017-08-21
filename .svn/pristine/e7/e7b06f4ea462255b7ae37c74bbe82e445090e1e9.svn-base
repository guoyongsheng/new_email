/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wesley
 * @date: 2016-12-19
 * @Description: 账号的适配器
 */
public class EmailListAccountAdapter extends BaseAdapter {

	private List<AccountInfo> list = new ArrayList<>();
	private LayoutInflater inflater;
	private String currentAccount; // 当前账号

	public EmailListAccountAdapter(Context context, String currentAccount, List<AccountInfo> list) {
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}
		this.currentAccount = currentAccount;
		this.list = list;
	}

	public List<AccountInfo> getAllItems() {
		return list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null || list == null || list.size() <= position || list.get(position) == null) {
			return null;
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_email_list_account, null);
			viewHolder.tv_account = (TextView) convertView.findViewById(R.id.item_email_list_account);
			viewHolder.iv_selected = (ImageView) convertView.findViewById(R.id.item_email_list_selected);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AccountInfo accountInfo = list.get(position);
		String account = accountInfo.getAccount(); // 账号

		viewHolder.tv_account.setText(account);
		if (account != null && account.equals(currentAccount)) {
			viewHolder.iv_selected.setVisibility(View.VISIBLE);
		} else {
			viewHolder.iv_selected.setVisibility(View.GONE);
		}
		return convertView;
	}

	// 内部类
	private static class ViewHolder {
		private TextView tv_account; // 账号
		private ImageView iv_selected; // 是否是当前账号
	}
}
