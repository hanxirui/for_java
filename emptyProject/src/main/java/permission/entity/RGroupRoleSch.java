package permission.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RGroupRoleSch extends SearchBUI {

    private Integer groupIdSch;
    private Integer roleIdSch;

    public void setGroupIdSch(Integer groupIdSch){
        this.groupIdSch = groupIdSch;
    }
    
    @ValueField(column = "group_id")
    public Integer getGroupIdSch(){
        return this.groupIdSch;
    }

    public void setRoleIdSch(Integer roleIdSch){
        this.roleIdSch = roleIdSch;
    }
    
    @ValueField(column = "role_id")
    public Integer getRoleIdSch(){
        return this.roleIdSch;
    }


}