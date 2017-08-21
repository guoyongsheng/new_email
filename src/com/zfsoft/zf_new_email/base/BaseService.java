/**
 * 
 */
package com.zfsoft.zf_new_email.base;

import android.os.AsyncTask;

/**
 * @author wesley
 * @date: 2016-12-23
 * @Description:基类的接口
 */
public interface BaseService<Params, Progress, Result> {

	void add(AsyncTask<Params, Progress, Result> asyncTask);

	void clear();
	
	boolean checkAsyncIsCancel(AsyncTask<Params, Progress, Result> asyncTask);
	
	void cancelRequest(AsyncTask<Params, Progress, Result> asyncTask);
}
