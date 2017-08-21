/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.modules.checkemail;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zfsoft.zf_new_email.R;
import com.zfsoft.zf_new_email.base.BaseFragment;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.modules.authorizationcode.AuthorizationCodeActivity;
import com.zfsoft.zf_new_email.modules.emaillist.EmailListActivity;
import com.zfsoft.zf_new_email.util.SharedPreferencedUtis;
import com.zfsoft.zf_new_email.util.SoftPanUtis;

/**
 * @author wesley
 * @date 2016-10-17下午7:32:30
 * @Description: ui展示层
 */
public class CheckEmailFragment extends BaseFragment<CheckEmailPresenter> implements CheckEmailContract.View, OnClickListener {

	private int email_type;
	private EditText et_user; // 邮箱账户
	private EditText et_pass; // 邮箱密码---授权码
	private Button btn_done; // 完成
	private ProgressDialog dialog;
	private Dialog authCodeDialog;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_email_check;
	}

	@Override
	public void initVariables() {
		handleBundle();
	}

	@Override
	public void initViews(View view) {
		et_user = (EditText) view.findViewById(R.id.fragment_check_email_user);
		et_pass = (EditText) view.findViewById(R.id.fragment_check_emails_pass);
		btn_done = (Button) view.findViewById(R.id.fragment_check_email_done);
		btn_done.setOnClickListener(this);
	}

	public static CheckEmailFragment newInstance(int email_type) {
		CheckEmailFragment fragment = new CheckEmailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("email_type", email_type);
		fragment.setArguments(bundle);
		return fragment;
	}

	// 处理bundle
	private void handleBundle() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			email_type = bundle.getInt("email_type");
		}
	}

	@Override
	public String getEmailAccount() {
		return et_user.getText().toString();
	}

	@Override
	public String getEmailPassword() {
		return et_pass.getText().toString();
	}

	@Override
	public void login(String emailAccount, String emailPassword) {
		presenter.login(emailAccount, emailPassword, email_type);
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showProgressDialog() {
		dialog = new ProgressDialog(context);
		dialog.setMessage(getResources().getString(R.string.loging));
		dialog.show();
	}

	@Override
	public void hideProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int key = view.getId();
		if (key == R.id.fragment_check_email_done) {
			SoftPanUtis.hidePan(context);
			login(getEmailAccount(), getEmailPassword());
		} else if (key == R.id.dialog_save_draft) {
			hideDialog();
		}
	}

	@Override
	public void loginError(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void loginSuccess() {
		Intent intent = new Intent(context, EmailListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.EMAIL_TYPE, email_type);
		bundle.putString(Constant.EMAIL_ACCOUNT, getEmailAccount());
		intent.putExtras(bundle);
		startActivity(intent);
		((CheckEmaillActivity) context).finish();
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
	}

	@Override
	public ArrayList<AccountInfo> getAccount() {
		Type type = new TypeToken<ArrayList<AccountInfo>>() {
		}.getType();
		return SharedPreferencedUtis.getValue(context, Constant.ACCOUNT_NAME, Constant.ACCOUNT_KEY, type);
	}

	@Override
	public void showDialog(String url) {

		if (authCodeDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_draft, null);
			builder.setView(view);
			TextView tv_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
			TextView tv_save_draft = (TextView) view.findViewById(R.id.dialog_save_draft);
			TextView tv_leave = (TextView) view.findViewById(R.id.dialog_leave);
			TextView tv_login_error = (TextView) view.findViewById(R.id.leave_write_mail);
			TextView tv_hint = (TextView) view.findViewById(R.id.sure_leave);

			tv_login_error.setText(R.string.login_error);
			tv_hint.setText(R.string.auth_code_error_hint);
			tv_save_draft.setText(R.string.cancle);
			tv_leave.setText(R.string.open_imap);
			tv_leave.setTextColor(getResources().getColor(R.color.color_bule));
			tv_cancle.setVisibility(View.GONE);
			authCodeDialog = builder.create();

			tv_save_draft.setOnClickListener(this);
			tv_leave.setOnClickListener(new OnTextClick(url));
		}
		authCodeDialog.show();
	}

	// 内部类---点击事件
	private class OnTextClick implements OnClickListener {

		private String url;

		private OnTextClick(String url) {
			this.url = url;
		}

		@Override
		public void onClick(View view) {
			if (view == null) {
				return;
			}

			int key = view.getId();
			if (key == R.id.dialog_leave) {
				hideDialog();
				Intent intent = new Intent(context, AuthorizationCodeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", url);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}

	@Override
	public void hideDialog() {
		if (authCodeDialog != null && authCodeDialog.isShowing()) {
			authCodeDialog.dismiss();
		}
	}
}
