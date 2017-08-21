/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaillist;

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
import com.zfsoft.core.utils.VariableUtil;
import com.zfsoft.zf_new_email.base.BaseApplication;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-10
 * @Description: 接口实现
 */
public class EmialListUpdateInOAServiceImpl extends WebServiceUtil implements IEmailListUpdateMailInOAService {

	private final static String TAG = "EmialListUpdateInOAServiceImpl";
	private CallBackListener<Boolean> listener;

	@Override
	public void updateMailStatus(String[] eid, String curEmailType, String url, String app_token, CallBackListener<Boolean> listener) {

		this.listener = listener;
		List<DataObject> paramList = new ArrayList<DataObject>();

		try {
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("eid", CodeUtil.encode(VariableUtil.arrayToString(eid).trim(), app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, " updateMailStatus 跟新邮件异常 异常信息是:" + e.getMessage());
		}
		asyncConnect(NAMESPACE_OA, FUN_EMAIL_UPDATEMAILBYID, url, paramList, BaseApplication.getInstance());
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) throws Exception {

		if (response != null && !bTimeOut && listener != null) {
			boolean result = getResult(response);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_UPDATE_FAILURE);
			}
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.MAIL_UPDATE_FAILURE);
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
			Logger.print(TAG, " getResult 跟新OA邮件结果解析失败 " + e.getMessage());
		}
		return false;
	}
}
