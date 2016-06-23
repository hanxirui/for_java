package com.chinal.emp.report.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 解压缩工具类 <br>
 * <p>
 * Create on : 2012-7-12<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liuyang<br>
 * @version riil.webcommons v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class ZipFileUtil {

	/**
	 * Constructors.
	 */
	private ZipFileUtil() {

	}

	/**
	 * 解压缩ZIP文件.
	 * 
	 * @param targetPath
	 *            目标路径
	 * @param in
	 *            文件输入流
	 * @throws IOException
	 *             抛出异常
	 */
	public static void unzipFile(final String targetPath, final InputStream in) throws IOException {
		ZipInputStream t_zis = new ZipInputStream(in);
		ZipEntry t_entry = null;
		while ((t_entry = t_zis.getNextEntry()) != null) {
			String t_zipPath = t_entry.getName();

			if (t_entry.isDirectory()) {
				File t_zipFolder = new File(targetPath + File.separator + t_zipPath);
				if (!t_zipFolder.exists()) {
					if (!t_zipFolder.mkdirs()) {
						throw new IOException("make dirs error.");
					}
				}
			} else {
				File t_file = new File(targetPath + File.separator + t_zipPath);
				if (!t_file.exists()) {
					File t_pathDir = t_file.getParentFile();
					if (t_pathDir.mkdirs()) {
						if (!t_file.createNewFile()) {
							throw new IOException("create file error.");
						}
					}
				}

				FileOutputStream t_fos = new FileOutputStream(t_file);

				IOUtils.copy(t_zis, t_fos);
				t_fos.close();

			}
		}
		t_zis.close();
		in.close();

	}

	/**
	 * 压缩文件.
	 * 
	 * @param targetDir
	 *            待加压的目录
	 * @param outPath
	 *            输入zip文件路径
	 * @param isJar
	 *            是否为jar文件（true:表示为jar文件，false:表示为zip文件）
	 * @return 压缩后的文件
	 * @throws IOException
	 *             抛出异常
	 */
	public static File zipFile(final String targetDir, final String outPath, final boolean isJar) throws IOException {
		File t_zipFile = new File(targetDir);
		String t_fileExt = isJar ? ".jar" : ".zip";
		String t_zipFileName = t_zipFile.getName() + t_fileExt;
		File t_returnFile = new File(outPath, t_zipFileName);
		BufferedOutputStream t_bs = new BufferedOutputStream(new FileOutputStream(t_returnFile));
		ZipOutputStream t_zipOut = new ZipOutputStream(t_bs);
		zip(t_zipFile, t_zipOut);
		t_zipOut.close();
		return t_returnFile;
	}

	/**
	 * 压缩方法.
	 * 
	 * @param zipDir
	 *            待压缩的文件
	 * @param zipOut
	 *            输出的流
	 * @throws IOException
	 *             抛出异常
	 */
	private static void zip(final File zipDir, final ZipOutputStream zipOut) throws IOException {
		String t_dirEndPoint = "/";
		String t_root = zipDir.getPath();
		t_root = t_root.replace("\\", t_dirEndPoint) + t_dirEndPoint;
		Collection<File> t_files = FileUtils.listFiles(zipDir, null, true);
		FileInputStream t_fin;
		if (t_files.size() == 0) {
			zipOut.putNextEntry(new ZipEntry(t_dirEndPoint));
			zipOut.close();
		} else {
			for (File t_file : t_files) {
				t_fin = new FileInputStream(t_file);
				String t_tmp = t_file.getPath().replace("\\", t_dirEndPoint).replace(t_root, "");
				zipOut.putNextEntry(new ZipEntry(t_tmp));
				IOUtils.copy(t_fin, zipOut);
				t_fin.close();

			}
		}

	}

}
