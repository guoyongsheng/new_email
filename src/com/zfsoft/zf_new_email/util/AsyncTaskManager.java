/**
 * 
 */
package com.zfsoft.zf_new_email.util;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

/**
 * @author wesley
 * @date: 2016-12-23
 * @Description:
 */
public class AsyncTaskManager {

	private List<AsyncTask<Object, Object, Object>> list;

	public AsyncTaskManager() {
		list = new ArrayList<>();
	}

	/**
	 * 添加任务队列
	 * 
	 * @param asyncTask
	 */
	public void add(AsyncTask<Object, Object, Object> asyncTask) {
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(asyncTask);
	}

	/**
	 * 取消请求
	 */
	public void clear() {
		if (list == null) {
			return;
		}

		for (AsyncTask<Object, Object, Object> asyncTask : list) {
			cancelRequest(asyncTask);
		}
		list = null;
	}

	/**
	 * 检查任务是否已经取消
	 * 
	 * @param asyncTask
	 *            任务
	 * @return true:取消 false:没有取消
	 */
	public boolean checkAsyncTaskIsCancel(@SuppressWarnings("rawtypes") AsyncTask asyncTask) {
		if (asyncTask != null && !asyncTask.isCancelled() && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
			return false;
		}
		return true;
	}

	/**
	 * 取消請求
	 * 
	 * @param asyncTask
	 */
	public void cancelRequest(AsyncTask<Object, Object, Object> asyncTask) {
		if (asyncTask != null && !asyncTask.isCancelled() && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
			asyncTask.cancel(true);
			asyncTask = null;
		}
	}
}
