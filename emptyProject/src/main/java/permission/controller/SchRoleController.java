package permission.controller;

import java.util.List;

import org.durcframework.core.controller.SearchController;
import org.durcframework.core.expression.QBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RGroup;
import permission.entity.RRole;
import permission.service.RGroupService;
import permission.service.RRoleService;
import permission.util.TreeUtil;

@Controller
public class SchRoleController extends SearchController<RGroup, RGroupService> {

	@Autowired
	private RRoleService roleService;

	@RequestMapping("/listTreeRole.do")
	public @ResponseBody List<RGroup> listRRole() {
		List<RGroup> rows = QBC.create(this.getService().getDao()).listAll();

		List<RRole> roles = null;
		for (RGroup group : rows) {
			roles = roleService.getRolesByGroupId(group.getGroupId());
			group.setRoles(roles);
		}
		
		List<RGroup> groupData = TreeUtil.buildGroupData(rows);
		
		return groupData;
	}
	
}
