package com.zfsoft.zf_new_email.modules.searchcontact;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 搜索联系人适配器
 * 
 * @author wesley
 * @date 2017-2-28
 * @Description:
 */
public class SearchContactAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<InnerContractsInfo> list;

	public SearchContactAdapter(Context context) {
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}
	}

	public void notifyDataSetChanged(List<InnerContractsInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void changeSelectStatus(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				int checkMode = info.getCheckMode();
				if (checkItemIsSelect(checkMode)) {
					updateItemStatus(position, 0);
				} else {
					updateItemStatus(position, 2);
				}
			}
		}
	}

	// 改变item的状态
	private void updateItemStatus(int position, int checkMode) {
		if (list != null && list.size() > position && list.get(position) != null) {
			list.get(position).setCheckMode(checkMode);
			notifyDataSetChanged();
		}
	}

	// 校验item是否选中
	private boolean checkItemIsSelect(int checkMode) {
		if (checkMode == 0 || checkMode == 1) {
			return false;
		}

		return true;
	}

	/**
	 * 获取选中的items
	 * 
	 * @return 联系人集合
	 */
	public List<InnerContractsInfo> getSelectItems() {
		List<InnerContractsInfo> listSelect = new ArrayList<>();
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					if (checkItemIsSelect(info.getCheckMode())) {
						listSelect.add(info);
					}
				}
			}
		}
		return listSelect;
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
			convertView = inflater.inflate(R.layout.item_select_inner_contact, null);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_inner_contact_image);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.item_inner_contact_name);
			viewHolder.iv_select_status = (ImageView) convertView.findViewById(R.id.item_inner_contact_image_select);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		InnerContractsInfo info = list.get(position);
		String name = info.getName();
		int checkMode = info.getCheckMode();

		viewHolder.iv_icon.setVisibility(View.INVISIBLE);
		viewHolder.tv_name.setText(name);

		switch (checkMode) {
		/**
		 * 未选中
		 */
		case 0:
			viewHolder.iv_select_status.setImageResource(R.drawable.image_icon_no_select);
			break;

		/**
		 * 部分选中
		 */
		case 1:
			viewHolder.iv_select_status.setImageResource(R.drawable.image_icon_part_select);
			break;

		/**
		 * 全部选中
		 */
		case 2:
			viewHolder.iv_select_status.setImageResource(R.drawable.image_icon_all_select);
			break;

		default:
			break;
		}

		return convertView;
	}

	// 静态内部类
	private static class ViewHolder {
		private ImageView iv_icon;
		private TextView tv_name;
		private ImageView iv_select_status;
	}
}
