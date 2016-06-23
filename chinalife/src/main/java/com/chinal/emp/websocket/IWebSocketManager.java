package com.chinal.emp.websocket;

import com.corundumstudio.socketio.SocketIOServer;

/**
 * WebSocket管理类 <br>
 * 
 */
public interface IWebSocketManager {

	/**
	 * <code>S_SERVICE_ID</code> - S_SERVICE_ID.
	 */
	String S_SERVICE_ID = "webSocketManager";

	/**
	 * 启动.
	 * 
	 * @throws ServiceException
	 *             ServiceException
	 */
	void startup();

	/**
	 * 停止.
	 */
	void shutdown();

	/**
	 * 是否启动.
	 * 
	 * @return BOOLEAN
	 */
	boolean isStart();

	/**
	 * 连续运行时间.
	 * 
	 * @return ms
	 */
	long upTime();

	/**
	 * 获取WebSocket Server.
	 * 
	 * @return SocketIOServer
	 */
	SocketIOServer getWebSocketServer();

}