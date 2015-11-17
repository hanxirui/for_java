package com.ez.framework.core.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ez.framework.core.util.PojoClassInfo;

public abstract class AbsPojo implements IPojo {


	/**
	 * <code>m_id</code> - ID.
	 */
	protected String m_id;

	/**
	 * Constructor.
	 */
	public AbsPojo() {
	}

	/**
	 * get UUID key.
	 * 
	 * @return UUID key.
	 */
	protected String getUUIDKey() {

		return java.util.UUID.randomUUID().toString();

	}

	public String getId() {
		if (this.m_id == null || this.m_id.length() == 0) {
			this.m_id = getUUIDKey();
		}
		return this.m_id;
	}
	public void setId(String id) {
		this.m_id = id;

	}
	

	public Object getFieldValueByName(String fieldName) {

		if (null != fieldName && fieldName.length() > 0) {

			PojoClassInfo t_clazzInfo = PojoClassInfo.getInstance(this.getClass());

			if (null != t_clazzInfo) {

				Method t_method = t_clazzInfo.getGetter(fieldName);

				if (null != t_method) {
					try {
						return t_method.invoke(this, new Object[] {});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
		return null;
	}

	public void setFieldValueByName(String fieldName, Object[] value) {

		if (null != fieldName && fieldName.length() > 0) {

			PojoClassInfo t_clazzInfo = PojoClassInfo.getInstance(this.getClass());

			if (null != t_clazzInfo) {

				Method t_method = t_clazzInfo.getSetter(fieldName);

				if (null != t_method) {
					try {
						t_method.invoke(this, value);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
	}

	public String[] getFieldNames(int type) {
		PojoClassInfo t_clazzInfo = PojoClassInfo.getInstance(this.getClass());

		if (null != t_clazzInfo) {

			if (type == IPojo.S_FIELDTYPE_WRITE) {
				return t_clazzInfo.getWriteablePropertyNames();
			} else if (type == IPojo.S_FIELDTYPE_READ) {
				return t_clazzInfo.getReadablePropertyNames();
			}
		}

		return null;
	}

	public int hashCode() {
		return new HashCodeBuilder(-1137714557, 409667233).appendSuper(super.hashCode()).append(this.m_id).toHashCode();
	}

	public boolean equals(Object object) {
		if (!(object instanceof AbsPojo)) {
			return false;
		}
		AbsPojo rhs = (AbsPojo) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.m_id, rhs.m_id).isEquals();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	

	public Object deepClone() throws IOException, ClassNotFoundException {

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(this);
		oo.flush();
		oo.close();

		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		return oi.readObject();
	}

}
