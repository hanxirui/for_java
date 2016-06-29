package com.chinal.emp.report.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.report.util.ToolUtil;
import com.chinal.emp.report.vo.PortletVo;
import com.chinal.emp.report.vo.TemplateVo;
import com.chinal.emp.report.vo.UserDataVo;
import com.chinal.emp.util.ServerEnvUtil;

/**
 * 读写用户数据,用户个性化数据文件名格式：用户ID_模板ID.xml <br>
 * <p>
 * Create on : 2016年1月21日<br>
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
public class UserDataService {

	/**
	 * <code>S_UNDERLINE</code> - {description}.
	 */
	private static final String S_UNDERLINE = "_";

	/**
	 * <code>S_DATA</code> - "data".
	 */
	private static final String S_DATA = "data";

	/**
	 * <code>S_INSTANCE</code> - {description}.
	 */
	private static final UserDataService S_INSTANCE = new UserDataService();

	/**
	 * Constructors.
	 */
	private UserDataService() {
	}

	/**
	 * {method description}.
	 * 
	 * @return ReadWriteFile
	 */
	public static UserDataService instance() {
		return S_INSTANCE;
	}

	/**
	 * {method description}.
	 * 
	 * @param templateId
	 *            模板ID
	 * @param userId
	 *            userId
	 * @return List<UserDataJsonVo>
	 * @throws Exception
	 *             Exception
	 */
	public List<UserDataVo> getUserData(final String templateId, final String userId) throws Exception {
		try {
			TemplateVo template = TemplateEngine.instance().execute(templateId, userId);
			return ToolUtil.convert2UserData(template.getPortlets());
		} catch (Exception t_e) {
			throw new Exception(t_e);
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param templateId
	 *            模板ID
	 * @param userId
	 *            userId
	 * @param portletId
	 *            portlet id
	 * @return UserDataVo
	 * @throws Exception
	 *             Exception
	 */
	public UserDataVo getUserDataByPortletId(final String templateId, final String userId, final String portletId)
			throws Exception {
		try {
			TemplateVo template = TemplateEngine.instance().execute(templateId, userId);
			List<PortletVo> portlets = template.getPortlets();
			UserDataVo userDataVo = null;
			for (PortletVo portletVo : portlets) {
				if (portletVo.getId().equals(portletId)) {
					userDataVo = ToolUtil.convert2UserData(portletVo);
					break;
				}
			}
			return userDataVo;
		} catch (Exception t_e) {
			throw new Exception(t_e);
		}
	}

	/**
	 * 恢复出厂布局.
	 * 
	 * @param templateId
	 *            templateId
	 * @param userId
	 *            userId
	 * @throws Exception
	 *             Exception
	 */
	public void restoreFactoryLayout(final String templateId, final String userId) throws Exception {
		try {
			TemplateVo template = TemplateEngine.instance().execute(templateId, userId);
			List<UserDataVo> udatas = ToolUtil.convert2UserData(template.getPortlets());
			write(templateId, ToolUtil.getUserId(), udatas);
		} catch (Exception t_e) {
			throw new Exception(t_e);
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param templateId
	 *            templateId
	 * @param userId
	 *            userId
	 * @param userData
	 *            List<UserDataJsonVo>
	 * @throws Exception
	 *             Exception
	 */
	public void write(final String templateId, final String userId, final List<UserDataVo> userData) throws Exception {
		try {
			write(filePath(userId, templateId), JSONArray.toJSONString(userData));
		} catch (IOException t_e) {
			throw new Exception(t_e);
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param str1
	 *            str1
	 * @param str2
	 *            str2
	 * @return String
	 */
	public String fileName(final String str1, final String str2) {
		return str1 + S_UNDERLINE + str2;
	}

	/**
	 * {method description}.
	 * 
	 * @param str1
	 *            str1
	 * @param str2
	 *            str2
	 * @return String
	 */
	private String filePath(final String str1, final String str2) {
		return ServerEnvUtil.S_UDATA_RELATIVE_PATH + fileName(str1, str2) + ".json";
	}

	/**
	 * {method description}.
	 * 
	 * @param filePath
	 *            filePath
	 * @param str
	 *            str
	 * @throws IOException
	 *             IOException
	 */
	public void write(final String filePath, final String str) throws IOException {
		PrintWriter out = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new PrintWriter(file);
			out.write(str);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param path
	 *            path
	 * @return String
	 * @throws IOException
	 *             IOException
	 */
	private String read(final String path) throws IOException {
		File file = new File(path);
		BufferedReader reader = null;
		StringBuilder fileContent = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				fileContent.append(tempString);
			}
			reader.close();
		} finally {
			if (reader != null) {
				reader.close();

			}
		}
		return fileContent.toString();
	}

	/**
	 * 查询用户数据.
	 * 
	 * @param templateId
	 *            模板ID
	 * @param userId
	 *            userId
	 * @param template
	 *            TemplateVo
	 * @throws Exception
	 *             Exception
	 */
	public void mergeUdata(final String templateId, final String userId, final TemplateVo template) throws Exception {
		List<UserDataVo> udatas = queryUdata(templateId, userId, template);
		if (CollectionUtils.isNotEmpty(udatas)) {
			ToolUtil.fillUserData(udatas, template.getPortlets(), false);
			return;
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param templateId
	 *            templateId
	 * @param userId
	 *            userId
	 * @param template
	 *            TemplateVo
	 * @return List<UserDataVo>
	 * @throws Exception
	 *             Exception
	 */
	public List<UserDataVo> queryUdata(final String templateId, final String userId, final TemplateVo template)
			throws Exception {
		String fn = filePath(userId, templateId);
		File file = new File(fn);
		List<UserDataVo> udatas = new ArrayList<UserDataVo>();
		if (file.exists()) {
			try {
				udatas = toUdatBean(read(file.getAbsolutePath()));
			} catch (IOException t_e) {
				throw new Exception(t_e);
			}
		}
		return udatas;
	}

	/**
	 * {method description}.
	 * 
	 * @param udata
	 *            udata
	 * @return List<UserDataJsonVo>
	 * @throws Exception
	 *             Exception
	 */
	private List<UserDataVo> toUdatBean(final String udata) throws Exception {
		List<UserDataVo> list = new ArrayList<UserDataVo>();
		JSONArray array = JSONArray.parseArray(udata);
		for (int i = 0; i < array.size(); i++) {
			JSONObject t_object = (JSONObject) array.get(i);
			try {
				UserDataVo bean = JSONObject.toJavaObject(t_object, UserDataVo.class);
				bean.setData(t_object.getString(S_DATA));
				list.add(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
