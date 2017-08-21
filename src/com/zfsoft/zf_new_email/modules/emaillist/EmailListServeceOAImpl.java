/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.zfsoft.core.data.DataObject;
import com.zfsoft.core.data.UserInfoData;
import com.zfsoft.core.net.WebServiceUtil;
import com.zfsoft.core.utils.CodeUtil;
import com.zfsoft.core.utils.Logger;
import com.zfsoft.zf_new_email.base.BaseApplication;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-6
 * @Description:接口实现
 */
public class EmailListServeceOAImpl extends WebServiceUtil implements IEmailListServiceInOA {

	private int type;
	private CallBackListener<ArrayList<Email>> listener;
	private static final String TAG = "EmailListServeceOAImpl";

	@Override
	public void loadDataInOA(int type, int start, int size, String endpoint, String app_token, String cond, CallBackListener<ArrayList<Email>> listener) {
		this.listener = listener;
		this.type = type;

		String method_name = "getMailListByTypeAndCond";
		List<DataObject> paramList = new ArrayList<DataObject>();
		try {
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("type", CodeUtil.encode(type + "", app_token)));
			paramList.add(new DataObject("start", CodeUtil.encode(start + "", app_token)));
			paramList.add(new DataObject("size", CodeUtil.encode(size + "", app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("cond", CodeUtil.encode(cond, app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, "loadDataInOA " + e.getMessage());
		}
		asyncConnect(NAMESPACE_OA, method_name, endpoint, paramList, BaseApplication.getInstance());
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) throws Exception {

		Logger.print(TAG, " taskexecute response = " + response + "    bTimeOut = " + bTimeOut);

		if (response != null && !bTimeOut && listener != null) {
			listener.onSuccess(getMailList(response));
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.EMAIL_LIST_GET_FAILURE);
		} else if (response == null && bTimeOut && listener != null) {
			listener.onFailure(Constant.TIME_OUT);
		}
	}

	// 解析邮件
	private ArrayList<Email> getMailList(String xml) {
		ArrayList<Email> list = new ArrayList<>();
		if (xml == null) {
			return list;
		}
		SAXReader reader = new SAXReader();
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		Document doc;
		try {
			doc = reader.read(stream);
			Element root = doc.getRootElement();
			for (Iterator<?> i = root.elementIterator("mail"); i.hasNext();) {
				Email obj = new Email();
				Element foo = (Element) i.next();
				obj.setSubject(foo.elementText("zt").toString());
				obj.setSenderName(foo.elementText("fsz").toString());
				obj.setSentdata(foo.elementText("fssj").toString());
				obj.setMessageID(foo.elementText("yjid").toString());
				String isRead = foo.elementText("sfyd").toString();
				if (isRead != null && isRead.equals("0")) {
					obj.setNews(true);
				} else if (isRead != null && isRead.equals("1")) {
					obj.setNews(false);
				}
				String hasAttachment = foo.elementText("sffj").toString();
				if (hasAttachment != null && hasAttachment.equals("0")) {
					obj.setHasAttachment(false);
				} else if (hasAttachment != null && hasAttachment.equals("1")) {
					obj.setHasAttachment(true);
				}

				String isFlag = foo.elementText("sfxb").toString();
				if (isFlag != null && isFlag.equals("1")) {
					obj.setFlaged(true);
				} else if (isFlag != null && isFlag.equals("0")) {
					obj.setFlaged(false);
				}
				list.add(obj);
			}

			for (Iterator<?> i = root.elementIterator("page"); i.hasNext();) {
				Element foo = (Element) i.next();
				if (list != null && list.size() > 0) {
					try {
						list.get(0).setInbox_type(Integer.parseInt(foo.elementText("type").toString()));
						list.get(0).setTotalEmailCount(Integer.parseInt(foo.elementText("sum").toString()));
					} catch (NumberFormatException e) {
						list.get(0).setInbox_type(type);
						list.get(0).setTotalEmailCount(list.size());
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			Logger.print(TAG, "getMailList 邮件解析失败 失败信息为:" + e.getMessage());
		}
		return list;
	}
}
