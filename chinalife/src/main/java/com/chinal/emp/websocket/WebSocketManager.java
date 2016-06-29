package com.chinal.emp.websocket;

import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import com.chinal.emp.util.XmlUtils;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import io.netty.util.concurrent.Future;

/**
 * WebSocket管理类 <br>
 */
@Component
public final class WebSocketManager implements IWebSocketManager {

	/**
	 * <code>m_start</code> - 启动标识.
	 */
	private boolean m_start;

	/**
	 * <code>m_startTime</code> - 启动时间.
	 */
	private long m_startTime;

	/**
	 * <code>m_server</code> - SocketIOServer.
	 */
	private SocketIOServer m_server;

	@Override
	@PostConstruct
	public synchronized void startup() {
		if (!isStart()) {
			Configuration t_config = init();
			m_server = new SocketIOServer(t_config);
			Future<Void> t_future = m_server.startAsync();
			try {
				t_future.get();
				if (t_future.isSuccess()) {
					m_startTime = System.currentTimeMillis();
					m_start = true;
				}
			} catch (InterruptedException t_e) {
			} catch (ExecutionException t_e) {
			}
		}
	}

	@Override
	@PreDestroy
	public synchronized void shutdown() {
		if (isStart()) {
			m_server.stop();
			m_startTime = 0L;
			m_start = false;
		}
	}

	@Override
	public boolean isStart() {
		return m_start;
	}

	@Override
	public long upTime() {
		if (isStart()) {
			return System.currentTimeMillis() - m_startTime;
		}
		return 0L;
	}

	@Override
	public SocketIOServer getWebSocketServer() {
		return m_server;
	}

	/**
	 * 初始化配置.
	 * 
	 * @return Configuration
	 */
	private Configuration init() {
		Configuration t_config = new Configuration();
		Document t_doc = XmlUtils
				.loadDocByFilename(WebSocketManager.class.getClassLoader().getResourceAsStream("websocket.xml"));
		if (t_doc != null) {
			Element t_root = t_doc.getRootElement();
			if (t_root.element("ws") != null) {
				Element t_ws = t_root.element("ws");
				t_config.setHostname(t_ws.attributeValue("ip"));
				t_config.setPort(Integer.valueOf(t_ws.attributeValue("port")).intValue());
			}
		}
		return t_config;
	}

}
