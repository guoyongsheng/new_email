/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SearchTerm;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.zfsoft.core.utils.Logger;
import com.zfsoft.zf_new_email.base.BaseApplication;
import com.zfsoft.zf_new_email.constant.Constant;
import com.zfsoft.zf_new_email.entity.Attachment;
import com.zfsoft.zf_new_email.entity.Email;
import com.zfsoft.zf_new_email.entity.MailInfo;
import com.zfsoft.zf_new_email.entity.MailReceiver;
import com.zfsoft.zf_new_email.entity.MyAuthenticator;
import com.zfsoft.zf_new_email.entity.ResponseInfo;

/**
 * @author wesley
 * @date 2016-10-18上午9:54:04
 * @Description: 邮件帮助类
 */
public class MailHelper {
	private static volatile MailHelper instance;

	private MailHelper() {

	}

	// 单例模式
	public static MailHelper getInstance() {
		if (instance == null) {
			synchronized (MailHelper.class) {
				if (instance == null) {
					instance = new MailHelper();
				}
			}
		}
		return instance;
	}

	/**
	 * 登陆 smtp负责
	 * 
	 * @param mail
	 *            邮箱账号
	 * @param password
	 *            密码
	 * @return true:登录成功 false：登录失败
	 */
	public ResponseInfo login(String mail, String password, String login_from) {

		ResponseInfo responseInfo = new ResponseInfo();
		MailInfo mailInfo = new MailInfo();
		String host = "smtp." + mail.substring(mail.lastIndexOf("@") + 1);
		mailInfo.setMailServerHost(host);
		// mailInfo.setMailServerHost("smtphm.qiye.163.com");
		if (login_from != null && login_from.equals(String.valueOf(Constant.EMAIL_TYPE_WANGYI_qq))) {
			mailInfo.setMailServerPort("587"); // qq邮箱的端口好是587
		} else {
			mailInfo.setMailServerPort("25"); // qq邮箱的端口好是587
		}
		mailInfo.setLoginFrom(Integer.parseInt(login_from));
		mailInfo.setUserName(mail);
		mailInfo.setPassword(password);
		mailInfo.setValidate(true);
		// 判断是否要登入验证
		MyAuthenticator authenticator = null;
		if (mailInfo.isValidate()) {
			// 创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getInstance(mailInfo.getProperties(), authenticator);
		try {
			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
			transport.close();
		} catch (MessagingException e) {

			responseInfo.setCode(1);
			if (e != null) {
				String errorMessage = e.getMessage();
				switch (Integer.parseInt(login_from)) {
				case 0:

					break;

				/**
				 * 163邮箱
				 */
				case Constant.EMAIL_TYPE_WANGYI_163:
					if (errorMessage != null && errorMessage.contains("550")) {
						responseInfo.setAuthorizationCode(1);
						responseInfo.setUrl("");
					}
					break;

				/**
				 * 126邮箱
				 */
				case Constant.EMAIL_TYPE_WANGYI_126:
					if (errorMessage != null && errorMessage.contains("550")) {
						responseInfo.setAuthorizationCode(1);
						responseInfo.setUrl("");
					}
					break;

				/**
				 * sin邮箱
				 */
				case Constant.EMAIL_TYPE_SINA:
					if (errorMessage != null && errorMessage.contains("535")) {
						responseInfo.setAuthorizationCode(1);
						responseInfo.setUrl("");
					}
					break;

				/**
				 * qq邮箱
				 */
				case Constant.EMAIL_TYPE_WANGYI_qq:
					if (errorMessage != null && errorMessage.contains("535")) {
						responseInfo.setAuthorizationCode(Constant.EMAIL_TYPE_WANGYI_qq);
						responseInfo.setUrl("http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256");
					}
					break;

				default:
					break;
				}
				Logger.print(" MailHelper login ", "登录失败 失败信息为: " + errorMessage);
			}
			return responseInfo;
		}
		SharedPreferencedUtis.saveMailInfo(mailInfo, BaseApplication.getInstance());
		return responseInfo;
	}

	/**
	 * 获取所有的邮件
	 * 
	 * @param position
	 *            开始加载的位置
	 * @return 从position----position + 10之间的邮件
	 */
	@Deprecated
	public List<MailReceiver> getAllMail(int position) {
		List<MailReceiver> list = new ArrayList<>();

		try {
			Store store = StoreAndSessionHelper.getInstance().getSession().getStore("pop3");
			String temp = StoreAndSessionHelper.getInstance().getMailInfo().getMailServerHost();
			String host = temp.replace("smtp", "pop");
			store.connect(host, 110, StoreAndSessionHelper.getInstance().getMailInfo().getUserName(), StoreAndSessionHelper.getInstance().getMailInfo().getPassword());
			// 打开文件夹
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			// 总的邮件数
			int mailCount = folder.getMessageCount();
			if (mailCount == 0) {
				folder.close(true);
				store.close();
				Logger.print(" MailHelper getListMail 邮件总数 mailCount = " + mailCount, "");
				return null;
			} else {
				// 取得所有的邮件
				Message[] messages = folder.getMessages();
				int length = messages.length;

				for (int i = length - 1 - position; i >= 0; i--) {
					MailReceiver reciveMail = new MailReceiver((MimeMessage) messages[i], messages[i], false);
					list.add(reciveMail);// 添加到邮件列表中
					if (list.size() == 10) {
						break;
					}
				}
				return list;
			}
		} catch (MessagingException e) {
			Logger.print("MailHelper getListMail ", e.getMessage());
		}

		return list;
	}

	/**
	 * 获取所有的邮件 qq邮箱
	 * 
	 * @param position
	 *            开始加载的位置
	 * 
	 * @param type
	 *            类型
	 * @return 从position----position + 10之间的邮件
	 */
	public ArrayList<Email> getAllMailBySSL(int position, int type) {
		ArrayList<Email> list = new ArrayList<>();
		try {
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.put("mail.imap.host", "imap.qq.com");
			props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.imap.ssl.enable", "true");
			props.setProperty("mail.debug", "true");
			// props.put("mail.imap.timeout", 20000);
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print("MailHelper getAllMailBySSL ", "邮箱获取失败 mailInfo = null");
				return list;
			}
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Session mailsession = Session.getInstance(props);
			Store store = mailsession.getStore("imap");
			store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
			// 打开文件夹
			// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages, Junk]
			Folder folder = null;
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				folder = store.getFolder("Drafts");
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = store.getFolder("Sent Messages");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = store.getFolder("Deleted Messages");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_ONLY);
			if (type == 1 || type == 5) {
				if (type == 1) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return list;
					}
					SearchTerm term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMachNew(msg);
						}
					};
					message = folder.search(term, message);
					if (message == null || message.length <= 0) {
						folder.close(true);
						store.close();
						return list;
					}

					int messageCount = message.length;
					for (int i = messageCount - position - 1; i >= 0; i--) {
						MailReceiver reciveMail = new MailReceiver((MimeMessage) message[i], message[i], false);
						Email email = mailReceToEmail(reciveMail);
						email.setTotalEmailCount(messageCount);
						email.setInbox_type(type);
						list.add(email);// 添加到邮件列表中
						if (list.size() == 10) {
							break;
						}
					}
					folder.close(true);
					store.close();
					return list;
				} else if (type == 5) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return list;
					}
					SearchTerm term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchFlag(msg);
						}
					};
					message = folder.search(term, message);
					if (message == null || message.length <= 0) {
						folder.close(true);
						store.close();
						return list;
					}

					int messageCount = message.length;
					for (int i = messageCount - position - 1; i >= 0; i--) {
						MailReceiver reciveMail = new MailReceiver((MimeMessage) message[i], message[i], false);
						Email email = mailReceToEmail(reciveMail);
						email.setTotalEmailCount(messageCount);
						email.setInbox_type(type);
						list.add(email);// 添加到邮件列表中
						if (list.size() == 10) {
							break;
						}
					}
					folder.close(true);
					store.close();
					return list;
				}
			} else {
				int messageCount = folder.getMessageCount();
				if (messageCount <= 0) {
					folder.close(true);
					store.close();
					return list;
				}
				int start = messageCount - (position + 10) + 1;
				int end = messageCount - position;
				if (start <= 1) {
					start = 1;
				}
				if (end <= 1) {
					end = 1;
				}
				Message[] messages = folder.getMessages(start, end);
				if (messages != null) {
					int size = messages.length;
					for (int i = size - 1; i >= 0; i--) {
						MailReceiver reciveMail = new MailReceiver((MimeMessage) messages[i], messages[i], false);
						Email email = mailReceToEmail(reciveMail);
						email.setTotalEmailCount(messageCount);
						email.setInbox_type(type);
						list.add(email);// 添加到邮件列表中
					}
				}
				folder.close(true);
				store.close();
				return list;
			}
		} catch (Exception e) {
			Logger.print("MailHelper getAllMailBySSL ", e.getMessage());
		}
		return list;
	}

	// 匹配未读
	private boolean isMachNew(Message message) {
		if (message == null) {
			return false;
		}
		Flags flags;
		try {
			flags = message.getFlags();
			return flags.contains(Flags.Flag.SEEN) ? false : true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 匹配星標
	private boolean isMatchFlag(Message message) {
		if (message == null) {
			return false;
		}
		Flags flags;
		try {
			flags = message.getFlags();
			return flags.contains(Flags.Flag.FLAGGED) ? true : false;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 对象转换
	private Email mailReceToEmail(MailReceiver mailReceiver) {
		Email email = new Email();
		if (mailReceiver == null) {
			return email;
		}
		email.setMessageID(mailReceiver.getMessageID()); // 邮件id
		email.setMessageNumber(mailReceiver.getMessageNumber()); // 邮件编号
		email.setSenderName(mailReceiver.getSenderName()); // 发件人姓名
		email.setSendderAddress(mailReceiver.getSenderAddress()); // 发件人邮箱地址
		email.setToAddress(mailReceiver.getReceiveAddress()); // 收件人地址
		email.setToName(mailReceiver.getReceiveName()); // 收件人姓名
		email.setSubject(mailReceiver.getSubject()); // 邮件主题
		email.setSentdata(mailReceiver.getSentData()); // 邮件发送日期
		email.setNews(mailReceiver.isNew()); // 判断邮件是否已读
		email.setFlaged(mailReceiver.isFlaged()); // 是否为星标邮件
		email.setContent(mailReceiver.getMailContent()); // 邮件内容
		email.setHtml(mailReceiver.isHtml()); // 判断邮件类型 是文本邮件还是网页邮件
		email.setAttachments(mailReceiver.getAttachments()); // 邮件的附件
		email.setHasAttachment(mailReceiver.isHasAttachment()); // 是否有附件
		email.setTotalSize(Attachment.convertStorage(mailReceiver.getTotalSize())); // 附件的总大小
		return email;
	}

	/**
	 * imap 获取邮件信息
	 * 
	 * @param position
	 * @param type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除 5:星标邮件
	 * @return
	 */
	public ArrayList<Email> getMailReceiveListByImap(int position, int type) {
		ArrayList<Email> list = new ArrayList<>();
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper getMailReceiveListByImap ", " 获取邮件失败 信息是: mailInfo == null");
				return list;
			}
			int loginFrom = mailInfo.getLoginFrom();
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Properties prop = System.getProperties();
			prop.put("mail.imap.host", host);
			prop.put("mail.store.protocol", "imap");
			Session mailsession = Session.getInstance(prop);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			// 打开文件夹 qq [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,
			// Junk]
			// 打开文件夹 126 163[INBOX, 草稿箱, 已发送, 已删除, 垃圾邮件, 病毒文件夹]
			// 打开文件夹 sina[其它邮件, INBOX, 草稿夹, 已发送, 已删除, 垃圾邮件, 订阅邮件, 星标邮件, 商讯信息,
			// 网站通知]
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				if (loginFrom == Constant.EMAIL_TYPE_SINA) {
					folder = (IMAPFolder) store.getFolder("草稿夹");
				} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
					folder = (IMAPFolder) store.getFolder("草稿箱");
				}
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = (IMAPFolder) store.getFolder("已发送");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = (IMAPFolder) store.getFolder("已删除");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_ONLY);

			if (type == 1 || type == 5) {
				if (type == 1) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return list;
					}
					SearchTerm term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMachNew(msg);
						}
					};
					message = folder.search(term, message);
					if (message == null || message.length <= 0) {
						folder.close(true);
						store.close();
						return list;
					}

					int messageCount = message.length;
					for (int i = messageCount - position - 1; i >= 0; i--) {
						MailReceiver reciveMail = new MailReceiver((MimeMessage) message[i], message[i], false);
						Email email = mailReceToEmail(reciveMail);
						email.setTotalEmailCount(messageCount);
						email.setInbox_type(type);
						list.add(email);// 添加到邮件列表中
						if (list.size() == 10) {
							break;
						}
					}
					folder.close(true);
					store.close();
					return list;
				} else if (type == 5) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return list;
					}
					SearchTerm term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchFlag(msg);
						}
					};
					message = folder.search(term, message);
					if (message == null || message.length <= 0) {
						folder.close(true);
						store.close();
						return list;
					}

					int messageCount = message.length;
					for (int i = messageCount - position - 1; i >= 0; i--) {
						MailReceiver reciveMail = new MailReceiver((MimeMessage) message[i], message[i], false);
						Email email = mailReceToEmail(reciveMail);
						email.setTotalEmailCount(messageCount);
						email.setInbox_type(type);
						list.add(email);// 添加到邮件列表中
						if (list.size() == 10) {
							break;
						}
					}
					folder.close(true);
					store.close();
					return list;
				}
			} else {
				// 取得所有的邮件
				int messageCount = folder.getMessageCount();
				if (messageCount <= 0) {
					folder.close(true);
					store.close();
					return list;
				}
				int start = messageCount - (position + 10) + 1;
				int end = messageCount - position;
				if (start <= 1) {
					start = 1;
				}
				if (end <= 1) {
					end = 1;
				}
				Message[] messages = folder.getMessages(start, end);
				if (messages != null) {
					int size = messages.length;
					for (int i = size - 1; i >= 0; i--) {
						MailReceiver reciveMail = new MailReceiver((MimeMessage) messages[i], messages[i], false);
						Email email = mailReceToEmail(reciveMail);
						email.setTotalEmailCount(messageCount);
						email.setInbox_type(type);
						list.add(email);// 添加到邮件列表中
					}
				}
				folder.close(true);
				store.close();
				return list;
			}
		} catch (MessagingException e) {
			Logger.print("MailHelper getMailReceiveListByImap ", " 获取邮件失败 信息是 :" + e.getMessage());
		}
		return list;
	}

	/**
	 * 根据消息id删除邮件
	 * 
	 * @param messageId
	 *            消息id
	 * @param type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
	 * @return 是否删除成功
	 */
	public boolean deleteMailByMessageId(final String id, final String messageId, int type) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper deleteMailByMessageId ", " 删除失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Properties prop = System.getProperties();
			prop.put("mail.imap.host", host);
			Session mailsession = Session.getInstance(prop);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			// 打开文件夹[INBOX, 草稿箱, 已发送, 已删除, 垃圾邮件, 病毒文件夹]
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				if (loginFrom == Constant.EMAIL_TYPE_SINA) {
					folder = (IMAPFolder) store.getFolder("草稿夹");
				} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
					folder = (IMAPFolder) store.getFolder("草稿箱");
				}
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = (IMAPFolder) store.getFolder("已发送");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = (IMAPFolder) store.getFolder("已删除");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_WRITE);
			if (type == 1 || type == 5) {
				if (type == 1) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}
					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchMessageId(msg, id);
						}
					};

					message = folder.search(term, message);
					if (message == null || message.length <= 0 || message[0] == null) {
						folder.close(true);
						store.close();
						return false;
					}

					saveDeleteMessage((MimeMessage) message[0], store, mailInfo.getLoginFrom(), type);
					folder.close(true);
					store.close();
					return true;
				} else if (type == 5) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchMessageId(msg, id);
						}
					};

					message = folder.search(term, message);
					if (message == null || message.length <= 0 || message[0] == null) {
						folder.close(true);
						store.close();
						return false;
					}

					saveDeleteMessage((MimeMessage) message[0], store, mailInfo.getLoginFrom(), type);
					folder.close(true);
					store.close();
					return true;
				}
			} else {
				int count = folder.getMessageCount();
				if (count >= Integer.parseInt(messageId) && Integer.parseInt(messageId) > 0) {
					MimeMessage message = (MimeMessage) folder.getMessage(Integer.parseInt(messageId));
					if (type == 2) {
						message.setFlag(Flag.DELETED, true);
					} else {
						saveDeleteMessage(message, store, mailInfo.getLoginFrom(), type);
					}
					folder.close(true);
					store.close();
					return true;
				}
				Logger.print(" MailHelper deleteMailByMessageId ", " 删除失败 失败信息  = " + "邮件总数 小于 " + messageId);
				return false;
			}
		} catch (MessagingException e) {
			Logger.print(" MailHelper deleteMailByMessageId ", " 删除失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	/**
	 * 根据消息id删除邮件
	 * 
	 * @param list
	 *            消息id集合
	 * @param type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
	 * @return 是否删除成功
	 */
	public boolean deleteMailGroupByMessageId(ArrayList<Email> list, int type) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null || list == null) {
				Logger.print(" MailHelper deleteMailByMessageId ", " 删除失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Properties prop = System.getProperties();
			prop.put("mail.imap.host", host);
			Session mailsession = Session.getInstance(prop);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			// 打开文件夹[INBOX, 草稿箱, 已发送, 已删除, 垃圾邮件, 病毒文件夹]
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				if (loginFrom == Constant.EMAIL_TYPE_SINA) {
					folder = (IMAPFolder) store.getFolder("草稿夹");
				} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
					folder = (IMAPFolder) store.getFolder("草稿箱");
				}
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = (IMAPFolder) store.getFolder("已发送");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = (IMAPFolder) store.getFolder("已删除");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_WRITE);
			if (type == 1 || type == 5) {
				if (type == 1) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMachNew(msg);
						}
					};

					message = folder.search(term, message);
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					int size = list.size();
					for (int i = 0; i < size; i++) {
						final int messageNumber = list.get(i).getMessageNumber();
						term = new SearchTerm() {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {

								return isMatchMessageNumber(msg, messageNumber);
							}
						};

						Message[] messages = folder.search(term, message);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						saveDeleteMessage((MimeMessage) messages[0], store, mailInfo.getLoginFrom(), type);
					}
					folder.close(true);
					store.close();
					return true;
				} else if (type == 5) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchFlag(msg);
						}
					};

					message = folder.search(term, message);
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					int size = list.size();
					for (int i = 0; i < size; i++) {
						final int messageNumber = list.get(i).getMessageNumber();
						term = new SearchTerm() {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {

								return isMatchMessageNumber(msg, messageNumber);
							}
						};

						Message[] messages = folder.search(term, message);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						saveDeleteMessage((MimeMessage) messages[0], store, mailInfo.getLoginFrom(), type);
					}
					folder.close(true);
					store.close();
					return true;
				}
			} else {
				int count = folder.getMessageCount();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					int messageNumber = list.get(i).getMessageNumber();
					if (count >= messageNumber && messageNumber > 0) {
						MimeMessage message = (MimeMessage) folder.getMessage(messageNumber);
						if (type == 2) {
							message.setFlag(Flag.DELETED, true);
						} else {
							saveDeleteMessage(message, store, mailInfo.getLoginFrom(), type);
						}
					} else {
						folder.close(true);
						store.close();
						Logger.print(" MailHelper deleteMailByMessageId ", " 删除失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
						return false;
					}
				}
				folder.close(true);
				store.close();
				return true;
			}

		} catch (MessagingException e) {
			Logger.print(" MailHelper deleteMailByMessageId ", " 删除失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	/**
	 * 分組刪除qq邮件
	 * 
	 * @param list
	 *            要删除的qq邮件
	 * @param type
	 *            0:收件箱 1：未读邮件
	 * @return true:删除成功 false:删除失败
	 */
	public boolean deleteMailGroupBySSL(ArrayList<Email> list, int type) {

		try {
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.put("mail.imap.host", "imap.qq.com");
			props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.imap.ssl.enable", "true");
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null || list == null) {
				Logger.print("MailHelper deleteMailGroupBySSL ", "邮箱删除失败 mailInfo = null");
				return false;
			}
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Session mailsession = Session.getInstance(props, null);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());

			// 打开文件夹
			// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
			Folder folder = null;
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				folder = store.getFolder("Drafts");
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = store.getFolder("Sent Messages");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = store.getFolder("Deleted Messages");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_WRITE);

			if (type == 1 || type == 5) {
				if (type == 1) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMachNew(msg);
						}
					};

					message = folder.search(term, message);
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					int size = list.size();
					for (int i = 0; i < size; i++) {
						final int messageNumber = list.get(i).getMessageNumber();
						term = new SearchTerm() {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {

								return isMatchMessageNumber(msg, messageNumber);
							}
						};

						Message[] messages = folder.search(term, message);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						saveDeleteMessage((MimeMessage) messages[0], store, mailInfo.getLoginFrom(), type);
					}
					folder.close(true);
					store.close();
					return true;
				} else if (type == 5) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchFlag(msg);
						}
					};

					message = folder.search(term, message);
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}

					int size = list.size();
					for (int i = 0; i < size; i++) {
						final int messageNumber = list.get(i).getMessageNumber();
						term = new SearchTerm() {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {

								return isMatchMessageNumber(msg, messageNumber);
							}
						};

						Message[] messages = folder.search(term, message);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						saveDeleteMessage((MimeMessage) messages[0], store, mailInfo.getLoginFrom(), type);
					}
					folder.close(true);
					store.close();
					return true;
				}
			} else {
				int count = folder.getMessageCount();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					int messageNumber = list.get(i).getMessageNumber();
					if (count >= messageNumber && messageNumber > 0) {
						MimeMessage message = (MimeMessage) folder.getMessage(messageNumber);
						if (type == 2) {
							message.setFlag(Flag.DELETED, true);
						} else {
							saveDeleteMessage(message, store, mailInfo.getLoginFrom(), type);
						}
					} else {
						folder.close(true);
						store.close();
						Logger.print(" MailHelper deleteMailGroupBySSL ", " 删除失败 邮件id  = " + messageNumber);
						return false;
					}
				}
				folder.close(true);
				store.close();
				return true;
			}
		} catch (MessagingException e) {
			Logger.print(" MailHelper deleteMailGroupBySSL ", " 删除失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	// 保存已删除邮件
	public boolean saveDeleteMessage(MimeMessage deleteMessage, IMAPStore store, int loginFrom, int type) {
		// 打开文件夹 [INBOX, 草稿箱, 已发送, 已删除, 垃圾邮件, 病毒文件夹]
		IMAPFolder deleteMailBoxFolder;
		try {
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				deleteMailBoxFolder = (IMAPFolder) store.getFolder("Deleted Messages");
			} else {
				deleteMailBoxFolder = (IMAPFolder) store.getFolder("已删除");
			}
			deleteMailBoxFolder.open(Folder.READ_WRITE);
			MimeMessage deleteMessages[] = { deleteMessage };
			if (type == 4) {
				deleteMessage.setFlag(Flag.DELETED, true);
				deleteMailBoxFolder.expunge(deleteMessages);
			} else {
				deleteMailBoxFolder.appendMessages(deleteMessages);
				deleteMessage.setFlag(Flag.DELETED, true);
			}
			deleteMailBoxFolder.close(true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 根据邮件id来删除邮件 删除qq邮件
	 * 
	 * @param messageId
	 *            邮件id
	 * 
	 * @param type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
	 * @return 是否删除成功
	 */
	public boolean deleteMailBySSL(final String id, final String messageId, int type) {

		try {
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.put("mail.imap.host", "imap.qq.com");
			props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.imap.ssl.enable", "true");
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print("MailHelper getAllMailBySSL ", "邮箱删除失败 mailInfo = null");
				return false;
			}
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Session mailsession = Session.getInstance(props, null);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());

			// 打开文件夹
			// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
			Folder folder = null;
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				folder = store.getFolder("Drafts");
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = store.getFolder("Sent Messages");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = store.getFolder("Deleted Messages");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_WRITE);
			if (type == 1 || type == 5) {
				if (type == 1) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}
					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchMessageId(msg, id);
						}
					};

					message = folder.search(term, message);
					if (message == null || message.length <= 0 || message[0] == null) {
						folder.close(true);
						store.close();
						return false;
					}

					saveDeleteMessage((MimeMessage) message[0], store, mailInfo.getLoginFrom(), type);
					folder.close(true);
					store.close();
					return true;
				} else if (type == 5) {
					Message[] message = folder.getMessages();
					if (message == null) {
						folder.close(true);
						store.close();
						return false;
					}
					SearchTerm term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchMessageId(msg, id);
						}
					};

					message = folder.search(term, message);
					if (message == null || message.length <= 0 || message[0] == null) {
						folder.close(true);
						store.close();
						return false;
					}

					saveDeleteMessage((MimeMessage) message[0], store, mailInfo.getLoginFrom(), type);
					folder.close(true);
					store.close();
					return true;
				}
			} else {
				int count = folder.getMessageCount();
				if (count >= Integer.parseInt(messageId) && Integer.parseInt(messageId) > 0) {
					MimeMessage message = (MimeMessage) folder.getMessage(Integer.parseInt(messageId));
					if (type == 2) {
						message.setFlag(Flag.DELETED, true);
					} else {
						saveDeleteMessage(message, store, mailInfo.getLoginFrom(), type);
					}
					folder.close(true);
					store.close();
					return true;
				}
				Logger.print(" MailHelper deleteMailBySSL ", " 删除失败 邮件id = " + messageId);
				return false;
			}
		} catch (MessagingException e) {
			Logger.print(" MailHelper deleteMailBySSL ", " 删除失败 邮件id = " + messageId);
		}
		return false;
	}

	/**
	 * @param mailInfo
	 *            邮件信息
	 * @param loginFrom
	 *            qq登录还是163登录
	 * @return 是否发送成功
	 */
	public boolean sendTextMail(MailInfo mailInfo, int loginFrom, boolean isDraft) {

		if (mailInfo == null) {
			Logger.print(" MailHelper sendTextMail ", " 发送失败 失败信息  = " + "mailInfo == null");
			return false;
		}

		// 判断是否需要身份认证
		try {
			MailInfo mailLogin = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailLogin == null) {
				Logger.print(" MailHelper sendTextMail ", " 发送失败 失败信息  = " + "mailLogin == null");
				return false;
			}
			String userName = mailLogin.getUserName();
			if (userName == null) {
				Logger.print(" MailHelper sendTextMail ", " 发送失败 失败信息  = " + "用戶账号为空");
				return false;
			}
			String host = "smtp." + userName.substring(userName.lastIndexOf("@") + 1);
			mailInfo.setMailServerHost(host);
			loginFrom = mailLogin.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				mailInfo.setMailServerPort("587"); // qq邮箱的端口好是587
			} else {
				mailInfo.setMailServerPort("25"); // qq邮箱的端口好是587
			}
			mailInfo.setValidate(true);
			// 判断是否要登入验证
			MyAuthenticator authenticator = null;
			if (mailInfo.isValidate()) {
				// 创建一个密码验证器
				authenticator = new MyAuthenticator(userName, mailLogin.getPassword());
			}
			// 根据邮件会话属性和密码验证器构造一个发送邮件的session
			Session sendMailSession = Session.getInstance(mailInfo.getProperties(), authenticator);

			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);

			// 创建邮件发送者地址
			Address address = new InternetAddress(userName);
			// 设置邮件消息的发送者
			mailMessage.setFrom(address);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address[] tos;
			String[] receivers = mailInfo.getReceivers();
			if (receivers != null) {
				// 为每个邮件接收者创建一个地址
				tos = new InternetAddress[receivers.length];
				for (int i = 0; i < receivers.length; i++) {
					tos[i] = new InternetAddress(receivers[i]);
				}
			} else {
				Logger.print(" MailHelper sendTextMail ", " 发送失败 失败信息  = " + "没有收件人");
				return false;
			}
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, tos);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();

			Multipart mm = new MimeMultipart();// 新建一个MimeMultipart对象用来存放多个BodyPart对象
			// 设置信件文本内容
			BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
			mdp.setContent(mailContent, "text/plain;charset=utf-8");// 给BodyPart对象设置内容和格式/编码方式
			mm.addBodyPart(mdp);// 将含有信件内容的BodyPart加入到MimeMultipart对象中

			Attachment affInfos;
			FileDataSource fds1;
			List<Attachment> list = mailInfo.getAttachmentInfos();
			for (int i = 0; i < list.size(); i++) {
				affInfos = list.get(i);
				fds1 = new FileDataSource(affInfos.getFilePath());
				mdp = new MimeBodyPart();
				mdp.setDataHandler(new DataHandler(fds1));
				try {
					mdp.setFileName(MimeUtility.encodeText(fds1.getName()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				mm.addBodyPart(mdp);
			}
			mailMessage.setContent(mm);
			mailMessage.saveChanges();

			// 设置邮件支持多种格式
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			if (isDraft) {
				return saveDraftMessage((MimeMessage) mailMessage);
			} else {
				// 发送邮件
				Transport.send(mailMessage);
				if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
					saveSentMessage((MimeMessage) mailMessage);
				} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
					saveMessageSina((MimeMessage) mailMessage);
				}
			}
			return true;
		} catch (MessagingException ex) {
			Logger.print(" MailHelper sendTextMail ", " 发送失败 失败信息  = " + ex.getMessage());
		}
		return false;
	}

	// 保存草稿箱邮件
	public boolean saveDraftMessage(MimeMessage draftMessage) throws MessagingException {

		MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
		if (mailInfo == null) {
			Logger.print(" MailHelper getMailReceiveListByImap ", " 保存邮件失败 信息是: mailInfo == null");
			return false;
		}
		int loginFrom = mailInfo.getLoginFrom();
		if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.put("mail.imap.host", "imap.qq.com");
			props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.imap.ssl.enable", "true");
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Session mailsession = Session.getInstance(props, null);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
			IMAPFolder folder = (IMAPFolder) store.getFolder("Drafts");
			folder.open(Folder.READ_WRITE);
			draftMessage.setFlag(Flag.DRAFT, true);
			MimeMessage draftMessages[] = { draftMessage };
			folder.appendMessages(draftMessages);
			folder.close(true);
			store.close();
		} else {
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Properties prop = System.getProperties();
			prop.put("mail.imap.host", host);
			prop.put("mail.store.protocol", "imap");
			Session mailsession = Session.getInstance(prop);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			// 打开文件夹 [INBOX, 草稿箱, 已发送, 已删除, 垃圾邮件, 病毒文件夹]
			IMAPFolder draftsMailBoxFolder = (IMAPFolder) store.getFolder("草稿箱");
			if (loginFrom == Constant.EMAIL_TYPE_SINA) {
				draftsMailBoxFolder = (IMAPFolder) store.getFolder("草稿夹");
			} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
				draftsMailBoxFolder = (IMAPFolder) store.getFolder("草稿箱");
			}
			draftsMailBoxFolder.open(Folder.READ_WRITE);
			draftMessage.setFlag(Flag.DRAFT, true);
			MimeMessage draftMessages[] = { draftMessage };
			draftsMailBoxFolder.appendMessages(draftMessages);
			draftsMailBoxFolder.close(true);
			store.close();
		}
		return true;
	}

	// 保存已发送邮件
	public boolean saveSentMessage(MimeMessage sentMessage) throws MessagingException {
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties props = System.getProperties();
		props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.put("mail.imap.host", "imap.qq.com");
		props.put("mail.imap.auth.plain.disable", "true");
		props.put("mail.imap.ssl.enable", "true");
		MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
		if (mailInfo == null) {
			Logger.print("MailHelper saveSentMessage ", "邮箱获取失败 mailInfo = null");
			return false;
		}
		String temp = mailInfo.getMailServerHost();
		String host = temp.replace("smtp", "imap");
		Session mailsession = Session.getInstance(props, null);
		IMAPStore store = (IMAPStore) mailsession.getStore("imap");
		store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
		IMAPFolder folder = (IMAPFolder) store.getFolder("Sent Messages");
		folder.open(Folder.READ_WRITE);
		MimeMessage draftMessages[] = { sentMessage };
		folder.appendMessages(draftMessages);
		folder.close(true);
		store.close();
		return true;
	}

	// 新浪邮件发送成功后保存到已发送的文件夹
	public boolean saveMessageSina(MimeMessage sentMessage) {
		MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
		if (mailInfo == null) {
			Logger.print("MailHelper saveMessageSina ", "邮箱保存失败 mailInfo = null");
			return false;
		}
		String temp = mailInfo.getMailServerHost();
		String host = temp.replace("smtp", "imap");
		Properties prop = System.getProperties();
		prop.put("mail.imap.host", host);
		prop.put("mail.store.protocol", "imap");
		Session mailsession = Session.getInstance(prop);
		IMAPStore store;
		try {
			store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			IMAPFolder sendMailBoxFolder = (IMAPFolder) store.getFolder("已发送");
			sendMailBoxFolder.open(Folder.READ_WRITE);
			MimeMessage draftMessages[] = { sentMessage };
			sendMailBoxFolder.appendMessages(draftMessages);
			sendMailBoxFolder.close(true);
			store.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 改变邮件的状态 未读邮件变为已读邮件
	 * 
	 * @param messageId
	 *            邮件number
	 * @param status
	 *            0：标记已读 1:标记未读
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * @return
	 */
	public boolean updateMessageStatus(final String messageId, final int messageNumber, int status, int type) {

		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				/**
				 * 星标邮件
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);

				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.SEEN, true);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.SEEN, false);
						}
						folder.close(true);
						store.close();
						return true;

					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.SEEN, true);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.SEEN, false);
						}
						folder.close(true);
						store.close();
						return true;
					}

				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					if (count >= messageNumber && messageNumber > 0) {
						Message message = folder.getMessage(messageNumber);
						if (status == 0) {
							message.setFlag(Flags.Flag.SEEN, true);
						} else if (status == 1) {
							message.setFlag(Flags.Flag.SEEN, false);
						}
						folder.close(true);
						store.close();
						return true;
					}
					folder.close(true);
					store.close();
					return false;
				}
			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						folder = (IMAPFolder) store.getFolder("草稿夹");
					} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
						folder = (IMAPFolder) store.getFolder("草稿箱");
					}
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				/**
				 * 星标邮件
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);

				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.SEEN, true);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.SEEN, false);
						}
						folder.close(true);
						store.close();
						return true;

					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.SEEN, true);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.SEEN, false);
						}
						folder.close(true);
						store.close();
						return true;
					}

				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					if (count >= messageNumber && messageNumber > 0) {
						Message message = folder.getMessage(messageNumber);
						if (status == 0) {
							message.setFlag(Flags.Flag.SEEN, true);
						} else if (status == 1) {
							message.setFlag(Flags.Flag.SEEN, false);
						}
						folder.close(true);
						store.close();
						return true;
					}

					folder.close(true);
					store.close();
					return false;
				}
			}
			Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
			return false;
		} catch (MessagingException e) {
			Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	/**
	 * 改变邮件的状态 未读邮件变为已读邮件
	 * 
	 * @param list
	 *            邮件集合
	 * @param status
	 *            0：标记已读 1:标记未读
	 * @param type
	 *            0:收件箱 1:未读邮件 3:已发送 4:已删除
	 * @return
	 */
	public boolean updateMessageGroupStatus(List<Email> list, int status, int type) {

		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null || list == null) {
				Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				/**
				 * 星标邮件
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);

				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMachNew(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();
							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || message[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}
							if (status == 0) {
								msg[0].setFlag(Flags.Flag.SEEN, true);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.SEEN, false);
							}
						}
						folder.close(true);
						store.close();
						return true;

					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchFlag(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();
							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}
							if (status == 0) {
								msg[0].setFlag(Flags.Flag.SEEN, true);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.SEEN, false);
							}
						}
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					int size = list.size();
					for (int i = 0; i < size; i++) {
						int messageNumber = list.get(i).getMessageNumber();
						if (count >= messageNumber && messageNumber > 0) {
							Message message = folder.getMessage(messageNumber);
							if (status == 0) {
								message.setFlag(Flags.Flag.SEEN, true);
							} else if (status == 1) {
								message.setFlag(Flags.Flag.SEEN, false);
							}
						} else {
							folder.close(true);
							store.close();
							Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
							return false;
						}
					}
					folder.close(true);
					store.close();
					return true;
				}

			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						folder = (IMAPFolder) store.getFolder("草稿夹");
					} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
						folder = (IMAPFolder) store.getFolder("草稿箱");
					}
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				/**
				 * 星标邮件
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);

				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}
						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMachNew(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();
							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}
							if (status == 0) {
								msg[0].setFlag(Flags.Flag.SEEN, true);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.SEEN, false);
							}
						}
						folder.close(true);
						store.close();
						return true;

					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchFlag(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();
							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}
							if (status == 0) {
								msg[0].setFlag(Flags.Flag.SEEN, true);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.SEEN, false);
							}
						}
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					int size = list.size();
					for (int i = 0; i < size; i++) {
						int messageNumber = list.get(i).getMessageNumber();
						if (count >= messageNumber && messageNumber > 0) {
							Message message = folder.getMessage(messageNumber);
							if (status == 0) {
								message.setFlag(Flags.Flag.SEEN, true);
							} else if (status == 1) {
								message.setFlag(Flags.Flag.SEEN, false);
							}
						} else {
							folder.close(true);
							store.close();
							Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
							return false;
						}
					}
					folder.close(true);
					store.close();
					return true;
				}
			}
		} catch (MessagingException e) {
			Logger.print(" MailHelper updateMessageStatus ", " 跟新失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	/**
	 * 回复邮件
	 * 
	 * @param messageNumber
	 *            邮件number
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param type
	 *            0:回复 1:回复全部
	 * @param inbox_type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除
	 * 
	 * @param receiveMail
	 *            接收者的邮箱地址集合
	 * 
	 * @param listAttachment
	 *            附件的集合
	 * 
	 * @param isDraft
	 *            是否保存到草稿箱中
	 * @return true：回复成功 false：回复失败
	 * 
	 */
	public boolean replyMail(final String messageId, final String messageNumber, String subject, String content, int type, int inbox_type, List<String> receiveMail, List<Attachment> listAttachment, boolean isDraft) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper replyMail ", " 回复邮件信息失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = null;
				switch (inbox_type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					folder = (IMAPFolder) store.getFolder("Drafts");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				/**
				 * star email
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}

				folder.open(Folder.READ_WRITE);
				Message message = null;
				if (inbox_type == 1 || inbox_type == 5) {
					if (inbox_type == 1) {
						Message[] messages = folder.getMessages();
						if (messages == null) {
							folder.close(true);
							store.close();
							return false;
						}
						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						messages = folder.search(term, messages);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}
						message = messages[0];

					} else if (inbox_type == 5) {
						Message[] messages = folder.getMessages();
						if (messages == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						messages = folder.search(term, messages);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}
						message = messages[0];
					}

					MimeMessage replayMessage;
					if (type == 1) {
						replayMessage = (MimeMessage) message.reply(true);
					} else {
						replayMessage = (MimeMessage) message.reply(false);
					}
					replayMessage.setFrom(new InternetAddress(mailInfo.getUserName()));
					replayMessage.setText(content);
					replayMessage.setSubject(subject);
					int size = receiveMail.size();
					Address[] address = new InternetAddress[size];
					for (int i = 0; i < size; i++) {
						address[i] = new InternetAddress(receiveMail.get(i));
					}
					replayMessage.setRecipients(Message.RecipientType.TO, address);

					Multipart mm = new MimeMultipart();
					BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
					mdp.setContent(content, "text/plain;charset=utf-8"); // 给BodyPart对象设置内容和格式/编码方式
					mm.addBodyPart(mdp);

					MailReceiver mailReceiver = new MailReceiver((MimeMessage) message, message, true);
					Email email = mailReceToEmail(mailReceiver);
					mdp = new MimeBodyPart();
					mdp.setContent(mailInfoToString(email, 0), "text/plain;charset=utf-8");
					mm.addBodyPart(mdp);

					if (listAttachment != null) {
						int sizeAttachment = listAttachment.size();
						for (int i = 0; i < sizeAttachment; i++) {
							Attachment affInfos = listAttachment.get(i);
							FileDataSource fds1 = new FileDataSource(affInfos.getFilePath());
							mdp = new MimeBodyPart();
							mdp.setDataHandler(new DataHandler(fds1));
							try {
								mdp.setFileName(MimeUtility.encodeText(fds1.getName()));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							mm.addBodyPart(mdp);
						}
					}
					replayMessage.setContent(mm);
					replayMessage.saveChanges();

					// 设置邮件支持多种格式
					MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
					mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
					mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
					mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
					mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
					mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
					CommandMap.setDefaultCommandMap(mc);

					Transport transport = mailsession.getTransport("smtp");
					transport.connect(temp, 587, mailInfo.getUserName(), mailInfo.getPassword());

					if (isDraft) {
						return saveDraftMessage((MimeMessage) replayMessage);
					} else {
						transport.sendMessage(replayMessage, replayMessage.getAllRecipients());
						if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
							saveSentMessage((MimeMessage) replayMessage);
						} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
							saveMessageSina((MimeMessage) replayMessage);
						}
						transport.close();
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					int totalCount = folder.getMessageCount();
					if (totalCount >= Integer.parseInt(messageNumber)) {
						message = folder.getMessage(Integer.parseInt(messageNumber));
						MimeMessage replayMessage;
						if (type == 1) {
							replayMessage = (MimeMessage) message.reply(true);
						} else {
							replayMessage = (MimeMessage) message.reply(false);
						}
						replayMessage.setFrom(new InternetAddress(mailInfo.getUserName()));
						replayMessage.setText(content);
						replayMessage.setSubject(subject);
						int size = receiveMail.size();
						Address[] address = new InternetAddress[size];
						for (int i = 0; i < size; i++) {
							address[i] = new InternetAddress(receiveMail.get(i));
						}
						replayMessage.setRecipients(Message.RecipientType.TO, address);

						Multipart mm = new MimeMultipart();
						BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
						mdp.setContent(content, "text/plain;charset=utf-8"); // 给BodyPart对象设置内容和格式/编码方式
						mm.addBodyPart(mdp);

						MailReceiver mailReceiver = new MailReceiver((MimeMessage) message, message, true);
						Email email = mailReceToEmail(mailReceiver);
						mdp = new MimeBodyPart();
						mdp.setContent(mailInfoToString(email, 0), "text/plain;charset=utf-8");
						mm.addBodyPart(mdp);

						if (listAttachment != null) {
							int sizeAttachment = listAttachment.size();
							for (int i = 0; i < sizeAttachment; i++) {
								Attachment affInfos = listAttachment.get(i);
								FileDataSource fds1 = new FileDataSource(affInfos.getFilePath());
								mdp = new MimeBodyPart();
								mdp.setDataHandler(new DataHandler(fds1));
								try {
									mdp.setFileName(MimeUtility.encodeText(fds1.getName()));
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								mm.addBodyPart(mdp);
							}
						}
						replayMessage.setContent(mm);
						replayMessage.saveChanges();

						// 设置邮件支持多种格式
						MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
						mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
						mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
						mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
						mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
						mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
						CommandMap.setDefaultCommandMap(mc);

						Transport transport = mailsession.getTransport("smtp");
						transport.connect(temp, 587, mailInfo.getUserName(), mailInfo.getPassword());

						if (isDraft) {
							return saveDraftMessage((MimeMessage) replayMessage);
						} else {
							transport.sendMessage(replayMessage, replayMessage.getAllRecipients());
							if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
								saveSentMessage((MimeMessage) replayMessage);
							} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
								saveMessageSina((MimeMessage) replayMessage);
							}
							transport.close();
							folder.close(true);
							store.close();
							return true;
						}
					}
					Logger.print(" MailHelper replyMail ", " 回复邮件信息失败 失败信息是 邮件总数小于 " + messageNumber);
					folder.close(true);
					store.close();
					return false;
				}

			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				// 打开文件夹
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (inbox_type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						folder = (IMAPFolder) store.getFolder("草稿夹");
					} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
						folder = (IMAPFolder) store.getFolder("草稿箱");
					}
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				/**
				 * star email
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_ONLY);

				if (type == 1 || type == 5) {
					Message message = null;
					if (inbox_type == 1) {
						Message[] messages = folder.getMessages();
						if (messages == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						messages = folder.search(term, messages);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}
						message = messages[0];

					} else if (inbox_type == 5) {
						Message[] messages = folder.getMessages();
						if (messages == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						messages = folder.search(term, messages);
						if (messages == null || messages.length <= 0 || messages[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}
						message = messages[0];
					}

					MimeMessage replayMessage;
					if (type == 1) {
						replayMessage = (MimeMessage) message.reply(true);
					} else {
						replayMessage = (MimeMessage) message.reply(false);
					}
					replayMessage.setFrom(new InternetAddress(mailInfo.getUserName()));
					replayMessage.setText(content);
					replayMessage.setSubject(subject);
					int size = receiveMail.size();
					Address[] address = new InternetAddress[size];
					for (int i = 0; i < size; i++) {
						address[i] = new InternetAddress(receiveMail.get(i));
					}
					replayMessage.setRecipients(Message.RecipientType.TO, address);

					Multipart mm = new MimeMultipart();
					BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
					mdp.setContent(content, "text/plain;charset=utf-8"); // 给BodyPart对象设置内容和格式/编码方式
					mm.addBodyPart(mdp);

					MailReceiver mailReceiver = new MailReceiver((MimeMessage) message, message, true);
					Email email = mailReceToEmail(mailReceiver);
					mdp = new MimeBodyPart();
					mdp.setContent(mailInfoToString(email, 0), "text/plain;charset=utf-8");
					mm.addBodyPart(mdp);

					if (listAttachment != null) {
						int sizeAttachment = listAttachment.size();
						for (int i = 0; i < sizeAttachment; i++) {
							Attachment affInfos = listAttachment.get(i);
							FileDataSource fds1 = new FileDataSource(affInfos.getFilePath());
							mdp = new MimeBodyPart();
							mdp.setDataHandler(new DataHandler(fds1));
							try {
								mdp.setFileName(MimeUtility.encodeText(fds1.getName()));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							mm.addBodyPart(mdp);
						}
					}
					replayMessage.setContent(mm);
					replayMessage.saveChanges();

					// 设置邮件支持多种格式
					MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
					mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
					mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
					mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
					mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
					mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
					CommandMap.setDefaultCommandMap(mc);

					Transport transport = mailsession.getTransport("smtp");
					transport.connect(temp, 587, mailInfo.getUserName(), mailInfo.getPassword());

					if (isDraft) {
						return saveDraftMessage((MimeMessage) replayMessage);
					} else {
						transport.sendMessage(replayMessage, replayMessage.getAllRecipients());
						if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
							saveSentMessage((MimeMessage) replayMessage);
						} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
							saveMessageSina((MimeMessage) replayMessage);
						}
						transport.close();
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					int totalCount = folder.getMessageCount();
					if (totalCount >= Integer.parseInt(messageNumber)) {
						Message message = folder.getMessage(Integer.parseInt(messageNumber));
						MimeMessage replayMessage;
						if (type == 0) {
							replayMessage = (MimeMessage) message.reply(false);
						} else {
							replayMessage = (MimeMessage) message.reply(true);
						}
						replayMessage.setFrom(new InternetAddress(mailInfo.getUserName()));
						// replayMessage.setText(content);
						replayMessage.setSubject(subject);
						int size = receiveMail.size();
						Address[] address = new InternetAddress[size];
						for (int i = 0; i < size; i++) {
							address[i] = new InternetAddress(receiveMail.get(i));
						}
						replayMessage.setRecipients(Message.RecipientType.TO, address);
						Multipart mm = new MimeMultipart();
						BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
						mdp.setContent(content, "text/plain;charset=utf-8"); // 给BodyPart对象设置内容和格式/编码方式
						mm.addBodyPart(mdp);

						MailReceiver mailReceiver = new MailReceiver((MimeMessage) message, message, true);
						Email email = mailReceToEmail(mailReceiver);
						mdp = new MimeBodyPart();
						mdp.setContent(mailInfoToString(email, 0), "text/plain;charset=utf-8");
						mm.addBodyPart(mdp);

						if (listAttachment != null) {
							int sizeAttachment = listAttachment.size();
							for (int i = 0; i < sizeAttachment; i++) {
								Attachment affInfos = listAttachment.get(i);
								FileDataSource fds1 = new FileDataSource(affInfos.getFilePath());
								mdp = new MimeBodyPart();
								mdp.setDataHandler(new DataHandler(fds1));
								try {
									mdp.setFileName(MimeUtility.encodeText(fds1.getName()));
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								mm.addBodyPart(mdp);
							}
						}
						replayMessage.setContent(mm);
						replayMessage.saveChanges();

						// 设置邮件支持多种格式
						MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
						mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
						mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
						mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
						mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
						mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
						CommandMap.setDefaultCommandMap(mc);

						Transport transport = mailsession.getTransport("smtp");
						transport.connect(temp, mailInfo.getUserName(), mailInfo.getPassword());
						if (isDraft) {
							return saveDraftMessage((MimeMessage) replayMessage);
						} else {
							transport.sendMessage(replayMessage, replayMessage.getAllRecipients());
							if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
								saveSentMessage((MimeMessage) replayMessage);
							} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
								saveMessageSina((MimeMessage) replayMessage);
							}
							transport.close();
							folder.close(true);
							store.close();
							return true;
						}
					}
					Logger.print(" MailHelper getMessageByMessageNumber ", " 回复邮件信息失败 失败信息是 邮件总数小于 " + messageNumber);
					folder.close(true);
					store.close();
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(" MailHelper getMessageByMessageNumber ", " 回复邮件信息失败 失败信息是 " + e.getMessage());
		}
		return false;
	}

	/**
	 * 转发邮件
	 * 
	 * @param messageNumber
	 *            邮件number
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param type
	 *            类型
	 * 
	 * @param isDraft
	 *            是否存入草稿箱
	 * 
	 * @param forwradTo
	 *            转发给谁
	 * @return true:转发成功 false：转发失败
	 */
	public boolean forwardMail(final String messageId, String messageNumber, String subject, String content, int type, boolean isDraft, ArrayList<String> forwradTo, List<Attachment> listAttachment) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper replyMail ", " 转发邮件信息失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = null;
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					folder = (IMAPFolder) store.getFolder("Drafts");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				/**
				 * 星标邮件
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}

				folder.open(Folder.READ_ONLY);

				Message[] messages = folder.getMessages();
				if (messages == null) {
					folder.close(true);
					store.close();
					return false;
				}

				SearchTerm term = new SearchTerm() {

					private static final long serialVersionUID = 1L;

					@Override
					public boolean match(Message msg) {
						return isMatchMessageId(msg, messageId);
					}
				};

				messages = folder.search(term, messages);
				if (messages == null || messages.length <= 0 || messages[0] == null) {
					folder.close(true);
					store.close();
					return false;
				}
				Message message = messages[0];
				MimeMessage forward = new MimeMessage(mailsession);
				forward.setSubject(subject);
				String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
				forward.setFrom(new InternetAddress(to));
				int size = forwradTo.size();
				Address[] address = new InternetAddress[size];
				for (int i = 0; i < size; i++) {
					address[i] = new InternetAddress(forwradTo.get(i));
				}
				forward.setRecipients(Message.RecipientType.TO, address);

				// 文字邮件体部分
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(content, "text/plain;charset=utf-8"); // 创建Multipart的容器

				// Multipart
				MimeMultipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart); // 被转发的文字邮件体部分
				messageBodyPart = new MimeBodyPart();
				MailReceiver mailReceiver = new MailReceiver((MimeMessage) message, message, true);
				Email email = mailReceToEmail(mailReceiver);
				messageBodyPart.setContent(mailInfoToString(email, 0), "text/plain;charset=utf-8"); //
				// 添加到Multipart容器
				multipart.addBodyPart(messageBodyPart);
				if (listAttachment != null) {
					int sizeAttachment = listAttachment.size();
					for (int i = 0; i < sizeAttachment; i++) {
						Attachment affInfos = listAttachment.get(i);
						FileDataSource fds1 = new FileDataSource(affInfos.getFilePath());
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.setDataHandler(new DataHandler(fds1));
						try {
							messageBodyPart.setFileName(MimeUtility.encodeText(fds1.getName()));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						multipart.addBodyPart(messageBodyPart);
					}
				}
				forward.setContent(multipart);
				forward.saveChanges();

				// 设置邮件支持多种格式
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);

				// 发送
				Transport transport = mailsession.getTransport("smtp");
				transport.connect(temp, 587, mailInfo.getUserName(), mailInfo.getPassword());

				if (isDraft) {
					return saveDraftMessage(forward);
				} else {
					transport.sendMessage(forward, forward.getAllRecipients());
					if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
						saveSentMessage((MimeMessage) forward);
					} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						saveMessageSina((MimeMessage) forward);
					}
					transport.close();
					folder.close(true);
					store.close();
					return true;
				}
			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				// 打开文件夹
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						folder = (IMAPFolder) store.getFolder("草稿夹");
					} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
						folder = (IMAPFolder) store.getFolder("草稿箱");
					}
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				/**
				 * 星标邮件
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}

				folder.open(Folder.READ_ONLY);
				Message[] messages = folder.getMessages();
				if (messages == null) {
					folder.close(true);
					store.close();
					return false;
				}

				SearchTerm term = new SearchTerm() {

					private static final long serialVersionUID = 1L;

					@Override
					public boolean match(Message msg) {
						return isMatchMessageId(msg, messageId);
					}
				};

				messages = folder.search(term, messages);
				if (messages == null || messages.length <= 0 || messages[0] == null) {
					folder.close(true);
					store.close();
					return false;
				}
				Message message = messages[0];
				MimeMessage forward = new MimeMessage(mailsession);
				forward.setSubject(subject);
				String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
				forward.setFrom(new InternetAddress(to));
				int size = forwradTo.size();
				Address[] address = new InternetAddress[size];
				for (int i = 0; i < size; i++) {
					address[i] = new InternetAddress(forwradTo.get(i));
				}
				forward.setRecipients(Message.RecipientType.TO, address);

				// 文字邮件体部分
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(content, "text/plain;charset=utf-8"); // 创建Multipart的容器
																					// Multipart
				MimeMultipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart); // 被转发的文字邮件体部分
				messageBodyPart = new MimeBodyPart();
				MailReceiver mailReceiver = new MailReceiver((MimeMessage) message, message, true);
				Email email = mailReceToEmail(mailReceiver);
				messageBodyPart.setContent(mailInfoToString(email, 0), "text/plain;charset=utf-8");
				// 添加到Multipart容器
				multipart.addBodyPart(messageBodyPart);
				if (listAttachment != null) {
					int sizeAttachment = listAttachment.size();
					for (int i = 0; i < sizeAttachment; i++) {
						Attachment affInfos = listAttachment.get(i);
						FileDataSource fds1 = new FileDataSource(affInfos.getFilePath());
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.setDataHandler(new DataHandler(fds1));
						try {
							messageBodyPart.setFileName(MimeUtility.encodeText(fds1.getName()));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						multipart.addBodyPart(messageBodyPart);
					}
				}
				forward.setContent(multipart);
				forward.saveChanges();

				// 设置邮件支持多种格式
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);

				// 发送
				Transport transport = mailsession.getTransport("smtp");
				transport.connect(temp, mailInfo.getUserName(), mailInfo.getPassword());

				if (isDraft) {
					return saveDraftMessage(forward);
				} else {
					transport.sendMessage(forward, forward.getAllRecipients());
					if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
						saveSentMessage((MimeMessage) forward);
					} else if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						saveMessageSina((MimeMessage) forward);
					}
					transport.close();
					folder.close(true);
					store.close();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.print(" MailHelper forwardMail ", " 转发邮件信息失败 失败信息是 " + e.getMessage());
		}
		return false;
	}

	/**
	 * 搜索邮件
	 * 
	 * @param startPosition
	 *            开始位置
	 * @param type
	 *            0:收件箱 1:未读邮件 2:草稿箱 3:已发送 4:已删除 5:星标邮件
	 * @param status
	 *            0:
	 * @param content
	 *            搜索内容
	 * @return
	 */
	public List<Email> searchMail(int startPosition, int type, int status, final String content) {
		try {
			List<Email> list = new ArrayList<>();
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper searchMail ", " 搜索失败 失败信息是 mailInfo == null");
				return list;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					folder = (IMAPFolder) store.getFolder("Drafts");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_ONLY);
				SearchTerm term = null;
				switch (status) {
				/**
				 * 按发件人搜索
				 */
				case 0:
					term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchSender(msg, content);
						}
					};
					break;

				/**
				 * 按收件人搜索
				 */
				case 1:
					term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchReceiver(msg, content);
						}
					};
					break;

				/**
				 * 按主题搜索
				 */
				case 2:
					term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchSubject(msg, content);
						}
					};
					break;

				/**
				 * 搜索全部
				 */
				case 3:
					term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchSubject(msg, content) || isMatchReceiver(msg, content) || isMatchSender(msg, content) || isMatchContent(msg, content);
						}
					};
					break;

				default:
					break;
				}

				Message[] message = folder.search(term);
				if (message == null) {
					return list;
				}
				int length = message.length;
				for (int i = length - 1 - startPosition; i >= 0; i--) {
					MailReceiver mailReceiver = new MailReceiver((MimeMessage) message[i], message[i], false);
					Email email = mailReceToEmail(mailReceiver);
					email.setInbox_type(status);
					list.add(email);// 添加到邮件列表中
					if (list.size() == 10) {
						break;
					}
				}
				folder.close(true);
				store.close();
				return list;

			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = null;
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 草稿箱
				 */
				case 2:
					if (loginFrom == Constant.EMAIL_TYPE_SINA) {
						folder = (IMAPFolder) store.getFolder("草稿夹");
					} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
						folder = (IMAPFolder) store.getFolder("草稿箱");
					}
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_ONLY);
				SearchTerm term = null;
				switch (status) {
				/**
				 * 按发件人搜索
				 */
				case 0:
					term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchSender(msg, content);
						}
					};
					break;

				/**
				 * 按收件人搜索
				 */
				case 1:
					term = new SearchTerm() {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchReceiver(msg, content);
						}
					};
					break;

				/**
				 * 按主题搜索
				 */
				case 2:
					term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchSubject(msg, content);
						}
					};
					break;

				/**
				 * 搜索全部
				 */
				case 3:
					term = new SearchTerm() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean match(Message msg) {
							return isMatchSubject(msg, content) || isMatchReceiver(msg, content) || isMatchSender(msg, content) || isMatchContent(msg, content);
						}
					};
					break;

				default:
					break;
				}

				Message[] message = folder.search(term);
				if (message == null) {
					return list;
				}
				int length = message.length;
				for (int i = length - 1 - startPosition; i >= 0; i--) {
					MailReceiver mailReceiver = new MailReceiver((MimeMessage) message[i], message[i], false);
					Email email = mailReceToEmail(mailReceiver);
					email.setInbox_type(status);
					list.add(email);// 添加到邮件列表中
					if (list.size() == 10) {
						break;
					}
				}
				folder.close(true);
				store.close();
				return list;
			}
		} catch (MessagingException e) {
			Logger.print(" MailHelper searchMail ", " 搜索失败 失败信息  = " + e.getMessage());
		}
		return null;
	}

	// 是否匹配主题
	private boolean isMatchSubject(Message msg, String content) {
		if (msg != null) {
			try {
				String subject = msg.getSubject();
				if (subject != null && subject.contains(content)) {
					return true;
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 是否匹配收件人
	private boolean isMatchReceiver(Message msg, String content) {
		if (msg != null) {
			try {
				InternetAddress[] internetAddresses = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);
				if (internetAddresses != null) {
					int length = internetAddresses.length;
					for (int i = 0; i < length; i++) {
						String address = internetAddresses[i].getAddress();
						String name = internetAddresses[i].getPersonal();
						if ((address != null && address.contains(content)) || (name != null && name.contains(content))) {
							return true;
						}
					}
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 是否匹配发件人
	private boolean isMatchSender(Message msg, String content) {
		if (msg != null) {
			try {
				InternetAddress[] internetAddresses = (InternetAddress[]) msg.getFrom();
				if (internetAddresses != null && internetAddresses.length > 0) {
					String address = internetAddresses[0].getAddress();
					String name = internetAddresses[0].getPersonal();
					if ((address != null && address.contains(content)) || (name != null && name.contains(content))) {
						return true;
					}
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 是否匹配number
	private boolean isMatchMessageNumber(Message msg, int messageNumber) {
		if (msg != null) {
			int number = msg.getMessageNumber();
			if (number == messageNumber) {
				return true;
			}
		}
		return false;
	}

	// 是否比配messageId
	private boolean isMatchMessageId(Message msg, String messageId) {
		if (msg != null) {
			String id;
			try {
				id = ((MimeMessage) msg).getMessageID();
				if (id != null && id.equals(messageId)) {
					return true;
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 是否匹配内容
	private boolean isMatchContent(Message msg, String content) {
		if (msg != null) {
			MailReceiver mailReceiver = new MailReceiver((MimeMessage) msg, msg, false);
			String mailContent = mailReceiver.getMailContent();
			if (mailContent != null && mailContent.contains(content)) {
				return true;
			}
		}
		return false;
	}

	// 邮件转化为内容
	public String mailInfoToString(Email email, int type) {
		StringBuffer buffer = new StringBuffer();
		if (email == null) {
			return buffer.toString();
		}

		String senderName = email.getSenderName(); // 发件人姓名
		String senderAddress = email.getSendderAddress(); // 发件人地址
		String sendTime = email.getSentdata(); // 发件时间
		StringBuffer bufferReceive = new StringBuffer();
		ArrayList<String> listName = email.getToName();
		ArrayList<String> listAddress = email.getToAddress();
		if (listName != null && listAddress != null && listName.size() == listAddress.size()) {
			int size = listName.size();
			for (int i = 0; i < size; i++) {
				String receiveName = listName.get(i);
				String receiveAddress = listAddress.get(i);
				bufferReceive.append(receiveName + "<" + receiveAddress + ">");
			}
		}
		String subject = email.getSubject(); // 主题
		buffer.append("\n");
		if (type == 0) {
			buffer.append("\n\n");
		}
		buffer.append("---原始邮件---" + "\n");
		buffer.append("发件人:" + senderName + "<" + senderAddress + ">" + "\n");
		buffer.append("发送时间:" + sendTime + "\n");
		buffer.append("收件人:" + bufferReceive.toString() + "\n");
		buffer.append("主题:" + subject + "\n");
		return buffer.toString();
	}

	/**
	 * 添加星标或者取消添加星标
	 * 
	 * @param messageNumber
	 *            messageNumber
	 * @param status
	 *            0:取消星标 1:添加星标
	 * @param type
	 *            收件箱类型
	 * @return true:成功 false:失败
	 */
	public Boolean markOrUnMarkmail(final String messageId, final int messageNumber, int status, int type) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper markOrUnMarkmail ", " 标记失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				/**
				 * star email
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);
				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.FLAGGED, false);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.FLAGGED, true);
						}
						folder.close(true);
						store.close();
						return true;

					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.FLAGGED, false);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.FLAGGED, true);
						}
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					if (count >= messageNumber && messageNumber > 0) {
						Message message = folder.getMessage(messageNumber);
						if (status == 0) {
							message.setFlag(Flags.Flag.FLAGGED, false);
						} else if (status == 1) {
							message.setFlag(Flags.Flag.FLAGGED, true);
						}
						folder.close(true);
						store.close();
						return true;
					}
					folder.close(true);
					store.close();
					return false;
				}
			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				/**
				 * star email
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);

				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.FLAGGED, false);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.FLAGGED, true);
						}
						folder.close(true);
						store.close();
						return true;

					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchMessageId(msg, messageId);
							}
						};

						message = folder.search(term, message);
						if (message == null || message.length <= 0 || message[0] == null) {
							folder.close(true);
							store.close();
							return false;
						}

						if (status == 0) {
							message[0].setFlag(Flags.Flag.FLAGGED, false);
						} else if (status == 1) {
							message[0].setFlag(Flags.Flag.FLAGGED, true);
						}
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					if (count >= messageNumber && messageNumber > 0) {
						Message message = folder.getMessage(messageNumber);
						if (status == 0) {
							message.setFlag(Flags.Flag.FLAGGED, false);
						} else if (status == 1) {
							message.setFlag(Flags.Flag.FLAGGED, true);
						}
						folder.close(true);
						store.close();
						return true;
					}

					folder.close(true);
					store.close();
					return false;
				}
			}
			Logger.print(" MailHelper markOrUnMarkmail ", " 标记失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
			return false;
		} catch (MessagingException e) {
			Logger.print(" MailHelper markOrUnMarkmail ", " 标记失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	/**
	 * @param list
	 *            邮件集合
	 * @param status
	 *            0:取消标记 1: 标记
	 * @param type
	 *            收件箱
	 * @return true:成功 false:失败
	 */
	public Boolean markOrUnMarkMailGroup(List<Email> list, int status, int type) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null || list == null) {
				Logger.print(" MailHelper markOrUnMarkMailGroup ", " 标记失败 失败信息是 mailInfo == null");
				return false;
			}
			int loginFrom = mailInfo.getLoginFrom();
			if (loginFrom == Constant.EMAIL_TYPE_WANGYI_qq) {
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				Properties props = System.getProperties();
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.put("mail.imap.host", "imap.qq.com");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imap.ssl.enable", "true");
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Session mailsession = Session.getInstance(props, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
				// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("Sent Messages");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("Deleted Messages");
					break;

				/**
				 * star email
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);

				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMachNew(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}
						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();

							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}

							if (status == 0) {
								msg[0].setFlag(Flags.Flag.FLAGGED, false);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.FLAGGED, true);
							}
						}
						folder.close(true);
						store.close();
						return true;
					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchFlag(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}
						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();

							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}

							if (status == 0) {
								msg[0].setFlag(Flags.Flag.FLAGGED, false);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.FLAGGED, true);
							}
						}
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					int size = list.size();
					for (int i = 0; i < size; i++) {
						int messageNumber = list.get(i).getMessageNumber();
						if (count >= messageNumber && messageNumber > 0) {
							Message message = folder.getMessage(messageNumber);
							if (status == 0) {
								message.setFlag(Flags.Flag.FLAGGED, false);
							} else if (status == 1) {
								message.setFlag(Flags.Flag.FLAGGED, true);
							}
						} else {
							folder.close(true);
							store.close();
							Logger.print(" MailHelper markOrUnMarkMailGroup ", " 跟新失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
							return false;
						}
					}
					folder.close(true);
					store.close();
					return true;
				}

			} else {
				String temp = mailInfo.getMailServerHost();
				String host = temp.replace("smtp", "imap");
				Properties prop = System.getProperties();
				prop.put("mail.imap.host", host);
				prop.put("mail.imap.auth.plain.disable", "true");
				Session mailsession = Session.getInstance(prop, null);
				IMAPStore store = (IMAPStore) mailsession.getStore("imap");
				store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
				IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
				switch (type) {
				/**
				 * 收件箱
				 */
				case 0:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 未读邮件
				 */
				case 1:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				/**
				 * 已发送
				 */
				case 3:
					folder = (IMAPFolder) store.getFolder("已发送");
					break;

				/**
				 * 已删除
				 */
				case 4:
					folder = (IMAPFolder) store.getFolder("已删除");
					break;

				/**
				 * star email
				 */
				case 5:
					folder = (IMAPFolder) store.getFolder("INBOX");
					break;

				default:
					break;
				}
				folder.open(Folder.READ_WRITE);
				if (type == 1 || type == 5) {
					if (type == 1) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMachNew(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}
						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();

							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}

							if (status == 0) {
								msg[0].setFlag(Flags.Flag.FLAGGED, false);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.FLAGGED, true);
							}
						}
						folder.close(true);
						store.close();
						return true;
					} else if (type == 5) {
						Message[] message = folder.getMessages();
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}

						SearchTerm term = new SearchTerm() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean match(Message msg) {
								return isMatchFlag(msg);
							}
						};

						message = folder.search(term, message);
						if (message == null) {
							folder.close(true);
							store.close();
							return false;
						}
						int size = list.size();
						for (int i = 0; i < size; i++) {
							final int messageNumber = list.get(i).getMessageNumber();

							term = new SearchTerm() {

								private static final long serialVersionUID = 1L;

								@Override
								public boolean match(Message msg) {
									return isMatchMessageNumber(msg, messageNumber);
								}
							};

							Message[] msg = folder.search(term, message);
							if (msg == null || msg.length <= 0 || msg[0] == null) {
								folder.close(true);
								store.close();
								return false;
							}

							if (status == 0) {
								msg[0].setFlag(Flags.Flag.FLAGGED, false);
							} else if (status == 1) {
								msg[0].setFlag(Flags.Flag.FLAGGED, true);
							}
						}
						folder.close(true);
						store.close();
						return true;
					}
				} else {
					// 取得所有的邮件
					int count = folder.getMessageCount();
					int size = list.size();
					for (int i = 0; i < size; i++) {
						int messageNumber = list.get(i).getMessageNumber();
						if (count >= messageNumber && messageNumber > 0) {
							Message message = folder.getMessage(messageNumber);
							if (status == 0) {
								message.setFlag(Flags.Flag.FLAGGED, false);
							} else if (status == 1) {
								message.setFlag(Flags.Flag.FLAGGED, true);
							}
						} else {
							folder.close(true);
							store.close();
							Logger.print(" MailHelper markOrUnMarkMailGroup ", " 标记失败 失败信息  = " + "邮件总数 小于 " + messageNumber);
							return false;
						}
					}
					folder.close(true);
					store.close();
					return true;
				}
			}
		} catch (MessagingException e) {
			Logger.print(" MailHelper markOrUnMarkMailGroup ", " 标记失败 失败信息  = " + e.getMessage());
		}
		return false;
	}

	/**
	 * @param messageNumber
	 *            邮件number
	 * @param type
	 *            收件箱类型
	 * @param status
	 *            按收件人 按发件人 按主题 全部
	 * @param content
	 *            搜索内容
	 * @param startPosition
	 *            开始的位置
	 * @return true:删除成功 false:删除失败
	 */
	public Boolean deleteMialInSearchBySSL(final String messageId, final int messageNumber, int type, int status, final String content, int startPosition) {

		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper deleteMialInSearchBySSL ", " 刪除失败 失败信息是 mailInfo == null");
				return false;
			}
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.put("mail.imap.host", "imap.qq.com");
			props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.imap.ssl.enable", "true");
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Session mailsession = Session.getInstance(props, null);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
			// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages,Junk]
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				folder = (IMAPFolder) store.getFolder("Drafts");
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = (IMAPFolder) store.getFolder("Sent Messages");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = (IMAPFolder) store.getFolder("Deleted Messages");
				break;

			/**
			 * star email
			 */
			case 5:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_ONLY);
			Message message[] = folder.getMessages();
			if (message == null) {
				folder.close(true);
				store.close();
				return false;
			}
			SearchTerm term = new SearchTerm() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(Message msg) {
					return isMatchMessageId(msg, messageId);
				}
			};
			message = folder.search(term, message);
			if (message == null || message.length <= 0 || message[0] == null) {
				folder.close(true);
				store.close();
				return false;
			}
			MimeMessage mimeMessage = (MimeMessage) message[0];
			if (type == 2) {
				mimeMessage.setFlag(Flag.DELETED, true);
			} else {
				saveDeleteMessage(mimeMessage, store, mailInfo.getLoginFrom(), type);
			}
			folder.close(true);
			store.close();
			return true;
		} catch (Exception e) {
			Logger.print(" MailHelper deleteMialInSearchBySSL ", " 刪除失败 失败信息是 mailInfo == null");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param messageId
	 * @param messageNumber
	 *            邮件number
	 * @param type
	 *            收件箱类型
	 * @param status
	 *            按收件人 按发件人 按主题 全部
	 * @param content
	 *            搜索内容
	 * @param startPosition
	 *            开始的位置
	 * @return true:删除成功 false:删除失败
	 */
	public Boolean deleteMialInSearchByImap(final String messageId, final int messageNumber, int type, int status, final String content, int startPosition, int loginFrom) {

		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper deleteMialInSearchByImap ", " 刪除失败 失败信息是 mailInfo == null");
				return false;
			}
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Properties prop = System.getProperties();
			prop.put("mail.imap.host", host);
			prop.put("mail.imap.auth.plain.disable", "true");
			Session mailsession = Session.getInstance(prop, null);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				if (loginFrom == Constant.EMAIL_TYPE_SINA) {
					folder = (IMAPFolder) store.getFolder("草稿夹");
				} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
					folder = (IMAPFolder) store.getFolder("草稿箱");
				}
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = (IMAPFolder) store.getFolder("已发送");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = (IMAPFolder) store.getFolder("已删除");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_ONLY);
			Message message[] = folder.getMessages();
			if (message == null) {
				folder.close(true);
				store.close();
				return false;
			}
			SearchTerm term = new SearchTerm() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(Message msg) {
					return isMatchMessageId(msg, messageId);
				}
			};
			message = folder.search(term, message);
			if (message == null || message.length <= 0 || message[0] == null) {
				folder.close(true);
				store.close();
				return false;
			}
			MimeMessage mimeMessage = (MimeMessage) message[0];
			if (type == 2) {
				mimeMessage.setFlag(Flag.DELETED, true);
			} else {
				saveDeleteMessage(mimeMessage, store, mailInfo.getLoginFrom(), type);
			}
			folder.close(true);
			store.close();
			return true;
		} catch (Exception e) {
			Logger.print(" MailHelper deleteMialInSearchBySSL ", " 刪除失败 失败信息是 mailInfo == null");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据邮件id获取邮件详情
	 * 
	 * @param mailId
	 *            邮件id
	 * @param type
	 *            类型
	 * @return 邮件详情
	 */
	public Email searchMailByIdWithSSL(final String mailId, int type) {
		try {
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			props.put("mail.imap.host", "imap.qq.com");
			props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.imap.ssl.enable", "true");
			props.setProperty("mail.debug", "true");
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print("MailHelper searchMailByIdWithSSL ", "邮箱詳情获取失败 mailInfo = null");
				return null;
			}
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Session mailsession = Session.getInstance(props);
			Store store = mailsession.getStore("imap");
			store.connect(host, 993, mailInfo.getUserName(), mailInfo.getPassword());
			// 打开文件夹
			// [INBOX, 其他文件夹, Sent Messages, Drafts, Deleted Messages, Junk]
			Folder folder = null;
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				folder = store.getFolder("Drafts");
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = store.getFolder("Sent Messages");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = store.getFolder("Deleted Messages");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_ONLY);

			Message[] message = folder.getMessages();
			if (message == null) {
				folder.close(true);
				store.close();
				return null;
			}

			SearchTerm term = new SearchTerm() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(Message msg) {
					return isMatchMessageId(msg, mailId);
				}
			};
			message = folder.search(term, message);
			if (message == null || message.length <= 0 || message[0] == null) {
				folder.close(true);
				store.close();
				return null;
			}
			MailReceiver reciveMail = new MailReceiver((MimeMessage) message[0], message[0], true);
			Email email = mailReceToEmail(reciveMail);
			folder.close(true);
			store.close();
			return email;
		} catch (Exception e) {
			Logger.print("MailHelper searchMailByIdWithSSL ", e.getMessage());
		}
		return null;
	}

	/**
	 * 根据邮件id获取详情
	 * 
	 * @param mailId
	 *            邮件id
	 * @param email_type
	 *            邮件类型
	 * @return
	 */
	public Email searchMailById(final String mailId, int type) {
		try {
			MailInfo mailInfo = SharedPreferencedUtis.getMailInfo(BaseApplication.getInstance());
			if (mailInfo == null) {
				Logger.print(" MailHelper searchMailById ", " 邮件详情获取失败 失败信息是 mailInfo == null");
				return null;
			}
			int loginFrom = mailInfo.getLoginFrom();
			String temp = mailInfo.getMailServerHost();
			String host = temp.replace("smtp", "imap");
			Properties prop = System.getProperties();
			prop.put("mail.imap.host", host);
			prop.put("mail.imap.auth.plain.disable", "true");
			Session mailsession = Session.getInstance(prop, null);
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(host, mailInfo.getUserName(), mailInfo.getPassword());
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			switch (type) {
			/**
			 * 收件箱
			 */
			case 0:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 未读邮件
			 */
			case 1:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			/**
			 * 草稿箱
			 */
			case 2:
				if (loginFrom == Constant.EMAIL_TYPE_SINA) {
					folder = (IMAPFolder) store.getFolder("草稿夹");
				} else if (loginFrom == Constant.EMAIL_TYPE_WANGYI_126 || loginFrom == Constant.EMAIL_TYPE_WANGYI_163) {
					folder = (IMAPFolder) store.getFolder("草稿箱");
				}
				break;

			/**
			 * 已发送
			 */
			case 3:
				folder = (IMAPFolder) store.getFolder("已发送");
				break;

			/**
			 * 已删除
			 */
			case 4:
				folder = (IMAPFolder) store.getFolder("已删除");
				break;

			/**
			 * 星标邮件
			 */
			case 5:
				folder = (IMAPFolder) store.getFolder("INBOX");
				break;

			default:
				break;
			}
			folder.open(Folder.READ_ONLY);
			Message message[] = folder.getMessages();
			if (message == null) {
				folder.close(true);
				store.close();
				return null;
			}
			SearchTerm term = new SearchTerm() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(Message msg) {
					return isMatchMessageId(msg, mailId);
				}
			};
			message = folder.search(term, message);
			if (message == null || message.length <= 0 || message[0] == null) {
				folder.close(true);
				store.close();
				return null;
			}
			MailReceiver reciveMail = new MailReceiver((MimeMessage) message[0], message[0], true);
			Email email = mailReceToEmail(reciveMail);
			folder.close(true);
			store.close();
			return email;
		} catch (Exception e) {
			Logger.print(" MailHelper searchMailById ", " 邮件详情获取失败 失败信息是 mailInfo == null");
			e.printStackTrace();
		}
		return null;
	}
}
