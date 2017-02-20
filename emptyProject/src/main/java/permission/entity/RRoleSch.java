package permission.entity;

import org.durcframework.core.expression.annotation.LikeDoubleField;
import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RRoleSch extends SearchBUI {

    private Integer roleIdSch;
    private String roleNameSch;
    private Byte roleTypeSch;

    public void setRoleIdSch(Integer roleIdSch){
        this.roleIdSch = roleIdSch;
    }
    
    @ValueField(column = "role_id")
    public Integer getRoleIdSch(){
        return this.roleIdSch;
    }

    public void setRoleNameSch(String roleNameSch){
        this.roleNameSch = roleNameSch;
    }
    
    @LikeDoubleField(column = "role_name")
    public String getRoleNameSch(){
        return this.roleNameSch;
    }

    @ValueField(column = "role_type")
	public Byte getRoleTypeSch() {
		return roleTypeSch;
	}

	public void setRoleTypeSch(Byte roleTypeSch) {
		this.roleTypeSch = roleTypeSch;
	}

}