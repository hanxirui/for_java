package com.hxr.javatone.xstream;

import java.util.ArrayList;
import java.util.List;

import com.hxr.javatone.xstream.PersonBean.Animal;
import com.hxr.javatone.xstream.PersonBean.Friends;
import com.hxr.javatone.xstream.PersonBean.Pets;
import com.hxr.javatone.xstream.PersonBean.PhoneNumber;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class PersonTest {
	public static void main(String[] args) {

		// toXml
		// String xmlStr=new PersonTest().toXml();

		// toBean
		// PersonBean per=new PersonTest().toBean();
		String xmlStr = "<person>" + "<firstName>lei</firstName>" + "<lastName>sun</lastName>" + "<telphone>"
				+ "<code>137280</code>" + "<number>137280968</number>" + "</telphone>" + "<faxphone>"
				+ "<code>20</code>" + "<number>020221327</number>" + "</faxphone>" + "<friends>" + "<name>A1</name>"
				+ "<name>A2</name>" + "<name>A3</name>" + "</friends>" + "<pets>" + "<pet>" + "<name>doly</name>"
				+ "<age>2</age>" + "</pet>" + "<pet>" + "<name>Ketty</name>" + "<age>2</age>" + "</pet>" + "</pets>"
				+ "</person>";
		PersonBean person = XmlUtil.toBean(xmlStr, PersonBean.class);
		System.out.println("person=firstname==" + person.getFirstName());
		System.out.println("person==Friends==name1==" + person.getFriend().getName().get(0));
		System.out.println("person==Pets==name2==" + person.getPet().getAnimalList().get(1).getName());

	}

	public String toXml() {
		PersonBean per = new PersonBean();
		per.setFirstName("lei");
		per.setLastName("sun");

		PhoneNumber tel = new PhoneNumber();
		tel.setCode(137280);
		tel.setNumber("137280968");

		PhoneNumber fax = new PhoneNumber();
		fax.setCode(20);
		fax.setNumber("020221327");
		per.setTel(tel);
		per.setFax(fax);

		List<String> friendList = new ArrayList<String>();
		friendList.add("A1");
		friendList.add("A2");
		friendList.add("A3");
		Friends friend1 = new Friends();
		friend1.setName(friendList);
		per.setFriend(friend1);

		Animal dog = new Animal("doly", 2);
		Animal cat = new Animal("Ketty", 2);
		List<Animal> petList = new ArrayList<Animal>();
		petList.add(dog);
		petList.add(cat);
		Pets pet = new Pets();
		pet.setAnimalList(petList);
		per.setPet(pet);

		XStream xstream = new XStream(new DomDriver("utf-8"));
		xstream.processAnnotations(PersonBean.class);
		xstream.aliasSystemAttribute(null, "class");
		String xmlString = xstream.toXML(per);
		System.out.println("xml===" + xmlString);
		return xmlString;
	}

	/**
	 * toBean
	 * 
	 * @Title: toBean
	 * @Description: TODO
	 * @return
	 * @return PersonBean
	 */
	public PersonBean toBean() {
		String xmlStr = "<person>" + "<firstName>lei</firstName>" + "<lastName>sun</lastName>" + "<telphone>"
				+ "<code>137280</code>" + "<number>137280968</number>" + "</telphone>" + "<faxphone>"
				+ "<code>20</code>" + "<number>020221327</number>" + "</faxphone>" + "<friends>" + "<name>A1</name>"
				+ "<name>A2</name>" + "<name>A3</name>" + "</friends>" + "<pets>" + "<pet>" + "<name>doly</name>"
				+ "<age>2</age>" + "</pet>" + "<pet>" + "<name>Ketty</name>" + "<age>2</age>" + "</pet>" + "</pets>"
				+ "</person>";

		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(PersonBean.class);
		PersonBean person = (PersonBean) xstream.fromXML(xmlStr);
		System.out.println("person=firstname==" + person.getFirstName());
		System.out.println("person==Friends==name1==" + person.getFriend().getName().get(0));
		System.out.println("person==Pets==name==" + person.getPet().getAnimalList().get(1).getName());
		return person;
	}

	public static class XmlUtil {

		public static String toXml(Object obj) {
			XStream xstream = new XStream();
			xstream.processAnnotations(obj.getClass());
			return xstream.toXML(obj);
		}

		public static <T> T toBean(String xmlStr, Class<T> cls) {
			XStream xstream = new XStream(new DomDriver());
			xstream.processAnnotations(cls);
			T obj = (T) xstream.fromXML(xmlStr);
			return obj;
		}

	}

}
