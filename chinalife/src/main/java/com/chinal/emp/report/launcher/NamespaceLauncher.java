package com.chinal.emp.report.launcher;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinal.emp.report.helper.PortalHelper;
import com.chinal.emp.websocket.WebSocketManager;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

/**
 * {class description} <br>
 * <p>
 * Create on : 2016年5月25日<br>
 * </p>
 * <br>
 * 
 * @author chengxuetao@ruijie.com.cn<br>
 * @version riil-education-action v6.7.8 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
@Component
public class NamespaceLauncher {

	/**
	 * 用户体验分析获取数据websocket命名空间.
	 */
	private static final String S_NAMESPACE = "/cl";

	/**
	 * <code>m_webSocketMgr</code> - webSocketMgr.
	 */
	@Autowired
	private WebSocketManager m_webSocketMgr;

	/**
	 * 注册命名空间.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void init() {
		SocketIOServer webSocketServer = m_webSocketMgr.getWebSocketServer();
		final SocketIONamespace namespace = webSocketServer.addNamespace(S_NAMESPACE);
		// 添加监听，客户端发送指令获取portlet数据
		namespace.addEventListener("load", HashMap.class, new DataListener<HashMap>() {
			@Override
			public void onData(final SocketIOClient client, final HashMap data, final AckRequest ackRequest) {

				System.out.println(S_NAMESPACE + " - GetContent data=" + data);
				final String templateId = (String) data.get("templateId");
				try {
					PortalHelper.instance().getContent(client, templateId, data);
				} catch (Exception e) {
					System.out.println(S_NAMESPACE + " - GetContent error, data=" + data);
				}
			}
		});
		// 添加监听，客户端发送指令获取portlet数据
		namespace.addEventListener("ResponseData", HashMap.class, new DataListener<HashMap>() {
			@Override
			public void onData(final SocketIOClient client, final HashMap data, final AckRequest ackRequest) {

				System.out.println(S_NAMESPACE + " - GetContent data=" + data);
				final String templateId = (String) data.get("templateId");
				try {
					PortalHelper.instance().getContent(client, templateId, data);
				} catch (Exception e) {
					System.out.println(S_NAMESPACE + " - GetContent error, data=" + data);
				}
			}
		});
		// 添加监听，客户端发送指令获取portlet数据
		namespace.addEventListener("GetContent", HashMap.class, new DataListener<HashMap>() {
			@Override
			public void onData(final SocketIOClient client, final HashMap data, final AckRequest ackRequest) {

				System.out.println(S_NAMESPACE + " - GetContent data=" + data);
				final String templateId = (String) data.get("templateId");
				try {
					PortalHelper.instance().getContent(client, templateId, data);
				} catch (Exception e) {
					System.out.println(S_NAMESPACE + " - GetContent error, data=" + data);
				}
			}
		});
		// 添加监听，客户端发送指令获取portlet数据
		namespace.addEventListener("GetPortletContent", HashMap.class, new DataListener<HashMap>() {
			@Override
			public void onData(final SocketIOClient client, final HashMap data, final AckRequest ackRequest) {
				System.out.println(S_NAMESPACE + " - GetPortletContent data=" + data);
				String templateId = (String) data.get("templateId");
				String portletId = (String) data.get("portletId");
				try {
					PortalHelper.instance().getPortletContent(client, templateId, portletId, data);
				} catch (Exception e) {
					System.out.println(S_NAMESPACE + " - GetPortletContent error, data=" + data);
				}
			}
		});
		namespace.addConnectListener(new ConnectListener() {
			@Override
			public void onConnect(final SocketIOClient client) {
				System.out.println(S_NAMESPACE + " - Client[" + client.getRemoteAddress() + "] connected!");
			}
		});
		namespace.addDisconnectListener(new DisconnectListener() {
			@Override
			public void onDisconnect(final SocketIOClient client) {
				System.out.println(S_NAMESPACE + " - Client[" + client.getRemoteAddress() + "] disconnected!");
			}
		});
		System.out.println("Add namespace [" + S_NAMESPACE + "] success.");
	}

}
