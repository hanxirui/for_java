package permission.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.AddOperateParam;
import permission.entity.RSysFunction;
import permission.entity.RSysRes;
import permission.service.RRolePermissionService;
import permission.service.RSysFunctionService;
import permission.service.RSysResService;

@Controller
public class RSysFunctionController extends
		CrudController<RSysFunction, RSysFunctionService> {
	@Autowired
	private RSysResService resService;
	@Autowired
	private RRolePermissionService permissionService;

	// 根据资源ID获取操作权限
	@RequestMapping("/listSysFunctionBySrId.do")
	public @ResponseBody GridResult listSysFunctionBySrId(
			@RequestParam(value = "srId", required = true, defaultValue = "0") int srId) {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("sr_id", srId));
		query.addSort("operate_code");
		return this.query(query);
	}

	/**
	 * 添加操作权限
	 * 
	 * @param sysFunction
	 * @return
	 */
	@RequestMapping("/addSysFunction.do")
	public @ResponseBody MessageResult addSysFunction(RSysFunction sysFunction) {
		RSysRes res = resService.get(sysFunction.getSrId());
		if (res == null) {
			return this.error("资源不存在");
		}

		if (this.getService().isExistSysFun(sysFunction.getOperateCode(),
				sysFunction.getSrId())) {
			return this.error("操作点已添加");
		}
		return this.save(sysFunction);
	}
	
	

	@RequestMapping("/setSysFunctionRole.do")
	public @ResponseBody MessageResult setSysFunctionRole(AddOperateParam addOperateParam,
			int sfId) {
		this.permissionService.setSysFunctionRole(sfId,
				addOperateParam.getRoleId());
		return this.success();
	}
	
	// 添加授权
	@RequestMapping("/addSysFunctionRole.do")
	public @ResponseBody MessageResult addSysFunctionRole(AddOperateParam addOperateParam,
			int sfId) {
		this.permissionService.setSysFunctionRole(sfId,
				addOperateParam.getRoleId());
		return this.success();
	}
	
	// 删除授权
	@RequestMapping("/delSysFunctionRole.do")
	public @ResponseBody MessageResult delSysFunctionRole(AddOperateParam addOperateParam,
			int sfId) {
		this.permissionService.setSysFunctionRole(sfId,
				addOperateParam.getRoleId());
		return this.success();
	}

	@RequestMapping("/delRSysFunction.do")
	public @ResponseBody MessageResult delRSysFunction(RSysFunction sysFunction) {
		return this.delete(sysFunction);
	}

	@RequestMapping("/listOperateUse.do")
	public @ResponseBody Map<String,Object> listOperateUse(String operateCode) {

		List<RSysFunction> list = this.getService().getByOperateCode(
				operateCode);

		Map<String, Object> map = new HashMap<String, Object>();
		boolean operateCodeUsed = CollectionUtils.isNotEmpty(list);

		if (operateCodeUsed) {
			for (RSysFunction sysFun : list) {
				RSysRes res = resService.get(sysFun.getSrId());
				sysFun.setResName(res.getResName());
			}
		}

		map.put("operateCodeUsed", operateCodeUsed);
		map.put("operateCodeUsedList", list);

		return map;
	}

}