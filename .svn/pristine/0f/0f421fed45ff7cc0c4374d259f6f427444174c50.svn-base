/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.util.ArrayList;

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
 * @date: 2016-11-29
 * @Description:选择发信人的适配器
 */
public class SelectSenderAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<AccountInfo> list;
	private String currentAccount;

	public SelectSenderAdapter(Context context, ArrayList<AccountInfo> list, String currentAccount) {
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}

		this.list = list;
		this.currentAccount = currentAccount;
	}

	public void setDataSource(ArrayList<AccountInfo> list, String currentAccount) {
		this.list = list;
		this.currentAccount = currentAccount;
	}

	public ArrayList<AccountInfo> getDataSource() {
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
		if (inflater == null || list == null || list.size() <= position) {
			return null;
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_dialog_select_sender, null);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_image_icon);
			viewHolder.tv_account = (TextView) convertView.findViewById(R.id.item_account);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String account = list.get(position).getAccount();
		viewHolder.tv_account.setText(account);
		if (account != null && account.equals(currentAccount)) {
			viewHolder.iv_icon.setVisibility(View.VISIBLE);
		} else {
			viewHolder.iv_icon.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private static class ViewHolder {
		private ImageView iv_icon;
		private TextView tv_account;
	}
}
