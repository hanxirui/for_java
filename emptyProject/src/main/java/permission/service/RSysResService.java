package permission.service;

import java.util.ArrayList;
import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.QBC;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.common.RMSContext;
import permission.common.UserMenu;
import permission.dao.RSysResDao;
import permission.entity.RSysRes;
import permission.entity.RUserRole;
import permission.util.TreeUtil;

@Service
public class RSysResService extends CrudService<RSysRes, RSysResDao> {
	@Autowired
	private RSysFunctionService functionService;
	@Autowired
	private RUserRoleService userRoleService; 
	
	/**
	 * 判断是否有子节点
	 * @param sysRes
	 * @return
	 */
	public boolean hasChild(RSysRes sysRes){
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("parent_id", sysRes.getSrId()));
		int count = this.findTotalCount(query);
		
		return count > 0;
	}
	
	/**
	 * 根据用户名获取菜单
	 * @param userId 用户ID
	 * @return 返回用户菜单列表
	 */
	public List<UserMenu> getUserMenu(int userId){
		List<RUserRole> roles = userRoleService.getUserRole(userId);
		boolean isSuperAdmin = false;
		for (RUserRole userRole : roles) {
			if(RMSContext.getInstance().getAdminRoleId() == userRole.getRoleId()) {
				isSuperAdmin = true;
				break;
			}
		}
		
		List<RSysRes> list = null;
		
		if(isSuperAdmin) {
			list = this.getAllRSysRes();
		}else{
			list = this.getDao().findUserMenu(userId);
		}
		
		List<UserMenu> menus = new ArrayList<UserMenu>(list.size());
		
		UserMenu menu = null;
		for (RSysRes rSysRes : list) {
			menu = new UserMenu();
			menu.setMenuId(rSysRes.getId());
			menu.setText(rSysRes.getText());
			menu.setParentId(rSysRes.getParentId());
			menu.setUrl(rSysRes.getUrl());
			menu.setTabId(rSysRes.getTabId());
			menu.setTabName(rSysRes.getTabName());
			menus.add(menu);
		}
		
		menus = TreeUtil.buildTreeData(menus);
		
		return menus;
	}
	
	/**
	 * 全部菜单
	 * @return
	 */
	public List<RSysRes> getAllRSysRes(){
		return QBC.create(this.getDao()).listAll();
	}
	
	
	/**
	 * 删除资源
	 * 首先删除对应的系统功能,在删除自身
	 */
	@Override
	public int del(RSysRes entity) {
		functionService.delBySrId(entity.getSrId());
		this.getDao().del(entity);
		
		return 0;
	}
	
}
