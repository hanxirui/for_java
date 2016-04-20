package app;

import java.util.Set;

import javax.validation.ConstraintViolation;

import junit.framework.TestCase;

import org.durcframework.core.ValidateHolder;
import org.durcframework.core.util.ValidateUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import app.entity.Student;

public class HibernateValidateTest extends TestCase {

	private Student stu;

	@BeforeClass
	public void setUp() {
		stu = new Student();
		stu.setId(1);
		stu.setName("");
		stu.setStuNo("1000003210000032100000321000003210000032");
	}

	private Object getObj() {
		return stu;
	}

	@Test
	public void testValidate() {
		ValidateHolder holder = ValidateUtil.validate(getObj());
		Set<ConstraintViolation<Object>> set = holder.buildConstraintViolations();
		for (ConstraintViolation<Object> c : set) {
			Student s = (Student)c.getRootBean();
			System.out.println(c.getPropertyPath().toString()+"--" + c.getMessage());
		}

	}

}
