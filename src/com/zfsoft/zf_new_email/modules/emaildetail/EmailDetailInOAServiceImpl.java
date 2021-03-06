/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emaildetail;

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
import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.listener.CallBackListener;

/**
 * @author wesley
 * @date: 2017-1-11
 * @Description: 接口实现
 */
public class EmailDetailInOAServiceImpl extends WebServiceUtil implements IEmailDetailInOAService {

	private final static String TAG = "EmailDetailInOAServiceImpl";
	private CallBackListener<Email> listener;
	private String messageId;

	@Override
	public void getMailDetailInOA(String messageId, String endpoint, String app_token, CallBackListener<Email> listener) {
		this.listener = listener;
		this.messageId = messageId;

		List<DataObject> paramList = new ArrayList<DataObject>();
		try {
			paramList.add(new DataObject("yhm", CodeUtil.encode(UserInfoData.getInstance().getAccount(), app_token)));
			paramList.add(new DataObject("eid", CodeUtil.encode(messageId, app_token)));
			paramList.add(new DataObject("sign", CodeUtil.encode(UserInfoData.getInstance().getSign(), app_token)));
			paramList.add(new DataObject("apptoken", app_token));
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(TAG, "邮件详情获取失败 失败信息:" + e.getMessage());
		}
		asyncConnect(NAMESPACE_OA, FUN_EMAIL_GETMAILINFOBYID, endpoint, paramList, BaseApplication.getInstance());

	}

	@Override
	public void taskexecute(String response, boolean bTimeOut) {
		Logger.print(TAG, "taskexecute response = " + response + "     bTimeOut = " + bTimeOut);
		if (response != null && !bTimeOut && listener != null) {
			if (response.contains("<ResultInfo>")) {
				listener.onFailure(Constant.MAIL_DETAIL_GET_FAILURE);
			} else {
				listener.onSuccess(getMailInfo(response));
			}
		} else if (response == null && !bTimeOut && listener != null) {
			listener.onFailure(Constant.MAIL_DETAIL_GET_FAILURE);
		} else if (response == null && bTimeOut && listener != null) {
			listener.onFailure(Constant.TIME_OUT);
		}
	}

	// mail parse
	public Email getMailInfo(String xml) {
		Email mail = new Email();
		if (xml == null) {
			return mail;
		}
		SAXReader reader = new SAXReader();
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		Document doc;
		try {
			doc = reader.read(stream);
			Element root = doc.getRootElement();
			for (Iterator<?> i = root.elementIterator("mail"); i.hasNext();) {
				Element foo = (Element) i.next();
				mail.setSenderName(foo.elementText("fsr").toString());
				mail.setSendId(foo.elementText("fsryhm").toString());;
				ArrayList<String> receiverName = stringToList(foo.elementText("sjrlb").toString());
				mail.setToId(stringToList(foo.elementText("sjrdm").toString()));
				mail.setToName(receiverName);
				mail.setSentdata(foo.elementText("fssj").toString());
				mail.setSubject(foo.elementText("zt").toString());
				String attachName = foo.elementText("fjlb").toString();
				String attachSize = foo.elementText("fjsize");
				String attachId = foo.elementText("fjid");
				mail.setContent(foo.elementText("nr").toString());
				mail.setHtml(true);
				mail.setAttachments(getListAttachment(attachName, attachSize, attachId));
				mail.setTotalSize(getAttachmentTotalSize(attachSize));
				mail.setMessageID(messageId);
				// mail.setId(foo.elementText("id").toString());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			Logger.print(TAG, "getMailInfo  mail detail parse failure " + e.getMessage());
		}
		return mail;
	}

	// 计算附件的总大小
	private String getAttachmentTotalSize(String attachSize) {

		String[] arraySize = attachmentSizeToArray(attachSize);
		if (arraySize == null) {
			return null;
		}

		double totalSize = 0;
		int length = arraySize.length;
		for (int i = 0; i < length; i++) {
			String size = arraySize[i];
			if (size != null && (size.contains("M"))) {
				int index = size.indexOf("M");
				String value = size.substring(0, index);
				try {
					totalSize = totalSize + Double.valueOf(value);
				} catch (Exception e) {
					return null;
				}
			}
		}
		return totalSize + "M";
	}

	// string---list
	private ArrayList<String> stringToList(String value) {

		ArrayList<String> list = new ArrayList<>();
		if (value == null) {
			return list;
		}

		String[] array = value.split(",");
		if (array == null) {
			return list;
		}
		int length = array.length;
		for (int i = 0; i < length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	// 获取附件列表
	private ArrayList<Attachment> getListAttachment(String attachName, String attachSize, String attachId) {
		ArrayList<Attachment> list = new ArrayList<>();
		String[] arrayName = attachmentNameToArray(attachName);
		String[] arraySize = attachmentSizeToArray(attachSize);
		String[] arrayId = attachmentIdToArray(attachId);
		if (arrayName == null || arraySize == null || arrayId == null) {
			return list;
		}
		if (arrayName.length != arraySize.length || arrayName.length != arrayId.length || arraySize.length != arrayId.length) {
			return list;
		}
		int size = arrayName.length;
		for (int i = 0; i < size; i++) {
			Attachment attachment = new Attachment();
			String fileName = arrayName[i];
			String fileSize = arraySize[i];
			String id = arrayId[i];
			attachment.setAttachmentId(id);
			attachment.setFileSize(fileSize);
			attachment.setFileName(fileName);
			list.add(attachment);
		}
		return list;
	}

	// 附件名称转array
	private String[] attachmentNameToArray(String attachName) {
		if (isEmpty(attachName)) {
			return null;
		}
		return attachName.trim().split(("\\|"));
	}

	// 附件大小转array
	private String[] attachmentSizeToArray(String attachSize) {
		if (isEmpty(attachSize)) {
			return null;
		}

		return attachSize.trim().split(",");
	}

	// 附件id转array
	private String[] attachmentIdToArray(String attachId) {
		if (isEmpty(attachId)) {
			return null;
		}

		return attachId.trim().split(",");
	}

	// 判断是否为空
	private boolean isEmpty(String value) {
		if (value == null || value.length() == 0 || value.trim().length() == 0) {
			return true;
		}

		return false;
	}
}
