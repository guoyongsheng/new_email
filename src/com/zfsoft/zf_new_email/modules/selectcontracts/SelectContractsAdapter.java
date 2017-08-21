/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectcontracts;

import java.util.ArrayList;
import com.zfsoft.core.view.textdrawable.ColorGenerator;
import com.zfsoft.core.view.textdrawable.TextDrawable;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.entity.ContractsInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author wesley
 * @date: 2016-11-26
 * @Description: 选项联系人适配器
 */
public class SelectContractsAdapter extends BaseAdapter {

	private ArrayList<ContractsInfo> list = new ArrayList<>();
	private LayoutInflater inflater;
	private ColorGenerator colorGenerator;

	public SelectContractsAdapter(Context context) {
		if (context != null) {
			inflater = LayoutInflater.from(context);

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

	public ArrayList<ContractsInfo> getAllItems() {
		return list;
	}

	public void changeCheckedStatus(int position) {
		if (list != null && list.size() > position) {
			boolean isChecked = list.get(position).isSelected();
			if (isChecked) {
				list.get(position).setSelected(false);
			} else {
				list.get(position).setSelected(true);
			}
			notifyDataSetChanged();
		}
	}

	public void setDataSource(ArrayList<ContractsInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_select_contracts, null);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.item_select_contracts_check);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_select_contracts_icon);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.item_select_contracts_name);
			viewHolder.tv_address = (TextView) convertView.findViewById(R.id.item_select_contracts_address);
			viewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.item_relative);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ContractsInfo contractsInfo = list.get(position);
		if (contractsInfo == null) {
			return null;
		}

		boolean isSelected = contractsInfo.isSelected();
		String name = contractsInfo.getName();
		String address = contractsInfo.getEmailAddress();

		viewHolder.checkBox.setChecked(isSelected);
		viewHolder.tv_name.setText(name);
		viewHolder.tv_address.setText(address);
		TextDrawable drawable = TextDrawable.builder().buildRound("邮", colorGenerator.getColor(viewHolder.tv_address));
		viewHolder.iv_icon.setImageDrawable(drawable);

		viewHolder.rl_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeCheckedStatus(position);
			}
		});
		return convertView;
	}

	// 静态内部类
	private static class ViewHolder {

		private CheckBox checkBox; // checkbox
		private ImageView iv_icon; // 图标
		private TextView tv_name; // 姓名
		private TextView tv_address; // 地址
		private RelativeLayout rl_item; // item的佈局
	}
}
