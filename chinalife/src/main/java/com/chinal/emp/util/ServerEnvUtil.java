package com.chinal.emp.util;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;

public class ServerEnvUtil {

	public static final String DEFAULT_SERVER_CONFIG = "server.xml";

	public static String RIIL_SERVER_TYPE;

	public static String RIIL_VERSION;

	public static String RIIL_DEPLOY_TYPE = "Enterprise";

	public static String RIIL_WORK_SPACE_FOLDER;

	public static String RIIL_FOLDER;

	public static String RIIL_LOG_PATH;

	public static String RIIL_SERVER_ID;

	public static String RIIL_SERVER_IP;

	public static String RIIL_SERVER_PROTOCOL;

	public static String RIIL_SERVER_PORT;

	public static String RIIL_SERVER_PUSH_PORT;

	public static String RIIL_SERVER_PUSH_IP;

	// 是否本地保存原始数据,true/false
	public static boolean RIIL_STORAGE_LOCAL_SAVE;
	// 原始数据本地保存的路径
	public static String RIIL_STORAGE_LOCAL_PATH;
	// 是否上传原始数据,true/false
	public static boolean RIIL_STORAGE_REMOTE_SAVE;
	// 原始数据远程保存的路径
	public static String RIIL_STORAGE_REMOTE_PATH;

	public static String RIIL_MANAGER_IP;

	public static String RIIL_MANAGER_SERVER_TYPE;

	public static String RIIL_CLUSTER_PORT;

	public static String RCS_IP;

	public static String RCS_PORT;

	public static Boolean TEST_MODEL = Boolean.FALSE;

	public static String PLUGINS_PATH;

	public static boolean IPV6_SUPPORT = Boolean.FALSE;

	public static String RIIL_PROXY_IP;

	public static String RIIL_PROXY_PORT;

	public static int RIIL_TASK_POOL_SIZE = 10;

	public static int RIIL_TASK_POOL_CORE_SIZE = 20;

	public static int RIIL_TASK_POOL_MAX_SIZE = 30;
	// rest server
	public static String REST_SERVER_ID;
	public static String REST_SERVER_NAME;
	public static String REST_SERVER_IP;
	public static String REST_SERVER_PROTOCOL;
	public static int REST_SERVER_PORT;

	public static boolean BACKUP_ENABLE = false;

	public static int BACKUP_DURATION;

	public static ServerEnvUtil serverEnvUtil = new ServerEnvUtil();

	static {
		serverEnvUtil.init();
	}

	private ServerEnvUtil() {

	}

	public static final ServerEnvUtil getInstance() {
		return serverEnvUtil;
	}

	public void init(String configFile) {

		Document doc = XmlUtils.loadDocByFilename(ServerEnvUtil.class.getClassLoader().getResourceAsStream(configFile));

		if (doc != null) {

			Element root = doc.getRootElement();

			RIIL_WORK_SPACE_FOLDER = root.elementText("workSpace") == null ? System.getProperty("user.dir")
					: root.elementTextTrim("workSpace") + File.separator;

			RIIL_FOLDER = RIIL_WORK_SPACE_FOLDER + ".." + File.separator + ".." + File.separator;

			RIIL_LOG_PATH = RIIL_WORK_SPACE_FOLDER + "logs" + File.separator;

			// RIIL_CLUSTER_PORT =
			// root.element("cluster").attributeValue("port");

			if (root.element("localServer") != null) {

				Element tmp = root.element("localServer");

				RIIL_SERVER_ID = tmp.attributeValue("id");
				RIIL_SERVER_IP = tmp.attributeValue("ip");
				RIIL_SERVER_PORT = tmp.attributeValue("port");
				RIIL_SERVER_PROTOCOL = tmp.attributeValue("protocol");
				RIIL_SERVER_TYPE = tmp.attributeValue("serverType");
			}

			RIIL_STORAGE_LOCAL_SAVE = false;
			if (root.element("storageLocal") != null) {
				Element tmp = root.element("storageLocal");
				String tmpVal = tmp.attributeValue("save");
				if ("true".equalsIgnoreCase(tmpVal)) {
					RIIL_STORAGE_LOCAL_SAVE = true;
				}
				RIIL_STORAGE_LOCAL_PATH = tmp.attributeValue("path");
			}

			RIIL_STORAGE_REMOTE_SAVE = false;
			if (root.element("storageRemote") != null) {
				Element tmp = root.element("storageRemote");
				String tmpVal = tmp.attributeValue("save");
				if ("true".equalsIgnoreCase(tmpVal)) {
					RIIL_STORAGE_REMOTE_SAVE = true;
				}
				RIIL_STORAGE_REMOTE_PATH = tmp.attributeValue("path");
			}

			if (root.element("manager") != null) {
				Element tmp = root.element("manager");
				RIIL_MANAGER_IP = tmp.attributeValue("ip");
				RIIL_MANAGER_SERVER_TYPE = tmp.attributeValue("serverType");
			}

			if (root.element("serverPush") != null) {
				Element _elem = root.element("serverPush");
				RIIL_SERVER_PUSH_IP = _elem.attributeValue("ip");
				RIIL_SERVER_PUSH_PORT = _elem.attributeValue("port");
			}

			if (root.element("proxy") != null) {
				Element tmp = root.element("proxy");
				RIIL_PROXY_IP = tmp.attributeValue("ip");
				RIIL_PROXY_PORT = tmp.attributeValue("port");
			}

			if (root.element("rcs") != null) {
				Element _elem = root.element("rcs");
				RCS_IP = _elem.attributeValue("ip");
				RCS_PORT = _elem.attributeValue("port");
			}

			RIIL_VERSION = root.elementText("serverVersion");

			if (root.element("pluginsPath") != null) {
				PLUGINS_PATH = root.element("pluginsPath").getText();
			}

			if (root.element("devModel") != null) {
				TEST_MODEL = Boolean.parseBoolean(root.elementText("devModel"));
			}
			// rest Server
			if (root.element("restServer") != null) {

				Element tmp = root.element("restServer");

				REST_SERVER_ID = tmp.attributeValue("id");
				REST_SERVER_NAME = tmp.attributeValue("name");
				REST_SERVER_IP = tmp.attributeValue("ip");
				REST_SERVER_PROTOCOL = tmp.attributeValue("protocol");
				REST_SERVER_PORT = Integer.valueOf(tmp.attributeValue("port"));
			}

			// ssl连接
			if (RIIL_SERVER_PROTOCOL.equals("https")) {
				if (root.element("sslConnecter") != null) {
					Element sslConnecterEle = root.element("sslConnecter");
					SslConnectConfig.KEY_STORE_PATH = sslConnecterEle.elementTextTrim("keyStorePath");
					SslConnectConfig.KEY_STORE_PASSWORD = sslConnecterEle.elementTextTrim("keyStorePassword");
					SslConnectConfig.KEY_MANAGER_PASSWORD = sslConnecterEle.elementTextTrim("keyManagerPassword");
				} else {
					throw new IllegalArgumentException("Ssl connecter musted be configured!");
				}
			}

		} else {
			System.err.println("Server.xml not found, server maybe startup with errors.");
		}

	}

	public static void isBackupAble(boolean isable) {
		BACKUP_ENABLE = isable;
	}

	public static void setBackupDuation(int duation) {
		BACKUP_DURATION = duation;
	}

	public void init() {
		init(DEFAULT_SERVER_CONFIG);
	}

	/** Resource model related properties **/
	private static final String CUSTOM_FOLDER = "custom";

	private static final String SYSTEM_FOLDER = "system";

	private static final String RES_MODEL = "resmodel";

	private static final String POLICY = "policy";

	private static final String TEMPLATE = "template";

	private static final String SCRIPT = "script";

	private static final String MODEL = "model";

	public static final String RIIL_RESMODEL_FOLDER = RIIL_WORK_SPACE_FOLDER + RES_MODEL + File.separator;

	public static final String RIIL_CUSTOM_FOLDER = RIIL_RESMODEL_FOLDER + CUSTOM_FOLDER + File.separator;

	public static final String RIIL_SYSTEM_FOLDER = RIIL_RESMODEL_FOLDER + SYSTEM_FOLDER + File.separator;

	public static final String RIIL_POLICY_FOLDER = RIIL_CUSTOM_FOLDER + POLICY + File.separator;

	public static final String RIIL_SCRIPT_FOLDER = RIIL_CUSTOM_FOLDER + SCRIPT + File.separator;

	public static final String RIIL_TEMPLATE_FOLDER = RIIL_CUSTOM_FOLDER + TEMPLATE + File.separator;

	public static final String RIIL_MODEL_FOLDER = RIIL_CUSTOM_FOLDER + MODEL + File.separator;

	public static final String RIIL_SYSTEM_POLICY_FOLDER = RIIL_SYSTEM_FOLDER + POLICY + File.separator;

	public static final String RIIL_SYSTEM_SCRIPT_FOLDER = RIIL_SYSTEM_FOLDER + SCRIPT + File.separator;

	public static final String RIIL_SYSTEM_TEMPLATE_FOLDER = RIIL_SYSTEM_FOLDER + TEMPLATE + File.separator;

	public static final String RIIL_SYSTEM_MODEL_FOLDER = RIIL_SYSTEM_FOLDER + MODEL + File.separator;

	public static class SslConnectConfig {
		public static String KEY_STORE_PATH = null;

		public static String KEY_STORE_PASSWORD = null;

		public static String KEY_MANAGER_PASSWORD = null;
	}
}
