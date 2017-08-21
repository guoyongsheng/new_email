package com.zfsoft.zf_new_email.modules.emailsettings;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zfsoft.core.view.AlertDialog;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseActivity;
import com.zfsoft.zf_new_email.base.BaseApplication;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.modules.emaillist.EmailListActivity;
import com.zfsoft.zf_new_email.modules.emailtype.EmailTypeActivity;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;

// 删除邮箱账户
public class DeleteAccountActivity extends BaseActivity implements OnClickListener {
	private String email_account; // 邮箱账户
	private String currentAccount; // 当前账户
	private TextView include_head_title; // 标题
	private ImageView incluce_head_back; // 返回按钮
	private TextView delete_account_tv; // 删除账户

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_delete_account;
	}

	@Override
	public void initVariables() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				email_account = bundle.getString(Constant.EMAIL_ACCOUNT);
				currentAccount = bundle.getString("currentAccount");
			}
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		include_head_title = (TextView) findViewById(R.id.include_head_title);
		if (email_account != null) {
			include_head_title.setText(email_account);
		} else {
			include_head_title.setText(R.string.delete_account);
		}
		incluce_head_back = (ImageView) findViewById(R.id.incluce_head_back);
		incluce_head_back.setOnClickListener(this);
		delete_account_tv = (TextView) findViewById(R.id.delete_account_tv);
		delete_account_tv.setOnClickListener(this);
	}

	@Override
	public void initPresenter() {
		// TODO Auto-generated method stub

	}

	// 获取所有的账号
	public ArrayList<AccountInfo> getAccount() {
		Type type = new TypeToken<ArrayList<AccountInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(this, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, type);
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
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.incluce_head_back) {
			finish();
		} else if (id == R.id.delete_account_tv) {
			AlertDialog dialog = new AlertDialog(this).builder().setTitle("您确定删除该账户吗?").setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<AccountInfo> list = getAccount();
					for (AccountInfo info : list) {
						if (email_account != null) {
							if (info.getAccount().equals(email_account)) {
								list.remove(info);
								break;
							}
						}
					}
					SharedPreferencedUtis.saveValue(DeleteAccountActivity.this, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, list);
					if (email_account.equals(currentAccount)) {
						if (list.size() > 0) {
							if (saveMailInfo(list.get(0).getAccount(), list.get(0).getEmailType(), list.get(0).getPassword())) {
								saveAccount(list.get(0).getAccount(), list.get(0).getPassword(), list.get(0).getEmailType());
								Intent intent = new Intent(DeleteAccountActivity.this, EmailListActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt(Constant.EMAIL_TYPE, list.get(0).getEmailType());
								bundle.putString(Constant.EMAIL_ACCOUNT, list.get(0).getAccount());
								intent.putExtras(bundle);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(DeleteAccountActivity.this, R.string.account_switch_failure, Toast.LENGTH_SHORT).show();
							}

						} else {
							Intent intent = new Intent(DeleteAccountActivity.this, EmailTypeActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle bundle = new Bundle();
							bundle.putInt("type", 1);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}

					} else {
						finish();
					}

				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});
			dialog.show();
		}
	}

	public void saveAccount(String account, String password, int emailType) {
		ArrayList<AccountInfo> list = new ArrayList<>();
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setAccount(account);
		accountInfo.setPassword(password);
		accountInfo.setEmailType(emailType);
		list.add(accountInfo);
		ArrayList<AccountInfo> listSharedPreference = getAccount();
		list = getNoSmaeAccountList(list, listSharedPreference, account);
		SharedPreferencedUtis.saveValue(this, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, list);
	}

	public ArrayList<AccountInfo> getNoSmaeAccountList(ArrayList<AccountInfo> list, ArrayList<AccountInfo> listSharedPreference, String account) {
		if (listSharedPreference != null && list != null) {
			int size = listSharedPreference.size();
			for (int i = 0; i < size; i++) {
				String value = listSharedPreference.get(i).getAccount();
				if (value != null && value.equals(account)) {
					listSharedPreference.remove(i);
					break;
				}
			}
			list.addAll(listSharedPreference);
		}
		return list;
	}
}
