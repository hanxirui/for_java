package permission.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.durcframework.core.controller.SearchController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.common.RMSContext;
import permission.common.UserMenu;
import permission.entity.RSysFunction;
import permission.entity.RSysRes;
import permission.entity.RSysResTab;
import permission.service.RSysFunctionService;
import permission.service.RSysResService;
import permission.service.RSysResTabService;

@Controller
public class MenuController extends SearchController<RSysRes, RSysResService>{
	
	@Autowired
	private RSysFunctionService sysFunctionService;
	@Autowired
	private RSysResTabService sysResTabService;
	
	/**
	 * 加载用户菜单
	 * @return
	 */
	@RequestMapping("listUserMenu_backuser.do")
	public @ResponseBody List<UserMenu> listUserMenu(){
		List<UserMenu> menuList = RMSContext.getInstance().getUserMenu();
		return menuList;
	}
	
    // 获取所有菜单
	@RequestMapping("/listAllMenu.do")
    public @ResponseBody List<RSysRes> listAllMenu() {
    	List<RSysRes> rows = this.getService().getAllRSysRes();
    	List<RSysRes> treeData = buildTreeData(rows);
		return treeData;
    }
	
	 // 获取所有菜单
	@RequestMapping("/listRoleMenu.do")
    public @ResponseBody List<RSysRes> listRoleMenu(int parentId) {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		if(parentId > 0) {
			query.add(new ValueExpression("parent_id", parentId));
		}
    	List<RSysRes> rows = this.getService().find(query);
    	
    	for (RSysRes res : rows) {
    		List<RSysFunction> sysFuns = sysFunctionService.getBySySResId(res.getSrId());
			res.setSysFuns(sysFuns);
		}
    	
    	List<RSysRes> treeData = buildTreeData(rows,parentId);
    	
    	return treeData;
    }
	
	// 根据tab_id获取菜单
	@RequestMapping("/listMenuByTabId.do")
	public @ResponseBody List<RSysRes> listMenuByTabId(int tabId) {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("t.tab_id", tabId));
    	List<RSysRes> rows = this.getService().find(query);
    	
    	for (RSysRes res : rows) {
    		List<RSysFunction> sysFuns = sysFunctionService.getBySySResId(res.getSrId());
			res.setSysFuns(sysFuns);
		}
    	
    	List<RSysRes> treeData = buildTreeData(rows);
    	
    	return treeData;
	}
	
	// 获取顶级菜单
	@RequestMapping("/listTopMenu.do")
	public @ResponseBody List<RSysResTab> listTopMenu() {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.addSort("id");
		
		List<RSysResTab> rows = sysResTabService.find(query);
		
    	return rows;
	}
	
	@RequestMapping("/addTopMenu.do")
	public @ResponseBody Object addTopMenu(String tabName) {
		if(StringUtils.isNotBlank(tabName)) {
			RSysResTab tab = new RSysResTab();
			tab.setTabName(tabName);
			sysResTabService.save(tab);
			return tab;
		}
		return this.error("请输入正确选项");
	}
	
	
    
    /**
	 * 构建树形菜单
	 * @param list
	 * @return
	 */
	public static List<RSysRes> buildTreeData(List<RSysRes> list,int parentId) {

		List<RSysRes> menu = new ArrayList<RSysRes>();

		resolveMenuTree(list, parentId, menu);

		return menu;
	}
	
	public static List<RSysRes> buildTreeData(List<RSysRes> list) {
		return buildTreeData(list, 0);
	}
	
	public static int resolveMenuTree(List<RSysRes> menus, int parentMenuId,
			List<RSysRes> nodes) {

		int count = 0;
		for (RSysRes menu : menus) {
			if (menu.getParentId() == parentMenuId) {
				RSysRes node = new RSysRes();

				nodes.add(node);
				node.setSrId(menu.getSrId());
				node.setResName(menu.getText());
				node.setUrl(menu.getUrl());
				node.setParentId(menu.getParentId());
				node.setChildren(new ArrayList<RSysRes>());
				node.setSysFuns(menu.getSysFuns());

				resolveMenuTree(menus, menu.getId(), node.getChildren());
				count++;
			}
		}
		return count;
	}
	

	
}
