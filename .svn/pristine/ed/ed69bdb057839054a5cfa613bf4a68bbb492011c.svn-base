/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zfsoft.core.data.WebserviceConf;
import com.zfsoft.core.utils.FileManager;
import com.zfsoft.core.utils.PreferenceHelper;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.modules.emaildetail.EmailDetailActivity;
import com.zfsoft.zf_new_email.modules.emailsearch.EmailSearchActivity;
import com.zfsoft.zf_new_email.modules.emailsendorreply.EmailSendOrReplyActivity;
import com.zfsoft.zf_new_email.modules.emailsettings.EmailSettingActivity;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;
import com.zfsoft.zf_new_email.util.SizeUtils;
import com.zfsoft.zf_new_email.widget.swipemenulistview.SwipeMenu;
import com.zfsoft.zf_new_email.widget.swipemenulistview.SwipeMenuCreator;
import com.zfsoft.zf_new_email.widget.swipemenulistview.SwipeMenuItem;
import com.zfsoft.zf_new_email.widget.swipemenulistview.SwipeMenuListView;
import com.zfsoft.zf_new_email.widget.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

/**
 * @author wesley
 * @date 2016-10-17下午3:46:28
 * @Description: 邮件列表的fragment
 */
public class EmailListFragment extends BaseFragment<EmailListPresenter> implements EmailListContract.View, OnClickListener {

	private static final int PAGE_SIZE = 10;
	private static final int REQUEST_CODE_WRITE_MAIL = 1;
	private int email_type; // 邮件类型
	private SwipeRefreshLayout srfh_refresh; // 刷新控件
	private SwipeMenuListView listView; // listview
	private EmailListAdapter adapter;
	private int startPosition = 0;
	private int startPage = 1; // 开始的页数
	private TextView tv_title;
	private boolean isLoading = false; // 是否正在加载
	private boolean isRefreshing = false; // 是否正在刷新
	private boolean isLoadAll = false; // 是否全部加載完成
	private int type = 0; // 收件箱 已删除 已发送
	private int type_in_oa = 1; // 1:收件箱 2:草稿箱 3：已發送 4：未读邮件 5:星标邮件 6:已删除邮件
	private LinearLayout ll_write_mail; // 写邮件布局
	private TextView tv_write_mail; // 写邮件文本
	private ImageView iv_write_mail; // 写邮件图标
	private LinearLayout ll_search; // 搜索的布局
	private TextView tv_search; // 搜索的文本
	private ImageView iv_search; // 搜索的图标
	private LinearLayout ll_menu; // 菜单的布局
	private TextView tv_menu; // 菜单的文本
	private ImageView iv_menu; // 菜单的图标
	private ImageView iv_loading; // 动画图片
	private AnimationDrawable animationDrawable; // 动画
	private int isItemLongClick = 0; // 是否是长按 0:不是 1:是
	private OnHomeClickListener onHomeClickListener;
	private ProgressDialog dialog; // 对话框
	private PopupWindow popupWindow;
	private TextView tv_mark_or_not; // 标记或者不标记
	private TextView tv_star; // 添加星标
	private LinearLayout ll_cancle_mark_star; // 取消星标
	private View viewLine; // 线
	private ImageView iv_scroll_to_top; // 滑动到顶部
	private LinearLayout ll_empty_view; // 没有邮件的时候显示的布局
	private String url;
	private String app_token;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_email_list;
	}

	@Override
	public void initVariables() {
		handleArguments();
		adapter = new EmailListAdapter(context);
	}

	// 处理arguments
	private void handleArguments() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			email_type = bundle.getInt("email_type");
		}
	}

	@Override
	public void initViews(View view) {
		srfh_refresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_email_recycler);
		listView = (SwipeMenuListView) view.findViewById(R.id.fragment_email_list);
		tv_title = (TextView) ((EmailListActivity) (context)).findViewById(R.id.include_head_title);
		tv_title.setText(R.string.email_get);
		srfh_refresh.setColorSchemeResources(R.color.color_red, R.color.color_bule, R.color.color_email_blue_text);
		ll_write_mail = (LinearLayout) ((EmailListActivity) (context)).findViewById(R.id.email_list_write_mail_linear);
		tv_write_mail = (TextView) ((EmailListActivity) (context)).findViewById(R.id.email_list_write_mail);
		iv_write_mail = (ImageView) ((EmailListActivity) (context)).findViewById(R.id.email_list_write_mail_icon);
		ll_search = (LinearLayout) ((EmailListActivity) (context)).findViewById(R.id.email_list_search_linear);
		tv_search = (TextView) ((EmailListActivity) (context)).findViewById(R.id.email_list_search);
		iv_search = (ImageView) ((EmailListActivity) (context)).findViewById(R.id.email_list_search_icon);
		ll_menu = (LinearLayout) ((EmailListActivity) (context)).findViewById(R.id.email_list_menu_linear);
		tv_menu = (TextView) ((EmailListActivity) (context)).findViewById(R.id.email_list_menu);
		iv_menu = (ImageView) ((EmailListActivity) (context)).findViewById(R.id.email_list_menu_icon);
		iv_loading = (ImageView) ((EmailListActivity) (context)).findViewById(R.id.include_head_loading);
		iv_scroll_to_top = (ImageView) ((EmailListActivity) (context)).findViewById(R.id.email_list_image_scroll);
		animationDrawable = (AnimationDrawable) iv_loading.getBackground();
		ll_empty_view = (LinearLayout) view.findViewById(R.id.common_empty_view);

		initSwipeMenuCreator();
		listView.setAdapter(adapter);
		listView.setOnMenuItemClickListener(new OnMenuClickListView());
		listView.setOnItemClickListener(new OnItemClickListView());
		listView.setOnScrollListener(new OnScrollListView());
		listView.setOnItemLongClickListener(new OnItemLongClickListView());
		srfh_refresh.setOnRefreshListener(new OnRefreshListView());
		ll_write_mail.setOnClickListener(this);
		ll_search.setOnClickListener(this);
		ll_menu.setOnClickListener(this);
		iv_scroll_to_top.setOnClickListener(this);

		loading();
		switch (email_type) {
		case Constant.EMAIL_TYPE_OA:
			url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
			app_token = PreferenceHelper.token_read(context);
			loadDataInOA(type_in_oa, startPage, PAGE_SIZE, url, app_token, true);
			break;

		default:
			loadData(email_type, startPosition, true);
			break;
		}
	}

	public static EmailListFragment newInstance(int email_type) {
		Bundle bundle = new Bundle();
		bundle.putInt("email_type", email_type);
		EmailListFragment fragment = new EmailListFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	// 初始化SwipeMenuCreator
	private void initSwipeMenuCreator() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {

				// 删除
				SwipeMenuItem openItem = new SwipeMenuItem(context.getApplicationContext());
				openItem.setBackground(R.color.color_red_light);
				openItem.setWidth(SizeUtils.dp2px(context, 80));
				openItem.setTitle(getResources().getString(R.string.delete));
				openItem.setTitleSize(16);
				openItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(openItem);

				// 标记未读和已读
				SwipeMenuItem deleteItem = new SwipeMenuItem(context.getApplicationContext());
				deleteItem.setBackground(R.color.color_gray_light);
				deleteItem.setWidth(SizeUtils.dp2px(context, 100));
				deleteItem.setTitleSize(16);
				deleteItem.setTitleColor(Color.WHITE);

				// 添加星标和取消星标
				SwipeMenuItem markItem = new SwipeMenuItem(context.getApplicationContext());
				markItem.setBackground(R.color.color_yellow_light);
				markItem.setWidth(SizeUtils.dp2px(context, 100));
				markItem.setTitleSize(16);
				markItem.setTitleColor(Color.WHITE);

				int type = menu.getViewType();
				switch (type) {
				/**
				 * 删除 标记已读 取消星标
				 */
				case 0:
					deleteItem.setTitle(getResources().getString(R.string.mark_read));
					menu.addMenuItem(deleteItem);
					markItem.setTitle(getResources().getString(R.string.mark_un_star));
					menu.addMenuItem(markItem);
					break;

				/**
				 * 删除 标记未读 取消星标
				 */
				case 1:
					deleteItem.setTitle(getResources().getString(R.string.mark_un_read));
					menu.addMenuItem(deleteItem);
					markItem.setTitle(getResources().getString(R.string.mark_un_star));
					menu.addMenuItem(markItem);
					break;

				/**
				 * 删除 标记已读 添加星标
				 */
				case 2:
					deleteItem.setTitle(getResources().getString(R.string.mark_read));
					menu.addMenuItem(deleteItem);
					markItem.setTitle(getResources().getString(R.string.mark_star));
					menu.addMenuItem(markItem);
					break;

				/**
				 * 删除 标记未读 添加星标
				 */
				case 3:
					deleteItem.setTitle(getResources().getString(R.string.mark_un_read));
					menu.addMenuItem(deleteItem);
					markItem.setTitle(getResources().getString(R.string.mark_star));
					menu.addMenuItem(markItem);
					break;

				/**
				 * 刪除
				 */
				case 4:
					break;

				default:
					break;
				}
			}
		};
		listView.setMenuCreator(creator);
	}

	// 下拉刷新
	private class OnRefreshListView implements OnRefreshListener {

		@Override
		public void onRefresh() {
			if (isLoading) {
				return;
			}
			isRefreshing = true;
			srfh_refresh.setRefreshing(true);
			switch (email_type) {
			case Constant.EMAIL_TYPE_OA:
				startPage = 1;
				loadDataInOA(type_in_oa, startPage, PAGE_SIZE, url, app_token, true);
				break;

			default:
				startPosition = 0;
				loadData(email_type, startPosition, true);
				break;
			}
		}
	}

	// 滚动加载
	private class OnScrollListView implements OnScrollListener {

		private int positions;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			switch (scrollState) {
			/**
			 * 滚动之前
			 */
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				positions = view.getLastVisiblePosition();
				break;

			/**
			 * 不滚动时
			 */
			case OnScrollListener.SCROLL_STATE_IDLE:
				int lastVisiblePosition = view.getLastVisiblePosition();
				int count = view.getCount();
				if (email_type == Constant.EMAIL_TYPE_OA) {
					if (lastVisiblePosition + 1 == count && !isLoading && !isRefreshing && !isLoadAll && isItemLongClick == 0) {
						loadDataInOA(type_in_oa, startPage, PAGE_SIZE, url, app_token, false);
					}
				} else {
					if (lastVisiblePosition + 1 == count && !isLoading && !isRefreshing && !isLoadAll && isItemLongClick == 0) {
						loadData(email_type, startPosition, false);
					}
				}
				break;

			default:
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			int endPositon = view.getLastVisiblePosition();
			int position = view.getFirstVisiblePosition();
			if (endPositon >= positions || position == 0) {
				iv_scroll_to_top.setVisibility(View.GONE);
			} else if (endPositon < positions) {
				iv_scroll_to_top.setVisibility(View.VISIBLE);
			}
		}
	}

	// 内部类---ListView长安点击事件
	private class OnItemLongClickListView implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			isItemLongClick = 1;
			updateView(isItemLongClick);
			srfh_refresh.setEnabled(false);
			adapter.setOnItemLongClick(true);
			adapter.setItemSelected(position);
			onHomeClickListener.setHomeEnable(false);
			if (adapter.checkSelectedCountSameToTotal()) {
				tv_menu.setText(R.string.cancle_select_all);
				ll_menu.setTag("1");
			} else {
				tv_menu.setText(R.string.select_all);
				ll_menu.setTag("0");
			}
			return true;
		}
	}

	// 侧滑菜单点击事件
	private class OnMenuClickListView implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

			if (index == 0) {
				deleteMail(position);
			}
			int viewType = menu.getViewType();
			switch (viewType) {
			/**
			 * 删除 标记已读 取消星标
			 */
			case 0:
				if (index == 1) {
					updateMail(position, 0);
				} else if (index == 2) {
					markOrUnMarkMail(position, 0);
				}
				break;

			/**
			 * 删除 标记未读 取消星标
			 */
			case 1:
				if (index == 1) {
					updateMail(position, 1);
				} else if (index == 2) {
					markOrUnMarkMail(position, 0);
				}
				break;

			/**
			 * 删除 标记已读 添加星标
			 */
			case 2:
				if (index == 1) {
					updateMail(position, 0);
				} else if (index == 2) {
					markOrUnMarkMail(position, 1);
				}
				break;

			/**
			 * 删除 标记未读 添加星标
			 */
			case 3:
				if (index == 1) {
					updateMail(position, 1);
				} else if (index == 2) {
					markOrUnMarkMail(position, 1);
				}
				break;

			/**
			 * 删除
			 */
			case 4:
				break;

			default:
				break;
			}
			return false;
		}
	}

	// item点击事件
	private class OnItemClickListView implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (isItemLongClick == 1) {
				adapter.setItemSelected(position);
				if (adapter.checkSelectedCountSameToTotal()) {
					tv_menu.setText(R.string.cancle_select_all);
					ll_menu.setTag("1");
				} else {
					tv_menu.setText(R.string.select_all);
					ll_menu.setTag("0");
				}
				return;
			}

			ArrayList<Email> list = adapter.getAllItems();
			if (list != null && list.size() > position) {
				updateMailStatus(list.get(position).getMessageID(), list.get(position).getMessageNumber(), list.get(position).isNews(), position, list.get(position));
				Intent intent = new Intent(context, EmailDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("list", list);
				bundle.putInt("type", type);
				bundle.putInt("type_in_oa", type_in_oa);
				bundle.putInt("email_type", email_type);
				intent.putExtras(bundle);
				startActivityForResult(intent, Constant.REQUEST_CODE_LIST_TO_DETAIL);
			}
		}
	}

	@Override
	public void loadData(int emailType, int startPosition, boolean isRefreshing) {
		srfh_refresh.setVisibility(View.VISIBLE);
		ll_empty_view.setVisibility(View.GONE);
		isLoading = true;
		this.startPosition = startPosition;
		adapter.setIsRefreshing(isRefreshing);
		presenter.loadData(emailType, startPosition, type, isRefreshing);
	}

	@Override
	public void showData(ArrayList<Email> list) {
		stopLoading();
		srfh_refresh.setRefreshing(false);
		startPosition = (list == null ? 0 : list.size() + startPosition);
		isLoading = false;
		isLoadAll = false;
		isRefreshing = false;
		if (list != null && list.size() > 0) {
			if (startPosition == list.get(0).getTotalEmailCount()) {
				isLoadAll = true;
			}

			if (type == list.get(0).getInbox_type()) {
				adapter.addItems(list);
			}
		}
	}

	@Override
	public void showDataInOA(ArrayList<Email> list) {
		stopLoading();
		srfh_refresh.setRefreshing(false);
		isLoading = false;
		isLoadAll = false;
		isRefreshing = false;
		startPage = startPage + 1;
		if (list != null && list.size() > 0) {
			if (type_in_oa == list.get(0).getInbox_type()) {
				adapter.addItems(list);
			}

			if (adapter.getCount() == list.get(0).getTotalEmailCount()) {
				isLoadAll = true;
			}
		}
	}

	// 删除邮件
	private void deleteMail(int position) {
		List<Email> list = adapter.getAllItems();
		if (list != null && list.size() > position && list.get(position) != null) {
			Email email = list.get(position);
			adapter.removeItem(position);
			if (email_type == Constant.EMAIL_TYPE_OA) {
				String[] array = new String[1];
				array[0] = email.getMessageID();
				String url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
				String token = PreferenceHelper.token_read(context);
				presenter.deleteMailInOA(array, String.valueOf(type_in_oa), url, token, position, email, 0);
			} else {
				String messageId = email.getMessageNumber() + "";
				String id = email.getMessageID();
				presenter.deleteMailByMessageId(id, email_type, messageId, position, type, email);
			}
		}
	}

	// 跟新邮件状态
	private void updateMail(int position, int status) {
		List<Email> list = adapter.getAllItems();
		if (list != null && list.size() > position) {
			Email email = list.get(position);
			if (email_type == Constant.EMAIL_TYPE_OA) {
				if (email != null) {
					updateMailStatus(position, status, type_in_oa, email);
					String[] array = { email.getMessageID() };
					String url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
					presenter.updateMailStatusInOA(array, String.valueOf(type_in_oa), url, app_token, position, email, 0, status);
				}
			} else {
				updateMailStatus(position, status, type, email);
				int messageNumber = email.getMessageNumber();
				String messageId = email.getMessageID();
				presenter.updateMailStatus(messageId, messageNumber, status, type, position, email);
			}
		}
	}

	// 标记或者取消标记
	private void markOrUnMarkMail(int position, int status) {
		List<Email> list = adapter.getAllItems();
		if (list != null && list.size() > position) {
			Email email = list.get(position);
			markSuccess(status, position, email);
			int messageNumber = email.getMessageNumber();
			String messageId = email.getMessageID();
			if (email_type == Constant.EMAIL_TYPE_OA) {
				String[] array = { messageId };
				presenter.markMailInOA(array, status, getUrl(0), getToken(), position, email, 0);
			} else {
				presenter.markOrUnMarkMail(messageId, messageNumber, status, type, position, email);
			}
		}
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void showSuccessMessage(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void updateData(int position) {
		loadData(email_type, 0, true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 刪除 跟新
		if (requestCode == Constant.REQUEST_CODE_LIST_TO_DETAIL && resultCode == Constant.RESULT_CODE_DETAIL_BACK_LIST) {

			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					int position = bundle.getInt("position");
					deleteMail(position);
				}
			} else {
				if (email_type == Constant.EMAIL_TYPE_OA) {
					loadDataInOA(type_in_oa, 1, PAGE_SIZE, url, app_token, true);
				} else {
					loadData(email_type, 0, true);
				}
			}
		} else if (requestCode == REQUEST_CODE_WRITE_MAIL && resultCode == Activity.RESULT_OK) {
			if (email_type == Constant.EMAIL_TYPE_OA) {
				type_in_oa = 1;
				startPage = 1;
				isLoading = false;
				isLoadAll = false;
				loadDataInOA(type_in_oa, startPage, PAGE_SIZE, url, app_token, true);
				setTitle(getResources().getString(R.string.email_get));
			} else {
				setType(0);
				clearData();
				loadData(email_type, 0, true);
				setTitle(getResources().getString(R.string.email_get));
			}
		}
	}

	public void setType(int type) {
		this.type = type;
		adapter.setInboxType(type);
	}

	public void setTitle(String title) {
		tv_title.setText(title);
	}

	@Override
	public void updateMailStatus(String messageId, int messageNumber, boolean isNews, int position, Email email) {
		if (isNews) {
			if (email_type == Constant.EMAIL_TYPE_OA) {
				String[] array = { email.getMessageID() };
				String url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
				presenter.updateMailStatusInOA(array, String.valueOf(type_in_oa), url, app_token, position, email, 0, 0);
			} else {
				presenter.updateMailStatus(messageId, messageNumber, 0, type, position, email);
			}
		}
	}

	public void clearData() {
		isLoading = false;
		isLoadAll = false;
		startPosition = 0;
		adapter.refreshData();
	}

	@Override
	public void updateMailStatus(int position, int status, int type, Email email) {
		ArrayList<Email> list = adapter.getAllItems();

		switch (email_type) {
		// TODO 未读邮件
		case Constant.EMAIL_TYPE_OA:
			if (type == 9) {
				if (list != null) {
					if (status == 0 && list.size() > position) {
						list.get(position).setNews(false);
						list.remove(position);
					} else if (status == 1 && list.size() >= position) {
						list.add(position, email);
						list.get(position).setNews(true);
					}
					adapter.updateItemStatus(list);
				}
			} else {
				if (list != null && list.size() > position) {
					if (status == 0) {
						list.get(position).setNews(false);
					} else if (status == 1) {
						list.get(position).setNews(true);
					}
					adapter.updateItemStatus(list);
				}
			}
			break;

		default:
			if (type == 1) {
				if (list != null) {
					if (status == 0 && list.size() > position) {
						list.get(position).setNews(false);
						list.remove(position);
					} else if (status == 1 && list.size() >= position) {
						list.add(position, email);
						list.get(position).setNews(true);
					}
					adapter.updateItemStatus(list);
				}
			} else {
				if (list != null && list.size() > position) {
					if (status == 0) {
						list.get(position).setNews(false);
					} else if (status == 1) {
						list.get(position).setNews(true);
					}
					adapter.updateItemStatus(list);
				}
			}
			break;
		}
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.email_list_write_mail_linear) {
			if (isItemLongClick == 1) {
				// 标记
				showPopupWindow();
			} else {
				// 写邮件
				Intent intent = new Intent(context, EmailSendOrReplyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("type", 0);
				bundle.putInt("email_type", email_type);
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE_WRITE_MAIL);
			}
		} else if (key == R.id.email_list_search_linear) {
			if (isItemLongClick == 1) {
				// 刪除
				ArrayList<Email> list = adapter.getSelectedItem();
				adapter.deleteItems(list);
				onHomeClickListener.setHomeEnable(true);
				reset();
				if (email_type == Constant.EMAIL_TYPE_OA) {
					if (list != null) {
						int size = list.size();
						String[] deleteItemId = new String[size];
						for (int i = 0; i < size; i++) {
							deleteItemId[i] = list.get(i).getMessageID();
						}
						String url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
						presenter.deleteMailInOA(deleteItemId, String.valueOf(type_in_oa), url, app_token, -1, null, 1);
					}
				} else {
					presenter.deleteMailGroundByMessageId(email_type, list, type);
				}
			} else {
				// 搜索
				Intent intent = new Intent(context, EmailSearchActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("type", type);
				bundle.putInt("email_type", email_type);
				bundle.putInt("type_in_oa", type_in_oa);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		} else if (key == R.id.email_list_menu_linear) {
			if (isItemLongClick == 1) {
				// 全选
				selectAll();
			} else {
				// 菜单 设置
				Intent intent = new Intent(getActivity(), EmailSettingActivity.class);
				String email_account = ((EmailListActivity) context).getCurrentAccount();
				Bundle bundle = new Bundle();
				bundle.putString(Constant.EMAIL_ACCOUNT, email_account);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		} else if (key == R.id.email_list_image_scroll) {
			listView.setSelection(0);
			iv_scroll_to_top.setVisibility(View.GONE);
		}
	}

	// loading
	public void loading() {
		iv_loading.setVisibility(View.VISIBLE);
		if (animationDrawable != null && !animationDrawable.isRunning()) {
			animationDrawable.start();
		}

		if (srfh_refresh != null && srfh_refresh.isRefreshing()) {
			srfh_refresh.setRefreshing(false);
		}
	}

	// not loading
	public void stopLoading() {
		isLoading = false;
		srfh_refresh.setRefreshing(false);
		iv_loading.setVisibility(View.GONE);
		if (animationDrawable != null && animationDrawable.isRunning()) {
			animationDrawable.stop();
		}
	}

	public int isOnItemLongClick() {
		return isItemLongClick;
	}

	public void reset() {
		isItemLongClick = 0;
		updateView(isItemLongClick);
		srfh_refresh.setEnabled(true);
		adapter.setOnItemLongClick(false);
		adapter.clearItemSelected();
	}

	public void updateView(int isItemLongClick) {
		switch (isItemLongClick) {
		/**
		 * 不是长按
		 */
		case 0:
			tv_write_mail.setText(R.string.write_mail);
			iv_write_mail.setImageResource(R.drawable.image_icon_add_email);
			tv_search.setText(R.string.search);
			iv_search.setImageResource(R.drawable.image_icon_search);
			tv_menu.setText(R.string.menu);
			iv_menu.setImageResource(R.drawable.email_setting);
			ll_write_mail.setVisibility(View.VISIBLE);
			break;

		/**
		 * 长按
		 */
		case 1:
			tv_write_mail.setText(R.string.mark);
			iv_write_mail.setImageResource(R.drawable.image_icon_add_email);
			tv_search.setText(R.string.delete);
			iv_search.setImageResource(R.drawable.image_icon_delete);
			tv_menu.setText(R.string.select_all);
			iv_menu.setImageResource(R.drawable.allselect);
			if (type == 2) {
				ll_write_mail.setVisibility(View.GONE);
			} else {
				ll_write_mail.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}

	public void setHomeClickListener(OnHomeClickListener onHomeClickListener) {
		this.onHomeClickListener = onHomeClickListener;
	}

	@Override
	public void refresh() {
		onHomeClickListener.setHomeEnable(true);
		reset();
		switch (email_type) {
		case Constant.EMAIL_TYPE_OA:
			url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
			app_token = PreferenceHelper.token_read(context);
			loadDataInOA(type_in_oa, 1, PAGE_SIZE, url, app_token, true);
			break;

		default:
			loadData(email_type, 0, true);
			break;
		}
	}

	// 全选或者反选
	private void selectAll() {
		int tag = Integer.parseInt(ll_menu.getTag().toString());
		if (tag == 0) {
			tv_menu.setText(R.string.cancle_select_all);
			ll_menu.setTag("1");
			adapter.selectAllItem();
		} else if (tag == 1) {
			tv_menu.setText(R.string.select_all);
			ll_menu.setTag("0");
			adapter.clearItemSelected();
		}
	}

	// 弹出popupwindow
	private void showPopupWindow() {

		initPopupWindow();
		List<Integer> listPosition = adapter.getSelectItemPosition();
		List<Email> list = adapter.getselectedItem();
		boolean hasNoReadMesasge = isHasNoReadMessage(list);
		boolean hasReadMessage = isHasReadMessage(list);
		boolean isAllMarkStar = isAllMarkStar(list);
		boolean isAllNotMarkStar = isAllNotMarkStar(list);
		if ((hasNoReadMesasge && hasReadMessage) || (hasNoReadMesasge && !hasReadMessage)) {
			tv_mark_or_not.setTag("0");
			tv_mark_or_not.setText(R.string.mark_read);
		} else if (!hasNoReadMesasge && hasReadMessage) {
			tv_mark_or_not.setTag("1");
			tv_mark_or_not.setText(R.string.mark_un_read);
		}
		if (isAllMarkStar && !isAllNotMarkStar) {
			viewLine.setVisibility(View.GONE);
			tv_star.setVisibility(View.GONE);
			ll_cancle_mark_star.setVisibility(View.VISIBLE);
			ll_cancle_mark_star.setOnClickListener(new OnClickView(list, listPosition));
		} else if (!isAllMarkStar && isAllNotMarkStar) {
			viewLine.setVisibility(View.VISIBLE);
			ll_cancle_mark_star.setVisibility(View.GONE);
			tv_star.setVisibility(View.VISIBLE);
			tv_star.setOnClickListener(new OnClickView(list, listPosition));
		} else {
			viewLine.setVisibility(View.VISIBLE);
			ll_cancle_mark_star.setVisibility(View.VISIBLE);
			tv_star.setVisibility(View.VISIBLE);
			ll_cancle_mark_star.setOnClickListener(new OnClickView(list, listPosition));
			tv_star.setOnClickListener(new OnClickView(list, listPosition));
		}
		tv_mark_or_not.setOnClickListener(new OnClickView(list, listPosition));
		setBackgroundAlpha(0.5f);
		popupWindow.showAtLocation(((EmailListActivity) context).findViewById(R.id.email_list_relative), Gravity.BOTTOM, 0, 0);
	}

	// 初始化popupwindow
	private void initPopupWindow() {
		if (popupWindow == null) {
			popupWindow = new PopupWindow((EmailListActivity) context);
			popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
			popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
			View view = LayoutInflater.from(context).inflate(R.layout.email_list_popup, null);
			tv_mark_or_not = (TextView) view.findViewById(R.id.popup_mark_or_not);
			tv_star = (TextView) view.findViewById(R.id.popup_star);
			ll_cancle_mark_star = (LinearLayout) view.findViewById(R.id.popup_un_mark_star);
			viewLine = view.findViewById(R.id.popup_view);
			popupWindow.setContentView(view);
			popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
			popupWindow.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPopupWindow弹出窗体的背景
			popupWindow.setBackgroundDrawable(dw);
			popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					setBackgroundAlpha(1f);
				}
			});
		}
	}

	// 设置透明度
	private void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((EmailListActivity) context).getWindow().getAttributes();
		lp.alpha = bgAlpha;
		if (bgAlpha == 1) {
			((EmailListActivity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
		} else {
			((EmailListActivity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 此行代码主要是解决在华为手机上半透明效果无效的bug
		}
		((EmailListActivity) context).getWindow().setAttributes(lp);
	}

	// 判断是否有未读邮件
	private boolean isHasNoReadMessage(List<Email> list) {
		if (list == null) {
			return true;
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			boolean isNew = list.get(i).isNews();
			if (isNew) {
				return true;
			}
		}
		return false;
	}

	// 判断是否有已读邮件
	private boolean isHasReadMessage(List<Email> list) {
		if (list == null) {
			return true;
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			boolean isNew = list.get(i).isNews();
			if (!isNew) {
				return true;
			}
		}
		return false;
	}

	// 判断是否全部为星标邮件
	private boolean isAllMarkStar(List<Email> list) {
		if (list == null) {
			return false;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			boolean flaged = list.get(i).isFlaged();
			if (!flaged) {
				return false;
			}
		}

		return true;
	}

	// 判断是否全部都不是星标邮件
	private boolean isAllNotMarkStar(List<Email> list) {
		if (list == null) {
			return false;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			boolean flaged = list.get(i).isFlaged();
			if (flaged) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void showProgress(String msg) {
		if (dialog == null) {
			dialog = new ProgressDialog(context);
		}

		if (!dialog.isShowing()) {
			dialog.setMessage(msg);
			dialog.show();
		}
	}

	@Override
	public void hideProgress() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	// 内部类---点击事件
	private class OnClickView implements OnClickListener {

		private List<Email> list;
		private List<Integer> listPosition;

		public OnClickView(List<Email> list, List<Integer> listPosition) {
			this.list = list;
			this.listPosition = listPosition;
		}

		@Override
		public void onClick(View view) {
			if (view == null) {
				return;
			}

			int key = view.getId();
			if (key == R.id.popup_mark_or_not) {
				popupWindow.dismiss();
				int tag = Integer.parseInt(tv_mark_or_not.getTag().toString());
				if (tag == 0) {
					updateMailGroupStatus(listPosition, 0, list);
					if (email_type == Constant.EMAIL_TYPE_OA) {
						if (list != null) {
							int size = list.size();
							String[] array = new String[size];
							for (int i = 0; i < size; i++) {
								array[i] = list.get(i).getMessageID();
							}
							String url = FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;
							presenter.updateMailStatusInOA(array, String.valueOf(type_in_oa), url, app_token, -1, null, 1, 0);
						}
					} else {
						presenter.updateMailGroupStatus(list, 0, type);
					}
				} else if (tag == 1) {
					updateMailGroupStatus(listPosition, 1, list);
					presenter.updateMailGroupStatus(list, 1, type);
				}
			} else if (key == R.id.popup_un_mark_star) {
				popupWindow.dismiss();
				markMailGroupStatus(listPosition, 0, list);
				if (email_type == Constant.EMAIL_TYPE_OA) {
					presenter.markMailInOA(listToStringArray(list), 0, getUrl(0), getToken(), 0, null, 1);
				} else {
					presenter.markOrUnmardMailGroup(list, 0, type);
				}
			} else if (key == R.id.popup_star) {
				popupWindow.dismiss();
				markMailGroupStatus(listPosition, 1, list);
				if (email_type == Constant.EMAIL_TYPE_OA) {
					presenter.markMailInOA(listToStringArray(list), 1, getUrl(0), getToken(), 0, null, 1);
				} else {
					presenter.markOrUnmardMailGroup(list, 1, type);
				}
			}
		}
	}

	// list----string数组
	private String[] listToStringArray(List<Email> list) {
		if (list == null) {
			return null;
		}

		int size = list.size();
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = list.get(i).getMessageID();
		}
		return array;
	}

	@Override
	public void showEmptyView() {
		srfh_refresh.setVisibility(View.GONE);
		ll_empty_view.setVisibility(View.VISIBLE);
	}

	@Override
	public void markSuccess(int status, int position, Email email) {

		ArrayList<Email> list = adapter.getAllItems();

		if (email_type == Constant.EMAIL_TYPE_OA) {
			if (type_in_oa == 5) {
				markStarInStarMail(list, status, position, email);
			} else {
				markStar(list, status, position, email);
			}
		} else {
			if (type == 5) {
				markStarInStarMail(list, status, position, email);
			} else {
				markStar(list, status, position, email);
			}
		}
	}

	// 标记星标
	private void markStarInStarMail(ArrayList<Email> list, int status, int position, Email email) {
		if (list != null) {
			if (status == 0 && list.size() > position) {
				list.remove(position);
			} else if (status == 1 && list.size() >= position) {
				list.add(position, email);
				list.get(position).setFlaged(true);
			}
			adapter.updateItemStatus(list);
		}
	}

	// 标记星标
	private void markStar(ArrayList<Email> list, int status, int position, Email email) {
		if (list != null && list.size() > position) {
			if (status == 0) {
				list.get(position).setFlaged(false);
			} else if (status == 1) {
				list.get(position).setFlaged(true);
			}
			adapter.updateItemStatus(list);
		}
	}

	@Override
	public void deleteFailure(int position, Email email) {
		adapter.insertItem(position, email);
	}

	@Override
	public void updateMailGroupStatus(List<Integer> list, int status, List<Email> listEmail) {
		switch (email_type) {
		// TODO
		case Constant.EMAIL_TYPE_OA:
			adapter.updateMailGroupStatus(list, status, 9, listEmail);
			break;

		default:
			adapter.updateMailGroupStatus(list, status, type, listEmail);
			break;
		}
		onHomeClickListener.setHomeEnable(true);
		reset();
	}

	@Override
	public void markMailGroupStatus(List<Integer> list, int status, List<Email> listEmail) {
		adapter.markMailGroupStatus(list, status, type, listEmail, email_type, type_in_oa);
		onHomeClickListener.setHomeEnable(true);
		reset();
	}

	@Override
	public void loadDataInOA(int type, int start, int size, String endpoint, String app_token, boolean isRefreshing) {
		srfh_refresh.setVisibility(View.VISIBLE);
		ll_empty_view.setVisibility(View.GONE);
		isLoading = true;
		this.startPage = start;
		adapter.setIsRefreshing(isRefreshing);
		presenter.loadDataInOA(type, start, size, endpoint, app_token, isRefreshing, "");
	}

	public void setTypeInOA(int type) {
		this.type_in_oa = type;
		switch (type) {
		/**
		 * 收件箱
		 */
		case 1:
			adapter.setInboxType(0);
			break;

		/**
		 * 草稿箱
		 */
		case 2:
			adapter.setInboxType(2);
			break;

		/**
		 * 已发送
		 */
		case 3:
			adapter.setInboxType(3);
			break;

		/**
		 * 未读邮件
		 */
		case 4:
			adapter.setInboxType(0);
			break;

		/**
		 * 星标邮件
		 */
		case 5:
			adapter.setInboxType(5);
			break;

		/**
		 * 已刪除
		 */
		case 6:
			adapter.setInboxType(4);
			break;

		default:
			break;
		}
	}

	public void loadDataInOA() {
		loadDataInOA(type_in_oa, 1, PAGE_SIZE, url, app_token, true);
	}

	public void saveAccount(String account, String password, int emailType) {
		ArrayList<AccountInfo> list = new ArrayList<>();
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setAccount(account);
		accountInfo.setPassword(password);
		accountInfo.setEmailType(emailType);
		list.add(accountInfo);
		ArrayList<AccountInfo> listSharedPreference = getAccount();
		list = presenter.getNoSmaeAccountList(list, listSharedPreference, account);
		SharedPreferencedUtis.saveValue(context, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, list);
	}

	@Override
	public ArrayList<AccountInfo> getAccount() {
		Type type = new TypeToken<ArrayList<AccountInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(context, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, type);
	}

	@Override
	public String getUrl(int type) {
		switch (type) {
		/**
		 * 标记星标
		 */
		case 0:
			return FileManager.getIp(context) + WebserviceConf.ENDPOINT_OA_EMAIL;

		/**
		 * 未读和已读
		 */
		case 1:
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	public String getToken() {
		return PreferenceHelper.token_read(context);
	}
}
