/**
 * 
 */
package com.zfsoft.zf_new_email.modules.emailsendorreply;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zfsoft.zf_new_email.base.BasePresenter;
import com.zfsoft.zf_new_email.base.BaseView;
import com.zfsoft.zf_new_email.entity.AccountInfo;
import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.entity.ContractsInfo;
import com.zfsoft.zf_new_email.entity.InnerContractsInfo;

/**
 * @author wesley
 * @date: 2016-10-24
 * @Description: 协议接口
 */
public interface EmailSendOrReplyContract {

	interface View extends BaseView<EmailSendOrReplyPresenter> {

		/**
		 * 获取主题
		 * 
		 * @return 主题
		 */
		String getSubject();

		/**
		 * 获取内容
		 * 
		 * @return 内容
		 */
		String getContent();

		/**
		 * 发送邮件
		 * 
		 * @param isDraft
		 *            是否要保存到草稿箱
		 */
		void sendEmail(boolean isDraft);

		/**
		 * 显示错误的信息
		 * 
		 * @param errorMessage
		 *            错误的信息
		 */
		void showErrorMessage(String errorMessage);

		/**
		 * 显示成功的信息
		 * 
		 * @param message
		 *            成功的信息
		 */
		void showMessage(String message);

		/**
		 * 显示对话框
		 * 
		 * @param message
		 *            信息
		 */
		void showProgressDialog(String message);

		/**
		 * 隐藏对话框
		 */
		void hideProgressDialog();

		/**
		 * 返回上个界面
		 */
		void destroryView();

		/**
		 * 显示存入草稿箱对话框
		 */
		void showDraftDialog();

		/**
		 * 保存收信人
		 */
		void saveReceives();

		/**
		 * 获取发送者地址
		 * 
		 * @return 发送者地址
		 */
		String getSenderAddress();

		/**
		 * 获取保存在sharedPreference中的数据
		 * 
		 * @param key
		 *            key
		 * 
		 * @return 联系人集合
		 */
		ArrayList<ContractsInfo> getSharedPreferencesValue(String key);

		/**
		 * 获取所有的账号列表
		 * 
		 * @return 所有的账号
		 */
		ArrayList<AccountInfo> getAccountInfo();

		/**
		 * 选择发件人的对话框
		 */
		void showAlertDialog(ArrayList<AccountInfo> list, String currentAccount);

		/**
		 * 获取提示列表
		 * 
		 * @return 提示列表
		 */
		String[] getHintList();

		/**
		 * 获取发送oa邮件的url
		 * 
		 * @return
		 */
		String getUrl();

		/**
		 * 获取发送凭证
		 * 
		 * @return
		 */
		String getToken();

		/**
		 * 获取收件人id
		 * 
		 * @return 收件人id
		 */
		String getReceiverIdInOA();

		/**
		 * 获取收件人姓名
		 * 
		 * @return 收件人姓名
		 */
		String getReceiverNameInOA();
	}

	interface Presenter extends BasePresenter {

		/**
		 * 发送邮件
		 * 
		 * @param listReceiver
		 *            收件人集合
		 * @param senderName
		 *            发送者地址
		 * @param subject
		 *            主题
		 * @param listAttachmenet
		 *            附件集合
		 * @param conetnt
		 *            内容
		 * @param isDraft
		 *            是否保存的草稿箱
		 */
		void sendEmail(List<String> listReceiver, String senderName, String subject, List<Attachment> listAttachmenet, String conetnt, boolean isDraft);

		/**
		 * 校验收件人是否为空
		 * 
		 * @param listReceiver
		 *            收件人集合
		 * @return true: 空 false:不为空
		 */
		boolean checkReceiveIsEmpty(List<String> listReceiver);

		/**
		 * 校验主题是否为空
		 * 
		 * @param subject
		 *            主题
		 * @return true: 空 false: 不为空
		 */
		boolean checkSubjectIsEmpty(String subject);

		/**
		 * 校验附件是否为空
		 * 
		 * @param listAttachmenet
		 *            附件集合
		 * @return true:空 false:不为空
		 */
		boolean checkAttachmentIsEmpty(List<Attachment> listAttachmenet);

		/**
		 * 回复邮件
		 * 
		 * @param messageNumber
		 *            被回复邮件的number
		 * @param subject
		 *            邮件的主题
		 * @param content
		 *            邮件的内容
		 * @param type
		 *            0:回复 1:回复全部
		 * 
		 * @param isDraft
		 *            是否存入草稿箱
		 * 
		 * @param inbox_type
		 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
		 * 
		 * @param receiveList
		 *            接收者邮箱地址集合
		 * 
		 * @param listAttachment
		 *            附件集合
		 */
		void replyEmail(String messageId, int messageNumber, String subject, String content, int type, boolean isDraft, int inbox_type, ArrayList<String> receiveList, List<Attachment> listAttachment);

		/**
		 * 转发邮件
		 * 
		 * @param messageNumber
		 *            被回复邮件的number
		 * @param subject
		 *            邮件的主题
		 * @param content
		 *            邮件的内容
		 * @param type
		 *            收件箱 垃圾箱 已发送
		 * @param isDraft
		 *            是否存入草稿箱 true:是 false:否
		 * 
		 * @param forwardTo
		 *            转发给谁
		 * 
		 * @param listAttachment
		 *            附件集合
		 */
		void forwardEmail(String messageId, int messageNumber, String subject, String content, int type, boolean isDraft, ArrayList<String> forwardTo, List<Attachment> listAttachment);

		/**
		 * 是否应该弹出存入草稿箱的对话框
		 * 
		 * @param listReceiver
		 *            收件人
		 * @param listCC
		 *            抄送
		 * @param subject
		 *            主题
		 * @param listAttachmenet
		 *            附件
		 * @param conetnt
		 *            内容
		 * @return true:显示 false:不显示
		 */
		boolean isShowDraftDialog(String listReceiver, List<String> listCC, String subject, List<Attachment> listAttachmenet, String conetnt);

		/**
		 * collection转化为数组
		 * 
		 * @param tags
		 *            collection集合
		 * @return 数组
		 */
		String[] convertTagSpanToArray(Collection<String> tags);

		/**
		 * string转化为对象
		 * 
		 * @param receivers
		 *            收件人
		 * @return 对象集合
		 */
		public ArrayList<ContractsInfo> stringToObject(String receivers);

		/**
		 * list转化为数组
		 * 
		 * @param list
		 *            集合
		 * @return 数组
		 */
		public String[] listToArray(ArrayList<ContractsInfo> list);

		/**
		 * string转化为arraylist
		 * 
		 * @param receivers
		 *            接收者地址
		 * @return arrayList
		 */
		public ArrayList<String> stringToArrayList(String receivers);

		/**
		 * 获取联系人列表
		 * 
		 * @param listSharedPreference
		 *            本地的联系人列表
		 * @param lists
		 *            刚发送的联系人列表
		 * @return 联系人列表
		 */
		public ArrayList<ContractsInfo> getNewList(ArrayList<ContractsInfo> listSharedPreference, ArrayList<ContractsInfo> lists);

		/**
		 * 本地的联系人列表是否全部包含刚刚发送的联系人列表
		 * 
		 * @param list_selected
		 *            本地的联系人列表
		 * @param list_input
		 *            刚刚发送的联系人列表
		 * @return true：包含 false:不包含
		 */
		public boolean contractsListContainsInputContracts(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input);

		/**
		 * 本地的联系人列表是否全部都不包含刚刚发送的联系人列表
		 * 
		 * @param list_selected
		 *            本地的联系人列表
		 * @param list_input
		 *            刚刚发送的联系人列表
		 * @return true：不包含 false:包含
		 */
		public boolean contractsListNotContainsInputContracts(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input);

		/**
		 * 获取本地联系人列表中没有的刚刚发送的联系人列表
		 * 
		 * @param list_selected
		 *            本地的联系人列表
		 * @param list_input
		 *            刚刚发送的联系人列表
		 * @return 本地联系人列表中没有的刚刚发送的联系人列表
		 */
		ArrayList<ContractsInfo> getNotContainsList(ArrayList<ContractsInfo> list_selected, ArrayList<ContractsInfo> list_input);

		/**
		 * list转string
		 * 
		 * @param list
		 *            联系人列表
		 * @return arrayList
		 */
		ArrayList<String> contractsListToStringList(ArrayList<ContractsInfo> list);

		/**
		 * 发送oa邮件
		 * 
		 * @param emailId
		 *            邮件id
		 * @param receiverId
		 *            接收者的id集合
		 * @param receiverName
		 *            接收者的姓名集合
		 * @param ccId
		 *            抄送者的id集合
		 * @param ccName
		 *            抄送者的姓名集合
		 * @param subject
		 *            主题
		 * @param content
		 *            内容
		 * @param mailType
		 *            邮件类型
		 * @param url
		 *            网址
		 * @param token
		 *            凭证
		 */
		void sendEmailInOA(String emailId, String receiverId, String receiverName, String ccId, String ccName, String subject, String content, int mailType, String url, String token);

		/**
		 * 保存邮件到草稿箱
		 * 
		 * @param emailId
		 *            邮件id
		 * @param receiverId
		 *            接收者id
		 * @param receiverName
		 *            接收者姓名
		 * @param ccId
		 *            抄送人id
		 * @param ccName
		 *            抄送人姓名
		 * @param subject
		 *            主题
		 * @param content
		 *            内容
		 * @param url
		 *            网址
		 * @param token
		 *            登录凭证
		 */
		void saveEmailInOA(String emailId, String receiverId, String receiverName, String ccId, String ccName, String subject, String content, String url, String token);

		/**
		 * 校验接收者是否为空
		 * 
		 * @param receiverId
		 *            接收者id集合
		 * @return true:空 false:不为空
		 */
		boolean checkReceiverIsEmpty(String[] receiverId);

		/**
		 * 校验主题的长度是否过长
		 * 
		 * @param subject
		 *            主题
		 * @return
		 */
		boolean checkSubjectLengthIsLong(String subject);

		/**
		 * 数组转string
		 * 
		 * @param list
		 *            数组
		 * @return string
		 */
		String arrayListToString(ArrayList<String> list);

		/**
		 * 判断上传的附件是否过大
		 * 
		 * @param filePath
		 *            附件路径
		 * @return true:过大 false:不大
		 */
		boolean checkAttachmentIsTooBig(String filePath);

		/**
		 * 判断总的附件是否过大
		 * 
		 * @param list
		 *            附件集合
		 * @return true:过大 false:不大
		 */
		boolean checkTotalAttachmentIsTooBig(List<Attachment> list);

		/**
		 * 获取不重复的内部收件人
		 * 
		 * @param listCurrent
		 *            当前list
		 * @param listSelect
		 *            选中的list
		 * @return 不重复list
		 */
		List<InnerContractsInfo> getInnerReceiver(List<InnerContractsInfo> listCurrent, List<InnerContractsInfo> listSelect);

		/**
		 * 选择的联系人列表是否全部包含输入的联系人列表
		 * 
		 * @param list_selected
		 *            选择的联系人列表
		 * @param list_input
		 *            输入的联系人列表
		 * @return true：包含 false：不包含
		 */
		boolean contractsListContainsInputContractsInner(List<InnerContractsInfo> list_selected, List<InnerContractsInfo> list_input);

		/**
		 * list转string
		 * 
		 * @param list
		 *            联系人列表
		 * @return arrayList
		 */
		ArrayList<String> contractsListToStringListInner(List<InnerContractsInfo> list);

		/**
		 * list转化为数组
		 * 
		 * @param list
		 *            集合
		 * @return 数组
		 */
		public String[] listToArrayInner(List<InnerContractsInfo> list);

		/**
		 * 选择的联系人列表是否全部都不包含输入的联系人列表
		 * 
		 * @param list_selected
		 *            选择的联系人列表
		 * @param list_input
		 *            输入的联系人列表
		 * @return true：不包含 false：包含
		 */
		boolean contractsListNotContainsInputContractsInner(List<InnerContractsInfo> list_selected, List<InnerContractsInfo> list_input);

		/**
		 * 获取不包含的list
		 * 
		 * @param list_selected
		 *            选择的联系人列表
		 * @param list_input
		 *            输入的联系人列表
		 * @return 不包含的list
		 */
		List<InnerContractsInfo> getNotContainsListInner(List<InnerContractsInfo> list_selected, List<InnerContractsInfo> list_input);

	}
}
