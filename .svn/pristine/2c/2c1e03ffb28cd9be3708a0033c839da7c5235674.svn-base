/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.util;

import com.zfsoft.zf_new_email.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author wesley
 * @date 2016-10-17下午2:01:53
 * @Description: activity的工具类
 */
public class ActivityUtils {

	private ActivityUtils() {

	}

	/**
	 * 添加fragment到activity中
	 * 
	 * @param manager
	 *            管理器
	 * @param fragment
	 *            具体的fragment
	 */
	public static void addFragmentToActivity(FragmentManager manager, Fragment fragment) {
		if (manager == null || fragment == null) {
			return;
		}

		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.include_content, fragment);
		transaction.commit();
	}
}
