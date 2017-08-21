/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.util;

import javax.mail.Session;
import javax.mail.Store;

import com.zfsoft.zf_new_email.entity.MailInfo;

/**
 * @author wesley
 * @date 2016-10-18下午12:00:17
 * @Description:
 */
public class StoreAndSessionHelper {

	private static volatile StoreAndSessionHelper instance;
	private Store store;
	private Session session;
	private MailInfo mailInfo;

	private StoreAndSessionHelper() {

	}

	public static StoreAndSessionHelper getInstance() {
		if (instance == null) {
			synchronized (StoreAndSessionHelper.class) {
				if (instance == null) {
					instance = new StoreAndSessionHelper();
				}
			}
		}

		return instance;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Store getStore() {
		return store;
	}

	public Session getSession() {
		return session;
	}

	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}

	public MailInfo getMailInfo() {
		return mailInfo;
	}

}
