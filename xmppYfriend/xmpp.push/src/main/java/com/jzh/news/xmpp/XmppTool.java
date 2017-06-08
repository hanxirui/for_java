package com.jzh.news.xmpp;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import com.google.gson.Gson;
import com.jzh.news.entity.User;

/**
 * <b>function:</b> ����Smack������ XMPP Э��ͨ��
 * 
 * @author hoojo
 * @createDate 2012-5-22 ����10:28:18
 * @file ConnectionServerTest.java
 * @package com.hoo.smack.conn
 * @project jwchat
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class XmppTool {

	private static XMPPConnection connection;
	private static ConnectionConfiguration config;

	 public static String server = "123.207.145.194";
	//public static String server = "127.0.0.1";

	public static boolean create(User user) {
		if (connection == null) {
			init();
		}

		// String strs = user.getUser() + ";" + user.getNickname() + ";"
		// + user.getIcon() + ";" + user.getSex();
		String pswd = user.getPassword();
		user.setPassword(null);
		String strs = new Gson().toJson(user);
		System.out.println(strs);
		AccountManager accountManager = AccountManager.getInstance(connection);
		try {
			/**
			 * ����һ���û�boy������Ϊboy��������ڹ���Ա����̨ҳ��http://192.168.8.32:9090/user-
			 * summary.jsp�鿴�û�/��������Ϣ�����鿴�Ƿ�ɹ������û�
			 */
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", strs);
			map.put("email", "admin@jzh.com");
			 Localpart localpart = Localpart.from(user.getUser());
			accountManager.createAccount(localpart, pswd);
			System.out.println(user.getUser() + "\t" + pswd + "��xmppע��ɹ�");
			return true;
			/** �޸����� */
			// accountManager.changePassword("abc");
		} catch (XMPPException e) {

			e.printStackTrace();
			return true;
		} catch (XmppStringprepException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��¼
	 * 
	 * @param user
	 * @param password
	 */
	public static void login(String user, String password) {

		if (connection == null) {
			init();
		}
		try {
			/** �û���½���û��������� */
			connection.login(user, password);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		/** ��ȡ��ǰ��½�û� */
		fail("User:", connection.getUser());
		addGroup(connection.getRoster(), "�ҵĺ���");
		addGroup(connection.getRoster(), "������");
		System.out.println("OK");

	}

	/** * ���һ���� */
	public static boolean addGroup(Roster roster, String groupName) {
		try {
			roster.createGroup(groupName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// -----------���²��ÿ�-----------------------------------------------------

	private final static void fail(Object o) {
		if (o != null) {
			System.out.println(o);
		}
	}

	private final static void fail(Object o, Object... args) {
		if (o != null && args != null && args.length > 0) {
			String s = o.toString();
			for (int i = 0; i < args.length; i++) {
				String item = args[i] == null ? "" : args[i].toString();
				if (s.contains("{" + i + "}")) {
					s = s.replace("{" + i + "}", item);
				} else {
					s += " " + item;
				}
			}
			System.out.println(s);
		}
	}

	/**
	 * <b>function:</b> ��ʼSmack��openfire���������ӵĻ�������
	 * 
	 * @author hoojo
	 * @createDate 2012-6-25 ����04:06:42
	 */

	public static void init() {
		try {
			  DomainBareJid domainBareJid = JidCreate.domainBareFrom("deta") ;
			    XMPPTCPConnectionConfiguration.Builder builder=
			            XMPPTCPConnectionConfiguration.builder();
			    builder.setServiceName(domainBareJid);
			    builder.setHostAddress(InetAddress.getByName("deta"));
			    builder.setUsernameAndPassword("", "");
			     builder.setResource("resource");
			    builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

			    mConnection = new XMPPTCPConnection(builder.build());
			    mConnection.addConnectionListener(this);
			    mConnection.connect();
			    mConnection.login();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		// fail(connection);
		// fail(connection.getConnectionID());
	}

}