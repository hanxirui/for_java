package com.chinal.emp.websocket;

/**
 * @author wangtao
 *
 */
public class DetailInfoWebSocket {

	/**
	 * <code>S_INSTANCE</code> - {description}.
	 */
	private static final DetailInfoWebSocket S_INSTANCE = new DetailInfoWebSocket();

	/**
	 * Constructors.
	 */
	private DetailInfoWebSocket() {

	}

	/**
	 * {method description}.
	 * 
	 * @return DetailInfoWebSocket
	 */
	public static DetailInfoWebSocket instance() {
		return S_INSTANCE;
	}

	/**
	 * @throws ContainerException
	 *             ContainerException
	 */
	// public void init() throws ContainerException {
	// final LoginUser user = (LoginUser) ThreadLocalUtil.get();
	// IWebSocketManager service =
	// ServiceContainer.getInstance().getService(IWebSocketManager.S_SERVICE_ID);
	// SocketIONamespace namespace =
	// service.getWebSocketServer().getNamespace("/detailThirdContent");
	// if (namespace == null) {
	// namespace =
	// service.getWebSocketServer().addNamespace("/detailThirdContent");
	// namespace.addEventListener("load", Map.class, new DataListener<Map>() {
	// @Override
	// public void onData(final SocketIOClient client, final Map data, final
	// AckRequest ackSender)
	// throws Exception {
	// ThreadLocalUtil.add(user);
	// String id = (String) data.get("id");
	// String ctx = (String) data.get("ctx");
	// TemplateVo template = TemplateEngine.instance().execute(id);
	// List<PorletVo> porlets = template.getPorlets();
	// if (CollectionUtils.isNotEmpty(porlets)) {
	// for (PorletVo porlet : porlets) {
	// new TaskExecutor(id, porlet, client, ctx, "load").run();
	// }
	// }
	// }
	// });
	// }
	// }
}
