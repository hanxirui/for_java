package com.chinal.emp.report.action;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.chinal.emp.report.ButtonVo;
import com.chinal.emp.report.PortletVo;
import com.chinal.emp.report.util.I18nUtils;
import com.chinal.emp.report.util.VmUtil;
import com.chinal.emp.util.WebUtil;

/**
 * action执行抽象类. <br>
 * <p>
 * Create on : 2016年1月26日<br>
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
public abstract class AbsAction {

	/**
	 * 组件执行器.
	 * 
	 * @param templateId
	 *            templateId
	 * @param portlet
	 *            PortletVo
	 * @param hasButton
	 *            hasButton
	 * @param request
	 *            request
	 * @return html
	 * @throws ServiceException
	 *             ServiceException
	 * @throws InfoException
	 *             InfoException
	 * @throws ContainerException
	 *             ContainerException
	 * @throws ParseException
	 *             解析异常
	 */
	public final String execute(final String templateId, final PortletVo portlet, final boolean hasButton,
			final HttpServletRequest request) throws Exception {
		String content = "";
		if (portlet.isVisibility()) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("request", request);
			param.put("ctx", WebUtil.getCtx(request));
			param.put("templateId", templateId);
			param.put("portlet", portlet);
			param.put("parameter", portlet.getData());
			param.put("i18nUtil", I18nUtils.getInstance());
			content += VmUtil.parseVM(actionData(param), portlet.getVm());
			if (hasButton) {
				content += VmUtil.parseVM(buttonData(portlet.getButtons(), param), "button.vm");
			}
		}
		return content;
	}

	/**
	 * 组件执行器.
	 * 
	 * @param templateId
	 *            templateId
	 * @param portlet
	 *            PortletVo
	 * @param hasButton
	 *            hasButton
	 * @param params
	 *            params
	 * @return html
	 * @throws ServiceException
	 *             ServiceException
	 * @throws InfoException
	 *             InfoException
	 * @throws ContainerException
	 *             ContainerException
	 * @throws ParseException
	 *             解析异常
	 */
	public final String execute(final String templateId, final PortletVo portlet, final boolean hasButton,
			final Map<String, Object> params) throws Exception {
		String content = "";
		if (portlet.isVisibility()) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.putAll(params);
			param.put("templateId", templateId);
			param.put("portlet", portlet);
			param.put("parameter", portlet.getData());
			param.put("i18nUtil", I18nUtils.getInstance());
			content += VmUtil.parseVM(actionData(param), portlet.getVm());
			if (hasButton) {
				content += VmUtil.parseVM(buttonData(portlet.getButtons(), param), "button.vm");
			}
		}
		return content;
	}

	/**
	 * 组装vm所需参数. (用于接收参数，调用业务方法，组装VM所需参数)
	 * 
	 * @param map
	 *            Map<String, Object> 公共参数
	 * @return 组件参数
	 * @throws ServiceException
	 *             ServiceException
	 * @throws InfoException
	 *             InfoException
	 * @throws ContainerException
	 *             ContainerException
	 * @throws ParseException
	 *             解析异常
	 */
	protected abstract Map<String, Object> actionData(Map<String, Object> map) throws Exception;

	/**
	 * 组装按钮vm所需参数.
	 * 
	 * @param buttons
	 *            List<ButtonVo>
	 * @param param
	 *            Map<String, Object> 公共参数
	 * @return 组件参数
	 * @throws ServiceException
	 *             ServiceException
	 */
	private Map<String, Object> buttonData(final List<ButtonVo> buttons, final Map<String, Object> param)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("buttons", buttons);
		map.putAll(param);
		return map;
	}

}
