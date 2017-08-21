/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.util.ArrayList;
import java.util.List;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author wesley
 * @date: 2017-2-9
 * @Description: 联系人适配器
 */
public class SelectInnerContactAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<InnerContractsInfo> list;
	private OnViewClickListener listener;
	private int padding; // 正常的padding
	private int padding_with_root_note;// 有根节点时的padding
	private int padding_with_root_child_note; // 有根节点和字节点的padding

	public SelectInnerContactAdapter(Context context) {
		if (context != null) {
			inflater = LayoutInflater.from(context);
			padding = (int) context.getResources().getDimension(R.dimen.item_email_list_margin);
			padding_with_root_note = padding + 51;
			padding_with_root_child_note = padding + 41;
		}
	}

	public void setOnViewClickListener(OnViewClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 获取选中的item
	 * 
	 * @return
	 */
	public List<InnerContractsInfo> getSelectItems() {
		List<InnerContractsInfo> listSelect = new ArrayList<>();
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					int checkMode = info.getCheckMode();
					if (checkMode == 2) {
						listSelect.add(info);
					}
				}
			}
		}
		return getSelectItemsNoSame(listSelect);
	}

	/**
	 * 获取不重复的items
	 * 
	 * @param list
	 * @return
	 */
	public List<InnerContractsInfo> getSelectItemsNoSame(List<InnerContractsInfo> list) {
		List<InnerContractsInfo> listSelect = new ArrayList<>();

		if (list == null) {
			return listSelect;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			InnerContractsInfo info = list.get(i);
			if (info != null) {
				boolean hasParentNote = info.isHasRootNode();
				if (hasParentNote) {
					InnerContractsInfo parentNote = info.getParentNode();
					if (parentNote != null) {
						int checkMode = parentNote.getCheckMode();
						if (checkMode != 2) {
							listSelect.add(info);
						}
					}
				} else {
					listSelect.add(info);
				}
			}
		}

		return listSelect;
	}

	/**
	 * 改变选中的状态
	 * 
	 * @param position
	 *            当前点击的位置
	 */
	public void changeSelectStatus(int position) {
		if (isItemSelect(position)) {
			setItemIsChecked(position, 0);
			if (checkHasChildNote(position)) {
				setItemIsChecked(position, 0);
				setChildNoteIsSelect(position, 0);
				if (checkHasParentNote(position)) {
					if (checkAllNoteisNotSelectInParent(position)) {
						setParentNoteStatus(position, 0);
					} else {
						setParentNoteStatus(position, 1);
					}
				}
				notifyDataSetChanged();
			} else {
				setItemIsChecked(position, 0);
				if (checkHasParentNote(position)) {
					if (isHasSelectInParent(position)) {
						setParentNoteStatus(position, 1);
					} else {
						setParentNoteStatus(position, 0);
					}
				}
				notifyDataSetChanged();
			}
		} else {
			setItemIsChecked(position, 2);
			if (checkHasChildNote(position)) {
				setChildNoteIsSelect(position, 2);
				if (checkHasParentNote(position)) {
					if (checkAllNoteIsSelectInParent(position)) {
						setParentNoteStatus(position, 2);
					} else {
						setParentNoteStatus(position, 1);
					}
				}
			} else if (checkHasParentNote(position)) {
				if (checkAllNoteIsSelectInParent(position)) {
					setParentNoteStatus(position, 2);
				} else {
					setParentNoteStatus(position, 1);
				}
			} else {

			}
			notifyDataSetChanged();
		}
	}

	// 设置子节点全部选中或者全部不选中
	private void setChildNoteIsSelect(int position, int checkMode) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				List<InnerContractsInfo> childList = info.getChildList();
				if (childList != null) {
					int parentSize = list.size();
					int size = childList.size();
					for (int i = 0; i < size; i++) {
						int childPosition = position + i + 1;
						if (childPosition < parentSize) {
							list.get(childPosition).setCheckMode(checkMode);
						}
					}
				}
			}
		}
	}

	// 设置父节点的状态
	private void setParentNoteStatus(int position, int checkMode) {
		int parentPosition = getParentNotePosition(position);
		if (list != null && list.size() > parentPosition && parentPosition >= 0) {
			list.get(parentPosition).setCheckMode(checkMode);
		}
	}

	// 获取父节点在列表中的位置
	private int getParentNotePosition(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				InnerContractsInfo parentNote = info.getParentNode();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					InnerContractsInfo currentNote = list.get(i);
					if (currentNote != null && currentNote.equals(parentNote)) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	// 设置当前位置以选中
	private void setItemIsChecked(int position, int checkMode) {
		if (list != null && list.size() > position) {
			list.get(position).setCheckMode(checkMode);
		}
	}

	// 判断子元素是否有选中的
	private boolean isHasChildItemSelect(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				List<InnerContractsInfo> childList = info.getChildList();
				if (childList != null) {
					int size = childList.size();
					for (int i = 0; i < size; i++) {
						InnerContractsInfo inner = childList.get(i);
						if (inner != null) {
							int checkMode = inner.getCheckMode();
							if (checkMode == 2 || checkMode == 1) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	// 判断子节点是否全部选中
	private boolean isAllChildItemSelect(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				List<InnerContractsInfo> childList = info.getChildList();
				if (childList != null) {
					int size = childList.size();
					for (int i = 0; i < size; i++) {
						InnerContractsInfo inner = childList.get(i);
						if (inner != null) {
							int checkMode = inner.getCheckMode();
							if (checkMode == 2 || checkMode == 1) {
								continue;
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	// 判断父节点下面的子节点是否有选中的
	private boolean isHasSelectInParent(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				InnerContractsInfo parentNode = info.getParentNode();
				if (parentNode != null) {
					List<InnerContractsInfo> childList = parentNode.getChildList();
					if (childList != null) {
						int size = childList.size();
						for (int i = 0; i < size; i++) {
							InnerContractsInfo inner = childList.get(i);
							if (inner != null) {
								int checkMode = inner.getCheckMode();
								if (checkMode == 2 || checkMode == 1) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	// 判断父节点下面的子节点是否全部都没有选中
	private boolean checkAllNoteisNotSelectInParent(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				InnerContractsInfo parentNode = info.getParentNode();
				if (parentNode != null) {
					List<InnerContractsInfo> childList = parentNode.getChildList();
					if (childList != null) {
						int size = childList.size();
						for (int i = 0; i < size; i++) {
							InnerContractsInfo inner = childList.get(i);
							if (inner != null) {
								int checkMode = inner.getCheckMode();
								if (checkMode == 2 || checkMode == 1) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	// 判断父节点下面的子节点是否全部都选中了
	private boolean checkAllNoteIsSelectInParent(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				InnerContractsInfo parentNode = info.getParentNode();
				if (parentNode != null) {
					List<InnerContractsInfo> childList = parentNode.getChildList();
					if (childList != null) {
						int size = childList.size();
						for (int i = 0; i < size; i++) {
							InnerContractsInfo inner = childList.get(i);
							if (inner != null) {
								int checkMode = inner.getCheckMode();
								if (checkMode == 2) {
									continue;
								} else {
									return false;
								}
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	// 设置item选中
	private void setItemIsSelect(int position, int checkMode) {
		if (list != null && list.size() > position) {
			list.get(position).setCheckMode(checkMode);
			notifyDataSetChanged();
		}
	}

	// 判断当前位置是否有子节点
	private boolean checkHasChildNote(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				List<InnerContractsInfo> childList = info.getChildList();
				if (childList != null && childList.size() > 0) {
					return true;
				}
			}
		}

		return false;
	}

	// 判断当前位置是否有父节点
	private boolean checkHasParentNote(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			if (info != null) {
				return info.isHasRootNode();
			}
		}
		return false;
	}

	/**
	 * 判断item是否选中
	 * 
	 * @param position
	 *            当前的位置
	 * @return true:选中 false:没选中
	 */
	public boolean isItemSelect(int position) {
		if (list != null && list.size() > position) {
			int checkMode = list.get(position).getCheckMode();
			if (checkMode == 0 || checkMode == 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断item是否可以展开
	 * 
	 * @param position
	 *            当前的位置
	 * @return true:可以展开 false:不能展开
	 */
	public boolean isCanExpand(int position) {
		if (list != null && list.size() > position) {
			String sum = list.get(position).getSum();
			if (sum != null && !sum.equals("0")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前位置是否已经展开了
	 * 
	 * @param position
	 *            当前位置
	 * @return true:已经展开 false:没有展开
	 */
	public boolean isExpanded(int position) {
		if (list != null && list.size() > position) {
			return list.get(position).isExpanded();
		}
		return false;
	}

	/**
	 * 设置当前位置展开或者不展开
	 * 
	 * @param position
	 *            当前位置
	 * @param isExpanded
	 *            是否展开了
	 */
	public void setItemIsExpanded(int position, boolean isExpanded) {
		if (list != null && list.size() > position) {
			list.get(position).setExpanded(isExpanded);
		}
	}

	/**
	 * 获取当前位置的对象
	 * 
	 * @param position
	 *            当前的位置
	 * @return 联系人对象
	 */
	public InnerContractsInfo getItemValue(int position) {
		if (list != null && list.size() > position) {
			return list.get(position);
		}
		return null;
	}

	/**
	 * 获取所有的list
	 * 
	 * @return 联系人列表集合
	 */
	public List<InnerContractsInfo> getAllItems() {
		return list;
	}

	/**
	 * 判断下一级数据在当前列表中的位置
	 * 
	 * @param childList
	 *            child数据集合
	 * @return 当前列表中的位置
	 */
	public int getCurrentPosition(List<InnerContractsInfo> childList) {

		if (list == null || childList == null || childList.size() < 1) {
			return -1;
		}
		String childId = childList.get(0).getId(); // 子节点第一个位置的id
		int size = list.size();
		for (int i = 0; i < size; i++) {
			String id = list.get(i).getId();
			if (id != null && id.equals(childId)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 从当前节点位置开始插入数据
	 * 
	 * @param childList
	 *            子节点数据集合
	 * @param currentPosition
	 *            当前节点位置
	 */
	public void insertChildData(List<InnerContractsInfo> childList, int currentPosition) {

		if (list != null && list.size() > currentPosition && childList != null) {
			InnerContractsInfo info = list.get(currentPosition);
			int childSize = childList.size();
			List<InnerContractsInfo> child = new ArrayList<>();
			for (int i = 1; i < childSize; i++) {
				InnerContractsInfo innerInfo = childList.get(i);
				if (child != null && !child.contains(innerInfo)) {
					innerInfo.setHasRootNode(true);
					innerInfo.setParentNode(info);
					child.add(innerInfo);
				}
			}
			boolean hasChildListId = checkHasChildListId(info);
			if (hasChildListId) {
				setDefaultCheckMode(info.getChildIdList(), childList);
			}
			info.setChildList(child);
			list.set(currentPosition, info);
			for (int i = 1; i < childSize; i++) {
				int position = i + currentPosition;
				if (position <= list.size()) {
					list.add(position, childList.get(i));
				}
			}
			notifyDataSetChanged();
		}
	}

	/**
	 * 设置默认选中的状态
	 * 
	 * @param idList
	 * @param childList
	 */
	public void setDefaultCheckMode(List<InnerContractsInfo> idList, List<InnerContractsInfo> childList) {
		if (idList == null || childList == null) {
			return;
		}
		int sizeId = idList.size();
		int sizeChild = childList.size();
		for (int i = 0; i < sizeId; i++) {
			InnerContractsInfo childInfo = idList.get(i);
			for (int j = 0; j < sizeChild; j++) {
				InnerContractsInfo info = childList.get(j);
				if (info != null && childInfo != null) {
					String childId = childInfo.getId();
					String id = info.getId();
					if (id != null && childId != null && id.equals(childId)) {
						int checkMode = childInfo.getCheckMode();
						childList.get(j).setCheckMode(checkMode);
					}
				}
			}
		}
	}

	/**
	 * 判断是否有childListid
	 * 
	 * @param info
	 *            联系人对象
	 * @return true:有 false:没有
	 */
	public boolean checkHasChildListId(InnerContractsInfo info) {

		if (info != null) {
			List<InnerContractsInfo> idList = info.getChildIdList();
			if (idList != null && idList.size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 不展开
	 * 
	 * @param position
	 *            列表中的位置
	 */
	public void setNotExpanded(int position) {
		if (list != null && list.size() > position) {
			InnerContractsInfo info = list.get(position);
			List<InnerContractsInfo> childList = info.getChildList();
			if (childList != null && childList.size() > 0 && list.containsAll(childList)) {
				list.removeAll(childList);
			}
			if (list != null && list.size() > position) {
				list.get(position).setChildList(null);
				if (checkChildNoteHasSelect(childList)) {
					List<InnerContractsInfo> idList = getChildSelectIdList(childList);
					list.get(position).setChildIdList(idList);
				}
			}
			notifyDataSetChanged();
		}
	}

	/**
	 * 判断子节点是否有选中的
	 * 
	 * @param list
	 *            子节点集合
	 * @return true:有选中的 false:没有选中的
	 */
	public boolean checkChildNoteHasSelect(List<InnerContractsInfo> list) {
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					int checkMode = info.getCheckMode();
					if (checkMode == 1 || checkMode == 2) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取子节点选中的id集合
	 * 
	 * @param list
	 *            集合
	 * @return id集合
	 */
	public List<InnerContractsInfo> getChildSelectIdList(List<InnerContractsInfo> list) {
		List<InnerContractsInfo> idList = new ArrayList<>();
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				InnerContractsInfo info = list.get(i);
				if (info != null) {
					int checkMode = info.getCheckMode();
					if (checkMode == 1 || checkMode == 2) {
						idList.add(info);
					}
				}
			}
		}

		return idList;
	}

	/**
	 * 通知列表数据已改变
	 * 
	 * @param list
	 *            列表数据
	 */
	public void notifyDataSetChanged(List<InnerContractsInfo> list) {
		this.list = list;
		notifyDataSetChanged();
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
			viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.item_inner_contact_image);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.item_inner_contact_name);
			viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.item_inner_contact_image_select);
			viewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.item_inner_contact_rela);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 取值
		InnerContractsInfo info = list.get(position);
		String name = info.getName(); // 姓名
		String sum = info.getSum(); // 总数
		int checkMode = info.getCheckMode(); // 选中的模式
		boolean hasRootNode = info.isHasRootNode(); // 是否有根节点

		viewHolder.tv_name.setText(name);
		if (hasRootNode) {
			if (sum != null && !sum.equals("0")) {
				viewHolder.rl_item.setPadding(padding_with_root_child_note, 0, padding, 0);
			} else {
				viewHolder.rl_item.setPadding(padding_with_root_note, 0, padding, 0);
			}
			InnerContractsInfo parentNode = info.getParentNode();
			if (parentNode != null) {
				int check_mode_parent = parentNode.getCheckMode();
				if (check_mode_parent == 0 || check_mode_parent == 2) {
					info.setCheckMode(check_mode_parent);
					updateCheckStatus(check_mode_parent, viewHolder.iv_select);
				} else {
					updateCheckStatus(checkMode, viewHolder.iv_select);
				}
			}
		} else {
			viewHolder.rl_item.setPadding(padding, 0, padding, 0);
			updateCheckStatus(checkMode, viewHolder.iv_select);
		}
		if (sum != null && !sum.equals("0")) {
			viewHolder.iv_image.setVisibility(View.VISIBLE);
			boolean isExpanded = info.isExpanded(); // 是否已经展开
			if (isExpanded) {
				viewHolder.iv_image.setImageResource(R.drawable.image_icon_expanded);
			} else {
				viewHolder.iv_image.setImageResource(R.drawable.image_icon_no_expanded);
			}
		} else {
			viewHolder.iv_image.setVisibility(View.GONE);
		}

		// 点击事件
		viewHolder.iv_select.setOnClickListener(new OnViewClick(position));
		viewHolder.rl_item.setOnClickListener(new OnViewClick(position));

		return convertView;
	}

	// 改变选中的状态
	private void updateCheckStatus(int checkMode, ImageView iv_select) {
		switch (checkMode) {
		/**
		 * 未选中
		 */
		case 0:
			iv_select.setImageResource(R.drawable.image_icon_no_select);
			break;

		/**
		 * 部分选中
		 */
		case 1:
			iv_select.setImageResource(R.drawable.image_icon_part_select);
			break;

		/**
		 * 全部选中
		 */
		case 2:
			iv_select.setImageResource(R.drawable.image_icon_all_select);
			break;

		default:
			break;
		}
	}

	// 内部类---点击事件
	private class OnViewClick implements OnClickListener {

		private int position;

		public OnViewClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			if (view == null) {
				return;
			}

			int key = view.getId();
			if (key == R.id.item_inner_contact_image_select) {
				if (listener != null) {
					listener.onViewClick(position);
				}
			} else if (key == R.id.item_inner_contact_rela) {
				if (listener != null) {
					listener.onItemClick(position);
				}
			}
		}
	}

	// 静态内部类
	private static final class ViewHolder {
		private ImageView iv_image; // 下拉指示
		private TextView tv_name; // 名字
		private ImageView iv_select; // 是否选中
		private RelativeLayout rl_item; // item的布局
	}
}
