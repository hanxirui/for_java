package permission.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RSysResTabSch extends SearchBUI {

    private Integer idSch;
    private String tabNameSch;

    public void setIdSch(Integer idSch){
        this.idSch = idSch;
    }
    
    @ValueField(column = "id")
    public Integer getIdSch(){
        return this.idSch;
    }

    public void setTabNameSch(String tabNameSch){
        this.tabNameSch = tabNameSch;
    }
    
    @ValueField(column = "tab_name")
    public String getTabNameSch(){
        return this.tabNameSch;
    }


}