/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author wesley
 * @date 2016-10-18上午9:42:41
 * @Description: 软键盘
 */
public class SoftPanUtis {

	private SoftPanUtis() {

	}

	// 弹出软键盘
	public static void showSoftPan(final EditText et_soft) {

		if (et_soft == null) {
			return;
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) et_soft.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_soft, 0);
			}
		}, 500);
	}

	// 隐藏软键盘
	public static void hidePan(Activity activity) {
		if (activity == null) {
			return;
		}
		View view = activity.getCurrentFocus();
		if (view == null) {
			view = new View(activity);
		}
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
