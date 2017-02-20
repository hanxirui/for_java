package app.entity;

import java.util.Date;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.LikeDoubleField;
import org.durcframework.core.expression.annotation.ListField;
import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.util.DateUtil;

/**
 * 负责接收页面查询参数的类
 * 2012-09-21
 */
public class SearchStudentEntity extends SearchEntity{

	private Integer schId;
	private String schName;
	private Integer schPoliticsStatus;
	private String schNationality;
	private String schStuNo;
	private Integer[] schGender;
	private Integer schDepartment;
	private String schAddress;
	private String schMobile;
	private Date schRegistDateStart;
	private Date schRegistDateEnd;
	private Date schBirthday;

	public void setSchId(Integer schId) {
		this.schId = schId;
	}

	@ValueField(column = "id")
	public Integer getSchId() {
		return this.schId;
	}

	public void setSchName(String schName) {
		this.schName = schName;
	}

	@LikeDoubleField(column="name")
	public String getSchName() {
		return this.schName;
	}

	public void setSchPoliticsStatus(Integer schPoliticsStatus) {
		this.schPoliticsStatus = schPoliticsStatus;
	}

	@ValueField(column = "politicsStatus")
	public Integer getSchPoliticsStatus() {
		return this.schPoliticsStatus;
	}

	public void setSchNationality(String schNationality) {
		this.schNationality = schNationality;
	}

	@ValueField(column = "nationality")
	public String getSchNationality() {
		return this.schNationality;
	}

	public void setSchStuNo(String schStuNo) {
		this.schStuNo = schStuNo;
	}

	@ValueField(column = "stu_No")
	public String getSchStuNo() {
		return this.schStuNo;
	}

	@ListField(column = "gender")
	public Integer[] getSchGender() {
		return schGender;
	}

	public void setSchGender(Integer[] schGender) {
		this.schGender = schGender;
	}

	public void setSchDepartment(Integer schDepartment) {
		this.schDepartment = schDepartment;
	}

	@ValueField(column = "department")
	public Integer getSchDepartment() {
		return this.schDepartment;
	}

	public void setSchAddress(String schAddress) {
		this.schAddress = schAddress;
	}

	@ValueField(column = "address")
	public String getSchAddress() {
		return this.schAddress;
	}

	public void setSchMobile(String schMobile) {
		this.schMobile = schMobile;
	}

	@ValueField(column = "mobile")
	public String getSchMobile() {
		return this.schMobile;
	}
	
	@ValueField(column="regist_date",equal=">=")
	public Date getSchRegistDateStart() {
		return schRegistDateStart;
	}

	public void setSchRegistDateStart(Date schRegistDateStart) {
		this.schRegistDateStart = schRegistDateStart;
	}

	@ValueField(column="regist_date",equal="<")
	public Date getSchRegistDateEnd() {
		if(schRegistDateEnd != null){
			return DateUtil.getDateAfterDay(schRegistDateEnd, 1);
		}
		return schRegistDateEnd;
	}

	public void setSchRegistDateEnd(Date schRegistDateEnd) {
		this.schRegistDateEnd = schRegistDateEnd;
	}

	public void setSchBirthday(Date schBirthday) {
		this.schBirthday = schBirthday;
	}

	@ValueField(column = "birthday")
	public Date getSchBirthday() {
		return this.schBirthday;
	}

}
