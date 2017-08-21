/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * @author wesley
 * @date 2016-10-17上午11:18:18
 * @Description: 基类的Activity
 */
public abstract class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(getLayoutId());
		initVariables();
		initViews();
		initPresenter();
	}

	/**
	 * 获取资源文件布局
	 * 
	 * @return 布局id
	 */
	public abstract int getLayoutId();

	/**
	 * 初始化变量---主要是从上个界面传过来的变量
	 */
	public abstract void initVariables();

	/**
	 * 初始化控件
	 */
	public abstract void initViews();

	/**
	 * 初始化presenter
	 */
	public abstract void initPresenter();
}
