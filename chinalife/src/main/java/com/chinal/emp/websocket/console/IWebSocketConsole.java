package com.chinal.emp.websocket.console;

import java.util.List;
import java.util.Map;

import com.chinal.emp.websocket.console.pojo.WSClientInfoPojo;
import com.chinal.emp.websocket.console.pojo.WSServerInfoPojo;

/**
 * WebSocket控制台 <br>
 */
public interface IWebSocketConsole {

	/**
	 * <code>S_SERVICE_ID</code> - S_SERVICE_ID.
	 */
	String S_SERVICE_ID = "webSocketConsole";

	/**
	 * Server信息.
	 * 
	 * @return WSServerInfoPojo
	 */
	WSServerInfoPojo getWSServerInfo();

	/**
	 * 获取所有命名空间.
	 * 
	 * @return List<String>
	 */
	List<String> getAllNameSpace();

	/**
	 * 获取所有客户端信息.
	 * 
	 * @return List<WSClientInfoPojo>
	 */
	List<WSClientInfoPojo> getAllWSClientInfo();

	/**
	 * 获取一个命名空间下的所有客户端信息.
	 * 
	 * @param namespace
	 *            命名空间
	 * @return List<WSClientInfoPojo>
	 */
	List<WSClientInfoPojo> getWSClientInfoByNameSpace(String namespace);

	/**
	 * 删除命名空间.
	 * 
	 * @param name
	 *            命名空间
	 */
	void removeNameSpace(String name);

	/**
	 * 断开客户端连接.
	 * 
	 * @param clientId
	 *            客户端UUID
	 */
	void disconnectClient(String clientId);

	/**
	 * 对客户断下按订阅注意分组统计未处理的消息数量.
	 * 
	 * @param clientId
	 *            客户端UUID
	 * @return Map<String, Integer>
	 */
	Map<String, Integer> getWaitMsgCounterGroupByTopic(String clientId);

	/**
	 * 清除客户端下的未处理消息.
	 * 
	 * @param clientId
	 *            客户端UUID
	 */
	void clearClientMsg(String clientId);

	/**
	 * 给命名空间下的主题广播发送信息.
	 * 
	 * @param namespace
	 *            命名空间
	 * @param topic
	 *            订阅主题
	 * @param msg
	 *            消息
	 */
	void sendToNameSpace(String namespace, String topic, Object msg);

	/**
	 * 对客户端订阅的主题进行点到点发送.
	 * 
	 * @param clientId
	 *            客户端UUID
	 * @param topic
	 *            订阅主题
	 * @param msg
	 *            消息
	 */
	void sendToClient(String clientId, String topic, Object msg);

}
