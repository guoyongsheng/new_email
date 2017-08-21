/**
 * 
 */
package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
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
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-2-9
 * @Description: 接口实现
 */
public class SelectInnerContactServiceImpl extends WebServiceUtil implements SelectInnerContactService {

	private static final String TAG = "SelectInnerContactServiceImpl";
	private CallBackListener<List<InnerContractsInfo>> listener;

	@Override
	public void loadData(String url, String app_token, CallBackListener<List<InnerContractsInfo>> listener) {
		this.listener = listener;
		List<DataObject> paramList = new ArrayList<DataObject>();
		try {
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, "loadData 获取oa联系人失败 失败信息:" + e.getMessage());
		}
		asyncConnect(NAMESPACE_OA, FUN_EMAIL_GETFIRSTDEPINFO, url, paramList, BaseApplication.getInstance());
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) throws Exception {

		if (response != null && !bTimeOut && listener != null) {
			List<InnerContractsInfo> result = getResult(response);
			listener.onSuccess(result);
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.CONTRACT_IN_OA_GET_FAILURE);
		} else if (response == null && bTimeOut && listener != null) {
			listener.onFailure(Constant.TIME_OUT);
		} else if (listener != null) {
			listener.onFailure(Constant.CONTRACT_IN_OA_GET_FAILURE);
		}
	}

	// 解析数据
	private List<InnerContractsInfo> getResult(String response) {
		List<InnerContractsInfo> list = new ArrayList<InnerContractsInfo>();
		SAXReader reader = new SAXReader();
		ByteArrayInputStream stream = new ByteArrayInputStream(response.getBytes());
		Document doc;
		try {
			doc = reader.read(stream);
			Element root = doc.getRootElement();
			Element foo = root.element("depinfo");
			Element ele;
			for (Iterator<?> i = foo.elementIterator(); i.hasNext();) {
				ele = (Element) i.next();
				InnerContractsInfo info = new InnerContractsInfo();
				String id = ((Attribute) ele.attributes().get(0)).getValue();
				String sum = ((Attribute) ele.attributes().get(1)).getValue();
				String name = ele.getText().toString();
				info.setId(id);
				info.setSum(sum);
				info.setName(name);
				list.add(info);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			Logger.print(TAG, "getResult oa联系人解析失败 失败信息:" + e.getMessage());
		}
		return list;
	}
}
