package permission.entity;


public class RDataType {
    private int dtId;
    private String typeName;

    public void setDtId(int dtId){
        this.dtId = dtId;
    }

    public int getDtId(){
        return this.dtId;
    }

    public void setTypeName(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return this.typeName;
    }

}