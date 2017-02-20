package permission.controller;

import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RSysRes;
import permission.entity.RSysResSch;
import permission.service.RSysResService;

@Controller
public class RSysResController extends CrudController<RSysRes, RSysResService> {

	@RequestMapping("/addRSysRes.do")
	public @ResponseBody MessageResult addRSysRes(RSysRes entity) {
		return this.save(entity);
	}

	@RequestMapping("/listRSysRes.do")
	public @ResponseBody GridResult listRSysRes(RSysResSch searchEntity) {
		return this.queryAll(searchEntity);
	}

	@RequestMapping("/updateRSysRes.do")
	public @ResponseBody MessageResult updateRSysRes(RSysRes enity) {
		return this.update(enity);
	}

	@RequestMapping("/delRSysRes.do")
	public @ResponseBody MessageResult delRSysRes(RSysRes enity) {
		if (this.getService().hasChild(enity)) {
			return this.error(enity.getResName() + "下含有子节点,不能删除.");
		}
		return this.delete(enity);
	}

}