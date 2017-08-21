/**
 * 
 */
package com.zfsoft.zf_new_email.modules.checkemail;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.zfsoft.zf_new_email.entity.ResponseInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;
import com.zfsoft.zf_new_email.util.MailHelper;

/**
 * @author wesley
 * @date: 2016-12-14
 * @Description: 接口实现
 */
public class CheckEmailServiceImpl extends ICheckEmailService {

	@Override
	public void login(String emailAddress, String password, String login_from, CallBackListener<Boolean> listener) {

		AsyncLogin login = new AsyncLogin(listener);
		add(login);
		login.execute(emailAddress, password, login_from);
	}

	// 静态内部类---实现登录
	private class AsyncLogin extends AsyncTask<String, Void, ResponseInfo> {

		private CallBackListener<Boolean> listener;

		public AsyncLogin(CallBackListener<Boolean> listener) {
			this.listener = listener;
		}

		@Override
		protected ResponseInfo doInBackground(String... params) {

			if (isCancelled()) {
				return null;
			}
			return MailHelper.getInstance().login(params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(ResponseInfo result) {
			super.onPostExecute(result);

			if (result != null && result.getCode() == 0) {
				listener.onSuccess(true);
			} else if (result != null && result.getCode() == 1) {
				Gson gson = new Gson();
				String value = gson.toJson(result);
				listener.onFailure(value);
			}
		}

		@Override
		protected void onCancelled(ResponseInfo result) {
			super.onCancelled(result);
		}
	}
}
