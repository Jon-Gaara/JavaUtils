package com.jon.poi;

public class User {
	private String name;
	private String age;
	private String heigh;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHeigh() {
		return heigh;
	}

	public void setHeigh(String heigh) {
		this.heigh = heigh;
	}

	@Override
	public String toString() {
	    return "User [name=" + name + ", age=" + age + ", heigh=" + heigh + "]";
	}
}
