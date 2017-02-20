package app.entity;

import java.util.Date;

import org.durcframework.core.Sortable;
import org.durcframework.core.expression.annotation.LikeDoubleField;
import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchEasyUI;

public class OrderInfoSch extends SearchEasyUI {

    private Integer orderIdSch;
    private String cityNameSch;
    private String mobileSch;
    private String addressSch;
    private Date createDateSch;
    

    @Override
	protected void initSortMap(Sortable sort) {
		super.initSortMap(sort);
		sort.addSortField("createDate", "create_date");
	}

	public OrderInfoSch() {
		super();
		this.setPageSize(10);
	}

	public void setOrderIdSch(Integer orderIdSch){
        this.orderIdSch = orderIdSch;
    }
    
    @ValueField(column = "order_id")
    public Integer getOrderIdSch(){
        return this.orderIdSch;
    }

    public void setCityNameSch(String cityNameSch){
        this.cityNameSch = cityNameSch;
    }
    
    @LikeDoubleField(column = "city_name")
    public String getCityNameSch(){
        return this.cityNameSch;
    }

    public void setMobileSch(String mobileSch){
        this.mobileSch = mobileSch;
    }
    
    @LikeDoubleField(column = "mobile")
    public String getMobileSch(){
        return this.mobileSch;
    }

    public void setAddressSch(String addressSch){
        this.addressSch = addressSch;
    }
    
    @LikeDoubleField(column = "address")
    public String getAddressSch(){
        return this.addressSch;
    }

    public void setCreateDateSch(Date createDateSch){
        this.createDateSch = createDateSch;
    }
    
    @ValueField(column = "create_date")
    public Date getCreateDateSch(){
        return this.createDateSch;
    }


}