package permission.controller;

import java.util.List;

import org.durcframework.core.DefaultGridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RGroup;
import permission.service.RGroupService;
import permission.util.TreeUtil;

@Controller
public class RGroupController extends CrudController<RGroup, RGroupService> {

	// 获取所有菜单
	@SuppressWarnings("unchecked")
	@RequestMapping("/listAllGroup.do")
	public @ResponseBody List<RGroup> listAllGroup() {

		ExpressionQuery query = ExpressionQuery.buildQueryAll();

		DefaultGridResult resultGrid = (DefaultGridResult) this.queryAll(query);
		List<RGroup> rows = (List<RGroup>) resultGrid.getRows();

		List<RGroup> list = TreeUtil.buildGroupData(rows);

		return list;
	}

	@RequestMapping("/addRGroup.do")
	public @ResponseBody MessageResult addRGroup(RGroup entity) {
		return this.save(entity);
	}

	@RequestMapping("/updateRGroup.do")
	public @ResponseBody MessageResult updateRGroup(RGroup entity) {
		return this.update(entity);
	}

	@RequestMapping("/delRGroup.do")
	public @ResponseBody MessageResult delRGroup(RGroup entity) {
		if (this.getService().hasChild(entity)) {
			return this.error(entity.getGroupName() + "下含有子节点,不能删除.");
		}
		return this.delete(entity);
	}

}