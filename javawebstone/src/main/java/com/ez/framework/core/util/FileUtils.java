package com.ez.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 文件操作工具类 <br>
 * <p>
 * Create on : 2011-11-15<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liuyang<br>
 * @version riil.cmdb.impl v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class FileUtils {

	/**
	 * 构造函数.
	 */
	private FileUtils() {

	}

	/**
	 * 创建文件.
	 * 
	 * @param filePath
	 *            文件路径
	 * @param inputStream
	 *            InputStream
	 * @throws IOException
	 *             IO异常
	 */
	public static void createFile(final String filePath, final InputStream inputStream) throws IOException {
		File t_file = new File(filePath);
		if (!t_file.exists()) {
			File t_dir = t_file.getParentFile();
			if (!t_dir.exists()) {
				if (t_dir.mkdirs()) {
					if (!t_file.createNewFile()) {
						throw new IOException("create file failt");
					}
				}
			}
		}
		FileOutputStream t_fos = null;
		try {
			t_fos = new FileOutputStream(t_file);
			IOUtils.copy(inputStream, t_fos);
		} catch (FileNotFoundException t_e) {
			throw t_e;
		} finally {
			if (t_fos != null) {
				t_fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

	}

	/**
	 * 读文件
	 * 
	 * @param filePath
	 *            文件全路径
	 * @return InputStream InputStream
	 * @throws IOException
	 *             IO异常
	 */
	public static InputStream readFileToInputStream(final String filePath) throws IOException {
		FileInputStream t_fis = null;
		try {
			t_fis = new FileInputStream(filePath);
		} catch (FileNotFoundException t_e) {
			throw t_e;
		}
		return t_fis;
	}

	/**
	 * 删除文件.
	 * 
	 * @param filePath
	 *            文件路径
	 * @return true/false
	 */
	public static boolean deleteFile(final String filePath) {
		File t_file = new File(filePath);
		return deleteFile(t_file);
	}

	/**
	 * 删除文件.
	 * 
	 * @param file
	 *            文件路径
	 * @return true/false
	 */
	public static boolean deleteFile(final File file) {
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 清除目录.
	 * 
	 * @param repositoryFolder
	 *            目录
	 * @throws IOException
	 *             IO异常
	 */
	public static void cleanFolder(final File repositoryFolder) throws IOException {
		org.apache.commons.io.FileUtils.deleteDirectory(repositoryFolder);

	}

}
