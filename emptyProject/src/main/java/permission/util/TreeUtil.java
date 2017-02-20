package permission.util;

import java.util.ArrayList;
import java.util.List;

import permission.common.UserMenu;
import permission.entity.RGroup;

public class TreeUtil {

	/**
	 * 构建树形菜单
	 * @param list
	 * @return
	 */
	public static List<UserMenu> buildTreeData(List<UserMenu> list) {

		List<UserMenu> menu = new ArrayList<UserMenu>();

		resolveMenuTree(list, 0, menu);

		return menu;
	}
	
	public static List<RGroup> buildGroupData(List<RGroup> list) {
		
		List<RGroup> menu = new ArrayList<RGroup>();
		
		resolveGroupTree(list, 0, menu);
		
		return menu;
	}
	

	public static int resolveGroupTree(List<RGroup> menus, int parentMenuId,
			List<RGroup> nodes) {
		
		int count = 0;
		for (RGroup menu : menus) {
			if (menu.getParentId() == parentMenuId) {
				RGroup node = new RGroup();
				
				nodes.add(node);
				node.setGroupId(menu.getGroupId());
				node.setGroupName(menu.getGroupName());
				node.setParentId(menu.getParentId());
				node.setChildren(new ArrayList<RGroup>());
				node.setRoles(menu.getRoles());
				
				resolveGroupTree(menus, menu.getGroupId(), node.getChildren());
				count++;
			}
		}
		return count;
	}
	
	public static int resolveMenuTree(List<UserMenu> menus, int parentMenuId,
			List<UserMenu> nodes) {

		int count = 0;
		for (UserMenu menu : menus) {
			if (menu.getParentId() == parentMenuId) {
				UserMenu node = new UserMenu();

				nodes.add(node);
				node.setId(menu.getId());
				node.setText(menu.getText());
				node.setUrl(menu.getUrl());
				node.setParentId(menu.getParentId());
				node.setChildren(new ArrayList<UserMenu>());
				node.setTabId(menu.getTabId());
				node.setTabName(menu.getTabName());

				resolveMenuTree(menus, menu.getId(), node.getChildren());
				count++;
			}
		}
		return count;
	}
}
