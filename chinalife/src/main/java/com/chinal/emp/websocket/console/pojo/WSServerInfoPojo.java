package com.chinal.emp.websocket.console.pojo;

/**
 * Server信息 <br>
 */
public class WSServerInfoPojo {

	/**
	 * <code>m_name</code> - 地址.
	 */
	private String m_name;

	/**
	 * <code>m_port</code> - 端口.
	 */
	private int m_port;

	/**
	 * <code>m_upTime</code> - 连续运行时间.
	 */
	private long m_upTime;

	/**
	 * <code>m_storeFactory</code> - 存储工厂.
	 */
	private String m_storeFactory;

	/**
	 * <code>m_isStart</code> - 是否启动.
	 */
	private boolean m_isStart;

	/**
	 * <code>m_heartInterval</code> - 心跳间隔.
	 */
	private int m_heartInterval;

	/**
	 * <code>m_heartTimeout</code> - 心跳超时时间.
	 */
	private int m_heartTimeout;

	/**
	 * <code>m_bossThreads</code> - boss线程数.
	 */
	private int m_bossThreads;

	/**
	 * <code>m_workerThreads</code> - work线程数.
	 */
	private int m_workerThreads;

	/**
	 * 地址.
	 * 
	 * @return 地址
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * 地址.
	 * 
	 * @param name
	 *            地址
	 */
	public void setName(final String name) {
		m_name = name;
	}

	/**
	 * 端口.
	 * 
	 * @return 端口
	 */
	public int getPort() {
		return m_port;
	}

	/**
	 * 端口.
	 * 
	 * @param port
	 *            端口
	 */
	public void setPort(final int port) {
		m_port = port;
	}

	/**
	 * 连续运行时间.
	 * 
	 * @return 连续运行时间
	 */
	public long getUpTime() {
		return m_upTime;
	}

	/**
	 * 连续运行时间.
	 * 
	 * @param upTime
	 *            连续运行时间
	 */
	public void setUpTime(final long upTime) {
		m_upTime = upTime;
	}

	/**
	 * 存储工厂.
	 * 
	 * @return 存储工厂
	 */
	public String getStoreFactory() {
		return m_storeFactory;
	}

	/**
	 * 存储工厂.
	 * 
	 * @param storeFactory
	 *            存储工厂
	 */
	public void setStoreFactory(final String storeFactory) {
		m_storeFactory = storeFactory;
	}

	/**
	 * 是否启动.
	 * 
	 * @return 是否启动
	 */
	public boolean isStart() {
		return m_isStart;
	}

	/**
	 * 是否启动.
	 * 
	 * @param isStart
	 *            是否启动
	 */
	public void setStart(final boolean isStart) {
		m_isStart = isStart;
	}

	/**
	 * 心跳间隔.
	 * 
	 * @return 心跳间隔
	 */
	public int getHeartInterval() {
		return m_heartInterval;
	}

	/**
	 * 心跳间隔.
	 * 
	 * @param heartInterval
	 *            心跳间隔
	 */
	public void setHeartInterval(final int heartInterval) {
		m_heartInterval = heartInterval;
	}

	/**
	 * 心跳超时时间.
	 * 
	 * @return 心跳超时时间
	 */
	public int getHeartTimeout() {
		return m_heartTimeout;
	}

	/**
	 * 心跳超时时间.
	 * 
	 * @param heartTimeout
	 *            心跳超时时间
	 */
	public void setHeartTimeout(final int heartTimeout) {
		m_heartTimeout = heartTimeout;
	}

	/**
	 * boss线程数.
	 * 
	 * @return boss线程数
	 */
	public int getBossThreads() {
		return m_bossThreads;
	}

	/**
	 * boss线程数.
	 * 
	 * @param bossThreads
	 *            boss线程数
	 */
	public void setBossThreads(final int bossThreads) {
		m_bossThreads = bossThreads;
	}

	/**
	 * work线程数.
	 * 
	 * @return work线程数
	 */
	public int getWorkerThreads() {
		return m_workerThreads;
	}

	/**
	 * work线程数.
	 * 
	 * @param workerThreads
	 *            work线程数
	 */
	public void setWorkerThreads(final int workerThreads) {
		m_workerThreads = workerThreads;
	}

}
