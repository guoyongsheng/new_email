/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wesley
 * @date 2016-10-18上午9:18:50
 * @Description: 邮箱格式校验工具类
 */
public class EmailFormatUtils {

	private EmailFormatUtils() {

	}

	/**
	 * 校验邮箱格式是否正确
	 * 
	 * @param email
	 *            邮箱地址
	 * @return true:正确 false:错误
	 */
	public static boolean checkEmailFormatIsOk(String email) {
		boolean tag = true;
		String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 校验特定的邮箱格式是否正确
	 * 
	 * @param email
	 *            邮箱
	 * @param format
	 *            特定的格式 如 163 126 qq
	 * @return true:正确 false:错误
	 */
	public static boolean checkEmailIsOk(String email, String format) {
		boolean tag = true;
		// String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@(" + format +
		// ".)+[a-zA-Z]{2,}$";
		String pattern1 = "[a-z0-9A-Z]@(" + format + ".)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}
}
