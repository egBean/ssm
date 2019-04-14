package cn.eg.entity;

public class People {
	private String name;
	private int id;
	private int age;
	private long version;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "People [name=" + name + ", id=" + id + ", age=" + age + ", version=" + version + "]";
	}
	
	
}
