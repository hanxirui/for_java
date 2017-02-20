package permission.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RSysFunctionSch extends SearchBUI{

    private Integer sfIdSch;
    private Integer soIdSch;
    private Integer srIdSch;
    private String funcNameSch;

    public void setSfIdSch(Integer sfIdSch){
        this.sfIdSch = sfIdSch;
    }
    
    @ValueField(column = "sf_id")
    public Integer getSfIdSch(){
        return this.sfIdSch;
    }

    public void setSoIdSch(Integer soIdSch){
        this.soIdSch = soIdSch;
    }
    
    @ValueField(column = "so_id")
    public Integer getSoIdSch(){
        return this.soIdSch;
    }

    public void setSrIdSch(Integer srIdSch){
        this.srIdSch = srIdSch;
    }
    
    @ValueField(column = "sr_id")
    public Integer getSrIdSch(){
        return this.srIdSch;
    }

    public void setFuncNameSch(String funcNameSch){
        this.funcNameSch = funcNameSch;
    }
    
    @ValueField(column = "func_name")
    public String getFuncNameSch(){
        return this.funcNameSch;
    }


}