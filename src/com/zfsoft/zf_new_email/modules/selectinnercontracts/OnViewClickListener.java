package com.zfsoft.zf_new_email.modules.selectinnercontracts;

/**
 * @author wesley
 * @date 2017-2-22
 * @Description:回调接口
 */
public interface OnViewClickListener {

	/**
	 * imageview是否选中的点击事件
	 * 
	 * @param position
	 *            当前的位置
	 */
	void onViewClick(int position);

	/**
	 * listview的item点击事件
	 * 
	 * @param position
	 *            当前的位置
	 */
	void onItemClick(int position);
}
