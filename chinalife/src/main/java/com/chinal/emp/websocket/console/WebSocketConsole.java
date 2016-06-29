package com.chinal.emp.websocket.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.chinal.emp.websocket.WebSocketManager;
import com.chinal.emp.websocket.console.pojo.WSClientInfoPojo;
import com.chinal.emp.websocket.console.pojo.WSServerInfoPojo;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.transport.NamespaceClient;

/**
 * WebSocket控制台 <br>
 * <p>
 * Create on : 2016-3-7<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author Liqiang<br>
 * @version riil-websocket-manager v6.4.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
@Component
public class WebSocketConsole implements IWebSocketConsole {

	/**
	 * <code>m_webSocketMgr</code> - webSocketMgr.
	 */
	@Autowired
	private WebSocketManager m_webSocketMgr;

	/**
	 * 注入.
	 * 
	 * @param webSocketMgr
	 *            webSocketMgr
	 */
	public void setWebSocketMgr(final WebSocketManager webSocketMgr) {
		m_webSocketMgr = webSocketMgr;
	}

	@Override
	public WSServerInfoPojo getWSServerInfo() {
		WSServerInfoPojo t_wsInfo = new WSServerInfoPojo();
		t_wsInfo.setStart(m_webSocketMgr.isStart());
		if (t_wsInfo.isStart()) {
			SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
			Configuration t_config = t_server.getConfiguration();
			t_wsInfo.setName(t_config.getHostname());
			t_wsInfo.setPort(t_config.getPort());
			t_wsInfo.setUpTime(m_webSocketMgr.upTime());
			t_wsInfo.setStoreFactory(t_config.getStoreFactory().getClass().getSimpleName());
			t_wsInfo.setHeartInterval(t_config.getPingInterval());
			t_wsInfo.setHeartTimeout(t_config.getPingTimeout());
			t_wsInfo.setBossThreads(t_config.getBossThreads());
			t_wsInfo.setWorkerThreads(t_config.getWorkerThreads());
		}
		return t_wsInfo;
	}

	@Override
	public List<String> getAllNameSpace() {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		List<String> t_names = new ArrayList<String>();
		if (m_webSocketMgr.isStart()) {
			Collection<SocketIONamespace> t_namespaces = t_server.getAllNamespaces();
			for (SocketIONamespace t_socketIONamespace : t_namespaces) {
				t_names.add(t_socketIONamespace.getName());
			}
		}
		return t_names;
	}

	@Override
	public List<WSClientInfoPojo> getAllWSClientInfo() {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		Collection<SocketIOClient> t_socketIOClients = null;
		if (m_webSocketMgr.isStart()) {
			t_socketIOClients = t_server.getAllClients();
		}
		return convertClient(t_socketIOClients);
	}

	@Override
	public List<WSClientInfoPojo> getWSClientInfoByNameSpace(final String namespace) {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		Collection<SocketIOClient> t_socketIOClients = null;
		if (m_webSocketMgr.isStart()) {
			SocketIONamespace t_namespace = t_server.getNamespace(namespace);
			t_socketIOClients = t_namespace.getAllClients();
		}
		return convertClient(t_socketIOClients);
	}

	/**
	 * 对象转换.
	 * 
	 * @param socketIOClients
	 *            SocketIOClient
	 * @return List<WSClientInfoPojo>
	 */
	private List<WSClientInfoPojo> convertClient(final Collection<SocketIOClient> socketIOClients) {
		List<WSClientInfoPojo> t_clients = null;
		if (!CollectionUtils.isEmpty(socketIOClients)) {
			t_clients = new ArrayList<WSClientInfoPojo>();
			for (SocketIOClient t_socketIOClient : socketIOClients) {
				WSClientInfoPojo t_client = new WSClientInfoPojo();
				t_client.setId(t_socketIOClient.getSessionId().toString());
				t_client.setName(t_socketIOClient.getRemoteAddress().toString());
				t_client.setTrasport(t_socketIOClient.getTransport().getValue());
				NamespaceClient t_namespaceClient = (NamespaceClient) t_socketIOClient;
				Queue<Packet> t_queue = t_namespaceClient.getBaseClient()
						.getPacketsQueue(t_socketIOClient.getTransport());
				t_client.setWaitMsgCount(t_queue.size());
				t_clients.add(t_client);
			}
		}
		return t_clients;
	}

	@Override
	public void removeNameSpace(final String namespace) {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		t_server.removeNamespace(namespace);
	}

	@Override
	public void disconnectClient(final String clientId) {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		SocketIOClient t_client = t_server.getClient(UUID.fromString(clientId));
		if (t_client != null) {
			t_client.disconnect();
		}
	}

	@Override
	public Map<String, Integer> getWaitMsgCounterGroupByTopic(final String clientId) {
		Queue<Packet> t_queue = getWaitMsgQueue(clientId);
		Map<String, Integer> t_group = new HashMap<String, Integer>();
		if (t_queue != null) {
			Iterator<Packet> t_iterator = t_queue.iterator();
			while (t_iterator.hasNext()) {
				Packet t_packet = t_iterator.next();
				if (!t_group.containsKey(t_packet.getName())) {
					t_group.put(t_packet.getName(), 1);
				} else {
					t_group.put(t_packet.getName(), t_group.get(t_packet.getName()) + 1);
				}
			}
		}
		return t_group;
	}

	@Override
	public void clearClientMsg(final String clientId) {
		Queue<Packet> t_queue = getWaitMsgQueue(clientId);
		if (t_queue != null) {
			t_queue.clear();
		}
	}

	/**
	 * 获取待处理消息队列.
	 * 
	 * @param clientId
	 *            客户端UUID
	 * @return Queue<Packet>
	 */
	private Queue<Packet> getWaitMsgQueue(final String clientId) {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		SocketIOClient t_client = t_server.getClient(UUID.fromString(clientId));
		if (t_client != null) {
			NamespaceClient t_namespaceClient = (NamespaceClient) t_client;
			return t_namespaceClient.getBaseClient().getPacketsQueue(t_client.getTransport());
		}
		return null;
	}

	@Override
	public void sendToNameSpace(final String namespace, final String topic, final Object msg) {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		SocketIONamespace t_namespace = t_server.getNamespace(namespace);
		if (t_namespace != null) {
			t_namespace.getBroadcastOperations().sendEvent(topic, msg);
		}
	}

	@Override
	public void sendToClient(final String clientId, final String topic, final Object msg) {
		SocketIOServer t_server = m_webSocketMgr.getWebSocketServer();
		SocketIOClient t_client = t_server.getClient(UUID.fromString(clientId));
		if (t_client != null) {
			t_client.sendEvent(topic, msg);
		}
	}

}
