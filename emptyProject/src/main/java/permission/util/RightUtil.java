package permission.util;

import permission.common.RMSContext;
import permission.common.UserPermission;

/**
 * 权限检查工具类
 *
 */
public class RightUtil {

	/**
	 * 根据资源ID和操作代码检查是否具有权限
	 * 
	 * @param srId SysRec表主键
	 * @param operateCode SysOperate表主键
	 * @return 返回true权限存在
	 */
	public static boolean checkOperateCode(String srId, String operateCode) {
		// 如果开启超级权限,并且当前用户具有超级管理员角色
		// 那么可以访问任何内容
		if(RMSContext.getInstance().isCurrentUserHaveSuperPermission()) {
			return true;
		}
		UserPermission userPermission = RMSContext.getInstance().getCurrentUserPermission();
		
		return userPermission.hasPermission(srId, operateCode);
	}
	
	/**
	 * 根据username和URL检查是否具有权限
	 * @param userId
	 * @param url
	 * @return 
	 */
	public static boolean checkCurrentUserUrl(int userId,String url) {
		// 如果开启超级权限,并且当前用户具有超级管理员角色
		// 那么可以访问任何内容
		if(RMSContext.getInstance().isCurrentUserHaveSuperPermission()) {
			return true;
		}
		UserPermission userPermission = RMSContext.getInstance().getUserPermission(userId);
		if(userPermission == null) {
			return false;
		}
		return userPermission.isValidUrl(url);
		//return true;
	}
}
