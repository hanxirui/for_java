package com.hxr.javatone.util;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private String AREA_ID; // 主键ID
    private String AREA_NAME;   // 用来显示的名称
    private String PARENT_ID;   // 父ID  参照AREA_ID
    private Tree parentObj; // 父节点对象
    private final List<Tree> childrenList = new ArrayList<Tree>();    // 子节点
    /**
     * @return aREA_ID - {return content description}
     */
    public String getAREA_ID() {
        return AREA_ID;
    }
    /**
     * @param aREA_ID - {parameter description}.
     */
    public void setAREA_ID(final String aREA_ID) {
        AREA_ID = aREA_ID;
    }
    /**
     * @return aREA_NAME - {return content description}
     */
    public String getAREA_NAME() {
        return AREA_NAME;
    }
    /**
     * @param aREA_NAME - {parameter description}.
     */
    public void setAREA_NAME(final String aREA_NAME) {
        AREA_NAME = aREA_NAME;
    }
    /**
     * @return pARENT_ID - {return content description}
     */
    public String getPARENT_ID() {
        return PARENT_ID;
    }
    /**
     * @param pARENT_ID - {parameter description}.
     */
    public void setPARENT_ID(final String pARENT_ID) {
        PARENT_ID = pARENT_ID;
    }
    /**
     * @return parentObj - {return content description}
     */
    public Tree getParentObj() {
        return parentObj;
    }
    /**
     * @param parentObj - {parameter description}.
     */
    public void setParentObj(final Tree parentObj) {
        this.parentObj = parentObj;
    }
    /**
     * @return childrenList - {return content description}
     */
    public List<Tree> getChildrenList() {
        return childrenList;
    }
    public void setChildrenList(final List<Tree> childrenHereList) {
        if(childrenHereList!=null){
            this.childrenList.addAll(childrenHereList);
        }
    
        
    }
    
}
