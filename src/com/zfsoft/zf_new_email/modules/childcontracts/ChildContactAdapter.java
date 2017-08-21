package com.zfsoft.zf_new_email.modules.childcontracts;

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
 * @author wesley
 * @date 2017-2-28
 * @Description: 子节点适配器
 */
public class ChildContactAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<InnerContractsInfo> list;

	public ChildContactAdapter(Context context) {
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}
	}

	public void notifyDataSetChanged(List<InnerContractsInfo> list) {
		if (list != null && list.size() > 0) {
			list.get(0).setParentNote(true);
			this.list = list;
			notifyDataSetChanged();
		}
	}

	/**
	 * 改变选中的状态
	 * 
	 * @param position
	 *            当前位置
	 */
	public void changeSelectStatus(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				boolean isParentNote = info.isParentNote();
				int checkMode = info.getCheckMode();
				if (isParentNote) {
					if (checkIsSelect(checkMode)) {
						updateAllNoteStatus(0);
					} else {
						updateAllNoteStatus(2);
					}
				} else {
					if (checkIsSelect(checkMode)) {
						updateCurrentNoteStatus(0, position);
						if (checkAllChildNoteIsNotSelect()) {
							updateParentNoteStatus(0);
						} else if (checkParentNoteHasSelect()) {
							updateParentNoteStatus(1);
						}
					} else {
						updateCurrentNoteStatus(2, position);
						if (checkChildNoteIsAllSelect()) {
							updateParentNoteStatus(2);
						} else {
							updateParentNoteStatus(1);
						}
					}
				}
			}
		}
	}

	/**
	 * 获取选中的items
	 * 
	 * @return items集合
	 */
	public List<InnerContractsInfo> getSelectItems() {

		List<InnerContractsInfo> listSelect = new ArrayList<>();
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					if (checkIsSelect(info.getCheckMode())) {
						listSelect.add(info);
						if (i == 0) {
							return listSelect;
						}
					}
				}
			}
		}
		return listSelect;
	}

	/**
	 * 改变根节点状态
	 */
	public void updateParentNoteStatus(int checkMode) {
		if (list != null && list.size() > 0 && list.get(0) != null) {
			list.get(0).setCheckMode(checkMode);
			notifyDataSetChanged();
		}
	}

	/**
	 * 判断所有子节点是否都没有选中
	 * 
	 * @return true:都没有选中 false:
	 */
	public boolean checkAllChildNoteIsNotSelect() {

		if (list != null) {
			int size = list.size();
			for (int i = 1; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					int checkMode = info.getCheckMode();
					if (checkIsSelect(checkMode)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * 判断子节点是否全部都选中了
	 * 
	 * @return true：全部选中 false:没有全部选中
	 */
	public boolean checkChildNoteIsAllSelect() {
		if (list != null) {
			int size = list.size();
			for (int i = 1; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					int checkMode = info.getCheckMode();
					if (!checkIsSelect(checkMode)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 判断子节点是否有选中的
	 * 
	 * @return
	 */
	public boolean checkParentNoteHasSelect() {
		if (list != null) {
			int size = list.size();
			for (int i = 1; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					int checkMode = info.getCheckMode();
					if (checkIsSelect(checkMode)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 跟新当前节点的状态
	 * 
	 * @param checkMode
	 *            选中模式
	 */
	public void updateCurrentNoteStatus(int checkMode, int position) {
		if (list != null && list.size() > position && list.get(position) != null) {
			list.get(position).setCheckMode(checkMode);
		}
	}

	/**
	 * 跟新狀態
	 * 
	 * @param checkMode
	 *            选中模式
	 */
	public void updateAllNoteStatus(int checkMode) {
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				if (list.get(i) != null) {
					list.get(i).setCheckMode(checkMode);
				}
			}
		}

		notifyDataSetChanged();
	}

	/**
	 * 判断当前节点的选中状态
	 * 
	 * @param checkMode
	 * @return true:选中 false:没选中
	 */
	public boolean checkIsSelect(int checkMode) {
		if (checkMode == 0 || checkMode == 1) {
			return false;
		}
		return true;
	}

	/**
	 * 获取根节点选中的状态
	 * 
	 * @return
	 */
	public int getParentNoteMode() {
		if (list != null && list.size() > 0) {
			return list.get(0).getCheckMode();
		}
		return 0;
	}

	/**
	 * 获取所有子节点选中的联系人集合
	 * 
	 * @return 联系人集合
	 */
	public List<InnerContractsInfo> getChildSelectList() {

		List<InnerContractsInfo> childList = new ArrayList<>();
		if (list != null && list.size() > 1) {
			int size = list.size();
			for (int i = 1; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					if (checkIsSelect(info.getCheckMode())) {
						childList.add(info);
					}
				}
			}
		}
		return childList;
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
		String name = info.getName(); // 姓名
		int checkMode = info.getCheckMode();// 选中模式
		boolean isParentNote = info.isParentNote(); // 是否是根节点

		viewHolder.iv_icon.setImageResource(R.drawable.image_icon_expanded);
		if (isParentNote) {
			viewHolder.iv_icon.setVisibility(View.VISIBLE);
		} else {
			viewHolder.iv_icon.setVisibility(View.INVISIBLE);
		}
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

		private ImageView iv_icon; // 图标
		private TextView tv_name; // 名字
		private ImageView iv_select_status; // 图片选中的状态
	}
}
