package com.zfsoft.zf_new_email.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.zfsoft.core.utils.Logger;

/**
 * Created by wesley on 2016/10/12. 工具类
 */
public class IOUtil {

	private final static String TAG = "IOUtil";

	/**
	 * 输入流保存到文件
	 * 
	 * @param source
	 *            输入流来源
	 * @param targetPath
	 *            目标文件路径
	 * @return 文件路径
	 */
	public static String stream2file(InputStream source, String targetPath) {
		File target = new File(targetPath);
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			if (!target.exists()) {
				String dir = targetPath.substring(0, targetPath.lastIndexOf("/"));
				new File(dir).mkdirs();
				try {
					target.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			inBuff = new BufferedInputStream(source);
			outBuff = new BufferedOutputStream(new FileOutputStream(target));
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inBuff != null) {
					inBuff.close();
				}
				if (outBuff != null) {
					outBuff.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (target.length() > 0) {
			return target.getAbsolutePath();
		} else {
			target.delete();
			return null;
		}
	}

	/**
	 * 字节数组转输入流
	 * 
	 * @param data
	 *            字节数组
	 * @return 输入流
	 */
	public InputStream Byte2InputStream(byte[] data) {
		return new ByteArrayInputStream(data);
	}

	/**
	 * 输入流转字节数组
	 * 
	 * @param is
	 *            输入流
	 * @return 字节数组
	 */
	public byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		try {
			while (is.read(readByte, 0, 1024) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getFileBytes(File file) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int bytes = (int) file.length();
			byte[] buffer = new byte[bytes];
			int readBytes = bis.read(buffer);
			if (readBytes != buffer.length) {
				throw new IOException("Entire file not read");
			}
			return buffer;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * 将输入流写入文件
	 * 
	 * @param path
	 *            路徑
	 * @param is
	 *            输入流
	 * @param append
	 *            是否追加在文件末
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
	 */
	public static boolean writeFileFromIS(String path, InputStream is, boolean append) {
		File file = new File(path);
		if (is == null) {
			return false;
		}
		if (!createOrExistsFile(file)) {
			return false;
		}
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file, append));
			byte data[] = new byte[1024];
			int len;
			while ((len = is.read(data, 0, 1024)) != -1) {
				os.write(data, 0, len);
			}
			is.close();
			os.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Logger.print(TAG, " writeFileFromIS 流写入失败 失败信息 " + e.getMessage());
			return false;
		} finally {
			try {
				is.close();
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Logger.print(TAG, " writeFileFromIS 流写入失败 失败信息 " + e.getMessage());
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return true:成功 false:失败
	 */
	public static boolean createFile(String path) {
		File file = new File(path);
		if (!createOrExistsFile(file)) {
			return false;
		}

		return true;
	}

	/**
	 * 判断文件是否存在，不存在则判断是否创建成功
	 * 
	 * @param file
	 *            文件
	 * @return {@code true}: 存在或创建成功<br>
	 *         {@code false}: 不存在或创建失败
	 */
	private static boolean createOrExistsFile(File file) {
		if (file == null) {
			return false;
		}
		// 如果存在，是文件则返回true，是目录则返回false
		if (file.exists()) {
			return file.isFile();
		}
		if (!createOrExistsDir(file.getParentFile())) {
			return false;
		}
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.print(TAG, " createOrExistsFile 文件判读失败 失败信息 " + e.getMessage());
			return false;
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return {@code true}: 存在<br>
	 *         {@code false}: 不存在
	 */
	public static boolean isFileExists(String filePath, String name) {
		return isFileExists(getFileByPath(filePath, name));
	}

	/**
	 * 根据文件路径获取文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件
	 */
	public static File getFileByPath(String filePath, String name) {
		return isSpace(filePath) ? null : new File(filePath, name);
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 *            文件
	 * @return {@code true}: 存在<br>
	 *         {@code false}: 不存在
	 */
	public static boolean isFileExists(File file) {
		return file != null && file.exists();
	}

	// 是否为空是否全部是空格
	private static boolean isSpace(String filePath) {
		return (filePath == null || filePath.trim().length() == 0);
	}

	/**
	 * 判断目录是否存在，不存在则判断是否创建成功
	 * 
	 * @param file
	 *            文件
	 * @return {@code true}: 存在或创建成功<br>
	 *         {@code false}: 不存在或创建失败
	 */
	private static boolean createOrExistsDir(File file) {
		// 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	/**
	 * 将一个输入流转化为字符串
	 */
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				StringBuilder tStringBuffer = new StringBuilder();
				String sTempOneLine;
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将一个字符串转化为输入流
	 */
	public static InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				return new ByteArrayInputStream(sInputString.getBytes());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 判断文件的大小是否大于1m
	 * 
	 * @param size
	 *            文件大小
	 * @return true:大于1m false:小于1m
	 */
	public static boolean checkFileSize(String size) {

		if (size != null && !"".equals(size.trim()) && Double.valueOf(size.replaceAll("M", "")) >= 1.0) {
			return true;
		}

		return false;
	}

}
