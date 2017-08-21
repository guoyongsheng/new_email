/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.emailtype;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.zfsoft.core.data.UserInfoData;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseApplication;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.modules.checkemail.CheckEmaillActivity;
import com.zfsoft.zf_new_email.modules.emaillist.EmailListActivity;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;

/**
 * @author wesley
 * @date 2016-10-17下午1:56:42
 * @Description: fragment 展示view
 */
public class EmailTypeFragment extends BaseFragment<EmailTypePresenter> implements EmailTypeContract.View, OnClickListener {

	private LinearLayout ll_head;
	private int type;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_email_type;
	}

	@Override
	public void initVariables() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			type = bundle.getInt("type");
		}
	}

	@Override
	public void initViews(View view) {
		LinearLayout ll_email_oa = (LinearLayout) view.findViewById(R.id.fragment_email_type_oa);
		LinearLayout ll_email_wangyi_163 = (LinearLayout) view.findViewById(R.id.fragment_email_type_wangyi_163);
		LinearLayout ll_email_wangyi_126 = (LinearLayout) view.findViewById(R.id.fragment_email_type_wangyi_126);
		LinearLayout ll_email_sina = (LinearLayout) view.findViewById(R.id.fragment_email_type_sina);
		LinearLayout ll_email_qq = (LinearLayout) view.findViewById(R.id.fragment_email_type_qq);
		LinearLayout ll_email_sohu = (LinearLayout) view.findViewById(R.id.fragment_email_type_sohu);
		ll_head = (LinearLayout) ((EmailTypeActivity) context).findViewById(R.id.include_linear);

		ll_email_oa.setOnClickListener(this);
		ll_email_wangyi_163.setOnClickListener(this);
		ll_email_wangyi_126.setOnClickListener(this);
		ll_email_sina.setOnClickListener(this);
		ll_email_qq.setOnClickListener(this);
		ll_email_sohu.setOnClickListener(this);

		if (type == 0) {
			login();
		}
	}

	public static EmailTypeFragment newInstance(int type) {
		EmailTypeFragment fragment = new EmailTypeFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.fragment_email_type_oa) {
			loginInOA();
		} else if (key == R.id.fragment_email_type_wangyi_163) {
			Intent intent_wangyi_163 = new Intent(context, CheckEmaillActivity.class);
			Bundle bundle_wangyi_163 = new Bundle();
			bundle_wangyi_163.putInt(Constant.EMAIL_TYPE, Constant.EMAIL_TYPE_WANGYI_163);
			intent_wangyi_163.putExtras(bundle_wangyi_163);
			startActivity(intent_wangyi_163);
		} else if (key == R.id.fragment_email_type_wangyi_126) {
			Intent intent_wangyi_126 = new Intent(context, CheckEmaillActivity.class);
			Bundle bundle_wangyi_126 = new Bundle();
			bundle_wangyi_126.putInt(Constant.EMAIL_TYPE, Constant.EMAIL_TYPE_WANGYI_126);
			intent_wangyi_126.putExtras(bundle_wangyi_126);
			startActivity(intent_wangyi_126);
		} else if (key == R.id.fragment_email_type_sina) {
			Intent intent_sina = new Intent(context, CheckEmaillActivity.class);
			Bundle bundle_sina = new Bundle();
			bundle_sina.putInt(Constant.EMAIL_TYPE, Constant.EMAIL_TYPE_SINA);
			intent_sina.putExtras(bundle_sina);
			startActivity(intent_sina);
		} else if (key == R.id.fragment_email_type_qq) {
			Intent intent_wangyi_qq = new Intent(context, CheckEmaillActivity.class);
			Bundle bundle_wangyi_qq = new Bundle();
			bundle_wangyi_qq.putInt(Constant.EMAIL_TYPE, Constant.EMAIL_TYPE_WANGYI_qq);
			intent_wangyi_qq.putExtras(bundle_wangyi_qq);
			startActivity(intent_wangyi_qq);
		} else if (key == R.id.fragment_email_type_sohu) {
			Intent intent_sohu = new Intent(context, CheckEmaillActivity.class);
			Bundle bundle_sohu = new Bundle();
			bundle_sohu.putInt(Constant.EMAIL_TYPE, Constant.EMAIL_TYPE_SOHU);
			intent_sohu.putExtras(bundle_sohu);
			startActivity(intent_sohu);
		}
	}

	@Override
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

		saveMailInfo(account, emailType, password);
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

	@Override
	public ArrayList<AccountInfo> getAccount() {
		Type type = new TypeToken<ArrayList<AccountInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(context, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, type);
	}

	@Override
	public void login() {
		ArrayList<AccountInfo> list = getAccount();
		if (list != null && list.size() > 0) {
			ll_head.setAlpha(0);

			AccountInfo accountInfo = list.get(0);
			if (accountInfo != null) {
				int emailType = accountInfo.getEmailType();
				if (emailType == Constant.EMAIL_TYPE_OA) {
					loginInOA();
				} else {
					String emailAccount = accountInfo.getAccount();
					String emailPassword = accountInfo.getPassword();
					loginSuccess(emailAccount, emailPassword, emailType);
				}
			}
		}
	}

	// oa登录
	private void loginInOA() {
		String account = UserInfoData.getInstance().getAccount();
		String password = UserInfoData.getInstance().getPassword();
		saveAccount(account, password, Constant.EMAIL_TYPE_OA);
		Intent intent = new Intent(context, EmailListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.EMAIL_TYPE, Constant.EMAIL_TYPE_OA);
		bundle.putString(Constant.EMAIL_ACCOUNT, account);
		intent.putExtras(bundle);
		startActivity(intent);
		ll_head.setAlpha(1);
	}

	@Override
	public void loginError(String errorMessage) {
		ll_head.setAlpha(1);
	}

	@Override
	public void loginSuccess(String account, String password, int emailType) {
		saveAccount(account, password, emailType);
		Intent intent = new Intent(context, EmailListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.EMAIL_TYPE, emailType);
		bundle.putString(Constant.EMAIL_ACCOUNT, account);
		intent.putExtras(bundle);
		startActivity(intent);
		ll_head.setAlpha(1);
	}
}
