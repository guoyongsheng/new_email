/**
 * @date2016-10-19
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.util;

import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfsoft.zf_new_email.entity.HistoryInfo;
import com.zfsoft.zf_new_email.entity.MailInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author wesley
 * @date 2016-10-19下午3:54:10
 * @Description: 工具类---保存邮箱账号和密码
 */
public class SharedPreferencedUtis {

	private static final String MAIL_ACCOUNT_PASSWORD = "MAIL_ACCOUNT_PASSWORD";
	private static final String SEARCH_HISTORY = "SEARCH_HISTORY";

	private SharedPreferencedUtis() {

	}

	/**
	 * 保存mailinfo的信息
	 * 
	 * @param mailInfo
	 *            对象
	 * @param context
	 *            上下文对象
	 */
	public static void saveMailInfo(MailInfo mailInfo, Context context) {

		if (context == null) {
			return;
		}
		Gson gson = new Gson();
		String value = gson.toJson(mailInfo);
		SharedPreferences sharedPreferences = context.getSharedPreferences(MAIL_ACCOUNT_PASSWORD, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("mail_account", value);
		editor.commit();
	}

	/**
	 * 获取mailinfo
	 * 
	 * @param context
	 *            上下文对象
	 * @return mailinfo
	 */
	public static MailInfo getMailInfo(Context context) {

		if (context == null) {
			return null;
		}
		Gson gson = new Gson();
		MailInfo mailInfo = gson.fromJson(getMailValue(context), MailInfo.class);
		return mailInfo;
	}

	// 获取mailinfo
	private static String getMailValue(Context context) {
		if (context == null) {
			return "";
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(MAIL_ACCOUNT_PASSWORD, Context.MODE_PRIVATE);
		return sharedPreferences.getString("mail_account", "");
	}

	/**
	 * 保存搜索历史
	 * 
	 * @param list
	 *            搜索历史集合
	 * @param context
	 *            上下文对象
	 */
	public static void saveHistory(List<HistoryInfo> list, Context context) {
		if (context == null) {
			return;
		}

		Gson gson = new Gson();
		String value = gson.toJson(list);
		SharedPreferences sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("SEARCH_HISTORY", value);
		editor.commit();
	}

	/**
	 * 获取搜索历史
	 * 
	 * @param context
	 *            上下文对象
	 * @return 搜索历史
	 */
	public static List<HistoryInfo> getHistory(Context context) {
		if (context == null) {
			return null;
		}

		SharedPreferences sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
		String value = sharedPreferences.getString("SEARCH_HISTORY", "");
		Gson gson = new Gson();
		Type type = new TypeToken<List<HistoryInfo>>() {
		}.getType();
		return gson.fromJson(value, type);
	}

	/**
	 * 获取保存在sharedpreferences中的对象
	 * 
	 * @param <T>
	 * @param <T>
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            name
	 * @param key
	 *            key
	 * @param type
	 *            type
	 * @return
	 */
	public static <T> T getValue(Context context, String name, String key, Type type) {
		if (context == null) {
			return null;
		}

		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		Gson gson = new Gson();
		return gson.fromJson(value, type);
	}

	/**
	 * 保存對象到sharedPreferences中
	 * 
	 * @param context
	 *            上下文對象
	 * @param name
	 *            名字
	 * @param key
	 *            key
	 * @param t
	 *            对象
	 */
	public static <T> void saveValue(Context context, String name, String key, T t) {
		if (context == null) {
			return;
		}

		Gson gson = new Gson();
		String value = gson.toJson(t);
		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
