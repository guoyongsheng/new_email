/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailattachmentdownload;

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
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-13
 * @Description: 接口实现
 */
public class EmailAttachmentDownLoadServiceImpl extends WebServiceUtil implements EmailAttachmentDownLoadService {

	private final static String TAG = "EmailAttachmentDownLoadServiceImpl";
	private CallBackListener<Attachment> listener;

	@Override
	public void downLoadAttachment(String id, String attachmentName, String type, String endpoint, String app_token, CallBackListener<Attachment> listener) {
		this.listener = listener;

		if (type != null && type.equals("1")) {
			List<DataObject> param = new ArrayList<DataObject>();
			try {
				param.add(new DataObject("id", CodeUtil.encode(id, app_token)));
				param.add(new DataObject("apptoken", app_token));
			} catch (Exception e) {
				e.printStackTrace();
				Logger.print(TAG, "downLoadAttachment 附件下载失败 失败信息：" + e.getMessage());
			}
			syncConnect(NAMESPACE_OA, FUN_EMAIL_GETFILEMODEL, endpoint, param);
		} else if (type != null && type.equals("2")) {
			List<DataObject> param = new ArrayList<DataObject>();
			try {
				param.add(new DataObject("id", CodeUtil.encode(id, app_token)));
				param.add(new DataObject("fileName", CodeUtil.encode(attachmentName, app_token)));
				param.add(new DataObject("length", CodeUtil.encode("", app_token)));
				param.add(new DataObject("strKey", CodeUtil.encode(UserInfoData.getInstance().getJWSign(attachmentName), app_token)));
				param.add(new DataObject("apptoken", app_token));
			} catch (Exception e) {
				e.printStackTrace();
				Logger.print(TAG, "downLoadAttachment 附件下载失败 失败信息：" + e.getMessage());
			}
			syncConnect(NAMESPACE_JW, FUN_JW_GETFILEMODEL, endpoint, param);
		}
	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) {
		if (response != null && !bTimeOut && listener != null) {
			if (response.equals(Constant.TOKEN_ERROR)) {
				listener.onFailure(Constant.ATTACHMENT_DOWN_LOAD_FAILURE);
				return;
			}
			listener.onSuccess(getFileModel(response));
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.ATTACHMENT_DOWN_LOAD_FAILURE);
		} else if (response == null && bTimeOut && listener != null) {
			listener.onFailure(Constant.TIME_OUT);
		}
	}

	// 解析数据
	private Attachment getFileModel(String xml) {
		Attachment emailFJ = new Attachment();
		SAXReader reader = new SAXReader();
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		Document doc;
		try {
			doc = reader.read(stream);
			Element root = doc.getRootElement();
			for (Iterator<?> i = root.elementIterator("DOWNLOAD"); i.hasNext();) {
				Element foo = (Element) i.next();
				if (foo.elementText("FILENAME").equals("正文")) {
					emailFJ.setFileName("正文.doc");
				} else {
					emailFJ.setFileName(foo.elementText("FILENAME").toString());
				}
				emailFJ.setAttachmentId(foo.elementText("ADJUNCTID").toString());
				emailFJ.setDownLoadUrl(foo.elementText("DOWNLOADURL").toString());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			Logger.print(TAG, "getFileModel 数据解析失败 失败信息:" + e.getMessage());
		}
		return emailFJ;
	}
}
