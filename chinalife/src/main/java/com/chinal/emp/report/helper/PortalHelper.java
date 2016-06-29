package com.chinal.emp.report.helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinal.emp.report.action.AbsAction;
import com.chinal.emp.report.generator.ActionGenerator;
import com.chinal.emp.report.template.TemplateEngine;
import com.chinal.emp.report.vo.PortletVo;
import com.chinal.emp.report.vo.TemplateVo;
import com.chinal.emp.service.InsuranceRecordService;
import com.corundumstudio.socketio.SocketIOClient;

/**
 * portal 辅助类.<br>
 * <p>
 * Create on : 2016年1月22日<br>
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
public class PortalHelper {

	/**
	 * <code>S_INSTANCE</code> - {description}.
	 */
	private static final PortalHelper S_INSTANCE = new PortalHelper();

	/**
	 * <code>S_EXECUTOR</code> - S_EXECUTOR.
	 */
	private static final ExecutorService S_EXECUTOR = Executors.newFixedThreadPool(20);

	@Autowired
	private InsuranceRecordService insuranceRecordService;

	/**
	 * Constructors.
	 */
	private PortalHelper() {
	}

	/**
	 * {method description}.
	 * 
	 * @return MonitorDetailHelper
	 */
	public static PortalHelper instance() {
		return S_INSTANCE;
	}

	/**
	 * {method description}.
	 * 
	 * @param client
	 *            SocketIOClient
	 * @param templateId
	 *            templateId
	 * @param params
	 *            params
	 * @throws Exception
	 *             Exception
	 */
	public void getContent(final SocketIOClient client, final String templateId, final HashMap<String, Object> params)
			throws Exception {
		System.out.println(insuranceRecordService);
		TemplateVo template = TemplateEngine.instance().execute(templateId, null);
		List<PortletVo> portlets = template.getPortlets();
		if (CollectionUtils.isNotEmpty(portlets)) {
			for (final PortletVo portlet : portlets) {
				if (portlet.isVisibility()) {
					S_EXECUTOR.execute(new Runnable() {
						@Override
						public void run() {
							try {
								AbsAction action = ActionGenerator.generator(portlet.getAction());
								String content = action.execute(templateId, portlet, true, params);
								Map<String, String> result = new HashMap<String, String>();
								result.put("portletId", portlet.getId());
								result.put("content", content);
								client.sendEvent("ResponseData", result);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param client
	 *            SocketIOClient
	 * @param templateId
	 *            templateId
	 * @param portletId
	 *            portletId
	 * @param params
	 *            params
	 * @throws Exception
	 *             Exception
	 */
	public void getPortletContent(final SocketIOClient client, final String templateId, final String portletId,
			final HashMap<String, Object> params) throws Exception {
		String userId = (String) params.get("userId");
		TemplateVo template = TemplateEngine.instance().execute(templateId, userId);
		List<PortletVo> portlets = template.getPortlets();
		PortletVo portlet = null;
		if (CollectionUtils.isNotEmpty(portlets)) {
			for (PortletVo portletVo : portlets) {
				if (portletId.equals(portletVo.getId()) && portletVo.isVisibility()) {
					portlet = portletVo;
					break;
				}
			}
		}
		if (portlet != null) {
			String content = ActionGenerator.generator(portlet.getAction()).execute(templateId, portlet, true, params);
			Map<String, String> result = new HashMap<String, String>();
			result.put("portletId", portlet.getId());
			result.put("content", content);
			client.sendEvent("ResponsePortletData", result);
		}
	}

	/**
	 * 返回前台页面部分内容.
	 * 
	 * @param writer
	 *            PrintWriter
	 * @param portletId
	 *            portletId
	 * @param content
	 *            内容
	 * @throws IOException
	 *             异常
	 */
	public void flushFragment(final PrintWriter writer, final String portletId, final String content)
			throws IOException {
		// bigpipe推送到前台
		StringBuilder builder = new StringBuilder();
		builder.append("<div id=\"").append(portletId).append("_content\" style=\"display:none\">");
		builder.append(content).append("</div><script>bigpipe.add({id:'").append(portletId);
		builder.append("_content'});ButtonComm.resizePorletInfoHeight('");
		builder.append(portletId).append("');</script>");
		flushFragment(writer, builder.toString());
	}

	/**
	 * 返回前台页面部分内容.
	 * 
	 * @param writer
	 *            PrintWriter
	 * @param string
	 *            内容
	 * @throws IOException
	 *             异常
	 */
	public void flushFragment(final PrintWriter writer, final String string) throws IOException {
		if (writer != null) {
			synchronized (writer) {
				writer.write(string);
				writer.flush();
			}
		}
	}
}
