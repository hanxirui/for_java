package permission.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import permission.dao.RDataPermissionRoleDao;
import permission.entity.RDataPermissionRole;

@Service
public class RDataPermissionRoleService extends CrudService<RDataPermissionRole, RDataPermissionRoleDao> {

	
	public void saveDataPermissionRole(int dpId,List<Integer> roleIds) {
		if(dpId > 0 && CollectionUtils.isNotEmpty(roleIds)){
			
			for (Integer roleId : roleIds) {
				this.save(new RDataPermissionRole(dpId,roleId));
			}
		}
	}
	
	public void delByDpId(int dpId) {
		RDataPermissionRole pojo = new RDataPermissionRole();
		pojo.setDpId(dpId);
		this.del(pojo);
	}
}