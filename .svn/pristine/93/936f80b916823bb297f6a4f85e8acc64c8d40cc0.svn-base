/**
 * @date2016-10-18
   @userAdministrator
   
 */
package com.zfsoft.zf_new_email.entity;

import java.io.File;
import java.io.Serializable;

/**
 * @author wesley
 * @date 2016-10-18上午9:57:23
 * @Description: 附件实体类
 */
public class Attachment implements Serializable {

	private static final long serialVersionUID = 1L;
	private String filePath;// 文件路径
	private String fileName;// 文件名
	private String fileSize;// 文件大小
	private long fileLength; // 文件大小---字节
	private String attachmentId; //附件id
	private String downLoadUrl; //下载地址

	public Attachment() {
	}

	public Attachment(String filePath, String fileName, String fileSize) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}

	/**
	 * 文件大小转换
	 * 
	 * @param size
	 *            大小
	 * @return 具体大小
	 */
	public static String convertStorage(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", size);
		}
	}
	
	
	public static boolean checkAttachisTooBig(long size){
		if(size >= 10 * 1024 * 1024){
			return true;
		}
		return false;
	}

	/**
	 * 获取完整的目录名字
	 * 
	 * @param filepath
	 *            文件路径
	 * @return 文件名
	 */
	private static String getNameFromFilepath(String filepath) {
		int pos = filepath.lastIndexOf('/');
		if (pos != -1) {
			return filepath.substring(pos + 1);
		}
		return "";
	}

	/**
	 * 获取附件属性
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件对象
	 */
	public static Attachment GetFileInfo(String filePath) {

		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
		Attachment fileInfo = new Attachment();
		fileInfo.setFileName(getNameFromFilepath(filePath));
		fileInfo.setFilePath(filePath);
		fileInfo.setFileSize(convertStorage(file.length()));
		fileInfo.setFileLength(file.length());
		return fileInfo;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
}
