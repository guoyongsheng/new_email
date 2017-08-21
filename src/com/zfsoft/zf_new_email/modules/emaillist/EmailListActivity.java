/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.base.BaseApplication;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.modules.emailtype.EmailTypeActivity;
import com.zfsoft.zf_new_email.util.ActivityUtils;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;

/**
 * @author wesley
 * @date 2016-10-17下午3:12:15
 * @Description: 邮件列表界面
 */
public class EmailListActivity extends BaseActivity implements OnClickListener, OnHomeClickListener {

	private int email_type; // 邮件类型 0:oa
	private String email_account; // 邮箱账户
	private TextView tv_account; // 账号
	private ImageView iv_home; // 主页
	private LinearLayout ll_back; // 返回
	private LinearLayout ll_home; // 主頁
	private EmailListFragment fragment;
	private SlidingMenu menu; // 侧滑菜单
	private EmailListAccountAdapter adapter; // 账号适配器
	private int closedType = 0;
	private AccountInfo accountInfo;
	private ProgressDialog dialog; // 对话框

	@Override
	public int getLayoutId() {
		return R.layout.activity_email_list;
	}

	@Override
	public void initVariables() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				email_type = bundle.getInt(Constant.EMAIL_TYPE);
				email_account = bundle.getString(Constant.EMAIL_ACCOUNT);
			}
		}
	}

	public String getCurrentAccount() {
		return email_account;
	}

	@Override
	public void initViews() {
		tv_account = (TextView) findViewById(R.id.include_head_account);
		ll_back = (LinearLayout) findViewById(R.id.inclue_head_back_linear);
		iv_home = (ImageView) findViewById(R.id.incluce_head_home);
		ll_home = (LinearLayout) findViewById(R.id.include_head_home_linear);

		ll_home.setVisibility(View.VISIBLE);
		iv_home.setImageResource(R.drawable.image_icon_home);
		tv_account.setVisibility(View.VISIBLE);
		tv_account.setText(email_account);
		ll_back.setOnClickListener(this);
		ll_home.setOnClickListener(this);

		FragmentManager manager = getSupportFragmentManager();
		fragment = (EmailListFragment) manager.findFragmentById(R.id.include_content);
		if (fragment == null) {
			fragment = EmailListFragment.newInstance(email_type);
			fragment.setHomeClickListener(this);
			ActivityUtils.addFragmentToActivity(manager, fragment);
		}

		initSlideMenu();
	}

	@Override
	public void initPresenter() {
		new EmailListPresenter(fragment);
	}

	// 初始化slidemenu
	private void initSlideMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);

		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 为侧滑菜单设置布局
		menu.setMenu(R.layout.activity_email_list_slide_menu);

		View view = menu.getMenu();
		RelativeLayout rl_email_get = (RelativeLayout) view.findViewById(R.id.slide_menu_email_get);
		RelativeLayout rl_email_not_read = (RelativeLayout) view.findViewById(R.id.slide_menu_email_not_read);
		RelativeLayout rl_email_star = (RelativeLayout) view.findViewById(R.id.slide_menu_email_star);
		RelativeLayout rl_email_cao = (RelativeLayout) view.findViewById(R.id.slide_menu_email_cao);
		RelativeLayout rl_email_send = (RelativeLayout) view.findViewById(R.id.slide_menu_email_send);
		RelativeLayout rl_email_delete = (RelativeLayout) view.findViewById(R.id.slide_menu_email_delete);
		RelativeLayout rl_add_new_account = (RelativeLayout) view.findViewById(R.id.slide_menu_add_account);
		ListView ll_account = (ListView) view.findViewById(R.id.slide_menu_list_account);
		adapter = new EmailListAccountAdapter(this, email_account, getAccount());
		ll_account.setAdapter(adapter);
		setListViewHeightBasedOnChildren(ll_account);

		// 点击事件
		rl_email_get.setOnClickListener(this);
		rl_email_not_read.setOnClickListener(this);
		rl_email_star.setOnClickListener(this);
		rl_email_cao.setOnClickListener(this);
		rl_email_send.setOnClickListener(this);
		rl_email_delete.setOnClickListener(this);
		rl_add_new_account.setOnClickListener(this);
		ll_account.setOnItemClickListener(new OnItemClickListView());
		menu.setOnClosedListener(new OnClosedSlideMenu());
	}

	@Override
	public void onClick(View view) {

		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.inclue_head_back_linear) {
			onBackPress();
		} else if (key == R.id.include_head_home_linear) {
			closedType = 0;
			menu.toggle();
		} else if (key == R.id.slide_menu_email_get) {
			closedType = 0;
			menu.toggle();
			fragment.clearData();
			fragment.loading();
			fragment.setTitle(getResources().getString(R.string.email_get));
			if (email_type == Constant.EMAIL_TYPE_OA) {
				fragment.setTypeInOA(1);
				fragment.loadDataInOA();
			} else {
				fragment.setType(0);
				fragment.loadData(email_type, 0, true);
			}
		} else if (key == R.id.slide_menu_email_not_read) {
			closedType = 0;
			menu.toggle();
			fragment.clearData();
			fragment.loading();
			fragment.setTitle(getResources().getString(R.string.email_not_read));
			if (email_type == Constant.EMAIL_TYPE_OA) {
				fragment.setTypeInOA(4);
				fragment.loadDataInOA();
			} else {
				fragment.setType(1);
				fragment.loadData(email_type, 0, true);
			}
		} else if (key == R.id.slide_menu_email_star) {
			closedType = 0;
			menu.toggle();
			fragment.clearData();
			fragment.loading();
			fragment.setTitle(getResources().getString(R.string.email_star));
			if(email_type == Constant.EMAIL_TYPE_OA){
				fragment.setTypeInOA(5);
				fragment.loadDataInOA();
			}else{
				fragment.setType(5);
				fragment.loadData(email_type, 0, true);
			}
		} else if (key == R.id.slide_menu_email_cao) {
			closedType = 0;
			menu.toggle();
			fragment.clearData();
			fragment.loading();
			fragment.setTitle(getResources().getString(R.string.email_cao));
			if (email_type == Constant.EMAIL_TYPE_OA) {
				fragment.setTypeInOA(2);
				fragment.loadDataInOA();
			} else {
				fragment.setType(2);
				fragment.loadData(email_type, 0, true);
			}
		} else if (key == R.id.slide_menu_email_send) {
			closedType = 0;
			menu.toggle();
			fragment.clearData();
			fragment.loading();
			fragment.setTitle(getResources().getString(R.string.email_send));
			if (email_type == Constant.EMAIL_TYPE_OA) {
				fragment.setTypeInOA(3);
				fragment.loadDataInOA();
			} else {
				fragment.setType(3);
				fragment.loadData(email_type, 0, true);
			}
		} else if (key == R.id.slide_menu_email_delete) {
			closedType = 0;
			menu.toggle();
			fragment.clearData();
			fragment.loading();
			fragment.setTitle(getResources().getString(R.string.email_delete));
			if (email_type == Constant.EMAIL_TYPE_OA) {
				fragment.setTypeInOA(6);
				fragment.loadDataInOA();
			} else {
				fragment.setType(4);
				fragment.loadData(email_type, 0, true);
			}
		} else if (key == R.id.slide_menu_add_account) {
			closedType = 2;
			menu.toggle();
		}
	}

	@Override
	public void onBackPressed() {
		onBackPress();
	}

	private void onBackPress() {
		int isOnItemLongClick = fragment.isOnItemLongClick();
		if (isOnItemLongClick == 1) {
			ll_home.setVisibility(View.VISIBLE);
			fragment.reset();
		} else {
			finish();
		}
	}

	@Override
	public void setHomeEnable(boolean isVisible) {
		ll_home.setVisibility(isVisible ? View.VISIBLE : View.GONE);
	}

	// 内部类
	private class OnItemClickListView implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			List<AccountInfo> list = adapter.getAllItems();
			if (list != null && list.size() > position && list.get(position) != null) {
				closedType = 1;
				accountInfo = list.get(position);
				menu.toggle();
			}
		}
	}

	// 内部类---slidemenu完全关闭监听
	private class OnClosedSlideMenu implements OnClosedListener {

		@Override
		public void onClosed() {
			if (closedType == 0) {
				return;
			}

			switch (closedType) {
			/**
			 * 切換账号
			 */
			case 1:
				if (accountInfo == null) {
					return;
				}
				String account = accountInfo.getAccount();
				if (account != null && account.equals(email_account)) {
					return;
				}
				showProgressDialog();
				int emailType = accountInfo.getEmailType();
				if (saveMailInfo(account, emailType, accountInfo.getPassword())) {
					fragment.saveAccount(account, accountInfo.getPassword(), emailType);
					hideProgrressDialog();
					Intent intent = new Intent(EmailListActivity.this, EmailListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt(Constant.EMAIL_TYPE, emailType);
					bundle.putString(Constant.EMAIL_ACCOUNT, account);
					intent.putExtras(bundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else {
					hideProgrressDialog();
					Toast.makeText(EmailListActivity.this, R.string.account_switch_failure, Toast.LENGTH_SHORT).show();
				}
				break;

			/**
			 * 添加账号
			 */
			case 2:
				Intent intent = new Intent(EmailListActivity.this, EmailTypeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle bundle = new Bundle();
				bundle.putInt("type", 1);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		}
	}

	// 获取所有的账号
	public ArrayList<AccountInfo> getAccount() {
		Type type = new TypeToken<ArrayList<AccountInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(EmailListActivity.this, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, type);
	}

	// 保存账号
	private boolean saveMailInfo(String mail, int login_from, String password) {

		MailInfo mailInfo = new MailInfo();
		if (login_from != Constant.EMAIL_TYPE_OA) {
			if (mail == null || password == null || !mail.contains("@")) {
				return false;
			}
			String host = "smtp." + mail.substring(mail.lastIndexOf("@") + 1);
			mailInfo.setMailServerHost(host);
			if (login_from == Constant.EMAIL_TYPE_WANGYI_qq) {
				mailInfo.setMailServerPort("587"); // qq邮箱的端口好是587
			} else {
				mailInfo.setMailServerPort("25"); // qq邮箱的端口好是587
			}
		}
		mailInfo.setLoginFrom(login_from);
		mailInfo.setUserName(mail);
		mailInfo.setPassword(password);
		mailInfo.setValidate(true);
		SharedPreferencedUtis.saveMailInfo(mailInfo, BaseApplication.getInstance());
		return true;
	}

	// 显示对话框
	private void showProgressDialog() {
		dialog = new ProgressDialog(this);
		dialog.setMessage(getResources().getString(R.string.acccount_swiching));
		dialog.show();
	}

	private void hideProgrressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	// 计算listview的高度
	private void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int count = listAdapter.getCount();
		int totalHeight = 0;
		for (int i = 0, len = count; i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (count));
		listView.setLayoutParams(params);
	}
}
