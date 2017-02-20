package permission.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RGroupSch extends SearchBUI{

    private Integer groupIdSch;
    private String groupNameSch;
    private Integer parentIdSch;

    public void setGroupIdSch(Integer groupIdSch){
        this.groupIdSch = groupIdSch;
    }
    
    @ValueField(column = "group_id")
    public Integer getGroupIdSch(){
        return this.groupIdSch;
    }

    public void setGroupNameSch(String groupNameSch){
        this.groupNameSch = groupNameSch;
    }
    
    @ValueField(column = "group_name")
    public String getGroupNameSch(){
        return this.groupNameSch;
    }

    public void setParentIdSch(Integer parentIdSch){
        this.parentIdSch = parentIdSch;
    }
    
    @ValueField(column = "parent_id")
    public Integer getParentIdSch(){
        return this.parentIdSch;
    }


}