package permission.controller;

import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.dao.AddGroupUserPojo;
import permission.entity.RGroupUser;
import permission.entity.RGroupUserSch;
import permission.service.RGroupUserService;

@Controller
public class RGroupUserController extends
		CrudController<RGroupUser, RGroupUserService> {

	@RequestMapping("/addGroupUser.do")
	public @ResponseBody MessageResult addGroupUser(AddGroupUserPojo addGroupUserPojo) {
		this.getService().addGroupUser(addGroupUserPojo);
		return this.success();
	}

	@RequestMapping("/listRGroupUser.do")
	public @ResponseBody GridResult listRGroupUser(RGroupUserSch searchEntity) {
		return this.query(searchEntity);
	}

	@RequestMapping("/delRGroupUser.do")
	public @ResponseBody MessageResult delRGroupUser(RGroupUser entity) {
		this.getService().delGroupUser(entity);
		return this.success();
	}

}