/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.util.ArrayList;
import java.util.List;

import com.zfsoft.core.view.textdrawable.ColorGenerator;
import com.zfsoft.core.view.textdrawable.TextDrawable;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.util.SizeUtils;
import com.zfsoft.zf_new_email.widget.swipemenulistview.BaseSwipListAdapter;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author wesley
 * @date 2016-10-18上午11:08:20
 * @Description: 邮件列表适配器
 */
public class EmailListAdapter extends BaseSwipListAdapter {

	private LayoutInflater inflater;
	private ArrayList<Email> list = new ArrayList<>();
	private LayoutParams paramsNoRead;
	private LayoutParams params;
	private boolean isRefreshing; // true:下拉刷新 false:滚动加载
	private ColorGenerator colorGenerator;
	private int inbox_type;
	private int color_select;
	private int color_un_select;
	private boolean isItemLongClick;

	public EmailListAdapter(Context context) {
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

			color_select = context.getResources().getColor(R.color.color_f5f5f5);
			color_un_select = context.getResources().getColor(R.color.color_white);
		}
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
	public int getItemViewType(int position) {
		if (list != null && list.size() > position && list.get(position) != null) {
			if (inbox_type == 2) {
				return 4;
			}
			boolean isNews = list.get(position).isNews();
			boolean isFlaged = list.get(position).isFlaged();
			if (isNews && isFlaged) {
				return 0; // 删除 标记已读 取消星标
			}

			if (!isNews && isFlaged) {
				return 1; // 删除 标记未读 取消星标
			}

			if (isNews && !isFlaged) {
				return 2; // 删除 标记已读 添加星标
			}

			if (!isNews && !isFlaged) {
				return 3; // 删除 标记未读 添加星标
			}
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public boolean getSwipEnableByPosition(int position) {
		if (isItemLongClick) {
			return false;
		}
		return true;
	}

	public void addItems(ArrayList<Email> list) {
		if (isRefreshing) {
			this.list.clear();
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void refreshData() {
		list.clear();
		notifyDataSetChanged();
	}

	public ArrayList<Email> getAllItems() {
		return list;
	}

	public void removeItem(int position) {
		if (list != null && list.size() > position) {
			list.remove(position);
			notifyDataSetChanged();
		}
	}

	public void setIsRefreshing(boolean isRefreshing) {
		this.isRefreshing = isRefreshing;
	}

	public void setInboxType(int type) {
		this.inbox_type = type;
	}

	public void updateItemStatus(ArrayList<Email> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setItemSelected(int position) {
		if (list != null && list.size() > position) {
			int selected = list.get(position).getSelected();
			switch (selected) {
			case 0:
				list.get(position).setSelected(1);
				break;

			case 1:
				list.get(position).setSelected(0);
				break;

			default:
				break;
			}
			notifyDataSetChanged();
		}
	}

	public boolean checkSelectedCountSameToTotal() {
		if (list == null) {
			return false;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			int selected = list.get(i).getSelected();
			if (selected == 0) {
				return false;
			}
		}

		return true;
	}

	public void clearItemSelected() {
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				list.get(i).setSelected(0);
			}
			notifyDataSetChanged();
		}
	}

	public void insertItem(int position, Email email) {
		if (list != null && list.size() >= position && position >= 0) {
			list.add(position, email);
			notifyDataSetChanged();
		}
	}

	public void selectAllItem() {
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				list.get(i).setSelected(1);
			}
			notifyDataSetChanged();
		}
	}

	public void setOnItemLongClick(boolean isItemLongClick) {
		this.isItemLongClick = isItemLongClick;
	}

	public ArrayList<Email> getSelectedItem() {
		if (list == null) {
			return null;
		}
		ArrayList<Email> listSelected = new ArrayList<>();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			int isSelected = list.get(i).getSelected();
			if (isSelected == 1) {
				listSelected.add(list.get(i));
			}
		}
		return listSelected;
	}

	public void deleteItems(ArrayList<Email> list) {
		if (this.list != null && list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Email email = list.get(i);
				if (email == null) {
					return;
				}
			}
			this.list.removeAll(list);
			notifyDataSetChanged();
		}
	}

	public List<Email> getselectedItem() {
		ArrayList<Email> listSelected = new ArrayList<>();
		if (list == null) {
			return listSelected;
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			int isSelected = list.get(i).getSelected();
			if (isSelected == 1) {
				listSelected.add(list.get(i));
			}
		}
		return listSelected;
	}

	public List<Integer> getSelectItemPosition() {
		ArrayList<Integer> listSelected = new ArrayList<>();
		if (list == null) {
			return listSelected;
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			int isSelected = list.get(i).getSelected();
			if (isSelected == 1) {
				listSelected.add(i);
			}
		}
		return listSelected;
	}

	public void updateMailGroupStatus(List<Integer> list, int status, int type, List<Email> listEmail) {

		if (type == 1) {
			if (listEmail != null && this.list != null && this.list.size() >= listEmail.size() && status == 0) {
				this.list.removeAll(listEmail);
				notifyDataSetChanged();
			}
		} else {
			if (list != null && this.list != null && this.list.size() >= list.size()) {
				int size = list.size();
				for (int i = 0; i < size; i++) {
					int position = list.get(i);
					switch (status) {
					/**
					 * 已读
					 */
					case 0:
						if (this.list.size() > position) {
							this.list.get(position).setNews(false);
						}
						break;

					/**
					 * 未读
					 */
					case 1:
						if (this.list.size() > position) {
							this.list.get(position).setNews(true);
						}
						break;

					default:
						break;
					}
				}
				notifyDataSetChanged();
			}
		}
	}

	public void markMailGroupStatus(List<Integer> list, int status, int type, List<Email> listEmail, int email_type, int type_in_oa) {

		if (email_type == Constant.EMAIL_TYPE_OA) {
			if (type_in_oa == 5) {
				removeMailMarkStarInOA(listEmail, status);
			} else {
				markMailInOA(list, status);
			}
		} else {
			if (type == 5) {
				removeMailMarkStarInOA(listEmail, status);
			} else {
				markMailInOA(list, status);
			}
		}
	}

	// 标记oa邮件
	private void removeMailMarkStarInOA(List<Email> listEmail, int status) {
		if (listEmail != null && this.list != null && this.list.size() >= listEmail.size() && status == 0) {
			this.list.removeAll(listEmail);
			notifyDataSetChanged();
		}
	}

	// 标记oa邮件
	private void markMailInOA(List<Integer> list, int status) {
		if (list != null && this.list != null && this.list.size() >= list.size()) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				int position = list.get(i);
				switch (status) {
				/**
				 * 取消星标
				 */
				case 0:
					if (this.list.size() > position) {
						this.list.get(position).setFlaged(false);
					}
					break;

				/**
				 * 添加星标
				 */
				case 1:
					if (this.list.size() > position) {
						this.list.get(position).setFlaged(true);
					}
					break;

				default:
					break;
				}
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (list == null || list.size() <= position || inflater == null) {
			return null;
		}
		ViewHoler viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHoler();
			convertView = inflater.inflate(R.layout.item_email_list, parent, false);
			viewHolder.tv_noRead = (TextView) convertView.findViewById(R.id.item_email_list_no_read);
			viewHolder.tv_sendName = (TextView) convertView.findViewById(R.id.item_email_list_user);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.item_email_list_time);
			viewHolder.iv_attachment = (ImageView) convertView.findViewById(R.id.item_email_list_attachment);
			viewHolder.tv_subject = (TextView) convertView.findViewById(R.id.item_email_list_subject);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.item_email_list_content);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_email_list_icon);
			viewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.item_email_relative);
			viewHolder.iv_star = (ImageView) convertView.findViewById(R.id.item_email_iconn_star);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHoler) convertView.getTag();
		}

		Email email = list.get(position);
		if (email == null) {
			return null;
		}

		int select = email.getSelected(); // 是否被选中
		boolean isNew = email.isNews(); // 是否是未读 true:未读 false:已读
		String senderName = email.getSenderName(); // 发送人姓名
		String tiem = email.getSentdata(); // 发送时间
		String subject = email.getSubject(); // 主题
		String content = email.getContent(); // 内容
		boolean isFlaged = email.isFlaged(); // 是否是星标邮件
		boolean isHtml = email.isHtml();
		if (isHtml) {
			content = "这是一封网页邮件";
		}
		boolean isHasAttachment = email.isHasAttachment();

		if (TextUtils.isEmpty(senderName)) {
			String address = email.getSendderAddress();
			viewHolder.tv_sendName.setText(address);
		} else {
			viewHolder.tv_sendName.setText(senderName);
		}
		viewHolder.tv_time.setText(tiem);
		viewHolder.tv_subject.setText(subject);
		if (TextUtils.isEmpty(content)) {
			viewHolder.tv_content.setVisibility(View.GONE);
		} else {
			viewHolder.tv_content.setVisibility(View.GONE);
			viewHolder.tv_content.setText(content);
		}
		if (inbox_type == 2) {
			viewHolder.tv_noRead.setVisibility(View.GONE);
			viewHolder.tv_sendName.setLayoutParams(params);
			viewHolder.iv_star.setVisibility(View.GONE);
		} else {
			if (isNew) {
				viewHolder.tv_noRead.setVisibility(View.VISIBLE);
				viewHolder.tv_sendName.setLayoutParams(paramsNoRead);
			} else {
				viewHolder.tv_noRead.setVisibility(View.GONE);
				viewHolder.tv_sendName.setLayoutParams(params);
			}

			if (isFlaged) {
				viewHolder.iv_star.setVisibility(View.VISIBLE);
			} else {
				viewHolder.iv_star.setVisibility(View.GONE);
			}
		}

		if (isItemLongClick) {
			if (select == 1) {
				viewHolder.iv_icon.setImageResource(R.drawable.item_select);
			} else {
				viewHolder.iv_icon.setImageResource(R.drawable.item_select_no);
			}
		} else {
			if (TextUtils.isEmpty(senderName)) {
				senderName = "邮";
			}
			TextDrawable drawable = TextDrawable.builder().buildRound("邮", colorGenerator.getColor(senderName));
			viewHolder.iv_icon.setImageDrawable(drawable);
		}

		if (select == 1) {
			viewHolder.rl_item.setBackgroundColor(color_select);
		} else {
			viewHolder.rl_item.setBackgroundColor(color_un_select);
		}

		viewHolder.iv_attachment.setVisibility(isHasAttachment ? View.VISIBLE : View.GONE);
		return convertView;
	}

	// 静态内部类
	private static final class ViewHoler {

		private TextView tv_noRead; // 未读
		private TextView tv_sendName;// 发信人
		private TextView tv_time; // 发信时间
		private ImageView iv_attachment; // 附件图标
		private TextView tv_subject; // 主题
		private TextView tv_content; // 内容
		private ImageView iv_icon; // 图标
		private ImageView iv_star; // 星标
		private RelativeLayout rl_item; // item的布局
	}
}
