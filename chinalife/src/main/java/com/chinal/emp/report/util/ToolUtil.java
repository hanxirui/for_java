package com.chinal.emp.report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.report.vo.PortletVo;
import com.chinal.emp.report.vo.UserDataVo;
import com.chinal.emp.security.AuthUser;

/**
 * 用户数据工具类.<br>
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
public final class ToolUtil {

	/**
	 * Constructors.
	 */
	private ToolUtil() {
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @return AuthUser AuthUser
	 * @throws ServiceException
	 */
	public static AuthUser getAuthUser() {
		return (AuthUser) ThreadLocalUtil.get();
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @return String userId
	 * @throws ServiceException
	 */
	public static String getUserId() {
		return getAuthUser().getCode();
	}

	/**
	 * json2Map.
	 * 
	 * @param jsonData
	 *            String
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2Map(final String jsonData) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(jsonData)) {
			// 最外层解析
			JSONObject json = JSONObject.parseObject(jsonData);
			for (Object k : json.keySet()) {
				Object v = json.get(k);
				// 如果内层还是数组的话，继续解析
				if (v instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Iterator<Object> it = ((JSONArray) v).iterator();
					while (it.hasNext()) {
						JSONObject json2 = (JSONObject) it.next();
						list.add(json2Map(json2.toString()));
					}
					map.put(k.toString(), list);
				} else {
					map.put(k.toString(), v);
				}
			}
		}
		return map;
	}

	/**
	 * {method description}.
	 * 
	 * @param list
	 *            List<PortletVo>
	 * @return List<UserDataJsonVo>
	 */
	public static List<UserDataVo> convert2UserData(final List<PortletVo> list) {
		ArrayList<UserDataVo> userdatas = new ArrayList<UserDataVo>();
		for (PortletVo portlet : list) {
			userdatas.add(convert2UserData(portlet));
		}
		return userdatas;
	}

	/**
	 * {method description}.
	 * 
	 * @param portlet
	 *            PortletVo
	 * @return UserDataVo
	 */
	public static UserDataVo convert2UserData(final PortletVo portlet) {
		UserDataVo userdata = new UserDataVo();
		userdata.setPortletId(portlet.getId());
		userdata.setX(portlet.getX());
		userdata.setY(portlet.getY());
		userdata.setWidth(portlet.getWidth());
		userdata.setHeight(portlet.getHeight());
		userdata.setData(portlet.getData());
		userdata.setVisibility(portlet.isVisibility());
		return userdata;
	}

	/**
	 * {method description}.
	 * 
	 * @param userdatas
	 *            List<UserDataJsonVo>
	 * @param portlets
	 *            List<PortletVo>
	 * @param checkOverrideData
	 *            是否检测覆盖用户个性化数据
	 */
	public static void fillUserData(final List<UserDataVo> userdatas, final List<PortletVo> portlets,
			final boolean checkOverrideData) {
		// 保证存入和输出顺序一致
		Map<String, UserDataVo> map = new LinkedHashMap<String, UserDataVo>();
		for (UserDataVo userdata : userdatas) {
			map.put(userdata.getPortletId(), userdata);
		}
		Map<String, PortletVo> oriPortletMap = new HashMap<String, PortletVo>();
		for (PortletVo portlet : portlets) {
			String originalPortletId = portlet.getId();
			oriPortletMap.put(originalPortletId, portlet);
			if (map.containsKey(originalPortletId)) {
				UserDataVo userdata = map.get(portlet.getId());
				portlet.setX(userdata.getX());
				portlet.setY(userdata.getY());
				portlet.setWidth(userdata.getWidth());
				portlet.setHeight(userdata.getHeight());
				portlet.setVisibility(userdata.isVisibility());
				portlet.setData(userdata.getData());
				map.remove(originalPortletId);
			}
		}
		if (!map.isEmpty()) {
			Set<Entry<String, UserDataVo>> entrySet = map.entrySet();
			for (Entry<String, UserDataVo> entry : entrySet) {
				String portletId = entry.getKey();
				UserDataVo userData = entry.getValue();
				String originalPortletId = userData.getOriginalPortletId();
				if (StringUtils.isBlank(originalPortletId)) {
					continue;
				}
				if (oriPortletMap.containsKey(originalPortletId)) {
					PortletVo portletVo = oriPortletMap.get(originalPortletId);
					PortletVo newPortletVo = portletVo.clonePortlet();
					if (newPortletVo != null) {
						newPortletVo.setId(portletId);
						newPortletVo.setX(userData.getX());
						newPortletVo.setY(userData.getY());
						newPortletVo.setWidth(userData.getWidth());
						newPortletVo.setHeight(userData.getHeight());
						newPortletVo.setVisibility(userData.isVisibility());
						newPortletVo.setData(userData.getData());
						portlets.add(newPortletVo);
					} else {
					}
				}
			}
		}
	}
}
