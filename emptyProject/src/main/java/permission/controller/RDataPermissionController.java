package permission.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.EntityProcessor;
import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RDataPermission;
import permission.entity.RDataPermissionSch;
import permission.entity.RRole;
import permission.entity.RSysRes;
import permission.service.RDataPermissionService;
import permission.service.RRoleService;
import permission.service.RSysResService;

@Controller
public class RDataPermissionController extends
		CrudController<RDataPermission, RDataPermissionService> {

	@Autowired
	private RRoleService roleService;
	@Autowired
	private RSysResService resService;

	@RequestMapping("/addRDataPermission.do")
	public @ResponseBody MessageResult addRDataPermission(RDataPermission entity) {
		RSysRes res = resService.get(entity.getSrId());
		if (res == null) {
			return this.error("资源不存在");
		}
		if (CollectionUtils.isEmpty(entity.getRoleId())) {
			return this.error("请选择角色");
		}

		this.getService().saveDataPermission(entity);

		return this.success();
	}

	@RequestMapping("/listRDataPermission.do")
	public @ResponseBody GridResult listRDataPermission(RDataPermissionSch searchEntity) {
		return this.queryWithProcessor(searchEntity,
				new EntityProcessor<RDataPermission>() {
					@Override
					public void process(RDataPermission entity,
							Map<String, Object> jsonObject) {
						List<RRole> roles = roleService
								.getDataPermissionRole(entity);
						jsonObject.put("roles", roles);
					}
				});
	}

	@RequestMapping("/updateRDataPermission.do")
	public @ResponseBody MessageResult updateRDataPermission(RDataPermission entity) {
		RSysRes res = resService.get(entity.getSrId());
		if (res == null) {
			return this.error("资源不存在");
		}
		if (CollectionUtils.isEmpty(entity.getRoleId())) {
			return this.error("请选择角色");
		}

		this.getService().updateDataPermission(entity);

		return this.success();
	}

	@RequestMapping("/delRDataPermission.do")
	public @ResponseBody MessageResult delRDataPermission(RDataPermission entity) {
		this.getService().delDataPermission(entity);

		return this.success();
	}

}