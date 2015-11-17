package com.ez.framework.core.dam.mybatis;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;

final class SQLMapperXMLGetter {

	/** <code>S_DEFAULT_ENTITIES_FOLDER_NAME</code> - entity. */
	private static final String S_DEFAULT_ENTITIES_FOLDER_NAME = "entities";

	/**
	 * URL encoding, equals as System.getProperty("file.encoding")??
	 * 
	 * Copied from java.net.URLEncoder.java
	 */
	private static final String S_URL_ENCODING = System.getProperty("file.encoding", "UTF-8");

	/**
	 * 鏋勯�鍑芥暟.
	 */
	private SQLMapperXMLGetter() {
	}

	/**
	 * get ClassLoader.
	 * 
	 * @return class loader.
	 */
	protected static ClassLoader getClassLoader() {
		ClassLoader t_classLoader = null;
		ClassLoader t_cl1 = Thread.currentThread().getContextClassLoader();
		ClassLoader t_cl2 = SQLMapperXMLGetter.class.getClassLoader();
		if (!t_cl1.equals(t_cl2)) {
			t_classLoader = t_cl1;
		} else {
			t_classLoader = t_cl2;
		}
		return t_classLoader;
	}

	/**
	 * build SQLMapper.
	 * 
	 * @param dbType
	 *            - database type.
	 * @param configuration
	 *            - ibatis configuration instance.
	 * @throws IOException
	 */
	protected static void buildMapper(final String dbType, final Configuration configuration) throws IOException {

		Enumeration<URL> t_enumeration = Thread.currentThread().getContextClassLoader()
				.getResources(S_DEFAULT_ENTITIES_FOLDER_NAME);
		while (t_enumeration.hasMoreElements()) {
			URL t_url = t_enumeration.nextElement();
			// URL decoding, especially for %20. This is required.
			String t_path = URLDecoder.decode(t_url.getFile(), S_URL_ENCODING);
			File t_file = new File(t_path);

			if ("file".equals(t_url.getProtocol())) {
				// common file
				buildMapper4Dir(t_file.getParentFile(), dbType, configuration);
			} else if ("jar".equals(t_url.getProtocol())) {
				// in jar file
				String t_filePath = t_file.getPath();
				int t_index = t_filePath.indexOf(".jar!");
				if (t_index <= 0) {
					continue;
				}
				t_filePath = t_filePath.substring(0, t_index + ".jar".length());
				t_file = new File(new URL(t_filePath).getFile());
				buildMapper4Jar(t_file, dbType, configuration);
			}
		}
	}

	/**
	 * build SQLMapper from file director.
	 * 
	 * @param filePara
	 *            - mappers folder.
	 * @param dbType
	 *            - database type.
	 * @param configuration
	 *            - ibatis config.
	 * @throws IOException
	 *             when load files error.
	 */
	private static void buildMapper4Dir(final File filePara, final String dbType, final Configuration configuration)
			throws IOException {
		final Map<String, File> t_mappers = new LinkedHashMap<String, File>();

		filePara.listFiles(new FileFilter() {
			public boolean accept(final File pathname) {
				if (pathname.isDirectory() && pathname.getName().equals(S_DEFAULT_ENTITIES_FOLDER_NAME)) {
					pathname.listFiles(new FilenameFilter() {
						public boolean accept(final File dir, final String name) {
							File t_subFile = new File(dir, name);
							if (t_subFile.isFile() && name.endsWith(".xml")) {
								t_mappers.put(S_DEFAULT_ENTITIES_FOLDER_NAME + File.separatorChar + name, filePara);

								return true;
							} else if (t_subFile.isDirectory() && (name.equalsIgnoreCase(dbType))) {
								t_subFile.listFiles(new FilenameFilter() {
									public boolean accept(final File subDir, final String subName) {
										File t_subSubFile = new File(subDir, subName);
										if (t_subSubFile.isFile() && subName.endsWith(".xml")) {
											t_mappers.put(S_DEFAULT_ENTITIES_FOLDER_NAME + File.separatorChar + name
													+ File.separatorChar + subName, filePara);
											return true;
										} else {
											return false;
										}
									}
								});
								return true;
							} else {
								return false;
							}
						}
					});
					return true;
				} else {
					return false;
				}
			}
		});

		for (Iterator<Map.Entry<String, File>> t_itr = t_mappers.entrySet().iterator(); t_itr.hasNext();) {
			Map.Entry<String, File> t_ent = t_itr.next();
			ErrorContext.instance().resource(t_ent.getKey());
			InputStream t_inputStream = Resources.getResourceAsStream(t_ent.getKey());
			try {

				XMLMapperBuilder t_mapperParser = new XMLMapperBuilder(t_inputStream, configuration, t_ent.getKey(),
						configuration.getSqlFragments());
				t_mapperParser.parse();
			} catch (Throwable t_e) {
				throw new RuntimeException(ErrorContext.instance().toString(), t_e);
			} finally {
				try {
					t_inputStream.close();
				} catch (IOException t_e) {
					t_inputStream = null;
				}
			}
		}

	}

	/**
	 * build SQLMapper from JAR file.
	 * 
	 * @param filePara
	 * @param dbType
	 * @param configuration
	 * @throws IOException
	 */
	private static void buildMapper4Jar(File filePara, final String dbType, final Configuration configuration)
			throws IOException {
		Map<String, File> t_mappers = new LinkedHashMap<String, File>();
		ZipInputStream t_jarFileStream = null;
		try {
			t_jarFileStream = new ZipInputStream(new FileInputStream(filePara));

			ZipEntry t_zipEntry = null;
			while ((t_zipEntry = t_jarFileStream.getNextEntry()) != null) {
				if (!t_zipEntry.isDirectory() && t_zipEntry.getName().startsWith(S_DEFAULT_ENTITIES_FOLDER_NAME + "/")) {
					// && t_zipEntry.getName().endsWith(".xml")) {
					// t_mappers.put(t_zipEntry.getName(), filePara);
					String t_entryName = t_zipEntry.getName();
					// entity/ 鐩存帴鐩綍
					if (t_entryName.endsWith(".xml")
							&& t_entryName.indexOf("/", S_DEFAULT_ENTITIES_FOLDER_NAME.length() + 1) < 0) {
						t_mappers.put(t_entryName, filePara);
						continue;
					} else
					// entity/[dbType]/ 鐩存帴鐩綍
					if (dbType != null
							&& t_entryName.startsWith(S_DEFAULT_ENTITIES_FOLDER_NAME + "/" + dbType + "/")
							&& t_entryName.endsWith(".xml")
							&& t_entryName.indexOf("/", S_DEFAULT_ENTITIES_FOLDER_NAME.length() + 2 + dbType.length()) < 0) {
						t_mappers.put(t_entryName, filePara);
						continue;
					}
				}
			}
		} catch (FileNotFoundException e) {
			// 杩欎釜涓嶅彲鑳�
			e.printStackTrace();
		} finally {
			if (t_jarFileStream != null) {
				t_jarFileStream.close();
			}
		}

		for (Iterator<Map.Entry<String, File>> t_itr = t_mappers.entrySet().iterator(); t_itr.hasNext();) {
			Map.Entry<String, File> t_ent = t_itr.next();
			ErrorContext.instance().resource(t_ent.getKey());
			InputStream inputStream = Resources.getResourceAsStream(t_ent.getKey());

			try {
				XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, t_ent.getKey(),
						configuration.getSqlFragments());
				mapperParser.parse();
			} catch (Throwable t_e) {
				throw new RuntimeException(ErrorContext.instance().toString(), t_e);
			} finally {
				try {
					inputStream.close();
				} catch (Exception e) {
					inputStream = null;
				}
			}
		}
	}

}
