package com.zfsoft.zf_new_email.modules.selectinnercontracts;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
 * 接口实现
 * 
 * @author wesley
 * @date 2017-2-28
 * @Description:
 */
public class SelectInnerContactSearchServiceImpl extends WebServiceUtil implements SelectInnerContactSearchService {

	private static final String TAG = "SelectInnerContactSearchServiceImpl";
	private CallBackListener<List<InnerContractsInfo>> listener;

	@Override
	public void searchContact(String name, String url, String app_token, CallBackListener<List<InnerContractsInfo>> listener) {
		this.listener = listener;
		List<DataObject> paramList = new ArrayList<DataObject>();
		try {
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("name", CodeUtil.encode(name, app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, "searchContact 联系人搜索失败 失败信息:" + e.getMessage());
		}

		asyncConnect(NAMESPACE_OA, FUN_EMAIL_GETDEPANDUSERINFOFORSEARCH, url, paramList, BaseApplication.getInstance());
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) throws Exception {

		if (response != null && !bTimeOut && listener != null) {
			List<InnerContractsInfo> result = getResult(response);
			if (result != null && result.size() > 0) {
				listener.onSuccess(result);
			} else {
				listener.onFailure(Constant.NO_DATA_IN_DATABASE);
			}
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.CONTACT_SEARCH_FAILURE);
		} else if (response == null && bTimeOut && listener != null) {
			listener.onFailure(Constant.TIME_OUT);
		}
	}

	// 解析xml
	private List<InnerContractsInfo> getResult(String xml) throws DocumentException {
		List<InnerContractsInfo> nodeList = new ArrayList<InnerContractsInfo>();
		if (xml == null) {
			return nodeList;
		}
		SAXReader reader = new SAXReader();
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		Document doc = reader.read(stream);
		Element root = doc.getRootElement();
		Element foo;
		int index = 0;
		try {
			while (true) {
				foo = (Element) (root.elementIterator("dep" + (index++))).next();
				if (foo.getParent() == null) {
					break;
				}
				InnerContractsInfo info = new InnerContractsInfo();
				String id = ((Attribute) foo.attributes().get(0)).getValue();
				String name = foo.getText().toString();
				info.setId(id);
				info.setName(name);
				nodeList.add(info);
			}
		} catch (NoSuchElementException e) {
			Logger.print(TAG, "getResult 数据解析失败 失败信息:" + e.getMessage());
		}
		return nodeList;
	}
}
