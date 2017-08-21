/**
 * 
 */
package com.zfsoft.zf_new_email.util;

import android.os.Environment;

/**
 * @author wesley
 * @date: 2017-1-13
 * @Description:sd卡工具类
 */
public class SDCardUtils {

	private SDCardUtils() {

	}

	/**
	 * 判断SD卡是否可用
	 * 
	 * @return true : 可用 false : 不可用
	 */
	public static boolean isSDCardEnable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
}
