package com.ez.framework.core.pojo;

import java.io.Serializable;

public interface IPojo extends Serializable {
	/**
	 * <code>S_UNVALIDA_VALUE</code> - integer invalid value.
	 */
	public static final int S_UNVALIDA_VALUE = -1;
	
	/**
	 * <code>S_FIELDTYPE_WRITE</code> - field type:write. 
	 */
	public static final int S_FIELDTYPE_WRITE = 1;
	
	/**
	 * <code>S_FIELDTYPE_READ</code> - field type:read. 
	 */
	public static final int S_FIELDTYPE_READ = 2;
	
	/**
	 * get ID.
	 * @return POJO id.
	 */
	public String getId() ;
	
	/**
	 * set id.
	 * @param id - POJO id.
	 */
	public void setId(String id);
	/**
	 * get field value by field name.
	 * @param fieldName - field name.
	 * @return field value
	 */
	public Object getFieldValueByName(String fieldName);
	
	/**
	 * set field value by field name.
	 * @param fieldName - field name.
	 * @param value - field value.
	 */
	public void setFieldValueByName(String fieldName,Object[] value) ;
	
	/**
	 * get all field name by type.
	 * @return all field name array.
	 */
	public String[] getFieldNames(int type);
}
