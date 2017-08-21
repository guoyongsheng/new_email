/**
 * @date2016-10-19
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

/**
 * @author wesley
 * @date 2016-10-19下午4:11:21
 * @Description:
 */
public class BaseApplication extends Application {

	private static BaseApplication instance;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static BaseApplication getInstance() {
		return instance;
	}
}
