package permission.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

// key/value -> srId/List<UserOperation>
public class UserPermission extends HashMap<String, List<UserOperation>> {
	private static final long serialVersionUID = 1L;
	
	private Set<String> urls = new HashSet<String>();

	public List<UserOperation> getBySrId(String srId) {
		return this.get(srId);
	}
	
	/**
	 * 添加菜单权限
	 * 
	 * @param srId
	 *            菜单ID
	 * @param operateCode
	 *            权限代码
	 */
	public void addPermission(String srId, UserOperation userOperation) {
		List<UserOperation> userOperations = this.get(srId);
		if (userOperations == null) {
			userOperations = new ArrayList<UserOperation>();
			this.put(srId, userOperations);
		}
		userOperations.add(userOperation);
		
		this.addUrls(userOperation.getUrl());
	}
	
	/**
	 * 添加用户urls
	 * @param urls
	 */
	public void addUrls(String urls) {
		if(StringUtils.isBlank(urls)) {
			return;
		}
		
		String[] urlArr = urls.split(",|，");
		
		for (String url : urlArr) {
			this.urls.add(StringUtils.trim(url));
		}
		
	}
	
	/**
	 * 验证用户url
	 * @param url
	 * @return
	 */
	public boolean isValidUrl(String url) {
		return urls.contains(url);
	}

	/**
	 * 菜单srId是否具有某operateCode权限
	 * 
	 * @param srId
	 * @param operateCode
	 * @return true,有
	 */
	public boolean hasPermission(String srId, String operateCode) {
		if (StringUtils.isEmpty(srId) || StringUtils.isEmpty(operateCode)) {
			return false;
		}

		List<UserOperation> userOperations = this.get(srId);
		
		if(userOperations == null) {
			return false;
		}
		
		for (UserOperation userOperation : userOperations) {
			if(operateCode.equals(userOperation.getOperateCode())) {
				return true;
			}
		}

		return false;
	}
	
	public static void main(String[] args) {
		String urls = "aa.do,bb.do，cc.do";
		String[] urlArr = urls.split(",|，");
		
		for (String url : urlArr) {
			System.out.println(url);
		}
	}

}
