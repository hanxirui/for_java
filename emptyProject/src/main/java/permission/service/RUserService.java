package permission.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.QBC;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.common.RMSContext;
import permission.dao.RUserDao;
import permission.entity.RRole;
import permission.entity.RUser;
import permission.util.PasswordUtil;

@Service
public class RUserService extends CrudService<RUser, RUserDao> {

	@Autowired
	private RRoleService roleService;
	
	public RUser getByUsername(String username) {
		QBC<RUser> qbc = QBC.create(this.getDao());
		return qbc.eq("username", username).listOne();
	}
	
	/**
	 * 重置用户密码
	 * @param user
	 * @return 返回明文密码
	 */
	public String resetUserPassword(RUser user){
		
		String password = createNewPswd();
		
		String hash = PasswordUtil.createStorePswd(password);
		
		RUser record = this.get(user);
		
		record.setPassword(hash);
		
		this.update(record);
		
		return password;
	}
	
	public void updateUserPassword(RUser user,String md5Pswd){    	
		user.setPassword(PasswordUtil.createHash(md5Pswd));    	
    	update(user);
	}
	
	public void optState(int userId,byte destState) {
		List<RRole> userRoles = roleService.getUserRole(userId);
		if(CollectionUtils.isNotEmpty(userRoles)){
			int adminRoleId = RMSContext.getInstance().getAdminRoleId();
			for (RRole role : userRoles) {
				if(adminRoleId == role.getRoleId()) {
					throw new RuntimeException("无法禁用超级管理员");
				}
			}
		}
		
		RUser user = this.get(userId);
		user.setState(destState);
		this.update(user);
	}
	
	/**
	 * 生成随机密码,由三个小写字母+三个数字组成
	 * @return
	 */
	public static String createNewPswd(){
		StringBuilder pswd = new StringBuilder();
		
		// 随机三个小写英文字母
		for (int i = 0; i < 3; i++) {
			// ascii码 97~122
			char ascii = (char) ((int)(Math.random() * 26) + 97);
			pswd.append(ascii);
		}
		// 随机三个1~9的数
		for (int i = 0; i < 3; i++) {
			int num = (int)(Math.random() * 9) + 1;
			pswd.append(num);
		}
		
		return pswd.toString();
	}
	
	/**
	 * 获取所有用户
	 * @return
	 */
	public List<RUser> getAllUser() {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		return this.find(query);
	}
	
	public static void main(String[] args) {
		System.out.println(createNewPswd());
	}
	
}
