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
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-9
 * @Description:接口实现
 */
public class EmailListDeleteOAImpl extends WebServiceUtil implements IEmailListDeleteOAService {

	private static final String TAG = "EmailListDeleteOAImpl";
	private CallBackListener<Boolean> listener;

	@Override
	public void deleteMailInOA(String[] eid, String type, String endpoint, String app_token, int position, Email email, CallBackListener<Boolean> listener) {
		this.listener = listener;

		switch (Integer.parseInt(type)) {
		case 2:
			type = "2";
			break;

		case 6:
			type = "3";
			break;

		default:
			type = "1";
			break;
		}
		String method_name = "deleteMailByID";
		List<DataObject> paramList = new ArrayList<DataObject>();
		try {
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("eid", CodeUtil.encode(VariableUtil.arrayToString(eid), app_token)));
			paramList.add(new DataObject("type", CodeUtil.encode(type, app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, " deleteMailInOA 删除删除异常 异常信息是：" + e.getMessage());
		}
		asyncConnect(NAMESPACE_OA, method_name, endpoint, paramList, BaseApplication.getInstance());
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) throws Exception {

		if (response != null && !bTimeOut && listener != null) {
			boolean result = getResult(response);
			if (result) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.MAIL_DELETE_FAILURE);
			}
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.MAIL_DELETE_FAILURE);
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
			Logger.print(TAG, " getResult 刪除OA邮件结果解析失败 " + e.getMessage());
		}
		return false;
	}
}
