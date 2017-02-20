package permission.entity;

import org.durcframework.core.expression.annotation.LikeDoubleField;
import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RGroupUserSch extends SearchBUI{

    private Integer groupIdSch;
    private String usernameSch;

    public void setGroupIdSch(Integer groupIdSch){
        this.groupIdSch = groupIdSch;
    }
    
    @ValueField(column = "group_id")
    public Integer getGroupIdSch(){
        return this.groupIdSch;
    }

    public void setUsernameSch(String usernameSch){
        this.usernameSch = usernameSch;
    }
    
    @LikeDoubleField(column = "username")
    public String getUsernameSch(){
        return this.usernameSch;
    }


}