package com.hxr.javatone.xstream;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("person")
public class PersonBean {
	@XStreamAlias("firstName")
	private String firstName;
	@XStreamAlias("lastName")
	private String lastName;

	@XStreamAlias("telphone")
	private PhoneNumber tel;
	@XStreamAlias("faxphone")
	private PhoneNumber fax;

	@XStreamAlias("friends")
	private Friends friend;

	@XStreamAlias("pets")
	private Pets pet;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public PhoneNumber getTel() {
		return tel;
	}

	public void setTel(PhoneNumber tel) {
		this.tel = tel;
	}

	public PhoneNumber getFax() {
		return fax;
	}

	public void setFax(PhoneNumber fax) {
		this.fax = fax;
	}

	public Friends getFriend() {
		return friend;
	}

	public void setFriend(Friends friend) {
		this.friend = friend;
	}

	public Pets getPet() {
		return pet;
	}

	public void setPet(Pets pet) {
		this.pet = pet;
	}

	@XStreamAlias("phoneNumber")
	public static class PhoneNumber {
		@XStreamAlias("code")
		private int code;
		@XStreamAlias("number")
		private String number;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

	}

	public static class Friends {
		private List<String> name;

		public List<String> getName() {
			return name;
		}

		public void setName(List<String> name) {
			this.name = name;
		}
	}

	public static class Animal {
		@XStreamAlias("name")
		private String name;
		@XStreamAlias("age")
		private int age;

		public Animal(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

	public static class Pets {
		@XStreamImplicit(itemFieldName = "pet")
		private List<Animal> animalList;

		public List<Animal> getAnimalList() {
			return animalList;
		}

		public void setAnimalList(List<Animal> animalList) {
			this.animalList = animalList;
		}

	}
}
