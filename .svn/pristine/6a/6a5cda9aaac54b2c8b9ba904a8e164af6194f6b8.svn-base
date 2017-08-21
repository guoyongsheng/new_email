/**
 * 
 */
package com.zfsoft.zf_new_email.base;

import com.zfsoft.zf_new_email.util.AsyncTaskManager;

import android.os.AsyncTask;

/**
 * @author wesley
 * @date: 2016-12-23
 * @Description:
 */
public abstract class BaseServiceImpl implements BaseService<Object, Object, Object> {

	private AsyncTaskManager manager = new AsyncTaskManager();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void add(AsyncTask asyncTask) {
		manager.add(asyncTask);
	}

	@Override
	public void clear() {
		manager.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean checkAsyncIsCancel(AsyncTask asyncTask) {
		return manager.checkAsyncTaskIsCancel(asyncTask);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void cancelRequest(AsyncTask asyncTask) {
		manager.cancelRequest(asyncTask);
	}
}
