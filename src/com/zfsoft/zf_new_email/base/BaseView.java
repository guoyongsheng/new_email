/**
 * @date2016-10-17
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.base;

/**
 * @author wesley
 * @date 2016-10-17上午11:56:01
 * @Description: 所有的Fragment都要实现这个接口
 */
public interface BaseView<T> {

	// 让view层持有presenter的引用
	void setPresenter(T presenter);

	// fragment是否在activity上
	boolean isActive();
}
