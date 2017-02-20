package permission.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.SpringContext;
import org.durcframework.core.UserContext;
import org.durcframework.core.WebContext;
import org.durcframework.core.expression.Expression;
import org.durcframework.core.support.MyPropertyPlaceholderConfigurer;

import permission.entity.RUser;
import permission.entity.RUserRole;
import permission.service.RDataPermissionService;
import permission.service.RSysFunctionService;
import permission.service.RSysResService;
import permission.service.RUserRoleService;

public enum RMSContext {
	INS;

	private static final String USER_ROLE_IDS = "user_role_ids";
	private static final String ADMIN_ROLE_ID = "sys.adminRoleId";
	private static final String USE_SUPER_ADMIN = "sys.openSuperPermission";
	
	private static Integer adminRoleId;
	private static Boolean isOpenSuperAdmin;

	private static UserMenuContext userMenuContext = new UserMenuContext();
	private static UserPermissionContext permissionContext = new UserPermissionContext();
	
	public static RMSContext getInstance() {
		return INS;
	}

	
	/**
	 * 获取当前用户权限
	 * @return
	 */
	public UserPermission getCurrentUserPermission() {
		RUser user = UserContext.getInstance().getUser();
		if(user == null) {
			return new UserPermission();
		}
		return this.getUserPermission(user.getUserId());
	}
	
	public UserPermission getUserPermission(int userId) {
		return permissionContext.get(userId);
	}
	
	
	/**
	 * 刷新保存用权限数据.(系统功能=菜单+操作点)
	 */
	public void refreshUserRightData(int userId){
		RSysFunctionService sysFunctionService = SpringContext.getBean(RSysFunctionService.class);
		
		UserPermission userPermission = sysFunctionService.buildUserPermission(userId);
		
		permissionContext.put(userId, userPermission);
		
		//userSysFunctionMap.put(username, userSysFuns);
		
		saveUserRoleIds(userId);
		
		refreshUserMenu(userId);
	}
	
	/**
	 * 刷新用户菜单
	 * @param userId
	 */
	public void refreshUserMenu(int userId) {
		RSysResService sysResService =SpringContext.getBean(RSysResService.class);
		List<UserMenu> userMenu = sysResService.getUserMenu(userId);
		userMenuContext.put(userId, userMenu);
	}
	
	/**
	 * 获取用户菜单
	 * @return
	 */
	public List<UserMenu> getUserMenu() {
		RUser user = UserContext.getInstance().getUser();
		return userMenuContext.get(user.getUserId());
	}
	
	/**
	 * 获取用户数据权限条件
	 * @return
	 */
	public List<Expression> getUserDataExpressions() {
		String srId = WebContext.getInstance().getRequest().getParameter("srId");
		
		if(srId == null){
			return Collections.emptyList();
		}
    	List<Integer> roleIds = getCurrentUserRoleIds();
    	RDataPermissionService dataPermissionService = SpringContext.getBean(RDataPermissionService.class);
    	
    	return dataPermissionService.buildDataExpresstions(roleIds, Integer.valueOf(srId));
	}
	
	/**
	 * 保存用户角色ID
	 * @param userId
	 */
	public void saveUserRoleIds(int userId){
		RUserRoleService userRoleService = SpringContext.getBean(RUserRoleService.class);
		List<RUserRole> userRoles = userRoleService.getUserRole(userId);
		
		if(CollectionUtils.isNotEmpty(userRoles)){
			List<Integer> roleIds = new ArrayList<Integer>(userRoles.size());
			for (RUserRole userRole : userRoles) {
				roleIds.add(userRole.getRoleId());
			}
			WebContext.getInstance().setAttr(USER_ROLE_IDS, roleIds);
		}
	}
	
	/**
	 * 获取当前用户角色ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getCurrentUserRoleIds() {
		Object roleIds = WebContext.getInstance().getAttr(USER_ROLE_IDS);
		
		if(roleIds == null){
			return Collections.emptyList();
		}
		
		return (List<Integer>)roleIds;
	}
	
	/**
	 * 刷新所有用户的系统功能
	 */
	public void refreshAllUserRightData(){
		Set<Integer> userIdSet = permissionContext.keySet();
		for (int userId : userIdSet) {
			this.refreshUserRightData(userId);
		}
	}
	
	/**
	 * 移除用户权限数据,在用户注销或session失效可以用到
	 * @param userId
	 */
	public void clearUserRightData(int userId){
		if(userId == 0){
			return;
		}
		permissionContext.remove(userId);
		userMenuContext.remove(userId);
	}
	
	/**
	 * 移除当前用户权限数据
	 */
	public void clearCurrentUserRightData(){
		RUser user = UserContext.getInstance().getUser();
		if(user != null){
			this.clearUserRightData(user.getUserId());
		}
	}
	
	/**
	 * 获取超级管理员角色ID,该ID在config.properties中设置
	 * @return
	 */
	public int getAdminRoleId() {
		if(adminRoleId == null) {
			String adminRoleIdStr = MyPropertyPlaceholderConfigurer.getProperty(ADMIN_ROLE_ID);
			adminRoleId = Integer.valueOf(adminRoleIdStr);
		}
		return adminRoleId;
	}
	
	/**
	 * 是否开启超级权限,开启的话,超级管理员角色可以访问所有内容
	 * @return
	 */
	private boolean isOpenSuperPermission() {
		//USE_SUPER_ADMIN
		if(isOpenSuperAdmin == null) {
			String useSuperAdmin = MyPropertyPlaceholderConfigurer.getProperty(USE_SUPER_ADMIN);
			isOpenSuperAdmin = "true".equals(useSuperAdmin);
		}
		return isOpenSuperAdmin;
	}
	
	/**
	 * 当前用户是否具有管理员角色
	 * @return
	 */
	private boolean isCurrentUserHaveAdminRole() {
		List<Integer> currentUserRoleIds = this.getCurrentUserRoleIds();
		int superAdminRoleId = this.getAdminRoleId();
		return currentUserRoleIds.contains(superAdminRoleId);
	}
	
	/**
	 * 当前用户是否具有超级权限
	 * @return
	 */
	public boolean isCurrentUserHaveSuperPermission() {
		return this.isOpenSuperPermission() && isCurrentUserHaveAdminRole();
	}
}
