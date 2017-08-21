/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
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
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-2-8
 * @Description:
 */
public class EmailSendOrReplyInOASaveMailImpl extends WebServiceUtil implements EmailSendOrReplyInOASaveMailService {

	private static final String TAG = "EmailSendOrReplyInOASaveMailImpl";
	private CallBackListener<Boolean> listener;

	@Override
	public void saveEmailInOA(String emailId, String receiverId, String receiverName, String ccId, String ccName, String subject, String content, String url, String app_token, CallBackListener<Boolean> listener) {

		this.listener = listener;

		List<DataObject> paramList = new ArrayList<DataObject>();

		try {
			paramList.add(new DataObject("yjid", CodeUtil.encode(emailId, app_token)));
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("fsrxm", CodeUtil.encode(UserInfoData.getInstance().getName(), app_token)));
			paramList.add(new DataObject("sjrxm", CodeUtil.encode(receiverName, app_token)));
			paramList.add(new DataObject("sjrdm", CodeUtil.encode(receiverId, app_token)));
			paramList.add(new DataObject("csrxm", CodeUtil.encode(ccName, app_token)));
			paramList.add(new DataObject("csrdm", CodeUtil.encode(ccId, app_token)));
			paramList.add(new DataObject("title", CodeUtil.encode(subject, app_token)));
			paramList.add(new DataObject("content", CodeUtil.encode(content, app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, "oa邮件保存失败 失败信息是:" + e.getMessage());
		}

		asyncConnect(NAMESPACE_OA, FUN_EMAIL_SAVETODRAFT, url, paramList, BaseApplication.getInstance());
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) throws Exception {

		if (response != null && !bTimeOut && listener != null) {
			boolean result = getResult(response);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_SAVE_SUCCESS);
			}
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.MAIL_SAVE_SUCCESS);
		} else if (response == null && bTimeOut && listener != null) {
			listener.onFailure(Constant.TIME_OUT);
		}
	}

	// 解析响应结果
	private boolean getResult(String xml) {
		SAXReader reader = new SAXReader();
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		Document doc;
		try {
			doc = reader.read(stream);
			Element root = doc.getRootElement();
			String code = root.elementText("code").toString();
			if (code != null && code.equals(Constant.SUCCESS_CODE)) {
				return true;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			Logger.print(TAG, " getResult 保存OA邮件结果解析失败 " + e.getMessage());
		}
		return false;
	}
}
