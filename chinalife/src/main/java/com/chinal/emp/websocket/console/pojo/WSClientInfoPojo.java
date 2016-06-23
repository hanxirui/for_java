package com.chinal.emp.websocket.console.pojo;

/**
 * 客户端信息 <br>
 */
public class WSClientInfoPojo {

	/**
	 * <code>m_id</code> - 客户端ID.
	 */
	private String m_id;

	/**
	 * <code>m_name</code> - 客户端名称，有IP和端口组成.
	 */
	private String m_name;

	/**
	 * <code>m_trasport</code> - 传输协议.
	 */
	private String m_trasport;

	/**
	 * <code>m_waitMsgCount</code> - 待处理消息数量.
	 */
	private int m_waitMsgCount;

	/**
	 * 客户端ID.
	 * 
	 * @return uuid
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * 客户端ID.
	 * 
	 * @param id
	 *            uuid
	 */
	public void setId(final String id) {
		m_id = id;
	}

	/**
	 * 客户端名称，有IP和端口组成.
	 * 
	 * @return name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * 客户端名称，有IP和端口组成.
	 * 
	 * @param name
	 *            name
	 */
	public void setName(final String name) {
		m_name = name;
	}

	/**
	 * 传输协议.
	 * 
	 * @return transport
	 */
	public String getTrasport() {
		return m_trasport;
	}

	/**
	 * 传输协议.
	 * 
	 * @param trasport
	 *            传输协议
	 */
	public void setTrasport(final String trasport) {
		m_trasport = trasport;
	}

	/**
	 * 待处理消息数量.
	 * 
	 * @return waitMsgCount
	 */
	public int getWaitMsgCount() {
		return m_waitMsgCount;
	}

	/**
	 * 待处理消息数量.
	 * 
	 * @param waitMsgCount
	 *            待处理消息数量
	 */
	public void setWaitMsgCount(final int waitMsgCount) {
		m_waitMsgCount = waitMsgCount;
	}

}
