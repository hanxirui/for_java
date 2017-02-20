package permission.entity;


public class RDataObj {
    private int doId;
    private int dtId;
    private String groupName;

    public void setDoId(int doId){
        this.doId = doId;
    }

    public int getDoId(){
        return this.doId;
    }

    public void setDtId(int dtId){
        this.dtId = dtId;
    }

    public int getDtId(){
        return this.dtId;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public String getGroupName(){
        return this.groupName;
    }

}